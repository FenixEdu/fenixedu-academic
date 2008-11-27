package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * 
 * @author naat
 * 
 */

public class DissociateCurricularCourseByExecutionCourseId extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException {
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);

	curricularCourse.removeAssociatedExecutionCourses(executionCourse);

    }
}