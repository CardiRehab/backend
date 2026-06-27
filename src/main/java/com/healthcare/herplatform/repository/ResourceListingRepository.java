package com.healthcare.herplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.herplatform.entity.ResourceCategory;
import com.healthcare.herplatform.entity.ResourceListing;

@Repository
public interface ResourceListingRepository extends JpaRepository<ResourceListing, Long> {

	// Patient-facing: only active listings, optional category / city / keyword filters.
	@Query("SELECT r FROM ResourceListing r WHERE r.active = true "
			+ "AND (:category IS NULL OR r.category = :category) "
			+ "AND (:city IS NULL OR LOWER(r.city) = LOWER(:city)) "
			+ "AND (:q IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :q, '%')) "
			+ "       OR LOWER(r.description) LIKE LOWER(CONCAT('%', :q, '%')) "
			+ "       OR LOWER(r.brand) LIKE LOWER(CONCAT('%', :q, '%'))) "
			+ "ORDER BY r.displayOrder ASC, r.name ASC")
	List<ResourceListing> search(@Param("category") ResourceCategory category,
			@Param("city") String city,
			@Param("q") String q);

	// Distinct cities (for the patient filter dropdown), active listings only.
	@Query("SELECT DISTINCT r.city FROM ResourceListing r WHERE r.active = true "
			+ "AND r.city IS NOT NULL AND r.city <> '' "
			+ "AND (:category IS NULL OR r.category = :category) "
			+ "ORDER BY r.city ASC")
	List<String> findDistinctCities(@Param("category") ResourceCategory category);

	// Admin: all listings (active + inactive), optional category filter.
	@Query("SELECT r FROM ResourceListing r "
			+ "WHERE (:category IS NULL OR r.category = :category) "
			+ "ORDER BY r.displayOrder ASC, r.name ASC")
	List<ResourceListing> findForAdmin(@Param("category") ResourceCategory category);
}
