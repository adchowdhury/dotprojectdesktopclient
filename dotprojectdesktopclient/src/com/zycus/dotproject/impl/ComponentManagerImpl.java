package com.zycus.dotproject.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.zycus.dotproject.api.ICompanyManager;
import com.zycus.dotproject.bo.BOCompany;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.persistence.db.hibernate.BaseHibernateDataHandler;
import com.zycus.dotproject.persistence.db.hibernate.HibernateSessionFactory;

public class ComponentManagerImpl extends BaseHibernateDataHandler implements ICompanyManager  {

	/* (non-Javadoc)
	 * @see com.zycus.dotproject.impl.ICompanyManager#getAllCompanies(com.zycus.dotproject.bo.BOUser, com.zycus.dotproject.bo.BOCompany)
	 */
	public List<BOCompany> getAllCompanies(BOUser user) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			
			
			if(user.getUserType()==UserType.Administrator)
			{
				Criteria criteria = session.createCriteria(BOCompany.class);
				return criteria.list();
			}
			
			else
			{
				ArrayList<BOCompany> companyList = new ArrayList<BOCompany>();
				companyList.add(user.getContact().getContactCompany());
				return companyList;
			}
			
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}


	/* (non-Javadoc)
	 * @see com.zycus.dotproject.impl.ICompanyManager#deleteCompany(com.zycus.dotproject.bo.BOUser, com.zycus.dotproject.bo.BOCompany)
	 */
	public void deleteCompany(BOUser user, BOCompany company) {
		
		if(user.getUserType()==UserType.Administrator)
		{
			performDBAction(company, DBActionCriteria.Delete);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.zycus.dotproject.impl.ICompanyManager#saveCompany(com.zycus.dotproject.bo.BOUser, com.zycus.dotproject.bo.BOCompany)
	 */
	public void saveCompany(BOUser user, BOCompany company) 
	{
		if(user.getUserType()==UserType.Administrator)
		{
			performDBAction(company, DBActionCriteria.Update);
		}
	}
	
}