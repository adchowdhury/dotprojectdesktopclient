package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class BOForumMessage implements Serializable, Comparable<BOForumMessage> {
	private long				forumID			= -1;
	private BOForumMessage		parent			= null;
	private Set<BOForumMessage>	childs			= null;
	private BOUser				author			= null;
	private BOUser				editor			= null;
	private Date				creationDate	= null;
	private String				title			= null;
	private String				body			= null;
	private BOForum				forum			= null;

	public int compareTo(BOForumMessage o) {
		return 0;
	}

	public long getForumID() {
		return forumID;
	}

	public void setForumID(long forumID) {
		this.forumID = forumID;
	}

	public BOForumMessage getParent() {
		return parent;
	}

	public void setParent(BOForumMessage parent) {
		this.parent = parent;
	}

	public Set<BOForumMessage> getChilds() {
		return childs;
	}

	public void setChilds(Set<BOForumMessage> childs) {
		this.childs = childs;
	}

	public BOUser getAuthor() {
		return author;
	}

	public void setAuthor(BOUser author) {
		this.author = author;
	}

	public BOUser getEditor() {
		return editor;
	}

	public void setEditor(BOUser editor) {
		this.editor = editor;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public BOForum getForum() {
		return forum;
	}

	public void setForum(BOForum forum) {
		this.forum = forum;
	}

}
