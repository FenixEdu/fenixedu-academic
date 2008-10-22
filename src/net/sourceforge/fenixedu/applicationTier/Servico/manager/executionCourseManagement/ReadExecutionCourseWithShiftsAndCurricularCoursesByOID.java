/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID extends FenixService {

    public InfoExecutionCourse run(final Integer oid) {
	InfoExecutionCourse infoExecutionCourse = null;

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(oid);
	if (executionCourse != null) {
	    infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
	}

	return infoExecutionCourse;
    }

}
