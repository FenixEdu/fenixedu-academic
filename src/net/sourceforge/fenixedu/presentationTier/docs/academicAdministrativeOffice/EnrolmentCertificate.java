package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    protected EnrolmentCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	addParameter("curricularYear", getCurricularYear());
	addParameter("enrolmentsInfo", getEnrolmentsInfo());
    }

    @Override
    protected String getDegreeDescription() {
	final Registration registration = getDocumentRequest().getRegistration();
	return registration.getDegreeType().isComposite() ? registration.getDegreeDescription(null) : super
		.getDegreeDescription();
    }

    final private String getCurricularYear() {
	final StringBuilder result = new StringBuilder();

	if (!getDocumentRequest().getDegreeType().hasExactlyOneCurricularYear()) {
	    final ExecutionYear executionYear = getDocumentRequest().getExecutionYear();
	    final Integer curricularYear = Integer.valueOf(getDocumentRequest().getRegistration()
		    .getCurricularYear(executionYear));
	    final ResourceBundle enumerationResources = ResourceBundle
		    .getBundle("resources/EnumerationResources", LanguageUtils.getLocale());

	    result.append(enumerationResources.getString(curricularYear.toString() + ".ordinal").toUpperCase());
	    result.append(" ano curricular, do ");
	}

	return result.toString();
    }

    final private String getEnrolmentsInfo() {
	final StringBuilder result = new StringBuilder();

	if (((EnrolmentCertificateRequest) getDocumentRequest()).getDetailed()) {
	    final List<Enrolment> enrolments = new ArrayList<Enrolment>(((EnrolmentCertificateRequest) getDocumentRequest())
		    .getEnrolmentsToDisplay());
	    Collections.sort(enrolments, Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);

	    final List<Enrolment> extraCurricularEnrolments = new ArrayList<Enrolment>();
	    final List<Enrolment> propaedeuticEnrolments = new ArrayList<Enrolment>();

	    reportEnrolments(result, enrolments, extraCurricularEnrolments, propaedeuticEnrolments);

	    if (!extraCurricularEnrolments.isEmpty()) {
		reportRemainingEnrolments(result, extraCurricularEnrolments, "Extra-Curriculares");
	    }

	    if (!propaedeuticEnrolments.isEmpty()) {
		reportRemainingEnrolments(result, propaedeuticEnrolments, "Propedeuticas");
	    }

	    result.append(generateEndLine());
	}

	return result.toString();
    }

    final private void reportEnrolments(final StringBuilder result, final List<Enrolment> enrolments,
	    final List<Enrolment> extraCurricularEnrolments, final List<Enrolment> propaedeuticEnrolments) {
	for (final Enrolment enrolment : enrolments) {
	    if (enrolment.isExtraCurricular()) {
		extraCurricularEnrolments.add(enrolment);
		continue;
	    }

	    if (enrolment.isPropaedeutic()) {
		propaedeuticEnrolments.add(enrolment);
		continue;
	    }

	    reportEnrolment(result, enrolment);
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final List<Enrolment> enrolments, final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");

	for (final Enrolment enrolment : enrolments) {
	    reportEnrolment(result, enrolment);
	}
    }

    final private void reportEnrolment(final StringBuilder result, final Enrolment enrolment) {
	result.append(
		StringUtils.multipleLineRightPadWithSuffix(enrolment.getName().getContent().toUpperCase(), LINE_LENGTH, '-',
			getCreditsInfo(enrolment))).append("\n");
    }

    final private String getCreditsInfo(final Enrolment enrolment) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    result.append(enrolment.getCurricularCourse().getEctsCredits(enrolment.getExecutionPeriod()).toString()).append(
		    getCreditsDescription());
	}

	return result.toString();
    }

}
