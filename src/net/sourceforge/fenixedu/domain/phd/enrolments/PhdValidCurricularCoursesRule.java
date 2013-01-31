package net.sourceforge.fenixedu.domain.phd.enrolments;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CompositeRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleNotPersistent;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;

public class PhdValidCurricularCoursesRule extends CurricularRuleNotPersistent {

	transient private CurricularCourse toApply;

	public PhdValidCurricularCoursesRule(final CurricularCourse toApply) {
		if (toApply == null) {
			throw new DomainException("curricular.rule.invalid.parameters");
		}
		this.toApply = toApply;
	}

	@Override
	public VerifyRuleExecutor createVerifyRuleExecutor() {
		return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
	}

	@Override
	public ExecutionSemester getBegin() {
		return ExecutionSemester.readActualExecutionSemester();
	}

	@Override
	public ExecutionSemester getEnd() {
		return null;
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
		// label not add to resources
		return CurricularRuleType.PHD_VALID_CURRICULAR_COURSES;
	}

	@Override
	public CurricularCourse getDegreeModuleToApplyRule() {
		return toApply;
	}

	@Override
	public List<GenericPair<Object, Boolean>> getLabel() {
		return Collections.singletonList(new GenericPair<Object, Boolean>(AlertService
				.getMessageFromResource("label.phd.enrolments.PhdValidCurricularCoursesRule"), false));
	}
}
