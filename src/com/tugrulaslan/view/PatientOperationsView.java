package com.tugrulaslan.view;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.domain.Patient;
import com.tugrulaslan.util.DatabaseHelper;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PatientOperationsView extends JInternalFrame {

    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel formPanel;

    private final HospitalDAO hospitalDAO;
    private DatabaseHelper db;

    private JLabel lblhospitalId;
    private TextField txtHospitalId;
    private JLabel lblPatientName;
    private TextField txtPatientName;
    private Button btnAddPatient;
    private JTable tblPatients;

    public PatientOperationsView() {
        super("Patient Operations", true, true, true, true);
        setSize(500, 400);
        setLocation(15, 15);
        setLayout(new GridLayout(1, 1, 5, 5));

        //Create Panels
        northPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        southPanel = new JPanel(new BorderLayout());

        hospitalDAO = new HospitalDAOImpl();
        db = new DatabaseHelper();

        //Initialize components
        lblhospitalId = new JLabel("Hospital Id");
        txtHospitalId = new TextField();
        lblPatientName = new JLabel("Patient Name");
        txtPatientName = new TextField();
        btnAddPatient = new Button("Add Patient");
        tblPatients = new JTable();

        //Component settings
        txtHospitalId.setEditable(false);
        txtHospitalId.setEnabled(false);
        txtHospitalId.setText(hospitalDAO.getNewHospitalId().toString());

        formPanel = new JPanel(new FlowLayout(2, 2, 2));

        //Add components into the panels
        formPanel.add(lblhospitalId);
        formPanel.add(txtHospitalId);
        formPanel.add(lblPatientName);
        formPanel.add(txtPatientName);
        formPanel.add(new JLabel());
        formPanel.add(btnAddPatient);
        formPanel.add(Box.createVerticalBox());

        southPanel.add(new JScrollPane(tblPatients), BorderLayout.CENTER);

        northPanel.add(formPanel);
        //Add panels to the Internal Frame
        add(northPanel);
        add(southPanel);

        //Jtable specific
        tblPatients.setModel(getTableData());

        //Add action listener for the add button
        btnAddPatient.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer hospitalId = Integer.parseInt(txtHospitalId.getText());
                String patientName = String.valueOf(txtPatientName.getText());

                if (patientName.isEmpty()) {
                    JOptionPane.showMessageDialog(northPanel, "You've left the Patient Name blank.\n To continue please provide a name", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(northPanel, "The patient " + patientName + " has been added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Patient patient = new Patient(hospitalId, patientName);
                    db.getAllPatients().add(patient);
                    //clear fields
                    clearFields();
                }
            }
        });

    }

    private void clearFields() {
        txtHospitalId.setText(hospitalDAO.getNewHospitalId().toString());
        txtPatientName.setText("");
        tblPatients.setModel(getTableData());
    }

    private DefaultTableModel getTableData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Hospital Id");
        model.addColumn("Patient Name");

        List<Patient> patientListData = db.getAllPatients();
        for (int i = 0; i < patientListData.size(); i++) {
            String[] obj = {
                String.valueOf(patientListData.get(i).getHospitalId()),
                patientListData.get(i).getName(),};
            model.addRow(obj);
        }
        return model;
    }

}
