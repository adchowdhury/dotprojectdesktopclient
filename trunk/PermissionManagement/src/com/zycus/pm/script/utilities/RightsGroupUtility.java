package com.zycus.pm.script.utilities;

import java.util.HashMap;
import java.util.Map;

import com.zycus.pm.api.bo.IPermisibleResource;

public class RightsGroupUtility {

	public static Map<String, Integer>	rightGroups	= new HashMap<String, Integer>();

	public static int createRightGroup(int groupId, String groupName) {
		String sql = "insert into PM_RIGHT_GROUP(RIGHT_ID,RIGHT_NAME) values (" + groupId + ",'" + groupName + "')";
		System.out.println(sql);
		rightGroups.put(groupName, groupId);
		return groupId;
	}

	public static int getRightGroupId(String groupName) {
		return rightGroups.get(groupName);
	}

	public static void associateRightGroupToResource(int associationId, String groupName, String resourceName) {

		int groupId = rightGroups.get(groupName);
		IPermisibleResource resource = ResourceUtility.getResource(resourceName);
		String sql = "insert into PM_RIGHTS_RESOURCE_RELATION(RELATION_PK,RESOURCE_ID,RESOURCE_TYPE_ID,RIGHT_GROUP_ID) values ("
				+ associationId
				+ ","
				+ resource.getResourceID()
				+ ","
				+ resource.getResourceTypeID()
				+ ","
				+ groupId
				+ ")";
		System.out.println(sql);
	}
}
