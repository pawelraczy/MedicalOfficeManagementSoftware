package domain;

import java.io.Serializable;

/**
 * This is the class, which represents cells in Patients schedule table (each cell is a separate object).
 */
public class ScheduleCellData implements Serializable {
    private int row;
    private int column;
    private String patient;
    private int patientId;
    private String doctor;

    public ScheduleCellData(int row, int column) {
        this.row = row;
        this.column = column;
        this.patient = "";
        this.patientId = 0;
        this.doctor = "";
    }

    @Override
    public String toString() {
        return "[row = " + row +
                ", column = " + column +
                ", patient = " + patient +
                ", doctor = " + doctor + "]\n";
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getPatient() {
        return patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public int getPatientId() {
        return patientId;
    }
}