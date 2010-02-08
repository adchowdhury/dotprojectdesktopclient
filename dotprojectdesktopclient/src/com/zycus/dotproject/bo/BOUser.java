package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class BOUser implements Comparable<BOUser>, Serializable {

	private static final long	serialVersionUID	= 1l;
	private UserType			userType			= UserType.DefaultUser;
	private String				loginName			= null;
	private String				RealName			= null;
	private String				emailAddress		= null;
	private String				password			= null;
	private long				userID				= -1;

	private boolean				canView				= true;
	private boolean				canAdd				= true;
	private boolean				canEdit				= false;
	private boolean				canDelete			= false;
	
	private BOContact			contact				= null;
	
	private Locale				locale				= null;
	
	private Set<BOCompany>		comapaniesOwned		= null;
	private Set<BODepartment>	departmentsOwned	= null;
	private Set<BOTask>			assignedTasks	= null;
	
	public BOUser()
	{
		
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * @return the assignedTasks
	 */
	public Set<BOTask> getAssignedTasks()
	{
		return assignedTasks;
	}

	/**
	 * @param assignedTasks the assignedTasks to set
	 */
	public void setAssignedTasks(Set<BOTask> assignedTasks)
	{
		this.assignedTasks = assignedTasks;
	}

	public BOContact getContact() {
		if(contact == null) {
			contact = new BOContact();
		}
		return contact;
	}
	
	public void setContact(BOContact contact) {
		this.contact = contact;
	}
	
	/**
	 * @return the comapaniesOwned
	 */
	public Set<BOCompany> getComapaniesOwned()
	{
		return comapaniesOwned;
	}

	/**
	 * @param comapaniesOwned the comapaniesOwned to set
	 */
	public void setComapaniesOwned(Set<BOCompany> comapaniesOwned)
	{
		this.comapaniesOwned = comapaniesOwned;
	}

	/**
	 * @return the departmentsOwned
	 */
	public Set<BODepartment> getDepartmentsOwned()
	{
		return departmentsOwned;
	}

	/**
	 * @param departmentsOwned the departmentsOwned to set
	 */
	public void setDepartmentsOwned(Set<BODepartment> departmentsOwned)
	{
		this.departmentsOwned = departmentsOwned;
	}

	
	public long getUserID() {
		return userID;
	}
	
	public void setUserID(long userID) {
		this.userID = userID;
	}

	public int compareTo(BOUser o) {
		return getLoginName().compareTo(o.getLoginName());
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCanView() {
		return canView;
	}

	public void setCanView(boolean canView) {
		this.canView = canView;
	}

	public boolean isCanAdd() {
		return canAdd;
	}

	public void setCanAdd(boolean canAdd) {
		this.canAdd = canAdd;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return getLoginName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof BOUser)) {
			return false;
		}
		return getUserID() == ((BOUser)obj).getUserID();
	}
	
	@Override
	public int hashCode() {
		return (int)getUserID();
	}
	
	public boolean isContainedIn(Collection c)
	{
		ArrayList list = new ArrayList(c);
		return list.contains(this);
	}
}