package com.healthcare.herplatform.entity;

/**
 * Sections of the External Trusted Resources directory, covering the cardiac-rehab
 * patient journey: where to get care, who to consult, how to afford it, how to
 * monitor, what to eat, how to move, mind matters, learning, and emergencies.
 *
 * PSYCHOLOGIST and DIETICIAN listings now live under MENTAL_HEALTH and
 * DIET_NUTRITION respectively (the provider fields still apply).
 */
public enum ResourceCategory {
	HOSPITAL,        // Cardiac hospitals & rehab centres
	CARDIOLOGIST,    // Find / verify heart specialists
	DEVICE,          // Home monitoring devices
	GOVT_SCHEME,     // Government health schemes & support
	DIET_NUTRITION,  // Heart-healthy diet & nutrition
	EXERCISE_REHAB,  // Safe activity & rehab education
	MENTAL_HEALTH,   // Emotional wellbeing & helplines
	EDUCATION,       // Trusted heart-health education portals
	EMERGENCY        // Warning signs, CPR, emergency numbers
}
