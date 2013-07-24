package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID {

    @Atomic
    public static InfoExecutionCourse run(String executionCourseOID) throws FenixServiceException {

        if (executionCourseOID == null) {
            throw new FenixServiceException("nullId");
        }

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseOID);
        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}