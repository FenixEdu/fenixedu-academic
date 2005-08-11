package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateSites implements IService {

    public Integer run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod
                .readByOID(ExecutionPeriod.class, executionPeriodID);
        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        int numberCreatedSites = 0;
        for (final IExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getSite() == null) {
                persistentExecutionCourse.simpleLockWrite(executionCourse);
                executionCourse.createSite();
                persistentSite.simpleLockWrite(executionCourse.getSite());
                numberCreatedSites++;
            }
        }

        return Integer.valueOf(numberCreatedSites);
    }

}