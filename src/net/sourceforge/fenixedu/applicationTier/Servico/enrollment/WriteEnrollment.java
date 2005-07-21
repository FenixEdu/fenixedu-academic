package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrollment implements IService {

    // some of these arguments may be null. they are only needed for filter
    public Integer run(Integer executionDegreeId, Integer studentCurricularPlanID,
            Integer curricularCourseID, Integer executionPeriodID,
            CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView)
            throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSuport.getIPersistentExecutionPeriod();
        IPersistentCurricularCourse curricularCourseDAO = persistentSuport.getIPersistentCurricularCourse();

        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourseDAO.readByOID(CurricularCourse.class, curricularCourseID);

        IExecutionPeriod executionPeriod = null;
        if (executionPeriodID == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,executionPeriodID);
        }

		IEnrolment enrollment = studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,executionPeriod);

        if (enrollment == null) {

            IEnrolment enrolmentToWrite;
            if (enrollmentClass == null || enrollmentClass.equals(new Integer(1))
                    || enrollmentClass.equals(new Integer(0))) {

                enrolmentToWrite = new Enrolment();
            } else if (enrollmentClass.equals(new Integer(2))) {
                enrolmentToWrite = new EnrolmentInOptionalCurricularCourse();
            } else {
                enrolmentToWrite = new EnrolmentInExtraCurricularCourse();
            }
			
			enrolmentToWrite.initializeAsNew(studentCurricularPlan,curricularCourse,executionPeriod,getEnrollmentCondition(enrollmentType),userView.getUtilizador());

		} else {
            if (enrollment.getCondition().equals(EnrollmentCondition.INVISIBLE)) {
                enrollment.setCondition(getEnrollmentCondition(enrollmentType));
            } if (enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED)) {
                enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
            }
        }

        if (enrollment != null) {
            return enrollment.getIdInternal();
        }
        return null;
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