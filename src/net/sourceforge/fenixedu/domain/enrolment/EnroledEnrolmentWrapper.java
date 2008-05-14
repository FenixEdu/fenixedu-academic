package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnroledEnrolmentWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766503234444669518L;

    public EnroledEnrolmentWrapper(CurriculumModule curriculumModule, ExecutionSemester executionSemester) {
	super(curriculumModule, executionSemester);
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
	final Enrolment enrolment = (Enrolment) getCurriculumModule();

	return enrolment.isApproved() ? Collections.EMPTY_LIST : super.getCurricularRulesFromDegreeModule(executionSemester);
    }

}
