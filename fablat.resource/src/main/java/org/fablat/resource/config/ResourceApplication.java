package org.fablat.resource.config;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.fablat.resource.model.dao.AttendantDAO;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.ProjectDAO;
import org.fablat.resource.model.dao.ProjectMemberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.LocationDAO;
import org.fablat.resource.model.dao.ReplicationAttendantDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.RoleFabberDAO;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.fablat.resource.model.dao.WorkshopLocationDAO;
import org.fablat.resource.model.dao.WorkshopMemberDAO;
import org.fablat.resource.model.daoimpl.AttendantDAOImpl;
import org.fablat.resource.model.daoimpl.FabberDAOImpl;
import org.fablat.resource.model.daoimpl.ProjectDAOImpl;
import org.fablat.resource.model.daoimpl.ProjectMemberDAOImpl;
import org.fablat.resource.model.daoimpl.LabDAOImpl;
import org.fablat.resource.model.daoimpl.GroupDAOImpl;
import org.fablat.resource.model.daoimpl.LocationDAOImpl;
import org.fablat.resource.model.daoimpl.ReplicationAttendantDAOImpl;
import org.fablat.resource.model.daoimpl.RoleDAOImpl;
import org.fablat.resource.model.daoimpl.RoleFabberDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopLocationDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopMemberDAOImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
	@Bean(name = "attendantDAO")
	public AttendantDAO getAttendantDAO() {
		return new AttendantDAOImpl();
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
	@Bean(name = "projectDAO")
	public ProjectDAO getProjectDAO() {
		return new ProjectDAOImpl();
	}

	@Autowired
	@Bean(name = "projectMemberDAO")
	public ProjectMemberDAO getProjectMemberDAO() {
		return new ProjectMemberDAOImpl();
	}
	
	@Autowired
	@Bean(name = "replicationAttendantDAO")
	public ReplicationAttendantDAO getReplicationAttendantDAO() {
		return new ReplicationAttendantDAOImpl();
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
	@Bean(name = "workshopDAO")
	public WorkshopDAO getWorkshopDAO() {
		return new WorkshopDAOImpl();
	}

	@Autowired
	@Bean(name = "workshopLocationDAO")
	public WorkshopLocationDAO getWorkshopLocationDAO() {
		return new WorkshopLocationDAOImpl();
	}

	@Autowired
	@Bean(name = "workshopMemberDAO")
	public WorkshopMemberDAO getWorkshopMemberDAO() {
		return new WorkshopMemberDAOImpl();
	}
}
