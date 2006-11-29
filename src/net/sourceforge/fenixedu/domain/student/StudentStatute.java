package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentStatute extends StudentStatute_Base {

    private StudentStatute() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public StudentStatute(Student student, StudentStatuteType statuteType,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
	this();
	setStudent(student);
	setBeginExecutionPeriod(beginExecutionPeriod);
	setEndExecutionPeriod(endExecutionPeriod);
	setStatuteType(statuteType);
    }

    public boolean isValidInExecutionPeriod(final ExecutionPeriod executionPeriod) {

	if (getBeginExecutionPeriod() != null && getBeginExecutionPeriod().isAfter(executionPeriod)) {
	    return false;
	}

	if (getEndExecutionPeriod() != null && getEndExecutionPeriod().isBefore(executionPeriod)) {
	    return false;
	}

	return true;
    }

    public boolean isValidInCurrentExecutionPeriod() {
	return this.isValidInExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
    }

    public void delete() {
	removeBeginExecutionPeriod();
	removeEndExecutionPeriod();
	removeStudent();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public static class CreateStudentStatuteFactory extends ManageStudentStatuteBean implements
	    FactoryExecutor {

	public CreateStudentStatuteFactory(Student student) {
	    super(student);
	}

	public Object execute() {
	    return new StudentStatute(getStudent(), getStatuteType(), getBeginExecutionPeriod(),
		    getEndExecutionPeriod());
	}
    }

    public static class DeleteStudentStatuteFactory implements FactoryExecutor {

	StudentStatute studentStatute;

	public DeleteStudentStatuteFactory(StudentStatute studentStatute) {
	    this.studentStatute = studentStatute;
	}

	public Object execute() {
	    this.studentStatute.delete();
	    return true;
	}

    }

}
