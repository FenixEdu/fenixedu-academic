package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException {

        if (executionCourseOID == null) {
            throw new FenixServiceException("nullId");
        }

        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseOID);
        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}