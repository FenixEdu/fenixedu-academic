/*
 * Created on 2004/08/24
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

/**
 * @author Luis Cruz
 * 
 */
public class ClassEnrollmentAuthorizationFilter extends Filtro {

    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static String comparableDateFormatString = "yyyyMMddHHmm";

    @Override
    public void execute(Object[] parameters) throws Exception {
        Person person = AccessControl.getUserView().getPerson();

        final Registration registration = (Registration) parameters[0];

        if (AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.STUDENT_ENROLMENTS).contains(
                registration.getDegree())) {
            return;
        }

        if (person.hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            person = registration.getPerson();
        }

        if (person.getStudent().hasInquiriesToRespond()) {
            throw new InquiriesNotAnswered();
        }

        final SortedSet<StudentCurricularPlan> activeStudentCurricularPlans =
                person.getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName();

        if (activeStudentCurricularPlans.isEmpty()) {
            throw new NoActiveStudentCurricularPlanOfCorrectTypeException();
        }

        boolean hasOneOpen = false;
        Exception toThrow = null;
        for (final StudentCurricularPlan studentCurricularPlan : activeStudentCurricularPlans) {
            final Exception exception = verify(studentCurricularPlan);
            hasOneOpen = hasOneOpen || exception == null;
            toThrow = exception == null ? toThrow : exception;
        }
        if (!hasOneOpen) {
            throw toThrow;
        }
    }

    private Exception verify(StudentCurricularPlan studentCurricularPlan) {
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        Exception result = verify(degreeCurricularPlan);
        if (result == null) {
            return null;
        }
        for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : degreeCurricularPlan.getTargetEquivalencePlansSet()) {
            final DegreeCurricularPlan otherDegreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
            result = verify(otherDegreeCurricularPlan);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    private Exception verify(DegreeCurricularPlan degreeCurricularPlan) {
        final EnrolmentPeriodInClasses enrolmentPeriodInClasses = degreeCurricularPlan.getCurrentClassesEnrollmentPeriod();
        if (enrolmentPeriodInClasses == null || enrolmentPeriodInClasses.getStartDateDateTime() == null
                || enrolmentPeriodInClasses.getEndDateDateTime() == null) {
            return new CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan();
        }

        Date now = new Date();
        Date startDate = enrolmentPeriodInClasses.getStartDate();
        Date endDate = enrolmentPeriodInClasses.getEndDate();

        if (DateFormatUtil.compareDates(comparableDateFormatString, startDate, now) > 0
                || DateFormatUtil.compareDates(comparableDateFormatString, endDate, now) < 0) {
            String startDateString = outputDateFormat.format(startDate);
            String endDateString = outputDateFormat.format(endDate);

            StringBuilder buffer = new StringBuilder();
            buffer.append(startDateString);
            buffer.append(" - ");
            buffer.append(endDateString);
            return new OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(buffer.toString());
        }
        return null;
    }

    public class NoActiveStudentCurricularPlanOfCorrectTypeException extends NotAuthorizedFilterException {
    }

    public class CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan extends NotAuthorizedFilterException {
    }

    public class InquiriesNotAnswered extends NotAuthorizedFilterException {
    }

    public class OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan extends NotAuthorizedFilterException {
        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan() {
            super();
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(String message, Throwable cause) {
            super(message, cause);
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(Throwable cause) {
            super(cause);
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(String message) {
            super(message);
        }
    }

}