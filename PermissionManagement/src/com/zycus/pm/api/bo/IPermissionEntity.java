package com.zycus.pm.api.bo;

import java.io.Serializable;

public interface IPermissionEntity extends Serializable, Comparable<IPermissionEntity> {
	
	long getEntityID();
	long getEntityTypeID();
	
	String getEntityName();

	IPermissionEntity getParent();
}