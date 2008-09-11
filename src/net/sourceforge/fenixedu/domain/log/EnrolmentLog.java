/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.joda.time.DateTime;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

    protected EnrolmentLog() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDateDateTime(new DateTime());
    }

    public EnrolmentLog(final EnrolmentAction action, final Registration registration, final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester, final String who) {
	this();
	init(action, registration, curricularCourse, executionSemester, who);
    }

    protected void init(final EnrolmentAction action, final Registration registration, final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester, final String who) {

	checkParameters(action, registration, curricularCourse, executionSemester);

	setAction(action);
	setStudent(registration);
	setCurricularCourse(curricularCourse);
	setExecutionPeriod(executionSemester);
	setWho(who);
    }

    private void checkParameters(final EnrolmentAction action, final Registration registration,
	    final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	checkParameter(action, "error.log.EnrolmentLog.invalid.action");
	checkParameter(registration, "error.log.EnrolmentLog.invalid.registration");
	checkParameter(curricularCourse, "error.log.EnrolmentLog.invalid.curricularCourse");
	checkParameter(executionSemester, "error.log.EnrolmentLog.invalid.executionSemester");
    }

    protected void checkParameter(final Object obj, final String message) {
	if (obj == null) {
	    throw new DomainException(message);
	}
    }

    public String getCurricularCourseName() {
	return getCurricularCourse().getName(getExecutionPeriod());
    }

    public void delete() {
	removeRootDomainObject();
	removeStudent();
	removeCurricularCourse();
	removeExecutionPeriod();
	super.deleteDomainObject();
    }

}
