/*
 * Created on Feb 3, 2005
 *
 */
package Dominio.degree.enrollment.rules;

import java.util.List;

import Dominio.ISecretaryEnrolmentStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.exceptions.EnrolmentRuleDomainException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public class SecretaryEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;

    public SecretaryEnrollmentRule(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            Integer studentNumber = this.studentCurricularPlan.getStudent().getNumber();

            ISecretaryEnrolmentStudent secretaryEnrolmentStudent = persistentSuport
                    .getIPersistentSecretaryEnrolmentStudent().readByStudentNumber(studentNumber);

            if (secretaryEnrolmentStudent != null) {
                throw new EnrolmentRuleDomainException(secretaryEnrolmentStudent.getReasonType()
                        .getValue());
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return curricularCoursesToBeEnrolledIn;

    }
}