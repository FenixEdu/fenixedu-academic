/*
 * ICursoExecucaoPersistente.java
 *
 * Created on 2 de Novembro de 2002, 21:14
 */

package ServidorPersistente;

/**
 *
 * @author  rpfi
 */
import java.util.Collection;
import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import Util.TipoCurso;

public interface ICursoExecucaoPersistente extends IPersistentObject {
	/**
	 * 
	 * @param cursoExecucao
	 * @throws ExcepcaoPersistencia
	 */
	public void lockWrite(ICursoExecucao cursoExecucao)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param cursoExecucao
	 * @throws ExcepcaoPersistencia
	 */
	public Boolean delete(ICursoExecucao cursoExecucao)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	public void deleteAll() throws ExcepcaoPersistencia;

	/**
	 * Method readByExecutionYear.
	 * @param executionYear
	 * @return List
	 */
	public List readByExecutionYear(IExecutionYear executionYear)
		throws ExcepcaoPersistencia;

	/**
	* 
	* @param degree
	* @param executionYear
	* @return ICursoExecucao
	*/
	public ICursoExecucao readByDegreeCurricularPlanAndExecutionYear(
		IDegreeCurricularPlan degreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param degreeInitials
	 * @param nameDegreeCurricularPlan
	 * @param executionYear
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public ICursoExecucao readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
		String degreeInitials,
		String nameDegreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param executionPeriod
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readMasterDegrees(String executionYear)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param degreeName
	 * @param executionYear
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public ICursoExecucao readByDegreeCodeAndExecutionYear(
		String degreeCode,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param teacher
	 * @return The list of Execution Degrees that this Teacher Coordinates
	 * @throws ExcepcaoPersistencia
	 */
	public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

	/**
	 * @param executionYear
	 * @param curso
	 * @return
	 */
	public List readByExecutionYearAndDegreeType(
		IExecutionYear executionYear,
		TipoCurso degreeType)
		throws ExcepcaoPersistencia;
		

	/**
	 * @param executionCourse
	 * @param degreeCurricularPlans
	 */
	public List readByExecutionYearAndDegreeCurricularPlans(IExecutionYear executionYear, Collection degreeCurricularPlans) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param degreeCurricularPlan
	 * @return The List Of executions for this degree
	 * @throws ExcepcaoPersistencia
	 */
	public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	
	
}
