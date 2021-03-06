package domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


/**
 * This is the class, which represents days in Patients schedule (each day is a separate object).
 */

public class ScheduleDay implements Comparable<ScheduleDay>, Serializable {
    private Calendar date;
    private List<ScheduleCellData> scheduleList;

    public ScheduleDay(int year, int month, int day) {
        date = Calendar.getInstance();
        date.set(year, month, day, 0, 0, 0);
        scheduleList = new ArrayList<ScheduleCellData>();

        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 2; j++) {
                scheduleList.add(new ScheduleCellData(i, j));
            }
    }

    @Override
    public String toString() {
        String dateAfterFormat = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null)
            dateAfterFormat = formatter.format(date.getTime());

        return "[date = " + dateAfterFormat +
                ", scheduleList = " + scheduleList + "]\n";
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;
        ScheduleDay otherDay = (ScheduleDay) otherObject;

        return Objects.equals(this.date.get(Calendar.YEAR), otherDay.date.get(Calendar.YEAR))
                && Objects.equals(this.date.get(Calendar.MONTH), otherDay.date.get(Calendar.MONTH))
                && Objects.equals(this.date.get(Calendar.DAY_OF_MONTH), otherDay.date.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int compareTo(ScheduleDay other) {
        return date.compareTo(other.date);
    }

    public Calendar getDate() {
        return date;
    }

    public List<ScheduleCellData> getScheduleList() {
        return scheduleList;
    }

    /**
     * @param row    selected row in Patients Schedule table
     * @param column selected column in Patients Schedule table
     * @return domain.ScheduleCellData object from scheduleList or NULL if cell does not exist!
     */
    public ScheduleCellData getScheduleCellDataObject(int row, int column) {
        ScheduleCellData cellData = null;
        for (ScheduleCellData s : scheduleList) {
            if (s.getRow() == row && s.getColumn() == column) {
                cellData = s;
                break;
            }
        }
        return cellData;
    }
}
