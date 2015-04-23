package com.tugrulaslan.dao;

import com.tugrulaslan.domain.Appointment;
import java.util.Date;

public interface HospitalDAO {

	public Date getPreviousDay(Date currentDate);

	public Date getNextDay(Date currentDate);

	public boolean isWeekday(Date date);

	public Date convertStringDatetoDate(String formattedDate);

	public String convertDateToStringDate(Date date);

	public boolean isLateAppointment(Date date);

	public String getDay(Date date);

	public Integer getNewHospitalId();

	public boolean isNumeric(String number);

	public boolean appointmentStatus(Appointment appointment);

	public boolean areSameDaysStr(Date date1, Date date2);

	public String[] getMonths();

	public boolean isSpecificMonth(Date date, Integer month);

	public Date getCurrentDate(Date date);

	public void frontDesk();

	public Integer getDateHourVal(Date date);

	public Date getDateGivenFormat(Date date, Integer hour);

	public boolean isFutureDate(Date date);

	public Integer getDueDays(Date date);

}
