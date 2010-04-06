package com.zycus.pm.script.utilities;

import com.zycus.pm.api.bo.IPermisibleResource;
@SuppressWarnings("PMD")
public class PMScriptsUtility {

	public static void assignRight(int assignmentId, int entityId, int entityTypeId, String resourceName,
			String rightGroupName, String rightName, boolean isAllowed) {
		assignRight(assignmentId, entityId, entityTypeId, resourceName, rightGroupName, rightName, isAllowed, false);
	}

	public static void assignRight(int assignmentId, int entityId, int entityTypeId, String resourceName,
			String rightGroupName, String rightName, boolean isAllowed, boolean isInherited) {
		IPermisibleResource resource = ResourceUtility.getResource(resourceName);
		String sql = "insert into PM_PERMISSION_MASTER(PERMISSION_MASTER_PK,RESOURCE_ID,RESOURCE_TYPE_ID,ENTITY_ID,ENTITY_TYPE_ID,BASE_RIGHT_ID,IS_ALLOWED,IS_INHERITED) values ("
				+ assignmentId
				+ ","
				+ resource.getResourceID()
				+ ","
				+ resource.getResourceTypeID()
				+ ","
				+ entityId
				+ ","
				+ entityTypeId
				+ ","
				+ RightsUtility.getRightId(rightGroupName, rightName)
				+ ","
				+ (isAllowed ? 1 : 0) + "," + (isInherited ? 1 : 0) + ")";
		System.out.println(sql);
	}
}