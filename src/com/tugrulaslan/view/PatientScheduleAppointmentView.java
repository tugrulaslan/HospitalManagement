package com.tugrulaslan.view;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.domain.Patient;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.Box;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PatientScheduleAppointmentView extends JInternalFrame {

    private JPanel northPanel;
    private JPanel southPanel;

    // Form Components
    private JLabel lblHospitalId;
    private TextField txtHospitalId;
    private Button btnGetUser;
    private JLabel lblSpecialists;
    private JList listSpecialists;
    private JLabel lblDate;
    private Button btnPrev;
    private Button btnNext;
    private TextField txtDate;
    private JList listSlots;
    private JLabel lblAvailableSlots;
    private Button btnGetSlotList;
    private JLabel lblpatientAppointments;
    private JTable tblPatientAppointments;
    private Button btnSchedule;
    private JScrollPane slotsScrollPane;
    private JPanel criteriaPanel;
    private JPanel hospitalIdPanel;
    private JPanel specialistPanel;
    private JPanel datePanel;
    private JPanel schedulingPanel;

    private DatabaseHelper db;
    private HospitalDAO hospitalDAO;

    public PatientScheduleAppointmentView() {
        super("Patient Appointments", true, true, true, true);
        setSize(700, 600);
        setLocation(15, 15);
        setSize(600, 600);

        setLayout(new GridLayout(2, 1, 5, 5));

        hospitalDAO = new HospitalDAOImpl();
        db = new DatabaseHelper();

        // Create Panels
        northPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        southPanel = new JPanel(new BorderLayout());

        // Create components for the north panel
        lblHospitalId = new JLabel("Hospital Id");
        txtHospitalId = new TextField();
        btnGetUser = new Button("Get Patient");
        lblSpecialists = new JLabel("Specialists");
        listSpecialists = new JList(db.getallSpecialists().toArray());
        lblDate = new JLabel("Appointment Date");
        txtDate = new TextField(hospitalDAO.convertDateToStringDate(hospitalDAO
                .getCurrentDate(new java.util.Date())));
        btnPrev = new Button("Previous Day");
        btnNext = new Button("Next Day");
        lblAvailableSlots = new JLabel("Available Slots");
        listSlots = new JList();
        slotsScrollPane = new JScrollPane(listSlots);
        btnGetSlotList = new Button("Get Available Slots");
        lblpatientAppointments = new JLabel("Patient Appointments");
        tblPatientAppointments = new JTable();
        btnSchedule = new Button("Schedule Appointment");

        // Element Properties
        listSpecialists
                .setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        txtDate.setEditable(false);
        txtDate.setEnabled(false);
        tblPatientAppointments.setEnabled(false);

        criteriaPanel = new JPanel(new GridLayout(1, 3, 5, 5));

        hospitalIdPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        hospitalIdPanel.add(lblHospitalId);
        hospitalIdPanel.add(txtHospitalId);
        hospitalIdPanel.add(btnGetUser);
        hospitalIdPanel.add(Box.createVerticalBox());
        hospitalIdPanel.add(Box.createVerticalBox());

        specialistPanel = new JPanel(new BorderLayout(5, 5));
        specialistPanel.add(lblSpecialists, BorderLayout.PAGE_START);
        specialistPanel.add(new JScrollPane(listSpecialists), BorderLayout.CENTER);

        datePanel = new JPanel(new GridLayout(0, 1, 5, 5));
        datePanel.add(lblDate);
        datePanel.add(txtDate);
        datePanel.add(btnPrev);
        datePanel.add(btnNext);
        datePanel.add(Box.createVerticalBox());

        criteriaPanel.add(hospitalIdPanel);
        criteriaPanel.add(specialistPanel);
        criteriaPanel.add(datePanel);

        // Add components into the north panel
        schedulingPanel = new JPanel(new FlowLayout());

        schedulingPanel.add(lblAvailableSlots);
        schedulingPanel.add(slotsScrollPane);
        schedulingPanel.add(btnGetSlotList);
        schedulingPanel.add(btnSchedule);

        northPanel.add(criteriaPanel);
        northPanel.add(schedulingPanel);

        southPanel.add(new JScrollPane(tblPatientAppointments), BorderLayout.CENTER);

        // Add panels to the Internal Frame
        add(northPanel);
        add(southPanel);

        // Add Action Listeners
        // Get User Button
        btnGetUser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                if (checkFormElementsPhase1()) {
                    Integer hospitalId = Integer.parseInt(txtHospitalId
                            .getText());

                    Set<Appointment> patientAppointmens = db
                            .getPatientAppointments(hospitalId);
                    if (patientAppointmens.size() > 0) {

                        DefaultTableModel defaultTableModel = new DefaultTableModel();
                        defaultTableModel.addColumn("Hospital Id");
                        defaultTableModel.addColumn("Patient Name");
                        defaultTableModel.addColumn("Booking Code");
                        defaultTableModel.addColumn("Specialist");
                        defaultTableModel.addColumn("Date");
                        defaultTableModel.addColumn("Status");

                        for (Appointment appointment : patientAppointmens) {

                            String[] obj = {
                                String.valueOf(appointment.getPatient()
                                .getHospitalId()),
                                appointment.getPatient().getName(),
                                appointment.getSlot(),
                                appointment.getSpecialist(),
                                hospitalDAO
                                .convertDateToStringDate(appointment
                                .getAppointmentDate()),
                                appointment.getAppointmentStatus()};
                            defaultTableModel.addRow(obj);
                        }
                        tblPatientAppointments.setModel(defaultTableModel);
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

        // Get available slots's Button
        btnGetSlotList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Set<Appointment> specialistAppointments = new TreeSet();
                Map<Integer, String> hourSlots = new TreeMap<>();
                if (checkFormElementsPhase2()) {
                    hourSlots = db.getHourSlotList();

                    String selectedSpecialist = String.valueOf(listSpecialists
                            .getSelectedValue());
                    String currentSelectedDateString = String.valueOf(txtDate
                            .getText());
                    Date selectedDate = hospitalDAO
                            .convertStringDatetoDate(currentSelectedDateString);
                    specialistAppointments = renderAppointmentList(
                            db.getAppointmentsBySpecialist(selectedSpecialist),
                            selectedDate);

                    if (specialistAppointments.size() > 0) {
                        // specialist has appointments
                        buildProperSlotList(hourSlots, specialistAppointments,
                                selectedSpecialist.substring(0, 1), hospitalDAO
                                .getDay(selectedDate).substring(0, 2));
                    } else {
                        // specialist has no appointments
                        buildAvailableSlotList(hourSlots, selectedSpecialist
                                .substring(0, 1),
                                hospitalDAO.getDay(selectedDate)
                                .substring(0, 2));
                    }
                }
            }
        });

        btnSchedule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkFormElementsPhase3()) {
                    hospitalDAO = new HospitalDAOImpl();
                    Integer hospitalIdInteger = Integer.parseInt(txtHospitalId
                            .getText());
                    Patient patient = new Patient(hospitalIdInteger, db
                            .getPatientName(hospitalIdInteger));
                    String specialist = String.valueOf(listSpecialists
                            .getSelectedValue());
                    Date selectedDate = hospitalDAO
                            .convertStringDatetoDate(txtDate.getText());

                    String slot = String.valueOf(listSlots.getSelectedValue());
                    Integer selectedHour = db.getHourIntegerValue(slot
                            .substring(3));

                    Date properDate = hospitalDAO.getDateGivenFormat(
                            selectedDate, selectedHour);

                    Appointment appointment = new Appointment(patient,
                            specialist, properDate, slot,
                            StatusCodes.STATUSCODE_UNATTENDED);
                    // add to the database
                    db.addToAppointment(hospitalIdInteger, appointment);
                    clearFields();
                    JOptionPane.showMessageDialog(northPanel,
                            "The appointment has been successfully scheduled",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private Set<Appointment> renderAppointmentList(
            Set<Appointment> specialistList, Date appointmentDate) {
        hospitalDAO = new HospitalDAOImpl();
        Set<Appointment> appList = new TreeSet<>();
        for (Appointment appointment : specialistList) {
            if (hospitalDAO.areSameDaysStr(appointment.getAppointmentDate(),
                    appointmentDate)
                    && appointment.getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_WAITING)
                    || appointment.getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_INPROGRESS)) {
                appList.add(appointment);
            }
        }
        return appList;
    }

    private boolean checkFormElementsPhase1() {
        String hospitalId = txtHospitalId.getText();
        String currentSelectedDateString = String.valueOf(txtDate.getText());
        Date selectedDate = hospitalDAO
                .convertStringDatetoDate(currentSelectedDateString);
        // Check whether all form elements are completed
        if (hospitalId.isEmpty()) {
            JOptionPane
                    .showMessageDialog(
                            northPanel,
                            "You've not entered your Hospital ID.\n Please provide it to proceed",
                            "Error", JOptionPane.ERROR_MESSAGE);
            return false;

        } // check whether given hospital id is numeric
        else if (!hospitalDAO.isNumeric(hospitalId)) {
            JOptionPane
                    .showMessageDialog(
                            northPanel,
                            "The given Hospital ID "
                            + hospitalId
                            + " is not numeric.\n Please provide a proper one to proceed",
                            "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }// check whether the given patient exists
        else if (!db.patientExists(Integer.parseInt(hospitalId))) {
            JOptionPane
                    .showMessageDialog(
                            northPanel,
                            "The given Hospital ID "
                            + hospitalId
                            + " does not exist.\n Please provide an existing one to proceed",
                            "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean checkFormElementsPhase2() {
        // check first phase is completed
        if (!checkFormElementsPhase1()) {
            return false;
        } // check whether a specialst has been selected
        else if (listSpecialists.getSelectedIndex() == -1) {
            JOptionPane
                    .showMessageDialog(
                            northPanel,
                            "You've not selected a specialist.\n Please select one to proceed",
                            "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean checkFormElementsPhase3() {
        // check first and second phases are completed
        if (!checkFormElementsPhase1() && !checkFormElementsPhase2()) {
            return false;
        }// Check whether a slot is selected
        else if (listSlots.getSelectedIndex() == -1) {
            JOptionPane
                    .showMessageDialog(
                            northPanel,
                            "You've not selected a slot.\n Please select one to proceed",
                            "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void buildAvailableSlotList(Map<Integer, String> slots,
            String specialistLetter, String dayLetter) {

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (Iterator<Entry<Integer, String>> iterator = slots.entrySet()
                .iterator(); iterator.hasNext();) {
            Map.Entry<Integer, String> entry = iterator.next();
            listModel.addElement(specialistLetter + dayLetter
                    + entry.getValue());
        }
        listSlots.setModel(listModel);
    }

    private void buildProperSlotList(Map<Integer, String> givenSlots,
            Set<Appointment> appList, String specialistLetter, String dayLetter) {
        Map<Integer, String> slots = new LinkedHashMap<>();
        hospitalDAO = new HospitalDAOImpl();
        slots.putAll(givenSlots);

        Set<Integer> takenSlots = new TreeSet<>();
        for (Appointment appointment : appList) {
            Integer takenSlot = hospitalDAO.getDateHourVal(appointment
                    .getAppointmentDate());
            takenSlots.add(takenSlot);
        }
        for (Integer obj : takenSlots) {
            for (Iterator<Map.Entry<Integer, String>> iterator = slots
                    .entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<Integer, String> entry = iterator.next();
                if (entry.getKey().equals(obj)) {
                    iterator.remove();
                }
            }
        }

        // check slotlist size if equals zero do nothing
        if (slots.size() > 0) {
            // build the list
            DefaultListModel listModel = new DefaultListModel();
            for (Map.Entry<Integer, String> entry : slots.entrySet()) {
                listModel.addElement(specialistLetter + dayLetter
                        + entry.getValue());
            }
            listSlots.setModel(listModel);
        } else {
            JOptionPane.showMessageDialog(northPanel,
                    "The specialist has no free slot for the selected day",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void clearFields() {
        hospitalDAO = new HospitalDAOImpl();
        txtHospitalId.setText("");
        listSpecialists.setSelectedIndex(0);
        txtDate.setText(hospitalDAO.convertDateToStringDate(hospitalDAO
                .getCurrentDate(new java.util.Date())));
        listSlots.setModel(new DefaultListModel());
        tblPatientAppointments.setModel(new DefaultTableModel());
    }
}
