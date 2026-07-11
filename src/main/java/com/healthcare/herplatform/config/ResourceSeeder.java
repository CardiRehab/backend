package com.healthcare.herplatform.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.healthcare.herplatform.entity.ResourceCategory;
import com.healthcare.herplatform.entity.ResourceKind;
import com.healthcare.herplatform.entity.ResourceListing;
import com.healthcare.herplatform.entity.ResourceListingType;
import com.healthcare.herplatform.repository.ResourceListingRepository;

import static com.healthcare.herplatform.entity.ResourceCategory.CARDIOLOGIST;
import static com.healthcare.herplatform.entity.ResourceCategory.DEVICE;
import static com.healthcare.herplatform.entity.ResourceCategory.DIET_NUTRITION;
import static com.healthcare.herplatform.entity.ResourceCategory.EDUCATION;
import static com.healthcare.herplatform.entity.ResourceCategory.EMERGENCY;
import static com.healthcare.herplatform.entity.ResourceCategory.EXERCISE_REHAB;
import static com.healthcare.herplatform.entity.ResourceCategory.GOVT_SCHEME;
import static com.healthcare.herplatform.entity.ResourceCategory.HOSPITAL;
import static com.healthcare.herplatform.entity.ResourceCategory.MENTAL_HEALTH;
import static com.healthcare.herplatform.entity.ResourceKind.GOVERNMENT;
import static com.healthcare.herplatform.entity.ResourceKind.INTERNATIONAL;
import static com.healthcare.herplatform.entity.ResourceKind.NON_PROFIT;
import static com.healthcare.herplatform.entity.ResourceKind.PRIVATE;

/**
 * One-time starter content for the External Trusted Resources directory.
 *
 * Seeds a curated, verified set of cardiac-care resources (hospitals, specialist
 * registries, government schemes, diet/exercise/mental-health education, devices
 * and emergency helplines) ONLY when the table is completely empty, so anything
 * the Board admin later adds, edits or deletes is never touched or resurrected.
 * Every entry is a public, one-way outbound link — no partner integrations.
 *
 * Disable entirely with RESOURCE_SEED_ENABLED=false.
 */
