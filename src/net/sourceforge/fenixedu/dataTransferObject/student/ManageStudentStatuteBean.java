/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class ManageStudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private DomainReference<ExecutionSemester> beginExecutionPeriod;

    private DomainReference<ExecutionSemester> endExecutionPeriod;

    private DomainReference<Student> student;

    public ManageStudentStatuteBean(Student student) {
	super();
	this.student = new DomainReference<Student>(student);
    }

    public ExecutionSemester getBeginExecutionPeriod() {
	return beginExecutionPeriod != null ? beginExecutionPeriod.getObject() : null;
    }

    public ExecutionSemester getEndExecutionPeriod() {
	return endExecutionPeriod != null ? endExecutionPeriod.getObject() : null;
    }

    public StudentStatuteType getStatuteType() {
	return statuteType;
    }

    public Student getStudent() {
	return student != null ? student.getObject() : null;
    }

    public void setBeginExecutionPeriod(ExecutionSemester beginExecutionPeriod) {
	this.beginExecutionPeriod = beginExecutionPeriod != null ? new DomainReference<ExecutionSemester>(beginExecutionPeriod)
		: null;
    }

    public void setEndExecutionPeriod(ExecutionSemester endExecutionPeriod) {
	this.endExecutionPeriod = endExecutionPeriod != null ? new DomainReference<ExecutionSemester>(endExecutionPeriod) : null;
    }

    public void setStatuteType(StudentStatuteType statuteType) {
	this.statuteType = statuteType;
    }

    public void setStudent(Student student) {
	this.student = student != null ? new DomainReference<Student>(student) : null;
    }

}
