package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.zycus.dotproject.api.IUserManager;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.factory.CompanyManagerFactory;
import com.zycus.dotproject.factory.UserManagerFactory;
import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;

public class UserManagementPanel extends CustomJPanel implements ButtonBarListener, ListSelectionListener {
	private ButtonBar		buttonBar		= null;
	private JList			lstUsers		= null;
	private IUserManager	userManager		= null;
	private JTextField		userName		= null;
	private JTextField		userFirstName	= null;
	private JTextField		userLastName	= null;
	private List<BOUser>	users			= null;
	private JTextField		userTitle		= null;
	private JTextField		userPhone		= null;
	private JTextField		userEmail		= null;
	private JTextField		userMobile		= null;
	private DateComboBox	userDOB			= null;
	private JComboBox		userType		= null;
	private JComboBox		userCompany		= null;

	public UserManagementPanel() {
		init();
		initLayout();
	}

	private void init() {
		if (ApplicationContext.getCurrentUser().getUserType() == UserType.Administrator) {
			buttonBar = new ButtonBar(ButtonBar.ADD | ButtonBar.SAVE | ButtonBar.DELETE | ButtonBar.CANCEL);
		} else {
			buttonBar = new ButtonBar(ButtonBar.SAVE | ButtonBar.CANCEL);
			buttonBar.enableButton(ButtonBar.SAVE, false);
		}
		buttonBar.addButtonBarListener(this);
		userManager = UserManagerFactory.getUserManager();
		userName = new JTextField();
		userFirstName = new JTextField();
		userLastName = new JTextField();
		userTitle = new JTextField();
		userPhone = new JTextField();
		userEmail = new JTextField();
		userMobile = new JTextField();
		userDOB = new DateComboBox();
		userCompany = new JComboBox(CompanyManagerFactory.getCompanyManager().getAllCompanies(ApplicationContext.getCurrentUser()).toArray());
		userType = new JComboBox(UserType.values());

		updateUserList();
		lstUsers = new JList(new UserListModel());
		lstUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstUsers.addListSelectionListener(this);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (lstUsers.getSelectedIndex() >= 0) {
			userSelected((BOUser) lstUsers.getSelectedValue());
		}
	}

	private void userSelected(BOUser user) {
		userName.setText(user.getLoginName());
		userFirstName.setText(user.getContact().getFirstName());
		userLastName.setText(user.getContact().getLastName());
		userTitle.setText(user.getContact().getContactTitle());
		userPhone.setText(user.getContact().getPhoneNumber());
		userMobile.setText(user.getContact().getContactMobile());
		userEmail.setText(user.getContact().getContactEmail());
		userDOB.setSelectedItem(user.getContact().getDateOfBirth());
		userType.setSelectedItem(user.getUserType());
		if (ApplicationContext.getCurrentUser().getUserType() != UserType.Administrator) {
			if (user.getUserID() == ApplicationContext.getCurrentUser().getUserID()) {
				buttonBar.enableButton(ButtonBar.SAVE, true);
			} else {
				buttonBar.enableButton(ButtonBar.SAVE, false);
			}
		}
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		JPanel pnlEditorArea = new JPanel(new BorderLayout());
		pnlEditorArea.add(buttonBar, BorderLayout.SOUTH);
		JPanel pnlFields = new JPanel(new GridLayout(9, 2));

		// pnlFields.add(new JLabel("User Title"));
		// pnlFields.add(userTitle);

		pnlFields.add(new JLabel("User Login Name"));
		pnlFields.add(userName);

		pnlFields.add(new JLabel("User Type"));
		pnlFields.add(userType);
		
		pnlFields.add(new JLabel("Company"));
		pnlFields.add(userCompany);

		pnlFields.add(new JLabel("User First Name"));
		pnlFields.add(userFirstName);

		pnlFields.add(new JLabel("User Last Name"));
		pnlFields.add(userLastName);

		pnlFields.add(new JLabel("User Phone"));
		pnlFields.add(userPhone);

		pnlFields.add(new JLabel("User Mobile"));
		pnlFields.add(userMobile);

		pnlFields.add(new JLabel("User Email"));
		pnlFields.add(userEmail);

		pnlFields.add(new JLabel("User Date of Birth"));
		pnlFields.add(userDOB);

		pnlEditorArea.add(pnlFields, BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(lstUsers), new JScrollPane(pnlEditorArea));
		splitPane.setDividerLocation(200);
		add(splitPane, BorderLayout.CENTER);
	}

