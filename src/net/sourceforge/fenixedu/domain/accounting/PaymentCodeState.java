package net.sourceforge.fenixedu.domain.accounting;

public enum PaymentCodeState {

	NEW, PROCESSED, CANCELLED, INVALID;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return PaymentCodeState.class.getSimpleName() + "." + name();
	}

	public String getFullyQualifiedName() {
		return PaymentCodeState.class.getName() + "." + name();
	}

}
