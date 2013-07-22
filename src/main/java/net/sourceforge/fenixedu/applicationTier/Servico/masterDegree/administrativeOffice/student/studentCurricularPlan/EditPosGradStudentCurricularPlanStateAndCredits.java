package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditPosGradStudentCurricularPlanStateAndCredits {

    @Atomic
    public static void run(IUserView userView, String studentCurricularPlanId, String currentState, Double credits,
            String startDate, List<String> extraCurricularOIDs, String observations, String branchId, String specialization)
            throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        final StudentCurricularPlan scp = FenixFramework.getDomainObject(studentCurricularPlanId);
        if (scp == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person person = Person.readPersonByUsername(userView.getUtilizador());
        if (person == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Employee employee = person.getEmployee();
        if (employee == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Branch branch = FenixFramework.getDomainObject(branchId);
        if (branch == null) {
            throw new InvalidArgumentsServiceException();
        }

        scp.setStartDate(stringDateToCalendar(startDate).getTime());
        // scp.setCurrentState(newState);
        scp.setEmployee(employee);
        scp.setGivenCredits(credits);
        scp.setObservations(observations);
        scp.setBranch(branch);
        scp.setSpecialization(Specialization.valueOf(specialization));

        for (final Enrolment enrolment : scp.getEnrolmentsSet()) {
            if (extraCurricularOIDs.contains(enrolment.getExternalId())) {
                if (enrolment.isExtraCurricular()) {
                    enrolment.setIsExtraCurricular(false);
                } else {
                    enrolment.markAsExtraCurricular();
                }
            }
        }
    }

    private static Calendar stringDateToCalendar(String startDate) throws NumberFormatException {
        final Calendar result = Calendar.getInstance();

        String[] aux = startDate.split("/");
        result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aux[0]));
        result.set(Calendar.MONTH, Integer.parseInt(aux[1]) - 1);
        result.set(Calendar.YEAR, Integer.parseInt(aux[2]));

        return result;
    }

}