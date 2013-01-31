package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.util.Season;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

	static public final List<RegistrationAgreement> FREE_PAYMENT_AGREEMENTS = Arrays.asList(RegistrationAgreement.AFA,
			RegistrationAgreement.MA);

	protected ExamDateCertificateRequest() {
		super();
	}

	public ExamDateCertificateRequest(final DocumentRequestCreateBean bean) {
		this();
		super.init(bean);

		checkParameters(bean);
		checkRulesToCreate(bean);
		super.getEnrolments().addAll(bean.getEnrolments());
		super.getExams().addAll(bean.getExams());
		super.setExecutionPeriod(bean.getExecutionPeriod());
	}

	private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
		for (final Exam exam : bean.getExams()) {
			if (exam.isForSeason(Season.SPECIAL_SEASON_OBJ)
					&& !getEnrolmentFor(bean.getEnrolments(), exam).isSpecialSeasonEnroled(
							bean.getExecutionPeriod().getExecutionYear())) {

				throw new DomainExceptionWithLabelFormatter(
						"error.serviceRequests.documentRequests.ExamDateCertificateRequest.special.season.exam.requires.student.to.be.enroled",
						exam.getSeason().getDescription());
			}
		}
	}

	private Enrolment getEnrolmentFor(final Collection<Enrolment> enrolments, final Exam exam) {
		for (final Enrolment enrolment : enrolments) {
			if (exam.contains(enrolment.getCurricularCourse())) {
				return enrolment;
			}
		}

		throw new DomainException(
				"error.serviceRequests.documentRequests.ExamDateCertificateRequest.each.exam.must.belong.to.at.least.one.enrolment");

	}

	@Override
	protected void checkParameters(final DocumentRequestCreateBean bean) {
		if (bean.getExecutionYear() == null) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.ExamDateCertificateRequest.executionYear.cannot.be.null");
		}

		if (bean.getEnrolments() == null || bean.getEnrolments().isEmpty()) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.ExamDateCertificateRequest.enrolments.cannot.be.null.and.must.have.size.greater.than.zero");
		}

		if (bean.getExecutionPeriod() == null) {
			throw new DomainException(
					"error.net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExamDateCertificateRequest.executionPeriod.cannot.be.null");
		}

	}

	@Override
	public Integer getNumberOfUnits() {
		return 0;
	}

	@Override
	public DocumentRequestType getDocumentRequestType() {
		return DocumentRequestType.EXAM_DATE_CERTIFICATE;
	}

	@Override
	public String getDocumentTemplateKey() {
		return getClass().getName();
	}

	@Override
	public EventType getEventType() {
		return EventType.EXAM_DATE_CERTIFICATE_REQUEST;
	}

	public Exam getExamFor(final Enrolment enrolment, final Season season) {
		for (final Exam exam : getExams()) {
			if (exam.contains(enrolment.getCurricularCourse()) && exam.isForSeason(season)) {
				return exam;
			}
		}

		return null;
	}

	@Override
	public boolean isFree() {
		if (FREE_PAYMENT_AGREEMENTS.contains(getRegistration().getRegistrationAgreement())) {
			return true;
		}
		return super.isFree();
	}

}
