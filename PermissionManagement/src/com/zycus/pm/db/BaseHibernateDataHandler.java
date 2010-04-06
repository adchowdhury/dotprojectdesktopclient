package com.zycus.pm.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class BaseHibernateDataHandler {

	public static enum DBActionCriteria {
		Upsert, Delete
	}

	public void performDBAction(Object objToSave, DBActionCriteria criteria) {

		SessionFactory sessionFactory = null;
		Session session = null;

		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			
			performDBAction(objToSave, criteria, session);

		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	public void performDBAction(Object objToSave, DBActionCriteria criteria, Session session) {

		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			if (criteria == DBActionCriteria.Upsert) {
				session.saveOrUpdate(objToSave);
			} else if (criteria == DBActionCriteria.Delete) {
				session.delete(objToSave);
			}

			transaction.commit();

		} catch (Exception a_th) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(a_th);
		}
	}

	public static void close(SessionFactory sessionFactory, Session session) {
		if (session != null) {
			session.close();
		}
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
	
	public static void close(SessionFactory sessionFactory) {
		close(sessionFactory, null);
	}

	public static void close(Session session) {
		close(null, session);
	}
}