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
import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;

public interface ICursoExecucaoPersistente extends IPersistentObject {
	public void lockWrite(ICursoExecucao cursoExecucao)
		throws ExcepcaoPersistencia;
	public void delete(ICursoExecucao cursoExecucao)
		throws ExcepcaoPersistencia;
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
		IPlanoCurricularCurso degreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;
}
