package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class DeclarationRequestEvent extends DeclarationRequestEvent_Base {

    protected DeclarationRequestEvent() {
	super();
    }

    public DeclarationRequestEvent(AdministrativeOffice administrativeOffice, EventType eventType,
	    Person person, DeclarationRequest declarationRequest) {
	this();
	init(administrativeOffice, eventType, person, declarationRequest);

    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
	    DeclarationRequest declarationRequest) {
	checkParameters(declarationRequest);
	super.init(administrativeOffice, eventType, person, declarationRequest);
    }

    private void checkParameters(DeclarationRequest declarationRequest) {
	if (declarationRequest == null) {
	    throw new DomainException(
		    "error.accounting.events.serviceRequests.DeclarationRequestEvent.declarationRequest.cannot.be.null");
	}
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    protected PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();

	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(),
		LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getName());
	labelFormatter.appendLabel(" ) - ");
	labelFormatter.appendLabel(getExecutionYear().getYear());

	return labelFormatter;
    }

    private Degree getDegree() {
	return getAcademicServiceRequest().getDegree();
    }

    private ExecutionYear getExecutionYear() {
	return getAcademicServiceRequest().getExecutionYear();
    }

    public Integer getNumberOfPages() {
	return ((DeclarationRequest) getAcademicServiceRequest()).getNumberOfPages();
    }

}
