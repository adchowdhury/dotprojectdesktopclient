package com.zycus.pm.factory;

import com.zycus.pm.api.IPermissionManagerAPI;
import com.zycus.pm.api.IPermissionRightsAPI;
import com.zycus.pm.api.IPermissionStoreAPI;
import com.zycus.pm.impl.PermissionManagerImpl;
import com.zycus.pm.impl.PermissionRightsImpl;
import com.zycus.pm.impl.PermissionStoreImpl;

public final class PermissionAPIsFactory {
	private PermissionAPIsFactory() {
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This class is not to be cloned");
	}

	public static final IPermissionManagerAPI getPermissionManagerAPI() {
		return new PermissionManagerImpl();
	}

	public static final IPermissionStoreAPI getPermissionStoreAPI() {
		return new PermissionStoreImpl();
	}
	
	public static final IPermissionRightsAPI getPermissionRightsAPI() {
		return new PermissionRightsImpl();
	}
}