package net.sourceforge.fenixedu.domain.dissertation;


public enum DissertationStateValue {
    DRAFT, SUBMITTED, APPROVED, CONFIRMED, REVISION, // The discussion occured
    // but the student can
    // submit information
    EVALUATED;

    public String getName() {
        return name();
    }
}
