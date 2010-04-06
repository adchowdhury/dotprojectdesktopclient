package com.zycus.pm.impl;
import static com.zycus.pm.utility.NullValidator.validateNullEntries;

import java.util.List;

import com.zycus.pm.api.IPermissionStoreAPI;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.Permission;
import com.zycus.pm.db.PermissionManagementDBHandler;

public class PermissionStoreImpl implements IPermissionStoreAPI {
	private PermissionManagementDBHandler	dbHandler	= new PermissionManagementDBHandler();
	
	public List<Permission> getRights(IPermissionEntity entity, IPermisibleResource resource) {
		validateNullEntries(entity, resource);
		List<Permission> allPermissionMasters = dbHandler.getAllPermissionMaster(null, entity, resource);
		for(Permission pMaster : allPermissionMasters) {
			pMaster.setEntity(entity);
			pMaster.setResource(resource);
		}
		return allPermissionMasters;
	}
}