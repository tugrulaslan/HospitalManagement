package com.tugrulaslan.view;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.util.DatabaseHelper;
import com.tugrulaslan.util.StatusCodes;
import java.awt.BorderLayout;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class SpecialistAppointmentsView extends JInternalFrame {

    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel specialistPanel;
    private JPanel datePanel;
    private JPanel criteriaPanel;
    private JPanel buttonPanel;

    // Form Components
    private JLabel lblSpecialist;
    private JComboBox cmbSpecialist;

    private JLabel lblDate;
    private Button btnPrev;
    private Button btnNext;
    private TextField txtDate;

    private Button btnGetSpecialist;
    private JTable tblAppointments;
    private Button btnAttendAppointment;
    private Button btnCancelAppointment;
    private Button btnCompleteAppointment;

    private DatabaseHelper db;
    private HospitalDAO hospitalDAO;

    private Set<Appointment> specialistAppointments = new TreeSet();
    private Set<Appointment> appointments = new TreeSet<>();

    public SpecialistAppointmentsView() {
        super("Specialist Appointments", true, true, true, true);
        setSize(550, 550);
        setLocation(15, 15);
        setLayout(new BorderLayout());

        northPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new BorderLayout());
        hospitalDAO = new HospitalDAOImpl();
        db = new DatabaseHelper();

        lblSpecialist = new JLabel("Specialist");
        cmbSpecialist = new JComboBox(db.getallSpecialists().toArray());
        txtDate = new TextField(hospitalDAO.convertDateToStringDate(hospitalDAO
                .getCurrentDate(new java.util.Date())));
        lblDate = new JLabel("Appointment Date");
        btnPrev = new Button("Previous Day");
        btnNext = new Button("Next Day");
        btnGetSpecialist = new Button("Get Specialist Appointments");
        tblAppointments = new JTable();
        btnAttendAppointment = new Button("Attend to Appointment");
        btnCancelAppointment = new Button("Cancel Appointment");
        btnCompleteAppointment = new Button("Complete Appointment");

        // Component properties
        tblAppointments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtDate.setEditable(false);
        txtDate.setEnabled(false);

        //specialistpanel
        specialistPanel = new JPanel(new FlowLayout());
        specialistPanel.add(lblSpecialist);
        specialistPanel.add(cmbSpecialist);

        //date panel
        datePanel = new JPanel(new GridLayout(2, 2, 5, 15));
        datePanel.add(lblDate);
        datePanel.add(txtDate);
        datePanel.add(btnPrev);
        datePanel.add(btnNext);

        //criteria panel
        criteriaPanel = new JPanel(new GridLayout(1, 2, 3, 3));
        criteriaPanel.add(specialistPanel);
        criteriaPanel.add(datePanel);

        //buttonPanel
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnGetSpecialist);
        buttonPanel.add(btnAttendAppointment);
        buttonPanel.add(btnCancelAppointment);
        buttonPanel.add(btnCompleteAppointment);

        northPanel.add(criteriaPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        southPanel.add(new JScrollPane(tblAppointments), BorderLayout.CENTER);

        // Add panels to the Internal Frame
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        // add listeners
        btnCancelAppointment.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (tblAppointments.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(northPanel,
                            "You have not chosen an appointment to cancel",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    Appointment appointment = getSelectedAppointment();
                    if (appointment.getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_INPROGRESS)) {
                        JOptionPane.showMessageDialog(northPanel,
                                "You cannot cancel an ongoing appointment",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        db.alterAppointmentStatus(appointment,
                                StatusCodes.STATUSCODE_CANCELLED);
                        setTableData();
                    }

                }

            }
        });

        btnAttendAppointment.addActionListener(new ActionListener() {

            String selectedSpecialist = String.valueOf(cmbSpecialist
                    .getSelectedItem());

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblAppointments.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(northPanel,
                            "You have not chosen an appointment to attend",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (db.isSpecialistOccupied(selectedSpecialist)) {
                    JOptionPane
                            .showMessageDialog(
                                    northPanel,
                                    "The specialist "
                                    + selectedSpecialist
                                    + " has already attended to an appointment.\n Finish the active appointment and then attend to the selected one",
                                    "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    Appointment appointment = getSelectedAppointment();
                    if (appointment.getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_WAITING)) {
                        db.alterAppointmentStatus(appointment,
                                StatusCodes.STATUSCODE_INPROGRESS);
                        setTableData();
                    } else {
                        JOptionPane
                                .showMessageDialog(
                                        northPanel,
                                        "The patient "
                                        + appointment.getPatient()
                                        .getName()
                                        + " has not attended to their appointment yet",
                                        "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                }
            }
        });

        btnGetSpecialist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String currentSelectedDateString = String.valueOf(txtDate
                        .getText());
                String selectedSpecialist = String.valueOf(cmbSpecialist
                        .getSelectedItem());
                Date selectedDate = hospitalDAO
                        .convertStringDatetoDate(currentSelectedDateString);

                specialistAppointments = renderSpecialistAppointments(
                        db.getAppointmentsBySpecialist(selectedSpecialist),
                        selectedDate);

                if (specialistAppointments.size() > 0) {
                    // specialist has appointments
                    setTableData();
                } else {
                    // specialist has no appointments
                    JOptionPane.showMessageDialog(northPanel,
                            "Selected specialist " + selectedSpecialist
                            + " has no appointments in "
                            + currentSelectedDateString, "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    tblAppointments.setModel(new DefaultTableModel());
                }

            }

        });

        btnCompleteAppointment.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (tblAppointments.getSelectedRow() == -1) {
                    JOptionPane
                            .showMessageDialog(
                                    northPanel,
                                    "You have not chosen an ongoing appointment to complete",
                                    "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    Appointment appointment = getSelectedAppointment();
                    if (appointment.getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_INPROGRESS)) {
                        db.alterAppointmentStatus(appointment,
                                StatusCodes.STATUSCODE_FINISHED);
                        setTableData();
                    } else {
                        JOptionPane.showMessageDialog(northPanel,
                                "The selected appointment is not in progress",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

            }
        });

        // Next Day's Button
        btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                // Acquire current value
                String currentSelectedDateString = String.valueOf(txtDate
                        .getText());
                Date currentSelectedDate;
                Date nextDateValue;

                currentSelectedDate = hospitalDAO
                        .convertStringDatetoDate(currentSelectedDateString);
                nextDateValue = hospitalDAO.getNextDay(currentSelectedDate);
                txtDate.setText(hospitalDAO
                        .convertDateToStringDate(nextDateValue));

            }
        });

        // Previous Day's Button
        btnPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Acquire current value
                String currentSelectedDateString = String.valueOf(txtDate
                        .getText());
                String todaysDate = hospitalDAO
                        .convertDateToStringDate(hospitalDAO
                                .getCurrentDate(new java.util.Date()));
                Date currentSelectedDate;
                Date previousDateValue;
                // check whether it the current day's date
                if (!currentSelectedDateString.equals(todaysDate)) {
                    // proceed to provide the previous date
                    currentSelectedDate = hospitalDAO
                            .convertStringDatetoDate(currentSelectedDateString);
                    previousDateValue = hospitalDAO
                            .getPreviousDay(currentSelectedDate);
                    txtDate.setText(hospitalDAO
                            .convertDateToStringDate(previousDateValue));
                }
            }
        });

    }

    private Appointment getSelectedAppointment() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) tblAppointments
                .getModel();
        Integer hospitalId = Integer.parseInt(String.valueOf(defaultTableModel
                .getValueAt(tblAppointments.getSelectedRow(), 0)));
        String bookingCode = String.valueOf(String.valueOf(defaultTableModel
                .getValueAt(tblAppointments.getSelectedRow(), 2)));
        Appointment appointment = db.getAppointment(hospitalId, bookingCode);
        return appointment;

    }

    private Set<Appointment> renderSpecialistAppointments(
            Set<Appointment> appList, Date date) {
        hospitalDAO = new HospitalDAOImpl();

        for (Appointment appointment : appList) {
            if (hospitalDAO.areSameDaysStr(appointment.getAppointmentDate(),
                    date)) {
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public void setTableData() {

        String currentSelectedDateString = String.valueOf(txtDate.getText());
        String selectedSpecialist = String.valueOf(cmbSpecialist
                .getSelectedItem());
        Date selectedDate = hospitalDAO
                .convertStringDatetoDate(currentSelectedDateString);
        Set<Appointment> specialistAppointments = new TreeSet();
        specialistAppointments = renderSpecialistAppointments(
                db.getAppointmentsBySpecialist(selectedSpecialist),
                selectedDate);

        Set<Appointment> specialistAppointmens = renderSpecialistAppointments(
                db.getSpecialistAppointments(selectedSpecialist), selectedDate);
        if (specialistAppointmens.size() > 0) {
            DefaultTableModel defaultTableModel = new DefaultTableModel();
            defaultTableModel.addColumn("Hospital Id");
            defaultTableModel.addColumn("Patient Name");
            defaultTableModel.addColumn("Booking Code");
            defaultTableModel.addColumn("Specialist");
            defaultTableModel.addColumn("Date");
            defaultTableModel.addColumn("Status");

            Iterator<Appointment> iterator = specialistAppointmens.iterator();
            while (iterator.hasNext()) {
                Appointment appointment = iterator.next();
                String[] obj = {
                    String.valueOf(appointment.getPatient().getHospitalId()),
                    appointment.getPatient().getName(),
                    appointment.getSlot(),
                    appointment.getSpecialist(),
                    hospitalDAO.convertDateToStringDate(appointment
                    .getAppointmentDate()),
                    appointment.getAppointmentStatus()};
                defaultTableModel.addRow(obj);

            }

            tblAppointments.setModel(defaultTableModel);
        } else {
            tblAppointments.setModel(new DefaultTableModel());
            JOptionPane.showMessageDialog(northPanel,
                    "The specialist has no appointment for the given day",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
