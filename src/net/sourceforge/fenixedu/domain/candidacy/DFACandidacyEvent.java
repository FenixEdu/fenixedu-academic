package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {
    
    public DFACandidacyEvent(DateTime whenOccured, DFACandidacy candidacy) {
        super();
        init(candidacy);
        init(EventType.CANDIDACY_ENROLMENT_PAYMENT, whenOccured, candidacy.getPerson());
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
    protected void internalProcess() {
        
        final Account personExternalAccount = getPersonAccountBy(AccountType.EXTERNAL);
        final Account personInternalAccount = getPersonAccountBy(AccountType.INTERNAL);
        final Account degreeInternalAccount = getDegreeAccountBy(AccountType.INTERNAL);
        
        makeAccountingTransaction(personExternalAccount, personInternalAccount, calculateAmount());
        makeAccountingTransaction(personInternalAccount, degreeInternalAccount, calculateAmount());
    }
    
    @Override
    public List<EntryDTO> calculateEntries() {
        List<EntryDTO> result = new ArrayList<EntryDTO>();
        result.add(new EntryDTO(calculateAmount(), this));
        return result;
    }
    
    private Account getDegreeAccountBy(AccountType accountType) {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit().getAccountBy(accountType);
    }
    
    private Account getPersonAccountBy(AccountType accountType) {
        return getCandidacy().getPerson().getAccountBy(accountType);
    }
    
    // TODO: temporary (to remove after agreement and posting rules)
    private BigDecimal calculateAmount() {
        return new BigDecimal("100");
    }
    
    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
    }

}
