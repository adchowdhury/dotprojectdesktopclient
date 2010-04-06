package com.zycus.pm.client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zycus.pm.api.IPermissionManagerAPI;
import com.zycus.pm.api.IPermissionResolver;
import com.zycus.pm.api.IPermissionRightsAPI;
import com.zycus.pm.api.IPermissionStoreAPI;
import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.RightsGroup;
import com.zycus.pm.excpection.DuplicateNameException;
import com.zycus.pm.excpection.PermissionAccessException;
import com.zycus.pm.factory.PermissionAPIsFactory;
@SuppressWarnings("PMD")
public class PermissionManagementClient {
	private IPermissionRightsAPI	permissionRights	= PermissionAPIsFactory.getPermissionRightsAPI();
	private IPermissionManagerAPI	permissionManager	= PermissionAPIsFactory.getPermissionManagerAPI();

	public void createRights() {

		try {
			// this will add rights with same priority
			RightsGroup grp1 = permissionRights.addRights("Add-Edit-Delete", "Add", "Edit", "Delete");
			RightsGroup grp3 = permissionRights.addRights("Add-Edit-Delete2", "Add", "Edit", "Delete");
			
			// this shows how to add rights with different priority
			Map<String, Integer> rights = new LinkedHashMap<String, Integer>();
			rights.put("Can add 5 files", 1);
			rights.put("Can add 10 files", 2);
			rights.put("Can add 20 files", 3);

			RightsGroup grp2 = permissionRights.addRights("File count permissions", rights);

		} catch (DuplicateNameException a_excp) {
			a_excp.printStackTrace();
		}
	}
	
	public void associateRights() {
		IPermisibleResource resource = getDummyResource();
		try {
			// Getting required rights from the API at the beginning
			RightsGroup addEditDeleteGrp = permissionRights.getRightsGroup("Add-Edit-Delete");
			
			// explicitly deny add right for the specific entity to the specific resource
			permissionManager.associateRightGroup(resource, addEditDeleteGrp);

		} catch (Throwable a_excp) {
			a_excp.printStackTrace();
		}
	}
	
	public void removeAssociationRights() {
		IPermisibleResource resource = getDummyResource();
		try {
			// Getting required rights from the API at the beginning
			RightsGroup addEditDeleteGrp = permissionRights.getRightsGroup("Add-Edit-Delete");
			
			// explicitly deny add right for the specific entity to the specific resource
			permissionManager.removeAssociation(resource, addEditDeleteGrp);

		} catch (Throwable a_excp) {
			a_excp.printStackTrace();
		}
	}
	
	public void printAssociatedRights() {
		IPermisibleResource resource = getDummyResource();
		List<RightsGroup> lstRightGroup = permissionManager.getAllAssociatedGroups(resource);
		System.out.println(lstRightGroup);
	}

	public void assignRights() {
		IPermissionEntity entity = getDummyEntity();
		IPermisibleResource resource = getDummyResource();

		try {
			// Getting required rights from the API at the beginning
			BaseRight editRight = permissionRights.getBaseRight("Add-Edit-Delete", "Edit");
			BaseRight addRight = permissionRights.getBaseRight("Add-Edit-Delete", "Add");
			
			
			// explicitly deny add right for the specific entity to the specific resource
			permissionManager.upsertRight(addRight, entity, resource, false);

			// explicitly allow edit right for the specific entity to the specific resource
			permissionManager.upsertRight(editRight, entity, resource, true);

		} catch (PermissionAccessException a_excp) {
			a_excp.printStackTrace();
		}
	}
	
