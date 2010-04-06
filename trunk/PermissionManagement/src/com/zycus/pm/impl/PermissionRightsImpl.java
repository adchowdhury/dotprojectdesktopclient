package com.zycus.pm.impl;

import static com.zycus.pm.utility.NullValidator.validateNullEntries;
import java.util.Map;

import com.zycus.pm.api.IPermissionRightsAPI;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.RightsGroup;
import com.zycus.pm.db.PermissionManagementDBHandler;
import com.zycus.pm.excpection.DuplicateNameException;
import com.zycus.pm.excpection.DuplicateNameException.DuplicateNameType;

public class PermissionRightsImpl implements IPermissionRightsAPI {
	private PermissionManagementDBHandler	dbHandler	= new PermissionManagementDBHandler();

	public BaseRight addRight(String groupName, String rightName) throws DuplicateNameException {
		return addRight(groupName, rightName, 0);
	}

	public BaseRight addRight(String groupName, String rightName, int priority) throws DuplicateNameException {
		return addRight(groupName, rightName, priority, false);
	}

	public BaseRight addRight(String groupName, String rightName, int priority, boolean defaultAllowed)
			throws DuplicateNameException {
		validateNullEntries(groupName, rightName);
		RightsGroup group = dbHandler.getRightGroup(groupName);
		if (group == null) {
			group = new RightsGroup(groupName);
		}
		return addRight(group, rightName, priority, defaultAllowed);
	}

	public BaseRight addRight(RightsGroup group, String rightName, int priority, boolean defaultAllowed)
			throws DuplicateNameException {
		validateNullEntries(group, rightName);
		BaseRight baseRight = null;

		for (BaseRight right : group.getBaseRights()) {
			if (right.getRightDescription().equals(rightName)) {
				baseRight = right;
				break;
			}
		}

		if (baseRight == null) {
			baseRight = new BaseRight(rightName);
			baseRight.setGroup(group);
			baseRight.setPriority(priority);
			baseRight.setDefaultIsAllowed(defaultAllowed);

			dbHandler.upsertBaseRight(baseRight);
		}

		return baseRight;
	}

	public RightsGroup addRights(String groupName, String... rightNames) throws DuplicateNameException {
		RightsGroup group = dbHandler.getRightGroup(groupName);
		if (group == null) {
			group = new RightsGroup(groupName);
		}
		addRights(group, rightNames);
		return group;
	}

	public RightsGroup addRights(String groupName, Map<String, Integer> rights) throws DuplicateNameException {
		RightsGroup group = dbHandler.getRightGroup(groupName);
		if (group == null) {
			group = new RightsGroup(groupName);
		}
		addRights(group, rights);
		return group;
	}

	public void addRights(RightsGroup group, String... rightNames) throws DuplicateNameException {
		for (String rightName : rightNames) {
			BaseRight right = group.getBaseRight(rightName);
			if (right == null) {
				right = new BaseRight(rightName);
				right.setGroup(group);
				group.addBaseRights(right);
			}
		}
		dbHandler.upsertRightsGroup(group);
	}

	public void addRights(RightsGroup group, Map<String, Integer> rights) throws DuplicateNameException {
		for (String rightName : rights.keySet()) {
			BaseRight right = group.getBaseRight(rightName);
			if (right == null) {
				right = new BaseRight(rightName);
				right.setGroup(group);
				group.addBaseRights(right);
			}
			right.setPriority(rights.get(rightName));
		}
		dbHandler.upsertRightsGroup(group);
	}

	public void deleteBaseRight(long baseRightID) {
		deleteBaseRight(dbHandler.getBaseRight(baseRightID));
	}

	public void deleteBaseRight(String baseRightName, RightsGroup group) {
		deleteBaseRight(group.getBaseRight(baseRightName));
	}

	public void deleteBaseRight(String baseRightName, String groupName) {
		deleteBaseRight(dbHandler.getRightGroup(groupName).getBaseRight(baseRightName));
	}

	public void deleteBaseRight(BaseRight right) {
		validateNullEntries(right);
		dbHandler.deleteBaseRight(right);
	}

	public void deleteRightsGroup(long groupID) {
		deleteRightsGroup(getRightsGroup(groupID));

	}

	public void deleteRightsGroup(String groupName) {
		validateNullEntries(groupName);
		deleteRightsGroup(getRightsGroup(groupName));
	}

	public void deleteRightsGroup(RightsGroup group) {
		validateNullEntries(group);
		dbHandler.deleteRightsGroup(group);
	}

	public BaseRight getBaseRight(long baseRightID) {
		return dbHandler.getBaseRight(baseRightID);
	}

