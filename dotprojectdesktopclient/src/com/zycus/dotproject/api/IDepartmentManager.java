package com.zycus.dotproject.api;

import java.util.List;

import com.zycus.dotproject.bo.BODepartment;
import com.zycus.dotproject.bo.BOUser;

public interface IDepartmentManager
{

	public abstract List<BODepartment> getAllDepartments(BOUser user);

	public abstract void deleteDepartment(BOUser user, BODepartment department);

	public abstract void saveDepartment(BOUser user, BODepartment department);

}