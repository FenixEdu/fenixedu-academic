package ServidorPersistente;

import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentCurricularCourse extends IPersistentObject {

	/**
	 * @deprecated
	 */
	public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
	
	public ICurricularCourse readCurricularCourseByDegreeCurricularPlanAndNameAndCode(Integer degreeCurricularPlanId, String name, String code) throws ExcepcaoPersistencia;
	public List readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia;
	public List readCurricularCoursesBySemesterAndYear(Integer semester, Integer year) throws ExcepcaoPersistencia;
	public List readCurricularCoursesBySemesterAndYearAndBranch(Integer semester, Integer year,IBranch branch) throws ExcepcaoPersistencia;
	public List readCurricularCoursesBySemesterAndYearAndBranchAndNoBranch(Integer semester, Integer year,IBranch branch) throws ExcepcaoPersistencia;
//	public ArrayList readCurricularCoursesByCurricularSemesterAndCurricularYear(Integer semester, Integer year) throws ExcepcaoPersistencia;
	public List readCurricularCoursesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	public List readAllCurricularCoursesByBranch(IBranch branch) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public List readAllCurricularCoursesBySemester(Integer semester/*, IStudentCurricularPlan studentCurricularPlan*/) throws ExcepcaoPersistencia;
	public void lockWrite(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(IDegreeCurricularPlan degreeCurricularPlan,Boolean basic) throws ExcepcaoPersistencia;

	/**
	 * @param string
	 * @param degreeCurricularPlan
	 * @return
	 */
	public List readbyCourseCodeAndDegreeCurricularPlan(String curricularCourseCode, IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

 	public List readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(String courseCode, TipoCurso degreeType, DegreeCurricularPlanState degreeCurricularPlanState) throws ExcepcaoPersistencia;
}