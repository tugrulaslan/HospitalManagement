package com.tugrulaslan.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tugrul
 */
public class HospitalDAOTest {

    public HospitalDAOTest() {
    }

    Calendar calendar;
    Calendar calendarPassedDay;
    Calendar calendarFutureDay;
    HospitalDAO hospitalDAO;
    Date date;
    SimpleDateFormat simpleDateFormat;
    Calendar getDayCalendar;
    Calendar specificMonth;
    Calendar getHourValue;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
        calendarPassedDay = Calendar.getInstance();
        calendarPassedDay.setTime(new Date());
        calendarPassedDay.set(Calendar.YEAR, 2015);
        calendarPassedDay.set(Calendar.MONTH, 2);

        hospitalDAO = new HospitalDAOImpl();

        calendarFutureDay = Calendar.getInstance();
        calendarFutureDay.setTime(new Date());
        calendarFutureDay.set(Calendar.YEAR, 2015);
        calendarFutureDay.set(Calendar.MONTH, 2);

        date = new Date();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        getDayCalendar = Calendar.getInstance();
        specificMonth = Calendar.getInstance();
        getHourValue = Calendar.getInstance();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetPreviousDay() {
        calendarPassedDay.set(Calendar.DAY_OF_MONTH, 9);
        Date result = hospitalDAO.getPreviousDay(calendarPassedDay.getTime());

        calendarPassedDay.set(Calendar.DAY_OF_MONTH, 6);
        assertEquals(calendarPassedDay.getTime(), result);

    }

    @Test
    public void testGetNextDay() {
        calendarFutureDay.set(Calendar.DAY_OF_MONTH, 12);
        Date result = hospitalDAO.getNextDay(calendarFutureDay.getTime());
        calendarFutureDay.set(Calendar.DAY_OF_MONTH, 13);
        assertEquals(calendarFutureDay.getTime(), result);
    }

    @Test
    public void testConvertStringDatetoDate() throws ParseException {
        String formattedDate = "27-03-2015";
        Date expResult = simpleDateFormat.parse(formattedDate);
        Date result = hospitalDAO.convertStringDatetoDate(formattedDate);
        assertEquals(expResult, result);
    }

    @Test
    public void testConvertDateToStringDate() {
        String expResult = simpleDateFormat.format(date);
        String result = hospitalDAO.convertDateToStringDate(date);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsLateAppointment() {

        boolean expResult = false;
        boolean result = hospitalDAO.isLateAppointment(date);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDay() {
        getDayCalendar.set(Calendar.DAY_OF_MONTH, 27);
        String expResult = "FRIDAY";
        String result = hospitalDAO.getDay(getDayCalendar.getTime());
        assertEquals(expResult, result);
    }

    @Test
    public void testIsNumericTrue() {
        testIsNumeric("123", true);
    }

    @Test
    public void testIsNumericFalse() {
        testIsNumeric("123a", false);
    }

    private void testIsNumeric(String number, boolean expResult) {
        boolean result = hospitalDAO.isNumeric(number);
        assertEquals(expResult, result);
    }

    @Test
    public void testAreSameDaysStr() {
        String dateStrValue = simpleDateFormat.format(date);
        String dateStrValue2 = simpleDateFormat.format(date);
        boolean expResult = false;

        boolean result = hospitalDAO.areSameDaysStr(date, date);
        expResult = dateStrValue.equals(dateStrValue2);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsSpecificMonth() {
        calendar.setTime(date);
        boolean expResult = true;
        boolean result = hospitalDAO.isSpecificMonth(calendar.getTime(), calendar.get(Calendar.MONTH));
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCurrentDate() {
        Date result = hospitalDAO.getCurrentDate(date);
        assertEquals(date, result);
    }

    @Test
    public void testGetDateHourVal() {
        getHourValue.setTime(date);
        getHourValue.set(Calendar.HOUR_OF_DAY, 13);
        Integer result = hospitalDAO.getDateHourVal(getHourValue.getTime());
        Integer expResult = getHourValue.get(Calendar.HOUR_OF_DAY);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDateGivenFormat() {
        Integer hour = 14;
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        HospitalDAO hospitalDAO = new HospitalDAOImpl();
        Date expResult = calendar.getTime();
        Date result = hospitalDAO.getDateGivenFormat(date, hour);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsFutureDate() {

        boolean expResult = false;
        boolean result = hospitalDAO.isFutureDate(date);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDueDays() {

        Integer expResult = 0;
        Integer result = hospitalDAO.getDueDays(date);
        assertEquals(expResult, result);
    }

}
