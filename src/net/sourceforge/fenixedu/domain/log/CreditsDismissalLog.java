package net.sourceforge.fenixedu.domain.log;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.util.EnrolmentAction;

public class CreditsDismissalLog extends CreditsDismissalLog_Base {

    private CreditsDismissalLog() {
	super();
    }

    public CreditsDismissalLog(final EnrolmentAction action, final Registration registration, final CourseGroup courseGroup,
	    final Credits credits, final ExecutionSemester executionSemester, final String who) {
	this();
	check(courseGroup, "error.CreditsDismissalLog.invalid.courseGroup");
	init(action, registration, courseGroup, executionSemester, who);
	setCredits(BigDecimal.valueOf(credits.getGivenCredits()));
	setSourceDescription(buildSourceDescription(credits));
    }

    @Override
    protected String getDegreeModuleName() {
	return getDegreeModule().getName();
    }

}
