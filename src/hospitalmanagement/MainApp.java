package hospitalmanagement;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.domain.Patient;
import com.tugrulaslan.util.DatabaseHelper;
import com.tugrulaslan.util.StatusCodes;
import com.tugrulaslan.view.HospitalMainView;
import java.util.Date;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainApp {

    private static HospitalDAO hospitalDAO;

    private static String buildSlotCode(Date appointmentDate, String specialist, String hourValue) {
        hospitalDAO = new HospitalDAOImpl();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(specialist.substring(0, 1));
        stringBuilder.append(hospitalDAO.getDay(appointmentDate).substring(0, 2));
        stringBuilder.append(hourValue);

        return stringBuilder.toString();
    }

    public static void main(String[] args) {

        DatabaseHelper db = new DatabaseHelper();
        hospitalDAO = new HospitalDAOImpl();
        Date currentDate = new java.util.Date();
        Date previousDay = hospitalDAO.getPreviousDay(currentDate);
        Date nextDay = hospitalDAO.getNextDay(currentDate);

        // Add slot values
        db.addToHourSlot(9, "09");
        db.addToHourSlot(10, "10");
        db.addToHourSlot(11, "11");
        db.addToHourSlot(12, "12");
        db.addToHourSlot(13, "01");
        db.addToHourSlot(14, "02");
        db.addToHourSlot(15, "03");
        db.addToHourSlot(16, "04");
        db.addToHourSlot(17, "05");

        String specialist = "Cardiac";
        String specialist1 = "Renal";
        String specialist2 = "Paediatric";
        String specialist3 = "Oncologist";
        String specialist4 = "Neuro Surgeon";

        // Add 5 sort of specialists
        db.addToSpecialist(specialist);
        db.addToSpecialist(specialist1);
        db.addToSpecialist(specialist2);
        db.addToSpecialist(specialist3);
        db.addToSpecialist(specialist4);

        // Add 5 Patients
        Patient patient = new Patient(hospitalDAO.getNewHospitalId(), "Tugrul");
        Patient patient1 = new Patient(hospitalDAO.getNewHospitalId(), "John");
        Patient patient2 = new Patient(hospitalDAO.getNewHospitalId(), "Walter");
        Patient patient3 = new Patient(hospitalDAO.getNewHospitalId(), "Hollie");
        Patient patient4 = new Patient(hospitalDAO.getNewHospitalId(), "Jane");
        Patient patient5 = new Patient(hospitalDAO.getNewHospitalId(), "David");

        db.addToPatients(patient);
        db.addToPatients(patient1);
        db.addToPatients(patient2);
        db.addToPatients(patient3);
        db.addToPatients(patient4);

        // 6 passed appointments
        Appointment appointment = new Appointment(patient, specialist4, hospitalDAO.getDateGivenFormat(previousDay, 9), buildSlotCode(previousDay, specialist4, "09"),
                StatusCodes.STATUSCODE_FINISHED);
        Appointment appointment2 = new Appointment(patient2, specialist4, hospitalDAO.getDateGivenFormat(previousDay, 11), buildSlotCode(previousDay, specialist4, "11"),
                StatusCodes.STATUSCODE_CANCELLED);

        //current appointments
        Appointment appointment6 = new Appointment(patient, specialist2, hospitalDAO.getDateGivenFormat(currentDate, 9), buildSlotCode(currentDate, specialist2, "09"),
                StatusCodes.STATUSCODE_FINISHED);
        Appointment appointment7 = new Appointment(patient1, specialist, hospitalDAO.getDateGivenFormat(currentDate, 10), buildSlotCode(currentDate, specialist, "10"),
                StatusCodes.STATUSCODE_CANCELLED);

        //Future Appointments
        Appointment appointment12 = new Appointment(patient5, specialist, hospitalDAO.getDateGivenFormat(nextDay, 9), buildSlotCode(nextDay, specialist, "09"),
                StatusCodes.STATUSCODE_UNATTENDED);
        Appointment appointment13 = new Appointment(patient4, specialist1, hospitalDAO.getDateGivenFormat(nextDay, 11), buildSlotCode(nextDay, specialist1, "11"),
                StatusCodes.STATUSCODE_UNATTENDED);

        //add passed appointments to database
        db.addToAppointment(patient.getHospitalId(), appointment);
        db.addToAppointment(patient2.getHospitalId(), appointment2);

        //add current appointments to database
        db.addToAppointment(patient.getHospitalId(), appointment6);
        db.addToAppointment(patient1.getHospitalId(), appointment7);

        //add future appointments to database
        db.addToAppointment(patient5.getHospitalId(), appointment12);
        db.addToAppointment(patient4.getHospitalId(), appointment13);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                    new HospitalMainView().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex);
                } catch (InstantiationException ex) {
                    System.err.println(ex);
                } catch (IllegalAccessException ex) {
                    System.err.println(ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    System.err.println(ex);
                }
            }
        });

        /*Keep checking and canceling late appointments every 5 seconds throughout the life time of the application*/
        while (true) {
            try {
                Thread.sleep(5000);
                hospitalDAO.frontDesk();
            } catch (InterruptedException e) {
                System.err.println(e);
            }

        }

    }

}
