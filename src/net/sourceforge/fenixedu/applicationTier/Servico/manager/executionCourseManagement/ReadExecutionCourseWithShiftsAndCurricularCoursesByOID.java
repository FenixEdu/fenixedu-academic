/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID extends Service {

    public InfoExecutionCourse run(final Integer oid) throws ExcepcaoPersistencia {
	InfoExecutionCourse infoExecutionCourse = null;

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(oid);
	if (executionCourse != null) {
	    infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
	}

	return infoExecutionCourse;
    }

}
