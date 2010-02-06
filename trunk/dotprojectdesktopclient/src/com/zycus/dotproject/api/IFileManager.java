package com.zycus.dotproject.api;

import java.io.File;
import java.util.List;

import com.zycus.dotproject.bo.BOFile;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOUser;

public interface IFileManager {

	List<BOFile> getFiles(long taskID, BOUser user);

	void saveFile(BOFile file, File targetFile);

	void addFile(BOFile file, File realFile);

	void checkOutFile(BOFile file, BOUser user);

	void checkInFile(BOFile originalFile, BOFile newFile, File realFile, BOUser user);

	boolean isServerAvailable();

	void deleteFile(BOFile file, BOUser user);

	void updateFile(BOFile file, BOUser user);

	void updateFile(BOFile file, File realFile, BOUser user);

	List<BOFile> getAllVersions(long versionID, BOUser user);
	
	List<BOFile> getAllFile(BOProject project, BOUser user);
}