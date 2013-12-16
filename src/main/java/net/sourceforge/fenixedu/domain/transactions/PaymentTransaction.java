package net.sourceforge.fenixedu.domain.transactions;

import java.math.BigDecimal;
import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public abstract class PaymentTransaction extends PaymentTransaction_Base {

    public PaymentTransaction() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    /**
     * @param value
     * @param transactionDate
     * @param remarks
     * @param paymentType
     * @param transactionType
     * @param wasInternalBalance
     * @param responsiblePerson
     * @param personAccount
     * @param guideEntry
     */
    public PaymentTransaction(BigDecimal value, Timestamp transactionDate, String remarks, PaymentType paymentType,
            TransactionType transactionType, Boolean wasInternalBalance, Person responsiblePerson, PersonAccount personAccount,
            GuideEntry guideEntry) {
        this();
        setGuideEntry(guideEntry);
        setValueBigDecimal(value);
        setTransactionDate(transactionDate);
        setRemarks(remarks);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(wasInternalBalance);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
    }

    @Override
    public void delete() {
        if (this instanceof GratuityTransaction) {
            GratuityTransaction gratuityTransaction = (GratuityTransaction) this;
            GratuitySituation gratuitySituation = gratuityTransaction.getGratuitySituation();
            gratuityTransaction.setGratuitySituation(null);

            gratuitySituation.updateValues();
        }

        setGuideEntry(null);
        super.delete();
    }

    public BigDecimal getValueWithAdjustment() {

        BigDecimal reimbursedValue = BigDecimal.ZERO;
        if (hasGuideEntry()) {
            for (final ReimbursementGuideEntry reimbursementGuideEntry : getGuideEntry().getReimbursementGuideEntries()) {
                if (reimbursementGuideEntry.getReimbursementGuide().isPayed()) {
                    reimbursedValue = reimbursedValue.add(reimbursementGuideEntry.getValueBigDecimal());
                }
            }
        }

        return getValueBigDecimal().subtract(reimbursedValue);
    }

    @Deprecated
    public boolean hasGuideEntry() {
        return getGuideEntry() != null;
    }

}
