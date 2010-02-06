package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Date;

public class BOContact implements Serializable, Comparable<BOContact> {
	
	private static final long	serialVersionUID	= 1L;
	private String			firstName			= null;
	private String			lastName			= "";
	private Date			dateOfBirth			= null;
	private String			phoneNumber			= null;
	private long			contactID			= -1;
	private String			contactTitle		= null;
	private String			contactEmail		= null;
	private BOCompany		contactCompany		= null;
	private BODepartment 	contactDepartment 	= null;
	private String			contactMobile		= null;

	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public BOCompany getContactCompany() {
		return contactCompany;
	}

	public void setContactCompany(BOCompany contactCompany) {
		this.contactCompany = contactCompany;
	}

	/**
	 * @return the contactDepartment
	 */
	public BODepartment getContactDepartment()
	{
		return contactDepartment;
	}

	/**
	 * @param contactDepartment the contactDepartment to set
	 */
	public void setContactDepartment(BODepartment contactDepartment)
	{
		this.contactDepartment = contactDepartment;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public long getContactID() {
		return contactID;
	}

	public void setContactID(long contactID) {
		this.contactID = contactID;
	}

	public int compareTo(BOContact o) {
		return getFirstName().compareTo(o.getFirstName());
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}