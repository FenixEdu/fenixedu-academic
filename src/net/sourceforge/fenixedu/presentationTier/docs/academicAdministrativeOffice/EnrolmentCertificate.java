package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
	    final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources/EnumerationResources", Language
		    .getLocale());

	    result.append(enumerationResources.getString(curricularYear.toString() + ".ordinal").toUpperCase());
	    result.append(" ano curricular, do ");
	}

	return result.toString();
    }

    final private String getEnrolmentsInfo() {
	final StringBuilder result = new StringBuilder();
	final EnrolmentCertificateRequest request = (EnrolmentCertificateRequest) getDocumentRequest();

	if (request.getDetailed()) {
	    final Collection<Enrolment> enrolments = new TreeSet<Enrolment>(
		    Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);

	    enrolments.addAll(request.getEntriesToReport());
	    reportEnrolments(result, enrolments);
	    enrolments.clear();

	    enrolments.addAll(request.getExtraCurricularEntriesToReport());
	    if (!enrolments.isEmpty()) {
		reportRemainingEnrolments(result, enrolments, "Extra-Curriculares");
	    }
	    enrolments.clear();

	    enrolments.addAll(request.getPropaedeuticEntriesToReport());
	    if (!enrolments.isEmpty()) {
		reportRemainingEnrolments(result, enrolments, "Propedeuticas");
	    }
	    enrolments.clear();

	    result.append(generateEndLine());
	}

	return result.toString();
    }

    final private void reportEnrolments(final StringBuilder result, final Collection<Enrolment> enrolments) {
	for (final Enrolment enrolment : enrolments) {
	    reportEnrolment(result, enrolment);
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final Collection<Enrolment> enrolments,
	    final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");

	for (final Enrolment enrolment : enrolments) {
	    reportEnrolment(result, enrolment);
	}
    }

    final private void reportEnrolment(final StringBuilder result, final Enrolment enrolment) {
	result.append(
		StringUtils.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH, '-',
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
