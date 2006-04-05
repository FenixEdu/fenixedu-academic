package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateSites extends Service {

    public Integer run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        int numberCreatedSites = 0;
        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
            if (executionCourse.getSite() == null) {
                executionCourse.createSite();
                numberCreatedSites++;
            }
        }

        return Integer.valueOf(numberCreatedSites);
    }

}
