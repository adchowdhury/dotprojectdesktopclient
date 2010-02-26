package com.zycus.dotproject.ui.component.datepicker;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import com.zycus.dotproject.ui.event.datepicker.MonthEvent;
import com.zycus.dotproject.ui.event.datepicker.MonthListener;

public class CalendarADC extends JPanel implements ActionListener, MonthListener, ItemListener {
	private Calendar			m_calCurrent	= new GregorianCalendar();

	private JPanel				m_pnlInput		= null;

	private BasicArrowButton	m_btnCombo		= null;

	private JTextField			m_txtInput		= null;

	private JMonthView			m_monthView		= null;

	private JPopupMenu			m_menuPopup		= null;

	private JCheckBox			m_chkSelectable	= null;

	private boolean				m_bIsSelectable	= false;

	private SimpleDateFormat	m_objDateFormat	= new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

	public CalendarADC() {
		this(new GregorianCalendar());
	}

	public CalendarADC(final Calendar a_objCal) {
		super();
		initComponents();

		// set the calendar and input box date
		Date l_objDate = a_objCal.getTime();
		m_calCurrent.setTime(l_objDate);
		updateText();

		// create the GUI elements and assign listeners
		buildInputPanel();
		registerListeners();
		setLayout(new BorderLayout());
		// intially, only display the input panel
		add(m_pnlInput);
		updateTable(a_objCal);
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 6:01:28 PM<br>
	 */
	private void initComponents() {
		m_monthView = new JMonthView(m_calCurrent);
		m_pnlInput = new JPanel() {
			/**
			 * @author : aniruddha<br>
			 * @date : Sep 4, 2006, 12:51:21 PM<br>
			 * @see javax.swing.JComponent#getInsets()
			 */
			@Override
			public Insets getInsets() {
				return new Insets(0, 0, 0, 0);
			}
		};

		m_btnCombo = new BasicArrowButton(SwingConstants.SOUTH);
		m_btnCombo.setFocusable(false);
		m_txtInput = new JFormattedTextField(m_objDateFormat);
		m_chkSelectable = new JCheckBox("");
		m_chkSelectable.setSelected(true);
		m_menuPopup = new JPopupMenu() {
			/**
			 * @author : aniruddha<br>
			 * @date : Sep 4, 2006, 12:11:12 PM<br>
			 * @see javax.swing.JComponent#getInsets()
			 */
			@Override
			public Insets getInsets() {
				return new Insets(0, 0, 0, 0);
			}
		};
	}

	/*
	 * Creates a field and 'combo box' button above the calendar to allow user
	 * input.
	 */
	private void buildInputPanel() {
		// m_pnlInput.setLayout(new BoxLayout(m_pnlInput, BoxLayout.X_AXIS));
		m_pnlInput.setLayout(new BorderLayout());

		m_pnlInput.add(m_txtInput, BorderLayout.CENTER);
		m_pnlInput.add(m_btnCombo, BorderLayout.EAST);
		m_pnlInput.add(m_chkSelectable, BorderLayout.WEST);
		m_chkSelectable.setVisible(m_bIsSelectable);
	}

	private void registerListeners() {
		m_txtInput.addKeyListener(new InputListener());
		m_txtInput.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent a_objFEvent) {
				if (a_objFEvent == null) {
					return;
				}
				
				Object l_objSource = a_objFEvent.getSource();
				if (l_objSource != m_txtInput) {
					return;
				}
				
				Date l_objNewDate = null;
				try {
					l_objNewDate = m_objDateFormat.parse(m_txtInput.getText());
				} catch (Throwable ex) {
					if (m_calCurrent == null) {
						m_calCurrent = new GregorianCalendar();
					}
					m_txtInput.setText(m_objDateFormat.format(m_calCurrent.getTime()));
				}
				if (l_objNewDate == null) {
					return;
				}
				m_calCurrent.setTime(l_objNewDate);
				updateTable(m_calCurrent);
			}

		});
		m_btnCombo.addActionListener(this);
		m_monthView.addMonthListener(this);
		if (m_bIsSelectable)
			m_chkSelectable.addItemListener(this);
	}

	protected void updateTable(Calendar cal) {
		m_calCurrent = cal;
		m_monthView.updateTable(cal);
		updateText();
	}

	/*
	 * Returns the currently selected date as a <code>Calendar</code> object.
	 * 
	 * @return Calendar the currently selected calendar date
	 */
	public Calendar getCalendar() {
		if (m_bIsSelectable && m_chkSelectable.isSelected() == false) {
			return null;
		}
		
		return m_calCurrent;
	}

	public Date getDate() {
		if (m_bIsSelectable && m_chkSelectable.isSelected() == false) {
			return null;
		}
		
		if (m_calCurrent == null) {
			return null;
		}
		return m_calCurrent.getTime();
	}

	public String getText() {
		if (m_bIsSelectable && m_chkSelectable.isSelected() == false) {
			return null;
		}

		return m_txtInput.getText();
	}

	/**
	 * Sets the current date and updates the UI to reflect the new date.
	 * 
	 * @param newDate
	 *            the new date as a <code>Date</code> object.
	 * @see Date
	 * @author James Waldrop
	 */
	public void setDate(Date newDate) {
		if (m_bIsSelectable && m_chkSelectable.isSelected() == false) {
			return;
		}
		
		if (newDate == null) {
			m_calCurrent = null;
		} else {
			if (m_calCurrent == null) {
				m_calCurrent = new GregorianCalendar();
			}
			m_calCurrent.setTime(newDate);
		}

		updateText();
	}

	/*
	 * Captures user input in the 'combo box' If the input is a valid date and
	 * the user pressed ENTER or TAB, the calendar selection is updated
	 */
	class InputListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent a_objEvent) {
			if (a_objEvent == null) {
				return;
			}
			Object l_objSource = a_objEvent.getSource();
			if (l_objSource == null) {
				return;
			}
			if (l_objSource == m_txtInput) {
				switch (a_objEvent.getKeyChar()) {
					case KeyEvent.VK_ENTER:
						Date l_objNewDate = null;
						try {
							l_objNewDate = m_objDateFormat.parse(m_txtInput.getText());
						} catch (Throwable ex) {

						}
						if (l_objNewDate == null) {
							return;
						}
						m_calCurrent.setTime(l_objNewDate);
						updateTable(m_calCurrent);
				}
			}
		}
	}

	private int getTopPosition() {
		int l_iMainPanelHeight = m_monthView.getPanel().getHeight();
		int l_iYOnScreen = (int) getLocationOnScreen().getY();
		if (l_iYOnScreen + l_iMainPanelHeight + m_txtInput.getHeight() + 35 > Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
			return -l_iMainPanelHeight - 6;
		}
		return getHeight();
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 10:04:18 AM<br>
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent a_objEvent) {
		if (a_objEvent == null) {
			return;
		}
		Object l_objSource = a_objEvent.getSource();
		if (l_objSource == null) {
			return;
		}
		if (l_objSource == m_btnCombo) {
			if (m_chkSelectable.isSelected() == false) {
				m_chkSelectable.setSelected(true);
				changeStatus();
			}
			m_menuPopup.removeAll();
			m_menuPopup.add(m_monthView.getPanel());
			m_menuPopup.show(this, 0, getTopPosition());
		}
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Sep 4, 2006, 12:40:25 PM<br>
	 * @see components.event.MonthListener#valueChanged(components.event.MonthEvent)
	 */
	public void valueChanged(MonthEvent a_objMonthEvent) {
		updateTable(a_objMonthEvent.getCalendar());
		if (a_objMonthEvent.isToCloseView()) {
			m_menuPopup.setVisible(false);
		}
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Sep 4, 2006, 12:57:29 PM<br>
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent a_objEvent) {
		if (a_objEvent == null) {
			return;
		}
		Object l_objSource = a_objEvent.getSource();
		if (l_objSource == null) {
			return;
		}
		if (l_objSource == m_chkSelectable) {
			changeStatus();
		}
	}

	private void changeStatus() {
		if (m_chkSelectable.isSelected() == false) {
			m_txtInput.setText("<not set>");
			m_txtInput.setEnabled(false);
		} else {
			updateText();
			m_txtInput.setEnabled(true);
		}
		updateUI();
	}

	public void updateText() {
		if (m_calCurrent != null) {
			m_txtInput.setText(m_objDateFormat.format(m_calCurrent.getTime()));
		} else {
			m_txtInput.setText("");
		}

	}

	/**
	 * @author : aniruddha<br>
	 * @date : Nov 13, 2006, 5:13:32 PM<br>
	 */
	public void setFormat(String a_strFormat) {
		SimpleDateFormat l_objDateFormat = null;
		try {
			l_objDateFormat = new SimpleDateFormat(a_strFormat, Locale.ENGLISH);
		} catch (Throwable a_th) {
			// just ignore incase this is something wrong
			return;
		}
		m_objDateFormat = l_objDateFormat;
	}

	public boolean isSelectable() {
		return m_bIsSelectable;
	}

	public void setSelectable(boolean isSelectable) {
		m_bIsSelectable = isSelectable;
		m_chkSelectable.setVisible(m_bIsSelectable);
	}
	
	public static void main(String[] args) {
		JFrame frm = new JFrame("Datepicker demo");
		frm.add(new CalendarADC(), BorderLayout.NORTH);
		frm.setSize(300, 150);
		frm.setLocationRelativeTo(null);
		frm.setVisible(true);
	}
}