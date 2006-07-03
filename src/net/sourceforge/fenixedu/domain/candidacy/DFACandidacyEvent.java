package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.StateMachine;
import net.sourceforge.fenixedu.util.LabelFormatter;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {

    private DFACandidacyEvent() {
        super();
    }

    public DFACandidacyEvent(Person person, DFACandidacy candidacy) {
        this();
        init(person, candidacy);
    }

    private void init(Person person, DFACandidacy candidacy) {
        init(EventType.CANDIDACY_ENROLMENT, person);
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

        if (entryDTOs.size() != 1) {
            throw new DomainException("error.candidacy.dfaCandidacyEvent.invalid.number.of.entryDTOs");
        }

        final BigDecimal totalAmountToPay = calculateAmountToPay();
        final EntryDTO entryDTO = entryDTOs.get(0);

        checkIfCanAddAmount(totalAmountToPay, entryDTO.getAmountToPay());
        makeAccountingTransaction(user, this, getCandidacy().getPerson().getAccountBy(
                AccountType.EXTERNAL), getToAccount(), entryDTO.getEntryType(), entryDTO
                .getAmountToPay());

        if (canCloseEvent()) {
            closeEvent();
        }
    }

    @Override
    public Account getToAccount() {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit()
                .getAccountBy(AccountType.INTERNAL);
    }

    private void checkIfCanAddAmount(final BigDecimal totalAmountToPay, final BigDecimal amountToPay) {
        if (!amountToPay.equals(totalAmountToPay)) {
            throw new DomainException(
                    "error.accounting.dfaCandidacyEvent.amount.being.payed.must.match.amount.to.pay");
        }
    }

    @Override
    public List<EntryDTO> calculateEntries() {
        final List<EntryDTO> result = new ArrayList<EntryDTO>();
        final BigDecimal payedAmount = calculatePayedAmount();

        result.add(new EntryDTO(EntryType.CANDIDACY_ENROLMENT_FEE, this, calculateAmountToPay(),
                payedAmount, calculateAmountToPay().subtract(payedAmount),
                getDescriptionForEntryType(EntryType.CANDIDACY_ENROLMENT_FEE)));

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
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
                getDegree().getBolonhaDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
                getDegree().getName()).appendLabel(" - ").appendLabel(
                getExecutionDegree().getExecutionYear().getYear()).appendLabel(")");

        return labelFormatter;

    }

    private ExecutionDegree getExecutionDegree() {
        return getCandidacy().getExecutionDegree();

    }

    private Degree getDegree() {
        return getExecutionDegree().getDegreeCurricularPlan().getDegree();

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

    @Override
    public void closeEvent() {
        StateMachine.execute(getCandidacy().getActiveCandidacySituation());

        super.closeEvent();
    }

}
