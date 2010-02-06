package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DataSource;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.DataSource.DatabaseType;
import com.zycus.dotproject.util.UserPreferences.LookAndFeel;

public class Settings extends CustomJPanel implements ButtonBarListener {
	private static final String[]	DateFormats			= { "dd-MMM-yyyy", "dd-MM-yyyy", "dd/MM/yy", "MM/dd/yyyy" };
	private static final String[]	columns				= { "Name", "Database Type", "Server", "Database", "Username", "Password" };

	private ButtonBar				buttonBar			= null;

	private JTable					tblDataSources		= null;

	private JComboBox				dateFormatCombo		= null;
	private JComboBox				lookAndFeel			= null;

	private JPopupMenu				mnuTable			= null;

	private JMenuItem				addDataSource		= null;
	private JMenuItem				editDataSource		= null;
	private JMenuItem				deleteDataSource	= null;

	private JScrollPane				scrlPane			= null;

	private AddAction				addAction			= null;
	private EditAction				editAction			= null;
	private DeleteAction			deleteAction		= null;
	private PopupAction				popUpAction			= null;

	public Settings() {
		init();
		initLayout();
	}

	private void init() {
		lookAndFeel = new JComboBox(LookAndFeel.values());
		lookAndFeel.setToolTipText("Best performance with default look and feel");
		if(ApplicationContext.getCurrentLookAndFeel() != null) {
			lookAndFeel.setSelectedItem(ApplicationContext.getCurrentLookAndFeel());
		}else {
			lookAndFeel.setSelectedIndex(-1);
		}
		popUpAction = new PopupAction();
		mnuTable = new JPopupMenu();
		mnuTable.add(addDataSource = new JMenuItem(addAction = new AddAction()));
		mnuTable.add(editDataSource = new JMenuItem(editAction = new EditAction()));
		mnuTable.add(deleteDataSource = new JMenuItem(deleteAction = new DeleteAction()));

		dateFormatCombo = new JComboBox(DateFormats);
		dateFormatCombo.setSelectedItem(ApplicationContext.getUserPreferences().getDateDisplayFormat());

		tblDataSources = new JTable(new DataSourceTableModel());
		tblDataSources.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblDataSources.addMouseListener(popUpAction);
		tblDataSources.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_INSERT) {
					onAddDataSource();
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					onDeleteDataSource();
				}
			}
		});

		buttonBar = new ButtonBar(ButtonBar.SAVE | ButtonBar.CANCEL);
		buttonBar.addButtonBarListener(this);

		scrlPane = new JScrollPane(tblDataSources);
		scrlPane.addMouseListener(popUpAction);
		scrlPane.setBorder(new TitledBorder("DataSources"));
	}

	private void validateMenu() {
		editDataSource.setVisible(tblDataSources.getSelectedRowCount() > 0);
		deleteDataSource.setVisible(tblDataSources.getSelectedRowCount() > 0);
	}

	public void onAddDataSource() {
		DialogUtility.showDialog(new DataSourcePanel(), "Add new datasource", new Dimension(350, 250), false);
	}

	public void onEditDataSource() {
		DialogUtility.showDialog(new DataSourcePanel((DataSource) tblDataSources.getValueAt(tblDataSources.getSelectedRow(), -1)), "Edit datasource", new Dimension(350, 250), false);
	}

	public void onDeleteDataSource() {
		((DataSourceTableModel) tblDataSources.getModel()).deleteDataSource((DataSource) tblDataSources.getValueAt(tblDataSources.getSelectedRow(), -1));
	}

	private void initLayout() {
		setLayout(new BorderLayout(5, 5));
		
		JPanel pnlGeneralTop = new JPanel(new GridLayout(2, 2));
		JPanel pnlGeneralHolder = new JPanel(new BorderLayout(5, 5));
		
		pnlGeneralTop.add(new JLabel("Date Format"));
		pnlGeneralTop.add(dateFormatCombo);
		
		pnlGeneralTop.add(new JLabel("Current Look And Feel"));
		pnlGeneralTop.add(lookAndFeel);

		// pnlTop.add(new JLabel("Show Tasks From Other Projects"));
		// pnlTop.add(showTasksFromOtherProjects);
		pnlGeneralHolder.add(pnlGeneralTop, BorderLayout.NORTH);
		JTabbedPane tab = new JTabbedPane();
		tab.addTab("General", pnlGeneralHolder);
		tab.addTab("Data Sources", scrlPane);
		
//		add(pnlTop, BorderLayout.NORTH);
//		add(scrlPane, BorderLayout.CENTER);
		add(tab, BorderLayout.CENTER);
		add(buttonBar, BorderLayout.SOUTH);
	}

	public void onCancel() {
		DialogUtility.closeParent(this);
	}

	public void onSave() {
		try {
			new SimpleDateFormat(dateFormatCombo.getSelectedItem().toString());
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, "Invalid date format", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ApplicationContext.getUserPreferences().setDateDisplayFormat(dateFormatCombo.getSelectedItem().toString());
		ApplicationContext.getUserPreferences().setDataSources(((DataSourceTableModel) tblDataSources.getModel()).getDataSources());
		if(lookAndFeel.getSelectedIndex() >= 0) {
			DialogUtility.getRoot(this).setVisible(false);
			ApplicationContext.getCurrentFrame().setVisible(false);
			ApplicationContext.setCurrentLookAndFeel((LookAndFeel)lookAndFeel.getSelectedItem());
		}
		ApplicationContext.saveSettings();
		onCancel();
	}

	private void addDataSource(DataSource dataSource) {
		((DataSourceTableModel) tblDataSources.getModel()).addDataSource(dataSource);
	}

	private class DataSourceTableModel extends DefaultTableModel {
		private ArrayList<DataSource>	dataSources	= null;

		public DataSourceTableModel() {
			if (ApplicationContext.getUserPreferences().getDataSources() != null) {
				dataSources = new ArrayList<DataSource>(ApplicationContext.getUserPreferences().getDataSources());
			}
			if (dataSources == null) {
				dataSources = new ArrayList<DataSource>();
			}
		}

		public ArrayList<DataSource> getDataSources() {
			return dataSources;
		}

		public void deleteDataSource(DataSource dataSource) {
			if (dataSources.contains(dataSource) == false) {
				return;
			}
			dataSources.remove(dataSource);
			fireTableDataChanged();
		}

		public void addDataSource(DataSource dataSource) {
			if (dataSources.contains(dataSource)) {
				return;
			}
			dataSources.add(dataSource);
			fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			if (dataSources == null) {
				dataSources = new ArrayList<DataSource>();
			}
			return dataSources.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (dataSources.size() <= row) {
				return null;
			}
			if (column == -1) {
				return dataSources.get(row);
			} else if (column == 0) {
				return dataSources.get(row).getDataSourceName();
			} else if (column == 1) {
				return dataSources.get(row).getDataBaseType();
			} else if (column == 2) {
				return dataSources.get(row).getSetverIPName();
			} else if (column == 3) {
				return dataSources.get(row).getDatabaseName();
			} else if (column == 4) {
				return dataSources.get(row).getUserName();
			} else if (column == 5) {
				return dataSources.get(row).getPassword();
			}
			return super.getValueAt(row, column);
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	private class PopupAction extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent mouseEvent) {
			if (mouseEvent.isPopupTrigger()) {
				validateMenu();
				mnuTable.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
				return;
			}
			if (mouseEvent.getClickCount() == 2 && tblDataSources.getSelectedRowCount() > 0) {
				onEditDataSource();
			}
		}
	}

	private class AddAction extends AbstractAction {
		@Override
		public Object getValue(String key) {
			if (key == NAME) {
				return "Add new datasource";
			}
			return super.getValue(key);
		}

		public void actionPerformed(ActionEvent actionEvent) {
			onAddDataSource();
		}
	}

	private class EditAction extends AbstractAction {
		@Override
		public Object getValue(String key) {
			if (key == NAME) {
				return "Edit datasource";
			}
			return super.getValue(key);
		}

		public void actionPerformed(ActionEvent actionEvent) {
			onEditDataSource();
		}
	}

	private class DeleteAction extends AbstractAction {
		@Override
		public Object getValue(String key) {
			if (key == NAME) {
				return "Delete datasource";
			}
			return super.getValue(key);
		}

		public void actionPerformed(ActionEvent actionEvent) {
			onDeleteDataSource();
		}
	}

	private class DataSourcePanel extends CustomJPanel implements ButtonBarListener {
		private JTextField	dataSourceName	= null;
		private JTextField	server			= null;
		private JTextField	databaseName	= null;
		private JTextField	username		= null;
		private JTextField	password		= null;
		private DataSource	dataSource		= null;
		private ButtonBar	btns			= null;
		private JComboBox	dataBaseType	= null;

		public DataSourcePanel() {
			this(null);
		}

		public DataSourcePanel(DataSource dataSource) {
			this.dataSource = dataSource;
			initPanels();
		}

		public void initPanels() {
			dataSourceName = new JTextField();
			server = new JTextField();
			databaseName = new JTextField();
			username = new JTextField();
			password = new JTextField();
			dataBaseType = new JComboBox(DatabaseType.values());

			btns = new ButtonBar(ButtonBar.SAVE | ButtonBar.CANCEL);
			btns.addButtonBarListener(DataSourcePanel.this);

			// --------- if edit mode lets fill data
			if (dataSource != null) {
				dataSourceName.setText(dataSource.getDataSourceName());
				server.setText(dataSource.getSetverIPName());
				databaseName.setText(dataSource.getDatabaseName());
				username.setText(dataSource.getUserName());
				password.setText(dataSource.getPassword());
				dataBaseType.setSelectedItem(dataSource.getDataBaseType());
			}

			// ----------Layout
			setLayout(new BorderLayout());
			JPanel pnlNorth = new JPanel(new GridLayout(6, 2, 5, 5));

			pnlNorth.add(new JLabel("Datasource name"));
			pnlNorth.add(dataSourceName);

			pnlNorth.add(new JLabel("Database Type"));
			pnlNorth.add(dataBaseType);

			pnlNorth.add(new JLabel("Server"));
			pnlNorth.add(server);

			pnlNorth.add(new JLabel("Database name"));
			pnlNorth.add(databaseName);

			pnlNorth.add(new JLabel("Username"));
			pnlNorth.add(username);

			pnlNorth.add(new JLabel("Password"));
			pnlNorth.add(password);

			add(pnlNorth, BorderLayout.NORTH);
			add(btns, BorderLayout.SOUTH);
		}

		public void onCancel() {
			DialogUtility.closeParent(btns);

		}

		public void onSave() {
			boolean isAdd = false;
			if (dataSource == null) {
				dataSource = new DataSource();
				isAdd = true;
			}

			dataSource.setDataSourceName(dataSourceName.getText());
			dataSource.setSetverIPName(server.getText());
			dataSource.setDatabaseName(databaseName.getText());
			dataSource.setUserName(username.getText());
			dataSource.setPassword(password.getText());
			dataSource.setDataBaseType((DatabaseType) dataBaseType.getSelectedItem());

			if (isAdd) {
				addDataSource(dataSource);
			}
			onCancel();
		}

		public void actionPerformed(ActionType actionType) {
			if (actionType == ActionType.Cancel) {
				DataSourcePanel.this.onCancel();
			} else if (actionType == ActionType.Save) {
				DataSourcePanel.this.onSave();
			}
		}

		@Override
		public void onExit() {
			btns.removeButtonBarListener(this);
			btns.onExit();
			btns = null;

			dataSourceName = null;
			server = null;
			databaseName = null;
			username = null;
			password = null;

			dataBaseType.removeAllItems();
			dataBaseType = null;
		}
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
		buttonBar.removeButtonBarListener(this);
		buttonBar.onExit();
		buttonBar = null;

		dateFormatCombo = null;

		scrlPane.removeMouseListener(popUpAction);
		scrlPane.removeAll();
		scrlPane = null;

		mnuTable.removeMouseListener(popUpAction);
		mnuTable.removeAll();
		mnuTable = null;

		addDataSource.removeActionListener(addAction);
		editDataSource.removeActionListener(editAction);
		deleteDataSource.removeActionListener(deleteAction);

		addAction = null;
		editAction = null;
		deleteAction = null;

		addDataSource = null;
		editDataSource = null;
		deleteDataSource = null;
		
		ApplicationContext.getCurrentFrame().setVisible(true);
	}
}