package net.sourceforge.fenixedu.domain.phd.serviceRequests;

public class PhdStudentReingressionRequestDocument extends PhdStudentReingressionRequestDocument_Base {

    public PhdStudentReingressionRequestDocument() {
        super();
    }

    @Deprecated
    public boolean hasPhdStudentReingressionRequest() {
        return getPhdStudentReingressionRequest() != null;
    }

}
