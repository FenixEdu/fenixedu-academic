package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrolmentPeriod;


public class InfoEnrolmentPeriodInSchoolClasses extends InfoEnrolmentPeriod {

    public static InfoEnrolmentPeriodInSchoolClasses newInfoFromDomain(final IEnrolmentPeriod enrolmentPeriod) {
        InfoEnrolmentPeriodInSchoolClasses infoExecutionPeriod = null;
        if (enrolmentPeriod != null) {
            infoExecutionPeriod = new InfoEnrolmentPeriodInSchoolClasses();
            infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
        }
        return infoExecutionPeriod;
    }

}
