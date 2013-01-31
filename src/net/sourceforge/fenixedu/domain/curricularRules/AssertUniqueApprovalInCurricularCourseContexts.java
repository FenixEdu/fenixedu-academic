package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AssertUniqueApprovalInCurricularCourseContexts extends CurricularRuleNotPersistent {

	private CurricularCourse toApply;

	public AssertUniqueApprovalInCurricularCourseContexts(final CurricularCourse toApply) {
		if (toApply == null) {
			throw new DomainException("curricular.rule.invalid.parameters");
		} else {
			this.toApply = toApply;
		}
	}

	@Override
	public List<GenericPair<Object, Boolean>> getLabel() {
		return Collections.singletonList(new GenericPair<Object, Boolean>("label.assertUniqueApprovalInCurricularCourseContexts",
				true));
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
		return CurricularRuleType.ASSERT_UNIQUE_APPROVAL_IN_CURRICULAR_COURSE_CONTEXTS;
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
	public VerifyRuleExecutor createVerifyRuleExecutor() {
		return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
	}

}
