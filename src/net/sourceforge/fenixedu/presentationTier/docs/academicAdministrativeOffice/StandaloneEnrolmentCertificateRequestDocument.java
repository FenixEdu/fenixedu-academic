package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.StandaloneEnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

public class StandaloneEnrolmentCertificateRequestDocument extends AdministrativeOfficeDocument {

    protected StandaloneEnrolmentCertificateRequestDocument(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected StandaloneEnrolmentCertificateRequest getDocumentRequest() {
        return (StandaloneEnrolmentCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        addParameter("enrolmentsInfo", getEnrolmentsInfo());
    }

    @Override
    protected String getDegreeDescription() {
        return getDocumentRequest().getRegistration().getDegreeDescription(null, getLocale());
    }

    @Override
    protected void newFillReport() {
        Employee loggedEmployee = AccessControl.getPerson().getEmployee();
        Registration registration = getDocumentRequest().getRegistration();

        fillFirstParagraph(loggedEmployee);

        fillSecondParagraph();

        fillSeventhParagraph();

        fillTrailer(loggedEmployee, registration);

        fillPriceTags();
    }

    protected void fillFirstParagraph(Employee loggedEmployee) {
        String coordinatorTitle;
        Person coordinator = loggedEmployee.getCurrentWorkingPlace().getActiveUnitCoordinator();
        String adminOfficeName = getMLSTextContent(loggedEmployee.getCurrentWorkingPlace().getPartyName());
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

    protected void fillSecondParagraph() {
        addParameter("secondParagraph",
                "      " + getResourceBundle().getString("label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
    }

    protected void fillSeventhParagraph() {
        String stringTemplate =
                getResourceBundle().getString("label.academicDocument.standaloneEnrolmentCertificate.seventhParagraph");
        addParameter("seventhParagraph", MessageFormat.format(stringTemplate, getDegreeDescription()));
    }

    final private String getEnrolmentsInfo() {
        final StringBuilder result = new StringBuilder();
        StandaloneEnrolmentCertificateRequest request = getDocumentRequest();

        final Collection<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);
        enrolments.addAll(request.getEnrolments());

        for (final Enrolment enrolment : enrolments) {
            result.append(
                    StringUtils.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
                            END_CHAR, getCreditsAndGradeInfo(enrolment, enrolment.getExecutionYear()))).append(LINE_BREAK);
        }

        result.append(generateEndLine());

        return result.toString();
    }

    protected void fillTrailer(Employee loggedEmployee, Registration registration) {
        String coordinatorTitle;
        Person coordinator = loggedEmployee.getCurrentWorkingPlace().getActiveUnitCoordinator();
        String adminOfficeName = getMLSTextContent(loggedEmployee.getCurrentWorkingPlace().getPartyName());
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String location = loggedEmployee.getCurrentCampus().getLocation();
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

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.signer");
        addParameter("signer", MessageFormat.format(stringTemplate, coordinatorTitle, adminOfficeName));

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

        addParameter("checkedBy", getResourceBundle()
                .getString("label.academicDocument.standaloneEnrolmentCertificate.checkedBy"));
    }

    protected void fillPriceTags() {
        addParameter("priceTagsPrinting", getResourceBundle().getString("label.academicDocument.certificate.printingPriceLabel"));
        addParameter("priceTagsIssuing", getResourceBundle().getString("label.academicDocument.certificate.issuingPriceLabel"));
        addParameter("priceTagsFastDelivery",
                getResourceBundle().getString("label.academicDocument.certificate.fastDeliveryPriceLabel"));
        addParameter("priceTagsTotal", getResourceBundle().getString("label.academicDocument.certificate.totalsPriceLabel"));
    }

}
