package com.zycus.dotproject.factory;

import com.zycus.dotproject.api.IDepartmentManager;
import com.zycus.dotproject.impl.DepartmentManagerImpl;

public class DepartmentManagerFactory {

	public static IDepartmentManager getDepartmentManager() {
		return new DepartmentManagerImpl();
	}
}