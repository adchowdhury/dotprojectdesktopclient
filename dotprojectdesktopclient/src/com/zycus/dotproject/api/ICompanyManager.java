package com.zycus.dotproject.api;

import java.util.List;

import com.zycus.dotproject.bo.BOCompany;
import com.zycus.dotproject.bo.BOUser;

/**
 * 
 * this is to manage all company
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Mar 15, 2009 : 11:12:04 AM
 *
 */
public interface ICompanyManager
{

	public abstract List<BOCompany> getAllCompanies(BOUser user);

	public abstract void deleteCompany(BOUser user, BOCompany company);

	public abstract void saveCompany(BOUser user, BOCompany company);
}