package com.zycus.dotproject.bo;

import java.io.Serializable;

public class BOHistory implements Serializable, Comparable<BOHistory> {
	private static final long	serialVersionUID		= 1L;
	
	private	long 				historyID				= 0L;
	private BOUser				historyUser				= null;
	
	private long				historyProject			= 0;
	private long				historyItem			= 0;
	
	private HistoryAction		historyAction			= HistoryAction.Update;
	private HistoryTable		historyTable			= HistoryTable.Tasks;
	private String				historyName				= null;
	private String				historyChanges			= null;
	private String				historyDescription		= null;
	
	public BOHistory()
	{
		
	}
	
	public BOHistory(BOUser historyUser, long historyProject, long historyItem, 
			HistoryAction historyAction, HistoryTable historyTable, 
			String historyName, String historyChanges, String historyDescription)
	{
		super();
		this.historyAction = historyAction;
		this.historyChanges = historyChanges;
		this.historyDescription = historyDescription;
		this.historyItem = historyItem;
		this.historyName = historyName;
		this.historyProject = historyProject;
		this.historyTable = historyTable;
		this.historyUser = historyUser;
	}

	@Override
	public String toString() {
		return historyName;
	}
	
	public int compareTo(BOHistory o) {
		return historyName.compareTo(o.getHistoryName());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof BOHistory)) {
			return false;
		}
		return getHistoryID() == ((BOHistory)obj).getHistoryID();
	}
	
	@Override
	public int hashCode() {
		return (int)getHistoryID();
	}

	/**
	 * @return the historyID
	 */
	public long getHistoryID()
	{
		return historyID;
	}

	/**
	 * @param historyID the historyID to set
	 */
	public void setHistoryID(long historyID)
	{
		this.historyID = historyID;
	}

	/**
	 * @return the historyUser
	 */
	public BOUser getHistoryUser()
	{
		return historyUser;
	}

	/**
	 * @param historyUser the historyUser to set
	 */
	public void setHistoryUser(BOUser historyUser)
	{
		this.historyUser = historyUser;
	}

	/**
	 * @return the historyProject
	 */
	public long getHistoryProject()
	{
		return historyProject;
	}

	/**
	 * @param historyProject the historyProject to set
	 */
	public void setHistoryProject(long historyProject)
	{
		this.historyProject = historyProject;
	}

	/**
	 * @return the historyItem
	 */
	public long getHistoryItem()
	{
		return historyItem;
	}

	/**
	 * @param historyItem the historyItem to set
	 */
	public void setHistoryItem(long historyItem)
	{
		this.historyItem = historyItem;
	}

	/**
	 * @return the historyAction
	 */
	public HistoryAction getHistoryAction()
	{
		return historyAction;
	}

	/**
	 * @param historyAction the historyAction to set
	 */
	public void setHistoryAction(HistoryAction historyAction)
	{
		this.historyAction = historyAction;
	}

	/**
	 * @return the historyTable
	 */
	public HistoryTable getHistoryTable()
	{
		return historyTable;
	}

	/**
	 * @param historyTable the historyTable to set
	 */
	public void setHistoryTable(HistoryTable historyTable)
	{
		this.historyTable = historyTable;
	}

	/**
	 * @return the historyName
	 */
	public String getHistoryName()
	{
		return historyName;
	}

	/**
	 * @param historyName the historyName to set
	 */
	public void setHistoryName(String historyName)
	{
		this.historyName = historyName;
	}

	/**
	 * @return the historyChanges
	 */
	public String getHistoryChanges()
	{
		return historyChanges;
	}

	/**
	 * @param historyChanges the historyChanges to set
	 */
	public void setHistoryChanges(String historyChanges)
	{
		this.historyChanges = historyChanges;
	}

	/**
	 * @return the historyDescription
	 */
	public String getHistoryDescription()
	{
		return historyDescription;
	}

	/**
	 * @param historyDescription the historyDescription to set
	 */
	public void setHistoryDescription(String historyDescription)
	{
		this.historyDescription = historyDescription;
	}
	
}