package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public class ExamDateCertificate extends AdministrativeOfficeDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected ExamDateCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void fillReport() {
	super.fillReport();

	this.dataSource.addAll(getExamDateEntries());

	parameters.put("name", getDocumentRequest().getRegistration().getPerson().getName());
    }

    private List<ExamDateEntry> getExamDateEntries() {
	final List<ExamDateEntry> result = new ArrayList<ExamDateEntry>();
	// final ExamDateCertificateRequest request =
	// (ExamDateCertificateRequest) getDocumentRequest();
	//
	// for (final Enrolment enrolment : request.getEnrolments()) {
	// result.add(new ExamDateEntry(enrolment.getDegreeModule().getName(),
	// buildExamDates(enrolment.getAttendsFor(
	// request.getExecutionPeriod()).getExecutionCourse())));
	// }

	for (int i = 0; i < 50; i++) {
	    result.add(new ExamDateEntry("FINISH " + i, "10/10/2006", "10:00", "10/10/2006", "10:00"));
	}

	return result;
    }

    private String buildExamDates(ExecutionCourse executionCourse) {
	final StringBuilder result = new StringBuilder();
	for (final WrittenEvaluation writtenEvaluation : executionCourse.getWrittenEvaluations()) {
	    if (writtenEvaluation.isExam()) {
		result.append(writtenEvaluation.getDayDateYearMonthDay().toString("dd/MM/yyyy")).append("  ").append(
			writtenEvaluation.getBeginningDateHourMinuteSecond().toString("HH:mm")).append(", ");
	    }

	}

	return result.toString().endsWith(", ") ? result.substring(0, result.length() - 2) : result.toString();
    }

    @Override
    protected boolean hasPayment() {
	return false;
    }

}
