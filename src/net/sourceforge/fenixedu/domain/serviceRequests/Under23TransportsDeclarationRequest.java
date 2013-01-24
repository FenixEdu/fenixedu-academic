package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

import org.joda.time.LocalDate;
import org.joda.time.Period;

public class Under23TransportsDeclarationRequest extends Under23TransportsDeclarationRequest_Base {

    private Under23TransportsDeclarationRequest() {
	super();
    }

    public Under23TransportsDeclarationRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
	this();
	super.init(bean);

	checkPersonAge(getPerson());
    }

    private void checkPersonAge(final Person person) {
	if (new Period(person.getDateOfBirthYearMonthDay(), new LocalDate()).getYears() > 23) {
	    throw new DomainException("error.Under23TransportsDeclarationRequest.invalid.person.age");
	}
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
	super.createAcademicServiceRequestSituations(academicServiceRequestBean);

	if (academicServiceRequestBean.isNew()) {
	    AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
		    AcademicServiceRequestSituationType.PROCESSING, academicServiceRequestBean.getResponsible()));

	} else if (academicServiceRequestBean.isToConclude()) {
	    AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
		    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
	}
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	return false;
    }

    @Override
    public boolean isToPrint() {
	return !isDelivered();
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.UNDER_23_TRANSPORTS_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public boolean isPagedDocument() {
	return false;
    }

}
