/**
 * @author		: aniruddha<br>
 * @date		: Sep 4, 2006,  12:15:52 PM<br>
 * @source		: JMonthView.java<br>
 * @project		: WopHelp<br>
 */
package com.zycus.dotproject.ui.component.datepicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;

import com.zycus.dotproject.ui.event.datepicker.MonthEvent;
import com.zycus.dotproject.ui.event.datepicker.MonthListener;

/**
 * @author aniruddha
 * 
 */
public class JMonthView implements ActionListener, MouseListener, ChangeListener {
	private static DateFormatSymbols	m_objDFS			= new DateFormatSymbols();

	private static final String[]		m_strMonths			= m_objDFS.getMonths();

	private Calendar					m_calCurrent		= new GregorianCalendar();

	private JPanel						m_pnlCal			= null;

	private BasicArrowButton			m_btnNext			= null;

	private BasicArrowButton			m_btnPrev			= null;

	private JButton[]					m_btnDate			= new JButton[42];

	private JLabel						m_lblMonth			= null;

	private JLabel						m_lblYear			= null;

	private JLabel						m_lblToDay			= null;

	private JLabel[]					m_lblDays			= new JLabel[7];

	private JSpinner					m_spinYear			= null;

	private PopupMenu					m_menuMonth			= null;

	private MenuItem[]					m_menuMonths		= new MenuItem[m_strMonths.length - 1];

	private Vector<MonthListener>		m_vMonthLIstener	= null;

	public JMonthView() {
		this(new GregorianCalendar());
	}

