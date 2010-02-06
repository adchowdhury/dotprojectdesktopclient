package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

import com.zycus.dotproject.bo.BOFile;
import com.zycus.dotproject.bo.FileCategory;
import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;

public class FileEditorPanel extends CustomJPanel implements ButtonBarListener, ActionListener {
	public static enum FileEditorMode {
		FileAdd, FileCheckIn, FileEdit
	}

	private JTextArea		txtFileDescription	= null;
	private JComboBox		cmbFileCategory		= null;
	private JButton			btnFileChooser		= null;
	private JLabel			lblFileName			= null;
	private ButtonBar		btns				= null;
	private BOFile			file				= null;
	private File			realFile			= null;
	private FileEditorMode	fileEditorMode		= FileEditorMode.FileAdd;
	private BOFile			taskFile			= null;
	private Properties		properties			= new Properties();

	public FileEditorPanel(FileEditorMode a_fileEditorMode) {
		fileEditorMode = a_fileEditorMode;
		init();
		initLayout();
	}

	private void init() {
		btns = new ButtonBar(ButtonBar.SAVE | ButtonBar.CANCEL);
		btns.addButtonBarListener(this);
		txtFileDescription = new JTextArea(2, 10);
		cmbFileCategory = new JComboBox(FileCategory.values());
		btnFileChooser = new JButton(IconHelper.getBrowseIcon());
		btnFileChooser.setMargin(new Insets(0, 0, 0, 0));
		btnFileChooser.setToolTipText("Browse file for adding...");
		btnFileChooser.addActionListener(this);
		lblFileName = new JLabel("No file selected");
	}

	private void initLayout() {
		setLayout(new BorderLayout());

		JPanel pnlNorth = new JPanel(new GridLayout(fileEditorMode == FileEditorMode.FileAdd || fileEditorMode == FileEditorMode.FileEdit ? 2 : 1, 2));
		JPanel pnlNorthRight = new JPanel(new BorderLayout());
		pnlNorthRight.add(lblFileName, BorderLayout.CENTER);
		pnlNorthRight.add(btnFileChooser, BorderLayout.EAST);

		pnlNorth.add(new JLabel("Select File"));
		pnlNorth.add(pnlNorthRight);
		if (fileEditorMode == FileEditorMode.FileAdd || fileEditorMode == FileEditorMode.FileEdit) {
			pnlNorth.add(new JLabel("File Category"));
			pnlNorth.add(cmbFileCategory);
		}
		add(pnlNorth, BorderLayout.NORTH);
		add(new JScrollPane(txtFileDescription), BorderLayout.CENTER);
		add(btns, BorderLayout.SOUTH);
	}

	@Override
	public void onExit() {

	}

	public BOFile getFile() {
		return file;
	}

	public File getRealFile() {
		return realFile;
	}

	private boolean validateEntry() {
		if (txtFileDescription.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(this, "File Description is blank", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (fileEditorMode != FileEditorMode.FileEdit) {
			if (lblFileName.getText().equalsIgnoreCase("No file selected")) {
				JOptionPane.showMessageDialog(this, "No file is selected, please selecta file to attach", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}

	public void actionPerformed(ActionType actionType) {
		if (actionType == ActionType.Cancel) {
			file = null;
			realFile = null;
			DialogUtility.closeParent(this);
		} else if (actionType == ActionType.Save) {
			if (validateEntry() == false) {
				return;
			}
			file = new BOFile();
			file.setFileDescription(txtFileDescription.getText());
			file.setFileCategory((FileCategory) cmbFileCategory.getSelectedItem());
			file.setRealFilename(realFile.getName());
			file.setFileMimeType(FileSystemView.getFileSystemView().getSystemTypeDescription(realFile));
			file.setFileVersion(1.0f);
			file.setFileDate(new Date());
			file.setFileOwner(ApplicationContext.getCurrentUser());
			file.setFileSize((int) realFile.length());
			DialogUtility.closeParent(this);
		}
	}

	private void attachFile() {
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void cancelSelection() {
				Map<Thread, StackTraceElement[]> stackTrace = Thread.getAllStackTraces();
				if (stackTrace.get(Thread.currentThread())[4].getFileName().contains("AbstractButton")) {
					super.cancelSelection();
				}
			}
		};
		if (ApplicationContext.getUserPreferences().getUserSelectedDir() != null) {
			fileChooser.setCurrentDirectory(new File(ApplicationContext.getUserPreferences().getUserSelectedDir()));
		}
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.showOpenDialog(ApplicationContext.getCurrentFrame());
		File f = fileChooser.getSelectedFile();
		if (f == null) {
			return;
		}
		if (f.length() > getFileSize()) {
			JOptionPane.showMessageDialog(this, "File size too much", "Validation Error", JOptionPane.ERROR_MESSAGE);
			ApplicationContext.getUserPreferences().setUserSelectedDir(fileChooser.getCurrentDirectory().getAbsolutePath());
			ApplicationContext.saveSettings();
			return;
		}
		realFile = f;
		lblFileName.setText(f.getName());
		ApplicationContext.getUserPreferences().setUserSelectedDir(fileChooser.getCurrentDirectory().getAbsolutePath());
		ApplicationContext.saveSettings();
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == btnFileChooser) {
			attachFile();
		}
	}

	public void setFile(BOFile a_taskFile) {
		taskFile = a_taskFile;
	}

	private long getFileSize() {
		if (properties.size() <= 0) {
			try {
				properties.load(getClass().getClassLoader().getResourceAsStream("dotProjectClient.properties"));
			} catch (IOException a_th) {}
		}
		String strReturn = properties.getProperty("MAXIMUM_FILE_SIZE");
		try {
			return Long.parseLong(strReturn);
		} catch (Throwable a_th) {
			return 10485760;
		}
	}

}