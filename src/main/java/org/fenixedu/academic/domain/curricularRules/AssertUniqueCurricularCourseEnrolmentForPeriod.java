package org.fenixedu.academic.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class AssertUniqueCurricularCourseEnrolmentForPeriod extends CurricularRuleNotPersistent {

    private CurricularCourse toApply;

    public AssertUniqueCurricularCourseEnrolmentForPeriod(final CurricularCourse toApply) {
        if (toApply == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        this.toApply = toApply;

    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections
                .singletonList(new GenericPair<Object, Boolean>("label.assertUniqueCurricularCourseEnrolmentForPeriod", true));
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return toApply;
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return null;
    }

    @Override
    public CompositeRule getParentCompositeRule() {
        return null;
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return CurricularRuleType.ASSERT_UNIQUE_CURRICULAR_COURSE_ENROLMENT_FOR_PERIOD;
    }

    @Override
    public ExecutionInterval getBegin() {
        return ExecutionInterval.findFirstCurrentChild(toApply.getDegree().getCalendar());
    }

    @Override
    public ExecutionInterval getEnd() {
        return null;

    }

    @Override
    public ExecutionInterval getBeginInterval() {
        return getBegin();
    }

    @Override
    public ExecutionInterval getEndInterval() {
        return null;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }
}
