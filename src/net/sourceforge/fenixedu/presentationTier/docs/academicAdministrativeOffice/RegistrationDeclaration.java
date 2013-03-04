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
import org.joda.time.LocalDate;

public class RegistrationDeclaration extends AdministrativeOfficeDocument {

    protected RegistrationDeclaration(final IDocumentRequest documentRequest) {
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
        //Employee loggedEmployee = AccessControl.getPerson().getEmployee();
        Registration registration = getDocumentRequest().getRegistration();

        fillFirstParagraph(coordinator, adminOfficeUnit);

        fillSecondParagraph(registration);

        fillSeventhParagraph(registration);

        fillTrailer(coordinator, registration, adminOfficeUnit);
    }

    protected void fillFirstParagraph(Person coordinator, Unit adminOfficeUnit) {
        String coordinatorTitle;
        //Person coordinator = loggedEmployee.getCurrentWorkingPlace().getActiveUnitCoordinator();
        //Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        //Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();
        String adminOfficeName = getMLSTextContent(adminOfficeUnit.getPartyName());
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String universityName = getMLSTextContent(UniversityUnit.getInstitutionsUniversityUnit().getPartyName());

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
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
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

    protected void fillSeventhParagraph(Registration registration) {
        String registered;
        String situation =
                getResourceBundle().getString(getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");
        if (registration.getStudent().getPerson().isMale()) {
            registered = getResourceBundle().getString("label.academicDocument.declaration.maleRegistered");
        } else {
            registered = getResourceBundle().getString("label.academicDocument.declaration.femaleRegistered");
        }
        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.seventhParagraph");
        addParameter("seventhParagraph", MessageFormat.format(stringTemplate, situation, registered.toUpperCase(getLocale()),
                getExecutionYear().getYear(), getDegreeDescription()));
    }

    protected void fillTrailer(Person coordinator, Registration registration, Unit adminOfficeUnit) {

        String coordinatorTitle;
        //Person coordinator = coordinator2.getCurrentWorkingPlace().getActiveUnitCoordinator();

        //String adminOfficeName = getMLSTextContent(loggedEmployee.getCurrentWorkingPlace().getPartyName());
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String location = adminOfficeUnit.getCampus().getLocation();
        String dateDD = new LocalDate().toString("dd", getLocale());
        String dateMMMM = new LocalDate().toString("MMMM", getLocale());
        String dateYYYY = new LocalDate().toString("yyyy", getLocale());
        String student;

        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.declaration.maleStudent");
        } else {
            student = getResourceBundle().getString("label.academicDocument.declaration.femaleStudent");
        }

        if (coordinator.isMale()) {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }
        addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator());
        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.signer");
        addParameter("signer",
                MessageFormat.format(stringTemplate, coordinatorTitle, getMLSTextContent(adminOfficeUnit.getPartyName())));

        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.signerLocation");
        addParameter("signerLocation",
                MessageFormat.format(stringTemplate, institutionName, location, dateDD, dateMMMM, dateYYYY));

        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.studentNumber");
        addParameter("studentNumber", MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));

        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.documentNumber");
        addParameter("documentNumber",
                MessageFormat.format(stringTemplate, getDocumentRequest().getServiceRequestNumber().toString().trim()));

        addParameter("page", getResourceBundle().getString("label.academicDocument.declaration.footer.page"));
        addParameter("pageOf", getResourceBundle().getString("label.academicDocument.declaration.footer.pageOf"));
    }

}
