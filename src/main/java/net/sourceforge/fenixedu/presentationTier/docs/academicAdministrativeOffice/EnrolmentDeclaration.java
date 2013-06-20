package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentDeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class EnrolmentDeclaration extends AdministrativeOfficeDocument {

    protected EnrolmentDeclaration(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        Registration registration = getDocumentRequest().getRegistration();
        final Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        final Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();

        final List<Enrolment> enrolments =
                (List<Enrolment>) getDocumentRequest().getRegistration().getEnrolments(getExecutionYear());
        Integer numberEnrolments = Integer.valueOf(enrolments.size());

        String coordinatorTitle = getCoordinatorGender(coordinator);

        String student, studentEnrolment;
        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.declaration.theMaleStudent");
            studentEnrolment = getResourceBundle().getString("label.academicDocument.enrolment.declaration.maleEnrolment");
        } else {
            student = getResourceBundle().getString("label.academicDocument.declaration.theFemaleStudent");
            studentEnrolment = getResourceBundle().getString("label.academicDocument.enrolment.declaration.femaleEnrolment");
        }

        addParameter("documentTitle", getResourceBundle().getString("label.academicDocument.title.declaration"));
        addParameter("documentPurpose", getDocumentPurpose());
        fillFirstParagraph(adminOfficeUnit, coordinatorTitle, coordinator);
        fillSecondParagraph(registration, student);
        fillthirdthParagraph(registration, numberEnrolments, studentEnrolment);
        fillEmployeeFields();
        setFooter(getDocumentRequest());
    }

    private void fillthirdthParagraph(Registration registration, Integer numberEnrolments, String studentEnrolment) {
        String situation = "";
        if (getDocumentRequest().hasExecutionYear()) {
            situation = getResourceBundle().getString(getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");
        }

        String executionYear = getResourceBundle().getString("message.declaration.registration.execution.year.prefix");
        String stringTemplate1 = getResourceBundle().getString("message.academicDocument.enrolment.declaration");
        addParameter("thirdParagraph", MessageFormat.format(stringTemplate1, situation, studentEnrolment, executionYear,
                getDocumentRequest().getExecutionYear().getYear().toString(), getCurricularYear(), getDegreeDescription(),
                numberEnrolments, getApprovementInfo()));

    }

    protected void fillFirstParagraph(Unit adminOfficeUnit, String coordinatorTitle, Person coordinator) {

        String adminOfficeName = getMLSTextContent(adminOfficeUnit.getPartyName());
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String universityName = getMLSTextContent(UniversityUnit.getInstitutionsUniversityUnit().getPartyName());
        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));
    }

    protected void fillSecondParagraph(Registration registration, String student) {

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.secondParagraph");
        addParameter("secondParagraph",
                "      " + MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));
    }

    @Override
    protected String getDegreeDescription() {
        final Registration registration = getDocumentRequest().getRegistration();

        if (registration.getDegreeType().isComposite()) {
            return registration.getDegreeDescription(getDocumentRequest().getExecutionYear(), null);
        } else {
            final DegreeType degreeType = registration.getDegreeType();
            final CycleType cycleType =
                    degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
                            .getCycleType(getExecutionYear());
            return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
        }
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
        return (DocumentRequest) super.getDocumentRequest();
    }

    final private String getCurricularYear() {
        final StringBuilder result = new StringBuilder();

        if (!getDocumentRequest().getDegreeType().hasExactlyOneCurricularYear()) {
            final Integer curricularYear =
                    Integer.valueOf(getDocumentRequest().getRegistration().getCurricularYear(getExecutionYear()));

            result.append(getEnumerationBundle().getString(curricularYear.toString() + ".ordinal").toUpperCase());
            result.append(getResourceBundle().getString("label.academicDocument.enrolment.declaration.curricularYear"));
        }

        return result.toString();
    }

    final private String getApprovementInfo() {
        final StringBuilder result = new StringBuilder();

        final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();

        if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.PPRE) {
            final Registration registration = getDocumentRequest().getRegistration();
            final ExecutionYear executionYear = enrolmentDeclarationRequest.getExecutionYear();
            final boolean transition = registration.isTransition(executionYear);

            if (registration.isFirstTime(executionYear) && !transition) {
                result.append(getResourceBundle().getString(
                        "message.academicDocument.enrolment.declaration.approvement.firstTime"));
            } else {
                final Registration registrationToInspect = transition ? registration.getSourceRegistration() : registration;
                if (registrationToInspect.hasApprovement(executionYear.getPreviousExecutionYear())) {
                    result.append(getResourceBundle()
                            .getString("message.academicDocument.enrolment.declaration.approvement.have")
                            + executionYear.getPreviousExecutionYear().getYear());
                } else {
                    result.append(getResourceBundle().getString(
                            "message.academicDocument.enrolment.declaration.approvement.notHave")
                            + executionYear.getPreviousExecutionYear().getYear());
                }
            }
        }
        return result.toString();
    }

    final private String getDocumentPurpose() {
        final StringBuilder result = new StringBuilder();

        final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();

        if (enrolmentDeclarationRequest.getDocumentPurposeType() != null) {
            result.append(getResourceBundle().getString("documents.declaration.valid.purpose")).append(SINGLE_SPACE);
            if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.OTHER
                    && !StringUtils.isEmpty(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription())) {
                result.append(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription().toUpperCase());
            } else {
                result.append(getEnumerationBundle().getString(enrolmentDeclarationRequest.getDocumentPurposeType().name())
                        .toUpperCase());
            }
            result.append(".");
        }

        return result.toString();
    }

}
