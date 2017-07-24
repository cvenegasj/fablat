package org.fablat.resource.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.LocationDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.RoleFabberDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.model.dao.WorkshopEventDAO;
import org.fablat.resource.model.dao.WorkshopEventTutorDAO;
import org.fablat.resource.model.daoimpl.FabberDAOImpl;
import org.fablat.resource.model.daoimpl.GroupDAOImpl;
import org.fablat.resource.model.daoimpl.GroupMemberDAOImpl;
import org.fablat.resource.model.daoimpl.LabDAOImpl;
import org.fablat.resource.model.daoimpl.LocationDAOImpl;
import org.fablat.resource.model.daoimpl.RoleDAOImpl;
import org.fablat.resource.model.daoimpl.RoleFabberDAOImpl;
import org.fablat.resource.model.daoimpl.SubGroupDAOImpl;
import org.fablat.resource.model.daoimpl.SubGroupMemberDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopEventDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopEventTutorDAOImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.fablat.resource.config, org.fablat.resource.controller, org.fablat.resource.model.daoimpl", excludeFilters = { @Filter(Configuration.class) })
@EnableTransactionManagement
@RestController
@EnableResourceServer
public class ResourceApplication extends ResourceServerConfigurerAdapter {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Map<String, Object> home() {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");

		return model;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}
	
	@Override
	@Order(-20)
    public void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/public/**").permitAll() // Allow public
		.and()
		.authorizeRequests()
			.anyRequest().authenticated(); // Secure the rest
		//.and()
			//.csrf()
				//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				//.ignoringAntMatchers("/public/**"); // No csrf for public API
    }

	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource(DataSourceProperties properties) {
		// commons-dbcp2 pool
		return new BasicDataSource();
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.scanPackages("org.fablat.resource.entities");
		// Hibernate properties
		sessionBuilder.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		sessionBuilder.setProperty("hibernate.globally_quoted_identifiers", "true");
		// sessionBuilder.setProperty("hibernate.enable_lazy_load_no_trans", "true");
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
	
	@Autowired
	@Bean(name = "groupDAO")
	public GroupDAO getGroupDAO() {
		return new GroupDAOImpl();
	}
	
	@Autowired
	@Bean(name = "groupMemberDAO")
	public GroupMemberDAO getGroupMemberDAO() {
		return new GroupMemberDAOImpl();
	}
	
	@Autowired
	@Bean(name = "labDAO")
	public LabDAO getLabDAO() {
		return new LabDAOImpl();
	}
	
	@Autowired
	@Bean(name = "locationDAO")
	public LocationDAO getLocationDAO() {
		return new LocationDAOImpl();
	}

	@Autowired
	@Bean(name = "roleDAO")
	public RoleDAO getRoleDAO() {
		return new RoleDAOImpl();
	}

	@Autowired
	@Bean(name = "roleFabberDAO")
	public RoleFabberDAO getRoleFabberDAO() {
		return new RoleFabberDAOImpl();
	}
	
	@Autowired
	@Bean(name = "subGroupDAO")
	public SubGroupDAO getSubGroupDAO() {
		return new SubGroupDAOImpl();
	}

	@Autowired
	@Bean(name = "subGroupMemberDAO")
	public SubGroupMemberDAO getSubGroupMemberDAO() {
		return new SubGroupMemberDAOImpl();
	}

	@Autowired
	@Bean(name = "workshopEventDAO")
	public WorkshopEventDAO getWorkshopEventDAO() {
		return new WorkshopEventDAOImpl();
	}
	
	@Autowired
	@Bean(name = "workshopEventTutorDAO")
	public WorkshopEventTutorDAO getWorkshopEventTutorDAO() {
		return new WorkshopEventTutorDAOImpl();
	}
}
