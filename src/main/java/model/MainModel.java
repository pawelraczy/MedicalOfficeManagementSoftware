package model;

import domain.Employee;
import domain.Patient;
import domain.ScheduleDay;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


/**
 * This is the main model for Small Medical Office Management software.
 */


public class MainModel {

    private final String ID_FILE_NAME = "idFile.dat";
    private final String PATIENTS_LIST_FILE_NAME = "PatientsListFile.dat";
    private final String EMPLOYEE_LIST_FILE_NAME = "EmployeeListFile.dat";
    private final String SCHEDULE_MAP_FILE_NAME = "ScheduleMapFile.dat";

    private final String settingsFolderPath = createSettingsFolderPath();
    private final String idFilePath = createDataFilePath(settingsFolderPath, ID_FILE_NAME);
    private final String patientsListFilePath = createDataFilePath(settingsFolderPath, PATIENTS_LIST_FILE_NAME);
    private final String employeeListFilePath = createDataFilePath(settingsFolderPath, EMPLOYEE_LIST_FILE_NAME);
    private final String scheduleMapFilePath = createDataFilePath(settingsFolderPath, SCHEDULE_MAP_FILE_NAME);

    private List<Patient> patientsList = new ArrayList<Patient>();
    private List<Employee> employeeList = new ArrayList<Employee>();
    private Map<String, ScheduleDay> scheduleMap = new HashMap<String, ScheduleDay>();
    private int id = 1;

    public MainModel() {
        loadLastIdNumber();
        loadPatientsList();
        loadEmployeeList();
        loadScheduleMap();
    }

    private String createSettingsFolderPath() {
        String configPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + File.separator + "MedicalOffice_ManagementSoftware";
        createSettingsFolderIfNotExists(configPath);
        return configPath;
    }

    private void createSettingsFolderIfNotExists(String configPath) {
        File file = new File(configPath);
        if (!file.exists())
            file.mkdir();
    }

    private String createDataFilePath(String settingsFolderPath, String dataFileName) {
        return settingsFolderPath + File.separator + dataFileName;
    }

