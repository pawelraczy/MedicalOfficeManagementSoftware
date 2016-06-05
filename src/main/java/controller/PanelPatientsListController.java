package controller;

import domain.Appointment;
import domain.Patient;
import domain.VisitReport;
import model.MainModel;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

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
        PanelPatientsScheduleView panelPatientsScheduleView = theView.getPanelPatientsScheduleView();
        PanelAddPatientView panelAddPatientView = theView.getPanelAddPatientView();

        //First load patients list to table
        List<Patient> patientsList = theModel.getPatientList();
        DefaultTableModel model = getTableModel(panelPatientsListView);
        reloadTable(patientsList, model);

        //Search button
        panelPatientsListView.addActionListenerToSearchButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Patient> searchResults = theModel.getPatientsByText(panelPatientsListView.getSearchValue());

                //Load table using new searchResults
                DefaultTableModel model = getTableModel(panelPatientsListView);
                model.setRowCount(0);
                reloadTable(searchResults, model);
                theView.setInfoLabel("INFO: New search results.");
            }
        });

        //Show appointments button
        panelPatientsListView.addActionListenerToShowAppointments(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //Get selected row number from table
                JTable table = panelPatientsListView.getTable();
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Show patient appointments\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    if (isPatientExists(idFromTable, theModel)) {
                        Patient patient = theModel.getPatientById(idFromTable);
                        openAppointmentsDialog(patient);
                    }
                }
            }
        });

        //Add appointment button
        panelPatientsListView.addActionListenerToAddAppointment(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JTable table = panelPatientsListView.getTable();
                int selectedRow = table.getSelectedRow();

                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Add appointment\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    if (isPatientExists(idFromTable, theModel)) {
                        Patient patient = theModel.getPatientById(idFromTable);

                        //Enter patients name to TextField in PatientsSchedulePanel
                        panelPatientsScheduleView.setPatientField(patient.getLastName() + " " + patient.getFirstName());
                        theView.setInfoLabel("INFO: Adding an appointment.");
                        theView.setMainMenuVisibility(false);
                        panelPatientsScheduleView.setIdNumber(idFromTable);
                        theView.setSwitchingPanel(panelPatientsScheduleView);
                    }
                }
            }
        });

        //Add patient button
        panelPatientsListView.addActionListenerToAddPatient(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theView.setInfoLabel("INFO: ");
                theView.setSwitchingPanel(panelAddPatientView);
                theView.setMainMenuVisibility(false);
                getNewPatientIdNumber(panelAddPatientView, theModel.getId());
            }
        });

        //Edit button
        panelPatientsListView.addActionListenerToEditPatientButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = panelPatientsListView.getTable();
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Edit selected patient\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    if (isPatientExists(idFromTable, theModel)) {
                        Patient patient = theModel.getPatientById(idFromTable);
                        loadPatientsInfoToAddPatientPanel(patient, panelAddPatientView);
                        theView.setMainMenuVisibility(false);
                        panelAddPatientView.setClearFieldsButtonVisibility(false);
                        theView.setInfoLabel("INFO: Editing patient!");
                        panelAddPatientView.setIdNumber(idFromTable);
                        theView.setSwitchingPanel(panelAddPatientView);
                    }
                }
            }
        });

        //Patient card button
        panelPatientsListView.addActionListenerToShowPatientCardButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = panelPatientsListView.getTable();
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Show patient card\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    if (isPatientExists(idFromTable, theModel)) {
                        Patient patient = theModel.getPatientById(idFromTable);
                        openPatientCardDialog(patient);
                    }
                }
            }
        });

        //Register visit button
        panelPatientsListView.addActionListenerToRegisterVisitButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = panelPatientsListView.getTable();
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Register visit\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    if (isPatientExists(idFromTable, theModel)) {
                        Patient patient = theModel.getPatientById(idFromTable);
                        openVisitReportDialog(patient, theModel, theView);
                    }
                }
            }
        });

        //Visits history button
        panelPatientsListView.addActionListenerToVisitsHistoryButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = panelPatientsListView.getTable();
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Visit history\" button!");
                else {
                    int idFromTable = getIdFromSelectedRow(table, selectedRow);
                    if (isPatientExists(idFromTable, theModel)) {
                        Patient patient = theModel.getPatientById(idFromTable);
                        openVisitsHistoryDialog(patient, theView, theModel);
                    }
                }
            }
        });

        //Add to black list button
        panelPatientsListView.addActionListenerToAddToBlackListButton(new ActionListener() {
            JTable table = panelPatientsListView.getTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Add to blacklist\" button!");
                else {
                    int confirm = askForConfirmation(theView, "Are you sure you want add this patient to blacklist?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        int idFromTable = getIdFromSelectedRow(table, selectedRow);
                        if (isPatientExists(idFromTable, theModel)) {
                            Patient patient = theModel.getPatientById(idFromTable);
                            model.removeRow(selectedRow);
                            patient.setBlackListStatus(true);
                            String reason = enterBlacklistReason(theView);
                            patient.setBlacklistReason(reason);
                            theModel.savePatientList();
                            theView.setInfoLabel("INFO: domain.Patient has been added blacklist! You can check blacklist tab.");
                            reloadTableInPanelBlacklistView(theModel, theView);
                        }
                    }
                }
            }
        });

        //Remove patient button
        panelPatientsListView.addActionListenerToRemovePatientButton(new ActionListener() {
            JTable table = panelPatientsListView.getTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (!isAnyRowSelected(selectedRow))
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Delete patient\" button!");
                else {
                    int confirm = askForConfirmation(theView, "Are you sure that you want to permanently delete the selected patient?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        int idFromTable = getIdFromSelectedRow(table, selectedRow);
                        if (theModel.removePatient(idFromTable)) {
                            theView.setInfoLabel("INFO: domain.Patient has been delated!");
                            model.removeRow(selectedRow);
                            theModel.savePatientList();
                        } else theView.setInfoLabel("INFO: domain.Patient remove operation failed!");
                    } else theView.setInfoLabel("INFO:");
                }
            }
        });
    }

    private void reloadTableInPanelBlacklistView(MainModel theModel, MenuAndMainView theView) {
        List<Patient> patientsList = theModel.getPatientList();
        PanelBlacklistView panelBlacklistView = theView.getPanelBlacklistView();
        JTable table = panelBlacklistView.getTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Patient p : patientsList)
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

    private String enterBlacklistReason(MenuAndMainView theView) {
        return JOptionPane.showInputDialog(
                theView, "What is the reason for adding the patient to the blacklist?", "Blacklist reason");
    }

    private void openVisitsHistoryDialog(Patient patient, MenuAndMainView theView, MainModel theModel) {
        List<VisitReport> visits = patient.getVisitReportList();
        VisitsHistoryDialogView visitsHistoryDialogView = new VisitsHistoryDialogView();
        loadRowsInVisitsHistoryDialog(visits, visitsHistoryDialogView);
        visitsHistoryDialogView.pack();

        //Add ActionListeners to dialog buttons
        visitsHistoryDialogView.addActionListenerToEditButton(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JTable table = visitsHistoryDialogView.getTable();
                int selectedRowInDialog = table.getSelectedRow();
                if (selectedRowInDialog != -1) {
                    visitsHistoryDialogView.setVisible(false);
                    openVisitReportDialog(selectedRowInDialog, visits, theView, patient, theModel, visitsHistoryDialogView);


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
                    int confirm = askForConfirmation(theView, "Are you sure you want remove this visit report?");
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

    private void openVisitReportDialog(int selectedRowInDialog, List<VisitReport> visits, MenuAndMainView theView, Patient patient, MainModel theModel,
                                       VisitsHistoryDialogView visitsHistoryDialogView) {

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
                else {

                    int confirm = askForConfirmation(theView, "Are you sure you want edit this visit report?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        VisitReport visit = new VisitReport(visitReportDialog.getDate(), visitReportDialog.getReport());
                        patient.editVisitReport(visit, selectedRowInDialog);
                        Collections.sort(patient.getVisitReportList());
                        theModel.savePatientList();

                        //Reload table
                        JTable table = visitsHistoryDialogView.getTable();
                        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                        tableModel.setRowCount(0);
                        loadRowsInVisitsHistoryDialog(visits, visitsHistoryDialogView);
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

        visitReportDialog.pack();
        visitReportDialog.setVisible(true);
    }

    private int askForConfirmation(MenuAndMainView theView, String message) {
        return JOptionPane.showConfirmDialog(
                theView, message, "Confirmation", JOptionPane.YES_NO_OPTION);
    }

    private void loadRowsInVisitsHistoryDialog(List<VisitReport> visits, VisitsHistoryDialogView visitsHistoryDialogView) {
        String vDate;
        String[] vMedicalTreatmentsTokens;
        int brCount;
        int rowNumber = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
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
    }

    private void openVisitReportDialog(Patient patient, MainModel theModel, MenuAndMainView theView) {
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
                else {
                    VisitReport visit = new VisitReport(visitReportDialog.getDate(), visitReportDialog.getReport());
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

    private void openPatientCardDialog(Patient patient) {
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

    private void loadPatientsInfoToAddPatientPanel(Patient patient, PanelAddPatientView panelAddPatientView) {
        panelAddPatientView.setFirstName(patient.getFirstName());
        panelAddPatientView.setLastName(patient.getLastName());
        panelAddPatientView.setGender(patient.getGender());

        panelAddPatientView.setDateOfBirth(
                patient.getDateOfBirth().get(Calendar.YEAR),
                patient.getDateOfBirth().get(Calendar.MONTH),
                patient.getDateOfBirth().get(Calendar.DAY_OF_MONTH)
        );
        panelAddPatientView.setDateModelSelected(true);

        panelAddPatientView.setCity(patient.getCity());
        if (patient.getPostCode().length() == 6) {
            panelAddPatientView.setPostCode1(patient.getPostCode().substring(0, 2));
            panelAddPatientView.setPostCode2(patient.getPostCode().substring(3, 6));
        } else {
            panelAddPatientView.setPostCode1("");
            panelAddPatientView.setPostCode2("");
        }

        panelAddPatientView.setStreet(patient.getStreet());
        panelAddPatientView.setApartmentNumber(patient.getHouseApartmentNumber());

        if (patient.getPhoneNumber().length() > 8) {
            panelAddPatientView.setPhoneNumber1(patient.getPhoneNumber().substring(2, 4));
            panelAddPatientView.setPhoneNumber2(patient.getPhoneNumber().substring(6));
        } else {
            panelAddPatientView.setPhoneNumber1("");
            panelAddPatientView.setPhoneNumber2("");
        }

        panelAddPatientView.setEMail(patient.getEMail());
        panelAddPatientView.setPersonalIdentityNumber(patient.getPersonalIdentityNumber());
        panelAddPatientView.setAllergies(patient.getAllergies());
        panelAddPatientView.setMedicalHistoryArea(patient.getMedicalHistory());
    }

    private void getNewPatientIdNumber(PanelAddPatientView panelAddPatientView, int id) {
        panelAddPatientView.setIdNumber(id);
    }

    private void openAppointmentsDialog(Patient patient) {
        PatientAppointmentsDialogView patientAppointmentsDialogView = new PatientAppointmentsDialogView();
        patientAppointmentsDialogView.setPatientName("PATIENT: " + patient.getFirstName() + " " + patient.getLastName());

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
                            (day < today.get(Calendar.DAY_OF_MONTH))))) {
                patientAppointmentsDialogView.addRowToTable(new String[]{a.getDate() + ", " + a.getTime(), a.getDoctor()});
            }
        }
        patientAppointmentsDialogView.pack();
        patientAppointmentsDialogView.setVisible(true);
    }

    private boolean isPatientExists(int idFromTable, MainModel theModel) {
        return theModel.getPatientById(idFromTable) != null;
    }

    private int getIdFromSelectedRow(JTable table, int selectedRow) {
        return Integer.parseInt((String) table.getModel().getValueAt(selectedRow, 0));
    }

    private boolean isAnyRowSelected(int selectedRow) {
        return selectedRow != -1;
    }

    private void reloadTable(List<Patient> patientsList, DefaultTableModel model) {
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
    }

    private DefaultTableModel getTableModel(PanelPatientsListView panelPatientsListView) {
        JTable table = panelPatientsListView.getTable();
        return (DefaultTableModel) table.getModel();
    }
}
