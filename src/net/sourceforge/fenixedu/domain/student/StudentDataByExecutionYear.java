package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentDataByExecutionYear extends StudentDataByExecutionYear_Base {
    
    static final public Comparator<StudentDataByExecutionYear> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<StudentDataByExecutionYear>() {
	public int compare(StudentDataByExecutionYear o1, StudentDataByExecutionYear o2) {
	    return o1.getExecutionYear().compareTo(o2.getExecutionYear());
	}};

    private StudentDataByExecutionYear() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWorkingStudent(false);
    }

    public StudentDataByExecutionYear(final Student student) {
	this();
	final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	
	checkParameters(student, executionYear);
	checkConditions(student, executionYear);
	
	setStudent(student);
	setExecutionYear(executionYear);
    }

    public StudentDataByExecutionYear(final Student student, final ExecutionYear executionYear) {
	this();
	
	checkParameters(student, executionYear);
	checkConditions(student, executionYear);

	setStudent(student);
	setExecutionYear(executionYear);
    }
    
    public StudentDataByExecutionYear(final Student student, final ExecutionYear executionYear, final StudentPersonalDataAuthorizationChoice choice) {
	this(student, executionYear);
	setPersonalDataAuthorization(choice);
    }
    
    private void checkParameters(final Student student, final ExecutionYear executionYear) {
	if (student == null) {
	    throw new RuntimeException("error.StudentDataByExecutionYear.student.cannot.be.null");
	}
	if (executionYear == null) {
	    throw new RuntimeException("error.StudentDataByExecutionYear.executionYear.cannot.be.null");
	}
    }
    
    private void checkConditions(final Student student, final ExecutionYear executionYear) {
	if (student.getStudentDataByExecutionYear(executionYear) != null) {
	    throw new DomainException("error.StudentDataByExecutionYear.student.already.has.data.for.given.executionYear");
	}
	
    }
    public void delete() {
	removeExecutionYear();
	removeResidenceCandidacy();
	removeStudent();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
