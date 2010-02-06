package com.zycus.dotproject.api;

import java.util.List;

import com.zycus.dotproject.bo.BOCompany;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.DateRange;
import com.zycus.dotproject.bo.ProjectStatus;

public interface IProjectManager {

	void saveProjectTasks(BOUser user, BOProject project);

	void saveProjectInfo(BOUser user, BOUser oldOwner, BOProject project);

	void deleteProject(BOUser user, BOProject project);

	List<BOProject> getAllProjects(BOCompany company, BOUser user);

	List<BOProject> getAllProjects(BOCompany company, BOUser user, ProjectStatus projectStatus);

	BOProject getCompleteProject(long projectId);

	List<BOProject> getAllProjects(BOCompany company, ProjectStatus projectStatus, BOUser user);

	List<BOProject> getAllProjects(BOCompany company, ProjectStatus projectStatus, DateRange dateRange, BOUser user);
}