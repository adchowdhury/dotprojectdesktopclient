package com.zycus.pm.script.utilities;

import java.util.HashMap;
import java.util.Map;

public class RightsUtility {

	private static Map<Integer, Map<String, Integer>>	rights	= new HashMap<Integer, Map<String, Integer>>();

	public static int createRight(int rightId, String rightName, int groupId) {
		return createRight(rightId, rightName, groupId, 0, false);
	}

	public static int createRight(int rightId, String rightName, int groupId, int priority) {
		return createRight(rightId, rightName, groupId, priority, false);
	}

	public static int createRight(int rightId, String rightName, int groupId, int priority, boolean defaultAllowed) {

		String sql = "insert into PM_BASE_RIGHT(BASE_RIGHT_ID,BASE_RIGHT_NAME,RIGHT_ID,PRIORITY,DEFAUT_ALLOW_DENY) values ("
				+ rightId + ",'" + rightName + "'," + groupId + "," + priority + "," + (defaultAllowed ? 1 : 0) + ")";
		System.out.println(sql);

		Map<String, Integer> subRights = rights.get(groupId);
		if (subRights == null) {
			subRights = new HashMap<String, Integer>();
			rights.put(groupId, subRights);
		}
		subRights.put(rightName, rightId);

		return rightId;
	}

	public static int getRightId(String groupName, String rightName) {

		int groupId = RightsGroupUtility.getRightGroupId(groupName);
		return rights.get(groupId).get(rightName);
	}
}