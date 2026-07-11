package com.healthcare.herplatform.services;

import java.util.List;

import com.healthcare.herplatform.entity.ResourceCategory;
import com.healthcare.herplatform.entity.ResourceListing;

public interface ResourceListingService {

	// Patient-facing reads
	List<ResourceListing> listActive(ResourceCategory category, String city, String q);

	List<String> citiesFor(ResourceCategory category);

	ResourceListing getById(Long id);

	// Admin
	List<ResourceListing> listForAdmin(ResourceCategory category);

	ResourceListing create(ResourceListing listing, String createdBy);

	ResourceListing update(Long id, ResourceListing listing);

	ResourceListing setActive(Long id, boolean active);

	void delete(Long id);
}
