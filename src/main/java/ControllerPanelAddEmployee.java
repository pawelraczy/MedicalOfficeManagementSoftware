import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This is a controller for the PanelAddEmployee (main controller: "MedicalOfficeController" must initiate it)
 */


public class ControllerPanelAddEmployee 
{
	public ControllerPanelAddEmployee(MedicalOfficeView theView, MedicalOfficeModel theModel)
	{
		PanelAddEmployee panelAddEmployee = theView.getPanelAddEmployee();
		
		//Confirm button
		panelAddEmployee.addActionListenerToConfirmButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Check if fields are empty or contain invalid characters
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateFromPINumber = null;
				if (panelAddEmployee.getDateOfBirth() != null)
					dateFromPINumber = formatter.format(panelAddEmployee.getDateOfBirth().getTime());
				
				if (panelAddEmployee.getFirstName().matches("[0-9]+"))
					theView.setInfoLabel("INFO: First name contains invalid characters!");	
				else if (panelAddEmployee.getFirstName().equals(""))
					theView.setInfoLabel("INFO: First name cannot be empty!");	
				else if(panelAddEmployee.getLastName().matches("[0-9]+"))
					theView.setInfoLabel("INFO: Last name contains invalid characters!");
				else if(panelAddEmployee.getLastName().equals(""))
					theView.setInfoLabel("INFO: Last name cannot be empty!");	
				else if(panelAddEmployee.getGender() == null)
					theView.setInfoLabel("INFO: Gender must be chosen!");
				else if(panelAddEmployee.getDateOfBirth() == null)
					theView.setInfoLabel("INFO: Date of birth must be chosen!");
				else if(panelAddEmployee.getHireDate() == null)
					theView.setInfoLabel("INFO: Hire date must be chosen!");
				else if(panelAddEmployee.getJob() == null)
					theView.setInfoLabel("INFO: Job must be chosen!");
				else if(panelAddEmployee.getCity().matches("[0-9]+"))
					theView.setInfoLabel("INFO: City contains invalid characters!");
				else if(panelAddEmployee.getCity().equals(""))
					theView.setInfoLabel("INFO: City cannot be empty!");
				else if((!(panelAddEmployee.getPostCode1().equals("")) || !(panelAddEmployee.getPostCode2().equals(""))) && !(panelAddEmployee.getPostCode1().matches("[0-9]{2}")))
					theView.setInfoLabel("INFO: First part of post code must contain exactly 2 digits!");
				else if((!(panelAddEmployee.getPostCode1().equals("")) || !(panelAddEmployee.getPostCode2().equals(""))) && !(panelAddEmployee.getPostCode2().matches("[0-9]{3}")))
					theView.setInfoLabel("INFO: Second part of post code must contain exactly 3 digits!");
				else if (!(panelAddEmployee.getApartmentNumber().equals("")) && (!(panelAddEmployee.getApartmentNumber().matches(".*[0-9].*"))))
					theView.setInfoLabel("INFO: Apartment number must contain minimum 1 digit!");
				else if((!(panelAddEmployee.getPhoneNumber1().equals("")) || !(panelAddEmployee.getPhoneNumber2().equals(""))) && !(panelAddEmployee.getPhoneNumber1().matches("[0-9]{2}")))
					theView.setInfoLabel("INFO: First part of phone number must contain exactly 2 digits!");
				else if((!(panelAddEmployee.getPhoneNumber1().equals("")) || !(panelAddEmployee.getPhoneNumber2().equals(""))) && !(panelAddEmployee.getPhoneNumber2().matches("[0-9]{7,9}")))
					theView.setInfoLabel("INFO: Second part of phone number must contain between 7 and 9 digits!");
				else if(!(panelAddEmployee.getEMail().equals(""))  && !(panelAddEmployee.getEMail()
						.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")))
							theView.setInfoLabel("INFO: Incorrect E-mail address!");
				else if (!(panelAddEmployee.getPersonalIdentityNumber().equals("")) && (!(panelAddEmployee.getPersonalIdentityNumber().matches("[0-9]{11}"))))
					theView.setInfoLabel("INFO: Personal identity number must contain exactly 11 digit!");
				else if ((panelAddEmployee.getPersonalIdentityNumber().length() == 11) && (panelAddEmployee.getDateOfBirth() != null))
				{
					if (!(panelAddEmployee.getPersonalIdentityNumber().substring(0,2).equals(dateFromPINumber.substring(2,4)))
							|| !(panelAddEmployee.getPersonalIdentityNumber().substring(2,4).equals(dateFromPINumber.substring(5,7)))
							|| !(panelAddEmployee.getPersonalIdentityNumber().substring(4,6).equals(dateFromPINumber.substring(8,10))))
					theView.setInfoLabel("INFO: First 6 digits in personal identity number must match date of birth!");	
				}
				else if (!(panelAddEmployee.getBankAccountNumber().equals("")) && (!(panelAddEmployee.getBankAccountNumber().matches("[0-9]{26}"))))
					theView.setInfoLabel("INFO: Bank account number must contain exactly 26 digit!");

				//If all fields contain correct characters
				else
				{
					//Create new employee object
					Employee employee = new Employee(
							panelAddEmployee.getIdNumber(),
							panelAddEmployee.getFirstName(),
							panelAddEmployee.getLastName(),
							panelAddEmployee.getGender(),
							panelAddEmployee.getDateOfBirth(),
							panelAddEmployee.getCity(),
							panelAddEmployee.getPostCode1()+ "-" + panelAddEmployee.getPostCode2(),
							panelAddEmployee.getStreet(),
							panelAddEmployee.getApartmentNumber(),
							"+(" + panelAddEmployee.getPhoneNumber1() + ") " + panelAddEmployee.getPhoneNumber2(),
							panelAddEmployee.getEMail(),
							panelAddEmployee.getPersonalIdentityNumber(),
							panelAddEmployee.getBankAccountNumber(),
							panelAddEmployee.getHireDate(),
							panelAddEmployee.getJob()
							);
					
					//Check if employee already exists
					if (theModel.employeeExists(employee))
					{	
						//Ask about replacing employee if employee exists
						int confirm = JOptionPane.showConfirmDialog(
								theView, "This employee already exists. You can  press \"Cancel\" button and check it in \"Employee list\" tab!\n" 
										+ "Would you like to replace the existing employee with a new one though?", "Confirmation", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION)
						{
							//Edit employee, sort employeeList and save it to file
							theModel.editEmployee(employee);
							theModel.sortEmployeeList();
							theModel.saveEmployeeList();
							theView.getPanelPatientsSchedule().setDoctorsList(theModel.getDoctorsList());
						
							//Reload Table
							DefaultTableModel model = (DefaultTableModel) theView.getPanelEmployeeList().getTable().getModel();
							model.setRowCount(0);
							for (Employee e : theModel.getEmployeeList()) 
							{
								model.addRow(new String[] 
								{
									Integer.toString(e.getId()),
									e.getLastName(),
									e.getFirstName(),
									e.getPersonalIdentityNumber(),
									e.getJob()
								});
							}
							
							//EnableMenu (the end of editing)
							theView.isMenuEnable(true);
							panelAddEmployee.isClearFieldsButtonEnable(true);
							theView.setInfoLabel("INFO: Employee has been edited");
							theView.setSwitchingPanel(theView.getPanelEmployeeList());
							
						}
						else 
							theView.setInfoLabel("INFO: Employee list already containts this employee. Press \"Cancel\" button and check \"Employee list\" tab!");
					}
					
					//Employee does not exist. Add it to the employeeList then
					else
					{	
						//Add employee to list and save it, increment and save id
						theModel.addEmployeeToList(employee);
						theModel.saveEmployeeList();
						theModel.incrementAndSaveId();
						theView.getPanelPatientsSchedule().setDoctorsList(theModel.getDoctorsList());
																		
						//Reload Table
						DefaultTableModel model = (DefaultTableModel) theView.getPanelEmployeeList().getTable().getModel();
						model.setRowCount(0);
						for (Employee e : theModel.getEmployeeList()) 
						{
							model.addRow(new String[] 
							{
								Integer.toString(e.getId()),
								e.getLastName(),
								e.getFirstName(),
								e.getPersonalIdentityNumber(),
								e.getJob()
							});
						}
						//Clearing fields
						clearFields(panelAddEmployee);
						theView.setInfoLabel("INFO: ");
						
						//EnableMenu (the end of adding employee)
						theView.isMenuEnable(true);
						panelAddEmployee.isClearFieldsButtonEnable(true);
						
						//Clear ID number in panel
						panelAddEmployee.setIdNumber(0);
						
						//Return to EmployeeList Panel
						theView.setInfoLabel("INFO: The employee has been added.");
						theView.setSwitchingPanel(theView.getPanelEmployeeList());
					}	
				}
			}
		});
		
		
		//Cancel button
		panelAddEmployee.addActionListenerToCancelButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Clearing fields
				clearFields(panelAddEmployee);
				theView.setInfoLabel("INFO: ");
				
				//EnableMenu
				theView.isMenuEnable(true);
				panelAddEmployee.isClearFieldsButtonEnable(true);
				
				//Clear ID number in panel
				panelAddEmployee.setIdNumber(0);
				
				//Switch Panel
				theView.setSwitchingPanel(theView.getPanelEmployeeList());
			}
		});
		
		
		//Clear button
		panelAddEmployee.addActionListenerToClearButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Clearing fields
				clearFields(panelAddEmployee);
				theView.setInfoLabel("INFO: Fields cleared!");
			}
		});
	}
	
	/**
	 * Clear all fields in PanelAddEmployee object
	 * @param panel the PanelAddEmployee from constructor
	*/
	public void clearFields(PanelAddEmployee panel)
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
		Calendar today = Calendar.getInstance();
		panel.setHireDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
		panel.setJob(null);
	}	
}
