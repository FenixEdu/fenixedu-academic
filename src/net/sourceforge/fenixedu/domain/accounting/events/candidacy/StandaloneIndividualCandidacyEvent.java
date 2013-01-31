package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacy;

public class StandaloneIndividualCandidacyEvent extends StandaloneIndividualCandidacyEvent_Base {

	private StandaloneIndividualCandidacyEvent() {
		super();
	}

	public StandaloneIndividualCandidacyEvent(final StandaloneIndividualCandidacy candidacy, final Person person) {
		this();
		super.init(candidacy, EventType.STANDALONE_INDIVIDUAL_CANDIDACY, person);
	}

	@Override
	protected AdministrativeOffice readAdministrativeOffice() {
		return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
	}

	@Override
	protected EntryType getEntryType() {
		return EntryType.STANDALONE_INDIVIDUAL_CANDIDACY_FEE;
	}

	@Override
	public boolean isExemptionAppliable() {
		return true;
	}

}
