package com.zycus.pm.api;

import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
/**
 * this is the last point of execution where main resolving logic is 
 * written. this will be called from {@link IPermissionStoreAPI}.
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 29, 2009 : 1:42:58 PM
 *
 */
public interface IPermissionResolver {
	/**
	 * This will enable the resolver with a mechanism to get data from 
	 * database. 
	 * @param permissionStore is instance of {@link IPermissionStoreAPI}, with which
	 * this resolver can get data.
	 */
	void setPermissionStore(IPermissionStoreAPI permissionStore);

	/**
	 * this method will be finally be called from {@link IPermissionRights#isRightPermitted(BaseRight)}
	 * there is one default implementation provided by the component and at the same time any application 
	 * using this component can provide their own {@link IPermissionResolver} while calling {@link IPermissionManagerAPI}
	 * via {@link IPermissionManagerAPI#getRight(IPermissionEntity, IPermisibleResource, IPermissionResolver)} or can
	 * set their default {@link IPermissionResolver} by using {@link IPermissionManagerAPI#setDefaultPermissionResolver(IPermissionResolver)}
	 * 
	 * @see IPermissionRights#isRightPermitted(BaseRight)
	 * 
	 * @param right is {@link BaseRight} which is allowed or denied to be checked
	 * @param entity is {@link IPermissionEntity} which is allowed or denied is to be checked
	 * @param resource is {@link IPermisibleResource} for which the {@link BaseRight} is allowed or not to be checked 
	 * @return true if the specified {@link IPermissionEntity} is allowed the {@link BaseRight} on 
	 * the specified {@link IPermisibleResource}, false otherwise
	 */
	boolean isRightPermitted(BaseRight right, IPermissionEntity entity, IPermisibleResource resource);
}