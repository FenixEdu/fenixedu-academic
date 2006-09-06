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
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Luis Cruz
 *  
 */
public class ClassEnrollmentAuthorizationFilter extends Filtro {

    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    private static String comparableDateFormatString = "yyyyMMddHHmm";

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {

        Registration registration = getRemoteUser(request).getPerson().getStudentByUsername();
        if(registration == null){
            registration = getRemoteUser(request).getPerson().getStudentByType(DegreeType.DEGREE);
        }
        
        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan(); 
        
        if (studentCurricularPlan != null) {
        	EnrolmentPeriodInClasses enrolmentPeriodInClasses = studentCurricularPlan.getDegreeCurricularPlan().getCurrentClassesEnrollmentPeriod();
            if (enrolmentPeriodInClasses == null || enrolmentPeriodInClasses.getStartDateDateTime() == null
                    || enrolmentPeriodInClasses.getEndDateDateTime() == null) {
                throw new CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan();
            }

            Date now = new Date();
            Date startDate = enrolmentPeriodInClasses.getStartDate();
            Date endDate = enrolmentPeriodInClasses.getEndDate();
            
            if(DateFormatUtil.compareDates(comparableDateFormatString, startDate, now) > 0 || DateFormatUtil.compareDates(comparableDateFormatString, endDate, now) < 0) { 
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