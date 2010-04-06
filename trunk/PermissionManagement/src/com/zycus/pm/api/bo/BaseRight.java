package com.zycus.pm.api.bo;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;
import java.io.Serializable;

public class BaseRight implements Serializable, Comparable<BaseRight> {

	private static final long	serialVersionUID	= 1L;

	private RightsGroup			group				= null;
	private int					priority			= 0;
	private String				rightDescription	= null;
	private boolean				defaultIsAllowed	= false;
	private long				baseRightID			= 0L;

	public long getBaseRightID() {
		return baseRightID;
	}

	public void setBaseRightID(long baseRightID) {
		this.baseRightID = baseRightID;
	}

	public BaseRight() {

	}

	public BaseRight(String rightName) {
		this(rightName, 0, false);
	}

	public BaseRight(String rightName, int priority) {
		this(rightName, priority, false);
	}

	public BaseRight(String rightName, boolean defaultIsAllowed) {
		this(rightName, 0, defaultIsAllowed);
	}

	public BaseRight(String rightName, int priority, boolean defaultIsAllowed) {
		validateNullEntries(rightName);

		rightDescription = rightName;
	}

	public void setDefaultIsAllowed(boolean defaultIsAllowed) {
		this.defaultIsAllowed = defaultIsAllowed;
	}

	public boolean getDefaultIsAllowed() {
		return defaultIsAllowed;
	}

	public String getRightDescription() {
		return rightDescription;
	}

	public void setRightDescription(String rightDescription) {
		this.rightDescription = rightDescription;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public RightsGroup getGroup() {
		return group;
	}

	public void setGroup(RightsGroup group) {
		this.group = group;
	}

	public int compareTo(BaseRight o) {
		return 0;
	}

	@Override
	public String toString() {
		return rightDescription;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BaseRight)) {
			return false;
		}

		return ((BaseRight) obj).getRightDescription().equals(getRightDescription());
	}
}