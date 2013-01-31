package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPerson;

public class DegreeCandidacyForGraduatedPersonEvent extends DegreeCandidacyForGraduatedPersonEvent_Base {

	private DegreeCandidacyForGraduatedPersonEvent() {
		super();
	}

	public DegreeCandidacyForGraduatedPersonEvent(final DegreeCandidacyForGraduatedPerson candidacy, final Person person) {
		this();
		super.init(candidacy, EventType.DEGREE_CANDIDACY_FOR_GRADUATED_PERSON, person);

		attachAvailablePaymentCode(person);
	}

	@Override
	protected AdministrativeOffice readAdministrativeOffice() {
		return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
	}

	@Override
	public DegreeCandidacyForGraduatedPerson getIndividualCandidacy() {
		return (DegreeCandidacyForGraduatedPerson) super.getIndividualCandidacy();
	}

	public Degree getCandidacyDegree() {
		return getIndividualCandidacy().getSelectedDegree();
	}

	@Override
	protected EntryType getEntryType() {
		return EntryType.DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_FEE;
	}

}
