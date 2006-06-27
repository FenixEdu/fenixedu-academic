package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {

    public DFACandidacyEvent(DateTime whenOccured, DFACandidacy candidacy) {
        super();
        init(candidacy);
        init(EventType.CANDIDACY_ENROLMENT, whenOccured, candidacy.getPerson());
    }

    private void init(DFACandidacy candidacy) {
        checkParameters(candidacy);
        super.setCandidacy(candidacy);
    }

    private void checkParameters(Candidacy candidacy) {
        if (candidacy == null) {
            throw new DomainException("error.candidacy.dfaCandidacyEvent.invalid.candidacy");
        }
    }

    @Override
    protected void internalProcess(List<EntryDTO> entryDTOs) {
        if (canCloseEvent()) {
            closeEvent();
        }
    }

    private boolean canCloseEvent() {
        BigDecimal amountToBePayed = calculateAmount();
        for (final Event event : getPayments()) {
            if (!event.isClosed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<EntryDTO> calculateEntries() {
        List<EntryDTO> result = new ArrayList<EntryDTO>();
        result.add(new EntryDTO(EntryType.CANDIDACY_ENROLMENT_FEE, calculateAmount(),
                calculateTotalPayedAmount(), calculateAmount().subtract(calculateTotalPayedAmount()),
                this));
        return result;
    }

    private BigDecimal calculateTotalPayedAmount() {
        BigDecimal totalPayedAmount = new BigDecimal("0");

        for (Event event : getPayments()) {
            DFACandidacyPaymentEvent paymentEvent = (DFACandidacyPaymentEvent) event;
            totalPayedAmount = totalPayedAmount.add(paymentEvent.calculateAmount());
        }

        return totalPayedAmount;

    }

    // TODO: remove after posting rules?
    public BigDecimal calculateAmount() {
        return new BigDecimal("100");
    }

    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
    }

}
