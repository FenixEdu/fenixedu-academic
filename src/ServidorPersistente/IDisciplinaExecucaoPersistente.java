package ServidorPersistente;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;

public interface IDisciplinaExecucaoPersistente extends IPersistentObject {
	public boolean apagarTodasAsDisciplinasExecucao();
	public boolean escreverDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao);
	
	/**
	 * @deprecated
	 * @param chaveLicenciaturaExecucao
	 * @param sigla
	 * @return IDisciplinaExecucao
	 */
	public IDisciplinaExecucao lerDisciplinaExecucao(
		int chaveLicenciaturaExecucao,
		String sigla);
		
	/**
	 * @deprecated
	 * @param chaveLicenciaturaExecucao
	 * @param sigla
	 * @return boolean
	 */	
	public boolean apagarDisciplinaExecucao(
		int chaveLicenciaturaExecucao,
		String sigla);
	public ArrayList lerTodasDisciplinaExecucao();

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

	/**
	 * 
	 * @param anoCurricular
	 * @param anoLectivo
	 * @param semestre
	 * @param siglaLicenciatura
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(
		Integer anoCurricular,
		String anoLectivo,
		Integer semestre,
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