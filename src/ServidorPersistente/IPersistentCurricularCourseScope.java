package ServidorPersistente;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentCurricularCourseScope extends IPersistentObject {

	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(ICurricularCourseScope curricularCourseScopeToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia;
	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch) throws ExcepcaoPersistencia;
	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch, Calendar endDate) throws ExcepcaoPersistencia;
	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndBeginDate(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch, Calendar beginDate) throws ExcepcaoPersistencia;
	public List readCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
	public List readActiveCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
	public List readActiveCurricularCourseScopesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
	public ArrayList readAll() throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param curricularCourse
	 * @param semester
	 * @param branch
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByCurricularCourseAndSemesterAndBranch(ICurricularCourse curricularCourse, Integer semester, IBranch branch) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param curricularCourse
	 * @param year
	 * @param semester
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByCurricularCourseAndYearAndSemester(ICurricularCourse curricularCourse, Integer year, Integer semester) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param curricularCourse
	 * @param year
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, Integer year) throws ExcepcaoPersistencia;
	
	/**
	 * @param curricularCourse
	 * @param integer
	 * @return
	 */
	public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester) throws ExcepcaoPersistencia;
	/**
	 * @param curricularCourse
	 */
	public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}