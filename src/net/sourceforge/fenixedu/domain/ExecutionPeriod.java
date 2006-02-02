package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota
 * @author jpvl
 * 
 */
public class ExecutionPeriod extends ExecutionPeriod_Base implements INode, Comparable {

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
        ExecutionYear executionYear = getExecutionYear();
        return executionYear;
    }

    public int compareTo(Object object) {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) object; 
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();

        if (getExecutionYear() == executionYear) {
            return getSemester().compareTo(executionPeriod.getSemester());
        } else {
            return getExecutionYear().compareTo(executionYear);
        }
    }
    

	public String getQualifiedName()
	{
		return new StringBuilder().append(this.getName()).append(" ").append(this.getExecutionYear().getYear()).toString();
	}

    public boolean containsDay(Date day) {
        return !(this.getBeginDate().after(day) || this.getEndDate().before(day));
    }
}
