/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class StudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private DomainReference<StudentStatute> studentStatute;

    public StudentStatuteBean(StudentStatuteType statuteType, ExecutionPeriod executionPeriod) {
	this.statuteType = statuteType;
	this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public StudentStatuteBean(StudentStatuteType statuteType) {
	this.statuteType = statuteType;
    }

    public StudentStatuteBean(StudentStatute studentStatute, ExecutionPeriod executionPeriod) {
	this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
	this.studentStatute = new DomainReference<StudentStatute>(studentStatute);
    }

    public StudentStatuteBean(StudentStatute studentStatute) {
	this.studentStatute = new DomainReference<StudentStatute>(studentStatute);
    }

    public ExecutionPeriod getExecutionPeriod() {
	return executionPeriod.getObject();
    }

    public StudentStatuteType getStatuteType() {
	return statuteType != null ? statuteType : getStudentStatute().getStatuteType();
    }

    public StudentStatute getStudentStatute() {
	return studentStatute != null ? studentStatute.getObject() : null;
    }

    public String getBeginPeriodFormatted() {
	return getStudentStatute() != null && getStudentStatute().hasBeginExecutionPeriod() ? getStudentStatute()
		.getBeginExecutionPeriod().getQualifiedName()
		: " ... ";
    }

    public String getEndPeriodFormatted() {
	return getStudentStatute() != null && getStudentStatute().hasEndExecutionPeriod() ? getStudentStatute()
		.getEndExecutionPeriod().getQualifiedName()
		: " ... ";
    }

}
