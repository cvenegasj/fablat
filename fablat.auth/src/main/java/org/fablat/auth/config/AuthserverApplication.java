package org.fablat.auth.config;

import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.fablat.auth.model.dao.FabberDAO;
import org.fablat.auth.model.daoimpl.FabberDAOImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@RestController
@SessionAttributes("authorizationRequest")
@EnableResourceServer
public class AuthserverApplication extends WebMvcConfigurerAdapter {

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}
	
	@Configuration
	@Order(-20)
	protected static class LoginConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
			.formLogin().loginPage("/login")
			// redirect to UI /app/index.html if success
			.defaultSuccessUrl("http://lowcost-env.p2wn4fjmir.us-east-1.elasticbeanstalk.com/app/index.html", true).permitAll()
			.and()
				.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
			.and()
				.authorizeRequests().anyRequest().authenticated();
			// @formatter:on
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.parentAuthenticationManager(authenticationManager);
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer)
				throws Exception {
			oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
			clients.inMemory()
					.withClient("fablat")
					.secret("c6d4f0e6125fcc1879b8dc42983c73ea1b1adf4f67a1cba0010d29ed8895017c")
					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit",
							"client_credentials").authorities("ROLE_USER", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
					.scopes("read", "write", "trust", "openid")
					// .redirectUris("http://localhost:8080/login")
					.autoApprove(true);
			// @formatter:on
		}
	}

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, FabberDAO fabberDAO) throws Exception {
		builder.userDetailsService(userDetailsService(fabberDAO));
	}

	private UserDetailsService userDetailsService(final FabberDAO fabberDAO) {
		return new UserDetailsService() {

			// @Autowired
			// private FabberDAO fabberDAO;

			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return new CustomUserDetails(fabberDAO.findByUsername(username));
			}
		};
	}

	// database connection stuff -------------------------
	@Bean(destroyMethod = "close")
	public DataSource dataSource() throws ClassNotFoundException, SQLException, URISyntaxException {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		// dataSource.setUrl("jdbc:mysql://localhost:3306/fablat_db");
		dataSource.setUrl("jdbc:mysql://mysqlinstance.cq6tkpch7j8q.us-east-1.rds.amazonaws.com:3306/fablat_db");
		dataSource.setUsername("root");
		// dataSource.setPassword("admin");
		dataSource.setPassword("01382155144");

		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.scanPackages("org.fablat.auth.entities");
		// Hibernate properties
		sessionBuilder.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		sessionBuilder.setProperty("hibernate.globally_quoted_identifiers", "true");
		// sessionBuilder.setProperty("hibernate.enable_lazy_load_no_trans",
		// "true");
		sessionBuilder.setProperty("current_session_context_class", "thread");
		sessionBuilder.setProperty("cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
		sessionBuilder.setProperty("hibernate.show_sql", "true");

		return sessionBuilder.buildSessionFactory();
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}

	@Autowired
	@Bean(name = "fabberDAO")
	public FabberDAO getFabberDAO() {
		return new FabberDAOImpl();
	}
}
