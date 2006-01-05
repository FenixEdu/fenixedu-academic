package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateSites implements IService {

    public Integer run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentSupport
                .getIPersistentObject().readByOID(ExecutionPeriod.class, executionPeriodID);

        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        int numberCreatedSites = 0;
        for (final IExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getSite() == null) {
                executionCourse.createSite();
                numberCreatedSites++;
            }
        }

        return Integer.valueOf(numberCreatedSites);
    }

}