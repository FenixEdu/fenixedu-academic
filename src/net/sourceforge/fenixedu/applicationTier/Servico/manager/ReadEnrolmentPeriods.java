package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriodInSchoolClasses;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadEnrolmentPeriods implements IService {

    public List<InfoEnrolmentPeriod> run(final Integer executionPeriodID) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class, executionPeriodID);

        final List<IEnrolmentPeriod> enrolmentPeriods = executionPeriod.getEnrolmentPeriod();
        final List<InfoEnrolmentPeriod> infoEnrolmentPeriods = new ArrayList<InfoEnrolmentPeriod>();
        for (final IEnrolmentPeriod enrolmentPeriod : enrolmentPeriods) {
            if (enrolmentPeriod instanceof IEnrolmentPeriodInCurricularCourses) {
                infoEnrolmentPeriods.add(InfoEnrolmentPeriodInCurricularCourses.newInfoFromDomain(enrolmentPeriod));
            } else if (enrolmentPeriod instanceof IEnrolmentPeriodInClasses) {
                infoEnrolmentPeriods.add(InfoEnrolmentPeriodInSchoolClasses.newInfoFromDomain(enrolmentPeriod));
            }
        }
        return infoEnrolmentPeriods;
    }

}