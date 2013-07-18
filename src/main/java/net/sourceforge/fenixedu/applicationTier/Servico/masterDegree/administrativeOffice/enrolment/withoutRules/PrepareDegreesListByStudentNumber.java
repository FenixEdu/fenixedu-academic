package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.MasterDegreeEnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class PrepareDegreesListByStudentNumber {

    // student and degreeType used by filter
    public List<ExecutionDegree> run(final Registration registration, final DegreeType degreeType,
            final ExecutionSemester executionSemester) throws FenixServiceException {

        if (registration.getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
            return ExecutionDegree.getAllByExecutionYearAndDegreeType(executionSemester.getExecutionYear(),
                    DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
        }

        final List<ExecutionDegree> executionDegrees =
                (degreeType == null) ? ExecutionDegree.getAllByExecutionYear(executionSemester.getExecutionYear()) : ExecutionDegree
                        .getAllByExecutionYearAndDegreeType(executionSemester.getExecutionYear(), degreeType);

        if (executionDegrees.isEmpty()) {
            throw new FenixServiceException("errors.impossible.operation");
        }
        return executionDegrees;
    }

    // Service Invokers migrated from Berserk

    private static final PrepareDegreesListByStudentNumber serviceInstance = new PrepareDegreesListByStudentNumber();

    @Service
    public static List<ExecutionDegree> runPrepareDegreesListByStudentNumber(Registration registration, DegreeType degreeType,
            ExecutionSemester executionSemester) throws FenixServiceException, NotAuthorizedException {
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