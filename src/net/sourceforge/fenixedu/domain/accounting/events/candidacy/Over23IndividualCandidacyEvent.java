package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacy;

public class Over23IndividualCandidacyEvent extends Over23IndividualCandidacyEvent_Base {

    private Over23IndividualCandidacyEvent() {
	super();
    }

    public Over23IndividualCandidacyEvent(final Over23IndividualCandidacy candidacy, final Person person) {
	this();
	super.init(candidacy, EventType.OVER23_INDIVIDUAL_CANDIDACY, person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

}
