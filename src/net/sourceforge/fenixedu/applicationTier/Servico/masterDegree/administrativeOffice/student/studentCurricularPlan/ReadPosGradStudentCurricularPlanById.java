package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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