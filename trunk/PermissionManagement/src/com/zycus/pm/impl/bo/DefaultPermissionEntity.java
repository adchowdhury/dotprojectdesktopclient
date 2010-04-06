package com.zycus.pm.impl.bo;

import com.zycus.pm.api.bo.IPermissionEntity;

public class DefaultPermissionEntity implements IPermissionEntity {
	private static final long	serialVersionUID	= 1L;
	private long				entityID			= 0L;
	private long				entityTypeID		= 0L;
	private IPermissionEntity	entity				= null;

	public DefaultPermissionEntity() {
		// TODO Auto-generated constructor stub
	}

	public DefaultPermissionEntity(IPermissionEntity a_entity) {
		entity = a_entity;
	}

	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}

	public void setEntityTypeID(long entityTypeID) {
		this.entityTypeID = entityTypeID;
	}

	public long getEntityID() {
		if (entity != null) {
			return entity.getEntityID();
		}
		return entityID;
	}

	public String getEntityName() {
		if (entity != null) {
			return entity.getEntityName();
		}
		return null;
	}

	public long getEntityTypeID() {
		if (entity != null) {
			return entity.getEntityTypeID();
		}
		return entityTypeID;
	}

	public IPermissionEntity getParent() {
		if (entity != null) {
			entity.getParent();
		}
		return null;
	}

	public int compareTo(IPermissionEntity o) {
		if (entity != null) {
			return entity.compareTo(o);
		}
		return 0;
	}

}