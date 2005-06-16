/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Calendar;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Seminary extends Seminary_Base {

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentBegin());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDate(Calendar calendar) {
        this.setEnrollmentBegin(calendar.getTime());
    }
    
    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentTimeBegin());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        this.setEnrollmentTimeBegin(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentEnd());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDate(Calendar calendar) {
        this.setEnrollmentEnd(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentTimeEnd());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        this.setEnrollmentTimeEnd(calendar.getTime());
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

}
