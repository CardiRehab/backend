package com.healthcare.herplatform.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.healthcare.herplatform.entity.ResourceCategory;
import com.healthcare.herplatform.entity.ResourceListing;
import com.healthcare.herplatform.services.ResourceListingService;

/**
 * Read endpoints are open to all authenticated app roles (Patients, CR Specialists, LHCPs).
 * Write/management endpoints are limited to ADMIN only — the client's "Board of Directors"
 * curator login. CR Specialists and Patients are platform users and cannot manage listings.
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceListingController {

	private final ResourceListingService service;

	public ResourceListingController(ResourceListingService service) {
		super();
		this.service = service;
	}

	// ---- Patient-facing reads ----

	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("")
	public ResponseEntity<List<ResourceListing>> list(
			@RequestParam(value = "category", required = false) ResourceCategory category,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "q", required = false) String q) {
		return ResponseEntity.ok(service.listActive(category, city, q));
	}

	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("/cities")
	public ResponseEntity<List<String>> cities(
			@RequestParam(value = "category", required = false) ResourceCategory category) {
		return ResponseEntity.ok(service.citiesFor(category));
	}

	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<ResourceListing> getById(@PathVariable Long id) {
		ResourceListing listing = service.getById(id);
		return listing != null ? ResponseEntity.ok(listing) : ResponseEntity.notFound().build();
	}

	// ---- Admin (Board of Directors, ADMIN only) ----

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<List<ResourceListing>> listForAdmin(
			@RequestParam(value = "category", required = false) ResourceCategory category) {
		return ResponseEntity.ok(service.listForAdmin(category));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("")
	public ResponseEntity<ResourceListing> create(@RequestBody ResourceListing listing, Principal principal) {
		String createdBy = principal != null ? principal.getName() : null;
		return new ResponseEntity<>(service.create(listing, createdBy), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ResourceListing> update(@PathVariable Long id, @RequestBody ResourceListing listing) {
		ResourceListing updated = service.update(id, listing);
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{id}/active")
	public ResponseEntity<ResourceListing> setActive(@PathVariable Long id,
			@RequestParam("value") boolean value) {
		ResourceListing updated = service.setActive(id, value);
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
