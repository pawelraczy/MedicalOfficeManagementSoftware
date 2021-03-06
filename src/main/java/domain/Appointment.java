package domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is the class, which represents appointments (each appointment is a separate object).
 */

public class Appointment implements Comparable<Appointment>, Serializable {
    private String date;
    private String time;
    private String doctor;

    public Appointment(String date, String time, String doctor) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "[date = " + date +
                ", time = " + time +
                ", doctor = " + doctor + "]\n";
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;

        Appointment otherVisit = (Appointment) otherObject;
        return Objects.equals(this.date, otherVisit.date)
                && Objects.equals(this.time, otherVisit.time);
    }

    @Override
    public int compareTo(Appointment other) {
        String thisDate = date + ", " + time;
        String otherDate = other.date + ", " + other.time;

        return thisDate.compareTo(otherDate);
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
