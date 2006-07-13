package net.sourceforge.fenixedu.domain.accounting;

public enum EntryType {

    TRANSFER, CANDIDACY_ENROLMENT_FEE, ADJUSTMENT;

    public String getName() {
        return name();
    }
}
