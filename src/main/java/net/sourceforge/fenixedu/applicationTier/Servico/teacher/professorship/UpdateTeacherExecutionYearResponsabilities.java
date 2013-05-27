/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.CreditsServiceWithTeacherIdArgumentAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionYearResponsabilities {

    protected void run(Integer teacherId, Integer executionYearId, final List executionCourseResponsabilities)
            throws FenixServiceException, DomainException {
        final Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(teacherId);
        if (teacher == null) {
            throw new FenixServiceException("message.teacher-not-found");
        }

        teacher.updateResponsabilitiesFor(executionYearId, executionCourseResponsabilities);
    }

    // Service Invokers migrated from Berserk

    private static final UpdateTeacherExecutionYearResponsabilities serviceInstance =
            new UpdateTeacherExecutionYearResponsabilities();

    @Service
    public static void runUpdateTeacherExecutionYearResponsabilities(Integer teacherId, Integer executionYearId,
            List executionCourseResponsabilities) throws FenixServiceException, DomainException, NotAuthorizedException {
        CreditsServiceWithTeacherIdArgumentAuthorization.instance.execute(teacherId);
        serviceInstance.run(teacherId, executionYearId, executionCourseResponsabilities);
    }

}