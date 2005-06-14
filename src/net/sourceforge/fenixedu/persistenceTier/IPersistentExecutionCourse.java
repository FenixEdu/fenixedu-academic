package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface IPersistentExecutionCourse extends IPersistentObject {

    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer anoCurricular,
            Integer executionPeriodSemestre, String degreeCurricularPlanName, String degreeSigla,
            Integer executionPeriodID) throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndExecutionDegree(Integer executionPeriodID, String curricularPlanName, String degreeSigla) throws ExcepcaoPersistencia;

    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriodId(String courseInitials,
            Integer executionPeriodId) throws ExcepcaoPersistencia;

    public List readByExecutionPeriod(Integer executionPeriod, DegreeType curso)
            throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
            Integer executionPeriodID, Integer degreeCurricularPlanID,
            Integer curricularYearID, String executionCourseName) throws ExcepcaoPersistencia;

    public List readbyCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlanAndExecutionYear(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia;

    public List readByExecutionDegreeAndExecutionPeriod(Integer degreeCurricularPlanID,
            Integer executionPeriodID) throws ExcepcaoPersistencia;

    public List readByExecutionCourseIds(List<Integer> executionCourseIds) throws ExcepcaoPersistencia;

    public List readByExecutionPeriodWithNoCurricularCourses(Integer executionPeriodID)
            throws ExcepcaoPersistencia;

    public List readByCurricularYearAndAllExecutionPeriodAndExecutionDegree(Integer curricularYear,
            Integer executionPeriodID, String degreeCurricularPlanName, String degreeSigla)
            throws ExcepcaoPersistencia;

    public List readByCurricularYearAndExecutionPeriodAndExecutionDegreeList(Integer curricularYear,
            Integer executionPeriod, Integer executionPeriodSemestre, List<IExecutionDegree> executionDegreeList) throws ExcepcaoPersistencia;
}