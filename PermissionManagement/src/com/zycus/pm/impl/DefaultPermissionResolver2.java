package com.zycus.pm.impl;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;

import java.util.List;

import com.zycus.pm.api.IPermissionResolver;
import com.zycus.pm.api.IPermissionStoreAPI;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.Permission;

public class DefaultPermissionResolver2 implements IPermissionResolver {
	private IPermissionStoreAPI	permissionStore	= null;

	/**
	 * in-case for the entity and resource is the right available
	 */
	public boolean isRightPermitted(BaseRight right, IPermissionEntity entity, IPermisibleResource resource) {
		validateNullEntries(permissionStore);

		IPermisibleResource parentResource = resource;
		IPermissionEntity parentEntity = entity;

		List<Permission> rights = permissionStore.getRights(parentEntity, parentResource);
		while (parentResource != null) {
			if (isRightPresent(rights, right)) {// means this entity is assigned
				// the right
				return isRightAllowedByPriority(right, rights);
			} else {
				while (parentEntity != null) {
					if (isRightPresent(rights, right)) {
						// means this entity is assigned the right
						return isRightAllowedByPriority(right, rights);
					}
					// increase the parent entity
					parentEntity = parentEntity.getParent();
					rights = permissionStore.getRights(parentEntity, parentResource);
				}
			}
			/*
			 * if parent is present and the parent type is not as sane as
			 * currentResourceType then just ignore the parent
			 */
			if (parentResource.getParent() != null && 
					parentResource.getResourceTypeID() != parentResource.getParent().getResourceTypeID()) {
				break;
			}
			parentResource = parentResource.getParent();
			rights = permissionStore.getRights(parentEntity, parentResource);
		}
		// when nothing is found this is simple can be taken up
		return right.getDefaultIsAllowed();
	}

	private boolean isRightPresent(List<Permission> rights, BaseRight right) {
		if (rights == null || rights.size() < 1) {
			return false;
		}
		for (Permission pMaster : rights) {
			if (pMaster.getRight().equals(right)) {
				return true;
			}
		}
		return false;
	}

	private Permission getPermission(List<Permission> rights, BaseRight right) {
		if (rights == null || rights.size() < 1) {
			return null;
		}
		for (Permission pMaster : rights) {
			if (pMaster.getRight().equals(right)) {
				return pMaster;
			}
		}
		return null;
	}

	/**
	 * assuming that lowest value is highest right
	 */
	private boolean isRightAllowedByPriority(BaseRight right, List<Permission> rights) {
		if (right == null) {
			throw new IllegalStateException("BaseRight is null");
		}
		List<BaseRight> baseRights = right.getGroup().getBaseRights();
		if (baseRights.contains(right) == false) {
			throw new IllegalArgumentException("Invalid right provided");
		}
		int highestPriority = right.getPriority();
		Permission currentHighestRight = getPermission(rights, right);
		for (int iCounter = 0; iCounter < baseRights.size(); iCounter++) {
			BaseRight bRight = (BaseRight) baseRights.get(iCounter);
			// in-case this right is not assigned, ignore this one
			if (isRightPresent(rights, right) == false) {
				continue;
			}
			if (highestPriority > bRight.getPriority()) {
				highestPriority = bRight.getPriority();
				currentHighestRight = getPermission(rights, bRight);
			}
		}
		return currentHighestRight.isAllowed();
	}

	public void setPermissionStore(IPermissionStoreAPI a_permissionStore) {
		permissionStore = a_permissionStore;
	}

}