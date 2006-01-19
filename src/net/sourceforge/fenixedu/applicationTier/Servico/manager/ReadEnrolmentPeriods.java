package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInSchoolClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadEnrolmentPeriods extends Service {

    public List<InfoEnrolmentPeriod> run(final Integer executionPeriodID) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class, executionPeriodID);

        final List<EnrolmentPeriod> enrolmentPeriods = executionPeriod.getEnrolmentPeriod();
        final List<InfoEnrolmentPeriod> infoEnrolmentPeriods = new ArrayList<InfoEnrolmentPeriod>();
        for (final EnrolmentPeriod enrolmentPeriod : enrolmentPeriods) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses) {
                infoEnrolmentPeriods.add(InfoEnrolmentPeriodInCurricularCourses.newInfoFromDomain(enrolmentPeriod));
            } else if (enrolmentPeriod instanceof EnrolmentPeriodInClasses) {
                infoEnrolmentPeriods.add(InfoEnrolmentPeriodInSchoolClasses.newInfoFromDomain(enrolmentPeriod));
            }
        }
        return infoEnrolmentPeriods;
    }

}