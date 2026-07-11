package com.healthcare.herplatform.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * A single external "trusted resource" shown to patients in the Resources directory
 * (hospitals, specialists, devices, government schemes, diet, exercise, mental
 * health, education and emergency resources — see {@link ResourceCategory}).
 *
 * These are admin-authored content records only. The listed parties are NOT users of the
 * platform — they have no account, login or API access. All patient interaction is one-way
 * outbound (tel:/website/buy-rent/maps link), so the directory stays walled off from
 * platform and patient data.
 */
@Entity
@Table(name = "resource_listings")
public class ResourceListing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ---- Common ----
	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 32)
	private ResourceCategory category;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "image_url", length = 1000)
	private String imageUrl;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "pincode", length = 16)
	private String pincode;

	// Unused in Phase 1; reserved for Phase-2 "find near me" map search.
	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "phone", length = 64)
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "website_url", length = 1000)
	private String websiteUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "listing_type", length = 16)
	private ResourceListingType listingType;

	// Who is behind the resource (government / private / non-profit / international);
	// shown as a badge so patients can judge the source at a glance.
	@Enumerated(EnumType.STRING)
	@Column(name = "kind", length = 16)
	private ResourceKind kind;

	@Column(name = "verified")
	private boolean verified;

	@Column(name = "is_active")
	private boolean active = true;

	@Column(name = "display_order")
	private int displayOrder;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "created_by")
	private String createdBy;

	// ---- Device-specific (null for providers) ----
	@Column(name = "brand")
	private String brand;

	@Enumerated(EnumType.STRING)
	@Column(name = "device_type", length = 32)
	private DeviceType deviceType;

	@Column(name = "buy_url", length = 1000)
	private String buyUrl;

	@Column(name = "rent_url", length = 1000)
	private String rentUrl;

	@Column(name = "price_info")
	private String priceInfo;

	// Brand-vouched "clinically tested / recommended" badge — responsibility sits with the brand.
	@Column(name = "tested")
	private boolean tested;

	// ---- Provider-specific (null for devices) ----
	@Column(name = "specialization")
	private String specialization;

	@Column(name = "qualifications", columnDefinition = "TEXT")
	private String qualifications;

	@Column(name = "affiliation")
	private String affiliation;

	@Column(name = "consultation_info", columnDefinition = "TEXT")
	private String consultationInfo;

	public ResourceListing() {
		super();
	}

	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResourceCategory getCategory() {
		return category;
	}

	public void setCategory(ResourceCategory category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public ResourceListingType getListingType() {
		return listingType;
	}

	public void setListingType(ResourceListingType listingType) {
		this.listingType = listingType;
	}

	public ResourceKind getKind() {
		return kind;
	}

	public void setKind(ResourceKind kind) {
		this.kind = kind;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public String getRentUrl() {
		return rentUrl;
	}

	public void setRentUrl(String rentUrl) {
		this.rentUrl = rentUrl;
	}

	public String getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}

	public boolean isTested() {
		return tested;
	}

	public void setTested(boolean tested) {
		this.tested = tested;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getConsultationInfo() {
		return consultationInfo;
	}

	public void setConsultationInfo(String consultationInfo) {
		this.consultationInfo = consultationInfo;
	}
}
