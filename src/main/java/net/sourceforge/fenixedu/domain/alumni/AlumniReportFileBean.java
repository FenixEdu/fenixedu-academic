package net.sourceforge.fenixedu.domain.alumni;

import pt.ist.fenixframework.Atomic;

public class AlumniReportFileBean {

    @Atomic
    public static AlumniReportFile launchJob(boolean fullReport, boolean onlyRegisteredAlumni) {
        return AlumniReportFile.launchJob(fullReport, onlyRegisteredAlumni);
    }

}
