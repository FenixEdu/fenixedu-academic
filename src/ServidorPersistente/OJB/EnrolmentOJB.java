package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Enrolment;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import Util.CurricularCourseType;
import Util.EnrollmentState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentOJB extends ObjectFenixOJB implements
        IPersistentEnrollment {

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

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
            IStudentCurricularPlan studentCurricularPlan,
            EnrollmentState enrolmentState) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrolmentState", enrolmentState);
        return queryList(Enrolment.class, criteria);
    }

    public List readAllByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        List result = queryList(Enrolment.class, criteria);
        return result;
    }

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourseAndYear(
            ICurricularCourse curricularCourse, String year)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        crit.addEqualTo("executionPeriod.executionYear.year", year);
        return queryList(Enrolment.class, crit);
    }

    public List readByCurricularCourseAndExecutionPeriod(
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        crit.addEqualTo("executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(Enrolment.class, crit);
    }

    public IEnrollment readEnrolmentByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourse curricularCourse, String year)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        criteria.addEqualTo("executionPeriod.executionYear.year", year);
        criteria.addEqualTo("studentCurricularPlan.student.number",
                studentCurricularPlan.getStudent().getNumber());
        return (IEnrollment) queryObject(Enrolment.class, criteria);
    }

    public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(
            StudentCurricularPlanState state, EnrollmentState state2,
            IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.currentState", state);
        criteria.addEqualTo("enrolmentState", state2);
        criteria.addEqualTo(
                "studentCurricularPlan.degreeCurricularPlan.idInternal",
                degreeCurricularPlan.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        criteria.addEqualTo("enrolmentState", EnrollmentState.APROVED);
        return queryList(Enrolment.class, criteria);
    }

    public IEnrollment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return (IEnrollment) queryObject(Enrolment.class, criteria);
    }

    public List readByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readAllByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        return queryList(Enrolment.class, crit);
    }

    public List readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan,
            EnrollmentState enrolmentState, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrolmentState", enrolmentState);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEnrolment#readByCurricularCourseAndExecutionPeriodAndEnrolmentState(Dominio.ICurricularCourse,
     *      Dominio.IExecutionPeriod, Util.EnrolmentState)
     */
    public List readByCurricularCourseAndExecutionPeriodAndEnrolmentState(
            ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod, EnrollmentState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse
                .getIdInternal());
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        criteria.addEqualTo("enrolmentState", state);
        return queryList(Enrolment.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEnrolment#readEnrollmentsByStudentAndCurricularCourseNameAndCode(Dominio.IStudent,
     *      Dominio.ICurricularCourse)
     */
    public List readEnrollmentsByStudentAndCurricularCourseNameAndDegree(
            IStudent student, ICurricularCourse curricularCourse, ICurso degree) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.student.idInternal", student.getIdInternal());
        criteria.addEqualTo("curricularCourse.name", curricularCourse.getName());
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.idInternal", degree.getIdInternal());

        return queryList(Enrolment.class, criteria);
    }

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentStateAndCurricularCourseType(
            IStudentCurricularPlan studentCurricularPlan,
            EnrollmentState enrolmentState,
            CurricularCourseType curricularCourseType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal",
                studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrolmentState", enrolmentState);
        criteria.addEqualTo("curricularCourse.type", curricularCourseType);
        return queryList(Enrolment.class, criteria);
    }

    public int countEnrolmentsByCurricularCourseAndExecutionPeriod(
            Integer curricularCourseID, Integer executionPeriodID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourseID);
        crit.addEqualTo("executionPeriod.idInternal", executionPeriodID);
        return count(Enrolment.class, crit);
    }

    public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(
            IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.student.idInternal",
                student.getIdInternal());
        criteria.addEqualTo("studentCurricularPlan.currentState",
                StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.tipoCurso",
                TipoCurso.LICENCIATURA_OBJ);
        criteria.addEqualTo("enrolmentState", EnrollmentState.APROVED);

        int numberCompletedCourses = count(Enrolment.class, criteria);

        IStudentCurricularPlan studentCurricularPlan =
            	getStudentsActiveUndergraduateCurricularPlan(student);
        if ((studentCurricularPlan != null)
                && !(studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                        .getSigla().equals("LEEC"))) {
            criteria = new Criteria();
            criteria.addEqualTo("studentCurricularPlan.student.idInternal",
                    student.getIdInternal());
            criteria.addNotEqualTo("studentCurricularPlan.currentState",
                    StudentCurricularPlanState.ACTIVE_OBJ);
            criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.degree.tipoCurso",
                    TipoCurso.LICENCIATURA_OBJ);
            criteria.addEqualTo("enrolmentState", EnrollmentState.APROVED);

            List enrolmentsInOtherStudentCurricularPlans = queryList(Enrolment.class, criteria);
            for (int i = 0; i < enrolmentsInOtherStudentCurricularPlans.size(); i++) {
                IEnrollment enrolment = (IEnrollment) enrolmentsInOtherStudentCurricularPlans.get(i);

                criteria = new Criteria();
                criteria.addEqualTo("equivalentEnrolment.idInternal", enrolment.getIdInternal());
                int numberOfEquivilantEnrolments = count(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);

                if (numberOfEquivilantEnrolments == 0)
                {
                    numberCompletedCourses++;
                }
            }
        }

        return numberCompletedCourses;
    }

    private IStudentCurricularPlan getStudentsActiveUndergraduateCurricularPlan(
            IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        criteria.addEqualTo("currentState",
                StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso",
                TipoCurso.LICENCIATURA_OBJ);
        return (IStudentCurricularPlan) queryObject(
                StudentCurricularPlan.class, criteria);
    }

}