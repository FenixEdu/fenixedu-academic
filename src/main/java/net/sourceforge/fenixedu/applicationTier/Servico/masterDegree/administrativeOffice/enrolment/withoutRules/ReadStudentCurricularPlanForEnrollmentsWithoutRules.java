package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;


import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.MasterDegreeEnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;

public class ReadStudentCurricularPlanForEnrollmentsWithoutRules {

    protected StudentCurricularPlan run(Registration registration, DegreeType degreeType, ExecutionSemester executionSemester)
            throws FenixServiceException {

        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
        }

        if (isStudentCurricularPlanFromChosenExecutionYear(studentCurricularPlan, executionSemester.getExecutionYear())) {
            return studentCurricularPlan;

        } else {
            throw new FenixServiceException("error.student.curriculum.not.from.chosen.execution.year");
        }
    }

    private boolean isStudentCurricularPlanFromChosenExecutionYear(StudentCurricularPlan studentCurricularPlan,
            ExecutionYear executionYear) {
        return studentCurricularPlan.getDegreeCurricularPlan().hasExecutionDegreeFor(executionYear);
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentCurricularPlanForEnrollmentsWithoutRules serviceInstance =
            new ReadStudentCurricularPlanForEnrollmentsWithoutRules();

    @Atomic
    public static StudentCurricularPlan runReadStudentCurricularPlanForEnrollmentsWithoutRules(Registration registration,
            DegreeType degreeType, ExecutionSemester executionSemester) throws FenixServiceException, NotAuthorizedException {
        try {
            EnrollmentWithoutRulesAuthorizationFilter.instance.execute(registration, degreeType);
            return serviceInstance.run(registration, degreeType, executionSemester);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeEnrollmentWithoutRulesAuthorizationFilter.instance.execute(registration, degreeType);
                return serviceInstance.run(registration, degreeType, executionSemester);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}