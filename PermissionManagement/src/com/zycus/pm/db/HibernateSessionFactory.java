package com.zycus.pm.db;

import javax.naming.InitialContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class HibernateSessionFactory {

	/**
	 * Default constructor.
	 */
	private HibernateSessionFactory() {
	}

	private static String				CONFIG_FILE_LOCATION	= "pmHibernate.cfg.xml";

	public static final Configuration	cfg						= new Configuration();

	private static SessionFactory		sessionFactory			= null;

	/**
	 * initialises the configuration if not yet done and returns the current
	 * instance.
	 * 
	 * @return
	 */
	public static SessionFactory getInstance() {
		if (sessionFactory == null) {
			initSessionFactory();
		}
		return sessionFactory;
	}

	/**
	 * Returns the ThreadLocal Session instance. Lazy initialize the
	 * <code>SessionFactory</code> if needed.
	 * 
	 * @return Session
	 * @throws HibernateException
	 */
	public Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * The behaviour of this method depends on the session context you have
	 * configured. This factory is intended to be used with a hibernate.cfg.xml
	 * including the following property <property
	 * name="current_session_context_class">thread</property> This would return
	 * the current open session or if this does not exist, will create a new
	 * session.
	 * 
	 * @return Session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * initializes the sessionfactory in a safe way even if more than one thread
	 * tries to build a sessionFactory
	 */
	private static synchronized void initSessionFactory() {
		/*
		 * [laliluna] check again for null because sessionFactory may have been
		 * initialized between the last check and now
		 */
		// Logger log = Logger.getLogger(HibernateSessionFactory.class);
		if (sessionFactory == null) {
			try {
				cfg.configure(CONFIG_FILE_LOCATION);
				
				String sessionFactoryJndiName = cfg.getProperty(Environment.SESSION_FACTORY_NAME);
				if (sessionFactoryJndiName != null) {
					cfg.buildSessionFactory();
					// log.debug("get a jndi session factory");
					sessionFactory = (SessionFactory) (new InitialContext()).lookup(sessionFactoryJndiName);
				} else {
					// log.debug("classic factory");
					sessionFactory = cfg.buildSessionFactory();
				}
			} catch (Exception a_th) {
				// log.error(a_th);
				throw new HibernateException("Could not initialize the Hibernate configuration : " + a_th.getMessage(), a_th);
			}
		}
	}

	public static void close() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
		sessionFactory = null;
	}

	public static String getUserName() {
		return cfg.getProperty("connection.username");
	}

	public static String getPassword() {
		return cfg.getProperty("connection.password");
	}

	public static String getDriver() {
		return cfg.getProperty("connection.driver_class");
	}

	public static String getConnectionURL() {
		return cfg.getProperty("connection.url");
	}
}