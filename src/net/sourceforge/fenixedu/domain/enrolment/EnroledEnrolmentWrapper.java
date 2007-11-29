package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnroledEnrolmentWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766503234444669518L;

    public EnroledEnrolmentWrapper(CurriculumModule curriculumModule, ExecutionPeriod executionPeriod) {
	super(curriculumModule, executionPeriod);
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionPeriod executionPeriod) {
	final Enrolment enrolment = (Enrolment) getCurriculumModule();

	return enrolment.isApproved() ? Collections.EMPTY_LIST : super.getCurricularRulesFromDegreeModule(executionPeriod);
    }

}
