package com.healthcare.herplatform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.healthcare.herplatform.jwt.AuthEntryPointJwt;
import com.healthcare.herplatform.jwt.AuthTokenFilter;
import com.healthcare.herplatform.services.UserDetailsServiceImpl;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)


public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean // To disable the default behaviour for reading PATIENT to -> ROLE_PATIENT
//	GrantedAuthorityDefaults grantedAuthorityDefaults() {
//	    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
//	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable()
//			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//			.authorizeRequests().antMatchers("/api/auth/**").permitAll()
//			.antMatchers("/api/test/**").permitAll()
//			.anyRequest().authenticated().and();
//		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList(
	    		"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444",
	    	    "https://preprod.cardirehab.com:8444","https://www.cardirehab.com", "https://cardirehab.com",
			"https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595",
			"http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000",
			"http://localhost:3002")); 
	    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
	    configuration.setAllowCredentials(true);
	    configuration.setMaxAge(3600L);  // in seconds 3600(1 Hr)
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .cors()
		    .and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
			
			 // Public endpoints (login, register/sign-up, etc.)
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // ADD THIS
			.antMatchers("/api/auth/**").permitAll()
			.antMatchers("/crehab/api/auth/**").permitAll()
			.antMatchers("/api/geo/**").permitAll()
			.antMatchers("/ws/**").permitAll()
			.antMatchers("/chatroom/**").permitAll()
			
			// Role-based restrictions
			.antMatchers("/api/test/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/checkfilealgo/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/checkfileattachalgo/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/api/video/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/assignedusers/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/activitiesdiary/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/assessmentforms/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/api/user/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			.antMatchers("/user/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN") 
			.antMatchers("/app/**").hasAnyRole("PATIENT", "CRSPL", "LHCP", "ADMIN")
			
			.anyRequest().authenticated().and()
			.formLogin();
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//	    http
//	    .authorizeRequests()
//	    
//	    .antMatchers("/").permitAll()
//	    
//	    .antMatchers("/showStudentList").authenticated()
//	    .antMatchers("/showAddStudentPage").authenticated()
//	    .antMatchers("/save-student").authenticated()
//	    .antMatchers("/updateStudent").authenticated()
//	    .antMatchers("/deleteStudent").hasAuthority("admin") //only admin can delete the entry
//	    .antMatchers("/greet/{yourName}").authenticated()
//	    
//	    .and()
//	      .formLogin().loginPage("/myCustomLogin").loginProcessingUrl("/process-login")
//	      .and()
//	      .httpBasic() 
//	      .and().logout().permitAll()
//	      .and().exceptionHandling().accessDeniedPage("/accessDenied");
//    }
	
}
