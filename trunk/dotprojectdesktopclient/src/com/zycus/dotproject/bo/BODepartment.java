package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Set;

public class BODepartment implements Serializable, Comparable<BODepartment> {
	private static final long	serialVersionUID	= 1L;
	
	private	long 				departmentID			= 0L;
	private String				departmentName			= null;
	private BOUser				departmentOwner			= null;
	private long				parentDepartmentID    	= 0L;
	private BOCompany			departmentCompany		= null;
	private Set<BODepartment>   childDepartments		= null;

	@Override
	public String toString() {
		return departmentName;
	}
	
	public int compareTo(BODepartment o) {
		return departmentName.compareTo(o.getDepartmentName());
	}

	/**
	 * @return the departmentID
	 */
	public long getDepartmentID()
	{
		return departmentID;
	}

	/**
	 * @param departmentID the departmentID to set
	 */
	public void setDepartmentID(long departmentID)
	{
		this.departmentID = departmentID;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	/**
	 * @return the childDepartments
	 */
	public Set<BODepartment> getChildDepartments()
	{
		return childDepartments;
	}

	/**
	 * @param childDepartments the childDepartments to set
	 */
	public void setChildDepartments(Set<BODepartment> childDepartments)
	{
		this.childDepartments = childDepartments;
	}

	/**
	 * @return the departmentOwner
	 */
	public BOUser getDepartmentOwner()
	{
		return departmentOwner;
	}

	/**
	 * @param departmentOwner the departmentOwner to set
	 */
	public void setDepartmentOwner(BOUser departmentOwner)
	{
		this.departmentOwner = departmentOwner;
	}

	
	/**
	 * @return the parentDepartmentID
	 */
	public long getParentDepartmentID()
	{
		return parentDepartmentID;
	}

	/**
	 * @param parentDepartmentID the parentDepartmentID to set
	 */
	public void setParentDepartmentID(long parentDepartmentID)
	{
		this.parentDepartmentID = parentDepartmentID;
	}

	/**
	 * @return the departmentCompany
	 */
	public BOCompany getDepartmentCompany()
	{
		return departmentCompany;
	}

	/**
	 * @param departmentCompany the departmentCompany to set
	 */
	public void setDepartmentCompany(BOCompany departmentCompany)
	{
		this.departmentCompany = departmentCompany;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof BODepartment)) {
			return false;
		}
		return getDepartmentID() == ((BODepartment)obj).getDepartmentID();
	}
	
	@Override
	public int hashCode() {
		return (int)getDepartmentID();
	}
}