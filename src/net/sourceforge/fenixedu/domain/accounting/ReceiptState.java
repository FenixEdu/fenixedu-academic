package net.sourceforge.fenixedu.domain.accounting;

public enum ReceiptState {
    ACTIVE(true), ANNULLED(false);

    private boolean registerPrint;

    private ReceiptState(boolean registerPrint) {
	this.registerPrint = registerPrint;
    }

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return ReceiptState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return ReceiptState.class.getName() + "." + name();
    }

    public boolean isToRegisterPrint() {
	return registerPrint;
    }

}
