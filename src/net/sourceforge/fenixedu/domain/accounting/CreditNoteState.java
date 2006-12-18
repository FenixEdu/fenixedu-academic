package net.sourceforge.fenixedu.domain.accounting;

public enum CreditNoteState {
    EMITTED, PAYED, CANCELLED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return CreditNoteState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return CreditNoteState.class.getName() + "." + name();
    }
}
