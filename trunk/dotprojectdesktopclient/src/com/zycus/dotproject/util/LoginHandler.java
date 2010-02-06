package com.zycus.dotproject.util;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.zycus.dotproject.ui.IconHelper;
import com.zycus.dotproject.ui.LoginScreen;


public final class LoginHandler {

	private static LoginHandler loginHandler = null;
	
	private LoginHandler() {}
	
	public static synchronized LoginHandler getDefault()
	{
		if (loginHandler == null) {
			loginHandler = new LoginHandler();
		}
		
		return loginHandler;
	}
	
	public boolean performLogin() {
		JDialog dlgLogin = new JDialog(new JFrame(), "Login");
		dlgLogin.setIconImage(IconHelper.getProductLogoImage());
		LoginScreen loginScreen = new LoginScreen(dlgLogin);
		dlgLogin.add(loginScreen);
		dlgLogin.setModal(true);
		try
		{
		    UIManager.setLookAndFeel(ApplicationContext.getCurrentLookAndFeel().getClassName());
			SwingUtilities.updateComponentTreeUI(dlgLogin);
		}
		catch(Exception excp)
		{
		    System.out.println(excp);
		}
		dlgLogin.pack();
		dlgLogin.setLocationRelativeTo(null);
		dlgLogin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dlgLogin.setVisible(true);		
		return loginScreen.getLoginStatus();
	}
}