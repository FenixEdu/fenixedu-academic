package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrollment extends Service {

    // some of these arguments may be null. they are only needed for filter
    public Integer run(Integer executionDegreeId, Integer studentCurricularPlanID,
            Integer curricularCourseID, Integer executionPeriodID,
            CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView)
            throws ExcepcaoPersistencia {
        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSupport.getIStudentCurricularPlanPersistente();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
        IPersistentCurricularCourse curricularCourseDAO = persistentSupport.getIPersistentCurricularCourse();

        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) studentCurricularPlanDAO.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        CurricularCourse curricularCourse = (CurricularCourse) curricularCourseDAO.readByOID(CurricularCourse.class, curricularCourseID);

        ExecutionPeriod executionPeriod = null;
        if (executionPeriodID == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (ExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,executionPeriodID);
        }

		Enrolment enrollment = studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,executionPeriod);

        if (enrollment == null) {

            Enrolment enrolmentToWrite;
            if (enrollmentClass == null || enrollmentClass.intValue() == 1 || enrollmentClass.intValue() == 0) {
                enrolmentToWrite = DomainFactory.makeEnrolment();
            } else if (enrollmentClass.intValue() == 2) {
                enrolmentToWrite = DomainFactory.makeEnrolmentInOptionalCurricularCourse();
            } else {
                enrolmentToWrite = DomainFactory.makeEnrolmentInExtraCurricularCourse();
            }
			
			enrolmentToWrite.initializeAsNew(studentCurricularPlan,curricularCourse,executionPeriod,getEnrollmentCondition(enrollmentType),userView.getUtilizador());

		} else {
            if (enrollment.getCondition() == EnrollmentCondition.INVISIBLE) {
                enrollment.setCondition(getEnrollmentCondition(enrollmentType));
            } if (enrollment.getEnrollmentState() == EnrollmentState.ANNULED) {
                enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
            }
        }

        return (enrollment != null) ? enrollment.getIdInternal() : null;
    }

	

    protected EnrollmentCondition getEnrollmentCondition(CurricularCourseEnrollmentType enrollmentType) {
        switch (enrollmentType) {
        case TEMPORARY:
            return EnrollmentCondition.TEMPORARY;
        case DEFINITIVE:
            return EnrollmentCondition.FINAL;
        case NOT_ALLOWED:
            return EnrollmentCondition.IMPOSSIBLE;
        case VALIDATED:
            return EnrollmentCondition.VALIDATED;
        default:
            return null;
        }
    }

}