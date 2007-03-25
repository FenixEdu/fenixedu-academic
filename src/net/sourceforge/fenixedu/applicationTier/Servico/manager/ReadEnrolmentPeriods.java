package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInSchoolClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadEnrolmentPeriods extends Service {

    public List<InfoEnrolmentPeriod> run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        final List<EnrolmentPeriod> enrolmentPeriods = executionPeriod.getEnrolmentPeriod();
        final List<InfoEnrolmentPeriod> infoEnrolmentPeriods = new ArrayList<InfoEnrolmentPeriod>();
        for (final EnrolmentPeriod enrolmentPeriod : enrolmentPeriods) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses) {
                infoEnrolmentPeriods.add(InfoEnrolmentPeriodInCurricularCourses.newInfoFromDomain(enrolmentPeriod));
            } else if (enrolmentPeriod instanceof EnrolmentPeriodInClasses) {
                infoEnrolmentPeriods.add(InfoEnrolmentPeriodInSchoolClasses.newInfoFromDomain(enrolmentPeriod));
            } else if (enrolmentPeriod instanceof EnrolmentPeriodInImprovementOfApprovedEnrolment) {
        	infoEnrolmentPeriods.add(InfoEnrolmentPeriodInImprovementOfApprovedEnrolment.newInfoFromDomain(enrolmentPeriod));
            }
        }
        return infoEnrolmentPeriods;
    }

}