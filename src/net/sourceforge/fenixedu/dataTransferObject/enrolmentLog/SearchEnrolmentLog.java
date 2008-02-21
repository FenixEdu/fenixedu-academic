package net.sourceforge.fenixedu.dataTransferObject.enrolmentLog;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class SearchEnrolmentLog implements Serializable {

    private Integer studentNumber;
    private DomainReference<ExecutionPeriod> executionPeriod;

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod == null) ? null : this.executionPeriod.getObject();
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }

}
