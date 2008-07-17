package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;

public class SecondCycleIndividualCandidacyEvent extends SecondCycleIndividualCandidacyEvent_Base {

    private SecondCycleIndividualCandidacyEvent() {
	super();
    }

    public SecondCycleIndividualCandidacyEvent(final SecondCycleIndividualCandidacy candidacy, final Person person) {
	this();
	super.init(candidacy, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
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
