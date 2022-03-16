package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.bennu.search.domain.DomainIndexSystem;
import org.joda.time.LocalDate;

public class IBANPayment extends IBANPayment_Base {

    public static final String IBAN_PAYMENT_CREATED = IBANPayment.class.getSimpleName() + ".IBAN_PAYMENT_CREATED";

    public IBANPayment(final IBAN iban, final LocalDate operationDate, final LocalDate settlementDate, final Money amount, final String settlement) {
        super();
        setIBAN(iban);
        setOperationDate(operationDate);
        setSettlementDate(settlementDate);
        setAmount(amount);
        setSettlement(settlement);
        DomainIndexSystem.getInstance().index(settlement, (index) -> index.getIBANPaymentSet(), this);
        Signal.emit(IBAN_PAYMENT_CREATED, new DomainObjectEvent<>(this));
    }

    public static IBANPayment lookup(final String settlement) {
        return DomainIndexSystem.getInstance().search(settlement, (index) -> index.getIBANPaymentSet().stream())
                .filter(ibanPayment -> ibanPayment.getSettlement().equals(settlement))
                .findAny().orElse(null);
    }
    
}
