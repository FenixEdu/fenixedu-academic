/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.commons.student.GetEnrolmentGrade;
import ServidorAplicacao.Servico.equivalence.GetEnrollmentEvaluation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import framework.factory.ServiceManagerServiceFactory;

public class ReadStudentMarksListByCurricularCourse implements IService {

    public List run(IUserView userView, Integer curricularCourseID, String executionYear)
            throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        List enrolmentList = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(CurricularCourse.class, curricularCourseID);

            if (executionYear != null) {
                enrolmentList = sp.getIPersistentEnrolment().readByCurricularCourseAndYear(
                        curricularCourse, executionYear);
            } else {
                enrolmentList = sp.getIPersistentEnrolment().readByCurricularCourse(curricularCourse);
            }
            if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
                throw new NonExistingServiceException();
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return cleanList(enrolmentList, userView);
    }

    /**
     * @param enrollments
     * @return A list of Student curricular Plans without the duplicates
     */
    private List cleanList(List enrollments, IUserView userView) throws FenixServiceException {
        List result = new ArrayList();
        Integer numberAux = null;

        BeanComparator numberComparator = new BeanComparator("studentCurricularPlan.student.number");
        Collections.sort(enrollments, numberComparator);

        Iterator iterator = enrollments.iterator();
        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();

            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getStudent()
                            .getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getStudent().getNumber();

/*                Object args[] = { enrolment };
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) ServiceManagerServiceFactory
                        .executeService(userView, "GetEnrolmentGrade", args);
  */
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (new GetEnrolmentGrade()).run(enrolment);
                
                if (infoEnrolmentEvaluation != null) {

                    InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                            .newInfoFromDomain(enrolment);
                    infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);
                    result.add(infoEnrolment);
                }
            }
        }
        return result;
    }
}