package com.zycus.dotproject.factory;

import com.zycus.dotproject.api.IProjectManager;
import com.zycus.dotproject.impl.ProjectManagerImpl;

public class ProjectManagerFactory {
	public static IProjectManager getProjectManager() {
		return new ProjectManagerImpl();
	}
}
