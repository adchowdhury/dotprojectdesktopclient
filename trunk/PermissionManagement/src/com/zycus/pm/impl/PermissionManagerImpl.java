package com.zycus.pm.impl;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;

import java.util.ArrayList;
import java.util.List;

import com.zycus.pm.api.IPermissionManagerAPI;
import com.zycus.pm.api.IPermissionResolver;
import com.zycus.pm.api.IPermissionRights;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.Permission;
import com.zycus.pm.api.bo.RightsGroup;
import com.zycus.pm.db.PermissionManagementDBHandler;
import com.zycus.pm.db.BaseHibernateDataHandler.DBActionCriteria;
import com.zycus.pm.excpection.PermissionAccessException;
import com.zycus.pm.impl.bo.AssignedRightsGroup;

public class PermissionManagerImpl implements IPermissionManagerAPI {
	private PermissionManagementDBHandler	dbHandler		= new PermissionManagementDBHandler();
	private IPermissionResolver				defaultResolver	= null;

	public PermissionManagerImpl() {
		defaultResolver = new DefaultPermissionResolver();
	}

	public void setDefaultPermissionResolver(IPermissionResolver resolver) {
		validateNullEntries(resolver);
		defaultResolver = resolver;
	}

	public void associateRightGroup(IPermisibleResource resource, RightsGroup group) {
		validateNullEntries(resource, group);
		AssignedRightsGroup assignedRightsGroup = dbHandler.getAssociatedGroup(resource, group);
		if (assignedRightsGroup == null) {
			assignedRightsGroup = new AssignedRightsGroup();
			assignedRightsGroup.setResourceTypeID(resource.getResourceTypeID());
			assignedRightsGroup.setRightGroup(group);
			assignedRightsGroup.setResourceID(resource.getResourceID());
			dbHandler.upsertAssignedRightGroup(assignedRightsGroup);
		}
	}

	public List<RightsGroup> getAllAssociatedGroups(IPermisibleResource resource) {
		validateNullEntries(resource);
		List<AssignedRightsGroup> rightGroups = dbHandler.getAllAssociatedGroups(resource, null);
		List<RightsGroup> returnGroup = new ArrayList<RightsGroup>();
		for (AssignedRightsGroup aRightGroup : rightGroups) {
			returnGroup.add(aRightGroup.getRightGroup());
		}
		return returnGroup;
	}

	public void removeAllAssociations(IPermisibleResource resource) {
		validateNullEntries(resource);
		List<AssignedRightsGroup> rightGroups = dbHandler.getAllAssociatedGroups(resource, null);
		for (AssignedRightsGroup assignedGrp : rightGroups) {
			dbHandler.performDBAction(assignedGrp, DBActionCriteria.Delete);
		}
	}

	public void removeAllAssociations(RightsGroup group) {
		validateNullEntries(group);
		List<AssignedRightsGroup> rightGroups = dbHandler.getAllAssociatedGroups(null, group);
		for (AssignedRightsGroup assignedGrp : rightGroups) {
			dbHandler.performDBAction(assignedGrp, DBActionCriteria.Delete, null);
		}
	}

	public void removeAllRights(IPermissionEntity entity) throws PermissionAccessException {
		validateNullEntries(entity);
		List<Permission> lstRights = dbHandler.getAllPermissionMaster(null, entity, null);
		for (Permission permissionMaster : lstRights) {
			dbHandler.performDBAction(permissionMaster, DBActionCriteria.Delete);
		}
	}

	public void removeAllRights(IPermisibleResource resource) throws PermissionAccessException {
		validateNullEntries(resource);
		List<Permission> lstRights = dbHandler.getAllPermissionMaster(null, null, resource);
		for (Permission permissionMaster : lstRights) {
			dbHandler.performDBAction(permissionMaster, DBActionCriteria.Delete);
		}
	}

