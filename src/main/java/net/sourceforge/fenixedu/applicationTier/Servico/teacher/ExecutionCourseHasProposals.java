/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author joaosa & rmalo
 * 
 */
public class ExecutionCourseHasProposals {

    protected Boolean run(Integer executionCourseCode) throws FenixServiceException {
        boolean result = false;
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseCode);

        result = executionCourse.hasProposals();

        return result;

    }
    // Service Invokers migrated from Berserk

    private static final ExecutionCourseHasProposals serviceInstance = new ExecutionCourseHasProposals();

    @Service
    public static Boolean runExecutionCourseHasProposals(Integer executionCourseCode) throws FenixServiceException  , NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode);
    }

}