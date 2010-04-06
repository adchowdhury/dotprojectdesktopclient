package com.zycus.pm.api;

import java.util.List;

import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.RightsGroup;
import com.zycus.pm.excpection.PermissionAccessException;
/**
 * this is main for associating any right with any resource
 * and remove the association, assign rights and remove association,
 * checking permissions
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 29, 2009 : 2:08:43 PM
 *
 */
public interface IPermissionManagerAPI {
	
	void associateRightGroup(IPermisibleResource resource, RightsGroup group);
	
	void removeAllAssociations(IPermisibleResource resource);
	
	void removeAllAssociations(RightsGroup group);
	
	void removeAssociation(IPermisibleResource resource, RightsGroup group);
	
	List<RightsGroup> getAllAssociatedGroups(IPermisibleResource resource);
	
	/**
	 * 
	 * @param right is for particular entity and resource to be added / updated
	 * @param entity is the entity for which right to be set
	 * @param resource is the resource for which right to be set
	 * @param isAllowed will determine the allowed or denied.
	 * @throws PermissionAccessException in case the right already exists, even in case of
	 * some other business flaw
	 */
	void upsertRight(BaseRight right, IPermissionEntity entity, IPermisibleResource resource, boolean isAllowed)throws PermissionAccessException;

	void upsertAllRights(RightsGroup rightGroup, IPermissionEntity entity, IPermisibleResource resource, boolean isAllowed)throws PermissionAccessException;
	
	void upsertAllRights(List<BaseRight> rights, IPermissionEntity entity, IPermisibleResource resource, boolean isAllowed)throws PermissionAccessException;

	/**
	 * 
	 * @param right the particular right to be removed
	 * @param entity for which specified right to be removed
	 * @param resource
	 * @throws PermissionAccessException
	 */
	void removeRight(BaseRight right, IPermissionEntity entity, IPermisibleResource resource)throws PermissionAccessException;
	
	void removeRights(IPermissionEntity entity, IPermisibleResource resource)throws PermissionAccessException;

	void removeAllRights(IPermissionEntity entity)throws PermissionAccessException;

	void removeAllRights(IPermisibleResource resource)throws PermissionAccessException;
	
	void removeAllRights(BaseRight right)throws PermissionAccessException;
	
	
	void setDefaultPermissionResolver(IPermissionResolver resolver);
	
	IPermissionRights getRight(IPermissionEntity entity, IPermisibleResource resource)throws PermissionAccessException;
	
	IPermissionRights getRight(IPermissionEntity entity, IPermisibleResource resource, IPermissionResolver resolver)throws PermissionAccessException;
}