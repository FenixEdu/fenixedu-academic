package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author naat
 * 
 */

public class DissociateCurricularCourseByExecutionCourseId {

    @Service
    public static void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException {
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);

        CurricularCourse curricularCourse =
                (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseId);

        curricularCourse.removeAssociatedExecutionCourses(executionCourse);

    }
}