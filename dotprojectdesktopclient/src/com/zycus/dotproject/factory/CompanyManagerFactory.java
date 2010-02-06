package com.zycus.dotproject.factory;

import com.zycus.dotproject.api.ICompanyManager;
import com.zycus.dotproject.impl.CompanyManagerImpl;

public class CompanyManagerFactory {

	public static ICompanyManager getCompanyManager() {
		return new CompanyManagerImpl();
	}
}