package org.fablat.ui.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**").antMatchers("/js/**").antMatchers("/images/**");
	}*/

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/signup-1.html", "/signup-2.html", "/signup-successful.html",
						"/home.html", "/calendar.html", "/index.html", "/resource/public/**", "/login", "/")
				.permitAll()
				// .antMatchers("/app/index.html#/admin-lat/**").access("hasRole('ADMIN_LAT')")
				// .antMatchers("/app/index.html#/admin-lab/**").access("hasRole('ADMIN_LAB')")
				.anyRequest().authenticated()
				.and()
				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	@Bean
	protected OAuth2RestTemplate OAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new OAuth2RestTemplate(resource, context);
	}

}
