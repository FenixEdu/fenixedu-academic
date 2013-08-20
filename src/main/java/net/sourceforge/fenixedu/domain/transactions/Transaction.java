package net.sourceforge.fenixedu.domain.transactions;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public abstract class Transaction extends Transaction_Base {

    public static Comparator<Transaction> COMPARATOR_BY_TRANSACTION_DATE_TIME = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction leftTransaction, Transaction rightTransaction) {
            int comparationResult =
                    leftTransaction.getTransactionDateDateTime().compareTo(rightTransaction.getTransactionDateDateTime());
            return (comparationResult == 0) ? leftTransaction.getExternalId().compareTo(rightTransaction.getExternalId()) : comparationResult;
        }
    };

    public Transaction() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public Double getValue() {
        return getValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setValue(Double value) {
        setValueBigDecimal(BigDecimal.valueOf(value));
    }

    public void delete() {
        removePersonAccount();
        removeResponsiblePerson();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getTransactionDate() {
        org.joda.time.DateTime dt = getTransactionDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setTransactionDate(java.util.Date date) {
        if (date == null) {
            setTransactionDateDateTime(null);
        } else {
            setTransactionDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
