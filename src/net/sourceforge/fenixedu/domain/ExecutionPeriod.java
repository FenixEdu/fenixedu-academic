package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota
 * @author jpvl
 * 
 */
public class ExecutionPeriod extends ExecutionPeriod_Base {

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

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EP" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        IExecutionYear executionYear = getExecutionYear();
        return executionYear;
    }

    public int compareTo(Object o) {
        IExecutionPeriod executionPeriod = (IExecutionPeriod) o;
        String yearThis = this.getExecutionYear().getYear() + getSemester();
        String year = executionPeriod.getExecutionYear().getYear() + getSemester();
        return yearThis.compareTo(year);
    }

}
