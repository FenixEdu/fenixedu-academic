package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 *  
 */
public class ExecutionYear extends DomainObject implements IExecutionYear {

    private PeriodState state;

    private String year;

    private Date beginDate;

    private Date endDate;

    private List executionPeriods;

    /**
     * Constructor for ExecutionYear.
     */
    public ExecutionYear() {
    }

    /**
     * @param year
     */
    public ExecutionYear(String year) {
        setYear(year);
    }

    /**
     * @param executionYearId
     */
    public ExecutionYear(Integer executionYearId) {
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
        if (obj instanceof IExecutionYear) {
            IExecutionYear executionYear = (IExecutionYear) obj;
            return getYear().equals(executionYear.getYear());
        }
        return false;
    }

    public String toString() {
        String result = "[EXECUTION_YEAR";
        result += ", internalCode=" + getIdInternal();
        result += ", year=" + year;
        result += ", begin=" + beginDate;
        result += ", end=" + endDate;
        result += "]";
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IExecutionYear#setState(Util.PeriodState)
     */
    public void setState(PeriodState state) {
        this.state = state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IExecutionYear#getState()
     */
    public PeriodState getState() {
        return this.state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getSlideName()
     */
    public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getParentNode()
     */
    public INode getParentNode() {
        return null;
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
     * @return Returns the executionPeriods.
     */
    public List getExecutionPeriods() {
        return executionPeriods;
    }

    /**
     * @param executionPeriods
     *            The executionPeriods to set.
     */
    public void setExecutionPeriods(List executionPeriods) {
        this.executionPeriods = executionPeriods;
    }
}