package com.zycus.pm.db;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.Permission;
import com.zycus.pm.api.bo.RightsGroup;
import com.zycus.pm.impl.bo.AssignedRightsGroup;
import com.zycus.pm.impl.bo.DefaultPermisibleResource;
import com.zycus.pm.impl.bo.DefaultPermissionEntity;

public class PermissionManagementDBHandler extends BaseHibernateDataHandler {

	public void upsertRightsGroup(RightsGroup rightsGroup) {
		validateNullEntries(rightsGroup);
		performDBAction(rightsGroup, DBActionCriteria.Upsert);
	}

	public void deleteRightsGroup(RightsGroup rightsGroup) {
		validateNullEntries(rightsGroup);
		performDBAction(rightsGroup, DBActionCriteria.Delete);
	}

	public void upsertBaseRight(BaseRight baseRight) {
		validateNullEntries(baseRight);
		performDBAction(baseRight, DBActionCriteria.Upsert);
	}

	public void deleteBaseRight(BaseRight baseRight) {
		validateNullEntries(baseRight);
		performDBAction(baseRight, DBActionCriteria.Delete);
	}
	
	public RightsGroup getRightGroup(String groupName) {
		validateNullEntries(groupName);
		SessionFactory sessionFactory = null;
		Session session = null;
		RightsGroup group = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(RightsGroup.class);
			c.add(Restrictions.eq("groupName", groupName));
			List<RightsGroup> lstRights = c.list();
			if(lstRights != null && lstRights.size() > 0) {
				group = lstRights.get(0);
			}
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
		return group;
	}
	
	public RightsGroup getRightGroup(long groupID) {
		SessionFactory sessionFactory = null;
		Session session = null;
		RightsGroup group = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(RightsGroup.class);
			c.add(Restrictions.idEq(groupID));
			List<RightsGroup> lstRights = c.list();
			if(lstRights != null && lstRights.size() > 0) {
				group = lstRights.get(0);
			}
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
		return group;
	}
	
	public BaseRight getBaseRight(long baseRightID) {
		SessionFactory sessionFactory = null;
		Session session = null;
		BaseRight baseRight = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(BaseRight.class);
			c.add(Restrictions.idEq(baseRightID));
			List<BaseRight> lstRights = c.list();
			if(lstRights != null && lstRights.size() > 0) {
				baseRight = lstRights.get(0);
			}
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
		return baseRight;
	}
	
	///////////////////////////////////////////////////
	
	public void upsertAssignedRightGroup(AssignedRightsGroup assignGroup) {
		validateNullEntries(assignGroup);
		performDBAction(assignGroup, DBActionCriteria.Upsert);
	}
	
	public List<AssignedRightsGroup> getAllAssociatedGroups(IPermisibleResource resource, RightsGroup group) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(AssignedRightsGroup.class);
			if(resource != null) {
				c.add(Restrictions.eq("resourceTypeID", resource.getResourceTypeID()));	
			}
			
			if(group != null) {
				c.add(Restrictions.eq("rightGroup", group));
			}
			
			return c.list();
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}
	
	public AssignedRightsGroup getAssociatedGroup(IPermisibleResource resource, RightsGroup group) {
		List<AssignedRightsGroup> lstGrps = getAllAssociatedGroups(resource, group);
		AssignedRightsGroup aRightsGroup = null;
		if(lstGrps != null && lstGrps.size() > 0) {
			aRightsGroup = lstGrps.get(0);
		}
		return aRightsGroup;
	}	
	
	/////////////////////////////////////////////////////////////////
	
	public void upsertPermissionMaster(Permission permissionMaster) {
		validateNullEntries(permissionMaster);
		performDBAction(permissionMaster, DBActionCriteria.Upsert);
	}
	
	public List<Permission> getAllPermissionMaster(BaseRight right, IPermissionEntity entity, IPermisibleResource resource) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Permission.class);
			if(right != null) {
				criteria.add(Restrictions.eq("right", right));
			}
			if(resource != null) {
				DefaultPermisibleResource defaultResource = new DefaultPermisibleResource(resource);
				criteria.add(Restrictions.eq("resource", defaultResource));
			}
			if(entity != null) {
				DefaultPermissionEntity defaultEntity = new DefaultPermissionEntity(entity);
				criteria.add(Restrictions.eq("entity", defaultEntity));
			}
			return criteria.list();		
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}
}