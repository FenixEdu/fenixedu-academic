package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.credits.DepartmentInsertProfessorshipAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

public class InsertProfessorShip extends FenixService {

    protected void run(final Integer executionCourseId, final String teacherId, final Boolean responsibleFor, final Double hours)
            throws FenixServiceException {

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        final Teacher teacher = Teacher.readByIstId(teacherId);
        if (teacher == null) {
            throw new NonExistingServiceException("message.non.existing.teacher", null);
        }
        Professorship.create(responsibleFor, executionCourse, teacher, hours);
    }

    // Service Invokers migrated from Berserk

    private static final InsertProfessorShip serviceInstance = new InsertProfessorShip();

    @Service
    public static void runInsertProfessorShip(Integer executionCourseId, String teacherId, Boolean responsibleFor, Double hours)
            throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(executionCourseId, teacherId, responsibleFor, hours);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(executionCourseId, teacherId, responsibleFor, hours);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    // Service Invokers migrated from Berserk

    @Service
    public static void runInsertProfessorshipByDepartment(Integer executionCourseId, String teacherId, Boolean responsibleFor,
            Double hours) throws FenixServiceException, NotAuthorizedException {
        DepartmentInsertProfessorshipAuthorization.instance.execute(teacherId);
        serviceInstance.run(executionCourseId, teacherId, responsibleFor, hours);
    }

}