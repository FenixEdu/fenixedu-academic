package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.joda.time.DateTime;

abstract public class CurriculumLineLog extends CurriculumLineLog_Base {

    protected CurriculumLineLog() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDateDateTime(new DateTime());
    }

    protected void init(final EnrolmentAction action, final Registration registration, final DegreeModule degreeModule,
	    final ExecutionSemester executionSemester, final String who) {

	checkParameters(action, registration, degreeModule, executionSemester);

	setAction(action);
	setStudent(registration);
	setDegreeModule(degreeModule);
	setExecutionPeriod(executionSemester);
	setWho(who);
    }

    private void checkParameters(final EnrolmentAction action, final Registration registration, final DegreeModule degreeModule,
	    final ExecutionSemester executionSemester) {
	check(action, "error.log.EnrolmentLog.invalid.action");
	check(registration, "error.log.EnrolmentLog.invalid.registration");
	check(degreeModule, "error.log.DismissalLog.invalid.degreeModule");
	check(executionSemester, "error.log.EnrolmentLog.invalid.executionSemester");
    }

    public void delete() {
	disconnect();
	super.deleteDomainObject();
    }

    protected void disconnect() {
	removeRootDomainObject();
	removeStudent();
	removeDegreeModule();
	removeExecutionPeriod();
    }

    abstract public String getDescription();

    public boolean isFor(final ExecutionSemester executionSemester) {
	return getExecutionPeriod() == executionSemester;
    }

	@Deprecated
	public java.util.Date getDate(){
		org.joda.time.DateTime dt = getDateDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setDate(java.util.Date date){
		if(date == null) setDateDateTime(null);
		else setDateDateTime(new org.joda.time.DateTime(date.getTime()));
	}


}
