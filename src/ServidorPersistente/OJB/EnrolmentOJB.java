package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentOJB extends ObjectFenixOJB implements IPersistentEnrolment
{

    public void lockWrite(IEnrolment enrolmentToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IEnrolment enrolmentFromDB = null;

        // If there is nothing to write, simply return.
        if (enrolmentToWrite == null)
        {
            return;
        }

        // Read Enrolment from database.
        enrolmentFromDB =
            this.readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
                enrolmentToWrite.getStudentCurricularPlan(),
                enrolmentToWrite.getCurricularCourseScope(),
                enrolmentToWrite.getExecutionPeriod());
        // If Enrolment is not in database, then write it.
        if (enrolmentFromDB == null)
        {
            super.lockWrite(enrolmentToWrite);
            // else If the Enrolment is mapped to the database, then write any
            // existing changes.
        }
        else if (
            (enrolmentToWrite instanceof Enrolment)
                && ((Enrolment) enrolmentFromDB).getIdInternal().equals(
                    ((Enrolment) enrolmentToWrite).getIdInternal()))
        {
            super.lockWrite(enrolmentToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(enrolment);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "studentCurricularPlan.student.number",
            studentCurricularPlan.getStudent().getNumber());
        crit.addEqualTo(
            "studentCurricularPlan.student.degreeType",
            studentCurricularPlan.getStudent().getDegreeType());
        crit.addEqualTo("studentCurricularPlan.currentState", studentCurricularPlan.getCurrentState());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.name",
            curricularCourseScope.getCurricularCourse().getName());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.code",
            curricularCourseScope.getCurricularCourse().getCode());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.name",
            curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome",
            curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla",
            curricularCourseScope
                .getCurricularCourse()
                .getDegreeCurricularPlan()
                .getDegree()
                .getSigla());
        crit.addEqualTo(
            "curricularCourseScope.curricularSemester.semester",
            curricularCourseScope.getCurricularSemester().getSemester());
        crit.addEqualTo(
            "curricularCourseScope.curricularSemester.curricularYear.year",
            curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.name",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.degree.nome",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getNome());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.degree.sigla",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getSigla());
        crit.addEqualTo(
            "curricularCourseScope.branch.code",
            curricularCourseScope.getBranch().getCode());
        return (IEnrolment) queryObject(Enrolment.class, crit);

    }

    public List readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeList(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "studentCurricularPlan.student.number",
            studentCurricularPlan.getStudent().getNumber());
        crit.addEqualTo(
            "studentCurricularPlan.student.degreeType",
            studentCurricularPlan.getStudent().getDegreeType());
        crit.addEqualTo("studentCurricularPlan.currentState", studentCurricularPlan.getCurrentState());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.name",
            curricularCourseScope.getCurricularCourse().getName());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.code",
            curricularCourseScope.getCurricularCourse().getCode());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.name",
            curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome",
            curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla",
            curricularCourseScope
                .getCurricularCourse()
                .getDegreeCurricularPlan()
                .getDegree()
                .getSigla());
        crit.addEqualTo(
            "curricularCourseScope.curricularSemester.semester",
            curricularCourseScope.getCurricularSemester().getSemester());
        crit.addEqualTo(
            "curricularCourseScope.curricularSemester.curricularYear.year",
            curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.name",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.degree.nome",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getNome());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.degree.sigla",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getSigla());
        crit.addEqualTo(
            "curricularCourseScope.branch.code",
            curricularCourseScope.getBranch().getCode());
        crit.addEqualTo("enrolmentState", EnrolmentState.ENROLED);
        return queryList(Enrolment.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia
    {

        return queryList(Enrolment.class, new Criteria());
    }

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
        IStudentCurricularPlan studentCurricularPlan,
        EnrolmentState enrolmentState)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "studentCurricularPlan.student.number",
            studentCurricularPlan.getStudent().getNumber());
        crit.addEqualTo(
            "studentCurricularPlan.student.degreeType",
            studentCurricularPlan.getStudent().getDegreeType());
        crit.addEqualTo("studentCurricularPlan.currentState", studentCurricularPlan.getCurrentState());
        crit.addEqualTo("enrolmentState", enrolmentState);
        return queryList(Enrolment.class, crit);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentEnrolment#readAllByStudentCurricularPlan(Dominio.IStudentCurricularPlan)
	 */
    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlanKey", studentCurricularPlan.getIdInternal());
        List result = queryList(Enrolment.class, criteria);
        return result;

    }

    /**
	 * @deprecated
	 */
    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo(
            "studentCurricularPlan.student.number",
            studentCurricularPlan.getStudent().getNumber());
        crit.addEqualTo(
            "studentCurricularPlan.student.degreeType",
            studentCurricularPlan.getStudent().getDegreeType());
        crit.addEqualTo("studentCurricularPlan.currentState", studentCurricularPlan.getCurrentState());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.name",
            curricularCourseScope.getCurricularCourse().getName());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.code",
            curricularCourseScope.getCurricularCourse().getCode());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.name",
            curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome",
            curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla",
            curricularCourseScope
                .getCurricularCourse()
                .getDegreeCurricularPlan()
                .getDegree()
                .getSigla());
        crit.addEqualTo(
            "curricularCourseScope.curricularSemester.semester",
            curricularCourseScope.getCurricularSemester().getSemester());
        crit.addEqualTo(
            "curricularCourseScope.curricularSemester.curricularYear.year",
            curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.name",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.degree.nome",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getNome());
        crit.addEqualTo(
            "curricularCourseScope.branch.degreeCurricularPlan.degree.sigla",
            curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getSigla());
        crit.addEqualTo(
            "curricularCourseScope.branch.code",
            curricularCourseScope.getBranch().getCode());
        crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());

        return (IEnrolment) queryObject(Enrolment.class, crit);

    }

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "studentCurricularPlan.student.number",
            studentCurricularPlan.getStudent().getNumber());
        crit.addEqualTo(
            "studentCurricularPlan.student.degreeType",
            studentCurricularPlan.getStudent().getDegreeType());
        crit.addEqualTo("studentCurricularPlan.currentState", studentCurricularPlan.getCurrentState());
        crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        return queryList(Enrolment.class, crit);

    }

    public List readByCurricularCourseScope(ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseScope.idInternal", curricularCourseScope.getIdInternal());

        return queryList(Enrolment.class, crit);

    }

    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, String year)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo(
            "curricularCourseScope.curricularCourse.idInternal",
            curricularCourse.getIdInternal());
        crit.addEqualTo("executionPeriod.executionYear.year", year);

        return queryList(Enrolment.class, crit);

    }
    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse,
        String year)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "curricularCourseScope.curricularCourse.idInternal",
            curricularCourse.getIdInternal());
        criteria.addEqualTo("executionPeriod.executionYear.year", year);
        criteria.addEqualTo(
            "studentCurricularPlan.student.number",
            studentCurricularPlan.getStudent().getNumber());

        return (IEnrolment) queryObject(Enrolment.class, criteria);

    }

    public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(
        StudentCurricularPlanState state,
        EnrolmentState state2,
        IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.currentState", state);
        criteria.addEqualTo("enrolmentState", state2);
        criteria.addEqualTo(
            "studentCurricularPlan.degreeCurricularPlanKey",
            degreeCurricularPlan.getIdInternal());

        return queryList(Enrolment.class, criteria);
    }

    public IEnrolment readByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlanKey", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("curricularCourseScopeKey", curricularCourseScope.getIdInternal());
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());

        return (IEnrolment) queryObject(Enrolment.class, criteria);
    }

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "curricularCourseScope.curricularCourse.idInternal",
            curricularCourse.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo(
            "curricularCourseScope.curricularCourse.idInternal",
            curricularCourse.getIdInternal());
        criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        criteria.addEqualTo("enrolmentState", EnrolmentState.APROVED);
        return queryList(Enrolment.class, criteria);
    }
}