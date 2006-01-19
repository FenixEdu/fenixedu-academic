package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class CreateSites implements IService {

    public Integer run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSupport
                .getIPersistentObject().readByOID(ExecutionPeriod.class, executionPeriodID);

        final List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        int numberCreatedSites = 0;
        for (final ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getSite() == null) {
                executionCourse.createSite();
                numberCreatedSites++;
            }
        }

        return Integer.valueOf(numberCreatedSites);
    }

}