package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOTaskLog;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.factory.UserManagerFactory;
import com.zycus.dotproject.ui.component.datepicker.CalendarADC;
import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.NumericDocument;
import com.zycus.dotproject.util.NumericDocument.DocumentType;

public class TaskLogPanel extends CustomJPanel implements ButtonBarListener {
	private JTextField			logName			= null;
	private JTextField			logDuration		= null;
	private JTextField			taskPercentage	= null;
	private JTextArea			logDescription	= null;
	private CalendarADC		logDate			= null;
	private JComboBox			creator			= null;
	private ButtonBar			btnBar			= null;
	private BOTaskLog			log				= null;
	private SimpleDateFormat	formatter		= null;
	private BOTask				task			= null;
	private JCheckBox			taskAssignees	= null;
	private JCheckBox			taskContacts	= null;
	private JCheckBox			projectContacts	= null;

	public TaskLogPanel(BOTaskLog log, BOTask task) {
		this.log = log;
		this.task = task;
		initLogEditor();
		initLayoutLogEditor();
	}

	private void initLogEditor() {
		taskAssignees = new JCheckBox("Task Assignees");
		taskContacts = new JCheckBox("Task Contacts");
		projectContacts = new JCheckBox("Project Contacts");
		
		formatter = new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());
		logName = new JTextField();
		logDuration = new JTextField();
		taskPercentage = new JTextField();
		logDuration.setHorizontalAlignment(JTextField.RIGHT);
		logDescription = new JTextArea(2, 10);
		logDate = new CalendarADC();
		logDate.setDate(new Date());
		creator = new JComboBox(UserManagerFactory.getUserManager().getAllUsers().toArray());
		creator.setSelectedItem(ApplicationContext.getCurrentUser());
		if (log == null) {
			btnBar = new ButtonBar(ButtonBar.SAVE | ButtonBar.CANCEL);
		} else {
			btnBar = new ButtonBar(ButtonBar.CANCEL);
		}
		btnBar.addButtonBarListener(this);

		logDuration.setDocument(new NumericDocument(DocumentType.Double));
		taskPercentage.setDocument(new NumericDocument(DocumentType.Double));
		taskPercentage.setHorizontalAlignment(JTextField.RIGHT);

		if (log != null) {
			logName.setText(log.getTaskLogName());
			logDescription.setText(log.getTaskLogDescription());
			logDescription.setCaretPosition(0);
			logName.setCaretPosition(0);
			logDuration.setText(log.getTaskLogHours() + "");
			logDate.setDate(log.getTaskLogDate());
			creator.setSelectedItem(log.getTaskLogCreator());
		}else {
			logName.setText(task.getTaskName());
			logDescription.setText(task.getTaskName() + " : completed");
		}
		taskPercentage.setText(task.getPercentageCompleted() + "");
	}

	private void initLayoutLogEditor() {
		JPanel pnlNorth = new JPanel();
		if (log == null && task.getPercentageCompleted() < 100) {
			pnlNorth.setLayout(new GridLayout(5, 2));
		} else {
			pnlNorth.setLayout(new GridLayout(4, 2));
		}
		pnlNorth.add(new JLabel("Log Name"));
		pnlNorth.add(logName);

		pnlNorth.add(new JLabel("Log Duration"));
		pnlNorth.add(logDuration);

		if (log == null && task.getPercentageCompleted() < 100) {
			pnlNorth.add(new JLabel("Task Percentage"));
			pnlNorth.add(taskPercentage);
		}

		pnlNorth.add(new JLabel("Log Date"));
		pnlNorth.add(logDate);

		pnlNorth.add(new JLabel("Log Creator"));
		pnlNorth.add(creator);

		JPanel pnlMiddle = new JPanel(new GridLayout(1, 2));

		pnlMiddle.add(new JLabel("Log Description"));
		pnlMiddle.add(new JScrollPane(logDescription));
		
		JPanel pnlSouth = new JPanel(new BorderLayout());
		
		if(log == null) {
			JPanel pnlChk = new JPanel(new FlowLayout(FlowLayout.CENTER));
			pnlChk.setBorder(new TitledBorder("Email Log to:"));
			pnlChk.add(taskAssignees);
			pnlChk.add(taskContacts);
			pnlChk.add(projectContacts);
			
			pnlSouth.add(pnlChk, BorderLayout.NORTH);
		}
		
		pnlSouth.add(btnBar, BorderLayout.SOUTH);
		setLayout(new BorderLayout());
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlMiddle, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
	}

	public void onCancel() {
		DialogUtility.closeParent(this);

	}

	public void onSave() {
		if (logName.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(this, "Log name is blank", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (logDuration.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(this, "Duration is blank", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (logDescription.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(this, "Log description is blank", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (Double.parseDouble(taskPercentage.getText()) < task.getPercentageCompleted()) {
			JOptionPane.showMessageDialog(this, "Task completion percentage can't be less then " + task.getPercentageCompleted() + " %", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (Double.parseDouble(taskPercentage.getText()) > 100) {
			JOptionPane.showMessageDialog(this, "Task completion percentage can't be more then 100%", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (log == null) {
			log = new BOTaskLog();
			log.setEnhancedTaskLogID(-System.currentTimeMillis());
		}
		log.setTaskLogName(logName.getText());
		log.setTaskLogDescription(logDescription.getText());
		log.setTaskLogDate(logDate.getDate());
		log.setTaskLogHours(Float.parseFloat(logDuration.getText()));
		log.setTaskLogCreator((BOUser) creator.getSelectedItem());
		task.getTaskLogs().add(log);
		task.setPercentageCompleted(Float.parseFloat(taskPercentage.getText()));
		log.setTaskLogTask(task);
		onCancel();
	}

	public void actionPerformed(ActionType actionType) {
		if (actionType == ActionType.Cancel) {
			onCancel();
		} else if (actionType == ActionType.Save) {
			onSave();
		}
	}

	@Override
	public void onExit() {
		btnBar.removeButtonBarListener(this);
		btnBar.onExit();
		btnBar = null;

		formatter = null;
		logName = null;

		taskPercentage = null;
		logDescription = null;
		logDuration = null;
		logDate = null;

		creator.removeAllItems();
		creator = null;
	}
}