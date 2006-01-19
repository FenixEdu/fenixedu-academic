/*
 * Created on 23/Jul/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */

public class CountMetadatasByExecutionCourse extends Service {

    public Integer run(Integer executionCourseId) throws ExcepcaoPersistencia {
        return new Integer(PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentMetadata()
                .countByExecutionCourse(executionCourseId));
    }
}