package com.zycus.dotproject.impl;

import static com.zycus.dotproject.impl.ProjectManagerImpl.initCustomFields;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.zycus.dotproject.api.IUserManager;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.exception.GenericException;
import com.zycus.dotproject.persistence.db.hibernate.BaseHibernateDataHandler;
import com.zycus.dotproject.persistence.db.hibernate.HibernateSessionFactory;

public class UserManagerImpl extends BaseHibernateDataHandler implements IUserManager {

	
	public List<BOUser> getAllUsers() {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(BOUser.class);
			session.flush();
			
			return criteria.list();
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	public BOUser getUser(String userName, String password) {
		//initCustomFields();
		SessionFactory sessionFactory = null;
		Session session = null;
		BOUser user = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			Query query = session.createQuery("from BOUser as user where user.loginName = :username and user_password = MD5(:password)");
			query.setString("username", userName);
			query.setString("password", password);
			List<BOUser> lstUsers = query.list();
			if(lstUsers != null && lstUsers.size() > 0) {
				user = lstUsers.get(0);
			}
		} catch (Exception a_th) {
			throw new GenericException("Database problem", a_th, GenericException.Type.Database);
		} finally {
			close(sessionFactory, session);
		}
		return user;
	}
	
	public void saveUser(BOUser userSaving, BOUser userToBeSaved) 
	{
		performDBAction(userToBeSaved, DBActionCriteria.Update);
	}
	
	public void deleteUser(BOUser userDeleting, BOUser userToBeDeleted) 
	{
		performDBAction(userToBeDeleted, DBActionCriteria.Delete);
	}

	public List<BOUser> getUsers(String strIds) {
		List<Long> ids = new ArrayList<Long>();
		for(String id : strIds.split(",")) {
			ids.add(Long.parseLong(id));
		}
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(BOUser.class);
			
			criteria.add(Restrictions.in("userID", ids));
			return criteria.list();
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}
}