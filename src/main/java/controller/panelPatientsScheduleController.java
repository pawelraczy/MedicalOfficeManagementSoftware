package controller;

import domain.Appointment;
import domain.Patient;
import domain.ScheduleCellData;
import domain.ScheduleDay;
import model.MainModel;
import view.MenuAndMainView;
import view.PanelPatientsScheduleView;

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
 * This is a controller for the view.PanelPatientsScheduleView (main controller: "controller.MenuController" must initiate it)
 */

public class PanelPatientsScheduleController
{
	public PanelPatientsScheduleController(MenuAndMainView theView, MainModel theModel)
	{
		PanelPatientsScheduleView panelPatientsScheduleView = theView.getPanelPatientsScheduleView();
		
		//First load of doctors list to comboBox
		panelPatientsScheduleView.setDoctorsList(theModel.getDoctorsList());
		
		//First load of schedule list to table
		//Create key for selected date
		String key = panelPatientsScheduleView.getSelectedDate();
	
		//Get schedule day (it can return NULL if day is not in scheduleMap)
		ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
		
		//Check if day is on map
		if (dayFromMap != null) //Day is already on map
		{
			for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
				for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
				{
					//Set value in table
					ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
					if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
						panelPatientsScheduleView.setTableValue("", i, j);
					else
					{	
					String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
					panelPatientsScheduleView.setTableValue(value, i, j);
					}
				}		
		}	
		
		//ActionListener for Cancel/Finish button
		panelPatientsScheduleView.addActionListenerToCancelButton(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Clearing fields
				panelPatientsScheduleView.setPatientField("Patient is not selected!");

				//EnableMenu
				theView.setMainMenuVisibility(true);
				
				//Clear ID number in panel
				panelPatientsScheduleView.setIdNumber(0);
				
				//Switch Panel
				theView.setSwitchingPanel(theView.getPanelPatientsListView());
			}
		});
		
		//ActionListener for Add visit button
		panelPatientsScheduleView.addActionListenerToAddVisitButton(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Get selected row and column numbers from table
				int selectedRow = panelPatientsScheduleView.getSelectedRow();
				int selectedColumn = panelPatientsScheduleView.getSelectedColumn();
				
				//Check if any row is selected
				if (selectedRow == -1 || selectedColumn == -1)
					theView.setInfoLabel("INFO: Please select cell before pressing \"Add visit\" button!");
				else 
				{
					//Check if patient is selected
					if (panelPatientsScheduleView.getIdNumber() == 0)
						theView.setInfoLabel("INFO: Please select patient on \"Patients list\" and press \"Add a new appointment\" button, before pressing \"Add visit\" button!");
					else
					{
						//Check if doctor is selected
						if (panelPatientsScheduleView.getSelectedDoctor() == null)
							theView.setInfoLabel("INFO: Please select doctor before pressing \"Add visit\" button!");
						else
						{
							//Check the date (you can't add visit to past time)
							Calendar today = Calendar.getInstance();
							Calendar selectedDay = panelPatientsScheduleView.getDate();
							if ((selectedDay.get(Calendar.YEAR) < today.get(Calendar.YEAR)) ||
								((selectedDay.get(Calendar.YEAR) >= today.get(Calendar.YEAR)) && (selectedDay.get(Calendar.MONTH) < today.get(Calendar.MONTH))) ||
								((selectedDay.get(Calendar.YEAR) >= today.get(Calendar.YEAR)) && (selectedDay.get(Calendar.MONTH) >= today.get(Calendar.MONTH)) &&
																								(selectedDay.get(Calendar.DAY_OF_MONTH) < today.get(Calendar.DAY_OF_MONTH))))
								theView.setInfoLabel("INFO: You can not add a visit to the past!");
							else
							{
								//Check if visit time is available
								if (!(panelPatientsScheduleView.getTableValue(selectedRow, selectedColumn).equals("")))
									theView.setInfoLabel("INFO: This visit is already set for this time!");
								else
									{
									//Ask if you want to add this visit
									int confirm = JOptionPane.showConfirmDialog(
											theView, "Are you sure you want add visit at " + panelPatientsScheduleView.getSelectedTime() + " o'clock?", "Confirmation", JOptionPane.YES_NO_OPTION);
									if (confirm == JOptionPane.YES_OPTION)
									{
										//Create key for selected date
										String key = panelPatientsScheduleView.getSelectedDate();
										
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
											
											if (doctorInAdjacentCell.equals(panelPatientsScheduleView.getSelectedDoctor()) || patientIdInAdjacentCell == panelPatientsScheduleView.getIdNumber())
												theView.setInfoLabel("INFO: Doctor/patient is already assigned to the other room at the same time. Adding the visit failed!");
											else
											{
												ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(selectedRow, selectedColumn);
												cell.setPatient(panelPatientsScheduleView.getSelectedPatient());
												cell.setDoctor(panelPatientsScheduleView.getSelectedDoctor());
												cell.setPatientId(panelPatientsScheduleView.getIdNumber());
												
												theView.setInfoLabel("INFO: The visit has been added!");
												
												//Add visit to patient object
												Patient patient = theModel.getPatientById(panelPatientsScheduleView.getIdNumber());
												Appointment appointment = new Appointment(key, panelPatientsScheduleView.getHeaderTitle(selectedRow), panelPatientsScheduleView.getSelectedDoctor());
												patient.addAppointment(appointment);
												theModel.savePatientList();
												
												//Set value in table
												String value = "<html><center>" + panelPatientsScheduleView.getSelectedPatient() + "<br>("
																+ panelPatientsScheduleView.getSelectedDoctor() + ")</center></html>";
												panelPatientsScheduleView.setTableValue(value, selectedRow, selectedColumn);
												
												//Save scheduleMap to file
												theModel.saveScheduleMap();	
											}
										}
										else //Day is not on map
										{
											ScheduleDay newDay = new ScheduleDay(panelPatientsScheduleView.getDate().get(Calendar.YEAR),
													panelPatientsScheduleView.getDate().get(Calendar.MONTH),
													panelPatientsScheduleView.getDate().get(Calendar.DAY_OF_MONTH));
											ScheduleCellData cell = newDay.getScheduleCellDataObject(selectedRow, selectedColumn);
											cell.setPatient(panelPatientsScheduleView.getSelectedPatient());
											cell.setDoctor(panelPatientsScheduleView.getSelectedDoctor());
											cell.setPatientId(panelPatientsScheduleView.getIdNumber());
											theModel.addDayToScheduleMap(key, newDay);	
											
											theView.setInfoLabel("INFO: The visit has been added!");
											
											//Add visit to patient object
											Patient patient = theModel.getPatientById(panelPatientsScheduleView.getIdNumber());
											Appointment appointment = new Appointment(key, panelPatientsScheduleView.getHeaderTitle(selectedRow), panelPatientsScheduleView.getSelectedDoctor());
											patient.addAppointment(appointment);
											theModel.savePatientList();
											
											//Set value in table
											String value = "<html><center>" + panelPatientsScheduleView.getSelectedPatient() + "<br>("
															+ panelPatientsScheduleView.getSelectedDoctor() + ")</center></html>";
											panelPatientsScheduleView.setTableValue(value, selectedRow, selectedColumn);
											
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
		panelPatientsScheduleView.addActionListenerToDeleteVisitButton(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Get selected row and column numbers from table
				int selectedRow = panelPatientsScheduleView.getSelectedRow();
				int selectedColumn = panelPatientsScheduleView.getSelectedColumn();
				
				//Check if any row is selected
				if (selectedRow == -1 || selectedColumn == -1)
					theView.setInfoLabel("INFO: Please select cell before pressing \"Delete visit\" button!");
				else 
				{
					//Check if cell is not clear
					if(panelPatientsScheduleView.getTableValue(selectedRow, selectedColumn) == "")
						theView.setInfoLabel("INFO: This visit time is still avaliable!");
					else
					{
						//Ask if you want to delete this visit
						int confirm = JOptionPane.showConfirmDialog(
								theView, "Are you sure you want delete this visit?", "Confirmation", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION)
						{	
							//Create key for selected date
							String key = panelPatientsScheduleView.getSelectedDate();
							
							//Get schedule day (after checking if cell is not clear, the day must exists in scheduleMap, so it can't return NULL)
							ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(selectedRow, selectedColumn);
							
							//Delete visit in patient object
							Patient patient = theModel.getPatientById(cell.getPatientId());
							patient.removeAppointment(key, panelPatientsScheduleView.getHeaderTitle(selectedRow));
							theModel.savePatientList();
							
							//Clear cell data
							cell.setPatient("");
							cell.setDoctor("");
							cell.setPatientId(0);
							theView.setInfoLabel("INFO: The visit has been deleted!");
							
							//Set value in table
							panelPatientsScheduleView.setTableValue("", selectedRow, selectedColumn);
							
							//Save scheduleMap to file
							theModel.saveScheduleMap();
						}
					}	
				}					
			}
		});		
		
		//Change listener for JDatePicker
		panelPatientsScheduleView.addDateChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent event) 
			{
				Calendar cal = panelPatientsScheduleView.getDate();
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				panelPatientsScheduleView.setDateField(formatter.format(cal.getTime()));
				
				//LOAD TABLE
				//Create key for selected date
				String key = panelPatientsScheduleView.getSelectedDate();
			
				//Get schedule day (it can return NULL if day is not in scheduleMap)
				ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
				
				//Check if day is on map
				if (dayFromMap != null) //Day is already on map
				{
					for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
						for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
						{
							//Set value in table
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
							if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
								panelPatientsScheduleView.setTableValue("", i, j);
							else
							{	
							String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
							panelPatientsScheduleView.setTableValue(value, i, j);
							}
						}		
				}
				else
				{
					for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
						for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
						{
							//Set value in table
							panelPatientsScheduleView.setTableValue("", i, j);
						}	
				}
				
			}
		});
		
		//ActionListener for Next Day button
		panelPatientsScheduleView.addActionListenerToNextDayButton(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = panelPatientsScheduleView.getDate();
				cal.add(Calendar.DAY_OF_MONTH,1);
				panelPatientsScheduleView.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				
				//LOAD TABLE
				//Create key for selected date
				String key = panelPatientsScheduleView.getSelectedDate();
			
				//Get schedule day (it can return NULL if day is not in scheduleMap)
				ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
				
				//Check if day is on map
				if (dayFromMap != null) //Day is already on map
				{
					for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
						for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
						{
							//Set value in table
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
							if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
								panelPatientsScheduleView.setTableValue("", i, j);
							else
							{	
							String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
							panelPatientsScheduleView.setTableValue(value, i, j);
							}
						}		
				}
				else
				{
					for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
						for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
						{
							//Set value in table
							panelPatientsScheduleView.setTableValue("", i, j);
						}	
				}
			}
		});
		
		//ActionListener for Previous Day button
		panelPatientsScheduleView.addActionListenerToPreviousDayButton(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = panelPatientsScheduleView.getDate();
				cal.add(Calendar.DAY_OF_MONTH,-1);
				panelPatientsScheduleView.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				
				//LOAD TABLE
				//Create key for selected date
				String key = panelPatientsScheduleView.getSelectedDate();
			
				//Get schedule day (it can return NULL if day is not in scheduleMap)
				ScheduleDay dayFromMap = theModel.getScheduleDayFromScheduleMap(key);
				
				//Check if day is on map
				if (dayFromMap != null) //Day is already on map
				{
					for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
						for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
						{
							//Set value in table
							ScheduleCellData cell = dayFromMap.getScheduleCellDataObject(i, j);
							if ((cell.getPatient().equals("") && cell.getDoctor().equals("")))
								panelPatientsScheduleView.setTableValue("", i, j);
							else
							{	
							String value = "<html><center>" + cell.getPatient() + "<br>(" + cell.getDoctor() + ")</center></html>";
							panelPatientsScheduleView.setTableValue(value, i, j);
							}
						}		
				}
				else
				{
					for (int i = 0; i< panelPatientsScheduleView.getTableRowCount() ; i++)
						for (int j = 0; j< panelPatientsScheduleView.getTableColumnCount() ; j++)
						{
							//Set value in table
							panelPatientsScheduleView.setTableValue("", i, j);
						}	
				}
			}
		});
		
		//ListSelectionListener - selecting cells in schedule table
		panelPatientsScheduleView.addActionListenerToCellSelectionModel(new ListSelectionListener()
		{
			
			@Override
			public void valueChanged(ListSelectionEvent e) 
			{	
		        int selectedRow = panelPatientsScheduleView.getSelectedRow();
		        int selectedColumn = panelPatientsScheduleView.getSelectedColumn();
		        panelPatientsScheduleView.setTimeField(panelPatientsScheduleView.getHeaderTitle(selectedRow));
			}
		});
	}
}
