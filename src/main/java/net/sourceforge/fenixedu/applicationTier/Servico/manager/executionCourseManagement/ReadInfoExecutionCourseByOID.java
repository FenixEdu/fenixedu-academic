package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID {

    @Service
    public static InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException {

        if (executionCourseOID == null) {
            throw new FenixServiceException("nullId");
        }

        ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseOID);
        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}