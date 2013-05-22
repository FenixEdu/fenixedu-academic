package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentCurricularPlanForEnrollments extends FenixService {

    protected StudentCurricularPlan run(Integer executionDegreeId, Registration registration) throws FenixServiceException {
        return findStudentCurricularPlan(registration);
    }

    protected StudentCurricularPlan findStudentCurricularPlan(final Registration registration) throws ExistingServiceException {
        if (registration == null) {
            throw new ExistingServiceException("student");
        }
        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            throw new ExistingServiceException("studentCurricularPlan");
        }
        return studentCurricularPlan;
    }
    // Service Invokers migrated from Berserk

    private static final ReadStudentCurricularPlanForEnrollments serviceInstance = new ReadStudentCurricularPlanForEnrollments();

    @Service
    public static StudentCurricularPlan runReadStudentCurricularPlanForEnrollments(Integer executionDegreeId, Registration registration) throws FenixServiceException  , NotAuthorizedException {
        EnrollmentAuthorizationFilter.instance.execute(executionDegreeId, registration);
        return serviceInstance.run(executionDegreeId, registration);
    }

}