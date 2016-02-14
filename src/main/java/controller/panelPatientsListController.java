package controller;

import domain.Appointment;
import domain.Patient;
import domain.VisitReport;
import model.MainModel;
import org.apache.commons.lang3.StringUtils;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * This is a controller for the view.PanelPatientsListView (main controller: "controller.MenuController" must initiate it)
 */


public class PanelPatientsListController {

    public PanelPatientsListController(MenuAndMainView theView, MainModel theModel) {
        PanelPatientsListView panelPatientsListView = theView.getPanelPatientsListView();

        //First load patients list to table
        List<Patient> patientsList = theModel.getPatientList();
        DefaultTableModel model = (DefaultTableModel) panelPatientsListView.getTable().getModel();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Patient p : patientsList)
            //Check if patient is on the blacklist
            if (!(p.getBlacklistStatus())) {
                model.addRow(new String[]
                        {
                                Integer.toString(p.getId()),
                                p.getLastName(),
                                p.getFirstName(),
                                p.getPersonalIdentityNumber(),
                                formatter.format(p.getDateOfBirth().getTime())
                        });
            }

        //Search button
        panelPatientsListView.addActionListenerToSearchButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Patient> searchResults = theModel.getPatientsByText(panelPatientsListView.getSearchValue());

                //Load table using new searchResults
                DefaultTableModel model = (DefaultTableModel) panelPatientsListView.getTable().getModel();
                model.setRowCount(0);
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                for (Patient p : searchResults) {
                    //Check if patient is on the blacklist
                    if (!(p.getBlacklistStatus())) {
                        model.addRow(new String[]
                                {
                                        Integer.toString(p.getId()),
                                        p.getLastName(),
                                        p.getFirstName(),
                                        p.getPersonalIdentityNumber(),
                                        formatter.format(p.getDateOfBirth().getTime())
                                });
                    }
                }
                theView.setInfoLabel("INFO: New search results.");
            }
        });

        //Show appointments button
        panelPatientsListView.addActionListenerToShowAppointments(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Show patient appointments\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                    //Check if patient with selected id exists on the patientsList
                    if (theModel.getPatientById(idFromTable) != null) {
                        //Get patient using selected id
                        Patient patient = theModel.getPatientById(idFromTable);

                        //Create empty dialog
                        PatientAppointmentsDialogView patientAppointmentsDialogView = new PatientAppointmentsDialogView();

                        //Set patient name
                        patientAppointmentsDialogView.setPatientName("PATIENT: " + patient.getFirstName() + " " + patient.getLastName());

                        //Add rows
                        List<Appointment> appointmentslist = patient.getAppointmentsList();
                        for (Appointment a : appointmentslist) {
                            //Check the date (show only visits in the future)
                            int year = Integer.parseInt(a.getDate().substring(0, 4));
                            int month = Integer.parseInt(a.getDate().substring(5, 7));
                            int day = Integer.parseInt(a.getDate().substring(8, 10));
                            Calendar today = Calendar.getInstance();
                            if (!((year < today.get(Calendar.YEAR)) ||
                                    ((year >= today.get(Calendar.YEAR)) && (month < today.get(Calendar.MONTH))) ||
                                    ((year >= today.get(Calendar.YEAR)) && (month >= today.get(Calendar.MONTH)) &&
                                            (day < today.get(Calendar.DAY_OF_MONTH)))))
                                patientAppointmentsDialogView.addRowToTable(new String[]{a.getDate() + ", " + a.getTime(), a.getDoctor()});
                        }
                        patientAppointmentsDialogView.pack();
                        patientAppointmentsDialogView.setVisible(true);
                    }
                }
            }
        });

        //Add appointment button
        panelPatientsListView.addActionListenerToAddAppointment(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Add appointment\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                    //Check if patient with selected id exists on the patientsList
                    if (theModel.getPatientById(idFromTable) != null) {
                        //Get patient using selected id
                        Patient patient = theModel.getPatientById(idFromTable);

                        //Enter patients name to TextField in PatientsSchedulePanel
                        theView.getPanelPatientsScheduleView().setPatientField(patient.getLastName() + " " + patient.getFirstName());

                        //Set info label
                        theView.setInfoLabel("INFO: Adding an appointment.");

                        //Disable menu
                        theView.setMainMenuVisibility(false);

                        //Set id number on panel as selected id number from table
                        theView.getPanelPatientsScheduleView().setIdNumber(idFromTable);

                        //Switch Panel
                        theView.setSwitchingPanel(theView.getPanelPatientsScheduleView());
                    }
                }
            }
        });

        //Add patient button
        panelPatientsListView.addActionListenerToAddPatient(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Switch panel
                theView.setInfoLabel("INFO: ");
                theView.setSwitchingPanel(theView.getPanelAddPatientView());

                //Disable menu
                theView.setMainMenuVisibility(false);

                //Get new id number for patient
                theView.getPanelAddPatientView().setIdNumber(theModel.getId());
            }
        });

        //Edit button
        panelPatientsListView.addActionListenerToEditPatientButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Edit selected patient\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                    //Check if patient with selected id exists on the patientsList
                    if (theModel.getPatientById(idFromTable) != null) {
                        //Get patient using selected id
                        Patient patient = theModel.getPatientById(idFromTable);

                        //Load values to AddPatientPanel
                        theView.getPanelAddPatientView().setFirstName(patient.getFirstName());
                        theView.getPanelAddPatientView().setLastName(patient.getLastName());
                        theView.getPanelAddPatientView().setGender(patient.getGender());

                        theView.getPanelAddPatientView().setDateOfBirth(
                                patient.getDateOfBirth().get(Calendar.YEAR),
                                patient.getDateOfBirth().get(Calendar.MONTH),
                                patient.getDateOfBirth().get(Calendar.DAY_OF_MONTH)
                        );
                        theView.getPanelAddPatientView().setDateModelSelected(true);

                        theView.getPanelAddPatientView().setCity(patient.getCity());
                        if (patient.getPostCode().length() == 6) {
                            theView.getPanelAddPatientView().setPostCode1(patient.getPostCode().substring(0, 2));
                            theView.getPanelAddPatientView().setPostCode2(patient.getPostCode().substring(3, 6));
                        } else {
                            theView.getPanelAddPatientView().setPostCode1("");
                            theView.getPanelAddPatientView().setPostCode2("");
                        }

                        theView.getPanelAddPatientView().setStreet(patient.getStreet());
                        theView.getPanelAddPatientView().setApartmentNumber(patient.getHouseApartmentNumber());

                        if (patient.getPhoneNumber().length() > 8) {
                            theView.getPanelAddPatientView().setPhoneNumber1(patient.getPhoneNumber().substring(2, 4));
                            theView.getPanelAddPatientView().setPhoneNumber2(patient.getPhoneNumber().substring(6));
                        } else {
                            theView.getPanelAddPatientView().setPhoneNumber1("");
                            theView.getPanelAddPatientView().setPhoneNumber2("");
                        }

                        theView.getPanelAddPatientView().setEMail(patient.getEMail());
                        theView.getPanelAddPatientView().setPersonalIdentityNumber(patient.getPersonalIdentityNumber());
                        theView.getPanelAddPatientView().setAllergies(patient.getAllergies());
                        theView.getPanelAddPatientView().setMedicalHistoryArea(patient.getMedicalHistory());

                        //Disable menu
                        theView.setMainMenuVisibility(false);
                        theView.getPanelAddPatientView().setClearFieldsButtonVisibility(false);

                        //Set info label
                        theView.setInfoLabel("INFO: Editing patient!");

                        //Set id number on panel as selected id number from table
                        theView.getPanelAddPatientView().setIdNumber(idFromTable);

                        //Switch Panel
                        theView.setSwitchingPanel(theView.getPanelAddPatientView());
                    }
                }
            }
        });

        //domain.Patient card button
        panelPatientsListView.addActionListenerToShowPatientCardButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Show patient card\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                    //Check if patient with selected id exists on the patientsList
                    if (theModel.getPatientById(idFromTable) != null) {
                        //Get patient using selected id
                        Patient patient = theModel.getPatientById(idFromTable);

                        //Create empty dialog
                        PatientCardDialogView patientDialog = new PatientCardDialogView();

                        //Add rows
                        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                        patientDialog.addRowToTable(new String[]{"First Name", patient.getFirstName()});
                        patientDialog.addRowToTable(new String[]{"Last Name", patient.getLastName()});
                        patientDialog.addRowToTable(new String[]{"Gender", patient.getGender()});
                        patientDialog.addRowToTable(new String[]{"Date of birth", formatter.format(patient.getDateOfBirth().getTime())});
                        patientDialog.addRowToTable(new String[]{"City", patient.getCity()});
                        if (patient.getPostCode().length() > 5)
                            patientDialog.addRowToTable(new String[]{"Post code", patient.getPostCode()});
                        else
                            patientDialog.addRowToTable(new String[]{"Post code", ""});
                        patientDialog.addRowToTable(new String[]{"Street", patient.getStreet()});
                        patientDialog.addRowToTable(new String[]{"House apartment number", patient.getHouseApartmentNumber()});
                        if (patient.getPhoneNumber().length() > 6)
                            patientDialog.addRowToTable(new String[]{"Phone number", patient.getPhoneNumber()});
                        else
                            patientDialog.addRowToTable(new String[]{"Phone number", ""});
                        patientDialog.addRowToTable(new String[]{"E-mail", patient.getEMail()});
                        patientDialog.addRowToTable(new String[]{"Personal identity number", patient.getPersonalIdentityNumber()});

                        //Add allergies row
                        String[] allergiesTokens = patient.getAllergies().split("\n");        //split allergies string to separate rows and put it in table
                        String allergiesAfterSplit = "<html>";    //add html tag at start
                        for (String x : allergiesTokens)        //add <br> tags for separate rows
                        {
                            allergiesAfterSplit += (x + "<br>");
                        }
                        allergiesAfterSplit += "</html>";        //add html tag at the end
                        patientDialog.addRowToTable(new String[]{"Allergies", allergiesAfterSplit});
                        int brCountA = StringUtils.countMatches(allergiesAfterSplit, "<br>");    //how many <br> matches in allergiesAfterSplit string
                        patientDialog.setRowHeight(11, brCountA * 16);        //set row height (depends on <br> matches)

                        //Add medical history row
                        String[] medicalHistoryTokens = patient.getMedicalHistory().split("\n"); //split medical history string to separate rows, put it in table
                        String medicalHistoryAfterSplit = "<html>";    //add html tag at start
                        for (String x : medicalHistoryTokens)        //add <br> tags for separate rows
                        {
                            medicalHistoryAfterSplit += (x + "<br>");
                        }
                        medicalHistoryAfterSplit += "</html>";        //add html tag at the end
                        patientDialog.addRowToTable(new String[]{"Medical history", medicalHistoryAfterSplit});
                        int brCountM = StringUtils.countMatches(medicalHistoryAfterSplit, "<br>");    //how many <br> matches in allergiesAfterSplit string
                        patientDialog.setRowHeight(12, brCountM * 16);        //set row height (depends on <br> matches)
                        patientDialog.pack();
                        patientDialog.setVisible(true);
                    }
                }
            }
        });

        //Register visit button
        panelPatientsListView.addActionListenerToRegisterVisitButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Register visit\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                    //Check if patient with selected id exists on the patientsList
                    if (theModel.getPatientById(idFromTable) != null) {
                        //Get patient using selected id
                        Patient patient = theModel.getPatientById(idFromTable);

                        //Create empty dialog
                        VisitDialogView visitReportDialog = new VisitDialogView();

                        //Add ActionListeners to dialog buttons
                        visitReportDialog.addActionListenerToOkButton(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent event) {
                                //Check if fields are empty or contain invalid characters
                                if (visitReportDialog.getDate() == null)
                                    visitReportDialog.setInfoLabel("INFO: Adding the visit failed. Date must be chosen!");
                                else if (visitReportDialog.getTime1() == null || visitReportDialog.getTime2() == null)
                                    visitReportDialog.setInfoLabel("INFO: Adding the visit failed. Time must be chosen!");
                                else if (visitReportDialog.getReport().equals(""))
                                    visitReportDialog.setInfoLabel("INFO: Adding the visit failed. Report field cannot be empty!");

                                    //If all fields contain correct characters
                                else {
                                    //Create new domain.VisitReport object
                                    VisitReport visit = new VisitReport(visitReportDialog.getDate(), visitReportDialog.getReport());
                                    //Add visit to list in patient object, sort it and save patientsList to file
                                    patient.addVisitReport(visit);
                                    Collections.sort(patient.getVisitReportList());
                                    theModel.savePatientList();
                                    theView.setInfoLabel("INFO: Vist report has benn added!");
                                    visitReportDialog.dispose();
                                }
                            }
                        });

                        visitReportDialog.addActionListenerToCancelButton(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent event) {
                                theView.setInfoLabel("INFO:");
                                visitReportDialog.dispose();
                            }
                        });

                        //Dialog options
                        visitReportDialog.pack();
                        visitReportDialog.setVisible(true);
                    }
                }
            }
        });

        //Visits history button
        panelPatientsListView.addActionListenerToVisitsHistoryButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Visit history\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                    //Check if patient with selected id exists on the patientsList
                    if (theModel.getPatientById(idFromTable) != null) {
                        //Get patient using selected id
                        Patient patient = theModel.getPatientById(idFromTable);
                        List<VisitReport> visits = patient.getVisitReportList(); //Get visits history list

                        //Create empty dialog
                        VisitsHistoryDialogView visitsHistoryDialogView = new VisitsHistoryDialogView();

                        //Add rows
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
                        String vDate;
                        String[] vMedicalTreatmentsTokens;

                        int brCount;
                        int rowNumber = 0;

                        for (VisitReport v : visits) {
                            vDate = formatter.format(v.getVisitDate().getTime()); //Getting DATE
                            vMedicalTreatmentsTokens = v.getVisitReport().split("\n"); //split visit report to separate rows and put it in table
                            String vMedicalTreatmentsAfterSplit = "<html>"; //add html tag at start
                            for (String x : vMedicalTreatmentsTokens)        //add <br> tags for separate rows
                                vMedicalTreatmentsAfterSplit += (x + "<br>");
                            vMedicalTreatmentsAfterSplit += "</html>";        //add html tag at the end

                            visitsHistoryDialogView.addRowToTable(new String[]{vDate, vMedicalTreatmentsAfterSplit});

                            brCount = StringUtils.countMatches(vMedicalTreatmentsAfterSplit, "<br>");    //how many <br> matches in vMedicalTreatmentsAfterSplit
                            visitsHistoryDialogView.setRowHeight(rowNumber, brCount * 16); //set row height (depends on <br> matches)
                            rowNumber++;
                        }
                        visitsHistoryDialogView.pack();

                        //Add ActionListeners to dialog buttons
                        visitsHistoryDialogView.addActionListenerToEditButton(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent event) {
                                //Check if any row is selected
                                int selectedRowInDialog = visitsHistoryDialogView.getTable().getSelectedRow();
                                if (selectedRowInDialog != -1) {
                                    visitsHistoryDialogView.setVisible(false);

                                    //Create empty dialog
                                    VisitDialogView visitReportDialog = new VisitDialogView();


                                    VisitReport selectedVisit = visits.get(selectedRowInDialog);
                                    visitReportDialog.setDate(
                                            selectedVisit.getVisitDate().get(Calendar.YEAR),
                                            selectedVisit.getVisitDate().get(Calendar.MONTH),
                                            selectedVisit.getVisitDate().get(Calendar.DAY_OF_MONTH)
                                    );

                                    if ((selectedVisit.getVisitDate().get(Calendar.HOUR_OF_DAY)) == 8)
                                        visitReportDialog.setTime1("08");
                                    else if ((selectedVisit.getVisitDate().get(Calendar.HOUR_OF_DAY)) == 9)
                                        visitReportDialog.setTime1("09");
                                    else
                                        visitReportDialog.setTime1(Integer.toString(selectedVisit.getVisitDate().get(Calendar.HOUR_OF_DAY)));

                                    if ((selectedVisit.getVisitDate().get(Calendar.MINUTE)) == 0)
                                        visitReportDialog.setTime2("00");
                                    else
                                        visitReportDialog.setTime2(Integer.toString(selectedVisit.getVisitDate().get(Calendar.MINUTE)));
                                    visitReportDialog.setReport(selectedVisit.getVisitReport());

                                    //Add ActionListeners to dialog buttons
                                    visitReportDialog.addActionListenerToOkButton(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent event) {
                                            //Check if fields are empty or contain invalid characters
                                            if (visitReportDialog.getDate() == null)
                                                visitReportDialog.setInfoLabel("INFO: Adding the visit failed. Date must be chosen!");
                                            else if (visitReportDialog.getTime1() == null || visitReportDialog.getTime2() == null)
                                                visitReportDialog.setInfoLabel("INFO: Adding the visit failed. Time must be chosen!");
                                            else if (visitReportDialog.getReport().equals(""))
                                                visitReportDialog.setInfoLabel("INFO: Adding the visit failed. Report field cannot be empty!");

                                                //If all fields contain correct characters
                                            else {
                                                //Are you sure you want to edit this visit report question
                                                int confirm = JOptionPane.showConfirmDialog(
                                                        theView, "Are you sure you want edit this visit report?", "Confirmation", JOptionPane.YES_NO_OPTION);
                                                if (confirm == JOptionPane.YES_OPTION) {
                                                    //Edit selected domain.VisitReport object
                                                    VisitReport visit = new VisitReport(visitReportDialog.getDate(), visitReportDialog.getReport());
                                                    patient.editVisitReport(visit, selectedRowInDialog);
                                                    Collections.sort(patient.getVisitReportList());
                                                    theModel.savePatientList();

                                                    //Reload table
                                                    ((DefaultTableModel) visitsHistoryDialogView.getTable().getModel()).setRowCount(0);
                                                    String vDate;
                                                    String[] vMedicalTreatmentsTokens;
                                                    int brCount;
                                                    int rowNumber = 0;
                                                    for (VisitReport v : visits) {
                                                        vDate = formatter.format(v.getVisitDate().getTime()); //Getting DATE
                                                        vMedicalTreatmentsTokens = v.getVisitReport().split("\n"); //split visit report to separate rows and put it in table
                                                        String vMedicalTreatmentsAfterSplit = "<html>"; //add html tag at start
                                                        for (String x : vMedicalTreatmentsTokens)        //add <br> tags for separate rows
                                                            vMedicalTreatmentsAfterSplit += (x + "<br>");
                                                        vMedicalTreatmentsAfterSplit += "</html>";        //add html tag at the end

                                                        visitsHistoryDialogView.addRowToTable(new String[]{vDate, vMedicalTreatmentsAfterSplit});

                                                        brCount = StringUtils.countMatches(vMedicalTreatmentsAfterSplit, "<br>");    //how many <br> matches in vMedicalTreatmentsAfterSplit string
                                                        visitsHistoryDialogView.setRowHeight(rowNumber, brCount * 16); //set row height (depends on <br> matches)
                                                        rowNumber++;
                                                    }
                                                    visitReportDialog.dispose();
                                                    visitsHistoryDialogView.pack();
                                                    visitsHistoryDialogView.setVisible(true);
                                                }
                                            }
                                        }
                                    });

                                    visitReportDialog.addActionListenerToCancelButton(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent event) {
                                            theView.setInfoLabel("INFO:");
                                            visitReportDialog.dispose();
                                            visitsHistoryDialogView.setVisible(true);
                                        }
                                    });

                                    //Dialog options
                                    visitReportDialog.pack();
                                    visitReportDialog.setVisible(true);
                                }
                            }
                        });

                        visitsHistoryDialogView.addActionListenerToRemoveButton(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent event) {
                                //Check if any row is selected
                                int selectedRowInDialog = visitsHistoryDialogView.getTable().getSelectedRow();
                                if (selectedRowInDialog != -1) {
                                    //Are you sure you want to remove this visit report question
                                    int confirm = JOptionPane.showConfirmDialog(
                                            theView, "Are you sure you want remove this visit report?", "Confirmation", JOptionPane.YES_NO_OPTION);
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        DefaultTableModel model = (DefaultTableModel) visitsHistoryDialogView.getTable().getModel();
                                        visits.remove(selectedRowInDialog);
                                        model.removeRow(selectedRowInDialog);
                                        theModel.savePatientList();
                                    }
                                }
                            }
                        });
                        visitsHistoryDialogView.setVisible(true);
                    }
                }
            }
        });

        //Add to black list button
        panelPatientsListView.addActionListenerToAddToBlackListButton(new ActionListener() {
            DefaultTableModel model = (DefaultTableModel) panelPatientsListView.getTable().getModel();

            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Add to blacklist\" button!");
                else {
                    //Ask if you want add this patient to blacklist
                    int confirm = JOptionPane.showConfirmDialog(
                            theView, "Are you sure you want add this patient to blacklist?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        //Get Id from hidden column in selected row
                        int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                        //Check if patient with selected id exists on the patientsList
                        if (theModel.getPatientById(idFromTable) != null) {
                            //Get patient using selected id
                            Patient patient = theModel.getPatientById(idFromTable);

                            //Remove row in table
                            model.removeRow(selectedRow);

                            //Set blacklist status
                            patient.setBlackListStatus(true);

                            //Set blacklist reason
                            String reason = JOptionPane.showInputDialog(
                                    theView, "What is the reason for adding the patient to the blacklist?", "Blacklist reason");
                            patient.setBlacklistReason(reason);

                            //Save patientList
                            theModel.savePatientList();

                            //Set info label
                            theView.setInfoLabel("INFO: domain.Patient has been added blacklist! You can check blacklist tab.");

                            //Reload table in view.PanelBlacklistView
                            List<Patient> patientsList = theModel.getPatientList();
                            DefaultTableModel model = (DefaultTableModel) theView.getPanelBlacklistView().getTable().getModel();
                            model.setRowCount(0);
                            for (Patient p : patientsList)
                                //Check if patient is on the blacklist
                                if (p.getBlacklistStatus()) {
                                    model.addRow(new String[]
                                            {
                                                    Integer.toString(p.getId()),
                                                    p.getLastName(),
                                                    p.getFirstName(),
                                                    p.getPersonalIdentityNumber(),
                                                    p.getBlacklistReason()
                                            });
                                }
                        }
                    }
                }
            }
        });


        //Remove patient button
        panelPatientsListView.addActionListenerToRemovePatientButton(new ActionListener() {
            DefaultTableModel model = (DefaultTableModel) panelPatientsListView.getTable().getModel();

            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelPatientsListView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Delete patient\" button!");
                else {
                    //Ask if you want delete selected patient permanently
                    int confirm = JOptionPane.showConfirmDialog(
                            theView, "Are you sure that you want to permanently delete the selected patient?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        //Get Id from hidden column in selected row
                        int idFromTable = Integer.parseInt((String) panelPatientsListView.getTable().getModel().getValueAt(selectedRow, 0));

                        //Remove selected patient if it exists on the patientsList
                        if (theModel.removePatient(idFromTable)) {
                            //Set info label
                            theView.setInfoLabel("INFO: domain.Patient has been delated!");

                            //Remove row in table
                            model.removeRow(selectedRow);

                            //Save patientList after removing patient
                            theModel.savePatientList();
                        } else theView.setInfoLabel("INFO: domain.Patient remove operation failed!");
                    } else theView.setInfoLabel("INFO:");
                }
            }
        });
    }
}
