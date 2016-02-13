import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * This is a controller for the PanelEmployeeList (main controller: "MedicalOfficeController" must initiate it)
 */


public class ControllerPanelEmployeeList 
{
	
	public ControllerPanelEmployeeList(MedicalOfficeView theView, MedicalOfficeModel theModel)
	{
		PanelEmployeeList panelEmployeeList = theView.getPanelEmployeeList();
		
		//First load employee list to table
		List<Employee> employeeList = theModel.getEmployeeList();
		DefaultTableModel model = (DefaultTableModel) panelEmployeeList.getTable().getModel();
		for (Employee e : employeeList)
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
		
		//Search button
		panelEmployeeList.addActionListenerToSearchButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{		
				List<Employee> searchResults = theModel.getSearchResultsEmployee(panelEmployeeList.getSearchValue());
				
				//Load table using new searchResults
				DefaultTableModel model = (DefaultTableModel) panelEmployeeList.getTable().getModel();
				model.setRowCount(0);
				for (Employee e : searchResults) 
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
				theView.setInfoLabel("INFO: New search results.");
			}	
		});
		
		
		//Add employee button
		panelEmployeeList.addActionListenerToAddEmployee(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Switch panel
				theView.setInfoLabel("INFO: ");
				theView.setSwitchingPanel(theView.getPanelAddEmployee());
				
				//Disable menu
				theView.isMenuEnable(false);
				
				//Get new id number for employee
				theView.getPanelAddEmployee().setIdNumber(theModel.getId());
			}
		});
		
		
		//Edit button
		panelEmployeeList.addActionListenerToEditEmployeeButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Get selected row number from table
				int selectedRow = panelEmployeeList.getTable().getSelectedRow();
				
				//Check if any row is selected
				if (selectedRow == -1)
					theView.setInfoLabel("INFO: Employee must be choosen before pressing \"Edit selected employee\" button!");
				else 
				{
					//Get Id from hidden column in selected row
					int idFromTable = Integer.parseInt((String)panelEmployeeList.getTable().getModel().getValueAt(selectedRow, 0));
					
					//Check if employee with selected id exists on the employeeList
					if (theModel.getEmployee(idFromTable) != null)			
					{
						//Get employee using selected id
						Employee employee = theModel.getEmployee(idFromTable);
						
						//Load values to AddEmployee Panel
						theView.getPanelAddEmployee().setFirstName(employee.getFirstName());
						theView.getPanelAddEmployee().setLastName(employee.getLastName());
						theView.getPanelAddEmployee().setGender(employee.getGender());
						
						theView.getPanelAddEmployee().setDateOfBirth(
								employee.getDateOfBirth().get(Calendar.YEAR),
								employee.getDateOfBirth().get(Calendar.MONTH),
								employee.getDateOfBirth().get(Calendar.DAY_OF_MONTH)
								);
						theView.getPanelAddEmployee().setDateModelSelected(true);
						
						theView.getPanelAddEmployee().setCity(employee.getCity());
						if (employee.getPostCode().length() == 6)
						{
							theView.getPanelAddEmployee().setPostCode1(employee.getPostCode().substring(0, 2));
							theView.getPanelAddEmployee().setPostCode2(employee.getPostCode().substring(3, 6));
						}
						{
							theView.getPanelAddEmployee().setPostCode1("");
							theView.getPanelAddEmployee().setPostCode2("");
						}
						
						theView.getPanelAddEmployee().setStreet(employee.getStreet());
						theView.getPanelAddEmployee().setApartmentNumber(employee.getHouseApartmentNumber());
						
						if (employee.getPhoneNumber().length() > 8)
						{
							theView.getPanelAddEmployee().setPhoneNumber1(employee.getPhoneNumber().substring(2, 4));
							theView.getPanelAddEmployee().setPhoneNumber2(employee.getPhoneNumber().substring(6));
						}
						else 
						{
							theView.getPanelAddEmployee().setPhoneNumber1("");
							theView.getPanelAddEmployee().setPhoneNumber2("");
						}

						theView.getPanelAddEmployee().setEMail(employee.getEMail());
						theView.getPanelAddEmployee().setPersonalIdentityNumber(employee.getPersonalIdentityNumber());
						theView.getPanelAddEmployee().setBankAccountNumber(employee.getBankAccountNumber());
						
						theView.getPanelAddEmployee().setHireDate(
								employee.getHireDate().get(Calendar.YEAR),
								employee.getHireDate().get(Calendar.MONTH),
								employee.getHireDate().get(Calendar.DAY_OF_MONTH)
								);
						theView.getPanelAddEmployee().setHireDateModelSelected(true);
						
						theView.getPanelAddEmployee().setJob(employee.getJob());
						
						//Disable menu
						theView.isMenuEnable(false);
						theView.getPanelAddEmployee().isClearFieldsButtonEnable(false);
						
						//Set info label
						theView.setInfoLabel("INFO: Editing employee!");		
						
						//Set id number on panel as selected id number from table
						theView.getPanelAddEmployee().setIdNumber(idFromTable);

						//Switch Panel
						theView.setSwitchingPanel(theView.getPanelAddEmployee());
					}
				}	
			}
		});
		
		//Employee card button
		panelEmployeeList.addActionListenerToShowEmployeeCardButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Get selected row number from table
				int selectedRow = panelEmployeeList.getTable().getSelectedRow();
				
				//Check if any row is selected
				if (selectedRow == -1)
					theView.setInfoLabel("INFO: Employee must be choosen before pressing \"Show employee card\" button!");
				else 
				{
					//Get Id from hidden column in selected row
					int idFromTable = Integer.parseInt((String)panelEmployeeList.getTable().getModel().getValueAt(selectedRow, 0));
					
					//Check if employee with selected id exists on the employeeList
					if (theModel.getEmployee(idFromTable) != null)			
					{
						//Get employee using selected id
						Employee employee = theModel.getEmployee(idFromTable);
						System.out.println(employee);
						
						//Create empty dialog				
						EmployeeCardDialog employeeDialog = new EmployeeCardDialog();
						
						//Add rows
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
				}
			}
		});
				
		//Remove employee button
		panelEmployeeList.addActionListenerToRemoveEmployeeButton(new ActionListener() 
		{
			DefaultTableModel model = (DefaultTableModel)panelEmployeeList.getTable().getModel();
			
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				//Get selected row number from table
				int selectedRow = panelEmployeeList.getTable().getSelectedRow();
				
				//Check if any row is selected
				if (selectedRow == -1) 
					theView.setInfoLabel("INFO: Employee must be choosen before pressing \"Delete employee\" button!");
				else 
				{
					//Ask if you want delete selected employee permanently
					int confirm = JOptionPane.showConfirmDialog(
							theView, "Are you sure that you want to permanently delete the selected employee?", "Confirmation", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION)
					{
						//Get Id from hidden column in selected row
						int idFromTable = Integer.parseInt((String)panelEmployeeList.getTable().getModel().getValueAt(selectedRow, 0));
						
						//Remove selected employee if it exists on the employeeList
						if(theModel.removeEmployee(idFromTable))
						{
							//Set info label
							theView.setInfoLabel("INFO: Employee has been delated!");
							
							//Remove row in table
							model.removeRow(selectedRow);
							
							//Sort and save employeeList after removing employee
							theModel.sortEmployeeList();
							theModel.saveEmployeeList();
							theView.getPanelPatientsSchedule().setDoctorsList(theModel.getDoctorsList());
						}
						else theView.setInfoLabel("INFO: Employee remove operation failed!");
					}
					else theView.setInfoLabel("INFO:");	
				}
			}
		});
	}

	
	
	
	
	public class EmployeeCardDialog extends JDialog 
	{		 
		DefaultTableModel employeeModel;
		
		public EmployeeCardDialog() 
		{
			  	super((Frame)null, "Employee card", true);
			  	//Creating JTable
			  	String col[] = {"Data", "Value"};
				employeeModel = new DefaultTableModel(col, 0)
					{
						//Turning off editable possibility for JTable
						@Override
						public boolean isCellEditable(int row, int column)
						{
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
		
		public void addRowToTable(String[] row)
		{
			employeeModel.addRow(row);
		}

	}
}
