package com.zycus.pm.impl;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;

import com.zycus.pm.api.IPermissionResolver;
import com.zycus.pm.api.IPermissionRights;
import com.zycus.pm.api.IPermissionRightsAPI;
import com.zycus.pm.api.IPermissionStoreAPI;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.RightsGroup;

public class DefaultPermissionRights implements IPermissionRights {

	private static final long		serialVersionUID	= 1L;
	private IPermissionResolver		permissionResolver	= null;
	private IPermissionEntity		entity				= null;
	private IPermisibleResource		resource			= null;
	private IPermissionStoreAPI		permissionStore		= null;
	private IPermissionRightsAPI	permissionRights	= null;

	public DefaultPermissionRights(IPermissionEntity a_entity, IPermisibleResource a_resource) {
		permissionRights = new PermissionRightsImpl();
		entity = a_entity;
		resource = a_resource;
		permissionStore = new PermissionStoreImpl();
	}

	void setPermissionResolver(IPermissionResolver resolver) {
		if (resolver == null) {
			throw new NullPointerException("IPermissionResolver can't be null");
		}
		permissionResolver = resolver;
		permissionResolver.setPermissionStore(permissionStore);
	}

	public boolean isRightPermitted(BaseRight right) {
		validateNullEntries(right, entity, resource);
		return permissionResolver.isRightPermitted(right, entity, resource);
	}

	public boolean isRightPermitted(String strRightName, String groupName) {
		return isRightPermitted(permissionRights.getRightsGroup(groupName).getBaseRight(strRightName));
	}

	public boolean isRightPermitted(String strRightName, long groupID) {
		return isRightPermitted(permissionRights.getRightsGroup(groupID).getBaseRight(strRightName));
	}

	public boolean isRightPermitted(String strRightName, RightsGroup group) {
		return isRightPermitted(group.getBaseRight(strRightName));
	}

}