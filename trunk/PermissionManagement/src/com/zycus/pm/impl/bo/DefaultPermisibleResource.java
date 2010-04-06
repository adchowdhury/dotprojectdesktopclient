package com.zycus.pm.impl.bo;

import com.zycus.pm.api.bo.IPermisibleResource;

public class DefaultPermisibleResource implements IPermisibleResource {
	private static final long	serialVersionUID	= 1L;
	private IPermisibleResource	resource			= null;
	private IPermisibleResource	parent				= null;
	private long				resourceID			= 0L;
	private long				resourceTypeID		= 0L;
	private String				resourceName		= null;

	public DefaultPermisibleResource() {
	}

	public DefaultPermisibleResource(IPermisibleResource a_resource) {
		resource = a_resource;
	}

	public IPermisibleResource getParent() {
		if (resource != null) {
			return resource.getParent();
		}
		return parent;
	}

	public long getResourceID() {
		if (resource != null) {
			return resource.getResourceID();
		}
		return resourceID;
	}

	public void setResourceID(long a_resourceID) {
		resourceID = a_resourceID;
	}

	public String getResourceName() {
		if (resource != null) {
			return resource.getResourceName();
		}
		return resourceName;
	}

	public long getResourceTypeID() {
		if (resource != null) {
			return resource.getResourceTypeID();
		}
		return resourceTypeID;
	}

	public void setResourceTypeID(long a_resourceTypeID) {
		resourceTypeID = a_resourceTypeID;
	}

	public int compareTo(IPermisibleResource o) {
		if (resource != null) {
			return resource.compareTo(o);
		}
		return 0;
	}

}