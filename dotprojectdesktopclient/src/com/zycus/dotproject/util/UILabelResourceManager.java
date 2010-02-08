package com.zycus.dotproject.util;

import java.util.Locale;
import java.util.ResourceBundle;

public final class UILabelResourceManager {
	private static final Locale				DEFAULT_LOCALE				= Locale.ENGLISH;
	private static UILabelResourceManager	objUILabelResourceManager	= null;
	
	public static final String				TEXT_BUTTON_LOGIN			= "BUTTON_LOGIN";
	public static final String				TEXT_BUTTON_CLOSE			= "BUTTON_CLOSE";

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singleton class :(");
	}

	private UILabelResourceManager() {
	}

	public static UILabelResourceManager getInstance() {
		if (objUILabelResourceManager == null) {
			objUILabelResourceManager = new UILabelResourceManager();
		}
		return objUILabelResourceManager;
	}

	public String getString(String key) {
		if (ApplicationContext.getCurrentUser() == null) {
			return getString(key, DEFAULT_LOCALE);
		} else {
			return getString(key, ApplicationContext.getCurrentUser()
					.getLocale());
		}
	}

	public String getString(String key, Locale locale) {
		if (locale == null) {
			locale = DEFAULT_LOCALE;
		}
		return ResourceBundle.getBundle("languages.dotproject", locale).getString(key);
	}
}