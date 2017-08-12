package org.fablat.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	@Order(-20)
    public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/public/**").permitAll() // Allow public
			.anyRequest().authenticated(); // Secure the rest
    }
	
}
