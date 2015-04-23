package com.tugrulaslan.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class HospitalMainView extends JFrame {

    private JDesktopPane jDesktopPane;
    private JMenuBar jMenuBar;

    private JMenu jMenuPatient;
    private JMenuItem jMenuItemPatientOperations;
    private JMenuItem jMenuItemSchedule;
    private JMenuItem jMenuItemAttend;

    private JMenu jMenuSpecialist;
    private JMenuItem jMenuItemSpecialistOperations;
    private JMenuItem jMenuItemAppointments;

    private JMenu jMenuAdmin;
    private JMenuItem jMenuItemReports;

    private JFrame jFrame;

    public HospitalMainView() {
        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jDesktopPane = new JDesktopPane();
        this.add(jDesktopPane);

        jMenuBar = new JMenuBar();

        /*
         Patient Menu Items
         */
        jMenuPatient = new JMenu("Patient");
        jMenuItemPatientOperations = new JMenuItem("Patient Operations");
        jMenuItemSchedule = new JMenuItem("Schedule Appointment");
        jMenuItemAttend = new JMenuItem("Attend to Appointment");

        //Add menu items to their main container
        jMenuPatient.add(jMenuItemPatientOperations);
        jMenuPatient.add(jMenuItemSchedule);
        jMenuPatient.add(jMenuItemAttend);

        //Add main container to parent menu
        jMenuBar.add(jMenuPatient);

        /*
         Specialist Menu Items
         */
        jMenuSpecialist = new JMenu("Specialist");
        jMenuItemSpecialistOperations = new JMenuItem("Specialist Operations");
        jMenuItemAppointments = new JMenuItem("Appointments");

        //Add menu items to their main container
        jMenuSpecialist.add(jMenuItemSpecialistOperations);
        jMenuSpecialist.add(jMenuItemAppointments);

        //Add main container to parent menu
        jMenuBar.add(jMenuSpecialist);

        /*
         Admin Menu Items
         */
        jMenuAdmin = new JMenu("Administrator");
        jMenuItemReports = new JMenuItem("Reports");

        //Add menu items to their main container
        jMenuAdmin.add(jMenuItemReports);

        //Add main container to parent menu
        jMenuBar.add(jMenuAdmin);

        setJMenuBar(jMenuBar);

        //set it to be full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Action listeners for menu items
        //PatientScheduleAppointmentView listener
        jMenuItemSchedule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                PatientScheduleAppointmentView patientScheduleAppointmentView = new PatientScheduleAppointmentView();
                jDesktopPane.add(patientScheduleAppointmentView).setVisible(true);
            }
        });

        //PatientOperationsView listener
        jMenuItemSpecialistOperations.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SpecialistOperations specialistOperations = new SpecialistOperations();
                jDesktopPane.add(specialistOperations).setVisible(true);
            }
        });

        jMenuItemPatientOperations.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PatientOperationsView patientOperationsView = new PatientOperationsView();
                jDesktopPane.add(patientOperationsView).setVisible(true);
            }
        });

        jMenuItemAttend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                PatientAttedanceView patientAttedanceView = new PatientAttedanceView();
                jDesktopPane.add(patientAttedanceView).setVisible(true);
            }
        });

        jMenuItemReports.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                AdminReportsView adminReportsView = new AdminReportsView();
                jDesktopPane.add(adminReportsView).setVisible(true);
            }
        });

        jMenuItemAppointments.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SpecialistAppointmentsView specialistAppointmentsView = new SpecialistAppointmentsView();
                jDesktopPane.add(specialistAppointmentsView).setVisible(true);
            }
        });

    }

}
