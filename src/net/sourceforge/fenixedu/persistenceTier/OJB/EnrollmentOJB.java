package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrollmentOJB extends PersistentObjectOJB implements IPersistentEnrollment {

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(Integer studentCurricularPlanID,
            EnrollmentState enrollmentState) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanID);
        criteria.addEqualTo("enrollmentState", enrollmentState);
        return queryList(Enrolment.class, criteria);
    }

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
            Integer studentCurricularPlanId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanId);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourseAndYear(Integer curricularCourseId, String year)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        crit.addEqualTo("executionPeriod.executionYear.year", year);
        return queryList(Enrolment.class, crit);
    }

    public List readByCurricularCourseAndExecutionPeriod(Integer curricularCourseId,
            Integer executionPeriodId) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        crit.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        return queryList(Enrolment.class, crit);
    }

    public IEnrolment readEnrolmentByStudentNumberAndCurricularCourse(
            Integer studentNumber, Integer curricularCourseId, String year)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        criteria.addEqualTo("executionPeriod.executionYear.year", year);
        criteria.addEqualTo("studentCurricularPlan.student.number", studentNumber);
        return (IEnrolment) queryObject(Enrolment.class, criteria);
    }

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
            Integer studentCurricularPlanId, Integer curricularCourseId,
            Integer executionPeriodId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanId);
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriodId);
        criteria.addEqualTo("enrollmentState", EnrollmentState.APROVED);
        return queryList(Enrolment.class, criteria);
    }

    public IEnrolment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
            Integer studentCurricularPlanId, Integer curricularCourseId,
            Integer executionPeriodId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanId);
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        return (IEnrolment) queryObject(Enrolment.class, criteria);
    }

    public List readByStudentCurricularPlanAndCurricularCourse(
            Integer studentCurricularPlanId, Integer curricularCourseId)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanId);
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        return queryList(Enrolment.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEnrolment#readEnrollmentsByStudentAndCurricularCourseNameAndCode(Dominio.IStudent,
     *      Dominio.ICurricularCourse)
     */
    public List readEnrollmentsByStudentAndCurricularCourseNameAndDegree(Integer studentId,
            String curricularCourseName, Integer degreeId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.student.idInternal", studentId);
        criteria.addEqualTo("curricularCourse.name", curricularCourseName);
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.idInternal", degreeId);

        return queryList(Enrolment.class, criteria);
    }

    public int countEnrolmentsByCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourseID);
        crit.addEqualTo("executionPeriod.idInternal", executionPeriodID);
        return count(Enrolment.class, crit);
    }

    public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(Integer studentId)
            throws ExcepcaoPersistencia {

        int numberCompletedCourses = countCompletedCoursesInActiveStudentCurricularPlans(studentId);

        IStudentCurricularPlan studentCurricularPlan = getStudentsActiveUndergraduateCurricularPlan(studentId);
        if ((studentCurricularPlan != null)
                && !(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla()
                        .equals("LEEC"))) {
            List enrolmentsInOtherStudentCurricularPlans = getApprovedEnrolmentsInOtherStudentCurricularPlans(studentId);
            for (int i = 0; i < enrolmentsInOtherStudentCurricularPlans.size(); i++) {
                IEnrolment enrolment = (IEnrolment) enrolmentsInOtherStudentCurricularPlans.get(i);

                int numberOfEquivilantEnrolments = countEnrolmentEquivalences(enrolment);

                if (numberOfEquivilantEnrolments == 0) {
                    numberCompletedCourses++;
                }
            }
        }

        return numberCompletedCourses;
    }

	private int countEnrolmentEquivalences(IEnrolment enrolment) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("equivalentEnrolment.idInternal", enrolment.getIdInternal());
		return count(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
	}

	private int countCompletedCoursesInActiveStudentCurricularPlans(Integer studentId) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.student.idInternal", studentId);
		criteria.addEqualTo("studentCurricularPlan.currentState", StudentCurricularPlanState.ACTIVE);
		criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.tipoCurso",DegreeType.DEGREE);
		criteria.addEqualTo("enrollmentState", EnrollmentState.APROVED);
		return count(Enrolment.class, criteria);
	}

	private List getApprovedEnrolmentsInOtherStudentCurricularPlans(Integer studentId) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.student.idInternal", studentId);
		criteria.addNotEqualTo("studentCurricularPlan.currentState",
		        StudentCurricularPlanState.ACTIVE);
		criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.tipoCurso",
		        DegreeType.DEGREE);
		criteria.addEqualTo("enrollmentState", EnrollmentState.APROVED);
		return queryList(Enrolment.class, criteria);
	}

    private IStudentCurricularPlan getStudentsActiveUndergraduateCurricularPlan(Integer studentId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", studentId);
        criteria.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", DegreeType.DEGREE);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
    }
}