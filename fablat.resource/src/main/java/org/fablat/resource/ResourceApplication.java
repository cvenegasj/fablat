package org.fablat.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.fablat.resource.model.dao.ActivityLogDAO;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.FabberInfoDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.LocationDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.RoleFabberDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.fablat.resource.model.dao.WorkshopTutorDAO;
import org.fablat.resource.model.daoimpl.ActivityLogDAOImpl;
import org.fablat.resource.model.daoimpl.FabberDAOImpl;
import org.fablat.resource.model.daoimpl.FabberInfoDAOImpl;
import org.fablat.resource.model.daoimpl.GroupDAOImpl;
import org.fablat.resource.model.daoimpl.GroupMemberDAOImpl;
import org.fablat.resource.model.daoimpl.LabDAOImpl;
import org.fablat.resource.model.daoimpl.LocationDAOImpl;
import org.fablat.resource.model.daoimpl.RoleDAOImpl;
import org.fablat.resource.model.daoimpl.RoleFabberDAOImpl;
import org.fablat.resource.model.daoimpl.SubGroupDAOImpl;
import org.fablat.resource.model.daoimpl.SubGroupMemberDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopDAOImpl;
import org.fablat.resource.model.daoimpl.WorkshopTutorDAOImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableTransactionManagement
@RestController
public class ResourceApplication {
	
	// For Tomcat deployment
	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ResourceApplication.class);
    } */
	
	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");

		return model;
	}
	
	// for encoding password on user sign up
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	// =========== Database connection settings ===========
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
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
	
	// For handling Spring DataIntegrityViolationException and applying it transparently to all beans annotated with @Repository
	// from: http://www.baeldung.com/spring-dataIntegrityviolationexception
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	
	// ========== DAO Beans ==========

	@Bean(name = "activityLogDAO")
	public ActivityLogDAO getActivityLogDAO() {
		return new ActivityLogDAOImpl();
	}
	
	@Bean(name = "fabberDAO")
	public FabberDAO getFabberDAO() {
		return new FabberDAOImpl();
	}
	
	@Bean(name = "fabberInfoDAO")
	public FabberInfoDAO getFabberInfoDAO() {
		return new FabberInfoDAOImpl();
	}
	
	@Bean(name = "groupDAO")
	public GroupDAO getGroupDAO() {
		return new GroupDAOImpl();
	}
	
	@Bean(name = "groupMemberDAO")
	public GroupMemberDAO getGroupMemberDAO() {
		return new GroupMemberDAOImpl();
	}
	
	@Bean(name = "labDAO")
	public LabDAO getLabDAO() {
		return new LabDAOImpl();
	}
	
	@Bean(name = "locationDAO")
	public LocationDAO getLocationDAO() {
		return new LocationDAOImpl();
	}

	@Bean(name = "roleDAO")
	public RoleDAO getRoleDAO() {
		return new RoleDAOImpl();
	}

	@Bean(name = "roleFabberDAO")
	public RoleFabberDAO getRoleFabberDAO() {
		return new RoleFabberDAOImpl();
	}
	
	@Bean(name = "subGroupDAO")
	public SubGroupDAO getSubGroupDAO() {
		return new SubGroupDAOImpl();
	}

	@Bean(name = "subGroupMemberDAO")
	public SubGroupMemberDAO getSubGroupMemberDAO() {
		return new SubGroupMemberDAOImpl();
	}

	@Bean(name = "workshopDAO")
	public WorkshopDAO getWorkshopDAO() {
		return new WorkshopDAOImpl();
	}

	@Bean(name = "workshopTutorDAO")
	public WorkshopTutorDAO getWorkshopTutorDAO() {
		return new WorkshopTutorDAOImpl();
	}
	
}
