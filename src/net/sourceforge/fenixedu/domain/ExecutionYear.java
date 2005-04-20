package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 *  
 */
public class ExecutionYear extends ExecutionYear_Base {
    private PeriodState state;

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
        result += ", year=" + getYear();
        result += ", begin=" + getBeginDate();
        result += ", end=" + getEndDate();
        result += "]";
        return result;
    }

    public void setState(PeriodState state) {
        this.state = state;
    }

    public PeriodState getState() {
        return this.state;
    }

    public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        return null;
    }

}