package net.sourceforge.fenixedu.domain.student.scholarship.report;

public class UTLScholarshipSource extends UTLScholarshipSource_Base {

    public UTLScholarshipSource() {
        super();
    }

    @Deprecated
    public boolean hasUtlScholarshipReport() {
        return getUtlScholarshipReport() != null;
    }

}
