package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById implements IService {

    public Object run(Integer studentCurricularPlanId) throws FenixServiceException {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentStudentCurricularPlan
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);

            if (studentCurricularPlan != null) {
                infoStudentCurricularPlan = Cloner
                        .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
            }

            if (studentCurricularPlan.getEnrolments() != null) {
                List infoEnrolments = new ArrayList();

                ListIterator iterator = studentCurricularPlan.getEnrolments().listIterator();
                while (iterator.hasNext()) {
                    Enrolment enrolment = (Enrolment) iterator.next();
                    InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                            .newInfoFromDomain(enrolment);
                    infoEnrolments.add(infoEnrolment);
                }

                infoStudentCurricularPlan.setInfoEnrolments(infoEnrolments);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoStudentCurricularPlan;
    }
}