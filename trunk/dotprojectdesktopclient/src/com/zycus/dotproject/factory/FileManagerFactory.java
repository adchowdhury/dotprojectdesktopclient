package com.zycus.dotproject.factory;

import com.zycus.dotproject.api.IFileManager;
import com.zycus.dotproject.impl.FileManagerImpl;

public final class FileManagerFactory {
	public static final IFileManager getFileManager() {
		return new FileManagerImpl();
	}
}