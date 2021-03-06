package domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * This is the class, which represents employees (each employee is a separate object).
 */

public class Employee implements Comparable<Employee>, Serializable {
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
    private String bankAccountNumber;
    private Calendar hireDate;
    private String job;

    public Employee(int id, String firstName, String lastName, String gender,
                    Calendar dateOfBirth, String city, String postCode, String street, String houseApartmentNumber,
                    String phoneNumber, String eMail, String personalIdentityNumber,
                    String bankAccountNumber, Calendar hireDate, String job) {
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
        this.bankAccountNumber = bankAccountNumber;
        this.hireDate = hireDate;
        this.job = job;
    }

    @Override
    public String toString() {
        String date = "";
        String hDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");

        if (dateOfBirth != null)
            date = formatter.format(dateOfBirth.getTime());
        if (hireDate != null)
            hDate = formatter.format(hireDate.getTime());

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
                ", bankAccountNumber = " + bankAccountNumber +
                ", hireDate =  " + hDate +
                ", job = " + job + "]\n";
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;

        Employee otherEmployee = (Employee) otherObject;
        if (this.id == otherEmployee.id) return true;
        if ((Objects.equals(this.personalIdentityNumber, otherEmployee.personalIdentityNumber)) && !(this.personalIdentityNumber.equals("")))
            return true;
        if (!(Objects.equals(this.personalIdentityNumber, otherEmployee.personalIdentityNumber)) && !(this.personalIdentityNumber.equals("")))
            return false;
        return Objects.equals(this.firstName.toUpperCase(), otherEmployee.firstName.toUpperCase())
                && Objects.equals(this.lastName.toUpperCase(), otherEmployee.lastName.toUpperCase())
                && Objects.equals(this.dateOfBirth.get(Calendar.YEAR), otherEmployee.dateOfBirth.get(Calendar.YEAR))
                && Objects.equals(this.dateOfBirth.get(Calendar.MONTH), otherEmployee.dateOfBirth.get(Calendar.MONTH))
                && Objects.equals(this.dateOfBirth.get(Calendar.DAY_OF_MONTH), otherEmployee.dateOfBirth.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int compareTo(Employee other) {
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

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public Calendar getHireDate() {
        return hireDate;
    }

    public String getJob() {
        return job;
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

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setHireDate(Calendar hireDate) {
        this.hireDate = hireDate;
    }

    public void setJob(String job) {
        this.job = job;
    }
}