/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentCurriculum implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentCurriculum() {
    }

    public List run(Integer executionDegreeCode, Integer studentCurricularPlanID)
            throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;

        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente()
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
        List result = new ArrayList();

        GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
        while (iterator.hasNext()) {
            IEnrollment enrolmentTemp = (IEnrollment) iterator.next();

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrollmentGrade.run(enrolmentTemp);

            InfoEnrolment infoEnrolment = InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                    .newInfoFromDomain(enrolmentTemp);

            infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

            result.add(infoEnrolment);
        }

        return result;
    }    
}