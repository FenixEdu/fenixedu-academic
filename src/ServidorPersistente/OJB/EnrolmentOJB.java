package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

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

    public void deleteAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Enrolment.class.getName();
            super.deleteAll(oqlQuery);
        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

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
            // else If the Enrolment is mapped to the database, then write any existing changes.
        } else if (
            (enrolmentToWrite instanceof Enrolment)
                && ((Enrolment) enrolmentFromDB).getIdInternal().equals(
                    ((Enrolment) enrolmentToWrite).getIdInternal()))
        {
            super.lockWrite(enrolmentToWrite);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

    public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(enrolment);
        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    //	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
    //
    //		try {
    //			IEnrolment enrolment = null;
    //			String oqlQuery = "select all from " + Enrolment.class.getName();
    //			oqlQuery += " where studentCurricularPlan.student.number = $1";
    //			oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
    //			oqlQuery += " and studentCurricularPlan.currentState = $3";
    //			oqlQuery += " and curricularCourse.name = $4";
    //			oqlQuery += " and curricularCourse.code = $5";
    //			oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $6";
    //			oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.nome = $7";
    //			oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.sigla = $8";
    //
    //			query.create(oqlQuery);
    //
    //			query.bind(studentCurricularPlan.getStudent().getNumber());
    //			query.bind(studentCurricularPlan.getStudent().getDegreeType());
    //			query.bind(studentCurricularPlan.getCurrentState());
    //			query.bind(curricularCourse.getName());
    //			query.bind(curricularCourse.getCode());
    //			query.bind(curricularCourse.getDegreeCurricularPlan().getName());
    //			query.bind(curricularCourse.getDegreeCurricularPlan().getDegree().getNome());
    //			query.bind(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
    //
    //			List result = (List) query.execute();
    //			lockRead(result);
    //
    //			if ((result != null) && (result.size() != 0)) {
    //				enrolment = (IEnrolment) result.get(0);
    //			}
    //			return enrolment;
    //		} catch (QueryException ex) {
    //			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
    //		}
    //	}

    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {

        try
        {
            IEnrolment enrolment = null;
            String oqlQuery = "select all from " + Enrolment.class.getName();
            oqlQuery += " where studentCurricularPlan.student.number = $1";
            oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
            oqlQuery += " and studentCurricularPlan.currentState = $3";

            oqlQuery += " and curricularCourseScope.curricularCourse.name = $4";
            oqlQuery += " and curricularCourseScope.curricularCourse.code = $5";
            oqlQuery += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.name = $6";
            oqlQuery
                += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome = $7";
            oqlQuery
                += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla = $8";
            oqlQuery += " and curricularCourseScope.curricularSemester.semester = $9";
            oqlQuery += " and curricularCourseScope.curricularSemester.curricularYear.year = $10";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.name = $11";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.degree.nome = $12";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.degree.sigla = $13";
            oqlQuery += " and curricularCourseScope.branch.code = $14";

            query.create(oqlQuery);

            query.bind(studentCurricularPlan.getStudent().getNumber());
            query.bind(studentCurricularPlan.getStudent().getDegreeType());
            query.bind(studentCurricularPlan.getCurrentState());
            query.bind(curricularCourseScope.getCurricularCourse().getName());
            query.bind(curricularCourseScope.getCurricularCourse().getCode());
            query.bind(curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
            query.bind(
                curricularCourseScope
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getNome());
            query.bind(
                curricularCourseScope
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getSigla());
            query.bind(curricularCourseScope.getCurricularSemester().getSemester());
            query.bind(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
            query.bind(curricularCourseScope.getBranch().getDegreeCurricularPlan().getName());
            query.bind(
                curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getNome());
            query.bind(
                curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getSigla());
            query.bind(curricularCourseScope.getBranch().getCode());

            List result = (List) query.execute();
            lockRead(result);

            if ((result != null) && (result.size() != 0))
            {
                enrolment = (IEnrolment) result.get(0);
            }
            return enrolment;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeList(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {

        try
        {
            String oqlQuery = "select all from " + Enrolment.class.getName();
            oqlQuery += " where studentCurricularPlan.student.number = $1";
            oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
            oqlQuery += " and studentCurricularPlan.currentState = $3";
            oqlQuery += " and curricularCourseScope.curricularCourse.name = $4";
            oqlQuery += " and curricularCourseScope.curricularCourse.code = $5";
            oqlQuery += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.name = $6";
            oqlQuery
                += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome = $7";
            oqlQuery
                += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla = $8";
            oqlQuery += " and curricularCourseScope.curricularSemester.semester = $9";
            oqlQuery += " and curricularCourseScope.curricularSemester.curricularYear.year = $10";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.name = $11";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.degree.nome = $12";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.degree.sigla = $13";
            oqlQuery += " and curricularCourseScope.branch.code = $14";
            oqlQuery += " and enrolmentState = $15";

            query.create(oqlQuery);

            query.bind(studentCurricularPlan.getStudent().getNumber());
            query.bind(studentCurricularPlan.getStudent().getDegreeType());
            query.bind(studentCurricularPlan.getCurrentState());
            query.bind(curricularCourseScope.getCurricularCourse().getName());
            query.bind(curricularCourseScope.getCurricularCourse().getCode());
            query.bind(curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
            query.bind(
                curricularCourseScope
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getNome());
            query.bind(
                curricularCourseScope
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getSigla());
            query.bind(curricularCourseScope.getCurricularSemester().getSemester());
            query.bind(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
            query.bind(curricularCourseScope.getBranch().getDegreeCurricularPlan().getName());
            query.bind(
                curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getNome());
            query.bind(
                curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getSigla());
            query.bind(curricularCourseScope.getBranch().getCode());
            EnrolmentState state = EnrolmentState.ENROLED;
            query.bind(state);

            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAll() throws ExcepcaoPersistencia
    {

        try
        {
            String oqlQuery = "select all from " + Enrolment.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
        IStudentCurricularPlan studentCurricularPlan,
        EnrolmentState enrolmentState)
        throws ExcepcaoPersistencia
    {

        try
        {
            String oqlQuery = "select all from " + Enrolment.class.getName();
            oqlQuery += " where studentCurricularPlan.student.number = $1";
            oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
            oqlQuery += " and studentCurricularPlan.currentState = $3";
            oqlQuery += " and enrolmentState = $4";

            query.create(oqlQuery);

            query.bind(studentCurricularPlan.getStudent().getNumber());
            query.bind(studentCurricularPlan.getStudent().getDegreeType());
            query.bind(studentCurricularPlan.getCurrentState());
            query.bind(enrolmentState);

            List result = (List) query.execute();
            lockRead(result);

            return result;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentEnrolment#readAllByStudentCurricularPlan(Dominio.IStudentCurricularPlan)
     */
    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
        throws ExcepcaoPersistencia
    {
        //		List enrolments = null;
        //		try {
        //			String oqlQuery =
        //				"select all from "
        //					+ Enrolment.class.getName()
        //					+ " where studentCurricularPlan.student.number = $1"
        //					+ " and studentCurricularPlan.student.degreeType = $2"
        //					+ " and studentCurricularPlan.currentState = $3";
        //
        //			query.create(oqlQuery);
        //
        //			query.bind(studentCurricularPlan.getStudent().getNumber());
        //			query.bind(studentCurricularPlan.getStudent().getDegreeType());
        //			query.bind(studentCurricularPlan.getCurrentState());
        //
        //			enrolments = (List) query.execute();
        //			lockRead(enrolments);
        //		} catch (QueryException e) {
        //			e.printStackTrace();
        //			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        //		}
        //		return enrolments;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlanKey", studentCurricularPlan.getIdInternal());
        List result = queryList(Enrolment.class, criteria);
        return result;

    }

    //	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
    //		IStudentCurricularPlan studentCurricularPlan,
    //		ICurricularCourse curricularCourse,
    //		IExecutionPeriod executionPeriod)
    //		throws ExcepcaoPersistencia {
    //		try {
    //			IEnrolment enrolment = null;
    //			String oqlQuery = "select all from " + Enrolment.class.getName();
    //			oqlQuery += " where studentCurricularPlan.student.number = $1";
    //			oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
    //			oqlQuery += " and studentCurricularPlan.currentState = $3";
    //			oqlQuery += " and curricularCourse.name = $4";
    //			oqlQuery += " and curricularCourse.code = $5";
    //			oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $6";
    //			oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.nome = $7";
    //			oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.sigla = $8";
    //			oqlQuery += " and executionPeriod.name = $9";
    //			oqlQuery += " and executionPeriod.executionYear.year = $10";
    //
    //			query.create(oqlQuery);
    //
    //			query.bind(studentCurricularPlan.getStudent().getNumber());
    //			query.bind(studentCurricularPlan.getStudent().getDegreeType());
    //			query.bind(studentCurricularPlan.getCurrentState());
    //			query.bind(curricularCourse.getName());
    //			query.bind(curricularCourse.getCode());
    //			query.bind(curricularCourse.getDegreeCurricularPlan().getName());
    //			query.bind(curricularCourse.getDegreeCurricularPlan().getDegree()
    //					.getNome());
    //			query.bind(
    //				curricularCourse
    //					.getDegreeCurricularPlan()
    //					.getDegree()
    //					.getSigla());
    //
    //			query.bind(executionPeriod.getName());
    //			query.bind(executionPeriod.getExecutionYear().getYear());
    //
    //			List result = (List) query.execute();
    //			lockRead(result);
    //
    //			if ((result != null) && (result.size() != 0)) {
    //				enrolment = (IEnrolment) result.get(0);
    //			}
    //			return enrolment;
    //		} catch (QueryException ex) {
    //			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
    //		}
    //	}

    /**
     * @deprecated
     */
    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourseScope curricularCourseScope,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        try
        {
            IEnrolment enrolment = null;
            String oqlQuery = "select all from " + Enrolment.class.getName();
            oqlQuery += " where studentCurricularPlan.student.number = $1";
            oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
            oqlQuery += " and studentCurricularPlan.currentState = $3";

            oqlQuery += " and curricularCourseScope.curricularCourse.name = $4";
            oqlQuery += " and curricularCourseScope.curricularCourse.code = $5";
            oqlQuery += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.name = $6";
            oqlQuery
                += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome = $7";
            oqlQuery
                += " and curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla = $8";
            oqlQuery += " and curricularCourseScope.curricularSemester.semester = $9";
            oqlQuery += " and curricularCourseScope.curricularSemester.curricularYear.year = $10";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.name = $11";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.degree.nome = $12";
            oqlQuery += " and curricularCourseScope.branch.degreeCurricularPlan.degree.sigla = $13";
            oqlQuery += " and curricularCourseScope.branch.code = $14";

            oqlQuery += " and executionPeriod.name = $15";
            oqlQuery += " and executionPeriod.executionYear.year = $16";

            query.create(oqlQuery);

            query.bind(studentCurricularPlan.getStudent().getNumber());
            query.bind(studentCurricularPlan.getStudent().getDegreeType());
            query.bind(studentCurricularPlan.getCurrentState());

            query.bind(curricularCourseScope.getCurricularCourse().getName());
            query.bind(curricularCourseScope.getCurricularCourse().getCode());
            query.bind(curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
            query.bind(
                curricularCourseScope
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getNome());
            query.bind(
                curricularCourseScope
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getSigla());
            query.bind(curricularCourseScope.getCurricularSemester().getSemester());
            query.bind(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
            query.bind(curricularCourseScope.getBranch().getDegreeCurricularPlan().getName());
            query.bind(
                curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getNome());
            query.bind(
                curricularCourseScope.getBranch().getDegreeCurricularPlan().getDegree().getSigla());
            query.bind(curricularCourseScope.getBranch().getCode());

            query.bind(executionPeriod.getName());
            query.bind(executionPeriod.getExecutionYear().getYear());

            List result = (List) query.execute();
            lockRead(result);

            if ((result != null) && (result.size() != 0))
            {
                enrolment = (IEnrolment) result.get(0);
            }
            return enrolment;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Enrolment.class.getName();
            oqlQuery += " where studentCurricularPlan.student.number = $1";
            oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
            oqlQuery += " and studentCurricularPlan.currentState = $3";
            oqlQuery += " and executionPeriod.name = $4";
            oqlQuery += " and executionPeriod.executionYear.year = $5";

            query.create(oqlQuery);

            query.bind(studentCurricularPlan.getStudent().getNumber());
            query.bind(studentCurricularPlan.getStudent().getDegreeType());
            query.bind(studentCurricularPlan.getCurrentState());
            query.bind(executionPeriod.getName());
            query.bind(executionPeriod.getExecutionYear().getYear());

            List result = (List) query.execute();
            lockRead(result);

            return result;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
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
    	criteria.addEqualTo("curricularCourseScope.curricularCourse.idInternal", curricularCourse.getIdInternal());
    	return queryList(Enrolment.class, criteria);
    }

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addEqualTo("curricularCourseScope.curricularCourse.idInternal", curricularCourse.getIdInternal());
    	criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
    	criteria.addEqualTo("enrolmentState", EnrolmentState.APROVED);
    	return queryList(Enrolment.class, criteria);
    }
}