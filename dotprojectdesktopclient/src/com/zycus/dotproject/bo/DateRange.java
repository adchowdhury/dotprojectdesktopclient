package com.zycus.dotproject.bo;

import java.util.Calendar;
import java.util.Date;

public class DateRange {
	private Date		startDate		= new Date();
	private Date		endDate			= new Date();
	private long		durationInDays	= 0L;

	public static int	startHours		= 11;
	public static int	endHours		= 19;
	public static int	noOfHoursInAday	= 8;

	public DateRange() {

	}

	public DateRange(Date endDate, Date startDate) {
		this.endDate = endDate;
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the durationInDays
	 */
	public long getDurationInDays() {
		return durationInDays;
	}

	/**
	 * @param durationInDays
	 *            the durationInDays to set
	 */
	public void setDurationInDays(long durationInDays) {
		this.durationInDays = durationInDays;
	}

	public static void main(String[] args) {
		Date startDate = new Date(System.currentTimeMillis());
		// System.out.println("Start Date : " + startDate);
		startDate.setHours(11);
		
		startDate = getNormalizedStartDate(startDate, true);

		float durationInDays = 1F;
		
		//getStartDateForTheSuccessor(endDate, duration)

		Date endDate = calculateEndDate(startDate, durationInDays);

		// System.out.println("endDate " + endDate);

		durationInDays = calculateDuration(startDate, endDate);

		endDate.setHours(19);

		// System.out.println("successor : " +
		// getStartDateForTheSuccessor(endDate));

		// System.out.println("durationInDays " + durationInDays);
	}

	public static Date getNormalizedStartDate(Date startDate, boolean newTask) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		int startDateHours = startDate.getHours();
		// System.out.println("startDateHours : b4 " + startDateHours);

		if (startDateHours > endHours) 
		{
			int dayEndExtraHours = startDateHours - endHours;
			cal.add(Calendar.DATE, 1);
			startDateHours = startHours + dayEndExtraHours;
		}

		else if (startDateHours < startHours) 
		{
			startDateHours = startHours;
		}

		// If new task then reset time to next working day 11.00 hours
		// Else Make the changes as per normalized time only if its not a new task
		if(newTask)
		{
			cal.set(Calendar.HOUR_OF_DAY, startHours);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		
		}
		else
		{
			cal.set(Calendar.HOUR_OF_DAY, startDateHours);
		}
		

		while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) 
		{
			cal.add(Calendar.DATE, 1);
		}

		startDate = cal.getTime();
		
		// System.out.println("startDate : " + startDate);
		return startDate;
	}

	public static Date calculateEndDate(Date startDate, float durationInDays) {
		// this is for 0 duratation
		if (durationInDays == 0) {
			return (Date) startDate.clone();
		}

		int durationInHours = getDurationInHoursCeiled(durationInDays);
		// System.out.println("durationInHours : " + durationInHours);

		int noOfDays = (int) Math.floor(durationInHours / noOfHoursInAday);
		int extraHours = durationInHours % noOfHoursInAday;
		// System.out.println("noOfDays : " + noOfDays);
		// System.out.println("extraHours : " + extraHours);

		int endDateHours = startDate.getHours() + extraHours;

		if (startDate.getHours() == startHours && extraHours == 0) {
			noOfDays = noOfDays - 1;
			endDateHours = endHours;
		}

		// calculate the noOfDays and hours based on the end time...
		// System.out.println("B4 if : ");
		// System.out.println("noOfDays : " + noOfDays);
		// System.out.println("endDateHours : " + endDateHours);

		if (endDateHours > endHours) {
			int dayEndExtraHours = endDateHours - endHours;
			// System.out.println("dayEndExtraHours : " + dayEndExtraHours);
			noOfDays = noOfDays + 1;
			endDateHours = startHours + dayEndExtraHours;
			// System.out.println("endDateHours : " + endDateHours);
		}

		// System.out.println("After if : ");
		// System.out.println("noOfDays : " + noOfDays);
		// System.out.println("endDateHours : " + endDateHours);

		//
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		for (int i = 0; i < noOfDays; i++) {
			cal.add(Calendar.DATE, 1);
			Date now = cal.getTime();
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				noOfDays = noOfDays + 1;
			}
		}

		// System.out.println("noOfDays after excluding sat and sun: " +
		// noOfDays);

		cal.set(Calendar.HOUR_OF_DAY, endDateHours);
		Date endDate = cal.getTime();

		//System.out.println("endDate : " + endDate);

		return endDate;
	}

	public static float calculateDuration(Date startDate, Date endDate) {

		System.out.println("DateRange.calculateDuration() startDate.compareTo(endDate) : " + startDate.compareTo(endDate));
		if (startDate.compareTo(endDate) == 0) {
			return 0F;
		}
		
		
		
		
		//Iterate 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		float noOfDays = 0;
		Date now = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		while (now.before(endDate)) 
		{
			System.out.println("DateRange.calculateDuration() " + calendar.get(Calendar.DAY_OF_WEEK));
			if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) 
			{
				System.out.println("DateRange.calculateDuration() inc noOfDays");
				noOfDays = noOfDays + 1;
			}
			calendar.add(Calendar.DATE, 1);
			now = calendar.getTime();
			System.out.println("now : " + now);
		}
		
		System.out.println("noOfDays 1 : " + noOfDays);

		// if the start hour is 11 and endHour is 19 then there is day that has not been counted, simply because it spills over to the next day
		if (startDate.getHours() == startHours && endDate.getHours() == endHours) 
		{
			noOfDays = noOfDays + 1;
			System.out.println("noOfDays 2 : " + noOfDays);
		}
		
		// if the start hour and end hour are not 11 and 19 resp, then count extra hours if any
		if (!(startDate.getHours() == startHours && endDate.getHours() == endHours)) 
		{
			int extraHours = 0;
			if (endDate.getHours() > startDate.getHours()) 
			{
				extraHours = endDate.getHours() - startDate.getHours();
			} 
			
			// if end hour is less than start hour, that means though its counted on top as a complete day, it is not
			else if (endDate.getHours() < startDate.getHours()) 
			{
				extraHours = (endHours - startDate.getHours()) + (endDate.getHours() - startHours);
				noOfDays = noOfDays-1;
			}
			System.out.println("extraHours : " + extraHours);
			System.out.println("noOfDays 3 : " + noOfDays);
			
			float extraHoursInDays = extraHours / 8.0F;
			noOfDays = noOfDays + extraHoursInDays;
		}
		
		System.out.println("noOfDays final : " + noOfDays);

		return noOfDays;
	}

	public static Date getStartDateForTheSuccessor(Date endDate, float duration) {
		if (duration == 0) {
			return (Date) endDate.clone();
		}

		Date startDateForSuccessor = (Date) endDate.clone();
		int noOfDays = 0;
		int endDateHours = endDate.getHours();
		if (endDateHours >= endHours) {
			int dayEndExtraHours = endDateHours - endHours;
			noOfDays = noOfDays + 1;
			endDateHours = startHours + dayEndExtraHours;
		}

		startDateForSuccessor.setDate(startDateForSuccessor.getDate() + noOfDays);
		startDateForSuccessor.setHours(endDateHours);

		// System.out.println("startDateForSuccessor : " +
		// startDateForSuccessor);

		return startDateForSuccessor;

	}

	public static float getDurationInDays(float durationInHours) {
		float durationInDays = durationInHours / noOfHoursInAday;
		return durationInDays;
	}

	public static int getDurationInHoursCeiled(float durationInDays) {
		int durationInHours = (int) Math.ceil(durationInDays * noOfHoursInAday);
		return durationInHours;
	}

}