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
import Dominio.IExecutionPeriod;
import Dominio.ITurma;

public interface ITurmaPersistente extends IPersistentObject {
	public void lockWrite(ITurma turma) throws ExcepcaoPersistencia;
	public void delete(ITurma turma) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	/**
	 * @deprecated
	 * @param semestre
	 * @param anoCurricular
	 * @param siglaLicenciatura
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readBySemestreAndAnoCurricularAndSiglaLicenciatura(
		Integer semestre,
		Integer anoCurricular,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia;

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
	public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
		
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
		IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
		
	public ITurma readByNameAndExecutionDegreeAndExecutionPeriodAndCurricularYear(
		String className,
		ICursoExecucao executionDegree,
		IExecutionPeriod executionPeriod, 
		Integer curricularYear) throws ExcepcaoPersistencia;
		
	public List readByDegreeNameAndDegreeCode(String name, String code) throws ExcepcaoPersistencia;
		
	
		
}