	public BaseRight getBaseRight(String groupName, String baseRightName) {
		RightsGroup rightsGroup = getRightsGroup(groupName);
		if (rightsGroup != null)
			return rightsGroup.getBaseRight(baseRightName);
		else
			return null;
	}

	public BaseRight getBaseRight(long groupID, String baseRightName) {
		return dbHandler.getRightGroup(groupID).getBaseRight(baseRightName);
	}

	public RightsGroup getRightsGroup(long groupID) {
		return dbHandler.getRightGroup(groupID);
	}

	public RightsGroup getRightsGroup(String groupName) {
		validateNullEntries(groupName);
		return dbHandler.getRightGroup(groupName);
	}

	public void updateBaseRight(BaseRight baseRight) throws DuplicateNameException {
		validateNullEntries(baseRight);
		dbHandler.upsertBaseRight(baseRight);
	}

	public BaseRight updateBaseRightDefaultAllowed(long baseRightID, boolean defaultIsAllowed) {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		baseRight.setDefaultIsAllowed(defaultIsAllowed);
		return baseRight;
	}

	public BaseRight updateBaseRightName(long baseRightID, String baseRightName) throws DuplicateNameException {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		if (baseRight.getRightDescription().equals(baseRightName)) {
			return baseRight;
		}
		if (baseRight.getGroup().getBaseRight(baseRightName) != null) {
			throw new DuplicateNameException("Right name can't be duplicate", DuplicateNameType.DuplicateRightName);
		}
		baseRight.setRightDescription(baseRightName);
		dbHandler.upsertBaseRight(baseRight);
		return baseRight;
	}

	public BaseRight updateBaseRightNameDefaultAllowed(long baseRightID, String baseRightName, boolean defaultIsAllowed)
			throws DuplicateNameException {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		if (baseRight.getRightDescription().equals(baseRightName)) {
			return baseRight;
		}
		if (baseRight.getGroup().getBaseRight(baseRightName) != null) {
			throw new DuplicateNameException("Right name can't be duplicate", DuplicateNameType.DuplicateRightName);
		}
		baseRight.setRightDescription(baseRightName);
		baseRight.setDefaultIsAllowed(defaultIsAllowed);
		dbHandler.upsertBaseRight(baseRight);
		return baseRight;
	}

	public BaseRight updateBaseRightNamePriority(long baseRightID, String baseRightName, int priority)
			throws DuplicateNameException {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		if (baseRight.getRightDescription().equals(baseRightName)) {
			return baseRight;
		}
		if (baseRight.getGroup().getBaseRight(baseRightName) != null) {
			throw new DuplicateNameException("Right name can't be duplicate", DuplicateNameType.DuplicateRightName);
		}
		baseRight.setRightDescription(baseRightName);
		baseRight.setPriority(priority);
		dbHandler.upsertBaseRight(baseRight);
		return baseRight;
	}

	public BaseRight updateBaseRightNamePriorityDefaultAllowed(long baseRightID, String baseRightName, int priority,
			boolean defaultIsAllowed) throws DuplicateNameException {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		if (baseRight.getRightDescription().equals(baseRightName)) {
			return baseRight;
		}
		if (baseRight.getGroup().getBaseRight(baseRightName) != null) {
			throw new DuplicateNameException("Right name can't be duplicate", DuplicateNameType.DuplicateRightName);
		}
		baseRight.setRightDescription(baseRightName);
		baseRight.setPriority(priority);
		baseRight.setDefaultIsAllowed(defaultIsAllowed);
		dbHandler.upsertBaseRight(baseRight);
		return baseRight;
	}

	public BaseRight updateBaseRightPriority(long baseRightID, int priority) {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		baseRight.setPriority(priority);
		return baseRight;
	}

	public BaseRight updateBaseRightPriorityDefaultAllowed(long baseRightID, int priority, boolean defaultIsAllowed)
			throws DuplicateNameException {
		BaseRight baseRight = dbHandler.getBaseRight(baseRightID);
		baseRight.setPriority(priority);
		baseRight.setDefaultIsAllowed(defaultIsAllowed);
		return baseRight;
	}

	public void updateRightGroup(RightsGroup group) throws DuplicateNameException {
		dbHandler.upsertRightsGroup(group);
	}

	public RightsGroup updateRightsGroupName(long groupID, String a_groupName) throws DuplicateNameException {
		RightsGroup group = dbHandler.getRightGroup(groupID);
		RightsGroup groupName = dbHandler.getRightGroup(a_groupName);
		if (groupName != null && groupName.equals(group) == false) {
			throw new DuplicateNameException("Group name can't be duplicate", DuplicateNameType.DuplicateRightName);
		}
		group.setGroupName(a_groupName);
		dbHandler.upsertRightsGroup(group);
		return group;
	}
}