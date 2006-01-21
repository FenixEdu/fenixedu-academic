/*
 * Created on 23/Jul/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */

public class CountMetadatasByExecutionCourse extends Service {

    public Integer run(Integer executionCourseId) throws ExcepcaoPersistencia {
        return Integer.valueOf(persistentSupport.getIPersistentMetadata().countByExecutionCourse(executionCourseId));
    }
}