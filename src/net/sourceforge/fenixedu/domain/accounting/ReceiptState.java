package net.sourceforge.fenixedu.domain.accounting;

public enum ReceiptState {
    ACTIVE, CANCELLED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return ReceiptState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return ReceiptState.class.getName() + "." + name();
    }

}
