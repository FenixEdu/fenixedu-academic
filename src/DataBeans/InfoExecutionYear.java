package DataBeans;

import java.util.Date;

import Dominio.IExecutionYear;
import Util.PeriodState;

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

    /**
     * @param year
     * @return
     */
    public static InfoExecutionYear copyFromDomain(IExecutionYear year) {
        InfoExecutionYear infoExecutionYear = null;
        if (year != null) {
            infoExecutionYear = new InfoExecutionYear();
            infoExecutionYear.setIdInternal(year.getIdInternal());
            infoExecutionYear.setBeginDate(year.getBeginDate());
            infoExecutionYear.setEndDate(year.getEndDate());
            infoExecutionYear.setState(year.getState());
            infoExecutionYear.setYear(year.getYear());
        }
        return infoExecutionYear;
    }
}