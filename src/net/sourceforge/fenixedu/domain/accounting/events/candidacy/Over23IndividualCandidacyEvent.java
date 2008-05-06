package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class Over23IndividualCandidacyEvent extends Over23IndividualCandidacyEvent_Base {

    private Over23IndividualCandidacyEvent() {
	super();
    }

    public Over23IndividualCandidacyEvent(final Over23IndividualCandidacy candidacy, final Person person) {
	this();
	init(candidacy, person);
    }

    private void init(final Over23IndividualCandidacy candidacy, final Person person) {
	final AdministrativeOffice administrativeOffice = readAdministrativeOffice();
	checkParameters(candidacy, administrativeOffice);
	super.init(administrativeOffice, EventType.OVER23_INDIVIDUAL_CANDIDACY, person);
	setIndividualCandidacy(candidacy);
    }

    private void checkParameters(final Over23IndividualCandidacy candidacy, final AdministrativeOffice administrativeOffice) {
	if (candidacy == null) {
	    throw new DomainException("error.Over23IndividualCandidacyEvent.invalid.candidacy");
	}
	if (administrativeOffice == null) {
	    throw new DomainException("error.Over23IndividualCandidacyEvent.invalid.administrativeOffice");
	}
    }

    private AdministrativeOffice readAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

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
