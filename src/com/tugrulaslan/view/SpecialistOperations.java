package com.tugrulaslan.view;

import com.tugrulaslan.dao.HospitalDAO;
import com.tugrulaslan.dao.HospitalDAOImpl;
import com.tugrulaslan.util.DatabaseHelper;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SpecialistOperations extends JInternalFrame {

    private JPanel westPanel;
    private JPanel eastPanel;

    private HospitalDAO hospitalDAO;
    private DatabaseHelper db;

    private JLabel lblSpeciality;
    private JLabel lblListSpecialits;
    private TextField txtSpeciality;
    private Button btnAddSpecialist;
    private JList listSpecialists;

    public SpecialistOperations() {
        super("Specialist Operations", true, true, true, true);
        setSize(290, 250);
        setLocation(15, 15);
        setLayout(new BorderLayout());

        //Create Panels
        westPanel = new JPanel(new GridLayout(3, 2));
        eastPanel = new JPanel(new GridLayout(1, 2, 1, 1));
        hospitalDAO = new HospitalDAOImpl();
        db = new DatabaseHelper();

        //Create components for the north panel
        lblSpeciality = new JLabel("Specialist");
        txtSpeciality = new TextField();
        btnAddSpecialist = new Button("Add");
        listSpecialists = new JList(db.getallSpecialists().toArray());
        lblListSpecialits = new JLabel("List of specialists");

        //Add components into the panels
        westPanel.add(lblSpeciality);
        westPanel.add(txtSpeciality);
        westPanel.add(new JLabel());
        westPanel.add(btnAddSpecialist);

        eastPanel.add(lblListSpecialits);
        eastPanel.add(new JScrollPane(listSpecialists));

        //Add panels to the Internal Frame
        add(westPanel, BorderLayout.NORTH);
        add(eastPanel, BorderLayout.CENTER);

        //add actionlistener
        btnAddSpecialist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String givenSpecialist = String.valueOf(txtSpeciality.getText());

                db = new DatabaseHelper();

                if (db.getallSpecialists().contains(givenSpecialist)) {
                    JOptionPane.showMessageDialog(westPanel, "The speciality " + givenSpecialist + " you've entered already exists. Please add a different speciality", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(westPanel, "The specialist " + givenSpecialist + " has been added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    db.getallSpecialists().add(givenSpecialist);
                    refreshList(db.getallSpecialists().toArray());
                }
            }
        });
    }

    private void refreshList(Object[] args) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < args.length; i++) {
            listModel.addElement(args[i]);
        }
        listSpecialists.setModel(listModel);
        txtSpeciality.setText("");
    }
}