	public void checkRights() {
		IPermissionEntity entity = getDummyEntity();
		IPermisibleResource resource = getDummyResource();
		
		try {
			//Getting required rights from the API at the beginning
			BaseRight addRight = permissionRights.getBaseRight("Add-Edit-Delete", "Add");
			BaseRight editRight = permissionRights.getBaseRight("Add-Edit-Delete", "Edit");
			
			//checking whether the specified entity have add right on the specified resource or not 
			if(permissionManager.getRight(entity, resource).isRightPermitted(addRight)) {
				System.out.println("Add right permitted");
			}else {
				System.out.println("Add right not permitted");
			}
			
			//checking whether the specified entity have edit right on the specified resource or not 
			if(permissionManager.getRight(entity, resource).isRightPermitted(editRight)) {
				System.out.println("Edit right permitted");
			}else {
				System.out.println("Edit right not permitted");
			}
			
			//custom resolver
			if(permissionManager.getRight(entity, resource, new MyResolver()).isRightPermitted(editRight)) {
				System.out.println("Edit right permitted");
			}else {
				System.out.println("Edit right not permitted");
			}
		} catch (PermissionAccessException a_excp) {
			a_excp.printStackTrace();
		}
	}
	
	public void checkRightsHirerchy() {
		IPermissionEntity entity = getDummyChildEntity();
		IPermisibleResource resource = getDummyResource();
		IPermisibleResource childResource = getDummyChildResource();
		
		try {
			//Getting required rights from the API at the beginning
			BaseRight editRight = permissionRights.getBaseRight("Add-Edit-Delete", "Edit");
			
						
			//checking whether the specified entity have edit right on the specified resource or not 
			if(permissionManager.getRight(entity, resource).isRightPermitted(editRight)) {
				System.out.println("Edit right permitted");
			}else {
				System.out.println("Edit right not permitted");
			}	
			System.out.println("PermissionManagementClient.checkRightsHirerchy(==============================)");
			if(permissionManager.getRight(entity, childResource).isRightPermitted(editRight)) {
				System.out.println("Edit right permitted");
			}else {
				System.out.println("Edit right not permitted");
			}
		} catch (PermissionAccessException a_excp) {
			a_excp.printStackTrace();
		}
	}

	class MyResolver implements IPermissionResolver {
		private IPermissionStoreAPI permissionStore = null;
		public boolean isRightPermitted(BaseRight right, IPermissionEntity entity, IPermisibleResource resource) {
			if(permissionStore == null) {
				throw new NullPointerException("IPermissionStoreAPI is not set");
			}
			System.out.println("MyResolver.isRightPermitted(" + right + ", " + entity + ", " + resource + ")");
			return false;
		}

		public void setPermissionStore(IPermissionStoreAPI a_permissionStore) {
			System.out.println("MyResolver.setPermissionStore(" + a_permissionStore + ")");
			this.permissionStore = a_permissionStore;
		}
	}
	
	private IPermissionEntity getDummyChildEntity() {
		IPermissionEntity entity = new IPermissionEntity() {

			public long getEntityID() {
				return 44;
			}

			public String getEntityName() {
				return null;
			}

			public long getEntityTypeID() {
				return 5;
			}

			public IPermissionEntity getParent() {
				return getDummyEntity();
			}

			public int compareTo(IPermissionEntity o) {
				return 0;
			}
			
		};		
		return entity;
	}
	
	private IPermissionEntity getDummyEntity() {
		IPermissionEntity entity = new IPermissionEntity() {

			public long getEntityID() {
				return 43;
			}

			public String getEntityName() {
				return null;
			}

			public long getEntityTypeID() {
				return 4;
			}

			public IPermissionEntity getParent() {
				return null;
			}

			public int compareTo(IPermissionEntity o) {
				return 0;
			}
			
		};		
		return entity;
	}
	
	private IPermisibleResource getDummyChildResource() {
		IPermisibleResource resource = new IPermisibleResource() {

			public IPermisibleResource getParent() {
				return getDummyResource();
			}

			public long getResourceID() {
				return 23;
			}

			public String getResourceName() {
				return "Dummy Resource child";
			}

			public long getResourceTypeID() {
				return 11;
			}

			public int compareTo(IPermisibleResource o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
		return resource;
	}
	
	private IPermisibleResource getDummyResource() {
		IPermisibleResource resource = new IPermisibleResource() {

			public IPermisibleResource getParent() {
				return null;
			}

			public long getResourceID() {
				return 1;
			}

			public String getResourceName() {
				return "Dummy Resource";
			}

			public long getResourceTypeID() {
				return 1;
			}

			public int compareTo(IPermisibleResource o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
		return resource;
	}
}