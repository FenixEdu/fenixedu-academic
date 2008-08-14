/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class StudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private DomainReference<ExecutionSemester> executionSemester;

    private DomainReference<StudentStatute> studentStatute;

    public StudentStatuteBean(StudentStatuteType statuteType, ExecutionSemester executionSemester) {
	this.statuteType = statuteType;
	this.executionSemester = new DomainReference<ExecutionSemester>(executionSemester);
    }

    public StudentStatuteBean(StudentStatuteType statuteType) {
	this.statuteType = statuteType;
    }

    public StudentStatuteBean(StudentStatute studentStatute, ExecutionSemester executionSemester) {
	this.executionSemester = new DomainReference<ExecutionSemester>(executionSemester);
	this.studentStatute = new DomainReference<StudentStatute>(studentStatute);
    }

    public StudentStatuteBean(StudentStatute studentStatute) {
	this.studentStatute = new DomainReference<StudentStatute>(studentStatute);
    }

    public ExecutionSemester getExecutionPeriod() {
	return executionSemester.getObject();
    }

    public StudentStatuteType getStatuteType() {
	return statuteType != null ? statuteType : getStudentStatute().getStatuteType();
    }

    public StudentStatute getStudentStatute() {
	return studentStatute != null ? studentStatute.getObject() : null;
    }

    public String getBeginPeriodFormatted() {
	return getStudentStatute() != null && getStudentStatute().hasBeginExecutionPeriod() ? getStudentStatute()
		.getBeginExecutionPeriod().getQualifiedName() : " ... ";
    }

    public String getEndPeriodFormatted() {
	return getStudentStatute() != null && getStudentStatute().hasEndExecutionPeriod() ? getStudentStatute()
		.getEndExecutionPeriod().getQualifiedName() : " ... ";
    }

}
