package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

abstract public class AcademicServiceRequestEvent extends AcademicServiceRequestEvent_Base {

    protected AcademicServiceRequestEvent() {
	super();
    }

    protected void init(final AdministrativeOffice administrativeOffice, final EventType eventType, final Person person, final AcademicServiceRequest academicServiceRequest) {
	super.init(administrativeOffice, eventType, person);

	checkParameters(academicServiceRequest);
	super.setAcademicServiceRequest(academicServiceRequest);
    }

    final protected void checkParameters(final AcademicServiceRequest academicServiceRequest) {
	if (academicServiceRequest == null) {
	    throw new DomainException("AcademicServiceRequestEvent.academicServiceRequest.cannot.be.null");
	}
    }

    @Override
    public void setAcademicServiceRequest(final AcademicServiceRequest academicServiceRequest) {
	throw new DomainException("error.events.serviceRequests.AcademicServiceRequestEvent.cannot.modify.academicServiceRequest");
    }

    @Override
    final public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }
    
    @Override
    final protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }
    
    @Override
    final public PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }
    
    final protected Degree getDegree() {
	return getAcademicServiceRequest().getDegree();
    }
    
    final protected ExecutionYear getExecutionYear() {
	return getAcademicServiceRequest().getExecutionYear();
    }

    final public boolean isUrgentRequest() {
	return getAcademicServiceRequest().isUrgentRequest();
    }
    
}
