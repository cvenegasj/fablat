package org.fablat.ui.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
public class UiApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}
	
	@Bean
	protected OAuth2RestTemplate OAuth2RestTemplate(
			OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new OAuth2RestTemplate(resource, context);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
		.authorizeRequests()
			.antMatchers("/signup-successful.html", "/signup.html", "/signup-fabs.html", "/public.html", "/index.html", "/resource/public/**", "/login", "/").permitAll() // Allow anyone
			//.antMatchers("/app/index.html#/admin-lat/**").access("hasRole('ADMIN_LAT')")
			//.antMatchers("/app/index.html#/admin-lab/**").access("hasRole('ADMIN_LAB')")
			.anyRequest().authenticated() // All remaining URLs require the user to be authenticated
			.and()
			.logout().logoutSuccessUrl("/").permitAll()
			.and()
		.csrf()
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); 
		// @formatter:on
		
		/* http
			.antMatcher("/**").authorizeRequests()
				.antMatchers("/app/**").authenticated()
				.anyRequest().permitAll()
			.and()
				.logout().logoutSuccessUrl("/").permitAll()
			.and()
				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); */
	}
}
