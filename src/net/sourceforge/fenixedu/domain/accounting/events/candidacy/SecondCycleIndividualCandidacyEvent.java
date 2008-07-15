package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class SecondCycleIndividualCandidacyEvent extends SecondCycleIndividualCandidacyEvent_Base {

    private SecondCycleIndividualCandidacyEvent() {
	super();
    }

    public SecondCycleIndividualCandidacyEvent(final SecondCycleIndividualCandidacy candidacy, final Person person) {
	this();
	init(candidacy, person);
    }

    private void init(final SecondCycleIndividualCandidacy candidacy, final Person person) {
	final AdministrativeOffice administrativeOffice = readAdministrativeOffice();
	checkParameters(candidacy, administrativeOffice);
	super.init(administrativeOffice, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, person);
	setIndividualCandidacy(candidacy);
    }

    private void checkParameters(final SecondCycleIndividualCandidacy candidacy, final AdministrativeOffice administrativeOffice) {
	if (candidacy == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacyt.invalid.candidacy");
	}
	if (administrativeOffice == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.administrativeOffice");
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

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public boolean hasSecondCycleIndividualCandidacyExemption() {
	return getSecondCycleIndividualCandidacyExemption() != null;
    }

    public SecondCycleIndividualCandidacyExemption getSecondCycleIndividualCandidacyExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof SecondCycleIndividualCandidacyExemption) {
		return (SecondCycleIndividualCandidacyExemption) exemption;
	    }
	}

	return null;
    }
}