	public JMonthView(final Calendar a_objCal) {
		initComponents();
		Date l_objDate = a_objCal.getTime();
		m_calCurrent.setTime(l_objDate);
		builtPopupMenus();
		buildBasePanel();
		registerListeners();
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 6:01:28 PM<br>
	 */
	private void initComponents() {
		m_pnlCal = new JPanel();
		m_btnNext = new BasicArrowButton(SwingConstants.EAST);
		m_btnPrev = new BasicArrowButton(SwingConstants.WEST);

		m_lblMonth = new JLabel("", SwingConstants.CENTER);
		m_lblYear = new JLabel();
		m_lblToDay = new JLabel("Today: " + m_strMonths[m_calCurrent.get(Calendar.MONTH)] + " " + m_calCurrent.get(Calendar.DATE) + ", " + m_calCurrent.get(Calendar.YEAR), SwingConstants.CENTER);
		m_lblToDay.setFont(m_lblToDay.getFont().deriveFont(Font.BOLD, m_lblToDay.getFont().getSize() - 2));
		m_lblToDay.setBackground(Color.BLACK);
		m_lblToDay.setForeground(Color.YELLOW);
		m_lblToDay.setOpaque(true);

		m_spinYear = new JSpinner();
		m_vMonthLIstener = new Vector<MonthListener>();
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 3:16:55 PM<br>
	 */
	private void builtPopupMenus() {
		m_menuMonth = new PopupMenu();

		for (int l_iCounter = 0; l_iCounter < m_menuMonths.length; l_iCounter++) {
			m_menuMonths[l_iCounter] = new MenuItem(m_strMonths[l_iCounter]);
			m_menuMonths[l_iCounter].addActionListener(this);
			m_menuMonth.add(m_menuMonths[l_iCounter]);
		}
		//m_lblMonth.add(m_menuMonth);
	}

	private void buildBasePanel() {
		for (int l_iCounter = 0; l_iCounter < m_btnDate.length; l_iCounter++) {
			m_btnDate[l_iCounter] = getButton(l_iCounter + 1 + "");
		}

		String[] names = m_objDFS.getShortWeekdays();
		for (int l_iCounter = 0; l_iCounter < m_lblDays.length; l_iCounter++) {
			m_lblDays[l_iCounter] = getLabel(names[l_iCounter + 1].charAt(0) + "");
		}

		JPanel l_pnlMain = new JPanel(new GridLayout(7, m_btnDate.length));

		for (int l_iCounter = 0; l_iCounter < (m_btnDate.length + m_lblDays.length); l_iCounter++) {
			if (l_iCounter < m_lblDays.length) {
				l_pnlMain.add(m_lblDays[l_iCounter]);
			} else {
				l_pnlMain.add(m_btnDate[l_iCounter - m_lblDays.length]);
			}
		}

		m_pnlCal.setBorder(new LineBorder(Color.BLACK));
		m_pnlCal.setLayout(new BorderLayout());
		m_pnlCal.add(buildCalendarNavigationPanel(), BorderLayout.NORTH);
		m_pnlCal.add(l_pnlMain, BorderLayout.CENTER);
		m_pnlCal.add(m_lblToDay, BorderLayout.SOUTH);
	}

	private JButton getButton(String a_strText) {
		JButton l_btnReturn = new JButton(a_strText);
		l_btnReturn.setMargin(new Insets(0, 0, 0, 0));
		l_btnReturn.addActionListener(this);
		l_btnReturn.addMouseListener(this);
		l_btnReturn.setFocusable(false);
		return l_btnReturn;
	}

	private JLabel getLabel(String a_strText) {
		JLabel l_lblReturn = new JLabel(a_strText, SwingConstants.CENTER);
		l_lblReturn.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
		return l_lblReturn;
	}

	private JPanel buildCalendarNavigationPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		int fontSize = m_lblMonth.getFont().getSize();
		m_lblMonth.setFont(m_lblMonth.getFont().deriveFont(Font.PLAIN, fontSize - 2));

		m_spinYear.setEditor(m_lblYear);
		JPanel l_panel = new JPanel(new GridLayout(1, 2));
		l_panel.add(m_lblMonth);
		l_panel.add(m_spinYear);

		panel.add(m_btnPrev, BorderLayout.WEST);

		panel.add(l_panel, BorderLayout.CENTER);
		panel.add(m_btnNext, BorderLayout.EAST);

		return panel;
	}

	private void registerListeners() {
		m_btnPrev.addActionListener(this);
		m_btnNext.addActionListener(this);

		m_lblMonth.addMouseListener(this);
		m_lblToDay.addMouseListener(this);

		m_spinYear.addChangeListener(this);
	}

	public void updateTable(Calendar cal) {

		Calendar dayOne = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		Calendar l_objPreMonthCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1);

		// compute the number of days in the month and
		// the start column for the first day in the first week
		int l_iActualDays = cal.getActualMaximum(Calendar.DATE);
		int l_iStartIndex = dayOne.get(Calendar.DAY_OF_WEEK) - 1;
		int l_iCurrentDay = cal.get(Calendar.DAY_OF_MONTH);

		int l_iDayCounter = 1;
		int l_iNextCounter = 1;

		if (l_iStartIndex == 0)
			l_iStartIndex = 7;
		LineBorder l_border = new LineBorder(Color.MAGENTA, 2);

		for (int l_iCounter = 0; l_iCounter < m_btnDate.length; l_iCounter++) {
			m_btnDate[l_iCounter].setBackground(m_pnlCal.getBackground());
			m_btnDate[l_iCounter].setBorder(null);
			if (l_iCounter < l_iStartIndex || l_iDayCounter > l_iActualDays) {
				m_btnDate[l_iCounter].setEnabled(false);
				if (l_iDayCounter > l_iActualDays) {
					m_btnDate[l_iCounter].setText(l_iNextCounter + "");
					l_iNextCounter++;
				}
			} else {
				m_btnDate[l_iCounter].setEnabled(true);
				m_btnDate[l_iCounter].setText(l_iDayCounter + "");

				if (l_iCurrentDay == l_iDayCounter) {
					m_btnDate[l_iCounter].setBackground(Color.GREEN);
				}

				Date l_date = new Date(m_lblToDay.getText().substring(6).trim());

				if (dayOne.get(Calendar.YEAR) == l_date.getYear() + 1900 && dayOne.get(Calendar.MONTH) == l_date.getMonth() && l_date.getDate() == l_iDayCounter) {
					m_btnDate[l_iCounter].setBorder(l_border);
				}

				l_iDayCounter++;
			}
		}

		int l_iActualDaysPrev = l_objPreMonthCal.getActualMaximum(Calendar.DATE) + 1;
		for (int l_iCounter = l_iStartIndex; l_iCounter >= 0; l_iCounter--) {
			if (m_btnDate[l_iCounter].isEnabled() == false)
				m_btnDate[l_iCounter].setText(l_iActualDaysPrev + "");
			l_iActualDaysPrev--;
		}

		// set the month, year label
		m_lblMonth.setText(m_strMonths[cal.get(Calendar.MONTH)].trim());
		m_lblYear.setText(cal.get(Calendar.YEAR) + "");
		// m_spinYear.setValue(cal.get(Calendar.YEAR) + "");
		m_spinYear.setValue(cal.get(Calendar.YEAR));
	}

	public Calendar getCalendar() {
		return m_calCurrent;
	}

	public void setDate(Date newDate) {
		m_calCurrent.setTime(newDate);
	}

