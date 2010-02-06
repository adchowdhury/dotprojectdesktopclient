package com.zycus.dotproject.util;

public class ApplicationUtility {
	public static final boolean isRunningFromJar() {
		return ApplicationUtility.class.getClassLoader().getResource("hibernate.cfg.xml").getPath().contains("!");
	}
}