package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.List;

public class BOEvent implements Comparable<BOEvent>, Serializable {
	private static final long	serialVersionUID	= 1l;
	private String				eventName			= null;
	private String				eventDescription	= null;
	private DateRange			eventRange			= null;
	private List<BOUser>		members				= null;
	private EventType			eventType			= EventType.General;

	public int compareTo(BOEvent o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public DateRange getEventRange() {
		return eventRange;
	}

	public void setEventRange(DateRange eventRange) {
		this.eventRange = eventRange;
	}

	public List<BOUser> getMembers() {
		return members;
	}

	public void setMembers(List<BOUser> members) {
		this.members = members;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}