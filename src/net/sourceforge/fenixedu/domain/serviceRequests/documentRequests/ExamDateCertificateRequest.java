package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Season;

import org.joda.time.DateTime;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

    protected ExamDateCertificateRequest() {
	super();
    }

    public ExamDateCertificateRequest(Registration registration, DateTime requestDate, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, ExecutionYear executionYear,
	    Collection<Enrolment> enrolments, Collection<Exam> exams, ExecutionPeriod executionPeriod) {

	this();
	init(registration, requestDate, executionYear, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		enrolments, exams, executionPeriod);
    }

    protected void init(Registration registration, DateTime requestDate, ExecutionYear executionYear,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    Collection<Enrolment> enrolments, Collection<Exam> exams, ExecutionPeriod executionPeriod) {

	checkParameters(executionYear, enrolments, executionPeriod);
	checkRulesToCreate(enrolments, exams, executionPeriod);
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	super.getEnrolments().addAll(enrolments);
	super.getExams().addAll(exams);
	super.setExecutionPeriod(executionPeriod);

    }

    private void checkRulesToCreate(Collection<Enrolment> enrolments, Collection<Exam> exams, ExecutionPeriod executionPeriod) {

	for (final Exam exam : exams) {
	    if (exam.isForSeason(Season.SPECIAL_SEASON_OBJ)
		    && !getEnrolmentFor(enrolments, exam).isSpecialSeasonEnroled(executionPeriod.getExecutionYear())) {

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

    private void checkParameters(ExecutionYear executionYear, Collection<Enrolment> enrolments, ExecutionPeriod executionPeriod) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.executionYear.cannot.be.null");
	}

	if (enrolments == null || enrolments.isEmpty()) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.enrolments.cannot.be.null.and.must.have.size.greater.than.zero");
	}

	if (executionPeriod == null) {
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

}
