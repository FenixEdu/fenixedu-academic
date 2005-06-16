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
        Calendar result = Calendar.getInstance();
        result.setTime(this.getBeginningDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setBeginning(Calendar calendar) {
        this.setBeginningDate(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getDay() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getDayDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setDay(Calendar calendar) {
        this.setDayDate(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnd() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEndDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnd(Calendar calendar) {
        this.setEndDate(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDay() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentBeginDayDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDay(Calendar calendar) {
        this.setEnrollmentBeginDayDate(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentBeginTimeDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        this.setEnrollmentBeginTimeDate(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDay() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentEndDayDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDay(Calendar calendar) {
        this.setEnrollmentEndDayDate(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnrollmentEndTimeDate());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        this.setEnrollmentEndTimeDate(calendar.getTime());
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
