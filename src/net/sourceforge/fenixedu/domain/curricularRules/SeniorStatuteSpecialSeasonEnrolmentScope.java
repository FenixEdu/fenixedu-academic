package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SeniorStatuteSpecialSeasonEnrolmentScope extends CurricularRuleNotPersistent {

	private Enrolment enrolment;
	private Registration registration;

	public SeniorStatuteSpecialSeasonEnrolmentScope(final Enrolment enrolment, final Registration registration) {
		if (enrolment == null || registration == null) {
			throw new DomainException("curricular.rule.invalid.parameters");
		} else {
			this.enrolment = enrolment;
			this.registration = registration;
		}
	}

	@Override
	public List<GenericPair<Object, Boolean>> getLabel() {
		return Collections
				.singletonList(new GenericPair<Object, Boolean>("label.seniorStatuteSpecialSeasonEnrolmentScope", true));
	}

	public Enrolment getEnrolment() {
		return enrolment;
	}

	public Registration getRegistration() {
		return registration;
	}

	@Override
	public DegreeModule getDegreeModuleToApplyRule() {
		return getEnrolment().getDegreeModule();
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
		return CurricularRuleType.SENIOR_STATUTE_SCOPE;
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
	public boolean equals(Object obj) {
		if (obj instanceof SeniorStatuteSpecialSeasonEnrolmentScope) {
			SeniorStatuteSpecialSeasonEnrolmentScope seniorStatuteSpecialSeasonEnrolmentScope =
					(SeniorStatuteSpecialSeasonEnrolmentScope) obj;

			return (enrolment == seniorStatuteSpecialSeasonEnrolmentScope.getEnrolment() && registration == seniorStatuteSpecialSeasonEnrolmentScope
					.getRegistration());
		}

		return false;
	}

	@Override
	public VerifyRuleExecutor createVerifyRuleExecutor() {
		return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
	}

}
