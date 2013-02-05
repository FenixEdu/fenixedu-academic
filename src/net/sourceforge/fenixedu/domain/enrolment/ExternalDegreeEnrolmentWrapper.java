package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;

public class ExternalDegreeEnrolmentWrapper extends EnroledEnrolmentWrapper {

    public ExternalDegreeEnrolmentWrapper(final Enrolment enrolment, final ExecutionSemester executionSemester) {
        super(enrolment, executionSemester);
    }

    @Override
    public Context getContext() {
        // does not have any connection to student's curricular plan
        return null;
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }
}
