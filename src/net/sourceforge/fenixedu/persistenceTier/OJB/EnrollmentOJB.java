package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.util.CurricularCourseType;
import net.sourceforge.fenixedu.util.EnrollmentState;
import net.sourceforge.fenixedu.util.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrollmentOJB extends PersistentObjectOJB implements IPersistentEnrollment {

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Enrolment.class, new Criteria());
    }

    public void delete(IEnrollment enrolment) throws ExcepcaoPersistencia {
        try {
            super.delete(enrolment);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(Integer studentCurricularPlanID,
            EnrollmentState enrollmentState) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanID);
        criteria.addEqualTo("enrollmentState", enrollmentState);
        return queryList(Enrolment.class, criteria);
    }

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
            IStudentCurricularPlan studentCurricularPlan, EnrollmentState enrollmentState)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrollmentState", enrollmentState);
        return queryList(Enrolment.class, criteria);
    }

    public List readAllByStudentCurricularPlan(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanID);
        List result = queryList(Enrolment.class, criteria);
        return result;
    }

    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        List result = queryList(Enrolment.class, criteria);
        return result;
    }

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, String year)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addEqualTo("executionPeriod.executionYear.year", year);
        return queryList(Enrolment.class, crit);
    }

    public List readByCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(Enrolment.class, crit);
    }

    public IEnrollment readEnrolmentByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, String year)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("executionPeriod.executionYear.year", year);
        criteria.addEqualTo("studentCurricularPlan.student.number", studentCurricularPlan.getStudent()
                .getNumber());
        return (IEnrollment) queryObject(Enrolment.class, criteria);
    }

    public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(
            StudentCurricularPlanState state, EnrollmentState state2,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.currentState", state);
        criteria.addEqualTo("enrollmentState", state2);
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.idInternal",
                degreeCurricularPlan.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        criteria.addEqualTo("enrollmentState", EnrollmentState.APROVED);
        return queryList(Enrolment.class, criteria);
    }

    public IEnrollment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return (IEnrollment) queryObject(Enrolment.class, criteria);
    }

    public List readByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readAllByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return queryList(Enrolment.class, crit);
    }

    public List readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan, EnrollmentState enrollmentState,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrollmentState", enrollmentState);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEnrolment#readByCurricularCourseAndExecutionPeriodAndEnrolmentState(Dominio.ICurricularCourse,
     *      Dominio.IExecutionPeriod, Util.EnrolmentState)
     */
    public List readByCurricularCourseAndExecutionPeriodAndEnrolmentState(
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod, EnrollmentState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        criteria.addEqualTo("enrollmentState", state);
        return queryList(Enrolment.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEnrolment#readEnrollmentsByStudentAndCurricularCourseNameAndCode(Dominio.IStudent,
     *      Dominio.ICurricularCourse)
     */
    public List readEnrollmentsByStudentAndCurricularCourseNameAndDegree(IStudent student,
            ICurricularCourse curricularCourse, IDegree degree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.student.idInternal", student.getIdInternal());
        criteria.addEqualTo("curricularCourse.name", curricularCourse.getName());
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.idInternal", degree
                .getIdInternal());

        return queryList(Enrolment.class, criteria);
    }

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentStateAndCurricularCourseType(
            IStudentCurricularPlan studentCurricularPlan, EnrollmentState enrollmentState,
            CurricularCourseType curricularCourseType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrollmentState", enrollmentState);
        criteria.addEqualTo("curricularCourse.type", curricularCourseType);
        return queryList(Enrolment.class, criteria);
    }

    public int countEnrolmentsByCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourseID);
        crit.addEqualTo("executionPeriod.idInternal", executionPeriodID);
        return count(Enrolment.class, crit);
    }

    public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.student.idInternal", student.getIdInternal());
        criteria.addEqualTo("studentCurricularPlan.currentState", StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.tipoCurso",
                TipoCurso.LICENCIATURA_OBJ);
        criteria.addEqualTo("enrollmentState", EnrollmentState.APROVED);

        int numberCompletedCourses = count(Enrolment.class, criteria);

        IStudentCurricularPlan studentCurricularPlan = getStudentsActiveUndergraduateCurricularPlan(student);
        if ((studentCurricularPlan != null)
                && !(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla()
                        .equals("LEEC"))) {
            criteria = new Criteria();
            criteria.addEqualTo("studentCurricularPlan.student.idInternal", student.getIdInternal());
            criteria.addNotEqualTo("studentCurricularPlan.currentState",
                    StudentCurricularPlanState.ACTIVE_OBJ);
            criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.tipoCurso",
                    TipoCurso.LICENCIATURA_OBJ);
            criteria.addEqualTo("enrollmentState", EnrollmentState.APROVED);

            List enrolmentsInOtherStudentCurricularPlans = queryList(Enrolment.class, criteria);
            for (int i = 0; i < enrolmentsInOtherStudentCurricularPlans.size(); i++) {
                IEnrollment enrolment = (IEnrollment) enrolmentsInOtherStudentCurricularPlans.get(i);

                criteria = new Criteria();
                criteria.addEqualTo("equivalentEnrolment.idInternal", enrolment.getIdInternal());
                int numberOfEquivilantEnrolments = count(
                        EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);

                if (numberOfEquivilantEnrolments == 0) {
                    numberCompletedCourses++;
                }
            }
        }

        return numberCompletedCourses;
    }

    private IStudentCurricularPlan getStudentsActiveUndergraduateCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        criteria.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", TipoCurso.LICENCIATURA_OBJ);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
    }

}