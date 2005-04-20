package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota
 * @author jpvl
 *  
 */
public class ExecutionPeriod extends ExecutionPeriod_Base {
    private PeriodState state;

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

    public String toString() {
        String result = "[EXECUTION_PERIOD";
        result += ", internalCode=" + getIdInternal();
        result += ", name=" + getName();
        result += ", executionYear=" + getExecutionYear();
        result += ", begin Date=" + getBeginDate();
        result += ", end Date=" + getEndDate();
        result += "]\n";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IExecutionPeriod) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) obj;
            return getIdInternal().equals(executionPeriod.getIdInternal());
        }
        return super.equals(obj);
    }

    public void setState(PeriodState newState) {
        this.state = newState;
    }

    public PeriodState getState() {
        return this.state;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EP" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        IExecutionYear executionYear = getExecutionYear();
        return executionYear;
    }

}