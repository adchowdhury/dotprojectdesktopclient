package com.zycus.dotproject.factory;

import com.zycus.dotproject.api.IUserManager;
import com.zycus.dotproject.impl.UserManagerImpl;

public class UserManagerFactory {
	public static IUserManager getUserManager() {
		return new UserManagerImpl();
	}
}
