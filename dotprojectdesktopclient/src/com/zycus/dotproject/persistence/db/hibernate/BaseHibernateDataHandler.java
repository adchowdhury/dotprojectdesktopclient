package com.zycus.dotproject.persistence.db.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class BaseHibernateDataHandler {

	public static enum DBActionCriteria {
		Add, Update, Delete, Merge
	}

	public void performDBAction(Object objToSave, String criteria) {
		Transaction transaction = null;
		SessionFactory sessionFactory = null;
		Session session = null;

		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			if (criteria.equalsIgnoreCase("Add") || criteria.equalsIgnoreCase("Update")) {
				session.saveOrUpdate(objToSave);
			} else if (criteria.equalsIgnoreCase("Delete")) {
				session.delete(objToSave);
			}

//			transaction.commit();

		} catch (Exception a_th) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}
	
	public Object merge(Object objToSave) {

		SessionFactory sessionFactory = null;
		Session session = null;

		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Object mergedObject = session.merge(objToSave);
			return mergedObject;

		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
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

			if (criteria == DBActionCriteria.Add || criteria == DBActionCriteria.Update) {
				session.saveOrUpdate(objToSave);
			}else if (criteria == DBActionCriteria.Merge) {
				session.merge(objToSave);
			}else if (criteria == DBActionCriteria.Delete) {
				session.delete(objToSave);
			}
			
			session.flush();

			transaction.commit();

		} catch (Exception a_th) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(a_th);
		}
	}

	public static void close(SessionFactory sessionFactory) {
		close(sessionFactory, null);
	}

	public static void close(SessionFactory sessionFactory, Session session) {
		if (session != null) {
			session.close();
		}
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	public static void close(Session session) {
		close(null, session);
	}
}