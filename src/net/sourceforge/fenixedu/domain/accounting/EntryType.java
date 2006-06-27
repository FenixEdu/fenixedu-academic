package net.sourceforge.fenixedu.domain.accounting;

public enum EntryType {

    TRANSFER, CANDIDACY_ENROLMENT_FEE;

    public String getName() {
        return name();
    }
}
