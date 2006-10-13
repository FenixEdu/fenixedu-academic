package net.sourceforge.fenixedu.domain.candidacy;

public enum CandidacySituationType {
    PRE_CANDIDACY,
    STAND_BY,
    STAND_BY_FILLED_DATA,
    STAND_BY_CONFIRMED_DATA,
    ADMITTED,
    CANCELLED,
    SUBSTITUTE, 
    NOT_ADMITTED, 
    REGISTERED;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return CandidacySituationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return CandidacySituationType.class.getName() + "." + name();
    }
}
