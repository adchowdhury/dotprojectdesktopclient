package com.zycus.dotproject.ui;

import static com.zycus.dotproject.util.UILabelResourceManager.TEXT_BUTTON_CLOSE;
import static com.zycus.dotproject.util.UILabelResourceManager.TEXT_BUTTON_LOGIN;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.zycus.dotproject.api.IUserManager;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.exception.GenericException;
import com.zycus.dotproject.factory.UserManagerFactory;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.UILabelResourceManager;

public class LoginScreen extends JPanel implements ActionListener {

	private JTextField		txtUserName	= null;
	private JPasswordField	txtPass		= null;
//	private JComboBox		userList	= null;

	private JButton			btnOK		= null;
	private JButton			btnClose	= null;

	private JDialog			parent		= null;

	private boolean			loginStatus	= false;
	private IUserManager	userManager	= null;

	public LoginScreen(JDialog parent) {
		this.parent = parent;
		init();
		layoutComponents();
	}

	private void init() {
		userManager = UserManagerFactory.getUserManager();
//		userList = new JComboBox();
//		userList.setEditable(false);
//		List<BOUser> users = userManager.getAllUsers();
//		Collections.sort(users);
//		for(BOUser user : users) {
//			//System.out.println(user.toString() + "[" + user.getContact().getFirstName() + "]");
//			userList.addItem(user);
//		}
		txtUserName = new JTextField(ApplicationContext.getUserPreferences().getLastLoggedInUserID(), 15);
		txtPass = new JPasswordField("", 15);

		btnOK = new JButton(UILabelResourceManager.getInstance().getString(TEXT_BUTTON_LOGIN));
		btnOK.setIcon(IconHelper.getLoginIcon());
		btnClose = new JButton(UILabelResourceManager.getInstance().getString(TEXT_BUTTON_CLOSE));
		btnClose.setIcon(IconHelper.getCloseIcon());
		
		txtUserName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent event) {
				btnOK.setEnabled(txtUserName.getText().trim().length() > 0);
			}
		});
		txtUserName.addActionListener(this);
		txtPass.addActionListener(this);
		btnClose.addActionListener(this);
		btnOK.addActionListener(this);

		//btnOK.setEnabled(false);

		/*
		 * Toolkit.getDefaultToolkit().addAWTEventListener(new
		 * AWTEventListener() { public void eventDispatched(AWTEvent event) {
		 * System.out.println(".eventDispatched()"); } },
		 * AWTEvent.KEY_EVENT_MASK);
		 */
	}

	private void layoutComponents() {
		JPanel pnlLbl = new JPanel(new GridLayout(2, 1));
		pnlLbl.add(new JLabel("UserName"));
		pnlLbl.add(new JLabel("Password"));

		JPanel pnlTxt = new JPanel(new GridLayout(2, 1));
		pnlTxt.add(txtUserName);
		//pnlTxt.add(userList);
		pnlTxt.add(txtPass);

		JPanel pnlMiddle = new JPanel(new BorderLayout(15, 0));
		pnlMiddle.add(pnlLbl, BorderLayout.WEST);
		pnlMiddle.add(pnlTxt, BorderLayout.CENTER);

		JPanel pnlBtn = new JPanel();
		pnlBtn.add(btnOK);
		pnlBtn.add(btnClose);

		setLayout(new BorderLayout());
		add(pnlBtn, BorderLayout.SOUTH);
		add(pnlMiddle, BorderLayout.NORTH);
	}

	public boolean getLoginStatus() {
		return loginStatus;
	}

	public void actionPerformed(ActionEvent event) {
		if (event == null) {
			return;
		}

		Object source = event.getSource();
		if (source == null) {
			return;
		}

		if (source == btnClose) {
			parent.dispose();
		} else if (source == btnOK || source == txtUserName || source == txtPass) {
			if(txtUserName.getText().trim().length() <= 0) {
				JOptionPane.showMessageDialog(parent, "Blank username", "Validation", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				BOUser user = userManager.getUser(txtUserName.getText(), txtPass.getText());
				if (user != null) {
					loginStatus = true;
					parent.dispose();
					ApplicationContext.setCurrentUser(user);
					ApplicationContext.getUserPreferences().setLastLoggedInUserID(user.getLoginName());
					ApplicationContext.saveSettings();
				} else {
					JOptionPane.showMessageDialog(parent, "Invalid username/password", "Validation", JOptionPane.ERROR_MESSAGE);
				}
			}catch(GenericException gExcp) {
				if(gExcp.getType() == GenericException.Type.Database) {
					JOptionPane.showMessageDialog(parent, "Could not connect to database server", "Connection problem", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}