	public void removeAllRights(BaseRight right) throws PermissionAccessException {
		validateNullEntries(right);
		List<Permission> lstRights = dbHandler.getAllPermissionMaster(right, null, null);
		for (Permission permissionMaster : lstRights) {
			dbHandler.performDBAction(permissionMaster, DBActionCriteria.Delete);
		}
	}

	public void removeAssociation(IPermisibleResource resource, RightsGroup group) {
		validateNullEntries(resource, group);
		AssignedRightsGroup aRG = dbHandler.getAssociatedGroup(resource, group);
		if (aRG != null) {
			dbHandler.performDBAction(aRG, DBActionCriteria.Delete);
		}
		// TODO: in-case all assigned rights to be removed
		// for(BaseRight r : group.getBaseRights()) {
		// PermissionMaster pM = getPermissionMaster(r, null, resource);
		// dbHandler.performDBAction(pM, DBActionCriteria.Delete);
		// }
	}

	public void removeRight(BaseRight right, IPermissionEntity entity, IPermisibleResource resource)
			throws PermissionAccessException {
		List<Permission> lstRights = dbHandler.getAllPermissionMaster(right, entity, resource);
		for (Permission permissionMaster : lstRights) {
			dbHandler.performDBAction(permissionMaster, DBActionCriteria.Delete);
		}
	}

	public void removeRights(IPermissionEntity entity, IPermisibleResource resource) throws PermissionAccessException {
		validateNullEntries(entity, resource);
		removeRight(null, entity, resource);
	}

	public void upsertAllRights(RightsGroup rightGroup, IPermissionEntity entity, IPermisibleResource resource,
			boolean isAllowed) throws PermissionAccessException {
		for (BaseRight right : rightGroup.getBaseRights()) {
			upsertRight(right, entity, resource, isAllowed);
		}
	}

	public void upsertAllRights(List<BaseRight> rights, IPermissionEntity entity, IPermisibleResource resource,
			boolean isAllowed) throws PermissionAccessException {
		for (BaseRight right : rights) {
			upsertRight(right, entity, resource, isAllowed);
		}
	}

	public void upsertRight(BaseRight right, IPermissionEntity entity, IPermisibleResource resource, boolean isAllowed)
			throws PermissionAccessException {
		validateNullEntries(right, entity, resource, right.getGroup());

		AssignedRightsGroup aRightsGroup = dbHandler.getAssociatedGroup(resource, right.getGroup());
		if (aRightsGroup == null) {
			throw new PermissionAccessException("Right group is not associated with the specified resource type");
		}

		Permission permissionMaster = getPermissionMaster(right, entity, resource);
		if (permissionMaster == null) {
			permissionMaster = new Permission();
			permissionMaster.setResource(resource);
			permissionMaster.setEntity(entity);
			permissionMaster.setRight(right);
		}
		permissionMaster.setIsAllowed(isAllowed);
		dbHandler.upsertPermissionMaster(permissionMaster);
	}

	public IPermissionRights getRight(IPermissionEntity entity, IPermisibleResource resource)
			throws PermissionAccessException {
		return getRight(entity, resource, defaultResolver);
	}

	public IPermissionRights getRight(IPermissionEntity entity, IPermisibleResource resource,
			IPermissionResolver resolver) throws PermissionAccessException {
		validateNullEntries(resolver, entity, resource);
		DefaultPermissionRights defaultPR = new DefaultPermissionRights(entity, resource);
		defaultPR.setPermissionResolver(resolver);
		return defaultPR;
	}

	private Permission getPermissionMaster(BaseRight right, IPermissionEntity entity, IPermisibleResource resource) {
		List<Permission> lstRights = dbHandler.getAllPermissionMaster(right, entity, resource);
		Permission permissionMaster = null;
		if (lstRights != null && lstRights.size() > 0) {

			permissionMaster = lstRights.get(0);
			permissionMaster.setEntity(entity);
			permissionMaster.setResource(resource);
		}
		return permissionMaster;
	}
}