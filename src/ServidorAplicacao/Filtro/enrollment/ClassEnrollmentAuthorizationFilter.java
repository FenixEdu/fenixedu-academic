/*
 * Created on 2004/08/24
 *
 */
package ServidorAplicacao.Filtro.enrollment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.IEnrolmentPeriodInClasses;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 *  
 */
public class ClassEnrollmentAuthorizationFilter extends Filtro {

    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static SimpleDateFormat comparableDateFormat = new SimpleDateFormat("yyyyMMdd");

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {

        IUserView id = getRemoteUser(request);

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        IPersistentEnrolmentPeriod persistentEnrolmentPeriod = persistentSupport
                .getIPersistentEnrolmentPeriod();

        IStudentCurricularPlan studentCurricularPlan = null;

        studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(id
                .getUtilizador(), TipoCurso.LICENCIATURA_OBJ);

        if (studentCurricularPlan == null) {
            studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(id
                    .getUtilizador(), TipoCurso.MESTRADO_OBJ);
        }

        if (studentCurricularPlan != null) {
            IEnrolmentPeriodInClasses enrolmentPeriodInClasses = persistentEnrolmentPeriod
                    .readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(studentCurricularPlan
                            .getDegreeCurricularPlan());
            if (enrolmentPeriodInClasses == null || enrolmentPeriodInClasses.getStartDate() == null
                    || enrolmentPeriodInClasses.getEndDate() == null) {
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

                StringBuffer buffer = new StringBuffer();
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