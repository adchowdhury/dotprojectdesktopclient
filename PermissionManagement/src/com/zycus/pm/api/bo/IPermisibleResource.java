package com.zycus.pm.api.bo;

import java.io.Serializable;

public interface IPermisibleResource  extends Serializable, Comparable<IPermisibleResource>{
	long getResourceID();
	long getResourceTypeID();
	
	String getResourceName();
	
	IPermisibleResource getParent();
		
}