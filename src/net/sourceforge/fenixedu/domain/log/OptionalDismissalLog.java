package net.sourceforge.fenixedu.domain.log;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.util.EnrolmentAction;

public class OptionalDismissalLog extends OptionalDismissalLog_Base {

	private OptionalDismissalLog() {
		super();
	}

	public OptionalDismissalLog(final EnrolmentAction action, final Registration registration,
			final OptionalCurricularCourse optionalCurricularCourse, final Credits credits, final Double ectsCredits,
			final ExecutionSemester executionSemester, final String who) {
		this();
		check(optionalCurricularCourse, "error.OptionalDismissalLog.invalid.optionalCurricularCourse");
		init(action, registration, optionalCurricularCourse, executionSemester, who);
		setCredits(BigDecimal.valueOf(ectsCredits));
		setSourceDescription(buildSourceDescription(credits));
	}

}
