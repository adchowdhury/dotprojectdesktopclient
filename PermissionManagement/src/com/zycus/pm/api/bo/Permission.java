package com.zycus.pm.api.bo;

import java.io.Serializable;

import com.zycus.pm.impl.bo.DefaultPermisibleResource;
import com.zycus.pm.impl.bo.DefaultPermissionEntity;

public class Permission implements Comparable<Permission>, Serializable {

	private static final long			serialVersionUID	= 1L;
	private DefaultPermissionEntity		entity				= null;
	private DefaultPermisibleResource	resource			= null;
	private BaseRight					right				= null;
	private boolean						isAllowed			= false;
	private boolean						isInherited			= true;
	private long						permissionMasterPK	= 0L;

	public long getPermissionMasterPK() {
		return permissionMasterPK;
	}

	public void setPermissionMasterPK(long permissionMasterPK) {
		this.permissionMasterPK = permissionMasterPK;
	}

	public IPermissionEntity getEntity() {
		return entity;
	}

	public void setEntity(IPermissionEntity a_entity) {
		entity = new DefaultPermissionEntity(a_entity);
	}

	public IPermisibleResource getResource() {
		return resource;
	}

	public void setResource(IPermisibleResource a_resource) {
		resource = new DefaultPermisibleResource(a_resource);
	}

	public BaseRight getRight() {
		return right;
	}

	public void setRight(BaseRight right) {
		this.right = right;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public boolean getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public boolean isInherited() {
		return isInherited;
	}

	public boolean getIsInherited() {
		return isInherited;
	}

	public void setInherited(boolean isInheritable) {
		this.isInherited = isInheritable;
	}

	public void setIsInherited(boolean isInheritable) {
		this.isInherited = isInheritable;
	}

	public int compareTo(Permission o) {
		// TODO Auto-generated method stub
		return 0;
	}

}