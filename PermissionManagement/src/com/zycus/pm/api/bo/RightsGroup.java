package com.zycus.pm.api.bo;
import static com.zycus.pm.utility.NullValidator.validateNullEntries;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RightsGroup implements Comparable<RightsGroup>, Serializable {

	private static final long	serialVersionUID	= 1L;
	private List<BaseRight>		baseRights			= null;
	private long				groupID				= 0L;
	private String				groupName			= null;
	
	public RightsGroup() {
		
	}
	
	public RightsGroup(String a_groupName) {
		validateNullEntries(a_groupName);
		if(a_groupName.trim().length() == 0) {
			throw new NullPointerException("Name can't be null");
		}
		
		groupName = a_groupName;
		baseRights = new ArrayList<BaseRight>();
	}
	
	public BaseRight getBaseRight(String strBaseRightName) {
		for(BaseRight right : baseRights) {
			if(right.getRightDescription().equals(strBaseRightName)) {
				return right;
			}
		}
		return null;
	}

	public long getGroupID() {
		return groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int compareTo(RightsGroup o) {
		return 0;
	}

	public List<BaseRight> getBaseRights() {
		return baseRights;
	}

	public void setBaseRights(List<BaseRight> baseRights) {
		this.baseRights = baseRights;
	}

	public void addBaseRights(BaseRight baseRight) {
		if (baseRights == null) {
			baseRights = new ArrayList<BaseRight>();
		}
		if (baseRights.contains(baseRight)) {
			return;
		}
		baseRights.add(baseRight);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof RightsGroup)) {
			return false;
		}
		return getGroupID() == ((RightsGroup)obj).getGroupID();
	}
	
	@Override
	public String toString() {
		return groupName;
	}
}