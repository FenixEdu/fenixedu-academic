package ServidorPersistente;

import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface IDisciplinaExecucaoPersistente extends IPersistentObject {
	public boolean apagarTodasAsDisciplinasExecucao();
	public void escreverDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao) throws ExcepcaoPersistencia, ExistingPersistentException;

	/**
	 * 
	 * @param sigla
	 * @param anoLectivo
	 * @param siglaLicenciatura
	 * @return IDisciplinaExecucao
	 * @throws ExcepcaoPersistencia
	 */
	public IDisciplinaExecucao readBySiglaAndAnoLectivoAndSiglaLicenciatura(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia;



	public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(
		Integer anoCurricular,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param courseInitials
	 * @param executionPeriod
	 * @return IDisciplinaExecucao
	 * @throws ExcepcaoPersistencia
	 */
	public IDisciplinaExecucao readByExecutionCourseInitialsAndExecutionPeriod(
		String courseInitials,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;
		
		
	public void deleteExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
	

}