package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

public class OptionalEnrolmentLog extends OptionalEnrolmentLog_Base {

    private OptionalEnrolmentLog() {
        super();
    }

    public OptionalEnrolmentLog(final EnrolmentAction action, final Registration registration,
            final CurricularCourse curricularCourse, final OptionalCurricularCourse optionalCurricularCourse,
            final ExecutionSemester executionSemester, final String who) {

        this();
        String[] args = {};
        if (optionalCurricularCourse == null) {
            throw new DomainException("error.OptionalEnrolmentLog.invalid.optionalCurricularCourse", args);
        }
        init(action, registration, curricularCourse, executionSemester, who);
        setOptionalCurricularCourse(optionalCurricularCourse);
    }

    @Override
    protected void disconnect() {
        removeOptionalCurricularCourse();
        super.disconnect();
    }

    @Override
    public String getDescription() {
        return getOptionalCurricularCourse().getName(getExecutionPeriod()) + " (" + super.getDescription() + ")";
    }
}
