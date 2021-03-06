package domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This is the class, which represents patients (each patient is a separate object).
 */

public class Patient implements Comparable<Patient>, Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private Calendar dateOfBirth;
    private String city;
    private String postCode;
    private String street;
    private String houseApartmentNumber;
    private String phoneNumber;
    private String eMail;
    private String personalIdentityNumber;
    private String allergies;
    private String medicalHistory;
    private List<VisitReport> visitReportList;
    private List<Appointment> appointmentsList;
    private boolean blacklistStatus;
    private String blacklistReason;

    public Patient(int id, String firstName, String lastName, String gender,
                   Calendar dateOfBirth, String city, String postCode, String street, String houseApartmentNumber,
                   String phoneNumber, String eMail, String personalIdentityNumber,
                   String allergies, String medicalHistory) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.postCode = postCode;
        this.street = street;
        this.houseApartmentNumber = houseApartmentNumber;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.personalIdentityNumber = personalIdentityNumber;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
        this.visitReportList = new ArrayList<VisitReport>();
        this.appointmentsList = new ArrayList<Appointment>();
        blacklistStatus = false;
        this.blacklistReason = "";

    }

    @Override
    public String toString() {
        String date = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (dateOfBirth != null)
            date = formatter.format(dateOfBirth.getTime());

        return "[firstName = " + firstName +
                ", lastName = " + lastName +
                ", gender = " + gender +
                ", dateOfBirth = " + date +
                ", city = " + city +
                ", postCode = " + postCode +
                ", street = " + street +
                ", houseApartmentNumber = " + houseApartmentNumber +
                ", phoneNumber = " + phoneNumber +
                ", eMail = " + eMail +
                ", personalIdentityNumber = " + personalIdentityNumber +
                ", allergies = " + allergies +
                ". medicalHistory =  " + medicalHistory +
                ", blackList = " + blacklistStatus +
                ", visitReportObject = " + visitReportList + "]\n";
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;

        Patient otherPatient = (Patient) otherObject;
        if (this.id == otherPatient.id) return true;
        if ((Objects.equals(this.personalIdentityNumber, otherPatient.personalIdentityNumber)) && !(this.personalIdentityNumber.equals("")))
            return true;
        if (!(Objects.equals(this.personalIdentityNumber, otherPatient.personalIdentityNumber)) && !(this.personalIdentityNumber.equals("")))
            return false;
        return Objects.equals(this.firstName.toUpperCase(), otherPatient.firstName.toUpperCase())
                && Objects.equals(this.lastName.toUpperCase(), otherPatient.lastName.toUpperCase())
                && Objects.equals(this.dateOfBirth.get(Calendar.YEAR), otherPatient.dateOfBirth.get(Calendar.YEAR))
                && Objects.equals(this.dateOfBirth.get(Calendar.MONTH), otherPatient.dateOfBirth.get(Calendar.MONTH))
                && Objects.equals(this.dateOfBirth.get(Calendar.DAY_OF_MONTH), otherPatient.dateOfBirth.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int compareTo(Patient other) {
        return lastName.compareTo(other.lastName);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseApartmentNumber() {
        return houseApartmentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEMail() {
        return eMail;
    }

    public String getPersonalIdentityNumber() {
        return personalIdentityNumber;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public boolean getBlacklistStatus() {
        return blacklistStatus;
    }

    public String getBlacklistReason() {
        return blacklistReason;
    }

    public List<VisitReport> getVisitReportList() {
        return visitReportList;
    }

    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseApartmentNumber(String houseApartmentNumber) {
        this.houseApartmentNumber = houseApartmentNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public void setPersonalIdentityNumber(String personalIdentityNumber) {
        this.personalIdentityNumber = personalIdentityNumber;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setBlackListStatus(boolean blacklistStatus) {
        this.blacklistStatus = blacklistStatus;
    }

    public void setBlacklistReason(String blacklistReason) {
        this.blacklistReason = blacklistReason;
    }

    public void addAppointment(Appointment appointment) {
        appointmentsList.add(appointment);
        Collections.sort(appointmentsList);
    }

    public void removeAppointment(String date, String time) {
        Iterator<Appointment> i = appointmentsList.iterator();
        while (i.hasNext()) {
            Appointment a = i.next();
            if (a.getDate().equals(date) && a.getTime().equals(time))
                i.remove();
        }
    }

    public void addVisitReport(VisitReport visitReport) {
        visitReportList.add(visitReport);
        Collections.sort(visitReportList);
    }

    public void editVisitReport(VisitReport newVisit, int indexOfVisitToBeEdited) {
        VisitReport visitFromList = visitReportList.get(indexOfVisitToBeEdited);
        visitFromList.setVisitDate(newVisit.getVisitDate());
        visitFromList.setVisitReport(newVisit.getVisitReport());
        Collections.sort(visitReportList);
    }
}