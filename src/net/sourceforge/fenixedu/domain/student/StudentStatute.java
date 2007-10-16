package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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

    public StudentStatute(Student student, StudentStatuteType statuteType, ExecutionPeriod beginExecutionPeriod,
	    ExecutionPeriod endExecutionPeriod) {
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

    public boolean isValidInExecutionPeriod(final ExecutionPeriod executionPeriod) {

	if (getBeginExecutionPeriod() != null && getBeginExecutionPeriod().isAfter(executionPeriod)) {
	    return false;
	}

	if (getEndExecutionPeriod() != null && getEndExecutionPeriod().isBefore(executionPeriod)) {
	    return false;
	}

	return true;
    }

    public boolean isValidOn(final ExecutionYear executionYear) {
	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
	    if (!isValidInExecutionPeriod(executionPeriod)) {
		return false;
	    }
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

    public static class CreateStudentStatuteFactory extends ManageStudentStatuteBean implements FactoryExecutor {

	public CreateStudentStatuteFactory(Student student) {
	    super(student);
	}

	public Object execute() {
	    return new StudentStatute(getStudent(), getStatuteType(), getBeginExecutionPeriod(), getEndExecutionPeriod());
	}
    }

    public boolean overlapsWith(StudentStatute statute) {

	ExecutionPeriod statuteBegin = statute.getBeginExecutionPeriod() != null ? statute.getBeginExecutionPeriod()
		: ExecutionPeriod.readFirstExecutionPeriod();
	ExecutionPeriod statuteEnd = statute.getEndExecutionPeriod() != null ? statute.getEndExecutionPeriod() : ExecutionPeriod
		.readLastExecutionPeriod();

	return overlapsWith(statute.getStatuteType(), statuteBegin, statuteEnd);

    }

    public boolean overlapsWith(StudentStatuteType statuteType, ExecutionPeriod statuteBegin, ExecutionPeriod statuteEnd) {

	if (statuteType != getStatuteType()) {
	    return false;
	}

	ExecutionPeriod thisStatuteBegin = getBeginExecutionPeriod() != null ? getBeginExecutionPeriod() : ExecutionPeriod
		.readFirstExecutionPeriod();
	ExecutionPeriod thisStatuteEnd = getEndExecutionPeriod() != null ? getEndExecutionPeriod() : ExecutionPeriod
		.readLastExecutionPeriod();

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
