import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;


/**
 * This is a controller for the PanelAddPatient (main controller: "MedicalOfficeController" must initiate it)
 */


public class ControllerPanelAddPatient 
{
	public ControllerPanelAddPatient(MedicalOfficeView theView, MedicalOfficeModel theModel)
	{
		PanelAddPatient panelAddPatient = theView.getPanelAddPatient();
		
		//Confirm button
		panelAddPatient.addActionListenerToConfirmButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Check if fields are empty or contain invalid characters
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateFromPINumber = null;
				if (panelAddPatient.getDateOfBirth() != null)
					dateFromPINumber = formatter.format(panelAddPatient.getDateOfBirth().getTime());
				
				if (panelAddPatient.getFirstName().matches("[0-9]+"))
					theView.setInfoLabel("INFO: First name contains invalid characters!");	
				else if (panelAddPatient.getFirstName().equals(""))
					theView.setInfoLabel("INFO: First name cannot be empty!");	
				else if(panelAddPatient.getLastName().matches("[0-9]+"))
					theView.setInfoLabel("INFO: Last name contains invalid characters!");
				else if(panelAddPatient.getLastName().equals(""))
					theView.setInfoLabel("INFO: Last name cannot be empty!");	
				else if(panelAddPatient.getGender() == null)
					theView.setInfoLabel("INFO: Gender must be chosen!");
				else if(panelAddPatient.getDateOfBirth() == null)
					theView.setInfoLabel("INFO: Date of birth must be chosen!");
				else if(panelAddPatient.getCity().matches("[0-9]+"))
					theView.setInfoLabel("INFO: City contains invalid characters!");
				else if(panelAddPatient.getCity().equals(""))
					theView.setInfoLabel("INFO: City cannot be empty!");
				else if((!(panelAddPatient.getPostCode1().equals("")) || !(panelAddPatient.getPostCode2().equals(""))) && !(panelAddPatient.getPostCode1().matches("[0-9]{2}")))
					theView.setInfoLabel("INFO: First part of post code must contain exactly 2 digits!");
				else if((!(panelAddPatient.getPostCode1().equals("")) || !(panelAddPatient.getPostCode2().equals(""))) && !(panelAddPatient.getPostCode2().matches("[0-9]{3}")))
					theView.setInfoLabel("INFO: Second part of post code must contain exactly 3 digits!");
				else if (!(panelAddPatient.getApartmentNumber().equals("")) && (!(panelAddPatient.getApartmentNumber().matches(".*[0-9].*"))))
					theView.setInfoLabel("INFO: Apartment number must contain minimum 1 digit!");
				else if((!(panelAddPatient.getPhoneNumber1().equals("")) || !(panelAddPatient.getPhoneNumber2().equals(""))) && !(panelAddPatient.getPhoneNumber1().matches("[0-9]{2}")))
					theView.setInfoLabel("INFO: First part of phone number must contain exactly 2 digits!");
				else if((!(panelAddPatient.getPhoneNumber1().equals("")) || !(panelAddPatient.getPhoneNumber2().equals(""))) && !(panelAddPatient.getPhoneNumber2().matches("[0-9]{7,9}")))
					theView.setInfoLabel("INFO: Second part of phone number must contain between 7 and 9 digits!");
				else if(!(panelAddPatient.getEMail().equals(""))  && !(panelAddPatient.getEMail()
						.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")))
							theView.setInfoLabel("INFO: Incorrect E-mail address!");
				else if (!(panelAddPatient.getPersonalIdentityNumber().equals("")) && (!(panelAddPatient.getPersonalIdentityNumber().matches("[0-9]{11}"))))
					theView.setInfoLabel("INFO: Personal identity number must contain exactly 11 digit!");
				else if ((panelAddPatient.getPersonalIdentityNumber().length() == 11) && (panelAddPatient.getDateOfBirth() != null))
				{
					if (!(panelAddPatient.getPersonalIdentityNumber().substring(0,2).equals(dateFromPINumber.substring(2,4)))
							|| !(panelAddPatient.getPersonalIdentityNumber().substring(2,4).equals(dateFromPINumber.substring(5,7)))
							|| !(panelAddPatient.getPersonalIdentityNumber().substring(4,6).equals(dateFromPINumber.substring(8,10))))
					theView.setInfoLabel("INFO: First 6 digits in personal identity number must match date of birth!");
				}
				
				//If all fields contain correct characters
				else
				{
					//Create new patient object
					Patient patient = new Patient(
							panelAddPatient.getIdNumber(),
							panelAddPatient.getFirstName(),
							panelAddPatient.getLastName(),
							panelAddPatient.getGender(),
							panelAddPatient.getDateOfBirth(),
							panelAddPatient.getCity(),
							panelAddPatient.getPostCode1()+ "-" + panelAddPatient.getPostCode2(),
							panelAddPatient.getStreet(),
							panelAddPatient.getApartmentNumber(),
							"+(" + panelAddPatient.getPhoneNumber1() + ") " + panelAddPatient.getPhoneNumber2(),
							panelAddPatient.getEMail(),
							panelAddPatient.getPersonalIdentityNumber(), 
							panelAddPatient.getAllergies(),
							panelAddPatient.getMedicalHistoryArea()
							);
					
					//Check if patient already exists
					if (theModel.patientExists(patient))
					{	
						//Ask about replacing patient if patient exists
						int confirm = JOptionPane.showConfirmDialog(
								theView, "This patient already exists. You can  press \"Cancel\" button and check it in \"Patients list\" or \"Blacklist\" tab!\n" 
										+ "Would you like to replace the existing patient with a new one though?", "Confirmation", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION)
						{
							//Edit patient, sort patientsList and save it to file
							theModel.editPatient(patient);
							theModel.savePatientList();
							
							//Reload Table
							DefaultTableModel model = (DefaultTableModel) theView.getPanelPatientsList().getTable().getModel();
							model.setRowCount(0);
							//Format formatter = new SimpleDateFormat("yyyy-MM-dd");
							for (Patient p : theModel.getPatientList()) 
							{
									try
									{
										model.addRow(new String[] 
										{
											Integer.toString(p.getId()),
											p.getLastName(),
											p.getFirstName(),
											p.getPersonalIdentityNumber(),
											formatter.format(p.getDateOfBirth().getTime())
										});
									}
									catch (IllegalArgumentException e)
									{
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
							
							//EnableMenu (the end of editing)
							theView.isMenuEnable(true);
							panelAddPatient.isClearFieldsButtonEnable(true);
							theView.setInfoLabel("INFO: Patient has been edited");
							theView.setSwitchingPanel(theView.getPanelPatientsList());
							
						}
						else 
							theView.setInfoLabel("INFO: Patients list already containts this patient. Press \"Cancel\" button and check \"Patients list\" tab!");
					}
					//Patient does not exist. Add it to the patientList then
					else
					{	
						//Add patient to list and save it, increment and save id
						theModel.addPatientToList(patient);
						theModel.savePatientList();
						theModel.incrementAndSaveId();
																		
						//Reload Table
						DefaultTableModel model = (DefaultTableModel) theView.getPanelPatientsList().getTable().getModel();
						model.setRowCount(0);
						//Format formatter = new SimpleDateFormat("yyyy-MM-dd");
						for (Patient p : theModel.getPatientList()) 
						{
								try
								{
									model.addRow(new String[] 
									{
										Integer.toString(p.getId()),
										p.getLastName(),
										p.getFirstName(),
										p.getPersonalIdentityNumber(),
										formatter.format(p.getDateOfBirth().getTime())
									});
								}
								catch (IllegalArgumentException e)
								{
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
						//Clearing fields
						clearFields(panelAddPatient);
						theView.setInfoLabel("INFO: Fields cleared!");
						
						//Clear ID number in panel
						panelAddPatient.setIdNumber(0);
						
						//EnableMenu (the end of adding patient)
						theView.isMenuEnable(true);
						panelAddPatient.isClearFieldsButtonEnable(true);
						
						//Return to PatientsList Panel
						theView.setInfoLabel("INFO: The patient has been added.");
						theView.setSwitchingPanel(theView.getPanelPatientsList());
					}	
				}
			}
		});
		
		
		//Cancel button
		panelAddPatient.addActionListenerToCancelButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Clearing fields
				clearFields(panelAddPatient);
				theView.setInfoLabel("INFO: ");
				
				//EnableMenu
				theView.isMenuEnable(true);
				panelAddPatient.isClearFieldsButtonEnable(true);
				
				//Clear ID number in panel
				panelAddPatient.setIdNumber(0);
				
				//Switch Panel
				theView.setSwitchingPanel(theView.getPanelPatientsList());
			}
		});
		
		
		//Clear button
		panelAddPatient.addActionListenerToClearButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Clearing fields
				clearFields(panelAddPatient);
				theView.setInfoLabel("INFO: Fields cleared!");
			}
		});
	}	
	
	/**
	 * Clear all fields in PanelAddPatient object
	 * @param panel the PanelAddPatient from constructor
	*/
	public void clearFields(PanelAddPatient panel)
	{
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