	public Date getDate() {
		return m_calCurrent.getTime();
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
		MonthEvent l_me = new MonthEvent();
		if (l_objSource == m_btnPrev) {
			m_calCurrent.add(Calendar.MONTH, -1);
			l_me.setToCloseView(false);
		} else if (l_objSource == m_btnNext) {
			m_calCurrent.add(Calendar.MONTH, 1);
			l_me.setToCloseView(false);
		} else if (l_objSource instanceof JButton) {
			for (int l_iCounter = 0; l_iCounter < m_btnDate.length; l_iCounter++) {
				if (l_objSource == m_btnDate[l_iCounter]) {
					int l_iDay = Integer.parseInt(m_btnDate[l_iCounter].getText());
					m_calCurrent.set(Calendar.DATE, l_iDay);
					l_me.setToCloseView(true);
					break;
				}
			}
		} else if (l_objSource instanceof MenuItem) {
			for (int l_iCounter = 0; l_iCounter < m_menuMonths.length; l_iCounter++) {
				if (l_objSource == m_menuMonths[l_iCounter]) {
					int l_iCurrentDay = m_calCurrent.get(Calendar.DATE);
					m_calCurrent.set(Calendar.MONTH, l_iCounter);

					while (l_iCurrentDay != m_calCurrent.get(Calendar.DATE)) {
						l_iCurrentDay--;
						m_calCurrent.set(Calendar.MONTH, l_iCounter);
						m_calCurrent.set(Calendar.DATE, l_iCurrentDay);
					}
					l_me.setToCloseView(false);
					break;
				}
			}
		}
		updateTable(m_calCurrent);
		l_me.setCalendar(m_calCurrent);
		Enumeration<MonthListener> l_enum = m_vMonthLIstener.elements();
		while (l_enum.hasMoreElements()) {
			l_enum.nextElement().valueChanged(l_me);
		}
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 2:58:05 PM<br>
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent a_objEvent) {
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 2:58:05 PM<br>
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent a_objEvent) {
		if (a_objEvent == null)
			return;
		Object l_objSource = a_objEvent.getSource();
		if (l_objSource == null)
			return;
		if (l_objSource instanceof JButton) {
			if (((JButton) l_objSource).isEnabled()) {
				((JButton) l_objSource).setBackground(Color.YELLOW);

				try {
					int l_iDay = Integer.parseInt(((JButton) l_objSource).getText());
					if (m_calCurrent.get(Calendar.DATE) == l_iDay)
						((JButton) l_objSource).setBackground(Color.ORANGE);
				} catch (Throwable a_th) {}
			}
		}
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 2:58:05 PM<br>
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent a_objEvent) {
		if (a_objEvent == null)
			return;
		Object l_objSource = a_objEvent.getSource();
		if (l_objSource == null)
			return;
		if (l_objSource instanceof JButton) {
			if (((JButton) l_objSource).isEnabled()) {
				((JButton) l_objSource).setBackground(m_pnlCal.getBackground());
				try {
					int l_iDay = Integer.parseInt(((JButton) l_objSource).getText());
					if (m_calCurrent.get(Calendar.DATE) == l_iDay)
						((JButton) l_objSource).setBackground(Color.GREEN);
				} catch (Throwable a_th) {}
			}
		}
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 2:58:05 PM<br>
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent a_objEvent) {
	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 2:58:05 PM<br>
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent a_objEvent) {
		if (a_objEvent == null)
			return;
		Object l_objSource = a_objEvent.getSource();
		if (l_objSource == null)
			return;
		if (l_objSource == m_lblMonth) {
			//m_menuMonth.show(m_lblMonth, a_objEvent.getX(), a_objEvent.getY());
		} else if (l_objSource == m_lblToDay) {
			Date l_date = new Date(m_lblToDay.getText().substring(6).trim());
			m_calCurrent.setTime(l_date);
			updateTable(m_calCurrent);
			MonthEvent l_me = new MonthEvent();
			l_me.setCalendar(m_calCurrent);
			l_me.setToCloseView(true);
			Enumeration<MonthListener> l_enum = m_vMonthLIstener.elements();
			while (l_enum.hasMoreElements()) {
				l_enum.nextElement().valueChanged(l_me);
			}
		}

	}

	/**
	 * @author : aniruddha<br>
	 * @date : Aug 31, 2006, 4:50:12 PM<br>
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent a_objEvent) {
		if (a_objEvent == null)
			return;
		Object l_objSource = a_objEvent.getSource();
		if (l_objSource == null)
			return;
		if (l_objSource == m_spinYear) {
			int l_iYear = Integer.parseInt(m_spinYear.getValue().toString());
			m_calCurrent.set(Calendar.YEAR, l_iYear);
			updateTable(m_calCurrent);
			MonthEvent l_me = new MonthEvent();
			l_me.setCalendar(m_calCurrent);
			l_me.setToCloseView(false);
			Enumeration<MonthListener> l_enum = m_vMonthLIstener.elements();
			while (l_enum.hasMoreElements()) {
				l_enum.nextElement().valueChanged(l_me);
			}
		}
	}

	public JPanel getPanel() {
		return m_pnlCal;
	}

	public void addMonthListener(MonthListener a_objListener) {
		if (m_vMonthLIstener.contains(a_objListener) == false)
			m_vMonthLIstener.add(a_objListener);
	}

	public void removeMonthListener(MonthListener a_objListener) {
		if (m_vMonthLIstener.contains(a_objListener))
			m_vMonthLIstener.add(a_objListener);
	}
}
