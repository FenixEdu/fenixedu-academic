package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.utl.fenix.utils.NumberToWordsConverter;

public class ExternalCourseLoadRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected ExternalCourseLoadRequestDocument(final ExternalCourseLoadRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected ExternalCourseLoadRequest getDocumentRequest() {
        return (ExternalCourseLoadRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        setPersonFields();
        addParametersInformation();
    }

    private void addParametersInformation() {
        addParameter("studentNumber", getStudentNumber());
        addParameter("degreeDescription", getDegreeDescription());

        AdministrativeOffice administrativeOffice = getAdministrativeOffice();
        Unit adminOfficeUnit = administrativeOffice.getUnit();
        Person activeUnitCoordinator = adminOfficeUnit.getActiveUnitCoordinator();

        addParameter("administrativeOfficeCoordinatorName", activeUnitCoordinator.getName());
        addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));
        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getName());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());

        addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

        addParameter("numberOfCourseLoads", NumberToWordsConverter.convert(getDocumentRequest().getNumberOfCourseLoads()));
        addParameter("externalInstitutionName", getDocumentRequest().getInstitution().getName());
    }

    private String getStudentNumber() {
        final Registration registration = getDocumentRequest().getRegistration();
        if (ExternalCourseLoadRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
            final String agreementInformation = registration.getAgreementInformation();
            if (!StringUtils.isEmpty(agreementInformation)) {
                return registration.getRegistrationAgreement().toString() + SINGLE_SPACE + agreementInformation;
            }
        }
        return registration.getStudent().getNumber().toString();
    }

    @Override
    protected boolean showPriceFields() {
        return false;
    }

    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }
}
