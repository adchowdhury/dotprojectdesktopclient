package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class BOForum implements Serializable, Comparable<BOForum> {
	private long				forumID				= -1;
	private BOProject			project				= null;
	private Set<BOForumMessage>	messages			= null;
	private BOUser				forumOwner			= null;
	private Date				creationTime		= null;
	private Date				lastUpdationTime	= null;
	private BOUser				lastUpdationUser	= null;
	private String				forumName			= null;
	private String				description			= null;

	public int compareTo(BOForum o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getForumID() {
		return forumID;
	}

	public void setForumID(long forumID) {
		this.forumID = forumID;
	}

	public BOProject getProject() {
		return project;
	}

	public void setProject(BOProject project) {
		this.project = project;
	}

	public Set<BOForumMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<BOForumMessage> messages) {
		this.messages = messages;
	}

	public BOUser getForumOwner() {
		return forumOwner;
	}

	public void setForumOwner(BOUser forumOwner) {
		this.forumOwner = forumOwner;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastUpdationTime() {
		return lastUpdationTime;
	}

	public void setLastUpdationTime(Date lastUpdationTime) {
		this.lastUpdationTime = lastUpdationTime;
	}

	public BOUser getLastUpdationUser() {
		return lastUpdationUser;
	}

	public void setLastUpdationUser(BOUser lastUpdationUser) {
		this.lastUpdationUser = lastUpdationUser;
	}

	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
