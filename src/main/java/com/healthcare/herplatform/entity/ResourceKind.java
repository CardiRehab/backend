package com.healthcare.herplatform.entity;

/**
 * Who is behind a listed resource. Shown as a badge so patients can tell an
 * official government service from a private provider at a glance.
 */
public enum ResourceKind {
	GOVERNMENT,     // Indian government body / scheme / public hospital
	PRIVATE,        // Private hospital, company or commercial service
	NON_PROFIT,     // Charitable trust, professional society, NGO
	INTERNATIONAL   // Reputed international health organisation (AHA, BHF, NIH…)
}
