package com.zycus.dotproject.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import com.zycus.dotproject.util.ApplicationContext;

public class StatusBar extends JPanel {
	private static StatusBar	statusBar		= null;
	private TimeHolder			timeHolder		= new TimeHolder();
	private MessageHolder		messageHolder	= new MessageHolder();

	private StatusBar() {
		init();
	}
	
	public static void showWelcomeMessage() {
		statusBar.messageHolder.setIcon(IconHelper.getInformationIcon());
		statusBar.messageHolder.setText("Welcome : " + ApplicationContext.getCurrentUser().getLoginName());
	}

	public static void showStatusMessage(String strMessage) {
		statusBar.messageHolder.setText(strMessage);
	}
	
	public static void showInfoStatusMessage(String strMessage) {
		statusBar.messageHolder.setIcon(IconHelper.getInformationIcon());
		statusBar.messageHolder.setText(strMessage);
	}
	
	public static void showWarningStatusMessage(String strMessage) {
		statusBar.messageHolder.setIcon(IconHelper.getWarningIcon());
		statusBar.messageHolder.setText(strMessage);
	}
	
	public static void showErrorStatusMessage(String strMessage) {
		statusBar.messageHolder.setIcon(IconHelper.getErrorIcon());
		statusBar.messageHolder.setText(strMessage);
	}

	private void init() {
		// setLayout(new FlowLayout(SwingConstants.RIGHT));
		timeHolder.setBorder(new BevelBorder(BevelBorder.LOWERED));
		messageHolder.setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BorderLayout());
		add(timeHolder, BorderLayout.EAST);
		add(messageHolder, BorderLayout.CENTER);
		setBorder(new EtchedBorder());
	}

	public static StatusBar getstStatusBar() {
		if (statusBar == null) {
			statusBar = new StatusBar();
		}
		return statusBar;
	}
}