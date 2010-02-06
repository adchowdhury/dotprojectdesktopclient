package com.zycus.dotproject.util;

import java.io.Serializable;

/**
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 3, 2009 : 5:43:23 PM
 * 
 */
public class DataSource implements Serializable, Comparable<DataSource> {
	public static enum DatabaseType {
		Oracle, MySQL, MSSql
	}

	private String			dataSourceName	= null;
	private String			setverIPName	= null;
	private String			databaseName	= null;
	private String			userName		= null;
	private String			password		= null;
	private DatabaseType	dataBaseType	= null;
	
	public DatabaseType getDataBaseType() {
		return dataBaseType;
	}
	
	public void setDataBaseType(DatabaseType dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public int compareTo(DataSource o) {
		if (o == null || o.dataSourceName == null) {
			return 1;
		}
		if (dataSourceName == null) {
			return -1;
		}
		return dataSourceName.compareTo(o.dataSourceName);
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getSetverIPName() {
		return setverIPName;
	}

	public void setSetverIPName(String setverIPName) {
		this.setverIPName = setverIPName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return getDataSourceName();
	}
}