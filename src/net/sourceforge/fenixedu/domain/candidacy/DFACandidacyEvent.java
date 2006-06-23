package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {
    
    public DFACandidacyEvent(DateTime whenOccured, DFACandidacy candidacy) {
        super();
        init(EventType.CANDIDACY_ENROLMENT_PAYMENT, whenOccured);
        init(candidacy);
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
        
        final Account personExternalAccount = getCandidacy().getPerson().getAccountBy(AccountType.EXTERNAL);
        final Account personInternalAccount = getCandidacy().getPerson().getAccountBy(AccountType.INTERNAL);
        final Account degreeInternalAccount = getCandidacyDegree().getUnit().getAccountBy(AccountType.INTERNAL);
        
        makeAccountingTransaction(personExternalAccount, personInternalAccount, calculateAmount());
        makeAccountingTransaction(personInternalAccount, degreeInternalAccount, calculateAmount());
    }

    private Degree getCandidacyDegree() {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree();
    }
    
    // TODO: temporary (to remove after agreement and posting rules)
    public BigDecimal calculateAmount() {
        return new BigDecimal("100");
    }
    
    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
    }
    
}
