package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;

public class PreviousYearsEnrolmentCurricularRule extends CurricularRuleNotPersistent {

    private CourseGroup courseGroup;

    private PreviousYearsEnrolmentCurricularRule() {
	super();
    }

    public PreviousYearsEnrolmentCurricularRule(final CourseGroup courseGroup) {
	this();
	this.courseGroup = courseGroup;
    }

    public ExecutionSemester getBegin() {
	return ExecutionSemester.readActualExecutionSemester();
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

    public CourseGroup getDegreeModuleToApplyRule() {
	return this.courseGroup;
    }

    public ExecutionSemester getEnd() {
	return null;
    }

    public List<GenericPair<Object, Boolean>> getLabel() {
	return Collections.singletonList(new GenericPair<Object, Boolean>("label.previousYearsEnrolment", true));
    }

    public VerifyRuleExecutor createVerifyRuleExecutor() {
	return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
