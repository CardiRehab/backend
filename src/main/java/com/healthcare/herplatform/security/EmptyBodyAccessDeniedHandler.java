package com.healthcare.herplatform.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Renders every authorisation failure as a bare 403 with no body.
 *
 * <p>Spring's default handler calls {@code sendError}, which routes through the container error page
 * and returns a JSON body describing the failure. That body is an enumeration oracle: a caller
 * probing ids could tell "this record is not yours" apart from "this record does not exist" and map
 * out which ids are real. Setting the status directly, with nothing written, makes the two cases
 * indistinguishable.
 *
 * <p>This applies to {@link AccessDeniedException} only. Endpoints that deliberately return an
 * explanatory 403 of their own — {@code deleteMessage}'s "too old to delete", which is reachable
 * only by the message's own sender and leaks nothing — return a {@code ResponseEntity} rather than
 * throwing, and are unaffected.
 */
@Component
public class EmptyBodyAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		if (response.isCommitted()) {
			return;
		}
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentLength(0);
		response.flushBuffer();
	}
}
