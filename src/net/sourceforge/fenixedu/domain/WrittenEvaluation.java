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

    /**
     * @return
     */
    public Calendar getBeginning() {
        if (this.getBeginningDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBeginningDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setBeginning(Calendar calendar) {
        if (calendar != null) {
            this.setBeginningDate(calendar.getTime());
        } else {
            this.setBeginningDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getDay() {
        if (this.getDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getDayDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setDay(Calendar calendar) {
        if (calendar != null) {
            this.setDayDate(calendar.getTime());
        } else {
            this.setDayDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnd() {
        if(this.getEndDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnd(Calendar calendar) {
        if (calendar != null) {
            this.setEndDate(calendar.getTime());    
        } else {
            this.setEndDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDay() {
        if (this.getEnrollmentBeginDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBeginDayDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDay(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBeginDayDate(calendar.getTime());    
        } else {
            this.setEnrollmentBeginDayDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        if (this.getEnrollmentBeginTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBeginTimeDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBeginTimeDate(calendar.getTime());
        } else {
            this.setEnrollmentBeginTimeDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDay() {
        if (this.getEnrollmentEndDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEndDayDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDay(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentEndDayDate(calendar.getTime());
        } else {
            this.setEnrollmentEndDayDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        if(this.getEnrollmentEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEndTimeDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
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

    public boolean equals(Object obj) {
        if (obj instanceof IWrittenEvaluation) {
            IWrittenEvaluation writtenEvaluationObj = (IWrittenEvaluation) obj;
            return this.getIdInternal().equals(writtenEvaluationObj.getIdInternal());
        }

        return false;
    }

}
