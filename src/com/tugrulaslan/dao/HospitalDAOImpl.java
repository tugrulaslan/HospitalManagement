package com.tugrulaslan.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.util.DatabaseHelper;
import com.tugrulaslan.util.RandomIDGenerator;
import com.tugrulaslan.util.StatusCodes;

public class HospitalDAOImpl implements HospitalDAO {

    private DatabaseHelper db = new DatabaseHelper();
    private RandomIDGenerator RandomIDGenerator = new RandomIDGenerator();

    @Override
    public Date getPreviousDay(Date currentDate) {
        /* This function only return a weekday, weekdays are excluded */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -1);
        if (isWeekday(calendar.getTime())) {
            return calendar.getTime();
        } else {
            return getPreviousDay(calendar.getTime());
        }
    }

    @Override
    public Date getNextDay(Date currentDate) {
        /* This function only return a weekday, weekdays are excluded */
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 1);
        // check whether the next day value isn't a week day
        if (isWeekday(calendar.getTime())) {
            date = calendar.getTime();
        } else {
            date = getNextDay(calendar.getTime());
        }
        return date;
    }

    @Override
    public boolean isWeekday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return true;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            return true;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            return true;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return true;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Date convertStringDatetoDate(String formattedDate) {
        Date date = new Date();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy");
            date = simpleDateFormat.parse(formattedDate);
        } catch (ParseException ex) {
            System.err.println("convertFormattedtoDate " + ex);
        }
        return date;
    }

    @Override
    public String convertDateToStringDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    @Override
    public boolean isLateAppointment(Date date) {

        Integer difference = getDueDays(date);
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.setTime(date);
        int givenHour = calendar.get(Calendar.HOUR_OF_DAY);

        calendar1.setTime(new java.util.Date());
        int currentHour = calendar1.get(Calendar.HOUR_OF_DAY);

        if (difference > 0) {
            return false;
        } else if (difference == 0 && givenHour >= currentHour) {
            return false;
        }
        return true;
    }

    @Override
    public String getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayValue = "";
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            dayValue = "MONDAY";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            dayValue = "TUESDAY";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            dayValue = "WEDNESDAY";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            dayValue = "THURSDAY";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            dayValue = "FRIDAY";
        } else {
            dayValue = "";
        }
        return dayValue;
    }

    @Override
    public Integer getNewHospitalId() {
        return RandomIDGenerator.newRandomID();
    }

    @Override
    public boolean isNumeric(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean appointmentStatus(Appointment appointment) {

        // check whether appointment is late
        if (isLateAppointment(appointment.getAppointmentDate())) {
            // the appointment is late cancel it
            return db.alterAppointmentStatus(appointment,
                    StatusCodes.STATUSCODE_CANCELLED);
        } else {
            // Set the appointment as WAITING
            return db.alterAppointmentStatus(appointment,
                    StatusCodes.STATUSCODE_WAITING);
        }
    }

    @Override
    public boolean areSameDaysStr(Date date1, Date date2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = simpleDateFormat.format(date1);
        String dateStr1 = simpleDateFormat.format(date2);
        return (dateStr1.equals(dateStr1) && dateStr1.equals(dateStr));
    }

    @Override
    public String[] getMonths() {
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY",
            "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER",
            "DECEMBER"};
        return months;
    }

    @Override
    public boolean isSpecificMonth(Date date, Integer month) {
        /*
         Compares given the date's month with the month integer value which is 
         transformed to date format to be compared with the given date
         */
        Date thisDate = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.setTime(date);
        calendar1.setTime(thisDate);
        calendar1.set(Calendar.MONTH, month);

        return (calendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH) && calendar1
                .get(Calendar.MONTH) == calendar.get(Calendar.MONTH));
    }

    @Override
    public Date getCurrentDate(Date date) {
        if (isWeekday(date)) {
            return date;
        } else {
            return getCurrentDate(getNextDay(date));
        }
    }

    @Override
    public void frontDesk() {
        List<Appointment> appointments = new ArrayList<>();
        appointments = db.getAllAppointments();

        for (Appointment appointment : appointments) {

            if (appointment.getAppointmentStatus().equals(
                    StatusCodes.STATUSCODE_UNATTENDED)
                    && isLateAppointment(appointment.getAppointmentDate())) {
                db.alterAppointmentStatus(appointment,
                        StatusCodes.STATUSCODE_CANCELLED);
            }
        }
    }

    @Override
    public Integer getDateHourVal(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public Date getDateGivenFormat(Date date, Integer hour) {

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar1.setTime(date);

        calendar = calendar1;

        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Date newDate = calendar.getTime();

        return newDate;
    }

    @Override
    public boolean isFutureDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.setTime(date);
        calendar1.setTime(new java.util.Date());

        int difference = calendar.get(Calendar.DAY_OF_YEAR)
                - calendar1.get(Calendar.DAY_OF_YEAR);

        if (difference > 0) {
            return true;
        }

        return false;
    }

    @Override
    public Integer getDueDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.setTime(date);
        calendar1.setTime(new java.util.Date());

        return calendar.get(Calendar.DAY_OF_YEAR)
                - calendar1.get(Calendar.DAY_OF_YEAR);
    }

}
