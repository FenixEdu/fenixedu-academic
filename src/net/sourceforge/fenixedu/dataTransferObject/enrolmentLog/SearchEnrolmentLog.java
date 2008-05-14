package net.sourceforge.fenixedu.dataTransferObject.enrolmentLog;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class SearchEnrolmentLog implements Serializable {

    private Integer studentNumber;
    private DomainReference<ExecutionSemester> executionSemester;

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester == null) ? null : this.executionSemester.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

}
