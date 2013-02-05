package net.sourceforge.fenixedu.domain.accounting;

public enum EventState {

    OPEN, CLOSED, CANCELLED;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return EventState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return EventState.class.getName() + "." + name();
    }

}
