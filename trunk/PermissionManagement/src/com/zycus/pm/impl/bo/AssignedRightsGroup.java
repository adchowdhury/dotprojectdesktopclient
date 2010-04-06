package com.zycus.pm.impl.bo;

import java.io.Serializable;

import com.zycus.pm.api.bo.RightsGroup;

public class AssignedRightsGroup implements Serializable, Comparable<AssignedRightsGroup> {

	private static final long	serialVersionUID	= 1L;
	private long				relationPK			= 0L;
	private RightsGroup			rightGroup			= null;
	private long				resourceTypeID		= 0L;
	private long				resourceID			= 0L;

	public long getResourceID() {
		return resourceID;
	}

	public void setResourceID(long resourceID) {
		this.resourceID = resourceID;
	}

	public long getResourceTypeID() {
		return resourceTypeID;
	}

	public void setResourceTypeID(long a_resourceTypeID) {
		resourceTypeID = a_resourceTypeID;
	}

	public long getRelationPK() {
		return relationPK;
	}

	public void setRelationPK(long relationPK) {
		this.relationPK = relationPK;
	}

	public void setRightGroup(RightsGroup rightGroup) {
		this.rightGroup = rightGroup;
	}

	public RightsGroup getRightGroup() {
		return rightGroup;
	}

	public int compareTo(AssignedRightsGroup o) {
		return 0;
	}
}