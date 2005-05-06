package net.sourceforge.fenixedu.domain.transactions;

public enum PaymentType {

    CASH,

    ATM,

    CHEQUE,

    NIB_TRANSFER,

    POSTAL,

    SIBS;

    public String getName() {
        return name();
    }

}
