package com.tugrulaslan.view;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.util.DatabaseHelper;
import com.tugrulaslan.util.StatusCodes;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PatientAttedanceView extends JInternalFrame {

    private JPanel northPanel;
    private HospitalDAO hospitalDAO;
    private DatabaseHelper db;

    private JLabel lblhospitalId;
    private TextField txtHospitalId;
    private JLabel lblBookingCode;
    private TextField txtBookingCode;
    private JLabel lblStatus;
    private JLabel lblCurrentStatus;
    private Button btnProceed;
    private Button btnClearFields;
    private Appointment appointment;

    public PatientAttedanceView() {

        super("Patient Attending to Appointment", true, true, true, true);
        setSize(300, 200);
        setLocation(15, 15);
        setLayout(new BorderLayout());

        // Create Panels
        northPanel = new JPanel(new GridLayout(4, 2));

        // Create components
        lblhospitalId = new JLabel("Hospital Id");
        txtHospitalId = new TextField();
        lblBookingCode = new JLabel("Booking Code");
        txtBookingCode = new TextField();
        lblStatus = new JLabel("Status");
        lblCurrentStatus = new JLabel();
        btnProceed = new Button("Attend");
        btnClearFields = new Button("Clear Fields");

        hospitalDAO = new HospitalDAOImpl();
        db = new DatabaseHelper();

        // Add components into the panels
        northPanel.add(lblhospitalId);
        northPanel.add(txtHospitalId);
        northPanel.add(lblBookingCode);
        northPanel.add(txtBookingCode);
        northPanel.add(lblStatus);
        northPanel.add(lblCurrentStatus);
        northPanel.add(btnProceed);
        northPanel.add(btnClearFields);

        add(northPanel, BorderLayout.CENTER);

        btnProceed.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                patientStatus();
            }
        });

        btnClearFields.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clearFields();
            }
        });
    }

    private void clearFields() {
        txtHospitalId.setText("");
        txtBookingCode.setText("");
        lblCurrentStatus.setText("");
    }

    private void patientStatus() {

       appointment = new Appointment();

        String hospitalIdVal = String.valueOf(txtHospitalId.getText());
        boolean isNumeric = hospitalDAO.isNumeric(String.valueOf(txtHospitalId
                .getText()));
        String bookingCode = String.valueOf(txtBookingCode.getText())
                .toUpperCase();

        if (hospitalIdVal.isEmpty()) {
            JOptionPane.showMessageDialog(northPanel,
                    "You've not entered your Hospital Id", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else if (!isNumeric) {
            JOptionPane.showMessageDialog(northPanel,
                    "The value you've entered for Hospital Id is not numeric",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else if (bookingCode.isEmpty()) {
            JOptionPane.showMessageDialog(northPanel,
                    "You've not entered your booking code", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int hospitalid = Integer.parseInt(txtHospitalId.getText());
            appointment = db.getAppointment(hospitalid, bookingCode);

            if (appointment == null) {
                JOptionPane.showMessageDialog(northPanel,
                        "No appointment is found with the given details",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else if (hospitalDAO.isFutureDate(appointment
                    .getAppointmentDate())
                    && appointment.getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_UNATTENDED)) {
                JOptionPane.showMessageDialog(
                        northPanel,
                        "You cannot attend to a forthcoming due appointment which is in "
                        + hospitalDAO.getDueDays(appointment
                                .getAppointmentDate())
                        + " days. Only daily attendance is allowed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }// A completed appointment
            else if (appointment.getAppointmentStatus().equals(
                    StatusCodes.STATUSCODE_FINISHED)) {
                lblCurrentStatus.setText(StatusCodes.STATUSCODE_FINISHED);
                // A cancelled appointment
            } else if (appointment.getAppointmentStatus().equals(
                    StatusCodes.STATUSCODE_CANCELLED)) {
                lblCurrentStatus.setText(StatusCodes.STATUSCODE_CANCELLED);
            } // Appointment is in progress
            else if (appointment.getAppointmentStatus().equals(
                    StatusCodes.STATUSCODE_INPROGRESS)) {
                lblCurrentStatus.setText(StatusCodes.STATUSCODE_INPROGRESS);
            }// Patient is waiting
            else if (appointment.getAppointmentStatus().equals(
                    StatusCodes.STATUSCODE_WAITING)) {
                lblCurrentStatus.setText(StatusCodes.STATUSCODE_WAITING);
            }// status code is undefined
            else {
                // Define the status
                if (hospitalDAO.appointmentStatus(appointment)) {
                    // recall the method
                    patientStatus();
                }
            }
        }
    }
}
