package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class EnrolmentEvaluationLog extends EnrolmentEvaluationLog_Base {

	EnrolmentEvaluationLog() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	EnrolmentEvaluationLog(final EnrolmentEvaluation enrolmentEvaluation) {
		this();

		init(enrolmentEvaluation);
		setWho(getCurrentUser());
	}

	void init(final EnrolmentEvaluation enrolmentEvaluation) {
		setCurricularCourse(String.format("externalId: %s; code: %s; name %s", enrolmentEvaluation.getEnrolment()
				.getCurricularCourse().getExternalId(), enrolmentEvaluation.getEnrolment().getCurricularCourse().getCode(),
				enrolmentEvaluation.getEnrolment().getCurricularCourse().getName()));

		setGradeValue(enrolmentEvaluation.getGradeValue());
		setGradeScale(enrolmentEvaluation.getGradeScale() != null ? enrolmentEvaluation.getGradeScale().getName() : "");
		setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType().getName());
		setEnrolmentEvaluationState(enrolmentEvaluation.getEnrolmentEvaluationState() != null ? enrolmentEvaluation
				.getEnrolmentEvaluationState().getState().toString() : "");
		setExecutionSemester(enrolmentEvaluation.getEnrolment().getExecutionPeriod().getName());
		setExamDate(enrolmentEvaluation.getExamDateYearMonthDay() != null ? enrolmentEvaluation.getExamDateYearMonthDay()
				.toString("dd/MM/yyyy") : "");
		setEnrolmentEvaluationResponsible(enrolmentEvaluation.getPerson() != null ? enrolmentEvaluation.getPerson().getUsername() : "");
		setBook(enrolmentEvaluation.getBookReference());
		setPage(enrolmentEvaluation.getPage());
		setActionDate(new DateTime().toString());
	}

	public static void logEnrolmentEvaluationCreation(final EnrolmentEvaluation enrolmentEvaluation) {
		EnrolmentEvaluationLog log = new EnrolmentEvaluationLog(enrolmentEvaluation);
		log.setAction("create");
	}

	public static void logEnrolmentEvaluationDeletion(final EnrolmentEvaluation enrolmentEvaluation) {
		EnrolmentEvaluationLog log = new EnrolmentEvaluationLog(enrolmentEvaluation);
		log.setAction("delete");
	}

	@Override
	protected String getCurrentUser() {
		return AccessControl.getUserView() != null ? AccessControl.getUserView().getUtilizador() : null;
	}

}
