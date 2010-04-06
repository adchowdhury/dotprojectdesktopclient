package com.zycus.pm.impl;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;

import java.util.List;

import com.zycus.pm.api.IPermissionResolver;
import com.zycus.pm.api.IPermissionStoreAPI;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.Permission;

public class DefaultPermissionResolver implements IPermissionResolver {
	private IPermissionStoreAPI	permissionStore	= null;

	/**
	 * in-case for the entity and resource is the right available
	 */
	public boolean isRightPermitted(BaseRight right, IPermissionEntity entity, IPermisibleResource resource) {
		validateNullEntries(permissionStore);

		Permission answer = null;
		IPermissionEntity currentEntity = entity;
		IPermisibleResource currentResource = resource;

		while (currentEntity != null) {
			while (currentResource != null) {
				if ((answer = getRight(currentEntity, currentResource, right)) == null) {
					// answer = checkForAnyLevel(right, entity, resource);
				}
				IPermisibleResource parentResource = currentResource.getParent();
				currentResource = (parentResource == null) ? currentResource : parentResource;
				if (answer != null || parentResource == null) {
					break;
				}
			}

			if (answer != null) {
				break;
			} else {
				IPermissionEntity parentEntity = currentEntity.getParent();
				currentEntity = (parentEntity == null) ? currentEntity : parentEntity;
				if (parentEntity == null) {
					break;
				}
			}
		}

		if (answer != null) {
			return getHighestPriorityRight(right, permissionStore.getRights(currentEntity, currentResource))
					.getIsAllowed();
		} else {
			return right.getDefaultIsAllowed();
		}

	}

	private Permission getRight(IPermissionEntity entity, IPermisibleResource resource, BaseRight rightToCheck) {
		List<Permission> rights = permissionStore.getRights(entity, resource);
		if (rights == null || rights.size() < 1) {
			return null;
		}
		for (Permission pMaster : rights) {
			if (pMaster.getRight().equals(rightToCheck)) {
				return pMaster;
			}
		}
		return null;
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
	private Permission getHighestPriorityRight(BaseRight right, List<Permission> rights) {
		if (right == null) {
			throw new IllegalStateException("BaseRight is null");
		}
		List<BaseRight> baseRights = right.getGroup().getBaseRights();
		if (baseRights.contains(right) == false) {
			throw new IllegalArgumentException("Invalid right provided");
		}
		Permission highestPriorityRight = getPermission(rights, right);
		int highestPriority = right.getPriority();
		for (int iCounter = 0; iCounter < baseRights.size(); iCounter++) {
			BaseRight bRight = (BaseRight) baseRights.get(iCounter);
			// in-case this right is not assigned, ignore this one
			if (isRightPresent(rights, right) == false) {
				continue;
			}
			if (highestPriority > bRight.getPriority()) {
				highestPriority = bRight.getPriority();
				highestPriorityRight = getPermission(rights, bRight);
			}
		}
		return highestPriorityRight;
	}

	public void setPermissionStore(IPermissionStoreAPI a_permissionStore) {
		permissionStore = a_permissionStore;
	}

}