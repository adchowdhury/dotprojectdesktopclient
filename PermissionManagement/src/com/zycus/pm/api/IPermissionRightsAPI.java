package com.zycus.pm.api;

import java.util.Map;

import com.zycus.pm.api.bo.BaseRight;
import com.zycus.pm.api.bo.RightsGroup;
import com.zycus.pm.excpection.DuplicateNameException;
/**
 * This is for CRUD operations of {@link BaseRight} and {@link RightsGroup}
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 29, 2009 : 1:28:20 PM
 *
 */
public interface IPermissionRightsAPI {
	//Create
	
	/**
	 * 
	 * @param groupName is the name of the group to be created, in-case the group 
	 * is present then the right will be appended to the existing group.
	 *  
	 * @param rightName is the name of the right which is required to be added
	 * @param priority is the priority of the right
	 * @return newly created {@link BaseRight} object
	 * @throws DuplicateNameException in-case the rightName is exists for the same
	 * group
	 */
	
	BaseRight addRight(String groupName, String rightName) throws DuplicateNameException;
	
	BaseRight addRight(String groupName, String rightName, int priority) throws DuplicateNameException;
	
	BaseRight addRight(String groupName, String rightName, int priority, boolean defaultAllowed) throws DuplicateNameException;
	
	BaseRight addRight(RightsGroup group, String rightName, int priority, boolean defaultAllowed) throws DuplicateNameException;
	
	RightsGroup addRights(String groupName, String ...rightNames) throws DuplicateNameException;
	
	RightsGroup addRights(String groupName, Map<String, Integer> rights) throws DuplicateNameException;
	
	void addRights(RightsGroup group, String ...rightNames) throws DuplicateNameException;
	
	void addRights(RightsGroup group, Map<String, Integer> rights) throws DuplicateNameException;
	
	//Read
	RightsGroup getRightsGroup(long groupID);
	
	RightsGroup getRightsGroup(String groupName);
	
	BaseRight getBaseRight(long baseRightID);
	
	BaseRight getBaseRight(String groupName, String baseRightName);
	
	BaseRight getBaseRight(long groupID, String baseRightName);
	
	//Update
	void updateRightGroup(RightsGroup group) throws DuplicateNameException;
	
	void updateBaseRight(BaseRight baseRight) throws DuplicateNameException;
	
	RightsGroup updateRightsGroupName(long groupID, String groupName) throws DuplicateNameException;
	
	BaseRight updateBaseRightName(long baseRightID, String baseRightName) throws DuplicateNameException;
	
	BaseRight updateBaseRightPriority(long baseRightID, int priority);
	
	BaseRight updateBaseRightDefaultAllowed(long baseRightID, boolean defaultIsAllowed);
	
	BaseRight updateBaseRightNamePriority(long baseRightID, String baseRightName, int priority) throws DuplicateNameException;
	
	BaseRight updateBaseRightNameDefaultAllowed(long baseRightID, String baseRightName, boolean defaultIsAllowed) throws DuplicateNameException;
	
	BaseRight updateBaseRightPriorityDefaultAllowed(long baseRightID, int priority, boolean defaultIsAllowed) throws DuplicateNameException;
	
	BaseRight updateBaseRightNamePriorityDefaultAllowed(long baseRightID, String baseRightName, int priority, boolean defaultIsAllowed) throws DuplicateNameException;
	
	//Delete
	void deleteRightsGroup(long groupID);
	
	void deleteRightsGroup(String groupName);
	
	void deleteRightsGroup(RightsGroup group);
	
	void deleteBaseRight(long baseRightID);
	
	void deleteBaseRight(String baseRightName, RightsGroup group);
	
	void deleteBaseRight(String baseRightName, String groupName);
	
	void deleteBaseRight(BaseRight right);
	
}