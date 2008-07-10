package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.util.Season;

import org.joda.time.DateTime;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

    static public final List<RegistrationAgreement> FREE_PAYMENT_AGREEMENTS = Arrays.asList(RegistrationAgreement.AFA,
	    RegistrationAgreement.MA);

    protected ExamDateCertificateRequest() {
	super();
    }

    public ExamDateCertificateRequest(Registration registration, DateTime requestDate, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Collection<Enrolment> enrolments,
	    Collection<Exam> exams, ExecutionSemester executionSemester) {

	this();
	init(registration, requestDate, executionSemester.getExecutionYear(), documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest, enrolments, exams, executionSemester);
    }

    protected void init(Registration registration, DateTime requestDate, ExecutionYear executionYear,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    Collection<Enrolment> enrolments, Collection<Exam> exams, ExecutionSemester executionSemester) {

	checkParameters(executionYear, enrolments, executionSemester);
	checkRulesToCreate(enrolments, exams, executionSemester);
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	super.getEnrolments().addAll(enrolments);
	super.getExams().addAll(exams);
	super.setExecutionPeriod(executionSemester);

    }

    private void checkRulesToCreate(Collection<Enrolment> enrolments, Collection<Exam> exams, ExecutionSemester executionSemester) {

	for (final Exam exam : exams) {
	    if (exam.isForSeason(Season.SPECIAL_SEASON_OBJ)
		    && !getEnrolmentFor(enrolments, exam).isSpecialSeasonEnroled(executionSemester.getExecutionYear())) {

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

    private void checkParameters(ExecutionYear executionYear, Collection<Enrolment> enrolments,
	    ExecutionSemester executionSemester) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.executionYear.cannot.be.null");
	}

	if (enrolments == null || enrolments.isEmpty()) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.enrolments.cannot.be.null.and.must.have.size.greater.than.zero");
	}

	if (executionSemester == null) {
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
