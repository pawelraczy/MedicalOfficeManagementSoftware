import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;


/**
 * This is the main view for Small Medical Office Management software.
 */


public class MedicalOfficeView extends JFrame
{
	private PanelAddPatient panelAddPatient = new PanelAddPatient();
	private PanelPatientsList panelPatientsList = new PanelPatientsList();
	private PanelAddEmployee panelAddEmployee = new PanelAddEmployee();
	private PanelEmployeeList panelEmployeeList = new PanelEmployeeList();
	private PanelBlacklist panelBlacklist = new PanelBlacklist();
	private PanelPatientsSchedule panelPatientsSchedule  = new PanelPatientsSchedule();
	private JPanel switchingPanel = panelPatientsSchedule;
	private JMenu patients;
	private JMenu personnel;
	private JMenu calendar;
	private JMenuItem exit;
	private JMenuItem addPatient;
	private JMenuItem patientsList;
	private JMenuItem blackList;
	private JMenuItem addEmployee;
	private JMenuItem employeesList;
	private JMenuItem patientsSchedule;
	private JMenuItem about;
	private JToolBar shortcutToolBar;
	private JButton addPatientButton;
	private JButton patientListButton;
	private JButton patientsScheduleButton;
	private JLabel infoLabel;
	
	
	public MedicalOfficeView() 
	{
		//MenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		patients = new JMenu("Patients");
		personnel = new JMenu("Personnel");
		calendar = new JMenu("Calendar");
		JMenu help = new JMenu("Help");
		menuBar.add(file);
		menuBar.add(patients);
		menuBar.add(personnel);
		menuBar.add(calendar);
		menuBar.add(help);
		
		exit = new JMenuItem("Exit");
		file.add(exit);
		
		
		addPatient = new JMenuItem("Add patient");
		patientsList = new JMenuItem("Patients list");
		blackList = new JMenuItem("Black list");
		patients.add(addPatient);
		patients.add(patientsList);
		patients.add(blackList);
		
		addEmployee = new JMenuItem("Add employee");
		employeesList = new JMenuItem("Employees list");
		personnel.add(addEmployee);
		personnel.add(employeesList);
		
		patientsSchedule = new JMenuItem("Patients schedule");
		calendar.add(patientsSchedule);
		
		about = new JMenuItem("About program");
		help.add(about);

		
		//Shortcut buttons on top
		shortcutToolBar = new JToolBar();
		shortcutToolBar.setBorder(BorderFactory.createRaisedBevelBorder());
		shortcutToolBar.setFloatable(false);
		shortcutToolBar.setLayout(new GridLayout(1, 4));
		shortcutToolBar.setBackground(Color.GRAY);

		addPatientButton = new JButton("ADD PATIENT");
		patientListButton = new JButton("PATIENTS LIST");

		patientsScheduleButton = new JButton("PATIENTS SCHEDULE");
		shortcutButtonsSettings(addPatientButton);
		shortcutButtonsSettings(patientListButton);
		shortcutButtonsSettings(patientsScheduleButton);
		
		
		//Info JLabel on the bottom
		infoLabel = new JLabel("INFO: ");
		infoLabel.setForeground(Color.BLUE);
		infoLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		
		
		//Setting icons for JMenuItems and shortcuts		
		URL imgURL = MedicalOfficeView.class.getResource("images/addPatientSmall.jpg");
		addPatient.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/patientsListSmall.jpg");
		patientsList.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/blackListSmall.jpg");
		blackList.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/addEmployeeSmall.jpg");
		addEmployee.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/employeeListSmall.jpg");
		employeesList.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/patientsScheduleSmall.jpg");
		patientsSchedule.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/about.jpg");
		about.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/addPatientSmall.jpg");
		addPatientButton.setIcon(new ImageIcon(imgURL));
		
		imgURL = MedicalOfficeView.class.getResource("images/patientsListSmall.jpg");
		patientListButton.setIcon(new ImageIcon(imgURL));

		imgURL = MedicalOfficeView.class.getResource("images/patientsScheduleSmall.jpg");
		patientsScheduleButton.setIcon(new ImageIcon(imgURL));
		
		
		//Frame settings, adding panels, menu etc.
		this.setLayout(new BorderLayout());
		this.setTitle("Medical Office Management Program");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				int selectedOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirmation request",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (selectedOption == JOptionPane.OK_OPTION) System.exit(0);
			}
		});	
		this.setSize(1000, 750);

		
		//Adding panels and menu
		this.setJMenuBar(menuBar);
		this.add(shortcutToolBar, BorderLayout.NORTH);
		this.add(infoLabel, BorderLayout.SOUTH);
		switchingPanel = panelPatientsSchedule;
		this.add(switchingPanel, BorderLayout.CENTER);
	}
	
	
	
	
	
	/**
	 * Settings for shortcut Button and add it to the ToolBar
	 * @param button reference to initiated JButton object
	 * @param name should be the same like JMenuItem name to the same JPanel
	*/
	public void shortcutButtonsSettings(JButton button)
	{
		button.setBackground(Color.LIGHT_GRAY);
		button.setForeground(Color.BLACK);
		button.setFont(new Font("Serif", Font.BOLD, 13));
		shortcutToolBar.add(button);
	}
	
	//AddActionListener methods
	public void addActionListenerToExitMenu (ActionListener listener)
	{
		exit.addActionListener(listener);
	}
	
	public void addActionListenerToAddPatientMenu (ActionListener listener)
	{
		addPatient.addActionListener(listener);
	}
	
	public void addActionListenerToBlackListMenu (ActionListener listener)
	{
		blackList.addActionListener(listener);
	}
	
	public void addActionListenerToPatientsListMenu (ActionListener listener)
	{
		patientsList.addActionListener(listener);
	}
	
	public void addActionListenerToAddEmployeeMenu (ActionListener listener)
	{
		addEmployee.addActionListener(listener);
	}
	
	public void addActionListenerToEmployeesListMenu (ActionListener listener)
	{
		employeesList.addActionListener(listener);
	}
	
	public void addActionListenerToPatientsScheduleMenu (ActionListener listener)
	{
		patientsSchedule.addActionListener(listener);
	}
	
	
	public void addActionListenerToAboutMenu (ActionListener listener)
	{
		about.addActionListener(listener);
	}
	
	public void addActionListenerToAddPatientButton (ActionListener listener)
	{
		addPatientButton.addActionListener(listener);
	}
	
	public void addActionListenerToPatientListButton (ActionListener listener)
	{
		patientListButton.addActionListener(listener);
	}
	
	public void addActionListenerToPatientsScheduleButton (ActionListener listener)
	{
		patientsScheduleButton.addActionListener(listener);
	}
	
	
	//Get text from Components
	public PanelAddPatient getPanelAddPatient()
	{
		return panelAddPatient;
	}
	
	public PanelPatientsList getPanelPatientsList()
	{
		return panelPatientsList;
	}
	
	public PanelBlacklist getPanelBlacklist()
	{
		return panelBlacklist;
	}
	
	public PanelAddEmployee getPanelAddEmployee()
	{
		return panelAddEmployee;
	}
	public PanelEmployeeList getPanelEmployeeList()
	{
		return panelEmployeeList;
	}
	
	public PanelPatientsSchedule getPanelPatientsSchedule()
	{
		return panelPatientsSchedule;
	}

	
	/**
	 * Loading one of previously initiated JPanels.
	 * @param panel the panel you want to load
	*/
	public void setSwitchingPanel(JPanel panel)
	{
		try
		{
			this.remove(switchingPanel);
			switchingPanel = panel;
			this.add(switchingPanel, BorderLayout.CENTER);
			this.revalidate();
			this.repaint();
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}	
	}

		
	/**
	 * Display message dialog with about program information
	*/
	public void displayAboutMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "About program", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("doctor.jpg"));
	}
	
	
	/**
	 * Set message on info label on the bottom.
	 * @param message text you want to set on infoLabel
	*/

	public void setInfoLabel (String message)
	{
		infoLabel.setText(message);
	}
	
	
	/**
	 * Turn on/off menu and shortcut buttons
	 * @param value true (menu enabled) or false (menu disabled)
	*/
	public void isMenuEnable (boolean value)
	{
		if(value==false)
		{	
			patients.setEnabled(false);
			personnel.setEnabled(false);
			calendar.setEnabled(false);
			addPatientButton.setEnabled(false);
			patientListButton.setEnabled(false);
			patientsScheduleButton.setEnabled(false);
		}
		else
		{
			patients.setEnabled(true);
			personnel.setEnabled(true);
			calendar.setEnabled(true);
			addPatientButton.setEnabled(true);
			patientListButton.setEnabled(true);
			patientsScheduleButton.setEnabled(true);
		}
	}
}
