package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

/**
 * @author Tânia Pousão 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById extends Service {

    public Object run(Integer studentCurricularPlanId) throws ExcepcaoPersistencia {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);

        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch
                    .newInfoFromDomain(studentCurricularPlan);
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

        return infoStudentCurricularPlan;
    }
}