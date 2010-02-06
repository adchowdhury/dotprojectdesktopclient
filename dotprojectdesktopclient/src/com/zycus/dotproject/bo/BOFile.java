package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Date;

import com.zycus.dotproject.factory.UserManagerFactory;

public class BOFile implements Serializable, Comparable<BOFile> {
	private long			fileID				= 0l;
	private String			realFilename		= null;
	private int				folder				= 0;
	private BOProject		project				= null;
	//private BOTask			task				= null;
	private String			fileName			= null;
	private int				fileParent			= 0;
	private String			fileDescription		= null;
	private String			fileMimeType		= null;
	private BOUser			fileOwner			= null;
	private Date			fileDate			= null;
	private int				fileSize			= 0;
	private float			fileVersion			= 1.0f;
	private String			fileIcon			= "obj/";
	private FileCategory	fileCategory		= FileCategory.Unknown;
	private String			fileCheckout		= "";
	private String			fileCheckoutReason	= "";
	// not keeping null for keeping consistency
	private BOFile			rootVersionFile		= null;
	private BOUser			checkedOutUser		= null;
	private long			rootFileID			= 0l;
	private int				versionCount		= 0;
	
	private long			taskID				= 0l;
	private String			taskName			= null;
	
	public long getTaskID() {
		return taskID;
	}
	
	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getVersionCount() {
		return versionCount;
	}

	public void setVersionCount(int versionCount) {
		this.versionCount = versionCount;
	}

	public long getRootFileID() {
		return rootFileID;
	}

	public void setRootFileID(long rootFileID) {
		this.rootFileID = rootFileID;
	}

	public BOUser getCheckedOutUser() {
		return checkedOutUser;
	}

	public void setCheckedOutUser(BOUser checkedOutUser) {
		this.checkedOutUser = checkedOutUser;
		if (checkedOutUser != null) {
			setFileCheckout(checkedOutUser.getUserID() + "");
		} else {
			setFileCheckout("");
		}

	}

	public String getFileCheckoutReason() {
		return fileCheckoutReason;
	}

	public void setFileCheckoutReason(String fileCheckoutReason) {
		this.fileCheckoutReason = fileCheckoutReason;
	}

	public BOFile getRootVersionFile() {
		return rootVersionFile;
	}

	public void setRootVersionFile(BOFile rootVersionFile) {
		this.rootVersionFile = rootVersionFile;
	}

	public long getFileID() {
		return fileID;
	}

	public void setFileID(long fileID) {
		this.fileID = fileID;
	}

	public String getRealFilename() {
		return realFilename;
	}

	public void setRealFilename(String realFilename) {
		this.realFilename = realFilename;
	}

	public int getFolder() {
		return folder;
	}

	public void setFolder(int folder) {
		this.folder = folder;
	}

	public BOProject getProject() {
		return project;
	}

	public void setProject(BOProject project) {
		this.project = project;
	}

	/*public BOTask getTask() {
		return task;
	}

	public void setTask(BOTask task) {
		this.task = task;
	}*/

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileParent() {
		return fileParent;
	}

	public void setFileParent(int fileParent) {
		this.fileParent = fileParent;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public String getFileMimeType() {
		return fileMimeType;
	}

	public void setFileMimeType(String fileMimeType) {
		this.fileMimeType = fileMimeType;
	}

	public BOUser getFileOwner() {
		return fileOwner;
	}

	public void setFileOwner(BOUser fileOwner) {
		this.fileOwner = fileOwner;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public float getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(float fileVersion) {
		this.fileVersion = fileVersion;
	}

	public String getFileIcon() {
		return fileIcon;
	}

	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}

	public FileCategory getFileCategory() {
		return fileCategory;
	}

	public void setFileCategory(FileCategory fileType) {
		this.fileCategory = fileType;
	}

	public String getFileCheckout() {
		return fileCheckout;
	}

	public void setFileCheckout(String fileCheckout) {
		this.fileCheckout = fileCheckout;
		if (this.fileCheckout.trim().length() > 0)
			checkedOutUser = UserManagerFactory.getUserManager().getUsers(fileCheckout).get(0);
	}

	public String generateFileName() {
		StringBuilder strID = new StringBuilder();
		if (project != null) {
			strID.append(project.getProjectID() + "_");
		}
		strID.append(taskID + "_");
		if (fileOwner != null) {
			strID.append(fileOwner.getUserID() + "_");
		}
		strID.append(System.currentTimeMillis());
		return strID.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BOFile == false) {
			return false;
		}
		return compareTo((BOFile) obj) == 0;
	}

	public int compareTo(BOFile o) {
		if (o == null) {
			return -1;
		}
		return new Long(fileID).compareTo(o.fileID);
	}

}