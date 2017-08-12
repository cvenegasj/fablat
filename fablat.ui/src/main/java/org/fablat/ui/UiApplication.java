package org.fablat.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class UiApplication {
	
	// For Tomcat deployment
	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UiApplication.class);
    }*/

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}

}
