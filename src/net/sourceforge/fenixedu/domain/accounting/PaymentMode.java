package net.sourceforge.fenixedu.domain.accounting;

public enum PaymentMode {
    CASH;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return PaymentMode.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return PaymentMode.class.getName() + "." + name();
    }
}