@Component
public class ResourceSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ResourceSeeder.class);

	private final ResourceListingRepository repo;

	@Value("${app.resources.seed.enabled:true}")
	private boolean seedEnabled;

	public ResourceSeeder(ResourceListingRepository repo) {
		this.repo = repo;
	}

	@Override
	public void run(String... args) {
		if (!seedEnabled) {
			log.info("Resource seed disabled (RESOURCE_SEED_ENABLED=false); skipping.");
			return;
		}
		if (repo.count() > 0) {
			return; // Board-managed content exists; never overwrite it.
		}
		List<ResourceListing> seed = buildSeed();
		repo.saveAll(seed);
		log.info("Seeded {} starter resource listings across {} categories.",
				seed.size(), ResourceCategory.values().length);
	}

	private List<ResourceListing> buildSeed() {
		List<ResourceListing> l = new ArrayList<ResourceListing>();

		// ---- Hospitals & rehab centres (govt + private + charitable, spread across regions) ----
		l.add(item(HOSPITAL, GOVERNMENT, true, 1, "AIIMS New Delhi",
				"India's premier public medical institute. Its Cardiothoracic Sciences Centre provides complete heart care — diagnosis, surgery and follow-up — at government rates.",
				"https://www.aiims.edu", "New Delhi", "Delhi", null));
		l.add(item(HOSPITAL, GOVERNMENT, true, 2, "Sree Chitra Tirunal Institute (SCTIMST)",
				"Institute of National Importance specialising in cardiovascular care and research, run by the Government of India.",
				"https://www.sctimst.ac.in", "Thiruvananthapuram", "Kerala", null));
		l.add(item(HOSPITAL, GOVERNMENT, true, 3, "Sri Jayadeva Institute of Cardiovascular Sciences",
				"One of India's largest dedicated heart hospitals, known for high-volume, affordable cardiac care under the Karnataka government.",
				"https://jayadevacardiology.com", "Bengaluru", "Karnataka", null));
		l.add(item(HOSPITAL, GOVERNMENT, true, 4, "U. N. Mehta Institute of Cardiology",
				"Large government-affiliated heart hospital and research centre serving western India.",
				"https://www.unmicrc.org", "Ahmedabad", "Gujarat", null));
		l.add(item(HOSPITAL, GOVERNMENT, true, 5, "PGIMER Chandigarh",
				"Leading public medical institute in north India with an advanced cardiology department.",
				"https://pgimer.edu.in", "Chandigarh", "Chandigarh", null));
		l.add(item(HOSPITAL, PRIVATE, false, 6, "Fortis Escorts Heart Institute",
				"Dedicated heart hospital and one of the pioneers of cardiac care in India (part of Fortis Healthcare).",
				"https://www.fortishealthcare.com", "New Delhi", "Delhi", null));
		l.add(item(HOSPITAL, PRIVATE, false, 7, "Narayana Institute of Cardiac Sciences",
				"Among the world's largest heart hospitals, performing thousands of cardiac procedures every year.",
				"https://www.narayanahealth.org", "Bengaluru", "Karnataka", null));
		l.add(item(HOSPITAL, PRIVATE, false, 8, "Apollo Hospitals — Heart Institutes",
				"Pan-India private hospital network with dedicated centres of excellence for heart care in most major cities.",
				"https://www.apollohospitals.com", "Chennai", "Tamil Nadu", null));
		l.add(item(HOSPITAL, NON_PROFIT, false, 9, "Christian Medical College (CMC) Vellore",
				"Renowned charitable teaching hospital with a long-standing cardiology department and subsidised care.",
				"https://www.cmch-vellore.edu", "Vellore", "Tamil Nadu", null));

		// ---- Cardiologists: find & verify specialists ----
		l.add(item(CARDIOLOGIST, GOVERNMENT, true, 1, "Indian Medical Register (NMC)",
				"Official National Medical Commission register. Verify any doctor's medical registration before consulting.",
				"https://www.nmc.org.in", null, null, null));
		l.add(item(CARDIOLOGIST, GOVERNMENT, true, 2, "National Healthcare Professionals Registry",
				"Government registry of verified doctors and health professionals under the Ayushman Bharat Digital Mission.",
				"https://nhpr.abdm.gov.in", null, null, null));
		l.add(item(CARDIOLOGIST, NON_PROFIT, false, 3, "Cardiological Society of India",
				"The national professional society of Indian cardiologists — conferences, guidelines and public awareness material.",
				"https://csi.org.in", null, null, null));
		l.add(item(CARDIOLOGIST, PRIVATE, false, 4, "Find Cardiologists on Practo",
				"Popular directory to search cardiologists near you by city, with patient reviews and appointment booking.",
				"https://www.practo.com", null, null, null));

		// ---- Devices & monitoring ----
		l.add(item(DEVICE, INTERNATIONAL, false, 1, "STRIDE BP — Validated BP Monitors",
				"International scientific list of clinically validated blood-pressure monitors. Check your model here before buying.",
				"https://www.stridebp.org", null, null, null));
		l.add(item(DEVICE, GOVERNMENT, true, 2, "CDSCO — Medical Device Regulator",
				"India's official medical device and drug regulator. Lists approved medical devices and licensed manufacturers.",
				"https://cdsco.gov.in", null, null, null));
		l.add(item(DEVICE, INTERNATIONAL, false, 3, "Home Blood-Pressure Monitoring (AHA)",
				"How to measure your blood pressure correctly at home — cuff position, timing and recording, from the American Heart Association.",
				"https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings/monitoring-your-blood-pressure-at-home",
				null, null, null));
		l.add(item(DEVICE, INTERNATIONAL, false, 4, "Pacemaker Patient Guide (BHF)",
				"Clear patient guide to living with a pacemaker — checks, precautions and daily life, from the British Heart Foundation.",
				"https://www.bhf.org.uk/informationsupport/treatments/pacemakers", null, null, null));

		// ---- Government schemes & support ----
		l.add(item(GOVT_SCHEME, GOVERNMENT, true, 1, "Ayushman Bharat PM-JAY — Hospital Finder",
				"Health cover of ₹5 lakh per family per year for eligible families; many heart procedures are covered. Find empanelled hospitals near you.",
				"https://hospitals.pmjay.gov.in", null, null, null));
		l.add(item(GOVT_SCHEME, GOVERNMENT, true, 2, "PM-JAY — Check Your Eligibility",
				"Official National Health Authority portal to check if your family is eligible for the Ayushman Bharat scheme and get your card.",
				"https://beneficiary.nha.gov.in", null, null, null));
		l.add(item(GOVT_SCHEME, GOVERNMENT, true, 3, "ABHA — Digital Health ID",
				"Create your free ABHA number to keep prescriptions, reports and health records digitally in one place.",
				"https://abdm.gov.in", null, null, null));
		l.add(item(GOVT_SCHEME, GOVERNMENT, true, 4, "Jan Aushadhi — Affordable Medicines",
				"Government scheme for quality generic medicines at much lower prices — including common heart medicines — at Jan Aushadhi Kendras.",
				"https://janaushadhi.gov.in", null, null, null));

		// ---- Diet & nutrition ----
		l.add(item(DIET_NUTRITION, GOVERNMENT, true, 1, "Dietary Guidelines for Indians (ICMR-NIN)",
				"Official evidence-based dietary guidance from India's National Institute of Nutrition.",
				"https://www.nin.res.in", null, null, null));
		l.add(item(DIET_NUTRITION, GOVERNMENT, true, 2, "Eat Right India (FSSAI)",
				"National healthy-eating movement — practical tips to cut salt, sugar and oil in everyday Indian food.",
				"https://eatrightindia.gov.in", null, null, null));
		l.add(item(DIET_NUTRITION, INTERNATIONAL, false, 3, "DASH Eating Plan (NIH)",
				"Proven eating plan that lowers blood pressure and cholesterol — flexible enough to adapt to Indian meals.",
				"https://www.nhlbi.nih.gov/education/dash-eating-plan", null, null, null));
		l.add(item(DIET_NUTRITION, INTERNATIONAL, false, 4, "Healthy Eating (AHA)",
				"Heart-healthy nutrition guidance, food swaps and recipes from the American Heart Association.",
				"https://www.heart.org/en/healthy-living/healthy-eating", null, null, null));
		l.add(item(DIET_NUTRITION, INTERNATIONAL, false, 5, "Heart-Healthy Eating (BHF)",
				"Simple, practical healthy-eating advice for heart patients from the British Heart Foundation.",
				"https://www.bhf.org.uk/informationsupport/support/healthy-living/healthy-eating", null, null, null));

		// ---- Exercise & rehab ----
		l.add(item(EXERCISE_REHAB, INTERNATIONAL, false, 1, "What Is Cardiac Rehabilitation? (AHA)",
				"What a cardiac rehab programme involves, why it works and what to expect at every phase.",
				"https://www.heart.org/en/health-topics/cardiac-rehab", null, null, null));
		l.add(item(EXERCISE_REHAB, INTERNATIONAL, false, 2, "Staying Active with a Heart Condition (BHF)",
				"How to exercise safely after a heart event — pacing, warning signs and building up gradually.",
				"https://www.bhf.org.uk/informationsupport/support/healthy-living/staying-active", null, null, null));
		l.add(item(EXERCISE_REHAB, GOVERNMENT, true, 3, "Common Yoga Protocol (Ministry of Ayush)",
				"The government's standardised yoga programme. Ask your CR specialist which practices are safe for you before starting.",
				"https://yoga.ayush.gov.in", null, null, null));
		l.add(item(EXERCISE_REHAB, null, false, 4, "Guided Rehab Exercise Videos (YouTube)",
				"Ready-made video search for gentle, guided cardiac rehab exercises. Always follow the plan your CR specialist has set for you.",
				"https://www.youtube.com/results?search_query=cardiac+rehabilitation+exercises+at+home", null, null, null));

		// ---- Mental health ----
		l.add(item(MENTAL_HEALTH, GOVERNMENT, true, 1, "Tele-MANAS — 24×7 Mental Health Helpline",
				"Free 24×7 government mental-health support in your language. Feeling low or anxious after a heart event is common — call 14416.",
				"https://telemanas.mohfw.gov.in", null, null, "14416"));
		l.add(item(MENTAL_HEALTH, GOVERNMENT, true, 2, "NIMHANS",
				"India's premier institute for mental health and neurosciences, with outpatient and tele-consultation services.",
				"https://nimhans.ac.in", "Bengaluru", "Karnataka", null));
		l.add(item(MENTAL_HEALTH, INTERNATIONAL, false, 3, "Mental Health and Heart Disease (AHA)",
				"How stress, anxiety and depression affect the heart — and evidence-based ways to look after your mind during recovery.",
				"https://www.heart.org/en/healthy-living/healthy-lifestyle/mental-health-and-wellbeing", null, null, null));

		// ---- Education ----
		l.add(item(EDUCATION, INTERNATIONAL, false, 1, "British Heart Foundation",
				"Easy-to-understand booklets and videos on every heart condition, treatment and recovery step.",
				"https://www.bhf.org.uk", null, null, null));
		l.add(item(EDUCATION, INTERNATIONAL, false, 2, "American Heart Association",
				"World's leading heart-health organisation — patient education on conditions, treatments and healthy living.",
				"https://www.heart.org", null, null, null));
		l.add(item(EDUCATION, INTERNATIONAL, false, 3, "MedlinePlus — Heart Diseases",
				"Trusted plain-language health information from the US National Library of Medicine, with no advertising.",
				"https://medlineplus.gov/heartdiseases.html", null, null, null));
		l.add(item(EDUCATION, INTERNATIONAL, false, 4, "World Heart Federation",
				"Global heart-health body behind World Heart Day — awareness resources and global heart facts.",
				"https://world-heart-federation.org", null, null, null));
		l.add(item(EDUCATION, NON_PROFIT, false, 5, "Indian Heart Association",
				"Non-profit focused on heart-disease awareness among South Asians, who face higher and earlier heart risk.",
				"https://indianheartassociation.org", null, null, null));

		// ---- Emergency & helplines ----
		l.add(item(EMERGENCY, GOVERNMENT, true, 1, "National Emergency Number — 112",
				"India's single emergency number for ambulance, police and fire. Save it and call immediately in any emergency.",
				null, null, null, "112"));
		l.add(item(EMERGENCY, GOVERNMENT, true, 2, "108 Ambulance (EMRI)",
				"Free emergency ambulance service across most states. Call 108 for chest pain, breathlessness or collapse.",
				"https://www.emri.in", null, null, "108"));
		l.add(item(EMERGENCY, INTERNATIONAL, false, 3, "Heart Attack Warning Signs (AHA)",
				"Chest discomfort, breathlessness, arm or jaw pain, cold sweat — learn the warning signs and act fast.",
				"https://www.heart.org/en/health-topics/heart-attack/warning-signs-of-a-heart-attack", null, null, null));
		l.add(item(EMERGENCY, INTERNATIONAL, false, 4, "Hands-Only CPR (AHA)",
				"Two simple steps your family can learn today: call for help, then push hard and fast in the centre of the chest.",
				"https://cpr.heart.org/en/cpr-courses-and-kits/hands-only-cpr", null, null, null));

		return l;
	}

	private static ResourceListing item(ResourceCategory category, ResourceKind kind, boolean verified,
			int order, String name, String description, String websiteUrl, String city, String state, String phone) {
		ResourceListing r = new ResourceListing();
		r.setCategory(category);
		r.setKind(kind);
		r.setVerified(verified);
		r.setDisplayOrder(order);
		r.setName(name);
		r.setDescription(description);
		r.setWebsiteUrl(websiteUrl);
		r.setCity(city);
		r.setState(state);
		r.setPhone(phone);
		r.setListingType(ResourceListingType.PUBLIC);
		r.setActive(true);
		r.setCreatedBy("seed");
		return r;
	}
}
