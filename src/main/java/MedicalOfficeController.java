import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the main controller for Small Medical Office Management software.
 */

public class MedicalOfficeController {

	private MedicalOfficeView theView;
	private MedicalOfficeModel theModel;
	
	public MedicalOfficeController(MedicalOfficeView atheView, MedicalOfficeModel atheModel)
	{
		theView = atheView;
		theModel = atheModel;
		
		//Shortcut Buttons ActionListeners
		AddPatientListener addPatientListener = new AddPatientListener();
		PatientsListListener patientsListListener = new PatientsListListener();
		PatientsScheduleListener patientsScheduleListener = new PatientsScheduleListener();
		theView.addActionListenerToAddPatientButton(addPatientListener);
		theView.addActionListenerToPatientListButton(patientsListListener);
		theView.addActionListenerToPatientsScheduleButton(patientsScheduleListener);

		
		//Menu ActionListeners
		theView.addActionListenerToExitMenu(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int selectedOption = JOptionPane.showConfirmDialog(theView, "Are you sure you want to exit?", "Confirmation request",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (selectedOption == JOptionPane.OK_OPTION) System.exit(0);
			}
		});
		
		theView.addActionListenerToAddPatientMenu(addPatientListener);
		theView.addActionListenerToPatientsListMenu(patientsListListener);
		theView.addActionListenerToBlackListMenu(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				theView.setInfoLabel("INFO:");
				theView.setSwitchingPanel(theView.getPanelBlacklist());
			}
		});
		
		theView.addActionListenerToAddEmployeeMenu(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				theView.setInfoLabel("INFO:");
				theView.setSwitchingPanel(theView.getPanelAddEmployee());
				theView.isMenuEnable(false);
				theView.getPanelAddEmployee().setIdNumber(theModel.getId());
			}
		});
		
		theView.addActionListenerToEmployeesListMenu(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				theView.setInfoLabel("INFO:");
				theView.setSwitchingPanel(theView.getPanelEmployeeList());
			}
		});
		
		theView.addActionListenerToPatientsScheduleMenu(patientsScheduleListener);
		
		theView.addActionListenerToAboutMenu(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Message in about window
				String message = "<html><b>Medical Office Management Software</b><br><br>"
						+ "Version: 1.01<br><br>"
						+ "<center>Copyright \u00a9 2015 Pawe� R�czy</center></html>";
				//Display about window
				theView.displayAboutMessage(message);
			}
		});	
		
		
		//Controller for PanelAddPatient
		ControllerPanelAddPatient controllerPanelAddPatient= new ControllerPanelAddPatient(theView, theModel);
		//Controller for PanelPatientsList
		ControllerPanelPatientsList controllerPanelPatientsList= new ControllerPanelPatientsList(theView, theModel);
		//Controller for PanelBlacklist
		ControllerPanelBlacklist controllerPanelBlacklist= new ControllerPanelBlacklist(theView, theModel);
		//Controller for PanelAddEmployee
		ControllerPanelAddEmployee controllerPanelAddEmployee= new ControllerPanelAddEmployee(theView, theModel);
		//Controller for PanelEmployeeList
		ControllerPanelEmployeeList controllerPanelEmployeeList= new ControllerPanelEmployeeList(theView, theModel);
		//Controller for PanelPatientsSchedule
		ControllerPanelPatientsSchedule controllerPanelPatientsSchedule = new ControllerPanelPatientsSchedule(theView, theModel);
	}
	
	
	
	
	
	class AddPatientListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			theView.setInfoLabel("INFO:");
			theView.setSwitchingPanel(theView.getPanelAddPatient());
			theView.isMenuEnable(false);
			theView.getPanelAddPatient().setIdNumber(theModel.getId());
		}
	}

	
	class PatientsListListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			theView.setInfoLabel("INFO:");
			theView.setSwitchingPanel(theView.getPanelPatientsList());
		}
	}
	
	
	class PatientsScheduleListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			theView.setInfoLabel("INFO:");
			theView.setSwitchingPanel(theView.getPanelPatientsSchedule());
		}
	}
}		