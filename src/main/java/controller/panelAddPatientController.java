package controller;

import domain.Patient;
import model.MainModel;
import view.MenuAndMainView;
import view.PanelAddPatientView;
import view.PanelPatientsListView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;


/**
 * This is a controller for the view.PanelAddPatientView (main controller: "controller.MenuController" must initiate it)
 */


public class PanelAddPatientController {
    public PanelAddPatientController(MenuAndMainView theView, MainModel theModel) {
        PanelAddPatientView panelAddPatientView = theView.getPanelAddPatientView();
        PanelPatientsListView panelPatientsListView = theView.getPanelPatientsListView();

        //Confirm button
        panelAddPatientView.addActionListenerToConfirmButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (isAllFieldsCorrect(panelAddPatientView, theView)) {
                    Patient patient = createNewPatientFromView(panelAddPatientView);
                    if (theModel.isPatientExists(patient)) {
                        //Ask about replacing patient if patient exists
                        int confirm = openDialogAndAskForConfirmation(theView);
                        if (confirm == JOptionPane.YES_OPTION) {
                            theModel.editPatient(patient);
                            theModel.savePatientList();

                            DefaultTableModel model = getTableModel(panelPatientsListView);
                            reloadTable(model, theModel);

                            //EnableMenu
                            theView.setMainMenuVisibility(true);
                            panelAddPatientView.setClearFieldsButtonVisibility(true);
                            theView.setInfoLabel("INFO: domain.Patient has been edited");
                            theView.setSwitchingPanel(panelPatientsListView);

                        } else
                            theView.setInfoLabel("INFO: Patients list already containts this patient. Press \"Cancel\" button and check \"Patients list\" tab!");
                    } else {
                        theModel.addPatient(patient);
                        theModel.savePatientList();
                        theModel.incrementAndSaveId();

                        DefaultTableModel model = getTableModel(panelPatientsListView);
                        reloadTable(model, theModel);

                        //Clearing fields
                        clearFieldsInPanel(panelAddPatientView);
                        theView.setInfoLabel("INFO: Fields cleared!");

                        //Clear ID number in panel
                        panelAddPatientView.setIdNumber(0);

                        //EnableMenu (the end of adding patient)
                        theView.setMainMenuVisibility(true);
                        panelAddPatientView.setClearFieldsButtonVisibility(true);

                        //Return to PatientsList Panel
                        theView.setInfoLabel("INFO: The patient has been added.");
                        theView.setSwitchingPanel(panelPatientsListView);
                    }
                }
            }
        });

        //Cancel button
        panelAddPatientView.addActionListenerToCancelButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                clearFieldsInPanel(panelAddPatientView);
                theView.setInfoLabel("INFO: ");
                theView.setMainMenuVisibility(true);
                panelAddPatientView.setClearFieldsButtonVisibility(true);
                panelAddPatientView.setIdNumber(0);
                theView.setSwitchingPanel(panelPatientsListView);
            }
        });

        //Clear button
        panelAddPatientView.addActionListenerToClearButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                clearFieldsInPanel(panelAddPatientView);
                theView.setInfoLabel("INFO: Fields cleared!");
            }
        });
    }

    public boolean isAllFieldsCorrect(PanelAddPatientView panelAddPatientView, MenuAndMainView theView) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFromPINumber = null;
        if (panelAddPatientView.getDateOfBirth() != null)
            dateFromPINumber = formatter.format(panelAddPatientView.getDateOfBirth().getTime());

        if (panelAddPatientView.getFirstName().matches("[0-9]+")) {
            theView.setInfoLabel("INFO: First name contains invalid characters!");
            return false;
        } else if (panelAddPatientView.getFirstName().equals("")) {
            theView.setInfoLabel("INFO: First name cannot be empty!");
            return false;
        } else if (panelAddPatientView.getLastName().matches("[0-9]+")) {
            theView.setInfoLabel("INFO: Last name contains invalid characters!");
            return false;
        } else if (panelAddPatientView.getLastName().equals("")) {
            theView.setInfoLabel("INFO: Last name cannot be empty!");
            return false;
        } else if (panelAddPatientView.getGender() == null) {
            theView.setInfoLabel("INFO: Gender must be chosen!");
            return false;
        } else if (panelAddPatientView.getDateOfBirth() == null) {
            theView.setInfoLabel("INFO: Date of birth must be chosen!");
            return false;
        } else if (panelAddPatientView.getCity().matches("[0-9]+")) {
            theView.setInfoLabel("INFO: City contains invalid characters!");
            return false;
        } else if (panelAddPatientView.getCity().equals("")) {
            theView.setInfoLabel("INFO: City cannot be empty!");
            return false;
        } else if ((!(panelAddPatientView.getPostCode1().equals("")) || !(panelAddPatientView.getPostCode2().equals(""))) && !(panelAddPatientView.getPostCode1().matches("[0-9]{2}"))) {
            theView.setInfoLabel("INFO: First part of post code must contain exactly 2 digits!");
            return false;
        } else if ((!(panelAddPatientView.getPostCode1().equals("")) || !(panelAddPatientView.getPostCode2().equals(""))) && !(panelAddPatientView.getPostCode2().matches("[0-9]{3}"))) {
            theView.setInfoLabel("INFO: Second part of post code must contain exactly 3 digits!");
            return false;
        } else if (!(panelAddPatientView.getApartmentNumber().equals("")) && (!(panelAddPatientView.getApartmentNumber().matches(".*[0-9].*")))) {
            theView.setInfoLabel("INFO: Apartment number must contain minimum 1 digit!");
            return false;
        } else if ((!(panelAddPatientView.getPhoneNumber1().equals("")) || !(panelAddPatientView.getPhoneNumber2().equals(""))) && !(panelAddPatientView.getPhoneNumber1().matches("[0-9]{2}"))) {
            theView.setInfoLabel("INFO: First part of phone number must contain exactly 2 digits!");
            return false;
        } else if ((!(panelAddPatientView.getPhoneNumber1().equals("")) || !(panelAddPatientView.getPhoneNumber2().equals(""))) && !(panelAddPatientView.getPhoneNumber2().matches("[0-9]{7,9}"))) {
            theView.setInfoLabel("INFO: Second part of phone number must contain between 7 and 9 digits!");
            return false;
        } else if (!(panelAddPatientView.getEMail().equals("")) && !(panelAddPatientView.getEMail()
                .matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))) {
            theView.setInfoLabel("INFO: Incorrect E-mail address!");
            return false;
        } else if (!(panelAddPatientView.getPersonalIdentityNumber().equals("")) && (!(panelAddPatientView.getPersonalIdentityNumber().matches("[0-9]{11}")))) {
            theView.setInfoLabel("INFO: Personal identity number must contain exactly 11 digit!");
            return false;
        } else if ((panelAddPatientView.getPersonalIdentityNumber().length() == 11) && (panelAddPatientView.getDateOfBirth() != null)) {
            if (!(panelAddPatientView.getPersonalIdentityNumber().substring(0, 2).equals(dateFromPINumber.substring(2, 4)))
                    || !(panelAddPatientView.getPersonalIdentityNumber().substring(2, 4).equals(dateFromPINumber.substring(5, 7)))
                    || !(panelAddPatientView.getPersonalIdentityNumber().substring(4, 6).equals(dateFromPINumber.substring(8, 10))))
                theView.setInfoLabel("INFO: First 6 digits in personal identity number must match date of birth!");
            return false;
        }
        return true;
    }

    private void reloadTable(DefaultTableModel model, MainModel theModel) {
        model.setRowCount(0);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Patient p : theModel.getPatientList()) {
            if (!(p.getBlacklistStatus())) {
                try {
                    model.addRow(new String[]
                            {
                                    Integer.toString(p.getId()),
                                    p.getLastName(),
                                    p.getFirstName(),
                                    p.getPersonalIdentityNumber(),
                                    formatter.format(p.getDateOfBirth().getTime())
                            });
                } catch (IllegalArgumentException e) {
                    model.addRow(new String[]
                            {
                                    Integer.toString(p.getId()),
                                    p.getLastName(),
                                    p.getFirstName(),
                                    p.getPersonalIdentityNumber(),
                                    ""
                            });
                }
            }
        }
    }

    private int openDialogAndAskForConfirmation(MenuAndMainView theView) {
        return JOptionPane.showConfirmDialog(
                theView, "This patient already exists. You can  press \"Cancel\" button and check it in \"Patients list\" or \"Blacklist\" tab!\n"
                        + "Would you like to replace the existing patient with a new one though?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }

    private Patient createNewPatientFromView(PanelAddPatientView panelAddPatientView) {
        return new Patient(
                panelAddPatientView.getIdNumber(),
                panelAddPatientView.getFirstName(),
                panelAddPatientView.getLastName(),
                panelAddPatientView.getGender(),
                panelAddPatientView.getDateOfBirth(),
                panelAddPatientView.getCity(),
                panelAddPatientView.getPostCode1() + "-" + panelAddPatientView.getPostCode2(),
                panelAddPatientView.getStreet(),
                panelAddPatientView.getApartmentNumber(),
                "+(" + panelAddPatientView.getPhoneNumber1() + ") " + panelAddPatientView.getPhoneNumber2(),
                panelAddPatientView.getEMail(),
                panelAddPatientView.getPersonalIdentityNumber(),
                panelAddPatientView.getAllergies(),
                panelAddPatientView.getMedicalHistoryArea()
        );
    }

    private DefaultTableModel getTableModel(PanelPatientsListView panelPatientsListView) {
        JTable table = panelPatientsListView.getTable();
        return (DefaultTableModel) table.getModel();
    }

    public void clearFieldsInPanel(PanelAddPatientView panel) {
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
        panel.setAllergies("");
        panel.setMedicalHistoryArea("");
    }
}