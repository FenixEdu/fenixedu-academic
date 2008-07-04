package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

    public StudentStatute(Student student, StudentStatuteType statuteType, ExecutionSemester beginExecutionPeriod,
	    ExecutionSemester endExecutionPeriod) {
	this();
	setBeginExecutionPeriod(beginExecutionPeriod);
	setEndExecutionPeriod(endExecutionPeriod);
	setStatuteType(statuteType);

	for (StudentStatute statute : student.getStudentStatutes()) {
	    if (statute.overlapsWith(this)) {
		throw new DomainException("error.studentStatute.alreadyExistsOneOverlapingStatute");
	    }
	}

	setStudent(student);
    }

    public boolean isValidInExecutionPeriod(final ExecutionSemester executionSemester) {

	if (getBeginExecutionPeriod() != null && getBeginExecutionPeriod().isAfter(executionSemester)) {
	    return false;
	}

	if (getEndExecutionPeriod() != null && getEndExecutionPeriod().isBefore(executionSemester)) {
	    return false;
	}

	return true;
    }

    public boolean isValidOn(final ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    if (!isValidInExecutionPeriod(executionSemester)) {
		return false;
	    }
	}

	return true;
    }

    public boolean isValidOnAnyExecutionPeriodFor(final ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    if (isValidInExecutionPeriod(executionSemester)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isValidInCurrentExecutionPeriod() {
	return this.isValidInExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public void delete() {
	removeBeginExecutionPeriod();
	removeEndExecutionPeriod();
	removeStudent();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public static class CreateStudentStatuteFactory extends ManageStudentStatuteBean implements FactoryExecutor {

	public CreateStudentStatuteFactory(Student student) {
	    super(student);
	}

	public Object execute() {
	    return new StudentStatute(getStudent(), getStatuteType(), getBeginExecutionPeriod(), getEndExecutionPeriod());
	}
    }

    public boolean overlapsWith(StudentStatute statute) {

	ExecutionSemester statuteBegin = statute.getBeginExecutionPeriod() != null ? statute.getBeginExecutionPeriod()
		: ExecutionSemester.readFirstExecutionSemester();
	ExecutionSemester statuteEnd = statute.getEndExecutionPeriod() != null ? statute.getEndExecutionPeriod()
		: ExecutionSemester.readLastExecutionSemester();

	return overlapsWith(statute.getStatuteType(), statuteBegin, statuteEnd);

    }

    public boolean overlapsWith(StudentStatuteType statuteType, ExecutionSemester statuteBegin, ExecutionSemester statuteEnd) {

	if (statuteType != getStatuteType()) {
	    return false;
	}

	ExecutionSemester thisStatuteBegin = getBeginExecutionPeriod() != null ? getBeginExecutionPeriod() : ExecutionSemester
		.readFirstExecutionSemester();
	ExecutionSemester thisStatuteEnd = getEndExecutionPeriod() != null ? getEndExecutionPeriod() : ExecutionSemester
		.readLastExecutionSemester();

	return statuteBegin.isAfterOrEquals(thisStatuteBegin) && statuteBegin.isBeforeOrEquals(thisStatuteEnd)
		|| statuteEnd.isAfterOrEquals(thisStatuteBegin) && statuteEnd.isBeforeOrEquals(thisStatuteEnd);

    }

    public void add(StudentStatute statute) {
	if (this.overlapsWith(statute)) {
	    if (statute.getBeginExecutionPeriod() == null
		    || (getBeginExecutionPeriod() != null && statute.getBeginExecutionPeriod()
			    .isBefore(getBeginExecutionPeriod()))) {
		setBeginExecutionPeriod(statute.getBeginExecutionPeriod());
	    }

	    if (statute.getEndExecutionPeriod() == null
		    || (getEndExecutionPeriod() != null && statute.getEndExecutionPeriod().isAfter(getEndExecutionPeriod()))) {
		setEndExecutionPeriod(statute.getEndExecutionPeriod());
	    }
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

    public boolean isGrantOwnerStatute() {
	return getStatuteType() == StudentStatuteType.SAS_GRANT_OWNER;
    }

    public String toDetailedString() {
	return (getBeginExecutionPeriod() != null ? getBeginExecutionPeriod().getQualifiedName() : " - ") + " ..... "
		+ (getEndExecutionPeriod() != null ? getEndExecutionPeriod().getQualifiedName() : " - ");
    }

}
