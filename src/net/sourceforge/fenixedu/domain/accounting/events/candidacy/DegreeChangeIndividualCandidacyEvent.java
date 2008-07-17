package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacy;

public class DegreeChangeIndividualCandidacyEvent extends DegreeChangeIndividualCandidacyEvent_Base {

    private DegreeChangeIndividualCandidacyEvent() {
	super();
    }

    public DegreeChangeIndividualCandidacyEvent(final DegreeChangeIndividualCandidacy candidacy, final Person person) {
	this();
	super.init(candidacy, EventType.DEGREE_CHANGE_INDIVIDUAL_CANDICAY, person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

}