	private void updateUserList() {
		if (ApplicationContext.getCurrentUser().getUserType() == UserType.Administrator || ApplicationContext.getCurrentUser().getUserType() == UserType.CEO) {
			users = userManager.getAllUsers();
		} else {
			if (users == null) {
				users = new ArrayList<BOUser>();
			}
			users.clear();
			users.add(ApplicationContext.getCurrentUser());
		}
		Collections.sort(users);
	}

	private class UserListModel implements ListModel {

		public void addListDataListener(ListDataListener l) {
		}

		public Object getElementAt(int index) {
			return users.get(index);
		}

		public int getSize() {
			return users.size();
		}

		public void removeListDataListener(ListDataListener l) {
		}
	}

	public void onAdd() {
		userName.setText("");
		userFirstName.setText("");
		userLastName.setText("");
		lstUsers.clearSelection();
		userTitle.setText("");
		userTitle.setText("");
		userPhone.setText("");
		userMobile.setText("");
		userEmail.setText("");
		userDOB.setSelectedItem(new Date());
		userType.setSelectedIndex(-1);
	}

	public void onCancel() {
		DialogUtility.closeParent(this);
	}

	public void actionPerformed(ActionType actionType) {
		if(actionType == ActionType.Cancel) {
			onCancel();
		}else if(actionType == ActionType.Add) {
			onAdd();
		}else if(actionType == ActionType.Delete) {
			onDelete();
		}else if(actionType == ActionType.Save) {
			onSave();
		}
	}
	
	private boolean validateEntry() {
		if (userName.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please provide user name", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (userType.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select user type", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (userFirstName.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please provide user first name", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (userLastName.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please provide user last name", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void onSave() {
		if(validateEntry() == false) {
			return;
		}
		BOUser user = new BOUser();
		if(lstUsers.getSelectedValue() != null) {
			user = (BOUser)lstUsers.getSelectedValue();
		}else {
			user.setPassword("zycus123");
		}
		user.setLoginName(userName.getText());
		user.setUserType((UserType)userType.getSelectedItem());
		user.getContact().setFirstName(userFirstName.getText());
		user.getContact().setLastName(userLastName.getText());
		user.getContact().setPhoneNumber(userPhone.getText());
		user.getContact().setContactMobile(userMobile.getText());
		
		if(userDOB.getSelectedItem() == null) {
			user.getContact().setDateOfBirth(null);
		}else {
			user.getContact().setDateOfBirth(new Date(userDOB.getSelectedItem().toString()));	
		}
		
		userManager.saveUser(ApplicationContext.getCurrentUser(), user);
		initReload(user);
	}

	private void initReload(BOUser user) {
		final Object[] usersForSorting = userManager.getAllUsers().toArray();
		Arrays.sort(usersForSorting);
		lstUsers.setModel(new AbstractListModel() {
			public int getSize() {
				return usersForSorting.length;
			}

			public Object getElementAt(int index) {
				return usersForSorting[index];
			}
		});
		if(user != null) {
			lstUsers.setSelectedValue(user, true);
		}else {
			onAdd();
		}
	}

	private void onDelete() {
		if(lstUsers.getSelectedValue() == null) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select project to delete", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int returnCode = JOptionPane.showConfirmDialog(ApplicationContext.getCurrentFrame(), "Do you want to delete the selected project", "Confirm: ", JOptionPane.YES_NO_OPTION);
		if(returnCode == JOptionPane.NO_OPTION) {
			return;
		}
		userManager.deleteUser(ApplicationContext.getCurrentUser(), (BOUser)lstUsers.getSelectedValue());
		initReload(null);
	}
	
	@Override
	public void onExit() {
		buttonBar.removeButtonBarListener(this);
		buttonBar.onExit();
		buttonBar = null;
		
		userManager = null;
		
		lstUsers.removeListSelectionListener(this);
		lstUsers.removeAll();
		lstUsers = null;
		
		userName = null;
		userFirstName = null;
		userLastName = null;
		userTitle = null;
		userPhone = null;
		userMobile = null;
		userEmail = null;
		
		userCompany.removeAllItems();
		userCompany = null;
		
		userType.removeAllItems();
		userType = null;
		
		userDOB = null;
	}
}