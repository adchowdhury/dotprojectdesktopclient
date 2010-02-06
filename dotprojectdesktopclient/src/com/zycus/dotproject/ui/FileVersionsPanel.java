package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.zycus.dotproject.api.IFileManager;
import com.zycus.dotproject.bo.BOFile;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.FileCategory;
import com.zycus.dotproject.factory.FileManagerFactory;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.MenuUtility;

public class FileVersionsPanel extends CustomJPanel {
	private String[]				filesCols				= { "Name", "Description", "Version", "Category", "Size", "Owner", "Date" };
	private JTable					taskFiles				= null;
	private JPopupMenu				taskFilesPopupMenu		= null;
	private List<BOFile>			files					= null;
	private static SimpleDateFormat	dateFormatter			= new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());
	private IFileManager			fileManager				= null;
	private FileTableModel			fileTableModel			= null;
	private JMenuItem				viewFileMenu			= null;
	private FileActions				fileActions				= null;
	private FileTableMouseAdapter	fileTableMouseAdapter	= null;

	public FileVersionsPanel(List<BOFile> files) {
		if (files == null) {
			throw new IllegalArgumentException("null parameter not allowed");
		}
		this.files = files;
		init();
		initLayout();
	}

	private void init() {
		fileTableMouseAdapter = new FileTableMouseAdapter();
		fileActions = new FileActions();
		taskFiles = new JTable(fileTableModel = new FileTableModel(files));
		taskFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskFiles.setAutoCreateRowSorter(true);
		fileManager = FileManagerFactory.getFileManager();
		taskFilesPopupMenu = new JPopupMenu();
		
		taskFilesPopupMenu.add(viewFileMenu = MenuUtility.getMenuItem("Download", fileActions));
		viewFileMenu.setIcon(IconHelper.getDownloadIcon());
		taskFiles.addMouseListener(fileTableMouseAdapter);
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(taskFiles));
	}

	@Override
	public void onExit() {

	}

	private class FileTableModel extends DefaultTableModel {
		private List<BOFile>	files	= new ArrayList<BOFile>();

		public FileTableModel(List<BOFile> files) {
			this.files = files;
		}

		public void updateFileList(List<BOFile> files) {
			this.files = files;
			fireTableDataChanged();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return String.class;
			} else if (columnIndex == 1) {
				return String.class;
			} else if (columnIndex == 2) {
				return Float.class;
			} else if (columnIndex == 3) {
				return FileCategory.class;
			} else if (columnIndex == 4) {
				return Long.class;
			} else if (columnIndex == 5) {
				return BOUser.class;
			} else if (columnIndex == 6) {
				return String.class;
			}
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return filesCols.length;
		}

		@Override
		public String getColumnName(int column) {
			return filesCols[column];
		}

		@Override
		public int getRowCount() {
			if (files == null) {
				return 0;
			}
			return files.size();
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == -1) {
				return files.get(row);
			} else if (column == 0) {
				return files.get(row).getRealFilename();
			} else if (column == 1) {
				return files.get(row).getFileDescription();
			} else if (column == 2) {
				return files.get(row).getFileVersion();
			} else if (column == 3) {
				return files.get(row).getFileCategory();
			} else if (column == 4) {
				return files.get(row).getFileSize();
			} else if (column == 5) {
				return files.get(row).getFileOwner();
			} else if (column == 6) {
				if (dateFormatter != null) {
					return dateFormatter.format(files.get(row).getFileDate());
				} else {
					return files.get(row).getFileDate() + "";
				}
			}
			return "";
		}
	}

	private class FileActions extends AbstractAction {
		public void actionPerformed(ActionEvent actionPerformed) {
			onViewFile();
		}
	}

	private void onViewFile() {
		int selectedIndex = taskFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void cancelSelection() {
				Map<Thread, StackTraceElement[]> stackTrace = Thread.getAllStackTraces();
				if (stackTrace.get(Thread.currentThread())[4].getFileName().contains("AbstractButton")) {
					super.cancelSelection();
				}
			}
		};
		File f = null;
		if (ApplicationContext.getUserPreferences().getUserSelectedDir() != null) {
			f = new File(ApplicationContext.getUserPreferences().getUserSelectedDir() + File.separator + taskFile.getRealFilename());
		} else {
			f = new File(System.getProperty("java.io.tmpdir") + taskFile.getRealFilename());

		}
		fileChooser.setSelectedFile(f);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnOption = fileChooser.showSaveDialog(ApplicationContext.getCurrentFrame());
		if (JFileChooser.CANCEL_OPTION == returnOption
				|| JFileChooser.ERROR_OPTION == returnOption) {
			return;
		}
		if (fileChooser.getSelectedFile() == null) {
			return;
		}
		f = fileChooser.getSelectedFile();
		fileManager.saveFile(taskFile, f);
		try {
			Desktop.getDesktop().open(f);
		} catch (IOException a_excp) {
			a_excp.printStackTrace();
		}
		ApplicationContext.getUserPreferences().setUserSelectedDir(fileChooser.getCurrentDirectory().getAbsolutePath());
		ApplicationContext.saveSettings();
	}

	private class FileTableMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON3) {
				int selectedRow = taskFiles.getSelectedRow();
				if (selectedRow >= 0) {
					taskFilesPopupMenu.show((Component) event.getSource(), event.getX(), event.getY());
				}
			}else {
				if(event.getClickCount() == 2) {
					int selectedRow = taskFiles.getSelectedRow();
					if (selectedRow >= 0) {
						onViewFile();
					}
				}
			}
		}
	}
}