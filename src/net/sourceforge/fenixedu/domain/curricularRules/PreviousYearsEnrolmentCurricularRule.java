package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;

public class PreviousYearsEnrolmentCurricularRule extends CurricularRuleNotPersistent {

    private RootCurriculumGroup rootCurriculumGroup;

    private PreviousYearsEnrolmentCurricularRule() {
	super();
    }

    public PreviousYearsEnrolmentCurricularRule(final RootCurriculumGroup rootCurriculumGroup) {
	this();
	this.rootCurriculumGroup = rootCurriculumGroup;
    }

    public ExecutionPeriod getBegin() {
	return ExecutionPeriod.readActualExecutionPeriod();
    }

    public CourseGroup getContextCourseGroup() {
	return null;
    }

    public CompositeRule getParentCompositeRule() {
	return null;
    }

    public CurricularRuleType getCurricularRuleType() {
	return CurricularRuleType.PREVIOUS_YEARS_ENROLMENT;
    }

    public RootCourseGroup getDegreeModuleToApplyRule() {
	return this.rootCurriculumGroup.getDegreeModule();
    }

    public ExecutionPeriod getEnd() {
	return null;
    }

    public List<GenericPair<Object, Boolean>> getLabel() {
	return Collections.singletonList(new GenericPair<Object, Boolean>("label.previousYearsEnrolment", true));
    }

}
