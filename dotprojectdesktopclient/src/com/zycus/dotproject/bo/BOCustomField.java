package com.zycus.dotproject.bo;

import java.io.Serializable;

public class BOCustomField implements Serializable 
{
	private long				customFieldID			= 0l;
	private String				customFieldModule		= null;
	private String				customFieldPage			= null;
	private String				customFieldHtmlType		= null;
	private String				customFieldDataType		= null;
	private int					customFieldOrder		= 0;
	private String				customFieldName			= null;
	private String				customFieldExtragas		= null;
	private String				customFieldDescription	= null;

	public static BOCustomField	CustomTaskModule		= null;

	public long getCustomFieldID() {
		return customFieldID;
	}

	public void setCustomFieldID(long customFieldID) {
		this.customFieldID = customFieldID;
	}

	public String getCustomFieldModule() {
		return customFieldModule;
	}

	public void setCustomFieldModule(String customFieldModule) {
		this.customFieldModule = customFieldModule;
	}

	public String getCustomFieldPage() {
		return customFieldPage;
	}

	public void setCustomFieldPage(String customFieldPage) {
		this.customFieldPage = customFieldPage;
	}

	public String getCustomFieldHtmlType() {
		return customFieldHtmlType;
	}

	public void setCustomFieldHtmlType(String customFieldHtmlType) {
		this.customFieldHtmlType = customFieldHtmlType;
	}

	public String getCustomFieldDataType() {
		return customFieldDataType;
	}

	public void setCustomFieldDataType(String customFieldDataType) {
		this.customFieldDataType = customFieldDataType;
	}

	public int getCustomFieldOrder() {
		return customFieldOrder;
	}

	public void setCustomFieldOrder(int customFieldOrder) {
		this.customFieldOrder = customFieldOrder;
	}

	public String getCustomFieldName() {
		return customFieldName;
	}

	public void setCustomFieldName(String customFieldName) {
		this.customFieldName = customFieldName;
	}

	public String getCustomFieldExtragas() {
		return customFieldExtragas;
	}

	public void setCustomFieldExtragas(String customFieldExtragas) {
		this.customFieldExtragas = customFieldExtragas;
	}

	public String getCustomFieldDescription() {
		return customFieldDescription;
	}

	public void setCustomFieldDescription(String customFieldDescription) {
		this.customFieldDescription = customFieldDescription;
	}
}