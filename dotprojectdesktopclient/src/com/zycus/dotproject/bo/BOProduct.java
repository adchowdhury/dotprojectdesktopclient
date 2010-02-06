package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.zycus.dotproject.factory.UserManagerFactory;

public class BOProduct implements Serializable, Comparable<BOProduct> {

	private static final long	serialVersionUID	= 1l;

	private long				productID			= 0L;
	private String				productName			= null;
	private BOUser				productOwner		= null;
	private BOCompany			productCompany		= null;
	private Priority			productPriority		= Priority.Normal;
	private String				productDescription	= null;

	private BODepartment		productDepartment	= null;
	private Set<BOProject>		productProjects		= null;
	private List<BOUser>		contactUsers		= null;
	private DateRange			productRange		= new DateRange();
	private String				contactStringUsers	= null;
	
	public int compareTo(BOProduct o) {
		if(getProductID()==o.getProductID())
			return 1;
		else return 0;
	}
	
	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}
	/*
	public BOUser getProductOwner() {
		return productOwner;
	}

	public void setProductOwner(BOUser productOwner) {
		this.productOwner = productOwner;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public int compareTo(BOProduct o) {
		return getProductName().compareTo(o.getProductName());
	}

	@Override
	public String toString() {
		return getProductName();
	}

	public Set<BOTask> getTasks() {
		if (tasks == null) {
			tasks = new LinkedHashSet<BOTask>();
		}
		return tasks;
	}

	public void setTasks(Set<BOTask> tasks) {
		this.tasks = tasks;
	}

	public List<BOUser> getContactUsers() {
		if (contactUsers == null) {
			contactUsers = new ArrayList<BOUser>();
		}
		return contactUsers;
	}

	public void setContactUsers(List<BOUser> contactUsers) {
		this.contactUsers = contactUsers;
	}

	public void setContactUsersString(String strContactUsers) {
		contactStringUsers = strContactUsers;
	}

	public void loadContactUsers() {
		if (contactStringUsers == null || contactStringUsers.trim().length() <= 0) {
			return;
		}
		contactUsers = UserManagerFactory.getUserManager().getUsers(contactStringUsers);
	}

	public String getContactUsersString() {
		StringBuilder strReturn = new StringBuilder();
		
		if(contactUsers!=null)
		{
    		for (BOUser contact : contactUsers) {
    			if (strReturn.length() > 0) {
    				strReturn.append(",");
    			}
    			strReturn.append(contact.getUserID());
    		}
		}
		return strReturn.toString();
	}

	public DateRange getProductRange() {
		return productRange;
	}

	public void setProductRange(DateRange productRange) {
		this.productRange = productRange;
	}

	*//**
	 * @return
	 *//*
	public Date getProductStartDate() {
		return this.productRange.getStartDate();
	}

	public void setProductStartDate(Date startDate) {
		this.productRange.setStartDate(startDate);
	}

	*//**
	 * @return
	 *//*
	public Date getProductEndDate() {
		return this.productRange.getEndDate();
	}

	public void setProductEndDate(Date endDate) {
		this.productRange.setEndDate(endDate);
	}

	public Priority getProductPriority() {
		return productPriority;
	}

	public void setProductPriority(Priority productPriority) {
		this.productPriority = productPriority;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	*//**
	 * @return the productCompany
	 *//*
	public BOCompany getProductCompany() {
		return productCompany;
	}

	*//**
	 * @param productCompany
	 *            the productCompany to set
	 *//*
	public void setProductCompany(BOCompany productCompany) {
		this.productCompany = productCompany;
	}

	*//**
	 * @return the productDepartment
	 *//*
	public Set<BODepartment> getProductDepartments() {
		if (productDepartments == null) {
			productDepartments = new LinkedHashSet<BODepartment>();
		}
		return productDepartments;
	}

	*//**
	 * @param productDepartment
	 *            the productDepartment to set
	 *//*
	public void setProductDepartments(Set<BODepartment> productDepartments) {
		this.productDepartments = productDepartments;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BOProduct)) {
			return false;
		}
		return getProductID() == ((BOProduct) obj).getProductID();
	}

	@Override
	public int hashCode() {
		return (int) getProductID();
	}

	public boolean canViewProduct(BOUser user) {
		Set<BOTask> taskSet = tasks;
		for (Iterator taskIterator = taskSet.iterator(); taskIterator.hasNext();) {
			BOTask currentTask = (BOTask) taskIterator.next();
			if (currentTask.canAddTaskLog(user)) {
				return true;
			}
		}

		return false;
	}

	public boolean canSaveProductInfo(BOUser currentUser, BOUser oldUser) {
		// System.out.println("BOProduct.canSaveProductInfo() oldUser " +
		// oldUser);
		// System.out.println("BOProduct.canSaveProductInfo() oldUser " +
		// currentUser);

		if (oldUser == null) {
			return true;
		}
		if (oldUser.getUserID() == currentUser.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	// TODO send the parent task, if parent task is null then check for product
	// ownership
	public boolean canAddTasks(BOUser user) {
		if (productOwner.getUserID() == user.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canEditProductDataOtherThanTasks(BOUser user) {
		if (productOwner.getUserID() == user.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canDeleteProduct(BOUser user) {
		// TODO should the admin be allowed to delete a product ... I think not
		// (user.getUserType()==UserType.Administrator)
		if (productOwner.getUserID() == user.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canSaveProductTasks(BOUser user) {
		if (tasks != null) {
			Iterator<BOTask> taskIterator = tasks.iterator();
			while (taskIterator.hasNext()) {
				BOTask task = taskIterator.next();
				if (task.canBeEdited(user)) {
					return true;
				}
			}
			return false;
		}

		else if (productOwner.getUserID() == user.getUserID()) {
			return true;
		}

		else {
			return false;
		}
	}

	*//**
	 * @return the currentHistoryForProduct
	 *//*
	public ArrayList<BOHistory> getCurrentHistoryForProduct()
	{
		return currentHistoryForProduct;
	}

	*//**
	 * @param currentHistoryForProduct the currentHistoryForProduct to set
	 *//*
	public void setCurrentHistoryForProduct(
			ArrayList<BOHistory> currentHistoryForProduct)
	{
		this.currentHistoryForProduct = currentHistoryForProduct;
	}
*/}