package com.zycus.pm.api;

import java.io.Serializable;

import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.IPermisibleResource;
import com.zycus.pm.api.bo.IPermissionEntity;
import com.zycus.pm.api.bo.RightsGroup;
/**
 * This will be used for checking whether and {@link BaseRight} is allowed
 * for one {@link IPermissionEntity} and {@link IPermisibleResource}. This
 * class will internally call {@link IPermissionResolver#isRightPermitted(BaseRight, IPermissionEntity, IPermisibleResource)} to finally resolve.
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 29, 2009 : 1:32:03 PM
 *
 */
public interface IPermissionRights extends Serializable {
	
	/**
	 * This will call the {@link IPermissionResolver} internally
	 * and will return the return value from the {@link IPermissionResolver#isRightPermitted(BaseRight, IPermissionEntity, IPermisibleResource)}
	 * 
	 * @param right is {@link BaseRight} for which allowed or denied 
	 * to be checked 
	 * @return true if the {@link BaseRight} is allowed false otherwise
	 */
	boolean isRightPermitted(BaseRight right);

	/**
	 * This will call the {@link IPermissionResolver} internally
	 * and will return the return value from the {@link IPermissionResolver}
	 * 
	 * @param strRightName is the name of the {@link BaseRight} for which 
	 * allowed or denied is to be checked
	 * @param groupName is the name of the {@link RightsGroup} in which
	 * the {@link BaseRight} is to be present
	 * @return true if the {@link BaseRight} is allowed false otherwise
	 * /
	boolean isRightPermitted(String strRightName, String groupName);
	
	boolean isRightPermitted(String strRightName, long groupID);
	
	boolean isRightPermitted(String strRightName, RightsGroup group);*/
}