package net.sourceforge.fenixedu.domain.candidacy;

public enum CandidacySituationType {
    PRE_CANDIDACY(true),

    STAND_BY(true),

    STAND_BY_FILLED_DATA(true),

    STAND_BY_CONFIRMED_DATA(true),

    ADMITTED(true),

    CANCELLED(false),

    SUBSTITUTE(false),

    NOT_ADMITTED(false),

    REGISTERED(true);

    private boolean active;

    private CandidacySituationType(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return CandidacySituationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return CandidacySituationType.class.getName() + "." + name();
    }

    public boolean isActive() {
        return this.active;
    }
}
