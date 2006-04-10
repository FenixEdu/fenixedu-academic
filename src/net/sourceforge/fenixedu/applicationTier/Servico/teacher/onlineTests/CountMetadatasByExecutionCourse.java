/*
 * Created on 23/Jul/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class CountMetadatasByExecutionCourse extends Service {

    public Integer run(Integer executionCourseId) throws ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
    	return executionCourse.findVisibleMetadata().size();
    }

}