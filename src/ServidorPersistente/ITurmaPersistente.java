/*
 * ITurmaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 18:38
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public interface ITurmaPersistente extends IPersistentObject {
	public void lockWrite(ITurma turma)
		throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(ITurma turma) throws ExcepcaoPersistencia;
	
	public List readAll() throws ExcepcaoPersistencia;

	public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(
		IExecutionPeriod executionPeriod,
		Integer curricularYear,
		ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param executionPeriod
	 * @return ArrayList
	 * @throws ExcepcaoPersistencia
	 */
	public List readByExecutionPeriod(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;

	/**
	 * Method readByNameAndExecutionDegreeAndExecutionPeriod.
	 * @param className
	 * @param executionDegree
	 * @param executionPeriod
	 * @return ITurma
	 */
	public ITurma readByNameAndExecutionDegreeAndExecutionPeriod(
		String className,
		ICursoExecucao executionDegree,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;

	public List readByDegreeNameAndDegreeCode(String name, String code)
		throws ExcepcaoPersistencia;

	public List readByExecutionDegree(ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia;

	public List readByExecutionPeriodAndDegreeType(
		IExecutionPeriod executionPeriod,
		TipoCurso curso)
		throws ExcepcaoPersistencia;

	public List readByExecutionDegreeAndExecutionPeriod(
		ICursoExecucao execucao,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;
	/**
	 * @param executionCourse
	 * @return
	 */
	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
}
