package net.sourceforge.fenixedu.domain.accounting.events.phd;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhDRegistrationEvent extends PhDRegistrationEvent_Base {
    
    private PhDRegistrationEvent() {
	super();
    }
    
    public PhDRegistrationEvent(AdministrativeOffice administrativeOffice, Person person, Registration registration) {

	this();
	init(administrativeOffice, EventType.PHD_REGISTRATION, person);
	if(registration == null) {
	    throw new DomainException("error.accounting.events.phd.PhDRegistrationEvent.registration.cannot.be.null");
	}
	setRegistration(registration);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
		getDegree().getName()).appendLabel(" - ").appendLabel(
		getExecutionDegree().getExecutionYear().getYear()).appendLabel(")");

	return labelFormatter;
    }

    private ExecutionDegree getExecutionDegree() {
	return getRegistration().getStudentCandidacy().getExecutionDegree();
    }

    private Degree getDegree() {
	return getExecutionDegree().getDegree();
    }
    
    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
	return getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),getWhenOccured());
    }

    private AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
	return getAdministrativeOffice().getServiceAgreementTemplate();
    }
    
    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }
    
}
