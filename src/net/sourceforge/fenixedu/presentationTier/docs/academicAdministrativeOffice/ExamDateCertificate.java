package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExamDateCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.lang.StringUtils;

public class ExamDateCertificate extends AdministrativeOfficeDocument {

    public static class ExamDateEntry {

	private String curricularCourseName;

	private String firstSeasonDate;

	private String firstSeasonHour;

	private String secondSeasonDate;

	private String secondSeasonHour;

	private String specialSeasonDate;

	private String specialSeasonHour;

	public ExamDateEntry() {

	}

	public String getCurricularCourseName() {
	    return curricularCourseName;
	}

	public void setCurricularCourseName(String curricularCourseName) {
	    this.curricularCourseName = curricularCourseName;
	}

	public String getFirstSeasonDate() {
	    return firstSeasonDate;
	}

	public void setFirstSeasonDate(String firstSeasonDate) {
	    this.firstSeasonDate = firstSeasonDate;
	}

	public String getFirstSeasonHour() {
	    return firstSeasonHour;
	}

	public void setFirstSeasonHour(String firstSeasonHour) {
	    this.firstSeasonHour = firstSeasonHour;
	}

	public String getSecondSeasonDate() {
	    return secondSeasonDate;
	}

	public void setSecondSeasonDate(String secondSeasonDate) {
	    this.secondSeasonDate = secondSeasonDate;
	}

	public String getSecondSeasonHour() {
	    return secondSeasonHour;
	}

	public void setSecondSeasonHour(String secondSeasonHour) {
	    this.secondSeasonHour = secondSeasonHour;
	}

	public String getSpecialSeasonDate() {
	    return specialSeasonDate;
	}

	public void setSpecialSeasonDate(String specialSeasonDate) {
	    this.specialSeasonDate = specialSeasonDate;
	}

	public String getSpecialSeasonHour() {
	    return specialSeasonHour;
	}

	public void setSpecialSeasonHour(String specialSeasonHour) {
	    this.specialSeasonHour = specialSeasonHour;
	}

	public Boolean getAnyExamAvailable() {
	    return (!StringUtils.isEmpty(this.specialSeasonDate) && !StringUtils.isEmpty(this.specialSeasonHour))
		    || (!StringUtils.isEmpty(this.secondSeasonDate) && !StringUtils.isEmpty(this.secondSeasonHour))
		    || (!StringUtils.isEmpty(this.firstSeasonDate) && !StringUtils.isEmpty(this.firstSeasonHour));
	}
    }

    private static final long serialVersionUID = 1L;

    protected ExamDateCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void fillReport() {
	super.fillReport();
	addDataSourceElements(getExamDateEntries());
	addParameter("name", getDocumentRequest().getRegistration().getPerson().getName());
	addParameter("studentNumber", getStudentNumber());
    }
    
    private String getStudentNumber() {
	final Registration registration = getDocumentRequest().getRegistration();
	if (ExamDateCertificateRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
	    final String agreementInformation = registration.getAgreementInformation();
	    if (!StringUtils.isEmpty(agreementInformation)) {
		return registration.getRegistrationAgreement().toString() + " "  + agreementInformation;
	    }
	}
	return registration.getStudent().getNumber().toString();
    }

    private List<ExamDateEntry> getExamDateEntries() {
	final List<ExamDateEntry> result = new ArrayList<ExamDateEntry>();
	final ExamDateCertificateRequest request = (ExamDateCertificateRequest) getDocumentRequest();

	for (final Enrolment enrolment : request.getEnrolments()) {
	    final ExamDateEntry entry = new ExamDateEntry();
	    entry.setCurricularCourseName(enrolment.getCurricularCourse().getName());
	    fillFirstSeasonExam(request, enrolment, entry);
	    fillSecondSeasonExam(request, enrolment, entry);
	    fillSpecialSeasonExam(request, enrolment, entry);

	    result.add(entry);
	}

	return result;
    }

    private void fillSpecialSeasonExam(final ExamDateCertificateRequest request, final Enrolment enrolment,
	    final ExamDateEntry entry) {
	final Exam specialSeasonExam = request.getExamFor(enrolment, Season.SPECIAL_SEASON_OBJ);
	if (specialSeasonExam != null) {
	    entry.setSpecialSeasonDate(specialSeasonExam.getDayDateYearMonthDay().toString("dd/MM/yyyy"));
	    entry.setSpecialSeasonHour(specialSeasonExam.getBeginningDateHourMinuteSecond().toString("HH:mm"));
	}
    }

    private void fillSecondSeasonExam(final ExamDateCertificateRequest request, final Enrolment enrolment,
	    final ExamDateEntry entry) {
	final Exam secondSeasonExam = request.getExamFor(enrolment, Season.SEASON2_OBJ);
	if (secondSeasonExam != null) {
	    entry.setSecondSeasonDate(secondSeasonExam.getDayDateYearMonthDay().toString("dd/MM/yyyy"));
	    entry.setSecondSeasonHour(secondSeasonExam.getBeginningDateHourMinuteSecond().toString("HH:mm"));
	}
    }

    private void fillFirstSeasonExam(final ExamDateCertificateRequest request, final Enrolment enrolment,
	    final ExamDateEntry entry) {
	final Exam firstSeasonExam = request.getExamFor(enrolment, Season.SEASON1_OBJ);
	if (firstSeasonExam != null) {
	    entry.setFirstSeasonDate(firstSeasonExam.getDayDateYearMonthDay().toString("dd/MM/yyyy"));
	    entry.setFirstSeasonHour(firstSeasonExam.getBeginningDateHourMinuteSecond().toString("HH:mm"));
	}
    }

    @Override
    protected boolean showPriceFields() {
	return false;
    }

}
