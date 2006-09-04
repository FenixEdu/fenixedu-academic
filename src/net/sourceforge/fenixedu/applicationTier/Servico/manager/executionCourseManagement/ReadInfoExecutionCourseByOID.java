package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID extends Service {

    public InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException {

	if (executionCourseOID == null) {
	    throw new FenixServiceException("nullId");
	}

	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseOID);
	return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}
