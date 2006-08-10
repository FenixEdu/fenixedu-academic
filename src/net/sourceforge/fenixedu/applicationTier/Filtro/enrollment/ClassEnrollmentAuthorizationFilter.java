/*
 * Created on 2004/08/24
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Luis Cruz
 *  
 */
public class ClassEnrollmentAuthorizationFilter extends Filtro {

    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static SimpleDateFormat comparableDateFormat = new SimpleDateFormat("yyyyMMdd");

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {

        final Registration student = getRemoteUser(request).getPerson().getStudentByUsername();
        final StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan(); 
        
        if (studentCurricularPlan != null) {
        	EnrolmentPeriodInClasses enrolmentPeriodInClasses = studentCurricularPlan.getDegreeCurricularPlan().getCurrentClassesEnrollmentPeriod();
            if (enrolmentPeriodInClasses == null || enrolmentPeriodInClasses.getStartDateYearMonthDay() == null
                    || enrolmentPeriodInClasses.getEndDateYearMonthDay() == null) {
                throw new CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan();
            }

            Calendar now = Calendar.getInstance();
            Date startDate = enrolmentPeriodInClasses.getStartDate();
            Date endDate = enrolmentPeriodInClasses.getEndDate();

            Integer nowValue = new Integer(comparableDateFormat.format(now.getTime()));
            Integer startDateValue = new Integer(comparableDateFormat.format(startDate));
            Integer endDateValue = new Integer(comparableDateFormat.format(endDate));

            if ((nowValue.intValue() < startDateValue.intValue())
                    || (nowValue.intValue() > endDateValue.intValue())) {
                String startDateString = outputDateFormat.format(startDate);
                String endDateString = outputDateFormat.format(endDate);

                StringBuilder buffer = new StringBuilder();
                buffer.append(startDateString);
                buffer.append(" - ");
                buffer.append(endDateString);
                throw new OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(buffer
                        .toString());
            }
        } else {
            throw new NoActiveStudentCurricularPlanOfCorrectTypeException();
        }
    }

    public class NoActiveStudentCurricularPlanOfCorrectTypeException extends
            NotAuthorizedFilterException {
    }

    public class CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan extends
            NotAuthorizedFilterException {
    }

    public class OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan extends
            NotAuthorizedFilterException {
        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan() {
            super();
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(String message,
                Throwable cause) {
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