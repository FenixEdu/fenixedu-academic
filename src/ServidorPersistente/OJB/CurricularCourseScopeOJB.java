package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class CurricularCourseScopeOJB extends ObjectFenixOJB implements IPersistentCurricularCourseScope {

	

	public void lockWrite(ICurricularCourseScope curricularCourseScopeToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularCourseScope curricularCourseScopeFromDB = null;

		// If there is nothing to write, simply return.
		if (curricularCourseScopeToWrite == null) {
			return;
		}

		// Read CurricularCourseScope from database.
		curricularCourseScopeFromDB =
			this.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndBeginDate(
				curricularCourseScopeToWrite.getCurricularCourse(),
				curricularCourseScopeToWrite.getCurricularSemester(),
				curricularCourseScopeToWrite.getBranch(),
				curricularCourseScopeToWrite.getBeginDate());

		// If CurricularCourseScope is not in database, then write it.
		if (curricularCourseScopeFromDB == null) {
			super.lockWrite(curricularCourseScopeToWrite);
			// else If the CurricularCourseScope is mapped to the database, then write any existing changes.
		} else if (
			(curricularCourseScopeToWrite instanceof CurricularCourseScope)
				&& ((CurricularCourseScope) curricularCourseScopeFromDB).getIdInternal().equals(
					((CurricularCourseScope) curricularCourseScopeToWrite).getIdInternal())) {
			super.lockWrite(curricularCourseScopeToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularCourseScope scope) throws ExcepcaoPersistencia {
		try {
			Criteria crit = new Criteria();
			crit.addEqualTo("curricularCourseScopeKey", scope.getIdInternal());
			List result = queryList(Enrolment.class, crit);

			Iterator iter = result.iterator();
			EnrolmentOJB enrolmentOJB = new EnrolmentOJB();
			while (iter.hasNext()) {
				enrolmentOJB.delete((Enrolment) iter.next());
			}

			super.delete(scope);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
		ICurricularCourse curricularCourse,
		ICurricularSemester curricularSemester,
		IBranch branch)
		throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
		criteria.addEqualTo("branchKey", branch.getIdInternal());
		return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

	}

	public List readAll() throws ExcepcaoPersistencia {

		return queryList(CurricularCourseScope.class,new Criteria());
	}

	public List readCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		List result = queryList(CurricularCourseScope.class, crit);
		return result;
	}

	public List readByCurricularCourseAndSemesterAndBranch(ICurricularCourse curricularCourse, Integer semester, IBranch branch)
		throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		crit.addEqualTo("curricularSemester.semester", semester);
		crit.addEqualTo("branchKey", branch.getIdInternal());

		return queryList(CurricularCourseScope.class, crit);

	}

	public List readByCurricularCourseAndYearAndSemester(ICurricularCourse curricularCourse, Integer year, Integer semester)
		throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		crit.addEqualTo("curricularSemester.semester", semester);
		crit.addEqualTo("curricularSemester.curricularYear.year", year);

		return queryList(CurricularCourseScope.class, crit);
	}

	public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, Integer year) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		crit.addEqualTo("curricularSemester.curricularYear.year", year);

		return queryList(CurricularCourseScope.class, crit);
	}

	public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		crit.addEqualTo("curricularSemester.semester", semester);

		return queryList(CurricularCourseScope.class, crit);

	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentCurricularCourseScope#readByCurricularCourse(Dominio.ICurricularCourse)
	 */
	public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());

		return queryList(CurricularCourseScope.class, crit);

	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
		ICurricularCourse curricularCourse,
		ICurricularSemester curricularSemester,
		IBranch branch,
		Calendar endDate)
		throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
		criteria.addEqualTo("branchKey", branch.getIdInternal());
		if (endDate == null) {
			criteria.addIsNull("endDate");
		} else {
			criteria.addEqualTo("endDate", endDate);
		}
		return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

	}

	public List readActiveCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		crit.addIsNull("endDate");
		List result = queryList(CurricularCourseScope.class, crit);
		return result;
	}

	public List readActiveCurricularCourseScopesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
		crit.addIsNull("endDate");
		List result = queryList(CurricularCourseScope.class, crit);
		return result;
	}

	public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		crit.addLessThan("beginDate", executionPeriod.getEndDate());

		Criteria crit3 = new Criteria();
		crit3.addIsNull("endDate");

		Criteria crit4 = new Criteria();
		crit4.addGreaterThan("endDate", executionPeriod.getBeginDate());

		crit.addAndCriteria(crit3);
		crit3.addOrCriteria(crit4);

		List result = queryList(CurricularCourseScope.class, crit);
		return result;
	}
	
	public List readCurricularCourseScopesByCurricularCourseInExecutionYear(
			IDegreeCurricularPlan degreeCurricularPlan,
			IExecutionYear executionYear)
	throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
		crit.addLessThan("beginDate", executionYear.getEndDate());

		Criteria crit3 = new Criteria();
		crit3.addIsNull("endDate");

		Criteria crit4 = new Criteria();
		crit4.addGreaterThan("endDate", executionYear.getBeginDate());

		crit.addAndCriteria(crit3);
		crit3.addOrCriteria(crit4);

		List result = queryList(CurricularCourseScope.class, crit);
		return result;
	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndBeginDate(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch, Calendar beginDate) throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
		criteria.addEqualTo("branchKey", branch.getIdInternal());
		criteria.addEqualTo("beginDate", beginDate);
		return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

	}
	
	//added by Nuno Correia
	public List readByBranch(IBranch branch) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("branchKey", branch.getIdInternal());
		return queryList(CurricularCourseScope.class, criteria);
	}
}