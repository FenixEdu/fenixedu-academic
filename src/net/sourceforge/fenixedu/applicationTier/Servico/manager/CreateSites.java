package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateSites extends Service {

    public Integer run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, executionPeriodID);

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