package com.zycus.dotproject.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.zycus.dotproject.api.IDepartmentManager;
import com.zycus.dotproject.bo.BODepartment;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.persistence.db.hibernate.BaseHibernateDataHandler;
import com.zycus.dotproject.persistence.db.hibernate.HibernateSessionFactory;

public class DepartmentManagerImpl extends BaseHibernateDataHandler implements IDepartmentManager  {

	
	/* (non-Javadoc)
	 * @see com.zycus.dotproject.impl.IDepartmentManager#getAllDepartments(com.zycus.dotproject.bo.BOUser)
	 */
	public List<BODepartment> getAllDepartments(BOUser user) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			
			
			if(user.getUserType()==UserType.Administrator)
			{
				Criteria criteria = session.createCriteria(BODepartment.class);
				return criteria.list();
			}
			
			else
			{
				ArrayList<BODepartment> departmentList = new ArrayList<BODepartment>();
				departmentList.addAll(user.getContact().getContactCompany().getDepartments());
				return departmentList;
			}
			
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}


	/* (non-Javadoc)
	 * @see com.zycus.dotproject.impl.IDepartmentManager#deleteDepartment(com.zycus.dotproject.bo.BOUser, com.zycus.dotproject.bo.BODepartment)
	 */
	public void deleteDepartment(BOUser user, BODepartment department) {
		
		if(user.getUserType()==UserType.Administrator)
		{
			performDBAction(department, DBActionCriteria.Delete);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.zycus.dotproject.impl.IDepartmentManager#saveDepartment(com.zycus.dotproject.bo.BOUser, com.zycus.dotproject.bo.BODepartment)
	 */
	public void saveDepartment(BOUser user, BODepartment department) 
	{
		if(user.getUserType()==UserType.Administrator)
		{
			performDBAction(department, DBActionCriteria.Update);
		}
	}
	
}