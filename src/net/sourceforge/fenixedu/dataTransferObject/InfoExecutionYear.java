package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Nuno & Joana
 */
public class InfoExecutionYear extends InfoObject {

    private String year;

    private PeriodState state;

    private Date beginDate;

    private Date endDate;

    public InfoExecutionYear() {
    }

    public InfoExecutionYear(String year) {
        setYear(year);
    }

    public InfoExecutionYear(Integer executionYearId) {
        super(executionYearId);
    }

    /**
     * Returns the year.
     * 
     * @return String
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year.
     * 
     * @param year
     *            The year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionYear) {
            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) obj;
            return getYear().equals(infoExecutionYear.getYear());
        }
        return false;
    }

    public String toString() {
        String result = "[INFOEXECUTIONYEAR";
        result += ", year=" + year;
        result += "]";
        return result;
    }

    /**
     * @return PeriodState
     */
    public PeriodState getState() {
        return state;
    }

    /**
     * Sets the periodState.
     * 
     * @param periodState
     *            The periodState to set
     */
    public void setState(PeriodState state) {
        this.state = state;
    }

    public int compareTo(Object arg0) {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;
        return this.getYear().compareTo(infoExecutionYear.getYear());
    }

    /**
     * @return Returns the beginDate.
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return Returns the endDate.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void copyFromDomain(IExecutionYear year) {
        super.copyFromDomain(year);
        if (year != null) {
            setBeginDate(year.getBeginDate());
            setEndDate(year.getEndDate());
            setState(year.getState());
            setYear(year.getYear());
        }
    }

    /**
     * @param year
     * @return
     */
    public static InfoExecutionYear newInfoFromDomain(IExecutionYear year) {
        InfoExecutionYear infoExecutionYear = null;
        if (year != null) {
            infoExecutionYear = new InfoExecutionYear();
            infoExecutionYear.copyFromDomain(year);
        }
        return infoExecutionYear;
    }

    public void copyToDomain(InfoExecutionYear infoExecutionYear, IExecutionYear executionYear) {
        super.copyToDomain(infoExecutionYear, executionYear);
        executionYear.setBeginDate(infoExecutionYear.getBeginDate());
        executionYear.setEndDate(infoExecutionYear.getEndDate());
        executionYear.setState(infoExecutionYear.getState());
        executionYear.setYear(infoExecutionYear.getYear());
    }

}
