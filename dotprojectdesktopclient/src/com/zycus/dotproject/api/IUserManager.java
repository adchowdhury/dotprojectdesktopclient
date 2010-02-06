package com.zycus.dotproject.api;

import java.util.List;

import com.zycus.dotproject.bo.BOUser;

public interface IUserManager {
	
	BOUser getUser(String userName, String password);
	List<BOUser> getAllUsers();
	
	void saveUser(BOUser userSaving, BOUser userToBeSaved); 
	
	void deleteUser(BOUser userDeleting, BOUser userToBeDeleted);
	
	List<BOUser> getUsers(String strIds);
}