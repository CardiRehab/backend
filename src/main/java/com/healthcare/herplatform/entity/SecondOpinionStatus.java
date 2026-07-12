package com.healthcare.herplatform.entity;

/**
 * Lifecycle of a second-opinion request. NEW = just submitted (patient may still
 * add/remove attachments), IN_REVIEW = a reviewer has picked it up, ANSWERED = the
 * cardiologist's opinion has been recorded, CLOSED = archived.
 */
public enum SecondOpinionStatus {
	NEW,
	IN_REVIEW,
	ANSWERED,
	CLOSED
}
