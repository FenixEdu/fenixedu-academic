/*
 * Created on Mar 7, 2004
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionDegree;

/**
 * @author Luis Cruz
 *  
 */
public class Scheduleing extends DomainObject implements IScheduleing {

    private Integer keyExecutionDegree;

    private IExecutionDegree executionDegree;

    private Date startOfProposalPeriodDate;

    private Date startOfProposalPeriodTime;

    private Date endOfProposalPeriodDate;

    private Date endOfProposalPeriodTime;

    private Date startOfCandidacyPeriodDate;

    private Date startOfCandidacyPeriodTime;

    private Date endOfCandidacyPeriodDate;

    private Date endOfCandidacyPeriodTime;

    private Integer currentProposalNumber;

    private Integer minimumNumberOfCompletedCourses;

    private Integer minimumNumberOfStudents;

    private Integer maximumNumberOfStudents;

    private Integer maximumNumberOfProposalCandidaciesPerGroup;

    /* Construtores */
    public Scheduleing() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IScheduleing) {
            IScheduleing scheduleing = (IScheduleing) obj;

            if (getExecutionDegree() != null && scheduleing != null) {
                result = getExecutionDegree().equals(scheduleing.getExecutionDegree());
            }
        }
        return result;
    }

    public String toString() {
        String result = "[Proposal";
        result += ", idInternal=" + getIdInternal();
        result += ", executionDegree=" + getExecutionDegree();
        result += "]";
        return result;
    }

    /**
     * @return Returns the currentProposalNumber.
     */
    public Integer getCurrentProposalNumber() {
        return currentProposalNumber;
    }

    /**
     * @param currentProposalNumber
     *            The currentProposalNumber to set.
     */
    public void setCurrentProposalNumber(Integer currentProposalNumber) {
        this.currentProposalNumber = currentProposalNumber;
    }

    /**
     * @return Returns the endOfCandidacyPeriodDate.
     */
    public Date getEndOfCandidacyPeriodDate() {
        return endOfCandidacyPeriodDate;
    }

    /**
     * @param endOfCandidacyPeriodDate
     *            The endOfCandidacyPeriodDate to set.
     */
    public void setEndOfCandidacyPeriodDate(Date endOfCandidacyPeriodDate) {
        this.endOfCandidacyPeriodDate = endOfCandidacyPeriodDate;
    }

    /**
     * @return Returns the endOfCandidacyPeriodTime.
     */
    public Date getEndOfCandidacyPeriodTime() {
        return endOfCandidacyPeriodTime;
    }

    /**
     * @param endOfCandidacyPeriodTime
     *            The endOfCandidacyPeriodTime to set.
     */
    public void setEndOfCandidacyPeriodTime(Date endOfCandidacyPeriodTime) {
        this.endOfCandidacyPeriodTime = endOfCandidacyPeriodTime;
    }

    /**
     * @return Returns the endOfProposalPeriodDate.
     */
    public Date getEndOfProposalPeriodDate() {
        return endOfProposalPeriodDate;
    }

    /**
     * @param endOfProposalPeriodDate
     *            The endOfProposalPeriodDate to set.
     */
    public void setEndOfProposalPeriodDate(Date endOfProposalPeriodDate) {
        this.endOfProposalPeriodDate = endOfProposalPeriodDate;
    }

    /**
     * @return Returns the endOfProposalPeriodTime.
     */
    public Date getEndOfProposalPeriodTime() {
        return endOfProposalPeriodTime;
    }

    /**
     * @param endOfProposalPeriodTime
     *            The endOfProposalPeriodTime to set.
     */
    public void setEndOfProposalPeriodTime(Date endOfProposalPeriodTime) {
        this.endOfProposalPeriodTime = endOfProposalPeriodTime;
    }

    /**
     * @return Returns the executionDegree.
     */
    public IExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     *            The executionDegree to set.
     */
    public void setExecutionDegree(IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * @return Returns the keyExecutionDegree.
     */
    public Integer getKeyExecutionDegree() {
        return keyExecutionDegree;
    }

    /**
     * @param keyExecutionDegree
     *            The keyExecutionDegree to set.
     */
    public void setKeyExecutionDegree(Integer keyExecutionDegree) {
        this.keyExecutionDegree = keyExecutionDegree;
    }

    /**
     * @return Returns the startOfCandidacyPeriodDate.
     */
    public Date getStartOfCandidacyPeriodDate() {
        return startOfCandidacyPeriodDate;
    }

    /**
     * @param startOfCandidacyPeriodDate
     *            The startOfCandidacyPeriodDate to set.
     */
    public void setStartOfCandidacyPeriodDate(Date startOfCandidacyPeriodDate) {
        this.startOfCandidacyPeriodDate = startOfCandidacyPeriodDate;
    }

    /**
     * @return Returns the startOfCandidacyPeriodTime.
     */
    public Date getStartOfCandidacyPeriodTime() {
        return startOfCandidacyPeriodTime;
    }

    /**
     * @param startOfCandidacyPeriodTime
     *            The startOfCandidacyPeriodTime to set.
     */
    public void setStartOfCandidacyPeriodTime(Date startOfCandidacyPeriodTime) {
        this.startOfCandidacyPeriodTime = startOfCandidacyPeriodTime;
    }

    /**
     * @return Returns the startOfProposalPeriodDate.
     */
    public Date getStartOfProposalPeriodDate() {
        return startOfProposalPeriodDate;
    }

    /**
     * @param startOfProposalPeriodDate
     *            The startOfProposalPeriodDate to set.
     */
    public void setStartOfProposalPeriodDate(Date startOfProposalPeriodDate) {
        this.startOfProposalPeriodDate = startOfProposalPeriodDate;
    }

    /**
     * @return Returns the startOfProposalPeriodTime.
     */
    public Date getStartOfProposalPeriodTime() {
        return startOfProposalPeriodTime;
    }

    /**
     * @param startOfProposalPeriodTime
     *            The startOfProposalPeriodTime to set.
     */
    public void setStartOfProposalPeriodTime(Date startOfProposalPeriodTime) {
        this.startOfProposalPeriodTime = startOfProposalPeriodTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#getEndOfProposalPeriod()
     */
    public Date getEndOfProposalPeriod() {
        if (this.getEndOfProposalPeriodDate() != null && this.getEndOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#setEndOfProposalPeriod(java.util.Date)
     */
    public void setEndOfProposalPeriod(Date endOfProposalPeriod) {
        this.setEndOfProposalPeriodDate(endOfProposalPeriod);
        this.setEndOfProposalPeriodTime(endOfProposalPeriod);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#getStartOfProposalPeriod()
     */
    public Date getStartOfProposalPeriod() {
        if (this.getStartOfProposalPeriodDate() != null && this.getStartOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#setStartOfProposalPeriod(java.util.Date)
     */
    public void setStartOfProposalPeriod(Date startOfProposalPeriod) {
        this.setStartOfProposalPeriodDate(startOfProposalPeriod);
        this.setStartOfProposalPeriodTime(startOfProposalPeriod);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#getStartOfCandidacyPeriod()
     */
    public Date getStartOfCandidacyPeriod() {
        if (this.getStartOfCandidacyPeriodDate() != null && this.getStartOfCandidacyPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfCandidacyPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfCandidacyPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#setStartOfCandidacyPeriod(java.util.Date)
     */
    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod) {
        this.setStartOfCandidacyPeriodDate(startOfCandidacyPeriod);
        this.setStartOfCandidacyPeriodTime(startOfCandidacyPeriod);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#getEndOfCandidacyPeriod()
     */
    public Date getEndOfCandidacyPeriod() {
        if (this.getEndOfCandidacyPeriodDate() != null && this.getEndOfCandidacyPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfCandidacyPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfCandidacyPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.finalDegreeWork.IScheduleing#setEndOfCandidacyPeriod(java.util.Date)
     */
    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod) {
        this.setEndOfCandidacyPeriodDate(endOfCandidacyPeriod);
        this.setEndOfCandidacyPeriodTime(endOfCandidacyPeriod);
    }

    /**
     * @return Returns the minimumNumberOfCompletedCourses.
     */
    public Integer getMinimumNumberOfCompletedCourses() {
        return minimumNumberOfCompletedCourses;
    }

    /**
     * @param minimumNumberOfCompletedCourses
     *            The minimumNumberOfCompletedCourses to set.
     */
    public void setMinimumNumberOfCompletedCourses(Integer minimumNumberOfCompletedCourses) {
        this.minimumNumberOfCompletedCourses = minimumNumberOfCompletedCourses;
    }

    /**
     * @return Returns the maximumNumberOfStudents.
     */
    public Integer getMaximumNumberOfStudents() {
        return maximumNumberOfStudents;
    }

    /**
     * @param maximumNumberOfStudents
     *            The maximumNumberOfStudents to set.
     */
    public void setMaximumNumberOfStudents(Integer maximumNumberOfStudents) {
        this.maximumNumberOfStudents = maximumNumberOfStudents;
    }

    /**
     * @return Returns the minimumNumberOfStudents.
     */
    public Integer getMinimumNumberOfStudents() {
        return minimumNumberOfStudents;
    }

    /**
     * @param minimumNumberOfStudents
     *            The minimumNumberOfStudents to set.
     */
    public void setMinimumNumberOfStudents(Integer minimumNumberOfStudents) {
        this.minimumNumberOfStudents = minimumNumberOfStudents;
    }

    /**
     * @return Returns the maximumNumberOfProposalCandidaciesPerGroup.
     */
    public Integer getMaximumNumberOfProposalCandidaciesPerGroup() {
        return maximumNumberOfProposalCandidaciesPerGroup;
    }

    /**
     * @param maximumNumberOfProposalCandidaciesPerGroup
     *            The maximumNumberOfProposalCandidaciesPerGroup to set.
     */
    public void setMaximumNumberOfProposalCandidaciesPerGroup(
            Integer maximumNumberOfProposalCandidaciesPerGroup) {
        this.maximumNumberOfProposalCandidaciesPerGroup = maximumNumberOfProposalCandidaciesPerGroup;
    }
}