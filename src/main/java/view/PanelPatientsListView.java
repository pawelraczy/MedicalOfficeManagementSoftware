package view;

import helper.GBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This is a view for view.PanelPatientsListView (main view: "view.MenuAndMainView")
 */

public class PanelPatientsListView extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JButton showAppointments;
    private JButton addAppointment;
    private JButton addPatient;
    private JButton editPatient;
    private JButton showPatientCard;
    private JButton registerVisit;
    private JButton visitsHistory;
    private JButton addToBlackList;
    private JButton removePatient;
    private JTable patientsListTable;

    public PanelPatientsListView() {
        //JPanel settings
        setBorder(BorderFactory.createTitledBorder("domain.Employee List"));
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        //Search: JLabel and JTextField
        JLabel searchLabel = new JLabel("Enter one of: First Name/Last Name/Personal identity number");
        searchField = new JTextField();

        //Additional JButtons
        searchButton = new JButton("Search");
        showAppointments = new JButton("Show patient appointments");
        addAppointment = new JButton("Add a new appointment");
        addPatient = new JButton("Add new patient");
        editPatient = new JButton("Edit patient");
        showPatientCard = new JButton("Show patient card");
        registerVisit = new JButton("Create visit report");
        visitsHistory = new JButton("Show visits history");
        addToBlackList = new JButton("Add patient to the black list");
        removePatient = new JButton("Delete patient");

        //Creating JScrollPane with JTable
        String col[] = {"ID", "Last name", "First name", "Personal identity number", "Date of birth"};
        DefaultTableModel model = new DefaultTableModel(col, 0) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientsListTable = new JTable(model);
        //Hiding ID column (first column)
        patientsListTable.removeColumn(patientsListTable.getColumnModel().getColumn(0));
        JScrollPane scrollTable = new JScrollPane(patientsListTable);

        //Adding components to JPanel on the left side
        GridLayout leftPanelLayout = new GridLayout(18, 1);
        leftPanelLayout.setVgap(5);
        JPanel leftPanel = new JPanel(leftPanelLayout);
        leftPanel.add(searchLabel);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        leftPanel.add(new JLabel());
        leftPanel.add(showAppointments);
        leftPanel.add(addAppointment);
        leftPanel.add(new JLabel());
        leftPanel.add(visitsHistory);
        leftPanel.add(new JLabel());
        leftPanel.add(showPatientCard);
        leftPanel.add(new JLabel());
        leftPanel.add(addPatient);
        leftPanel.add(editPatient);
        leftPanel.add(removePatient);
        leftPanel.add(addToBlackList);
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel("For doctors only:"));
        leftPanel.add(registerVisit);

        //Adding components to main JPanel
        add(leftPanel, new GBC(0, 0, 1, 1).setInsets(1).setFill(GBC.HORIZONTAL));
        add(scrollTable, new GBC(1, 0, 1, 2).setWeight(100, 100).setInsets(1).setFill(GBC.BOTH));
    }

    //AddActionListener methods
    public void addActionListenerToSearchButton(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addActionListenerToShowAppointments(ActionListener listener) {
        showAppointments.addActionListener(listener);
    }

    public void addActionListenerToAddAppointment(ActionListener listener) {
        addAppointment.addActionListener(listener);
    }

    public void addActionListenerToAddPatient(ActionListener listener) {
        addPatient.addActionListener(listener);
    }

    public void addActionListenerToEditPatientButton(ActionListener listener) {
        editPatient.addActionListener(listener);
    }

    public void addActionListenerToShowPatientCardButton(ActionListener listener) {
        showPatientCard.addActionListener(listener);
    }

    public void addActionListenerToRegisterVisitButton(ActionListener listener) {
        registerVisit.addActionListener(listener);
    }

    public void addActionListenerToVisitsHistoryButton(ActionListener listener) {
        visitsHistory.addActionListener(listener);
    }

    public void addActionListenerToAddToBlackListButton(ActionListener listener) {
        addToBlackList.addActionListener(listener);
    }

    public void addActionListenerToRemovePatientButton(ActionListener listener) {
        removePatient.addActionListener(listener);
    }

    public String getSearchValue() {
        return searchField.getText();
    }

    public JTable getTable() {
        return patientsListTable;
    }
}