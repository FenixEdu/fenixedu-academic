package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public interface IDisciplinaExecucaoPersistente extends IPersistentObject {
	public List readAll() throws ExcepcaoPersistencia;
	public boolean apagarTodasAsDisciplinasExecucao();
	public void escreverDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao)
		throws ExcepcaoPersistencia, ExistingPersistentException;

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

	public void deleteExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	public List readByExecutionPeriod(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;
	/**
	 * @param executionPeriod
	 * @param curso
	 * @return
	 */
	public List readByExecutionPeriod(
		IExecutionPeriod executionPeriod,
		TipoCurso curso)
		throws ExcepcaoPersistencia;
	/**
	 * @param executionPeriod
	 * @param executionDegree
	 * @param curricularYear
	 * @param executionCourseName
	 * @return
	 */
	public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree,
		ICurricularYear curricularYear,
		String executionCourseName)
		throws ExcepcaoPersistencia;
		
//	returns a list of teachers in charge ids
//	public List readExecutionCourseTeachersInCharge(Integer executionCourseId) throws ExcepcaoPersistencia;
	  
	public List readExecutionCourseTeachers(Integer executionCourseId) throws ExcepcaoPersistencia ;
	public void lockWrite(IDisciplinaExecucao executionCourseToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public Boolean readSite(Integer executionCourseId) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IDisciplinaExecucao readbyCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

	public List readListbyCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
	
}