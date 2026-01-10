package com.healthcare.herplatform.jwt;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import com.healthcare.herplatform.services.UserDetailsImpl;
import com.healthcare.herplatform.services.UserDetailsServiceImpl;

import io.jsonwebtoken.*;
//import java.util.function.Function;

@Component
public class JwtUtils {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	/* JWT secret signature key */
	//@Value("CardiRehabSecretKeyX12138yAn4")
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	/* JWT session expiration time */
	//@Value("${cardirehab.app.jwtExpirationMs}")
	//@Value("86400000") // 24 Hours
	//@Value("120000")  // 2 Minutes or 120K Milliseconds
	@Value("7200000")  // 2 Hours or 7200K Milliseconds
	private long jwtExpirationMs;
	
//	public String generateJwtToken(Authentication authentication) {
//		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//		return Jwts.builder()
//				.setSubject((userPrincipal.getUsername()))
//				.setIssuedAt(new Date())
//				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//				.signWith(SignatureAlgorithm.HS512, jwtSecret)
//				.compact();
//	}
	
	public String generateJwtToken(Authentication authentication) {
	    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

	    List<String> roles = userPrincipal.getAuthorities()
	            .stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toList());
	    
	    return Jwts.builder()
	            .setSubject(userPrincipal.getUsername())
	            .claim("roles", roles)  // ADD ROLES HERE
	            .setIssuedAt(new Date())
	            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
	            .signWith(SignatureAlgorithm.HS512, jwtSecret)
	            .compact();
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}
	
//	public String generateTokenFromUsername(String username) {
//	    return Jwts.builder()
//	            .setSubject(username)
//	            .setIssuedAt(new Date())
//	            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//	            .signWith(SignatureAlgorithm.HS512, jwtSecret)
//	            .compact();
//	}
	
	public String generateTokenFromUsername(String username) {

	    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	    List<String> roles = userDetails.getAuthorities()
	            .stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toList());

	    return Jwts.builder()
	            .setSubject(username)
	            .claim("roles", roles)  // ADD ROLES
	            .setIssuedAt(new Date())
	            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
	            .signWith(SignatureAlgorithm.HS512, jwtSecret)
	            .compact();
	}
//	// Validate token
//    public boolean validateToken(String token, String username) {
//        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
//    }
//
//    // Extract user name from token
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//    
//    // Check if token is expired
//    public boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // Extract expiration date
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//    
//    // Generic method to extract claims
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtSecret)
//                .parseClaimsJws(token)
//                .getBody();
//        return claimsResolver.apply(claims);
//    }
}
