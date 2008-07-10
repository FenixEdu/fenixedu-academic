package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.utl.fenix.utils.NumberToWordsConverter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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

	final Employee employee = AccessControl.getPerson().getEmployee();

	addParameter("administrativeOfficeCoordinatorName", employee.getCurrentWorkingPlace().getActiveUnitCoordinator()
		.getName());
	addParameter("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());
	addParameter("day", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));

	addParameter("numberOfCourseLoads", NumberToWordsConverter.convert(getDocumentRequest().getNumberOfCourseLoads()));
	addParameter("externalInstitutionName", getDocumentRequest().getInstitution().getName());
    }

    private String getStudentNumber() {
	final Registration registration = getDocumentRequest().getRegistration();
	if (ExternalCourseLoadRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
	    final String agreementInformation = registration.getAgreementInformation();
	    if (!StringUtils.isEmpty(agreementInformation)) {
		return registration.getRegistrationAgreement().toString() + " " + agreementInformation;
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
