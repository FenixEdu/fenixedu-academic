package net.sourceforge.fenixedu.domain.log;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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
        String[] args = {};
        if (optionalCurricularCourse == null) {
            throw new DomainException("error.OptionalDismissalLog.invalid.optionalCurricularCourse", args);
        }
        init(action, registration, optionalCurricularCourse, executionSemester, who);
        setCredits(BigDecimal.valueOf(ectsCredits));
        setSourceDescription(buildSourceDescription(credits));
    }

}
