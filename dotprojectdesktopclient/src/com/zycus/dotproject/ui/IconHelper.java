package com.zycus.dotproject.ui;

import java.awt.Image;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class IconHelper {
	public IconHelper() {
		throw new IllegalAccessError("this class should not be initialised");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This class should not be cloned");
	}

	private static final Map<String, ImageIcon>	iconList			= new LinkedHashMap<String, ImageIcon>();

	private static final String					SAVE_ICON			= "save.png";
	private static final String					NEW_ICON			= "add.png";
	private static final String					REFRESH_ICON		= "refresh.png";
	private static final String					WARNING_ICON		= "warning.png";
	private static final String					INFORMATION_ICON	= "info.png";
	private static final String					ERROR_ICON			= "error.png";
	private static final String					CLOSE_ICON			= "close.png";
	private static final String					DELETE_ICON			= "delete.png";
	private static final String					PRODUCT_ICON		= "timeModule.png";
	private static final String					BROWSE_ICON			= "browse.png";
	private static final String					ADD_NEW_FILE_ICON	= "addNewFile.png";
	private static final String					CHECK_IN_ICON		= "checkIn.png";
	private static final String					CHECK_OUT_ICON		= "checkOut.png";
	private static final String					COLLAPSE_ICON		= "collapse.png";
	private static final String					DOWNLOAD_ICON		= "download.png";
	private static final String					EDIT_ICON			= "edit.png";
	private static final String					EXPAND_ICON			= "expand.png";
	private static final String					LOGIN_ICON			= "login.png";
	private static final String					PROJECT_ICON		= "project.png";
	private static final String					PROPERTIES_ICON		= "properties.png";
	private static final String					REPORT_ICON			= "report.png";
	private static final String					SETTINGS_ICON		= "settings.png";
	private static final String					TASK_ICON			= "task.png";
	private static final String					UNDO_CHECK_OUT_ICON	= "undoCheckOut.png";
	private static final String					VIEW_ALL_ICON		= "viewAll.png";

	private static final String					imageLocation		= "images/";
	
	public static Icon getViewAllIcon() {
		return getIcon(VIEW_ALL_ICON);
	}
	public static Icon getUndoCheckOutIcon() {
		return getIcon(UNDO_CHECK_OUT_ICON);
	}
	public static Icon getTaskIcon() {
		return getIcon(TASK_ICON);
	}
	public static Icon getSettingsIcon() {
		return getIcon(SETTINGS_ICON);
	}
	public static Icon getReportIcon() {
		return getIcon(REPORT_ICON);
	}
	public static Icon getPropertiesIcon() {
		return getIcon(PROPERTIES_ICON);
	}
	public static Icon getProjectIcon() {
		return getIcon(PROJECT_ICON);
	}
	public static Icon getLoginIcon() {
		return getIcon(LOGIN_ICON);
	}
	public static Icon getExpandIcon() {
		return getIcon(EXPAND_ICON);
	}
	public static Icon getEditIcon() {
		return getIcon(EDIT_ICON);
	}
	public static Icon getDownloadIcon() {
		return getIcon(DOWNLOAD_ICON);
	}
	public static Icon getCollapseIcon() {
		return getIcon(COLLAPSE_ICON);
	}
	public static Icon getCheckOutIcon() {
		return getIcon(CHECK_OUT_ICON);
	}

	public static Icon getCheckInIcon() {
		return getIcon(CHECK_IN_ICON);
	}

	public static Icon getAddNewFileIcon() {
		return getIcon(ADD_NEW_FILE_ICON);
	}

	public static Icon getBrowseIcon() {
		return getIcon(BROWSE_ICON);
	}

	public static Icon getErrorIcon() {
		return getIcon(ERROR_ICON);
	}

	public static Icon getInformationIcon() {
		return getIcon(INFORMATION_ICON);
	}

	public static Icon getWarningIcon() {
		return getIcon(WARNING_ICON);
	}

	public static Icon getDeleteIcon() {
		return getIcon(DELETE_ICON);
	}

	public static Icon getSaveIcon() {
		return getIcon(SAVE_ICON);
	}

	public static Icon getNewIcon() {
		return getIcon(NEW_ICON);
	}

	public static Icon getCloseIcon() {
		return getIcon(CLOSE_ICON);
	}

	public static Icon getRefreshIcon() {
		return getIcon(REFRESH_ICON);
	}

	public static Icon getProductLogo() {
		return getIcon(PRODUCT_ICON);
	}

	public static Image getProductLogoImage() {
		return getIcon(PRODUCT_ICON).getImage();
	}

	private static ImageIcon getIcon(String strIcon) {
		if (iconList.containsKey(strIcon) == false) {
			iconList.put(strIcon, new ImageIcon(IconHelper.class.getClassLoader().getResource(imageLocation + strIcon)));
		}
		return iconList.get(strIcon);
	}

}