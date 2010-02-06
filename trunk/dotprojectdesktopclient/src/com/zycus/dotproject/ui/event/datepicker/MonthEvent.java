/**
 * @author		: aniruddha<br>
 * @date		: Sep 4, 2006,  12:34:47 PM<br>
 * @source		: MonthEvent.java<br>
 * @project		: WopHelp<br>
 */
package com.zycus.dotproject.ui.event.datepicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author aniruddha
 *
 */
public class MonthEvent
{
	private Calendar	m_calCurrent		= new GregorianCalendar();

	private boolean		m_bIsToCloseView	= false;

	/**
	 * aniruddha
	 * 12:35:38 PM
	 * @return Returns the m_calCurrent.
	 */
	public Calendar getCalendar()
	{
		return m_calCurrent;
	}

	/**
	 * @param current the m_calCurrent to set
	 */
	public void setCalendar(Calendar current)
	{
		m_calCurrent = current;
	}

	/**
	 * aniruddha
	 * 12:37:21 PM
	 * @return Returns the m_bIsToCloseView.
	 */
	public boolean isToCloseView()
	{
		return m_bIsToCloseView;
	}

	/**
	 * @param isToCloseView the m_bIsToCloseView to set
	 */
	public void setToCloseView(boolean isToCloseView)
	{
		m_bIsToCloseView = isToCloseView;
	}
}
