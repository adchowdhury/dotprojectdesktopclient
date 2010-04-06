package com.zycus.pm.script.utilities;

public class PrimaryKeyLookup {

	private static int	RIGHT_GROUP_PRIMARY_KEY				= 0;

	private static int	RIGHT_GROUP_ASSOCIATION_PRIMARY_KEY	= 0;

	private static int	RIGHT_PRIMARY_KEY					= 0;

	private static int	PERMISSION_PRIMARY_KEY				= 0;

	public static synchronized int generateRightGroupPrimaryKey() {
		RIGHT_GROUP_PRIMARY_KEY++;
		return RIGHT_GROUP_PRIMARY_KEY;
	}

	public static synchronized int generateRightPrimaryKey() {
		RIGHT_PRIMARY_KEY++;
		return RIGHT_PRIMARY_KEY;
	}

	public static synchronized int generateRightGroupAssociationPrimaryKey() {
		RIGHT_GROUP_ASSOCIATION_PRIMARY_KEY++;
		return RIGHT_GROUP_ASSOCIATION_PRIMARY_KEY;
	}

	public static synchronized int generatePermissionPrimaryKey() {
		PERMISSION_PRIMARY_KEY++;
		return PERMISSION_PRIMARY_KEY;
	}
}