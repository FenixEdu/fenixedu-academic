package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota
 * @author jpvl
 *  
 */
public class ExecutionPeriod extends DomainObject implements IExecutionPeriod {

    private Integer semester;

    private PeriodState state;

    protected IExecutionYear executionYear;

    protected String name;

    private Integer keyExecutionYear;

    private Date beginDate;

    private Date endDate;

    private IExecutionPeriod previousExecutionPeriod;

    private Integer keyPreviousExecutionPeriod;
    
    private List schoolClasses;

    /**
     * Constructor for ExecutionPeriod.
     */
    public ExecutionPeriod() {
    }

    public ExecutionPeriod(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public ExecutionPeriod(String name, IExecutionYear executionYear) {
        setName(name);
        setExecutionYear(executionYear);

    }

    /**
     * Returns the executionYear.
     * 
     * @return ExecutionYear
     */
    public IExecutionYear getExecutionYear() {
        return executionYear;
    }

    /**
     * Returns the name.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the keyExecutionYear.
     * 
     * @return Integer
     */
    public Integer getKeyExecutionYear() {
        return keyExecutionYear;
    }

    /**
     * Sets the keyExecutionYear.
     * 
     * @param keyExecutionYear
     *            The keyExecutionYear to set
     */
    public void setKeyExecutionYear(Integer keyExecutionYear) {
        this.keyExecutionYear = keyExecutionYear;
    }

    /**
     * Sets the executionYear.
     * 
     * @param executionYear
     *            The executionYear to set
     */
    public void setExecutionYear(IExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String toString() {
        String result = "[EXECUTION_PERIOD";
        result += ", internalCode=" + getIdInternal();
        result += ", name=" + name;
        result += ", executionYear=" + getExecutionYear();
        result += ", begin Date=" + getBeginDate();
        result += ", end Date=" + getEndDate();
        result += "]\n";
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof IExecutionPeriod) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) obj;
            return getIdInternal().equals(executionPeriod.getIdInternal());
//            return getName().equals(executionPeriod.getName())
//                    && getExecutionYear().equals(executionPeriod.getExecutionYear());
        }
        return super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IExecutionPeriod#setState(Util.PeriodState)
     */
    public void setState(PeriodState newState) {
        this.state = newState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IExecutionPeriod#getState()
     */
    public PeriodState getState() {
        return this.state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IExecutionPeriod#getSemester()
     */
    public Integer getSemester() {
        return this.semester;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IExecutionPeriod#setSemester(java.lang.Integer)
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getSlideName()
     */
    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EP" + getIdInternal();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getParentNode()
     */
    public INode getParentNode() {
        IExecutionYear executionYear = getExecutionYear();
        return executionYear;
    }

    /**
     * @return
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param begin_date
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param end_date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return Returns the previousExecutionPeriod.
     */
    public IExecutionPeriod getPreviousExecutionPeriod() {
        return previousExecutionPeriod;
    }

    /**
     * @param previousExecutionPeriod
     *            The previousExecutionPeriod to set.
     */
    public void setPreviousExecutionPeriod(IExecutionPeriod previousExecutionPeriod) {
        this.previousExecutionPeriod = previousExecutionPeriod;
    }

    /**
     * @return Returns the keyPreviousExecutionPeriod.
     */
    public Integer getKeyPreviousExecutionPeriod() {
        return keyPreviousExecutionPeriod;
    }

    /**
     * @param keyPreviousExecutionPeriod
     *            The keyPreviousExecutionPeriod to set.
     */
    public void setKeyPreviousExecutionPeriod(Integer keyPreviousExecutionPeriod) {
        this.keyPreviousExecutionPeriod = keyPreviousExecutionPeriod;
    }
    /**
     * @return Returns the schoolClasses.
     */
    public List getSchoolClasses() {
        return schoolClasses;
    }
    /**
     * @param schoolClasses The schoolClasses to set.
     */
    public void setSchoolClasses(List schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}