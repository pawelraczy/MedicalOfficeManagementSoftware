package controller;

import domain.Employee;
import model.MainModel;
import view.MenuAndMainView;
import view.PanelAddEmployeeView;
import view.PanelEmployeeListView;
import view.PanelPatientsScheduleView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * This is a controller for the view.PanelEmployeeListView (main controller: "controller.MenuController" must initiate it)
 */


public class PanelEmployeeListController {

    public PanelEmployeeListController(MenuAndMainView theView, MainModel theModel) {
        PanelEmployeeListView panelEmployeeList = theView.getPanelEmployeeList();
        PanelAddEmployeeView panelAddEmployeeView = theView.getPanelAddEmployeeView();

        //First load employee list to table
        List<Employee> employeeList = theModel.getEmployeeList();
        DefaultTableModel model = getTableModel(panelEmployeeList);
        reloadTable(employeeList, model);

        //Search button
        panelEmployeeList.addActionListenerToSearchButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<Employee> searchResults = theModel.getEmployeesByText(panelEmployeeList.getSearchValue());
                DefaultTableModel model = getTableModel(panelEmployeeList);
                reloadTable(searchResults, model);
                theView.setInfoLabel("INFO: New search results.");
            }
        });

        //Add employee button
        panelEmployeeList.addActionListenerToAddEmployee(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                theView.setInfoLabel("INFO: ");
                theView.setSwitchingPanel(theView.getPanelAddEmployeeView());

                //Disable menu
                theView.setMainMenuVisibility(false);

                //Get new id number for employee
                panelAddEmployeeView.setIdNumber(theModel.getId());
            }
        });

        //Edit button
        panelEmployeeList.addActionListenerToEditEmployeeButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JTable table = panelEmployeeList.getTable();
                int selectedRow = table.getSelectedRow();

                //Check if any row is selected
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: Employee must be choosen before pressing \"Edit selected employee\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    //Check if employee with selected id exists on the employeeList
                    if (theModel.getEmployeeById(idFromTable) != null) {
                        Employee employee = theModel.getEmployeeById(idFromTable);
                        loadValuesToAddEmployeePanel(employee, theView);

                        //Disable menu
                        theView.setMainMenuVisibility(false);
                        theView.getPanelAddEmployeeView().setClearFieldsButtonVisibility(false);

                        //Set info label
                        theView.setInfoLabel("INFO: Editing employee!");

                        //Set id number on panel as selected id number from table
                        theView.getPanelAddEmployeeView().setIdNumber(idFromTable);

                        //Switch Panel
                        theView.setSwitchingPanel(theView.getPanelAddEmployeeView());
                    }
                }
            }
        });

        //Employee card button
        panelEmployeeList.addActionListenerToShowEmployeeCardButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JTable table = panelEmployeeList.getTable();
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: Employee must be choosen before pressing \"Show employee card\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);

                    //Check if employee with selected id exists on the employeeList
                    if (theModel.getEmployeeById(idFromTable) != null) {
                        Employee employee = theModel.getEmployeeById(idFromTable);
                        createEmployeeCardDialog(employee);
                    }
                }
            }
        });

        //Remove employee button
        panelEmployeeList.addActionListenerToRemoveEmployeeButton(new ActionListener() {
            DefaultTableModel model = getTableModel(panelEmployeeList);

            @Override
            public void actionPerformed(ActionEvent event) {
                JTable table = panelEmployeeList.getTable();
                int selectedRow = table.getSelectedRow();

                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: Employee must be choosen before pressing \"Delete employee\" button!");
                else {
                    int confirm = openDialogAndAskForConfirmation(theView);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int idFromTable = getIdFromSelectedRow(table, selectedRow);

                        //Remove selected employee if it exists on the employeeList
                        if (theModel.removeEmployee(idFromTable)) {
                            theView.setInfoLabel("INFO: Employee has been delated!");
                            model.removeRow(selectedRow);
                            theModel.sortEmployeeList();
                            theModel.saveEmployeeList();
                            PanelPatientsScheduleView panelPatientsScheduleView = theView.getPanelPatientsScheduleView();
                            panelPatientsScheduleView.setDoctorsList(theModel.getDoctorsList());
                        } else theView.setInfoLabel("INFO: Employee remove operation failed!");
                    } else theView.setInfoLabel("INFO:");
                }
            }
        });
    }

    private void createEmployeeCardDialog(Employee employee) {
        EmployeeCardDialog employeeDialog = new EmployeeCardDialog();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        employeeDialog.addRowToTable(new String[]{"First Name", employee.getFirstName()});
        employeeDialog.addRowToTable(new String[]{"Last Name", employee.getLastName()});
        employeeDialog.addRowToTable(new String[]{"Gender", employee.getGender()});
        employeeDialog.addRowToTable(new String[]{"Date of birth", formatter.format(employee.getDateOfBirth().getTime())});
        employeeDialog.addRowToTable(new String[]{"City", employee.getCity()});
        if (employee.getPostCode().length() > 5)
            employeeDialog.addRowToTable(new String[]{"Post code", employee.getPostCode()});
        else
            employeeDialog.addRowToTable(new String[]{"Post code", ""});
        employeeDialog.addRowToTable(new String[]{"Street", employee.getStreet()});
        employeeDialog.addRowToTable(new String[]{"House apartment number", employee.getHouseApartmentNumber()});
        if (employee.getPhoneNumber().length() > 6)
            employeeDialog.addRowToTable(new String[]{"Phone number", employee.getPhoneNumber()});
        else
            employeeDialog.addRowToTable(new String[]{"Phone number", ""});
        employeeDialog.addRowToTable(new String[]{"E-mail", employee.getEMail()});
        employeeDialog.addRowToTable(new String[]{"Personal identity number", employee.getPersonalIdentityNumber()});
        employeeDialog.addRowToTable(new String[]{"Bank account number", employee.getBankAccountNumber()});
        employeeDialog.addRowToTable(new String[]{"Hire date", formatter.format(employee.getHireDate().getTime())});
        employeeDialog.addRowToTable(new String[]{"Job", employee.getJob()});
        employeeDialog.setVisible(true);
    }

    private int openDialogAndAskForConfirmation(MenuAndMainView theView) {
        return JOptionPane.showConfirmDialog(
                theView, "Are you sure that you want to permanently delete the selected employee?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }

    private void loadValuesToAddEmployeePanel(Employee employee, MenuAndMainView theView) {
        theView.getPanelAddEmployeeView().setFirstName(employee.getFirstName());
        theView.getPanelAddEmployeeView().setLastName(employee.getLastName());
        theView.getPanelAddEmployeeView().setGender(employee.getGender());

        theView.getPanelAddEmployeeView().setDateOfBirth(
                employee.getDateOfBirth().get(Calendar.YEAR),
                employee.getDateOfBirth().get(Calendar.MONTH),
                employee.getDateOfBirth().get(Calendar.DAY_OF_MONTH)
        );
        theView.getPanelAddEmployeeView().setDateModelSelected(true);

        theView.getPanelAddEmployeeView().setCity(employee.getCity());
        if (employee.getPostCode().length() == 6) {
            theView.getPanelAddEmployeeView().setPostCode1(employee.getPostCode().substring(0, 2));
            theView.getPanelAddEmployeeView().setPostCode2(employee.getPostCode().substring(3, 6));
        }
        {
            theView.getPanelAddEmployeeView().setPostCode1("");
            theView.getPanelAddEmployeeView().setPostCode2("");
        }

        theView.getPanelAddEmployeeView().setStreet(employee.getStreet());
        theView.getPanelAddEmployeeView().setApartmentNumber(employee.getHouseApartmentNumber());

        if (employee.getPhoneNumber().length() > 8) {
            theView.getPanelAddEmployeeView().setPhoneNumber1(employee.getPhoneNumber().substring(2, 4));
            theView.getPanelAddEmployeeView().setPhoneNumber2(employee.getPhoneNumber().substring(6));
        } else {
            theView.getPanelAddEmployeeView().setPhoneNumber1("");
            theView.getPanelAddEmployeeView().setPhoneNumber2("");
        }

        theView.getPanelAddEmployeeView().setEMail(employee.getEMail());
        theView.getPanelAddEmployeeView().setPersonalIdentityNumber(employee.getPersonalIdentityNumber());
        theView.getPanelAddEmployeeView().setBankAccountNumber(employee.getBankAccountNumber());

        theView.getPanelAddEmployeeView().setHireDate(
                employee.getHireDate().get(Calendar.YEAR),
                employee.getHireDate().get(Calendar.MONTH),
                employee.getHireDate().get(Calendar.DAY_OF_MONTH)
        );
        theView.getPanelAddEmployeeView().setHireDateModelSelected(true);

        theView.getPanelAddEmployeeView().setJob(employee.getJob());
    }

    private int getIdFromSelectedRow(JTable table, int selectedRow) {
        return Integer.parseInt((String) table.getModel().getValueAt(selectedRow, 0));
    }

    private boolean isAnyRowSelected(int selectedRow) {
        return selectedRow != -1;
    }

    private void reloadTable(List<Employee> employeeList, DefaultTableModel model) {
        model.setRowCount(0);
        for (Employee e : employeeList) {
            model.addRow(new String[]
                    {
                            Integer.toString(e.getId()),
                            e.getLastName(),
                            e.getFirstName(),
                            e.getPersonalIdentityNumber(),
                            e.getJob()
                    });
        }
    }

    private DefaultTableModel getTableModel(PanelEmployeeListView panelEmployeeList) {
        JTable table = panelEmployeeList.getTable();
        return (DefaultTableModel) table.getModel();
    }

    public class EmployeeCardDialog extends JDialog {
        DefaultTableModel employeeModel;

        public EmployeeCardDialog() {
            super((Frame) null, "domain.Employee card", true);
            //Creating JTable
            String col[] = {"Data", "Value"};
            employeeModel = new DefaultTableModel(col, 0) {
                //Turning off editable possibility for JTable
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable table = new JTable(employeeModel);

            //Clear JTable
            employeeModel.setRowCount(0);

            //Add JTable to JDialog
            JScrollPane scrollTable = new JScrollPane(table);
            scrollTable.setBorder(null);
            scrollTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            getContentPane().add(scrollTable, BorderLayout.CENTER);
            setSize(500, 300);
        }

        public void addRowToTable(String[] row) {
            employeeModel.addRow(row);
        }

    }
}