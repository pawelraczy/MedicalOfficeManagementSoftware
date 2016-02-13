

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This is a controller for the PanelPatientsSchedule (main controller: "MedicalOfficeController" must initiate it)
 */

public class ControllerPanelPatientsSchedule
{
	public ControllerPanelPatientsSchedule (MedicalOfficeView theView, MedicalOfficeModel theModel)
	{
		PanelPatientsSchedule panelPatientsSchedule = theView.getPanelPatientsSchedule();
		
		//First load of doctors list to comboBox
		panelPatientsSchedule.setDoctorsList(theModel.getDoctorsList());
		
		//First load of schedule list to table
		//Create key for selected date
		String key = panelPatientsSchedule.getSelectedDate();
	
		//Get schedule day (it can return NULL if day is not in scheduleMap)
		ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
		
		//Check if day is on map
		if (dayFromMap != null) //Day is already on map
		{
			for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
				for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
				{
					//Set value in table
					ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
					if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
						panelPatientsSchedule.setTableValue("", i, j);
					else
					{	
					String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
					panelPatientsSchedule.setTableValue(value, i, j);
					}
				}		
		}	
		
		//ActionListener for Cancel/Finish button
		panelPatientsSchedule.addActionListenerToCancelButton(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Clearing fields
				panelPatientsSchedule.setPatientField("A Patient is not selected!");

				//EnableMenu
				theView.isMenuEnable(true);
				
				//Clear ID number in panel
				panelPatientsSchedule.setIdNumber(0);
				
				//Switch Panel
				theView.setSwitchingPanel(theView.getPanelPatientsList());
			}
		});
		
		//ActionListener for Add visit button
		panelPatientsSchedule.addActionListenerToAddVisitButton(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Get selected row and column numbers from table
				int selectedRow = panelPatientsSchedule.getSelectedRow();
				int selectedColumn = panelPatientsSchedule.getSelectedColumn();
				
				//Check if any row is selected
				if (selectedRow == -1 || selectedColumn == -1)
					theView.setInfoLabel("INFO: Please select cell before pressing \"Add visit\" button!");
				else 
				{
					//Check if patient is selected
					if (panelPatientsSchedule.getIdNumber() == 0)
						theView.setInfoLabel("INFO: Please select patient on \"Patients list\" and press \"Add a new appointment\" button, before pressing \"Add visit\" button!");
					else
					{
						//Check if doctor is selected
						if (panelPatientsSchedule.getSelectedDoctor() == null)
							theView.setInfoLabel("INFO: Please select doctor before pressing \"Add visit\" button!");
						else
						{
							//Check the date (you can't add visit to past time)
							Calendar today = Calendar.getInstance();
							Calendar selectedDay = panelPatientsSchedule.getDate();
							if ((selectedDay.get(Calendar.YEAR) < today.get(Calendar.YEAR)) ||
								((selectedDay.get(Calendar.YEAR) >= today.get(Calendar.YEAR)) && (selectedDay.get(Calendar.MONTH) < today.get(Calendar.MONTH))) ||
								((selectedDay.get(Calendar.YEAR) >= today.get(Calendar.YEAR)) && (selectedDay.get(Calendar.MONTH) >= today.get(Calendar.MONTH)) &&
																								(selectedDay.get(Calendar.DAY_OF_MONTH) < today.get(Calendar.DAY_OF_MONTH))))
								theView.setInfoLabel("INFO: You can not add a visit to the past!");
							else
							{
								//Check if visit time is available
								if (!(panelPatientsSchedule.getTableValue(selectedRow, selectedColumn).equals("")))
									theView.setInfoLabel("INFO: This visit is already set for this time!");
								else
									{
									//Ask if you want to add this visit
									int confirm = JOptionPane.showConfirmDialog(
											theView, "Are you sure you want add visit at " + panelPatientsSchedule.getSelectedTime() + " o'clock?", "Confirmation", JOptionPane.YES_NO_OPTION);
									if (confirm == JOptionPane.YES_OPTION)
									{
										//Create key for selected date
										String key = panelPatientsSchedule.getSelectedDate();
										
										//Get schedule day (it can return NULL if day is not in scheduleMap)
										ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
										
										//Check if day is on map
										if (dayFromMap != null) //Day is already on map
										{
											//Check if doctor or patient is not already in the adjacent column at the same time
											ScheduleCellData cellToCheck = null;   //If day exists (checked) and column is selected (checked) cell will also exists. Can't be NULL then!
											if (selectedColumn == 0)
												cellToCheck = dayFromMap.getScheduleCellDataObject(selectedRow, 1);
											else if (selectedColumn == 1)
												cellToCheck = dayFromMap.getScheduleCellDataObject(selectedRow, 0);
										
											String doctorInAdjacentCell = cellToCheck.getDoctor();
											int patientIdInAdjacentCell = cellToCheck.getPatientId();
											
											if (doctorInAdjacentCell.equals(panelPatientsSchedule.getSelectedDoctor()) || patientIdInAdjacentCell == panelPatientsSchedule.getIdNumber())
												theView.setInfoLabel("INFO: Doctor/patient is already assigned to the other room at the same time. Adding the visit failed!");
											else
											{
												ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(selectedRow, selectedColumn);
												cell.setPatient(panelPatientsSchedule.getSelectedPatient());
												cell.setDoctor(panelPatientsSchedule.getSelectedDoctor());
												cell.setPatientId(panelPatientsSchedule.getIdNumber());
												
												theView.setInfoLabel("INFO: The visit has been added!");
												
												//Add visit to patient object
												Patient patient = theModel.getPatient(panelPatientsSchedule.getIdNumber());
												Appointment appointment = new Appointment(key, panelPatientsSchedule.getHeaderTitle(selectedRow), panelPatientsSchedule.getSelectedDoctor());
												patient.addAppointment(appointment);
												theModel.savePatientList();
												
												//Set value in table
												String value = "<html><center>" + panelPatientsSchedule.getSelectedPatient() + "<br>(" 
																+ panelPatientsSchedule.getSelectedDoctor() + ")</center></html>";
												panelPatientsSchedule.setTableValue(value, selectedRow, selectedColumn);
												
												//Save scheduleMap to file
												theModel.saveScheduleMap();	
											}
										}
										else //Day is not on map
										{
											ScheduleDay newDay = new ScheduleDay(panelPatientsSchedule.getDate().get(Calendar.YEAR),
													panelPatientsSchedule.getDate().get(Calendar.MONTH),
													panelPatientsSchedule.getDate().get(Calendar.DAY_OF_MONTH));
											ScheduleCellData cell = newDay.getScheduleCellDataObject(selectedRow, selectedColumn);
											cell.setPatient(panelPatientsSchedule.getSelectedPatient());
											cell.setDoctor(panelPatientsSchedule.getSelectedDoctor());
											cell.setPatientId(panelPatientsSchedule.getIdNumber());
											theModel.addDayToScheduleMap(key, newDay);	
											
											theView.setInfoLabel("INFO: The visit has been added!");
											
											//Add visit to patient object
											Patient patient = theModel.getPatient(panelPatientsSchedule.getIdNumber());
											Appointment appointment = new Appointment(key, panelPatientsSchedule.getHeaderTitle(selectedRow), panelPatientsSchedule.getSelectedDoctor());
											patient.addAppointment(appointment);
											theModel.savePatientList();
											
											//Set value in table
											String value = "<html><center>" + panelPatientsSchedule.getSelectedPatient() + "<br>(" 
															+ panelPatientsSchedule.getSelectedDoctor() + ")</center></html>";
											panelPatientsSchedule.setTableValue(value, selectedRow, selectedColumn);
											
											//Save scheduleMap to file
											theModel.saveScheduleMap();
										}																	
									}
								}
							}
						}
					}		
				}		
			}
		});
		
		//ActionListener for Delete visit button
		panelPatientsSchedule.addActionListenerToDeleteVisitButton(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Get selected row and column numbers from table
				int selectedRow = panelPatientsSchedule.getSelectedRow();
				int selectedColumn = panelPatientsSchedule.getSelectedColumn();
				
				//Check if any row is selected
				if (selectedRow == -1 || selectedColumn == -1)
					theView.setInfoLabel("INFO: Please select cell before pressing \"Delete visit\" button!");
				else 
				{
					//Check if cell is not clear
					if(panelPatientsSchedule.getTableValue(selectedRow, selectedColumn) == "")					
						theView.setInfoLabel("INFO: This visit time is still avaliable!");
					else
					{
						//Ask if you want to delete this visit
						int confirm = JOptionPane.showConfirmDialog(
								theView, "Are you sure you want delete this visit?", "Confirmation", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION)
						{	
							//Create key for selected date
							String key = panelPatientsSchedule.getSelectedDate();
							
							//Get schedule day (after checking if cell is not clear, the day must exists in scheduleMap, so it can't return NULL)
							ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(selectedRow, selectedColumn);
							
							//Delete visit in patient object
							Patient patient = theModel.getPatient(cell.getPatientId());
							patient.removeAppointment(key, panelPatientsSchedule.getHeaderTitle(selectedRow));
							theModel.savePatientList();
							
							//Clear cell data
							cell.setPatient("");
							cell.setDoctor("");
							cell.setPatientId(0);
							theView.setInfoLabel("INFO: The visit has been deleted!");
							
							//Set value in table
							panelPatientsSchedule.setTableValue("", selectedRow, selectedColumn);
							
							//Save scheduleMap to file
							theModel.saveScheduleMap();
						}
					}	
				}					
			}
		});		
		
		//Change listener for JDatePicker
		panelPatientsSchedule.addDateChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent event) 
			{
				Calendar cal = panelPatientsSchedule.getDate();
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				panelPatientsSchedule.setDateField(formatter.format(cal.getTime()));
				
				//LOAD TABLE
				//Create key for selected date
				String key = panelPatientsSchedule.getSelectedDate();
			
				//Get schedule day (it can return NULL if day is not in scheduleMap)
				ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
				
				//Check if day is on map
				if (dayFromMap != null) //Day is already on map
				{
					for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
						for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
						{
							//Set value in table
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
							if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
								panelPatientsSchedule.setTableValue("", i, j);
							else
							{	
							String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
							panelPatientsSchedule.setTableValue(value, i, j);
							}
						}		
				}
				else
				{
					for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
						for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
						{
							//Set value in table
							panelPatientsSchedule.setTableValue("", i, j);
						}	
				}
				
			}
		});
		
		//ActionListener for Next Day button
		panelPatientsSchedule.addActionListenerToNextDayButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = panelPatientsSchedule.getDate();
				cal.add(Calendar.DAY_OF_MONTH,1);
				panelPatientsSchedule.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				
				//LOAD TABLE
				//Create key for selected date
				String key = panelPatientsSchedule.getSelectedDate();
			
				//Get schedule day (it can return NULL if day is not in scheduleMap)
				ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
				
				//Check if day is on map
				if (dayFromMap != null) //Day is already on map
				{
					for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
						for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
						{
							//Set value in table
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
							if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
								panelPatientsSchedule.setTableValue("", i, j);
							else
							{	
							String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
							panelPatientsSchedule.setTableValue(value, i, j);
							}
						}		
				}
				else
				{
					for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
						for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
						{
							//Set value in table
							panelPatientsSchedule.setTableValue("", i, j);
						}	
				}
			}
		});
		
		//ActionListener for Previous Day button
		panelPatientsSchedule.addActionListenerToPreviousDayButton(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = panelPatientsSchedule.getDate();
				cal.add(Calendar.DAY_OF_MONTH,-1);
				panelPatientsSchedule.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));	
				
				//LOAD TABLE
				//Create key for selected date
				String key = panelPatientsSchedule.getSelectedDate();
			
				//Get schedule day (it can return NULL if day is not in scheduleMap)
				ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
				
				//Check if day is on map
				if (dayFromMap != null) //Day is already on map
				{
					for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
						for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
						{
							//Set value in table
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
							if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
								panelPatientsSchedule.setTableValue("", i, j);
							else
							{	
							String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
							panelPatientsSchedule.setTableValue(value, i, j);
							}
						}		
				}
				else
				{
					for (int i=0 ; i<panelPatientsSchedule.getTableRowCount() ; i++)
						for (int j=0 ; j<panelPatientsSchedule.getTableColumnCount() ; j++)
						{
							//Set value in table
							panelPatientsSchedule.setTableValue("", i, j);
						}	
				}
			}
		});
		
		//ListSelectionListener - selecting cells in schedule table
		panelPatientsSchedule.addActionListenerToCellSelectionModel(new ListSelectionListener() 
		{
			
			@Override
			public void valueChanged(ListSelectionEvent e) 
			{	
		        int selectedRow = panelPatientsSchedule.getSelectedRow();
		        int selectedColumn = panelPatientsSchedule.getSelectedColumn();
		        panelPatientsSchedule.setTimeField(panelPatientsSchedule.getHeaderTitle(selectedRow));
			}
		});
	}
}
