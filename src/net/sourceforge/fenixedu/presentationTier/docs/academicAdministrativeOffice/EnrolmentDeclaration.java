package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
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
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EnrolmentDeclaration extends AdministrativeOfficeDocument {

    protected EnrolmentDeclaration(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        Employee loggedEmployee = AccessControl.getPerson().getEmployee();
        Registration registration = getDocumentRequest().getRegistration();

        addParameter("curricularYear", getCurricularYear());
        addParameter("documentTitle", getResourceBundle().getString("label.academicDocument.title.declaration"));
        final List<Enrolment> enrolments =
                (List<Enrolment>) getDocumentRequest().getRegistration().getEnrolments(getExecutionYear());
        Integer numberEnrolments = Integer.valueOf(enrolments.size());
        addParameter("numberEnrolments", Integer.valueOf(enrolments.size()));
        addParameter("approvementInfo", getApprovementInfo());
        addParameter("documentPurpose", getDocumentPurpose());
        fillFirstParagraph(loggedEmployee);
        fillSecondParagraph(registration);
        fillthirdthParagraph(registration, numberEnrolments);
        setFooter(registration);
    }

    private void fillthirdthParagraph(Registration registration, Integer numberEnrolments) {
        String situation = "";
        if (getDocumentRequest().hasExecutionYear()) {
            situation = getResourceBundle().getString(getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");
            //addParameter("situation", getResourceBundle().getString(situation));
        }
        String student;

        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.enrolment.declaration.maleEnrolment");
        } else {
            student = getResourceBundle().getString("label.academicDocument.enrolment.declaration.femaleEnrolment");
        }
        String executionYear = getResourceBundle().getString("message.declaration.registration.execution.year.prefix");
        String stringTemplate1 = getResourceBundle().getString("message.academicDocument.enrolment.declaration");
        addParameter("thirdParagraph", MessageFormat.format(stringTemplate1, situation, student, executionYear,
                getDocumentRequest().getExecutionYear().getYear().toString(), getCurricularYear(), getDegreeDescription(),
                numberEnrolments, getApprovementInfo()));

        addParameter("curricularYear", getCurricularYear());

    }

    protected void fillFirstParagraph(Employee loggedEmployee) {

        String coordinatorTitle;
        //Person coordinator = loggedEmployee.getCurrentWorkingPlace().getActiveUnitCoordinator();
        Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();
        String adminOfficeName = getMLSTextContent(adminOfficeUnit.getPartyName());
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String universityName = getMLSTextContent(UniversityUnit.getInstitutionsUniversityUnit().getPartyName());
        setEmployeeFields(institutionName);
        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.firstParagraph");
        if (coordinator.isMale()) {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }
        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));
    }

    protected void fillSecondParagraph(Registration registration) {
        String student;
        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.declaration.theMaleStudent");
        } else {
            student = getResourceBundle().getString("label.academicDocument.declaration.theFemaleStudent");
        }
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

    final private void setEmployeeFields(String institutionName) {

        Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();
        String coordinatorTitle;
        if (coordinator.isMale()) {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }

        String stringTemplate = getResourceBundle().getString("label.academicDocument.irs.declaration.signer");
        addParameter("signer",
                MessageFormat.format(stringTemplate, coordinatorTitle, getMLSTextContent(adminOfficeUnit.getPartyName())));

        addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator());
        String location = adminOfficeUnit.getCampus().getLocation();
        String dateDD = new LocalDate().toString("dd", getLocale());
        String dateMMMM = new LocalDate().toString("MMMM", getLocale());
        String dateYYYY = new LocalDate().toString("yyyy", getLocale());
        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.signerLocation");
        addParameter("signerLocation",
                MessageFormat.format(stringTemplate, institutionName, location, dateDD, dateMMMM, dateYYYY));
        //addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));

        //addParameter("employeeLocation", adminOfficeUnit.getCampus().getLocation());

    }

    final private void setFooter(Registration registration) {
        String student;

        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.declaration.maleStudent");
        } else {
            student = getResourceBundle().getString("label.academicDocument.declaration.femaleStudent");
        }

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.studentNumber");
        addParameter("studentNumber", MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));

        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.documentNumber");
        addParameter("documentNumber",
                MessageFormat.format(stringTemplate, getDocumentRequest().getServiceRequestNumber().toString().trim()));
        //addParameter("checked", getResourceBundle().getString("label.academicDocument.irs.declaration.checked"));

        addParameter("page", getResourceBundle().getString("label.academicDocument.declaration.footer.page"));
        addParameter("pageOf", getResourceBundle().getString("label.academicDocument.declaration.footer.pageOf"));
    }

}
