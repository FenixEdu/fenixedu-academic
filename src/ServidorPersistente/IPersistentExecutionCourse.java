package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.IExecutionDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Util.TipoCurso;

public interface IPersistentExecutionCourse extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    /**
     * @param sigla
     * @param anoLectivo
     * @param siglaLicenciatura
     * @return IDisciplinaExecucao
     * @throws ExcepcaoPersistencia
     */
    public IExecutionCourse readBySiglaAndAnoLectivoAndSiglaLicenciatura(String sigla,
            String anoLectivo, String siglaLicenciatura) throws ExcepcaoPersistencia;

    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer anoCurricular,
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia;

    //	public List
    // readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer
    // anoCurricular,
    //			   IExecutionPeriod executionPeriod, IExecutionCourse executionDegree)
    //			   throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndExecutionDegree(IExecutionPeriod executionPeriod,
            IExecutionDegree executionDegree) throws ExcepcaoPersistencia;

    /**
     * @param courseInitials
     * @param executionPeriod
     * @return IDisciplinaExecucao
     * @throws ExcepcaoPersistencia
     */
    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriod(String courseInitials,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriodId(String courseInitials,
            Integer executionPeriodId) throws ExcepcaoPersistencia;

    public void deleteExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @param curso
     * @return
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod, TipoCurso curso)
            throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @param executionDegree
     * @param curricularYear
     * @param executionCourseName
     * @return
     */
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree,
            ICurricularYear curricularYear, String executionCourseName) throws ExcepcaoPersistencia;

    public List readExecutionCourseTeachers(Integer executionCourseId) throws ExcepcaoPersistencia;

    public Boolean readSite(Integer executionCourseId) throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param executionPeriod
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readbyCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readListbyCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlanAndExecutionYear(IDegreeCurricularPlan degreeCurricularPlan,
            IExecutionYear executionYear) throws ExcepcaoPersistencia;

    public List readByExecutionDegreeAndExecutionPeriod(IExecutionDegree executionDegree,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    /**
     * @param responsabilitiesToAdd
     * @return
     */
    public List readByExecutionCourseIds(List executionCourseIds) throws ExcepcaoPersistencia;

    public List readByExecutionPeriodWithNoCurricularCourses(IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;
    public List readByCurricularYearAndAllExecutionPeriodAndExecutionDegree(Integer curricularYear,
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia;            
	public List readByCurricularYearAndExecutionPeriodAndExecutionDegreeList(Integer curricularYear,
					IExecutionPeriod executionPeriod, List executionDegreeList)
					throws ExcepcaoPersistencia;
               

}