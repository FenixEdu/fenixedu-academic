package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoEnrolment;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById implements IServico {

    private static ReadPosGradStudentCurricularPlanById servico = new ReadPosGradStudentCurricularPlanById();

    public static ReadPosGradStudentCurricularPlanById getService() {
        return servico;
    }

    private ReadPosGradStudentCurricularPlanById() {
    }

    public final String getNome() {
        return "ReadPosGradStudentCurricularPlanById";
    }

    public Object run(Integer studentCurricularPlanId)
            throws FenixServiceException {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentStudentCurricularPlan
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);

            if (studentCurricularPlan != null) {
                infoStudentCurricularPlan = Cloner
                        .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
            }

            if (studentCurricularPlan.getEnrolments() != null) {
                List infoEnrolments = new ArrayList();

                ListIterator iterator = studentCurricularPlan.getEnrolments()
                        .listIterator();
                while (iterator.hasNext()) {
                    Enrolment enrolment = (Enrolment) iterator.next();
                    InfoEnrolment infoEnrolment = Cloner
                            .copyIEnrolment2InfoEnrolment(enrolment);
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