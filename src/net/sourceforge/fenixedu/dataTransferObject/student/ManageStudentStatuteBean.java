/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class ManageStudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private DomainReference<ExecutionPeriod> beginExecutionPeriod;

    private DomainReference<ExecutionPeriod> endExecutionPeriod;

    private DomainReference<Student> student;

    public ManageStudentStatuteBean(Student student) {
	super();
	this.student = new DomainReference<Student>(student);
    }

    public ExecutionPeriod getBeginExecutionPeriod() {
	return beginExecutionPeriod != null ? beginExecutionPeriod.getObject() : null;
    }

    public ExecutionPeriod getEndExecutionPeriod() {
	return endExecutionPeriod != null ? endExecutionPeriod.getObject() : null;
    }

    public StudentStatuteType getStatuteType() {
	return statuteType;
    }

    public Student getStudent() {
	return student != null ? student.getObject() : null;
    }

    public void setBeginExecutionPeriod(ExecutionPeriod beginExecutionPeriod) {
	this.beginExecutionPeriod = beginExecutionPeriod != null ? new DomainReference<ExecutionPeriod>(
		beginExecutionPeriod) : null;
    }

    public void setEndExecutionPeriod(ExecutionPeriod endExecutionPeriod) {
	this.endExecutionPeriod = endExecutionPeriod != null ? new DomainReference<ExecutionPeriod>(
		endExecutionPeriod) : null;
    }

    public void setStatuteType(StudentStatuteType statuteType) {
	this.statuteType = statuteType;
    }

    public void setStudent(Student student) {
	this.student = student != null ? new DomainReference<Student>(student) : null;
    }

}
