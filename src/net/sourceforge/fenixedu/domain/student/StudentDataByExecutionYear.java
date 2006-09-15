package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentDataByExecutionYear extends StudentDataByExecutionYear_Base {

    public StudentDataByExecutionYear() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWorkingStudent(false);
    }

    public StudentDataByExecutionYear(Student student) {
	this();
	setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	setStudent(student);
    }

    public StudentDataByExecutionYear(Student student, ExecutionYear executionYear) {
	this();
	setExecutionYear(executionYear);
	setStudent(student);
    }

}
