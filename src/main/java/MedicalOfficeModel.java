import javax.swing.*;
import java.io.*;
import java.util.*;


/**
 * This is the main model for Small Medical Office Management software.
 */


public class MedicalOfficeModel
{

	private  List<Patient> patientsList = new ArrayList<Patient>();
	private  List<Employee> employeeList = new ArrayList<Employee>();
	private  Map<String, ScheduleDay> scheduleMap = new HashMap<String, ScheduleDay>();
	private int id = 1;
	private String patientsListFilePath;
	private String idFilePath;
	private String employeeListFilePath;
	private String scheduleMapFilePath;
	
	
	public MedicalOfficeModel()
	{
		//Check if settings folder exists
		String configPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + File.separator + "MedicalOffice_ManagementSoftware";
		File file = new File(configPath);
		if (!file.exists()) 
			file.mkdir();
		
		//Load ArrayList with patients from file
		patientsListFilePath = configPath + File.separator + "PatientsListFile.dat";
		if (new File(patientsListFilePath).isFile())
		{
			try 
			{
				FileInputStream fISPatients = new FileInputStream(patientsListFilePath);
				ObjectInputStream oISPatients = new ObjectInputStream(fISPatients);   
				patientsList = (ArrayList<Patient>)oISPatients.readObject();
				oISPatients.close(); 
				fISPatients.close(); 
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}		
			catch(ClassNotFoundException e)
			{
				System.out.println("Class not found");
				e.printStackTrace();
			}
		}
	
		//Load actual ID number from file
		idFilePath = configPath + File.separator + "idFile.dat";
		if (new File(idFilePath).isFile())
		{
			try 
			{
				DataInputStream in = new DataInputStream(new FileInputStream(idFilePath));
				id = in.readInt();
				in.close();
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}	
		}
		
		//Load ArrayList with employees from file
		employeeListFilePath = configPath + File.separator + "EmployeeListFile.dat";
		if (new File(employeeListFilePath).isFile())
		{
			try 
			{
				FileInputStream fISEmployee = new FileInputStream(employeeListFilePath);
				ObjectInputStream oISEmployee = new ObjectInputStream(fISEmployee);   
				employeeList = (ArrayList<Employee>)oISEmployee.readObject();
				oISEmployee.close(); 
				fISEmployee.close(); 
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}		
			catch(ClassNotFoundException e)
			{
				System.out.println("Class not found");
				e.printStackTrace();
			}
		}
		
		//Load Patients Schedule to memory from file
		scheduleMapFilePath = configPath + File.separator + "ScheduleMapFile.dat";
		if (new File(scheduleMapFilePath).isFile())
		{
			try 
			{
				FileInputStream fISSchedule = new FileInputStream(scheduleMapFilePath);
				ObjectInputStream oISSchedule = new ObjectInputStream(fISSchedule);   
				scheduleMap = (HashMap<String, ScheduleDay>)oISSchedule.readObject();
				oISSchedule.close(); 
				fISSchedule.close(); 
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}		
			catch(ClassNotFoundException e)
			{
				System.out.println("Class not found");
				e.printStackTrace();
			}
		}
		
	}

	
	//Methods for patientsList
	/**
	 * Saving patientsList to PatientsListFile.dat file.
	 */
	public void savePatientList()
	{
		Collections.sort(patientsList);
		try 
		{
			FileOutputStream fOSPatients = new FileOutputStream(patientsListFilePath);
			ObjectOutputStream oOSPatients = new ObjectOutputStream(fOSPatients);   
			oOSPatients.writeObject(patientsList);
			oOSPatients.close(); 
			fOSPatients.close(); 
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Add patient to patientsList (it's not saving it to file in this method)
	 * Before adding patient to list, you must check if patient already exists in the patientsList or blackList!
	 * @param patient the patient which the method will add
	 */
	public void addPatientToList(Patient patient)
	{
		patientsList.add(patient);
	}
	
	/**
	 * Remove patient from patientsList (if it exists in this list)
	 * @param id of patient to be removed
	 * @return boolean value ("true" if patient has been removed or "false" if patient with selected id does not exists on the list)
	 */
	public boolean removePatient(int id)
	{
		return patientsList.removeIf(p -> p.getId() == id);
	}
	
	/**
	 * If patient is already on the patientsList, editing it using data from patient object)
	 * @param patient object with new data to replace
	 */
	public void editPatient(Patient patient)
	{
		for (Patient p : patientsList)
		{
			if (p.equals(patient))
			{
				p.setFirstName(patient.getFirstName());
				p.setLastName(patient.getLastName());
				p.setGender(patient.getGender());
				p.setDateOfBirth(patient.getDateOfBirth());
				p.setCity(patient.getCity());
				p.setPostCode(patient.getPostCode());
				p.setStreet(patient.getStreet());
				p.setHouseApartmentNumber(patient.getHouseApartmentNumber());
				p.setPhoneNumber(patient.getPhoneNumber());
				p.setEMail(patient.getEMail());
				p.setPersonalIdentityNumber(patient.getPersonalIdentityNumber());
				p.setAllergies(patient.getAllergies());
				p.setMedicalHistory(patient.getMedicalHistory());
				break;
			}
		}	
	}
	
	/**
	 * Get actual patientsList
	 */
	public List<Patient> getPatientList()
	{
		return patientsList;
	}
	
	/**
	 * Get patient with selected id
	 * @param id if of patient you want to get from the patientsList
	 * @return if patient with selected if exists on the list it returns his Patient object, if not it returns null!
	 */
	public Patient getPatient (int id)
	{
		Patient patient = null;
		
		for (Patient p : patientsList)
			if (id == p.getId())
			{
				patient = p;
				break;
			}
		return patient;
	}
	
	/**
	 * Check if patient already exists on the patientsList.
	 * @param patient the patient which you want to check
	 * @return boolean value ("true" if patient exists on the list or "false" if patient does not exists on the list)
	 */
	public boolean patientExists(Patient patient)
	{
		boolean exists = false;
		for (Patient p : patientsList)
			if (p.equals(patient))
			{
				exists = true;
				break;
			}
		return exists;
	}
	
	/**
	 * Search if patients on the patientsList, contains selected String in FirstName, LastName or PersonalIdentityNumber
	 * @param text the string you want to search
	 * @return list with patients that meet the search conditions
	 */
	public List<Patient> getSearchResults (String text)
	{
		List<Patient> searchResults = new ArrayList<Patient>();
		for (Patient p : patientsList)
			if (p.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
					p.getLastName().toLowerCase().contains(text.toLowerCase()) ||
					p.getPersonalIdentityNumber().toLowerCase().contains(text.toLowerCase()))
				searchResults.add(p);
		return searchResults;
	}
	
	
	//Methods for employeeList
	/**
	 * Saving employeeList to EmployeeListFile.dat file.
	 */
	public void saveEmployeeList()
	{
		Collections.sort(employeeList);
		try 
		{
			FileOutputStream fOSEmployee = new FileOutputStream(employeeListFilePath);
			ObjectOutputStream oOSEmployee = new ObjectOutputStream(fOSEmployee);   
			oOSEmployee.writeObject(employeeList);
			oOSEmployee.close(); 
			fOSEmployee.close(); 
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	/**
	* Sorting employeeList using Employee.equals method.
	*/
	public void sortEmployeeList()
	{
		Collections.sort(employeeList);
	}
		
		
	/**
	* Add employee to employeeList (it's not saving it to file in this method)
	* Before adding employee to list, you must check if employee already exists in the patientsList.
	* @param employee the employee which the method will add
	*/
	public void addEmployeeToList(Employee employee)
	{
		employeeList.add(employee);
	}
		
	/**
	* Remove employee from employeeList (if it exists in this list)
	* @param id of employee to be removed
	* @return boolean value ("true" if employee has been removed or "false" if employee with selected id does not exists on the list)
	*/
	public boolean removeEmployee(int id)
	{
		return employeeList.removeIf(e -> e.getId() == id);
	}
		
	/**
	* If employee is already on the employeeList, editing it using data from employee object)
	* @param employee object with new data to replace
	*/
	public void editEmployee(Employee employee)
	{
		for (Employee e : employeeList)
		{
			if (e.equals(employee))
			{
				e.setFirstName(employee.getFirstName());
				e.setLastName(employee.getLastName());
				e.setGender(employee.getGender());
				e.setDateOfBirth(employee.getDateOfBirth());
				e.setCity(employee.getCity());
				e.setPostCode(employee.getPostCode());
				e.setStreet(employee.getStreet());
				e.setHouseApartmentNumber(employee.getHouseApartmentNumber());
				e.setPhoneNumber(employee.getPhoneNumber());
				e.setEMail(employee.getEMail());
				e.setPersonalIdentityNumber(employee.getPersonalIdentityNumber());
				e.setBankAccountNumber(employee.getBankAccountNumber());
				e.setHireDate(employee.getHireDate());
				e.setJob(employee.getJob());
				break;
			}
		}
			
	}
			
	/**
	* Get actual employeeList
	*/
	public List<Employee> getEmployeeList()
	{
		return employeeList;
	}
		
	/**
	* Get employee with selected id
	* @param id if of employee you want to get from the employeeList
	* @return if employee with selected if exists on the list it returns his Employee object, if not it returns null!
	*/
	public Employee getEmployee (int id)
	{
		Employee employee = null;
		
		for (Employee e : employeeList)
			if (id == e.getId())
			{
				employee = e;
				break;
			}
		return employee;
	}
		
	/**
	* Check if employee already exists on the employeeList.
	* @param employee the employee which you want to check
	* @return boolean value ("true" if employee exists on the list or "false" if employee does not exists on the list)
	*/
	public boolean employeeExists(Employee employee)
	{
		boolean exists = false;
		for (Employee e : employeeList)
			if (e.equals(employee))
			{
				exists = true;
				break;
			}
		return exists;
	}
		
	/**
	* Search if employee on the employeeList, contains selected String in FirstName, LastName or PersonalIdentityNumber
	* @param text the string you want to search
	* @return list with employee that meet the search conditions
	*/
	public List<Employee> getSearchResultsEmployee (String text)
	{
		List<Employee> searchResults = new ArrayList<Employee>();
		for (Employee e : employeeList)
			if (e.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
				e.getLastName().toLowerCase().contains(text.toLowerCase()) ||
				e.getPersonalIdentityNumber().toLowerCase().contains(text.toLowerCase()))
				searchResults.add(e);
		return searchResults;
	}
	
	/**
	 * Get doctors list
	 * @return list with doctors (employees)
	 */
	public List<Employee> getDoctorsList()
	{
		List<Employee> searchResults = new ArrayList<Employee>();
		for (Employee e : employeeList)
			if (e.getJob().toLowerCase().equals("doctor"))
				searchResults.add(e);
		return searchResults;
	}
	
	//Methods for id
	/**
	 * Increment id value and save id to idFile.dat file.
	 */
	public void incrementAndSaveId()
	{
		try 
		{
			id++;
			DataOutputStream out = new DataOutputStream(new FileOutputStream(idFilePath));
			out.writeInt(id);
			out.close();
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Get new (unused) id. 
	 * You always must use incrementAndSaveId method after it!
	 */
	public int getId()
	{
		return id;
	}
	
	//Methods for scheduleMap
	/**
	 * Saving scheduleMap to ScheduleMapFile.dat file.
	 */
	public void saveScheduleMap()
	{
		try 
		{
			FileOutputStream fOSSchedule = new FileOutputStream(scheduleMapFilePath);
			ObjectOutputStream oOSSchedule = new ObjectOutputStream(fOSSchedule);   
			oOSSchedule.writeObject(scheduleMap);
			oOSSchedule.close(); 
			fOSSchedule.close(); 
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Add day to scheduleMap (it's not saving it to file in this method)
	 * @param key date from selectedDateField in PanelPatientsSchedule
	 * @param day the ScheduleDay which the method will add
	 */
	public void addDayToScheduleMap(String key, ScheduleDay day)
	{
		scheduleMap.put(key, day);	
	}
	
	/**
	 * Get ScheduleDay
	 * @param key date from selectedDateField in PanelPatientsSchedule
	 * @return if date exists on the list it returns ScheduleDay object, if not it returns null!
	*/
	public ScheduleDay getScheduleDayFromScheduleMap (String key)
	{
		return scheduleMap.get(key);
	}
}
