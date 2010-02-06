package com.zycus.dotproject.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPreferences implements Externalizable {
	private static final long			serialVersionUID		= 1L;
	
	public static enum LookAndFeel{
		Nimbus("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"), 
		WindowsClassic("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"), 
		Metal("javax.swing.plaf.metal.MetalLookAndFeel"),
		Motif("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		
		private String className = null;
		
		private LookAndFeel(String a_className) {
			className = a_className;
		}
		
		public String getClassName() {
			return className;
		}
	}

	private Map<String, Serializable>	value					= new HashMap<String, Serializable>();
	private static final String			DATE_DISPLAY_FORMAT		= "date.display.format";
	private static final String			LAST_LOGGED_IN_USER_ID	= "last.logged.in.user.id";
	private static final String			DATASOURCE_LIST			= "datasource.list";
	private static final String			USER_SELECTED_DIR		= "user.selected.dir";
	private static final String			USER_COLUMNS_SETTINGS	= "user.columns.settings";
	private static final String			CURRENT_LOOK_AND_FEEL	= "current.look.and.feel";
	
	public LookAndFeel getCurrentLookAndFeel() {
		if (value.get(CURRENT_LOOK_AND_FEEL) == null) {
			value.put(CURRENT_LOOK_AND_FEEL, LookAndFeel.Metal);
		}
		return (LookAndFeel)value.get(CURRENT_LOOK_AND_FEEL);
	}
	
	public void setCurrentLookAndFeel(LookAndFeel lnf) {
		if(lnf == null) {
			value.put(CURRENT_LOOK_AND_FEEL, LookAndFeel.Metal);
		}
		value.put(CURRENT_LOOK_AND_FEEL, lnf);
	}
	
	public ArrayList<Boolean> getColumnsSettings() {
		if (value.get(USER_COLUMNS_SETTINGS) == null) {
			value.put(USER_COLUMNS_SETTINGS, new ArrayList<Boolean>());
		}
		return (ArrayList<Boolean>)value.get(USER_COLUMNS_SETTINGS);
	}
	
	public void setColumnsSettings(ArrayList<Boolean> columns) {
		value.put(USER_COLUMNS_SETTINGS, columns);
	}

	public String getUserSelectedDir() {
		if (value.get(USER_SELECTED_DIR) == null) {
			return null;
		}
		return value.get(USER_SELECTED_DIR).toString();
	}

	public void setUserSelectedDir(String userDir) {
		value.put(USER_SELECTED_DIR, userDir);
	}

	public ArrayList<DataSource> getDataSources() {
		if (value.containsKey(DATASOURCE_LIST) == false) {
			value.put(DATASOURCE_LIST, new ArrayList<DataSource>());
		}
		if (value.get(DATASOURCE_LIST) == null) {
			value.put(DATASOURCE_LIST, new ArrayList<DataSource>());
		}
		return (ArrayList<DataSource>) value.get(DATASOURCE_LIST);
	}

	public void setDataSources(ArrayList<DataSource> dataSources) {
		value.put(DATASOURCE_LIST, dataSources);
	}

	public String getDateDisplayFormat() {
		if (value.containsKey(DATE_DISPLAY_FORMAT) == false) {
			value.put(DATE_DISPLAY_FORMAT, "dd-MMM-yyyy");
		}
		return (String) value.get(DATE_DISPLAY_FORMAT);
	}

	public void setDateDisplayFormat(String dateDisplayFormat) {
		if (dateDisplayFormat == null) {
			return;
		}
		value.put(DATE_DISPLAY_FORMAT, dateDisplayFormat);
	}

	public void setLastLoggedInUserID(String lastLoggedInUserID) {
		if (lastLoggedInUserID == null) {
			return;
		}
		value.put(LAST_LOGGED_IN_USER_ID, lastLoggedInUserID);
	}

	public String getLastLoggedInUserID() {
		if (value.containsKey(LAST_LOGGED_IN_USER_ID) == false) {
			value.put(LAST_LOGGED_IN_USER_ID, "");
		}
		return (String) value.get(LAST_LOGGED_IN_USER_ID);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		value = (Map<String, Serializable>) in.readObject();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

}