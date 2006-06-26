package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
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
                
        final Account personExternalAccount = getPersonAccountBy(AccountType.EXTERNAL);
        final Account personInternalAccount = getPersonAccountBy(AccountType.INTERNAL);
        final Account degreeInternalAccount = getDegreeAccountBy(AccountType.INTERNAL);
        
        for (final EntryDTO entry : entryDTOs) {
            checkAmount(entry.getAmountToPay());
            makeAccountingTransaction(personExternalAccount, personInternalAccount, EntryType.TRANSFER, entry.getAmountToPay());
            makeAccountingTransaction(personInternalAccount, degreeInternalAccount, entry.getEntryType(), entry.getAmountToPay());
        }
        
        //TODO: modify according to penalties
        if (isTotalPayed()) {
            closeEvent();
        }
    }

    private void checkAmount(BigDecimal amountToPay) {
        if (amountToPay.add(calculateTotalPayedAmount()).compareTo(calculateAmount()) > 1) {
            throw new DomainException("error.candidacy.dfaCandidacy.invalid.amountToPay");
        }
    }

    //TODO: modify according to penalties
    private boolean isTotalPayed() {
        return calculateAmount().equals(calculateTotalPayedAmount());
    }
    
    @Override
    public List<EntryDTO> calculateEntries() {
        List<EntryDTO> result = new ArrayList<EntryDTO>();
        result.add(new EntryDTO(EntryType.CANDIDACY_ENROLMENT_FEE, calculateAmount(), calculateTotalPayedAmount(),
                calculateAmount().subtract(calculateTotalPayedAmount()), this));
        return result;
    }
    
    @Override
    protected boolean checkIfIsProcessed() {
        return (hasAnyEntries() && isTotalPayed());
    }
    
    private BigDecimal calculateTotalPayedAmount() {
        return calculateTotalPayedAmount(EntryType.CANDIDACY_ENROLMENT_FEE);
    }

    private BigDecimal calculateTotalPayedAmount(EntryType entryType) {
        BigDecimal result = new BigDecimal("0");
        for (final Entry entry : getEntriesSet()) {
            if (entry.getEntryType() == entryType && entry.isPositiveAmount()) {
                result = result.add(entry.getAmount());
            }
        }
        return result;
    }

    //TODO: remove after posting rules?
    private BigDecimal calculateAmount() {
        return new BigDecimal("100");
    }
    
    private Account getDegreeAccountBy(AccountType accountType) {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit().getAccountBy(accountType);
    }
    
    private Account getPersonAccountBy(AccountType accountType) {
        return getCandidacy().getPerson().getAccountBy(accountType);
    }

    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
    }

}
