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
import Util.SecretaryEnrolmentStudentReason;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public class SecretaryEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;
    
    private int LEIC_OLD_DCP = 10;

    public SecretaryEnrollmentRule(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();
            
            //check if student curricular plan is Old LEIC
            if(studentCurricularPlan.getDegreeCurricularPlan().getIdInternal().intValue() == LEIC_OLD_DCP) {
                throw new EnrolmentRuleDomainException(SecretaryEnrolmentStudentReason.LEIC_OLD_TYPE);
            }
            
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