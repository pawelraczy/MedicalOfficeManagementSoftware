package domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * This is the class, which represents visits (each visit is a separate object).
 */

public class VisitReport implements Comparable<VisitReport>, Serializable {
    private Calendar visitDate;
    private String visitReport;

    public VisitReport(Calendar visitDate, String visitReport) {
        this.visitDate = visitDate;
        this.visitReport = visitReport;
    }


    @Override
    public String toString() {
        String date = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, hh:mm");
        if (visitDate != null)
            date = formatter.format(visitDate.getTime());

        return "[visitDate = " + date +
                ", visitReport = " + visitReport + "]\n";
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;

        VisitReport otherVisit = (VisitReport) otherObject;
        return Objects.equals(this.visitDate.get(Calendar.YEAR), otherVisit.visitDate.get(Calendar.YEAR))
                && Objects.equals(this.visitDate.get(Calendar.MONTH), otherVisit.visitDate.get(Calendar.MONTH))
                && Objects.equals(this.visitDate.get(Calendar.DAY_OF_MONTH), otherVisit.visitDate.get(Calendar.DAY_OF_MONTH))
                && Objects.equals(this.visitDate.get(Calendar.HOUR_OF_DAY), otherVisit.visitDate.get(Calendar.HOUR_OF_DAY))
                && Objects.equals(this.visitDate.get(Calendar.MINUTE), otherVisit.visitDate.get(Calendar.MINUTE));
    }

    @Override
    public int compareTo(VisitReport other) {
        return visitDate.compareTo(other.visitDate);
    }

    public Calendar getVisitDate() {
        return visitDate;
    }

    public String getVisitReport() {
        return visitReport;
    }

    public void setVisitDate(Calendar visitDate) {
        this.visitDate = visitDate;
    }

    public void setVisitReport(String visitReport) {
        this.visitReport = visitReport;
    }
}
