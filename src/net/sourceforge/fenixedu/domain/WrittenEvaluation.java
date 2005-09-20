/*
 * Created on 8/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author Ana e Ricardo
 */
public class WrittenEvaluation extends WrittenEvaluation_Base {

    public Calendar getBeginning() {
        if (this.getBeginningDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBeginningDate());
            return result;
        }
        return null;
    }

    public void setBeginning(Calendar calendar) {
        if (calendar != null) {
            this.setBeginningDate(calendar.getTime());
        } else {
            this.setBeginningDate(null);
        }
    }

    public Calendar getDay() {
        if (this.getDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getDayDate());
            return result;
        }
        return null;
    }

    public void setDay(Calendar calendar) {
        if (calendar != null) {
            this.setDayDate(calendar.getTime());
        } else {
            this.setDayDate(null);
        }
    }

    public Calendar getEnd() {
        if(this.getEndDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndDate());
            return result;
        }
        return null;
    }

    public void setEnd(Calendar calendar) {
        if (calendar != null) {
            this.setEndDate(calendar.getTime());    
        } else {
            this.setEndDate(null);
        }
    }

    public Calendar getEnrollmentBeginDay() {
        if (this.getEnrollmentBeginDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBeginDayDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentBeginDay(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBeginDayDate(calendar.getTime());    
        } else {
            this.setEnrollmentBeginDayDate(null);
        }
    }

    public Calendar getEnrollmentBeginTime() {
        if (this.getEnrollmentBeginTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBeginTimeDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentBeginTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBeginTimeDate(calendar.getTime());
        } else {
            this.setEnrollmentBeginTimeDate(null);
        }
    }

    public Calendar getEnrollmentEndDay() {
        if (this.getEnrollmentEndDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEndDayDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentEndDay(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentEndDayDate(calendar.getTime());
        } else {
            this.setEnrollmentEndDayDate(null);
        }
    }

    public Calendar getEnrollmentEndTime() {
        if(this.getEnrollmentEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEndTimeDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentEndTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentEndTimeDate(calendar.getTime());
        } else {
            this.setEnrollmentEndTimeDate(null);
        }
    }

    public String toString() {
        return "[WRITTEN EVALUATION:" + " id= '" + this.getIdInternal() + "'\n" + " day= '"
                + this.getDay() + "'\n" + " beginning= '" + this.getBeginning() + "'\n" + " end= '"
                + this.getEnd() + "'\n" + "";
    }
}
