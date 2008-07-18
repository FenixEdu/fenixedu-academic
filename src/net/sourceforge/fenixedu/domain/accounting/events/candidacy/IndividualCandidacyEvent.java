package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class IndividualCandidacyEvent extends IndividualCandidacyEvent_Base {

    protected IndividualCandidacyEvent() {
	super();
    }

    protected void init(final IndividualCandidacy candidacy, final EventType eventType, final Person person) {
	final AdministrativeOffice administrativeOffice = readAdministrativeOffice();
	checkParameters(candidacy, administrativeOffice);
	super.init(administrativeOffice, eventType, person);
	setIndividualCandidacy(candidacy);
    }

    protected void checkParameters(final IndividualCandidacy candidacy, final AdministrativeOffice administrativeOffice) {
	if (candidacy == null) {
	    throw new DomainException("error.IndividualCandidacyEvent.invalid.candidacy");
	}
	if (administrativeOffice == null) {
	    throw new DomainException("error.IndividualCandidacyEvent.invalid.administrativeOffice");
	}
    }

    abstract protected AdministrativeOffice readAdministrativeOffice();

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	return new LabelFormatter().appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getExternalAccount();
    }

    @Override
    public PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
		getWhenOccured());
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getInternalAccount();
    }

}
