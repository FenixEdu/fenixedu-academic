package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeTransferIndividualCandidacyEvent extends DegreeTransferIndividualCandidacyEvent_Base {

    private static final List<EventType> COMPATIBLE_TYPES = Arrays.asList(

    EventType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY,

    EventType.DEGREE_CHANGE_INDIVIDUAL_CANDIDACY);

    private DegreeTransferIndividualCandidacyEvent() {
        super();
    }

    public DegreeTransferIndividualCandidacyEvent(final DegreeTransferIndividualCandidacy candidacy, final Person person) {
        this();
        super.init(candidacy, EventType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY, person);

        attachAvailablePaymentCode(person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Override
    protected void checkConditionsToTransferPaymentsAndCancel(Event targetEvent) {

        if (!COMPATIBLE_TYPES.contains(targetEvent.getEventType())) {
            throw new DomainException("error.accounting.Event.events.must.be.compatible");
        }

        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.transfer.payments.from.cancelled.events");
        }

        if (this == targetEvent) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.Event.target.event.must.be.different.from.source");
        }
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_FEE);
    }

    @Override
    public DegreeTransferIndividualCandidacy getIndividualCandidacy() {
        return (DegreeTransferIndividualCandidacy) super.getIndividualCandidacy();
    }

    public Degree getCandidacyDegree() {
        return getIndividualCandidacy().getSelectedDegree();
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_FEE;
    }

}
