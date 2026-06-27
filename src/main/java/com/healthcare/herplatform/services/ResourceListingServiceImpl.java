package com.healthcare.herplatform.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.healthcare.herplatform.entity.ResourceCategory;
import com.healthcare.herplatform.entity.ResourceListing;
import com.healthcare.herplatform.repository.ResourceListingRepository;

@Service
public class ResourceListingServiceImpl implements ResourceListingService {

	private final ResourceListingRepository repo;

	public ResourceListingServiceImpl(ResourceListingRepository repo) {
		super();
		this.repo = repo;
	}

	private static String trimToNull(String s) {
		return StringUtils.hasText(s) ? s.trim() : null;
	}

	@Override
	public List<ResourceListing> listActive(ResourceCategory category, String city, String q) {
		return repo.search(category, trimToNull(city), trimToNull(q));
	}

	@Override
	public List<String> citiesFor(ResourceCategory category) {
		return repo.findDistinctCities(category);
	}

	@Override
	public ResourceListing getById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public List<ResourceListing> listForAdmin(ResourceCategory category) {
		return repo.findForAdmin(category);
	}

	@Override
	public ResourceListing create(ResourceListing listing, String createdBy) {
		listing.setId(null);
		listing.setCreatedBy(createdBy);
		return repo.save(listing);
	}

	@Override
	public ResourceListing update(Long id, ResourceListing incoming) {
		Optional<ResourceListing> existingOpt = repo.findById(id);
		if (!existingOpt.isPresent()) {
			return null;
		}
		ResourceListing e = existingOpt.get();

		// Common
		e.setCategory(incoming.getCategory());
		e.setName(incoming.getName());
		e.setDescription(incoming.getDescription());
		e.setImageUrl(incoming.getImageUrl());
		e.setCity(incoming.getCity());
		e.setState(incoming.getState());
		e.setPincode(incoming.getPincode());
		e.setLatitude(incoming.getLatitude());
		e.setLongitude(incoming.getLongitude());
		e.setPhone(incoming.getPhone());
		e.setEmail(incoming.getEmail());
		e.setWebsiteUrl(incoming.getWebsiteUrl());
		e.setListingType(incoming.getListingType());
		e.setVerified(incoming.isVerified());
		e.setActive(incoming.isActive());
		e.setDisplayOrder(incoming.getDisplayOrder());

		// Device-specific
		e.setBrand(incoming.getBrand());
		e.setDeviceType(incoming.getDeviceType());
		e.setBuyUrl(incoming.getBuyUrl());
		e.setRentUrl(incoming.getRentUrl());
		e.setPriceInfo(incoming.getPriceInfo());
		e.setTested(incoming.isTested());

		// Provider-specific
		e.setSpecialization(incoming.getSpecialization());
		e.setQualifications(incoming.getQualifications());
		e.setAffiliation(incoming.getAffiliation());
		e.setConsultationInfo(incoming.getConsultationInfo());

		return repo.save(e);
	}

	@Override
	public ResourceListing setActive(Long id, boolean active) {
		Optional<ResourceListing> existingOpt = repo.findById(id);
		if (!existingOpt.isPresent()) {
			return null;
		}
		ResourceListing e = existingOpt.get();
		e.setActive(active);
		return repo.save(e);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}
}
