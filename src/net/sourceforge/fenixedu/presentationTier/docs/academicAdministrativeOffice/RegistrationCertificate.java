package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class RegistrationCertificate extends AdministrativeOfficeDocument {

    protected RegistrationCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
        return (DocumentRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        final Registration registration = getDocumentRequest().getRegistration();
        final DegreeType degreeType = registration.getDegreeType();
        final CycleType cycleType =
                degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration.getCycleType(getExecutionYear());
        return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
    }

    @Override
    protected void setDocumentTitle() {
        addParameter("documentTitle", getResourceBundle().getString("label.academicDocument.title.declaration"));
    }

    @Override
    protected void newFillReport() {
        Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();
        Registration registration = getDocumentRequest().getRegistration();
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String coordinatorTitle;
        if (coordinator.isMale()) {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }

        String studentRegistered;
        if (registration.getStudent().getPerson().isMale()) {
            studentRegistered = getResourceBundle().getString("label.academicDocument.declaration.maleRegistered");
        } else {
            studentRegistered = getResourceBundle().getString("label.academicDocument.declaration.femaleRegistered");
        }

        fillFirstParagraph(coordinator, adminOfficeUnit, coordinatorTitle);

        fillSecondParagraph(registration);

        fillSeventhParagraph(registration, studentRegistered);

        setEmployeeFields(institutionName, adminOfficeUnit);
        setFooter(getDocumentRequest(), true);

    }

    protected void fillFirstParagraph(Person coordinator, Unit adminOfficeUnit, String coordinatorTitle) {

        String adminOfficeName = getMLSTextContent(adminOfficeUnit.getPartyName());
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String universityName = getMLSTextContent(UniversityUnit.getInstitutionsUniversityUnit().getPartyName());

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));

        addParameter("certificate",
                getResourceBundle().getString("label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
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

    protected void fillSeventhParagraph(Registration registration, String studentRegistered) {

        String situation =
                getResourceBundle().getString(getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.seventhParagraph");
        addParameter("seventhParagraph", MessageFormat.format(stringTemplate, situation,
                studentRegistered.toUpperCase(getLocale()), getExecutionYear().getYear(), getDegreeDescription()));
    }

}
