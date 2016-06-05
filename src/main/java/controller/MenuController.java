package controller;

import model.MainModel;
import view.MenuAndMainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the main controller for Small Medical Office Management software.
 */

public class MenuController {

    private MenuAndMainView theView;
    private MainModel theModel;

    public MenuController(MenuAndMainView theView, MainModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        //Shortcut Buttons ActionListeners
        AddPatientListener addPatientListener = new AddPatientListener();
        PatientsListListener patientsListListener = new PatientsListListener();
        PatientsScheduleListener patientsScheduleListener = new PatientsScheduleListener();
        this.theView.addActionListenerToAddPatientButton(addPatientListener);
        this.theView.addActionListenerToPatientListButton(patientsListListener);
        this.theView.addActionListenerToPatientsScheduleButton(patientsScheduleListener);

        //Menu ActionListeners
        this.theView.addActionListenerToExitMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedOption = JOptionPane.showConfirmDialog(MenuController.this.theView, "Are you sure you want to exit?", "Confirmation request",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (selectedOption == JOptionPane.OK_OPTION) System.exit(0);
            }
        });

        this.theView.addActionListenerToAddPatientMenu(addPatientListener);
        this.theView.addActionListenerToPatientsListMenu(patientsListListener);
        this.theView.addActionListenerToBlackListMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuController.this.theView.setInfoLabel("INFO:");
                MenuController.this.theView.setSwitchingPanel(MenuController.this.theView.getPanelBlacklistView());
            }
        });

        this.theView.addActionListenerToAddEmployeeMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuController.this.theView.setInfoLabel("INFO:");
                MenuController.this.theView.setSwitchingPanel(MenuController.this.theView.getPanelAddEmployeeView());
                MenuController.this.theView.setMainMenuVisibility(false);
                MenuController.this.theView.getPanelAddEmployeeView().setIdNumber(MenuController.this.theModel.getId());
            }
        });

        this.theView.addActionListenerToEmployeesListMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuController.this.theView.setInfoLabel("INFO:");
                MenuController.this.theView.setSwitchingPanel(MenuController.this.theView.getPanelEmployeeList());
            }
        });

        this.theView.addActionListenerToPatientsScheduleMenu(patientsScheduleListener);

        this.theView.addActionListenerToAboutMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Message in about window
                String message = "<html><b>Medical Office Management Software</b><br><br>"
                        + "Version: 1.02<br><br>"
                        + "<center>Copyright \u00a9 2015 Pawel Raczy</center></html>";
                //Display about window
                MenuController.this.theView.displayAboutMessage(message);
            }
        });

        new PanelAddPatientController(this.theView, this.theModel);
        new PanelPatientsListController(this.theView, this.theModel);
        new PanelBlacklistController(this.theView, this.theModel);
        new PanelAddEmployeeController(this.theView, this.theModel);
        new PanelEmployeeListController(this.theView, this.theModel);
        new PanelPatientsScheduleController(this.theView, this.theModel);
    }

    class AddPatientListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theView.setInfoLabel("INFO:");
            theView.setSwitchingPanel(theView.getPanelAddPatientView());
            theView.setMainMenuVisibility(false);
            theView.getPanelAddPatientView().setIdNumber(theModel.getId());
        }
    }

    class PatientsListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theView.setInfoLabel("INFO:");
            theView.setSwitchingPanel(theView.getPanelPatientsListView());
        }
    }

    class PatientsScheduleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theView.setInfoLabel("INFO:");
            theView.setSwitchingPanel(theView.getPanelPatientsScheduleView());
        }
    }
}		