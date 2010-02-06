package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.zycus.dotproject.persistence.db.hibernate.HibernateSessionFactory;
import com.zycus.dotproject.ui.component.datepicker.CalendarADC;
import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.DataSource.DatabaseType;
import com.zycus.writereport.dto.ReportForm;
import com.zycus.writereport.dto.ReportParamField;
import com.zycus.writereport.dto.ReportParameterFieldType;
import com.zycus.writereport.impl.DefaultReportServiceImpl;
import com.zycus.writereport.impl.IReportService;
import com.zycus.writereport.util.UtilityDataSource;

public class ReporManagementPanel extends CustomJPanel implements ButtonBarListener {
	private JComboBox						reportsList			= null;
	private JComboBox						dataSourceList		= null;
	private ButtonBar						btns				= null;
	private JTable							reportTable			= null;
	private IReportService					reportService		= null;
	private LinkedHashMap<String, String>	reports				= null;
	private DataSource						currentDataSource	= null;
	private ReportForm						reportForm			= null;
	private ReportTableModel				tableModel			= null;
	private SimpleDateFormat				dateFormat			= new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());

	public ReporManagementPanel() {
		init();
		initLayout();
	}

	private void init() {
		initDefaultDataSet();

		reportService = new DefaultReportServiceImpl();
		reports = reportService.getAvailableReportContexts();

		reportsList = new JComboBox(reports.keySet().toArray());
		reportsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (reportsList.getSelectedIndex() < 0) {
					reportsList.hidePopup();
					return;
				}
				reportService.loadReportContext(reports.get(reportsList.getSelectedItem()));
				reportForm = reportService.loadReportForm(currentDataSource);
				tableModel.fireTableDataChanged();
				reportsList.hidePopup();
				// reportForm.getReportParamFieldSet();
				// reportService.selectReportParameterAndLoadNextParameter(
				// reportForm, TOOL_TIP_TEXT_KEY, TOOL_TIP_TEXT_KEY,
				// currentDataSource)
				// System.out.println(reportForm);
			}
		});
		reportsList.setSelectedIndex(-1);

		dataSourceList = new JComboBox(ApplicationContext.getUserPreferences().getDataSources().toArray());
		dataSourceList.addItem("Use default datasource");
		dataSourceList.setSelectedItem("Use default datasource");
		dataSourceList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (dataSourceList.getSelectedIndex() < 0) {
					return;
				}
				if (dataSourceList.getSelectedIndex() == (dataSourceList.getItemCount() - 1)) {
					initDefaultDataSet();
				} else {
					currentDataSource = getDataSource((com.zycus.dotproject.util.DataSource) dataSourceList.getSelectedItem());
				}
			}
		});
		tableModel = new ReportTableModel();
		reportTable = new JTable(tableModel);
		reportTable.setRowHeight(26);
		reportTable.getColumnModel().getColumn(0).setCellEditor(new BaseReportTableEditor());
		btns = new ButtonBar(ButtonBar.SAVE | ButtonBar.CANCEL);
		btns.addButtonBarListener(this);
		btns.enableButton(ButtonBar.SAVE, false);
	}

	private void initDefaultDataSet() {
		currentDataSource = new UtilityDataSource(HibernateSessionFactory.getDriver(), HibernateSessionFactory.getConnectionURL(), HibernateSessionFactory.getUserName(), HibernateSessionFactory
				.getPassword());
	}

	private DataSource getDataSource(com.zycus.dotproject.util.DataSource dataSource) {
		String mysqlDriverClassName = null;
		String mysqlDriverURL = null;
		if (dataSource.getDataBaseType() == DatabaseType.MySQL) {
			mysqlDriverClassName = "com.mysql.jdbc.Driver";
			mysqlDriverURL = "jdbc:mysql://" + dataSource.getSetverIPName() + "/" + dataSource.getDatabaseName() + "?zeroDateTimeBehavior=convertToNull";
		} else if (dataSource.getDataBaseType() == DatabaseType.MSSql) {

		} else if (dataSource.getDataBaseType() == DatabaseType.Oracle) {

		}
		String mysqlUsername = dataSource.getUserName();
		String mysqlPassword = dataSource.getPassword();
		return new UtilityDataSource(mysqlDriverClassName, mysqlDriverURL, mysqlUsername, mysqlPassword);
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		JPanel pnlNorth = new JPanel(new GridLayout(2, 2, 5, 5));

		pnlNorth.add(new JLabel("Select Report"));
		pnlNorth.add(reportsList);

		pnlNorth.add(new JLabel("Select Datasource"));
		pnlNorth.add(dataSourceList);

		JScrollPane scrlPane = new JScrollPane(reportTable);
		scrlPane.setBorder(new TitledBorder("Report data"));

		add(pnlNorth, BorderLayout.NORTH);
		add(scrlPane, BorderLayout.CENTER);
		add(btns, BorderLayout.SOUTH);
	}

	private class BaseReportTableEditor extends AbstractCellEditor implements TableCellEditor {
		private ReportParamField	currentFieldParam	= null;
		private MultiSelectCombo	multiCombo			= null;
		private JComboBox			singleCombo			= null;
		private CalendarADC			dateCombo			= null;
		private JPanel				editorPanel			= null;
		private JTextField			txtFreeValue		= null;

		public BaseReportTableEditor() {
			singleCombo = new JComboBox();
			dateCombo = new CalendarADC();
			dateCombo.setDate(new Date());
			multiCombo = new MultiSelectCombo();
			editorPanel = new JPanel(new BorderLayout());
			txtFreeValue = new JTextField();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			editorPanel.removeAll();
			if (value instanceof ReportParamField) {
				currentFieldParam = (ReportParamField) value;
				if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.SingleValue) {
					singleCombo.removeAllItems();
					String params = reportForm.getSelectedParams().get(currentFieldParam.getReportParamFieldName());
					String selected = null;

					for (String key : currentFieldParam.getReportParamFieldData().keySet()) {
						singleCombo.addItem(currentFieldParam.getReportParamFieldData().get(key));
						if (params != null) {
							selected = currentFieldParam.getReportParamFieldData().get(params);
						}
					}

					if (selected != null) {
						singleCombo.setSelectedItem(selected);
					}
					editorPanel.add(singleCombo, BorderLayout.CENTER);
				} else if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.MultiValue) {
					multiCombo.initTableCellEditorComponent(table, currentFieldParam, isSelected, row, column);
					editorPanel.add(multiCombo.getCombo(), BorderLayout.CENTER);
				} else if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.Date) {
					String params = reportForm.getSelectedParams().get(currentFieldParam.getReportParamFieldName());
					if (params != null) {
						try {
							//System.out.println(params);
							//System.out.println(dateFormat.parseObject(params));
							dateCombo.setDate(dateFormat.parse(params));
						} catch (Exception excp) {
							excp.printStackTrace();
							dateCombo.setDate(new Date());
						}
					}
					editorPanel.add(dateCombo, BorderLayout.CENTER);
				} else if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.FreeText) {
					editorPanel.add(txtFreeValue, BorderLayout.CENTER);
				}
			}

			return editorPanel;
		}

		public Object getCellEditorValue() {
			if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.SingleValue) {
				if (singleCombo.getSelectedItem() == null) {
					return null;
				}
				for (String key : currentFieldParam.getReportParamFieldData().keySet()) {
					if (currentFieldParam.getReportParamFieldData().get(key).equals(singleCombo.getSelectedItem())) {
						return key;
					}
				}
				// return singleCombo.getSelectedItem();
			} else if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.MultiValue) {
				return multiCombo.getCellEditorValue();
			} else if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.Date) {
				return dateCombo.getDate();
			} else if (currentFieldParam.getReportParameterType() == ReportParameterFieldType.FreeText) {
				return txtFreeValue.getText();
			}
			return null;
		}

	}

	private class MultiSelectCombo {
		private JComboBox			comboValue			= null;
		private List<String>		currentValues		= null;
		private ReportParamField	currentFieldParam	= null;

		public JComboBox getCombo() {
			return comboValue;
		}

		public MultiSelectCombo() {
			currentValues = new ArrayList<String>();
			comboValue = new JComboBox() {
				@Override
				public void hidePopup() {
					// super.hidePopup();
				}

				@Override
				public void setPopupVisible(boolean v) {
					if (!v) {
						return;
					}
					super.setPopupVisible(v);
				}
			};
			comboValue.setRenderer(new CheckComboRenderer());
			comboValue.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionPerformed) {
					// comboValue.showPopup();
					if (comboValue.getSelectedItem() == null) {
						return;
					}
					if (currentValues.contains(comboValue.getSelectedItem().toString())) {
						currentValues.remove(comboValue.getSelectedItem().toString());
						return;
					}
					currentValues.add(comboValue.getSelectedItem().toString());
				}
			});
		}

		public void initTableCellEditorComponent(JTable table, ReportParamField value, boolean isSelected, int row, int column) {
			currentValues.clear();
			comboValue.removeAllItems();
			currentFieldParam = value;
			System.out.println("Selected Params: " + reportForm.getSelectedParams());
			String params = reportForm.getSelectedParams().get(currentFieldParam.getReportParamFieldName());
			if (params != null) {
				String[] keys = params.split(",");
				for (String key : currentFieldParam.getReportParamFieldData().keySet()) {
					if (Arrays.binarySearch(keys, key) >= 0) {
						currentValues.add(currentFieldParam.getReportParamFieldData().get(key));
					}
				}
			}
			for (String displayData : currentFieldParam.getReportParamFieldData().values()) {
				comboValue.addItem(displayData);
			}
		}

		public Object getCellEditorValue() {
			StringBuilder strReturn = new StringBuilder();
			for (String key : currentFieldParam.getReportParamFieldData().keySet()) {
				String value = currentFieldParam.getReportParamFieldData().get(key);
				if (currentValues.contains(value)) {
					if (strReturn.length() > 0) {
						strReturn.append(",");
					}
					strReturn.append(key);
				}
			}
			return strReturn.toString();
		}

		private class CheckComboRenderer implements ListCellRenderer {
			private JCheckBox	checkBox	= null;
			private JLabel		lblBlank	= null;

			public CheckComboRenderer() {
				checkBox = new JCheckBox();
				lblBlank = new JLabel();
			}

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value == null) {
					return lblBlank;
				}
				checkBox.setText(value.toString());
				boolean isSelectedValue = currentValues.contains(value.toString());
				checkBox.setBackground(isSelected ? UIManager.getColor("Menu.selectionBackground") : UIManager.getColor("Menu.background"));
				checkBox.setSelected(isSelectedValue);
				return checkBox;
			}
		}
	}

	private class ReportTableModel extends DefaultTableModel {
		private List<ReportParamField>	data	= new ArrayList<ReportParamField>();

		@Override
		public void fireTableDataChanged() {
			data = new ArrayList<ReportParamField>(reportForm.getReportParamFieldSet());
			super.fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			if (reportForm == null) {
				return 0;
			}
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			return data.get(row);
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public String getColumnName(int column) {
			return "Report Parameters";
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return true;
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			if (value == null) {
				return;
			}
			System.out.println("Value to be SET: " + value);
			String newValue = value.toString();
			if(value instanceof Date) {
				newValue = dateFormat.format(value);
			}
			if (reportService.selectReportParameterAndLoadNextParameter(reportForm, data.get(row).getReportParamFieldName(), newValue, currentDataSource) == false) {
				btns.enableButton(ButtonBar.SAVE, true);
			} else {
				btns.enableButton(ButtonBar.SAVE, false);
			}
			fireTableDataChanged();
		}
	}

	public void actionPerformed(ActionType actionType) {
		if (actionType == ActionType.Cancel) {
			DialogUtility.closeParent(this);
		} else if (actionType == ActionType.Save) {
			onSave();
		}
	}

	private void onSave() {
		JFileChooser reportFileChooser = new JFileChooser() {
			@Override
			public void cancelSelection() {
				Map<Thread, StackTraceElement[]> stackTrace = Thread.getAllStackTraces();
				if (stackTrace.get(Thread.currentThread())[4].getFileName().contains("AbstractButton")) {
					super.cancelSelection();
				}
			}
		};
		reportFileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f == null) {
					return false;
				}
				if (f.isDirectory()) {
					return false;
				}
				return f.getName().toLowerCase().endsWith(".xml");
			}

			@Override
			public String getDescription() {
				return "XML Spreadsheet";
			}
		});
		if (ApplicationContext.getUserPreferences().getUserSelectedDir() != null) {
			reportFileChooser.setCurrentDirectory(new File(ApplicationContext.getUserPreferences().getUserSelectedDir()));
		}
		reportFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		reportFileChooser.setMultiSelectionEnabled(false);
		reportFileChooser.setAcceptAllFileFilterUsed(false);
		reportFileChooser.showSaveDialog(ApplicationContext.getCurrentFrame());
		if (reportFileChooser.getSelectedFile() == null) {
			return;
		}
		String fileName = reportFileChooser.getSelectedFile().getAbsolutePath();
		reportService.saveReportData(reportForm, currentDataSource, fileName);
		ApplicationContext.getUserPreferences().setUserSelectedDir(reportFileChooser.getCurrentDirectory().getAbsolutePath());
		ApplicationContext.saveSettings();
		JOptionPane.showMessageDialog(reportFileChooser, "Report saved at: " + fileName);
		reportFileChooser = null;
	}

	@Override
	public void onExit() {
		btns.removeButtonBarListener(this);
		btns.onExit();
		btns = null;

		reportsList.removeAllItems();
		reportsList = null;

		dataSourceList.removeAllItems();
		dataSourceList = null;

		reportService = null;
		reports.clear();

		currentDataSource = null;
		reportForm = null;

		reportTable.removeAll();
		reportTable = null;

		tableModel = null;
	}
}