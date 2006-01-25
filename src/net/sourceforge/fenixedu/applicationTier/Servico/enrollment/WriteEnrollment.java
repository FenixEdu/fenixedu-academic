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
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrollment extends Service {

    // some of these arguments may be null. they are only needed for filter
    public Integer run(Integer executionDegreeId, Integer studentCurricularPlanID,
            Integer curricularCourseID, Integer executionPeriodID,
            CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView)
            throws ExcepcaoPersistencia {
        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();

        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(CurricularCourse.class, curricularCourseID);

        ExecutionPeriod executionPeriod = null;
        if (executionPeriodID == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class,executionPeriodID);
        }

		Enrolment enrollment = studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,executionPeriod);

        if (enrollment == null) {

            Enrolment enrolmentToWrite;
            if (enrollmentClass == null || enrollmentClass.intValue() == 1 || enrollmentClass.intValue() == 0) {
                enrolmentToWrite = DomainFactory.makeEnrolment(studentCurricularPlan,curricularCourse,executionPeriod,getEnrollmentCondition(enrollmentType),userView.getUtilizador());
            } else if (enrollmentClass.intValue() == 2) {
                enrolmentToWrite = DomainFactory.makeEnrolmentInOptionalCurricularCourse(studentCurricularPlan,curricularCourse,executionPeriod,getEnrollmentCondition(enrollmentType),userView.getUtilizador());
            } else {
                enrolmentToWrite = DomainFactory.makeEnrolmentInExtraCurricularCourse(studentCurricularPlan,curricularCourse,executionPeriod,getEnrollmentCondition(enrollmentType),userView.getUtilizador());
            }

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