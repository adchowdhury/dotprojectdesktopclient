package com.zycus.dotproject.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.zycus.dotproject.ui.CustomJPanel;
import com.zycus.dotproject.ui.IconHelper;

public class DialogUtility {
	private static final Dimension	standardDimention	= new Dimension(600, 400);
	private static JDialog			waitDialog			= null;
	private static boolean			isWaitDialogOn		= false;
	private static JLabel			waitDialogLabel		= null;
	private static JProgressBar		waitProgressBar		= null;

	public static void showDialog(CustomJPanel child, String strCaption) {
		showDialog(child, strCaption, standardDimention);
	}
	
	public static void showDialog(JComponent child, String strCaption) {
		showDialog(child, strCaption, standardDimention);
	}

	public static JDialog showDialog(CustomJPanel child, String strCaption, Dimension size) {
		return showDialog(child, strCaption, size, true);
	}
	
	public static JDialog showDialog(JComponent child, String strCaption, Dimension size) {
		return showDialog(child, strCaption, size, true);
	}

	public static JDialog showDialog(final JComponent child, String strCaption, Dimension size, boolean isResizable) {
		JDialog dialog = new JDialog(ApplicationContext.getCurrentFrame(), strCaption) {
			@Override
			public void dispose() {
				if(child instanceof CustomJPanel) {
					((CustomJPanel)child).onExit();	
				}				
				super.dispose();
			}
		};
		dialog.setLayout(new BorderLayout());
		
		dialog.add(child, BorderLayout.CENTER);
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setSize(size);
		if(!size.equals(standardDimention)) {
			dialog.setMinimumSize(size);
		}
		dialog.setLocationRelativeTo(ApplicationContext.getCurrentFrame());
		dialog.setResizable(isResizable);
		if(waitDialog != null)
			hideWaitDialog();
		dialog.setVisible(true);
		return dialog;
	}

	public static JDialog getRoot(JComponent pchildComponent) {
		Container objParent = pchildComponent.getParent();
		while (objParent != null) {
			objParent = objParent.getParent();
			if (objParent instanceof JDialog) {
				return (JDialog) objParent;
			}
		}
		return null;
	}

	public static void closeParent(JComponent childComponent) {
		JDialog parent = getRoot(childComponent);
		if (parent != null) {
			parent.dispose();
		}
	}

	public static void showWaitDialog(String waitingText) {
		if (waitDialog == null) {
			initWaitDialog();
		}
		isWaitDialogOn = true;
		waitDialogLabel.setText(waitingText);
		waitDialog.setSize(new Dimension(350, 100));
		waitDialog.setLocationRelativeTo(null);
		waitProgressBar.setIndeterminate(true);
		waitDialog.setVisible(true);
	}

	private static void initWaitDialog() {
		waitDialog = new JDialog(ApplicationContext.getCurrentFrame(), "Please Wait");
		waitDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		waitDialog.setSize(new Dimension(350, 100));
		waitDialog.setLayout(new BorderLayout());
		waitDialogLabel = new JLabel();
		waitProgressBar = new JProgressBar();
		waitDialog.setModal(true);
		waitDialogLabel.setIcon(IconHelper.getInformationIcon());
		waitDialog.add(waitDialogLabel, BorderLayout.CENTER);
		waitDialog.add(waitProgressBar, BorderLayout.SOUTH);
	}

	public static void hideWaitDialog() {
		waitProgressBar.setIndeterminate(false);
		waitDialog.setVisible(false);
		isWaitDialogOn = false;
	}

	public static boolean isWaitDialogOn() {
		return isWaitDialogOn;
	}
}