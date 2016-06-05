package controller;

import domain.Patient;
import model.MainModel;
import view.MenuAndMainView;
import view.PanelBlacklistView;
import view.PanelPatientsListView;
import view.PatientCardDialogView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This is a controller for the view.PanelBlacklistView (main controller:
 * "controller.MenuController" must initiate it)
 */

public class PanelBlacklistController {

	public PanelBlacklistController(MenuAndMainView theView, MainModel theModel) {
		PanelBlacklistView panelBlacklistView = theView.getPanelBlacklistView();
		PanelPatientsListView panelPatientsListView = theView
				.getPanelPatientsListView();

		// First load blacklist to table
		List<Patient> patientsList = theModel.getPatientList();
		DefaultTableModel model = getTableModel(panelBlacklistView);
		reloadBlacklistTable(patientsList, model);

		// Search button
		panelBlacklistView
				.addActionListenerToSearchButton(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						List<Patient> searchResults = theModel
								.getPatientsByText(panelBlacklistView
										.getSearchValue());
						DefaultTableModel model = getTableModel(panelBlacklistView);
						reloadBlacklistTable(searchResults, model);
						theView.setInfoLabel("INFO: New search results.");
					}

				});

		// Patient card button
		panelBlacklistView
				.addActionListenerToShowPatientCardButton(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Get selected row number from table
						JTable table = panelBlacklistView.getTable();
						int selectedRow = table.getSelectedRow();
						if (!isAnyRowSelected(selectedRow))
							theView.setInfoLabel("Whoops! Patient must be chosen before pressing button!");
						else {
							int idFromTable = getIdFromSelectedRow(selectedRow,
									panelBlacklistView);
							if (theModel.getPatientById(idFromTable) != null) {
								Patient patient = theModel
										.getPatientById(idFromTable);
								createPatientCardDialog(patient);
							}
						}
					}
				});

		// Remove from blacklist button
		panelBlacklistView
				.addActionListenerToRemoveFromBlacklistButton(new ActionListener() {
					JTable table = panelBlacklistView.getTable();
					DefaultTableModel model = (DefaultTableModel) table
							.getModel();

					@Override
					public void actionPerformed(ActionEvent e) {
						JTable table = panelBlacklistView.getTable();
						int selectedRow = table.getSelectedRow();
						if (!isAnyRowSelected(selectedRow))
							theView.setInfoLabel("INFO: Patient must be choosen before pressing \"Remove from blacklist\" button!");
						else {
							// Ask if you want remove this patient from
							// blacklist
							int confirm = openDialogAndAskForConfirmation(theView);
							if (confirm == JOptionPane.YES_OPTION) {
								int idFromTable = getIdFromSelectedRow(
										selectedRow, panelBlacklistView);
								if (theModel.getPatientById(idFromTable) != null) {
									Patient patient = theModel
											.getPatientById(idFromTable);
									model.removeRow(selectedRow);
									patient.setBlackListStatus(false);
									patient.setBlacklistReason("");
									theModel.savePatientList();

									// Reload table in PanelPatientsListView
									List<Patient> patientsList = theModel
											.getPatientList();
									JTable patientsListTable = panelPatientsListView
											.getTable();
									DefaultTableModel model = (DefaultTableModel) patientsListTable
											.getModel();
									model.setRowCount(0);
									Format formatter = new SimpleDateFormat(
											"yyyy-MM-dd");
									for (Patient p : patientsList)
										if (!(p.getBlacklistStatus())) {
											model.addRow(new String[] {
													Integer.toString(p.getId()),
													p.getLastName(),
													p.getFirstName(),
													p.getPersonalIdentityNumber(),
													formatter.format(p
															.getDateOfBirth()
															.getTime()) });
										}
									theView.setInfoLabel("INFO: Patient has been removed from blacklist! You can check patients list tab.");
								}
							}
						}
					}
				});
	}

	private int openDialogAndAskForConfirmation(MenuAndMainView theView) {
		return JOptionPane.showConfirmDialog(theView,
				"Are you sure you want remove this patient from blacklist?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
	}

	private boolean isAnyRowSelected(int selectedRow) {
		return selectedRow != -1;
	}

	private void createPatientCardDialog(Patient patient) {
		PatientCardDialogView patientDialog = new PatientCardDialogView();
		SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
		patientDialog.addRowToTable(new String[] { "First Name",
				patient.getFirstName() });
		patientDialog.addRowToTable(new String[] { "Last Name",
				patient.getLastName() });
		patientDialog.addRowToTable(new String[] { "Gender",
				patient.getGender() });
		patientDialog.addRowToTable(new String[] { "Date of birth",
				formatter.format(patient.getDateOfBirth().getTime()) });
		patientDialog.addRowToTable(new String[] { "City", patient.getCity() });
		if (patient.getPostCode().length() > 5)
			patientDialog.addRowToTable(new String[] { "Post code",
					patient.getPostCode() });
		else
			patientDialog.addRowToTable(new String[] { "Post code", "" });
		patientDialog.addRowToTable(new String[] { "Street",
				patient.getStreet() });
		patientDialog.addRowToTable(new String[] { "House apartment number",
				patient.getHouseApartmentNumber() });
		if (patient.getPhoneNumber().length() > 6)
			patientDialog.addRowToTable(new String[] { "Phone number",
					patient.getPhoneNumber() });
		else
			patientDialog.addRowToTable(new String[] { "Phone number", "" });
		patientDialog
				.addRowToTable(new String[] { "E-mail", patient.getEMail() });
		patientDialog.addRowToTable(new String[] { "Personal identity number",
				patient.getPersonalIdentityNumber() });

		// Add allergies row
		String[] allergiesTokens = patient.getAllergies().split("\n"); // split
																		// allergies
																		// string
																		// to
																		// separate
																		// rows
																		// and
																		// put
																		// it in
																		// table
		String allergiesAfterSplit = "<html>"; // add html tag at start
		for (String x : allergiesTokens) // add <br> tags for separate rows
		{
			allergiesAfterSplit += (x + "<br>");
		}
		allergiesAfterSplit += "</html>"; // add html tag at the end
		patientDialog.addRowToTable(new String[] { "Allergies",
				allergiesAfterSplit });
		int brCountA = StringUtils.countMatches(allergiesAfterSplit, "<br>"); // how
																				// many
																				// <br>
																				// matches
																				// in
																				// allergiesAfterSplit
																				// string
		patientDialog.setRowHeight(11, brCountA * 16); // set row height
														// (depends on <br>
														// matches)

		// Add medical history row
		String[] medicalHistoryTokens = patient.getMedicalHistory().split("\n"); // split
																					// medical
																					// history
																					// string
																					// to
																					// separate
																					// rows
																					// and
																					// put
																					// it
																					// in
																					// table
		String medicalHistoryAfterSplit = "<html>"; // add html tag at start
		for (String x : medicalHistoryTokens) // add <br> tags for separate rows
		{
			medicalHistoryAfterSplit += (x + "<br>");
		}
		medicalHistoryAfterSplit += "</html>"; // add html tag at the end
		patientDialog.addRowToTable(new String[] { "Allergies",
				medicalHistoryAfterSplit });
		int brCountM = StringUtils.countMatches(allergiesAfterSplit, "<br>"); // how
																				// many
																				// <br>
																				// matches
																				// in
																				// allergiesAfterSplit
																				// string
		patientDialog.setRowHeight(12, brCountM * 16); // set row height
														// (depends on <br>
														// matches)

		if (patient.getBlacklistStatus())
			patientDialog.addRowToTable(new String[] { "Blacklist reason",
					patient.getBlacklistReason() });

		patientDialog.pack();
		patientDialog.setVisible(true);
	}

	private int getIdFromSelectedRow(int selectedRow,
			PanelBlacklistView panelBlacklistView) {
		return Integer.parseInt((String) panelBlacklistView.getTable()
				.getModel().getValueAt(selectedRow, 0));
	}

	private void reloadBlacklistTable(List<Patient> patientsList,
			DefaultTableModel model) {
		model.setRowCount(0);
		for (Patient p : patientsList) {
			if (p.getBlacklistStatus()) {
				model.addRow(new String[] { Integer.toString(p.getId()),
						p.getLastName(), p.getFirstName(),
						p.getPersonalIdentityNumber(), p.getBlacklistReason() });
			}
		}
	}

	private DefaultTableModel getTableModel(
			PanelBlacklistView panelBlacklistView) {
		JTable table = panelBlacklistView.getTable();
		return (DefaultTableModel) table.getModel();
	}
}