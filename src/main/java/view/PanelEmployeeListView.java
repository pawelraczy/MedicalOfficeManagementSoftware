package view;

import helper.GBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This is a view for view.PanelEmployeeListView (main view: "view.MenuAndMainView")
 */

public class PanelEmployeeListView extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addEmployee;
    private JButton editEmployee;
    private JButton showEmployeeCard;
    private JButton removeEmployee;
    private JTable employeeListTable;

    public PanelEmployeeListView() {
        //JPanel settings
        setBorder(BorderFactory.createTitledBorder("domain.Employee List"));
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        //Search: JLabel and JTextField
        JLabel searchLabel = new JLabel("Enter one of: First Name/Last Name/Personal identity number");
        searchField = new JTextField();

        //Additional JButtons
        searchButton = new JButton("Search");
        addEmployee = new JButton("Add new employee");
        editEmployee = new JButton("Edit employee");
        showEmployeeCard = new JButton("Show employee card");
        removeEmployee = new JButton("Delete employee");

        //Creating JScrollPane with JTable
        String col[] = {"ID", "Last name", "First name", "Personal identity number", "Job"};
        DefaultTableModel model = new DefaultTableModel(col, 0) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeListTable = new JTable(model);
        //Hiding ID column (first column)
        employeeListTable.removeColumn(employeeListTable.getColumnModel().getColumn(0));
        JScrollPane scrollTable = new JScrollPane(employeeListTable);

        //Adding components to JPanel on the left side
        GridLayout leftPanelLayout = new GridLayout(9, 1);
        leftPanelLayout.setVgap(5);
        JPanel leftPanel = new JPanel(leftPanelLayout);
        leftPanel.add(searchLabel);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        leftPanel.add(new JLabel());
        leftPanel.add(addEmployee);
        leftPanel.add(editEmployee);
        leftPanel.add(showEmployeeCard);
        leftPanel.add(removeEmployee);

        //Adding components to main JPanel
        add(leftPanel, new GBC(0, 0, 1, 1).setInsets(1).setFill(GBC.HORIZONTAL));
        add(scrollTable, new GBC(1, 0, 1, 2).setWeight(100, 100).setInsets(1).setFill(GBC.BOTH));
    }

    //AddActionListener methods
    public void addActionListenerToSearchButton(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addActionListenerToAddEmployee(ActionListener listener) {
        addEmployee.addActionListener(listener);
    }

    public void addActionListenerToEditEmployeeButton(ActionListener listener) {
        editEmployee.addActionListener(listener);
    }

    public void addActionListenerToShowEmployeeCardButton(ActionListener listener) {
        showEmployeeCard.addActionListener(listener);
    }

    public void addActionListenerToRemoveEmployeeButton(ActionListener listener) {
        removeEmployee.addActionListener(listener);
    }

    public String getSearchValue() {
        return searchField.getText();
    }

    public JTable getTable() {
        return employeeListTable;
    }
}