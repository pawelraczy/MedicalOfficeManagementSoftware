package controller;

import domain.Patient;
import model.MainModel;
import org.apache.commons.lang3.StringUtils;
import view.MenuAndMainView;
import view.PanelBlacklistView;
import view.PatientCardDialogView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * This is a controller for the view.PanelBlacklistView (main controller: "controller.MenuController" must initiate it)
 */


public class PanelBlacklistController {

    public PanelBlacklistController(MenuAndMainView theView, MainModel theModel) {
        PanelBlacklistView panelBlacklistView = theView.getPanelBlacklistView();

        //First load blacklist to table
        List<Patient> patientsList = theModel.getPatientList();
        DefaultTableModel model = (DefaultTableModel) panelBlacklistView.getTable().getModel();
        for (Patient p : patientsList)
            //Check if patient is on the blacklist
            if (p.getBlacklist()) {
                model.addRow(new String[]
                        {
                                Integer.toString(p.getId()),
                                p.getLastName(),
                                p.getFirstName(),
                                p.getPersonalIdentityNumber(),
                                p.getBlacklistReason()
                        });
            }

        //Search button
        panelBlacklistView.addActionListenerToSearchButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Patient> searchResults = theModel.getPatientsByText(panelBlacklistView.getSearchValue());

                //Load table using new searchResults
                DefaultTableModel model = (DefaultTableModel) panelBlacklistView.getTable().getModel();
                model.setRowCount(0);
                for (Patient p : searchResults)
                    //Check if patient is on the blacklist
                    if (p.getBlacklist()) {
                        model.addRow(new String[]
                                {
                                        Integer.toString(p.getId()),
                                        p.getLastName(),
                                        p.getFirstName(),
                                        p.getPersonalIdentityNumber(),
                                        p.getBlacklistReason()
                                });
                    }

                theView.setInfoLabel("INFO: New search results.");
            }
        });


        //domain.Patient card button
        panelBlacklistView.addActionListenerToShowPatientCardButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelBlacklistView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Show patient card\" button!");
                else {
                    //Get Id from hidden column in selected row
                    int idFromTable = Integer.parseInt((String) panelBlacklistView.getTable().getModel().getValueAt(selectedRow, 0));

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
                        String[] medicalHistoryTokens = patient.getMedicalHistory().split("\n");        //split medical history string to separate rows and put it in table
                        String medicalHistoryAfterSplit = "<html>";    //add html tag at start
                        for (String x : medicalHistoryTokens)        //add <br> tags for separate rows
                        {
                            medicalHistoryAfterSplit += (x + "<br>");
                        }
                        medicalHistoryAfterSplit += "</html>";        //add html tag at the end
                        patientDialog.addRowToTable(new String[]{"Allergies", medicalHistoryAfterSplit});
                        int brCountM = StringUtils.countMatches(medicalHistoryAfterSplit, "<br>");    //how many <br> matches in allergiesAfterSplit string
                        patientDialog.setRowHeight(12, brCountM * 16);        //set row height (depends on <br> matches)

                        if (patient.getBlacklist())
                            patientDialog.addRowToTable(new String[]{"Blacklist reason", patient.getBlacklistReason()});

                        patientDialog.pack();
                        patientDialog.setVisible(true);
                    }
                }
            }
        });

        //Remove from blacklist button
        panelBlacklistView.addActionListenerToRemoveFromBlacklistButton(new ActionListener() {
            DefaultTableModel model = (DefaultTableModel) panelBlacklistView.getTable().getModel();

            @Override
            public void actionPerformed(ActionEvent e) {
                //Get selected row number from table
                int selectedRow = panelBlacklistView.getTable().getSelectedRow();

                //Check if any row is selected
                if (selectedRow == -1)
                    theView.setInfoLabel("INFO: domain.Patient must be choosen before pressing \"Remove from blacklist\" button!");
                else {
                    //Ask if you want remove this patient from blacklist
                    int confirm = JOptionPane.showConfirmDialog(
                            theView, "Are you sure you want remove this patient from blacklist?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        //Get Id from hidden column in selected row
                        int idFromTable = Integer.parseInt((String) panelBlacklistView.getTable().getModel().getValueAt(selectedRow, 0));

                        //Check if patient with selected id exists on the patientsList
                        if (theModel.getPatientById(idFromTable) != null) {
                            //Get patient using selected id
                            Patient patient = theModel.getPatientById(idFromTable);

                            //Remove row in table
                            model.removeRow(selectedRow);

                            //Set blacklist status
                            patient.setBlackList(false);

                            //Clear blacklist reason
                            patient.setBlacklistReason("");

                            //Save patientList
                            theModel.savePatientList();

                            //Reload table in view.PanelPatientsListView
                            List<Patient> patientsList = theModel.getPatientList();
                            DefaultTableModel model = (DefaultTableModel) theView.getPanelPatientsListView().getTable().getModel();
                            model.setRowCount(0);
                            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                            for (Patient p : patientsList)
                                //Check if patient is on the blacklist
                                if (!(p.getBlacklist())) {
                                    model.addRow(new String[]
                                            {
                                                    Integer.toString(p.getId()),
                                                    p.getLastName(),
                                                    p.getFirstName(),
                                                    p.getPersonalIdentityNumber(),
                                                    formatter.format(p.getDateOfBirth().getTime())
                                            });
                                }

                            //Set info label
                            theView.setInfoLabel("INFO: domain.Patient has been removed from blacklist! You can check patients list tab.");
                        }
                    }
                }
            }
        });
    }
}
