package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Set;

public class BOCompany implements Serializable, Comparable<BOCompany> {
	private static final long	serialVersionUID	= 1l;
	
	private	long 				companyID			= 0L;
	private String				companyName			= null;
	private BOUser				companyOwner		= null;
	private Set<BODepartment>	departments			= null;

	@Override
	public String toString() {
		return companyName;
	}
	
	public int compareTo(BOCompany o) {
		return companyName.compareTo(o.getCompanyName());
	}

	/**
	 * @return the companyID
	 */
	public long getCompanyID()
	{
		return companyID;
	}

	/**
	 * @param companyID the companyID to set
	 */
	public void setCompanyID(long companyID)
	{
		this.companyID = companyID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	/**
	 * @return the departments
	 */
	public Set<BODepartment> getDepartments()
	{
		return departments;
	}

	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(Set<BODepartment> departments)
	{
		this.departments = departments;
	}

	/**
	 * @return the companyOwner
	 */
	public BOUser getCompanyOwner()
	{
		return companyOwner;
	}

	/**
	 * @param companyOwner the companyOwner to set
	 */
	public void setCompanyOwner(BOUser companyOwner)
	{
		this.companyOwner = companyOwner;
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
		if(obj == null || !(obj instanceof BOCompany)) {
			return false;
		}
		return getCompanyID() == ((BOCompany)obj).getCompanyID();
	}
	
	@Override
	public int hashCode() {
		return (int)getCompanyID();
	}
}