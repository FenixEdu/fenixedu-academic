package net.sourceforge.fenixedu.domain.transactions;

import java.math.BigDecimal;
import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class GratuityTransaction extends GratuityTransaction_Base {

    public GratuityTransaction() {
        super();
    }

    public GratuityTransaction(Double value, Timestamp transactionDate, String remarks, PaymentType paymentType,
            TransactionType transactionType, Boolean wasInternalBalance, Person responsiblePerson, PersonAccount personAccount,
            GuideEntry guideEntry, GratuitySituation gratuitySituation) {
        this();
        setValue(value);
        setTransactionDate(transactionDate);
        setRemarks(remarks);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(wasInternalBalance);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setGuideEntry(guideEntry);
        setGratuitySituation(gratuitySituation);
    }

    public GratuityTransaction(final BigDecimal value, final DateTime transactionDateTime, final PaymentType paymentType,
            final TransactionType transactionType, final Person responsiblePerson, final PersonAccount personAccount,
            final GratuitySituation gratuitySituation) {
        this();
        setValueBigDecimal(value);
        setTransactionDateDateTime(transactionDateTime);
        setRemarks(null);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(false);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setGuideEntry(null);
        setGratuitySituation(gratuitySituation);
    }

    public boolean isInsidePeriod(final YearMonthDay start, final YearMonthDay end) {
        final YearMonthDay date = getTransactionDateDateTime().toYearMonthDay();
        return !date.isBefore(start) && !date.isAfter(end);
    }

    @Deprecated
    public boolean hasGratuitySituation() {
        return getGratuitySituation() != null;
    }

}
