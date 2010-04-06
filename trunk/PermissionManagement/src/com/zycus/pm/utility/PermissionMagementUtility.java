package com.zycus.pm.utility;

import com.zycus.pm.api.IPermissionManagerAPI;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.excpection.PermissionAccessException;
import com.zycus.pm.factory.PermissionAPIsFactory;

public class PermissionMagementUtility {
	private static IPermissionManagerAPI	permissionManager	= PermissionAPIsFactory.getPermissionManagerAPI();

	public static boolean isRightPermitted(IPermissionEntity entity, IPermisibleResource resource, BaseRight right) throws PermissionAccessException {
		return permissionManager.getRight(entity, resource).isRightPermitted(right);
	}
}