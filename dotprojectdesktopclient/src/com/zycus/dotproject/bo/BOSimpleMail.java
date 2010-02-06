package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class BOSimpleMail implements Serializable, Comparable<BOSimpleMail> {
	private static final long	serialVersionUID	= 1l;
	private String				sender				= null;
	private String				subject				= null;
	private String				body				= null;
	private Set<String>			recipients			= new LinkedHashSet<String>();
	
	public Iterator<String> getAllRecipients(){
		return recipients.iterator();
	}	
	
	public void setSender(String from) {
		this.sender = from;
	}
	
	public String getSender() {
		return sender;
	}

	public void removeAllRecipients() {
		recipients.clear();
	}

	public void addRecipient(String strRecipient) {
		recipients.add(strRecipient);
	}

	public void removeRecipient(String strRecipient) {
		recipients.remove(strRecipient);
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public int compareTo(BOSimpleMail o) {
		// TODO Auto-generated method stub
		return 0;
	}

}