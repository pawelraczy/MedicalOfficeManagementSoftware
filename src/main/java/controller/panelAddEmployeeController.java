package controller;

import domain.Employee;
import model.MainModel;
import view.MenuAndMainView;
import view.PanelAddEmployeeView;
import view.PanelEmployeeListView;
import view.PanelPatientsScheduleView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This is a controller for the view.PanelAddEmployeeView (main controller: "controller.MenuController" must initiate it)
 */


public class PanelAddEmployeeController {
    public PanelAddEmployeeController(MenuAndMainView theView, MainModel theModel) {
        PanelAddEmployeeView panelAddEmployeeView = theView.getPanelAddEmployeeView();
        PanelEmployeeListView panelEmployeeListView = theView.getPanelEmployeeList();

        //Confirm button
        panelAddEmployeeView.addActionListenerToConfirmButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (isAllFieldsCorrect(panelAddEmployeeView, theView)) {
                    Employee employee = createNewEmployeeFromView(panelAddEmployeeView);
                    if (theModel.isEmployeeExists(employee)) {
                        //Ask about replacing employee if employee exists
                        int confirm = openDialogAndAskForConfirmation(theView);
                        if (confirm == JOptionPane.YES_OPTION) {
                            //Edit employee, sort employeeList and save it to file
                            theModel.editEmployee(employee);
                            theModel.sortEmployeeList();
                            theModel.saveEmployeeList();
                            PanelPatientsScheduleView panelPatientsScheduleView = theView.getPanelPatientsScheduleView();
                            panelPatientsScheduleView.setDoctorsList(theModel.getDoctorsList());

                            DefaultTableModel model = getTableModel(panelEmployeeListView);
                            reloadTable(model, theModel);

                            //EnableMenu (the end of editing)
                            theView.setMainMenuVisibility(true);
                            panelAddEmployeeView.setClearFieldsButtonVisibility(true);
                            theView.setInfoLabel("INFO: domain.Employee has been edited");
                            theView.setSwitchingPanel(panelEmployeeListView);

                        } else
                            theView.setInfoLabel("INFO: domain.Employee list already containts this employee. Press \"Cancel\" button and check \"domain.Employee list\" tab!");
                    } else {
                        theModel.addEmployeeToList(employee);
                        theModel.saveEmployeeList();
                        theModel.incrementAndSaveId();
                        PanelPatientsScheduleView panelPatientsScheduleView = theView.getPanelPatientsScheduleView();
                        panelPatientsScheduleView.setDoctorsList(theModel.getDoctorsList());

                        DefaultTableModel model = getTableModel(panelEmployeeListView);
                        reloadTable(model, theModel);

                        //Clearing fields
                        clearFieldsInPanel(panelAddEmployeeView);
                        theView.setInfoLabel("INFO: ");

                        //EnableMenu (the end of adding employee)
                        theView.setMainMenuVisibility(true);
                        panelAddEmployeeView.setClearFieldsButtonVisibility(true);

                        //Clear ID number in panel
                        panelAddEmployeeView.setIdNumber(0);

                        //Return to EmployeeList Panel
                        theView.setInfoLabel("INFO: The employee has been added.");
                        theView.setSwitchingPanel(panelEmployeeListView);
                    }
                }
            }
        });

        //Cancel button
        panelAddEmployeeView.addActionListenerToCancelButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //Clearing fields
                clearFieldsInPanel(panelAddEmployeeView);
                theView.setInfoLabel("INFO: ");

                //EnableMenu
                theView.setMainMenuVisibility(true);
                panelAddEmployeeView.setClearFieldsButtonVisibility(true);

                //Clear ID number in panel
                panelAddEmployeeView.setIdNumber(0);

                //Switch Panel
                theView.setSwitchingPanel(panelEmployeeListView);
            }
        });

        //Clear button
        panelAddEmployeeView.addActionListenerToClearButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //Clearing fields
                clearFieldsInPanel(panelAddEmployeeView);
                theView.setInfoLabel("INFO: Fields cleared!");
            }
        });
    }

    private int openDialogAndAskForConfirmation(MenuAndMainView theView) {
        return JOptionPane.showConfirmDialog(
                theView, "This employee already exists. You can  press \"Cancel\" button and check it in \"domain.Employee list\" tab!\n"
                        + "Would you like to replace the existing employee with a new one though?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }

    public boolean isAllFieldsCorrect(PanelAddEmployeeView panelAddEmployeeView, MenuAndMainView theView) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFromPINumber = null;
        if (panelAddEmployeeView.getDateOfBirth() != null)
            dateFromPINumber = formatter.format(panelAddEmployeeView.getDateOfBirth().getTime());

        if (panelAddEmployeeView.getFirstName().matches("[0-9]+")) {
            theView.setInfoLabel("INFO: First name contains invalid characters!");
            return false;
        } else if (panelAddEmployeeView.getFirstName().equals("")) {
            theView.setInfoLabel("INFO: First name cannot be empty!");
            return false;
        } else if (panelAddEmployeeView.getLastName().matches("[0-9]+")) {
            theView.setInfoLabel("INFO: Last name contains invalid characters!");
            return false;
        } else if (panelAddEmployeeView.getLastName().equals("")) {
            theView.setInfoLabel("INFO: Last name cannot be empty!");
            return false;
        } else if (panelAddEmployeeView.getGender() == null) {
            theView.setInfoLabel("INFO: Gender must be chosen!");
            return false;
        } else if (panelAddEmployeeView.getDateOfBirth() == null) {
            theView.setInfoLabel("INFO: Date of birth must be chosen!");
            return false;
        } else if (panelAddEmployeeView.getHireDate() == null) {
            theView.setInfoLabel("INFO: Hire date must be chosen!");
            return false;
        } else if (panelAddEmployeeView.getJob() == null) {
            theView.setInfoLabel("INFO: Job must be chosen!");
            return false;
        } else if (panelAddEmployeeView.getCity().matches("[0-9]+")) {
            theView.setInfoLabel("INFO: City contains invalid characters!");
            return false;
        } else if (panelAddEmployeeView.getCity().equals("")) {
            theView.setInfoLabel("INFO: City cannot be empty!");
            return false;
        } else if ((!(panelAddEmployeeView.getPostCode1().equals("")) || !(panelAddEmployeeView.getPostCode2().equals(""))) && !(panelAddEmployeeView.getPostCode1().matches("[0-9]{2}"))) {
            theView.setInfoLabel("INFO: First part of post code must contain exactly 2 digits!");
            return false;
        } else if ((!(panelAddEmployeeView.getPostCode1().equals("")) || !(panelAddEmployeeView.getPostCode2().equals(""))) && !(panelAddEmployeeView.getPostCode2().matches("[0-9]{3}"))) {
            theView.setInfoLabel("INFO: Second part of post code must contain exactly 3 digits!");
            return false;
        } else if (!(panelAddEmployeeView.getApartmentNumber().equals("")) && (!(panelAddEmployeeView.getApartmentNumber().matches(".*[0-9].*")))) {
            theView.setInfoLabel("INFO: Apartment number must contain minimum 1 digit!");
            return false;
        } else if ((!(panelAddEmployeeView.getPhoneNumber1().equals("")) || !(panelAddEmployeeView.getPhoneNumber2().equals(""))) && !(panelAddEmployeeView.getPhoneNumber1().matches("[0-9]{2}"))) {
            theView.setInfoLabel("INFO: First part of phone number must contain exactly 2 digits!");
            return false;
        } else if ((!(panelAddEmployeeView.getPhoneNumber1().equals("")) || !(panelAddEmployeeView.getPhoneNumber2().equals(""))) && !(panelAddEmployeeView.getPhoneNumber2().matches("[0-9]{7,9}"))) {
            theView.setInfoLabel("INFO: Second part of phone number must contain between 7 and 9 digits!");
            return false;
        } else if (!(panelAddEmployeeView.getEMail().equals("")) && !(panelAddEmployeeView.getEMail()
                .matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))) {
            theView.setInfoLabel("INFO: Incorrect E-mail address!");
            return false;
        } else if (!(panelAddEmployeeView.getPersonalIdentityNumber().equals("")) && (!(panelAddEmployeeView.getPersonalIdentityNumber().matches("[0-9]{11}")))) {
            theView.setInfoLabel("INFO: Personal identity number must contain exactly 11 digit!");
            return false;
        } else if ((panelAddEmployeeView.getPersonalIdentityNumber().length() == 11) && (panelAddEmployeeView.getDateOfBirth() != null)) {
            if (!(panelAddEmployeeView.getPersonalIdentityNumber().substring(0, 2).equals(dateFromPINumber.substring(2, 4)))
                    || !(panelAddEmployeeView.getPersonalIdentityNumber().substring(2, 4).equals(dateFromPINumber.substring(5, 7)))
                    || !(panelAddEmployeeView.getPersonalIdentityNumber().substring(4, 6).equals(dateFromPINumber.substring(8, 10))))
                theView.setInfoLabel("INFO: First 6 digits in personal identity number must match date of birth!");
            return false;
        } else if (!(panelAddEmployeeView.getBankAccountNumber().equals("")) && (!(panelAddEmployeeView.getBankAccountNumber().matches("[0-9]{26}")))) {
            theView.setInfoLabel("INFO: Bank account number must contain exactly 26 digit!");
            return false;
        }
        return true;
    }

    private Employee createNewEmployeeFromView(PanelAddEmployeeView panelAddEmployeeView) {
        return new Employee(
                panelAddEmployeeView.getIdNumber(),
                panelAddEmployeeView.getFirstName(),
                panelAddEmployeeView.getLastName(),
                panelAddEmployeeView.getGender(),
                panelAddEmployeeView.getDateOfBirth(),
                panelAddEmployeeView.getCity(),
                panelAddEmployeeView.getPostCode1() + "-" + panelAddEmployeeView.getPostCode2(),
                panelAddEmployeeView.getStreet(),
                panelAddEmployeeView.getApartmentNumber(),
                "+(" + panelAddEmployeeView.getPhoneNumber1() + ") " + panelAddEmployeeView.getPhoneNumber2(),
                panelAddEmployeeView.getEMail(),
                panelAddEmployeeView.getPersonalIdentityNumber(),
                panelAddEmployeeView.getBankAccountNumber(),
                panelAddEmployeeView.getHireDate(),
                panelAddEmployeeView.getJob()
        );
    }

    private void reloadTable(DefaultTableModel model, MainModel theModel) {
        model.setRowCount(0);
        for (Employee e : theModel.getEmployeeList()) {
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

    private DefaultTableModel getTableModel(PanelEmployeeListView panelEmployeeListView) {
        JTable table = panelEmployeeListView.getTable();
        return (DefaultTableModel) table.getModel();
    }

    public void clearFieldsInPanel(PanelAddEmployeeView panel) {
        panel.setFirstName("");
        panel.setLastName("");
        panel.setGender(null);
        panel.setDateOfBirth(1980, 0, 1);
        panel.setDateModelSelected(false);
        panel.setCity("");
        panel.setPostCode1("");
        panel.setPostCode2("");
        panel.setStreet("");
        panel.setApartmentNumber("");
        panel.setPhoneNumber1("");
        panel.setPhoneNumber2("");
        panel.setEMail("");
        panel.setPersonalIdentityNumber("");
        Calendar today = Calendar.getInstance();
        panel.setHireDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        panel.setJob(null);
    }
}