package com.zycus.pm.client;

public class Test {
	public static void main(String[] args) {
		/*
		 * PermissionManagementDBHandler pdb = new PermissionManagementDBHandler();
		 * 
		 * BaseRight r = new BaseRight("A"); pdb.upsertBaseRight(r);
		 */

		PermissionManagementClient pmc = new PermissionManagementClient();
		// pmc.createRights();
		pmc.associateRights();
		// pmc.printAssociatedRights();
		// pmc.assignRights();
		// pmc.checkRights();
		// pmc.checkRightsHirerchy();
		// pmc.removeAssociationRights();
	}
}