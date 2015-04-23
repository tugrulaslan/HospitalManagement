package com.tugrulaslan.view;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.util.DatabaseHelper;
import com.tugrulaslan.util.StatusCodes;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import javax.swing.JScrollPane;

public class AdminReportsView extends JInternalFrame {

    private JPanel northPanel;
    private JPanel southPanel;
    private HospitalDAO hospitalDAO;
    private DatabaseHelper db;

    private JLabel lblSpecialist;
    private JComboBox cmbSpecialist;
    private JLabel lblMonth;
    private JComboBox cmbMonth;
    private JLabel lblCompletion;
    private JLabel lblAmountCompletion;
    private JLabel lblCancellation;
    private JLabel lblAmountCancellation;
    private Button btnGetReport;
    private JTable tblPatientList;
    private static int completedAmount;
    private static int cancelledAmount;
    
    private Set<Appointment> specialistAppointments = new TreeSet<>();

    public AdminReportsView() {
        super("Administrator Reports", true, true, true, true);
        setSize(560, 570);
        setLocation(15, 15);

        hospitalDAO = new HospitalDAOImpl();
        db = new DatabaseHelper();
        setLayout(new BorderLayout());

        // Create Panels
        northPanel = new JPanel(new GridLayout(5, 2, 2, 2));
        southPanel = new JPanel(new BorderLayout());

        // Create components
        lblSpecialist = new JLabel("Specialist");
        cmbSpecialist = new JComboBox(db.getallSpecialists().toArray());
        lblMonth = new JLabel("Month");
        cmbMonth = new JComboBox(hospitalDAO.getMonths());
        lblCompletion = new JLabel("Finished Appointments");
        lblAmountCompletion = new JLabel("");
        lblCancellation = new JLabel("Cancelled Appointments");
        lblAmountCancellation = new JLabel("");
        btnGetReport = new Button("Get Report");
        tblPatientList = new JTable();

        // Component Settings
        tblPatientList.setEnabled(false);

        // Add components into the panels
        northPanel.add(lblSpecialist);
        northPanel.add(cmbSpecialist);
        northPanel.add(lblMonth);
        northPanel.add(cmbMonth);
        northPanel.add(lblCompletion);
        northPanel.add(lblAmountCompletion);
        northPanel.add(lblCancellation);
        northPanel.add(lblAmountCancellation);
        northPanel.add(btnGetReport);
        northPanel.add(new JLabel());

        southPanel.add(new JScrollPane(tblPatientList), BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        btnGetReport.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                clearAmounts();
                String selectedSpecialist = String.valueOf(cmbSpecialist
                        .getSelectedItem());
                String selectedMonth = String.valueOf(cmbMonth
                        .getSelectedItem());
                specialistAppointments = renderSpecialistList(
                        db.getAppointmentsBySpecialist(selectedSpecialist),
                        cmbMonth.getSelectedIndex());

                if (specialistAppointments.size() > 0) {
                    lblAmountCompletion.setText(String.valueOf(completedAmount));
                    lblAmountCancellation.setText(String
                            .valueOf(cancelledAmount));

                    DefaultTableModel defaultTableModel = new DefaultTableModel();
                    defaultTableModel.addColumn("Hospital Id");
                    defaultTableModel.addColumn("Patient Name");
                    defaultTableModel.addColumn("Booking Code");
                    defaultTableModel.addColumn("Specialist");
                    defaultTableModel.addColumn("Date");
                    defaultTableModel.addColumn("Status");

                    for (Appointment appointment : specialistAppointments) {

                        String[] obj = {
                            String.valueOf(appointment.getPatient()
                            .getHospitalId()),
                            appointment.getPatient().getName(),
                            appointment.getSlot(),
                            appointment.getSpecialist(),
                            hospitalDAO.convertDateToStringDate(appointment
                            .getAppointmentDate()),
                            appointment.getAppointmentStatus()};
                        defaultTableModel.addRow(obj);
                    }

                    tblPatientList.setModel(defaultTableModel);

                } else {
                    JOptionPane.showMessageDialog(northPanel,
                            selectedSpecialist + " has no appointments in "
                            + selectedMonth, "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    clearFormElements();
                }
            }
        });

    }

    Set<Appointment> renderSpecialistList(Set<Appointment> list, Integer month) {
        Iterator<Appointment> iterator = list.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = (Appointment) iterator.next();
            //pick only finished and cancelled days
            if (hospitalDAO.isSpecificMonth(appointment.getAppointmentDate(), month) && (appointment.getAppointmentStatus().equals(
                    StatusCodes.STATUSCODE_FINISHED) || appointment
                    .getAppointmentStatus().equals(
                            StatusCodes.STATUSCODE_CANCELLED))) {
                //increase amounts for each status
                if (appointment.getAppointmentStatus().equals(
                        StatusCodes.STATUSCODE_FINISHED)) {
                    completedAmount++;
                } else {
                    cancelledAmount++;
                }
            } else {
                //remove
                iterator.remove();
            }

        }
        return list;
    }

    private void clearFormElements() {
        cmbSpecialist.setSelectedIndex(0);
        cmbMonth.setSelectedIndex(0);
        lblAmountCompletion.setText("");
        lblAmountCancellation.setText("");
        tblPatientList.setModel(new DefaultTableModel());
    }

    private void clearAmounts() {
        cancelledAmount = 0;
        completedAmount = 0;
        lblAmountCompletion.setText("");
        lblAmountCancellation.setText("");
        tblPatientList.setModel(new DefaultTableModel());
    }
}
