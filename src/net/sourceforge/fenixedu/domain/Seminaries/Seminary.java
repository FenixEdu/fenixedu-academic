/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Calendar;
import java.util.List;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Seminary extends Seminary_Base {
    private Calendar enrollmentBeginDate;
    private Calendar enrollmentBeginTime;
    private Calendar enrollmentEndDate;
    private Calendar enrollmentEndTime;

    public Seminary() {
    }

    public Seminary(String name, String description, List equivalencies) {
        this.setDescription(description);
        this.setName(name);
        this.setEquivalencies(equivalencies);
    }

    public Seminary(String name, String description) {
        this.setDescription(description);
        this.setName(name);
    }

    public String toString() {
        String result;
        result = "[Seminary:";
        result += "ID=" + this.getIdInternal();
        result += ",Name=" + this.getName();
        result += ",Description=" + this.getDescription();
        result += ",Allowed Candidacies Per Student=" + this.getAllowedCandidaciesPerStudent();
        result += ",Modalities=" + this.getEquivalencies() + "]";
        return result;
    }

    /**
     * true if the names are equals
     */
    public boolean equals(Object obj) {
        boolean equalsResult = false;
        if (obj instanceof ISeminary) {
            ISeminary seminary = (ISeminary) obj;
            if (seminary.getName() == null)
                equalsResult = (this.getName() == null);
            else
                equalsResult = this.getName().equalsIgnoreCase(seminary.getName());
        }
        return equalsResult;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDate() {
        return enrollmentBeginDate;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        return enrollmentBeginTime;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDate() {
        return enrollmentEndDate;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        return enrollmentEndTime;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDate(Calendar calendar) {
        enrollmentBeginDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        enrollmentBeginTime = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDate(Calendar calendar) {
        enrollmentEndDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        enrollmentEndTime = calendar;
    }

}