package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacy;

public class DegreeTransferIndividualCandidacyEvent extends DegreeTransferIndividualCandidacyEvent_Base {

    private DegreeTransferIndividualCandidacyEvent() {
	super();
    }

    public DegreeTransferIndividualCandidacyEvent(final DegreeTransferIndividualCandidacy candidacy, final Person person) {
	this();
	super.init(candidacy, EventType.DEGREE_TRANSFER_INDIVIDUAL_CANDICAY, person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

}