    private final void loadLastIdNumber() {
        if (isFileExists(idFilePath)) {
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(idFilePath));
                id = in.readInt();
                in.close();
                System.out.println("Id number has been loaded");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final void loadPatientsList() {
        if (isFileExists(patientsListFilePath)) {
            openFileAndLoadObjectToClassField("patientsList", patientsListFilePath);
        }
    }

    private final void loadEmployeeList() {
        if (isFileExists(employeeListFilePath)) {
            openFileAndLoadObjectToClassField("employeeList", employeeListFilePath);
        }
    }

    private final void loadScheduleMap() {
        if (isFileExists(scheduleMapFilePath)) {
            openFileAndLoadObjectToClassField("scheduleMap", scheduleMapFilePath);
        }
    }

    private boolean isFileExists(String dataFilePath) {
        return new File(dataFilePath).isFile();
    }

    private void openFileAndLoadObjectToClassField(String classFieldName, String filePath) {
        try {
            Field field = getClass().getDeclaredField(classFieldName);
            field.setAccessible(true);
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            field.set(this, ois.readObject());
            System.out.println(classFieldName + " has been loaded");
            ois.close();
            fis.close();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            System.out.println("No such field!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Illegal access!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found!");
            e.printStackTrace();
        }
    }
    
    //--------------->>>Patients<<<---------------
    public void savePatientList() {
        Collections.sort(patientsList);
        saveObjectToFile("patientsList", patientsListFilePath);
    }
    public void addPatient(Patient patient) {
        if (!isPatientExists(patient)) {
            patientsList.add(patient);
        }
    }

    public boolean isPatientExists(Patient patient) {
        boolean exists = false;
        for (Patient p : patientsList)
            if (p.equals(patient)) {
                exists = true;
                break;
            }
        return exists;
    }

    public boolean removePatient(int id) {
        return patientsList.removeIf(p -> p.getId() == id);
    }

    public void editPatient(Patient patient) {
        for (Patient p : patientsList) {
            if (p.equals(patient)) {
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

    public List<Patient> getPatientList() {
        return patientsList;
    }
    
    /**
     * Get patient with selected id
     * @param id if of patient you want to get from the patientsList
     * @return if patient with selected if exists on the list it returns Patient object, if not it returns NULL!
     */
    public Patient getPatientById(int id) {
        Patient patient = null;

        for (Patient p : patientsList)
            if (id == p.getId()) {
                patient = p;
                break;
            }
        return patient;
    }

    public List<Patient> getPatientsByText(String text) {
        List<Patient> searchResults = new ArrayList<Patient>();
        for (Patient p : patientsList)
            if (p.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                    p.getLastName().toLowerCase().contains(text.toLowerCase()) ||
                    p.getPersonalIdentityNumber().toLowerCase().contains(text.toLowerCase()))
                searchResults.add(p);
        return searchResults;
    }
    

    //--------------->>>Employees<<<---------------
    public void saveEmployeeList() {
        Collections.sort(employeeList);
        saveObjectToFile("employeeList", employeeListFilePath);
    }

    public void sortEmployeeList() {
        Collections.sort(employeeList);
    }

    public void addEmployeeToList(Employee employee) {
        if (!isEmployeeExists(employee)){
            employeeList.add(employee);
        }
    }
    
    public boolean isEmployeeExists(Employee employee) {
        boolean exists = false;
        for (Employee e : employeeList)
            if (e.equals(employee)) {
                exists = true;
                break;
            }
        return exists;
    }
    
    public boolean removeEmployee(int id) {
        return employeeList.removeIf(e -> e.getId() == id);
    }
    
    public void editEmployee(Employee employee) {
        for (Employee e : employeeList) {
            if (e.equals(employee)) {
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
    
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * Get employee with selected id
     *
     * @param id if of employee you want to get from the employeeList
     * @return if employee with selected if exists on the list it returns his domain.Employee object, if not it returns null!
     */
    public Employee getEmployeeById(int id) {
        Employee employee = null;

        for (Employee e : employeeList)
            if (id == e.getId()) {
                employee = e;
                break;
            }
        return employee;
    }

    public List<Employee> getEmployeesByText(String text) {
        List<Employee> searchResults = new ArrayList<Employee>();
        for (Employee e : employeeList)
            if (e.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                    e.getLastName().toLowerCase().contains(text.toLowerCase()) ||
                    e.getPersonalIdentityNumber().toLowerCase().contains(text.toLowerCase()))
                searchResults.add(e);
        return searchResults;
    }

    public List<Employee> getDoctorsList() {
        List<Employee> searchResults = new ArrayList<Employee>();
        for (Employee e : employeeList)
            if (e.getJob().toLowerCase().equals("doctor"))
                searchResults.add(e);
        return searchResults;
    }


    //--------------->>>ID<<<---------------
    public void incrementAndSaveId() {
        try {
            id++;
            DataOutputStream out = new DataOutputStream(new FileOutputStream(idFilePath));
            out.writeInt(id);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    //--------------->>>Schedule<<<---------------
    public void saveScheduleMap() {
       saveObjectToFile("scheduleMap", scheduleMapFilePath);
    }
    
    public void addDayToScheduleMap(String dateStringKey, ScheduleDay scheduleDay) {
        scheduleMap.put(dateStringKey, scheduleDay);
    }
    
    public ScheduleDay getScheduleDayFromScheduleMap(String dateStringKey) {
        return scheduleMap.get(dateStringKey);
    }

    private void saveObjectToFile(String objectName, String filePath) {
        try {
            Field field = getClass().getDeclaredField(objectName);
            field.setAccessible(true);
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(field.get(this));
            oos.close();
            fos.close();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            System.out.println("No such field!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Illegal access!");
        }
    }
}
