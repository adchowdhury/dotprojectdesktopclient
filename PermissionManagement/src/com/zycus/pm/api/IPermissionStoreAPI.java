package com.zycus.pm.api;

import java.util.List;

import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.Permission;
/**
 * This class is mainly for getting all rights for a particular combination of
 * entity and resource. This will required by custom {@link IPermissionResolver}
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 21, 2009 : 2:17:52 PM
 *
 */
public interface IPermissionStoreAPI {
	
	/**
	 * This will return all available permissions for the specified combination
	 * of entity and resource
	 * 
	 * @param entity is the entity for which all rights need to be fetched
	 * @param resource for which all rights to be returned
	 * @return all available permissions for the specified combination
	 * of entity and resource
	 */
	List<Permission> getRights(IPermissionEntity entity, IPermisibleResource resource);
}