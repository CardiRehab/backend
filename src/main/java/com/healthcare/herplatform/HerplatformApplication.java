package com.healthcare.herplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//For SSL Bean
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties()
//@EntityScan(basePackages = {"./controllers","models","entity","com.healthcare.herplatform.chatserver.config"})
//@ComponentScan(basePackages = {"./controllers","models","entity","jwt","services","security","payloads"})


@EntityScan(basePackages = {"com.healthcare.herplatform.config","com.healthcare.herplatform.entity","com.healthcare.herplatform.controllers","com.healthcare.herplatform.models","com.healthcare.herplatform.websocketconfig"})
@ComponentScan(basePackages = {"com.healthcare.herplatform.config","com.healthcare.herplatform.websocketconfig","com.healthcare.herplatform.models","com.healthcare.herplatform.controllers","com.healthcare.herplatform.jwt","com.healthcare.herplatform.services","com.healthcare.herplatform.security","com.healthcare.herplatform.payloads"})
@EnableJpaRepositories(basePackages = "com.healthcare.herplatform.repository")
@EnableJpaAuditing

public class HerplatformApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HerplatformApplication.class);
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HerplatformApplication.class, args);
	}
	
//	@Bean
//    public ServletWebServerFactory servletContainer() {
//        // Enable SSL Trafic
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//
//        // Add HTTP to HTTPS redirect
//        tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());
//
//        return tomcat;
//    }

    /*
    We need to redirect from HTTP to HTTPS. Without SSL, this application used
    port 8082. With SSL it will use port 8443. So, any request for 8082 needs to be
    redirected to HTTPS on 8443.
     */
//    private Connector httpToHttpsRedirectConnector() {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setScheme("http");
//        connector.setPort(8443);
//        connector.setSecure(false);
//        connector.setRedirectPort(9595);
//        return connector;
//    }
	
	
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//			   registry.addMapping("/**").allowedOrigins("https://mbzjku.csb.app");	
//			}
//		};
//	}
	
}
