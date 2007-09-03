package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnroledOptionalEnrolment extends EnroledCurriculumModuleWrapper {

    /**
     * 
     */
    private static final long serialVersionUID = 3085559707039631255L;

    private DomainReference<OptionalCurricularCourse> optionalCurricularCourse;

    public EnroledOptionalEnrolment(CurriculumModule curriculumModule, OptionalCurricularCourse optionalCurricularCourse,
	    ExecutionPeriod executionPeriod) {
	super(curriculumModule, executionPeriod);
	setOptionalCurricularCourse(optionalCurricularCourse);

    }

    public OptionalCurricularCourse getOptionalCurricularCourse() {
	return (this.optionalCurricularCourse != null) ? this.optionalCurricularCourse.getObject() : null;
    }

    public void setOptionalCurricularCourse(OptionalCurricularCourse optionalCurricularCourse) {
	this.optionalCurricularCourse = (optionalCurricularCourse != null) ? new DomainReference<OptionalCurricularCourse>(
		optionalCurricularCourse) : null;
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionPeriod executionPeriod) {
	final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) getCurriculumModule();

	return optionalEnrolment.isApproved() ? Collections.EMPTY_LIST : getOptionalCurricularCourse().getCurricularRules(
		executionPeriod);
    }

    public Context getContext() {
	if (this.context == null) {
	    if (!getCurriculumModule().isRoot()) {
		final CurriculumGroup parentCurriculumGroup = getCurriculumModule().getCurriculumGroup();
		for (final Context context : parentCurriculumGroup.getDegreeModule().getValidChildContexts(getExecutionPeriod())) {
		    if (context.getChildDegreeModule() == getOptionalCurricularCourse()) {
			setContext(context);
			break;
		    }
		}
	    }

	}

	return (context != null) ? context.getObject() : null;
    }

    @Override
    public boolean isFor(DegreeModule degreeModule) {
	return getDegreeModule() == degreeModule || getOptionalCurricularCourse() == degreeModule;
    }

}
