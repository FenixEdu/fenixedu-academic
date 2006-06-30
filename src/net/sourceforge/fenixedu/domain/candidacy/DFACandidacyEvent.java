package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {

    private DFACandidacyEvent() {
        super();
    }

    public DFACandidacyEvent(Party party, DFACandidacy candidacy) {
        this();
        init(party, candidacy);
    }

    private void init(Party party, DFACandidacy candidacy) {
        init(EventType.CANDIDACY_ENROLMENT, party);
        checkParameters(candidacy);
        super.setCandidacy(candidacy);
    }

    private void checkParameters(Candidacy candidacy) {
        if (candidacy == null) {
            throw new DomainException("error.candidacy.dfaCandidacyEvent.invalid.candidacy");
        }
    }

    @Override
    protected void internalProcess(User user, List<EntryDTO> entryDTOs) {
        final BigDecimal totalAmountToPay = calculateAmountToPay();
        BigDecimal totalPayedAmount = calculatePayedAmount();
        for (final EntryDTO entryDTO : entryDTOs) {
            checkIfCanAddAmount(totalAmountToPay, totalPayedAmount, entryDTO.getAmountToPay());
            makeAccountingTransaction(user, this, getCandidacy().getPerson().getAccountBy(
                    AccountType.EXTERNAL), getToAccount(), entryDTO.getEntryType(), entryDTO
                    .getAmountToPay());

            totalPayedAmount = totalPayedAmount.add(entryDTO.getAmountToPay());

        }

        if (canCloseEvent()) {
            closeEvent();
        }
    }

    @Override
    public Account getToAccount() {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit()
                .getAccountBy(AccountType.INTERNAL);
    }

    private void checkIfCanAddAmount(final BigDecimal totalAmountToPay,
            final BigDecimal totalPayedAmount, final BigDecimal amountToPay) {
        if (amountToPay.add(totalPayedAmount).compareTo(totalAmountToPay) > 0) {
            throw new DomainException(
                    "error.accounting.dfaCandidacyEvent.amount.being.payed.exceeds.total");
        }
    }

    @Override
    public List<EntryDTO> calculateEntries() {
        final List<EntryDTO> result = new ArrayList<EntryDTO>();
        final BigDecimal payedAmount = calculatePayedAmount();

        result.add(new EntryDTO(EntryType.CANDIDACY_ENROLMENT_FEE, this, calculateAmountToPay(),
                payedAmount, calculateAmountToPay().subtract(payedAmount),
                getDescriptionEntryType(EntryType.CANDIDACY_ENROLMENT_FEE)));

        return result;
    }

    // TODO: remove after posting rules?
    @Override
    protected BigDecimal calculateAmountToPay() {
        return new BigDecimal("100");
    }

    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
    }

    @Override
    public String getDescriptionEntryType(EntryType entryType) {
        return "dfaCandidacyEvent.description";
    }

    @Override
    protected BigDecimal calculatePayedAmount() {
        BigDecimal payedAmount = new BigDecimal("0");
        final Account account = getToAccount();
        for (final AccountingTransaction transaction : getAccountingTransactions()) {
            payedAmount = payedAmount.add(transaction.getAmountByAccount(account));
        }

        return payedAmount;
    }

}
