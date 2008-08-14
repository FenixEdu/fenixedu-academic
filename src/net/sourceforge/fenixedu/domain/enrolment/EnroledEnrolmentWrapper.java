package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;

public class EnroledEnrolmentWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766503234444669518L;

    public EnroledEnrolmentWrapper(final Enrolment enrolment, final ExecutionSemester executionSemester) {
	super(enrolment, executionSemester);
    }

    @Override
    public Enrolment getCurriculumModule() {
	return (Enrolment) super.getCurriculumModule();
    }

    private boolean isApproved() {
	return getCurriculumModule().isApproved();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
	return isApproved() ? Collections.EMPTY_LIST : super.getCurricularRulesFromDegreeModule(executionSemester);
    }

    @Override
    public boolean isDissertation() {
	return getCurriculumModule().getDegreeModule().isDissertation();
    }
}
