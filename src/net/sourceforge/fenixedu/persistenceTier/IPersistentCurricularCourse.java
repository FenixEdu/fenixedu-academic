package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao 25/Mar/2003
 */

public interface IPersistentCurricularCourse extends IPersistentObject {

    /**
     * @deprecated
     */
    public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code)
            throws ExcepcaoPersistencia;
    
    public List readCurricularCoursesByName(String name)
    		throws ExcepcaoPersistencia;

    public ICurricularCourse readCurricularCourseByDegreeCurricularPlanAndNameAndCode(
            Integer degreeCurricularPlanId, String name, String code) throws ExcepcaoPersistencia;

    public List readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia;

    public List readCurricularCoursesBySemesterAndYear(Integer semester, Integer year)
            throws ExcepcaoPersistencia;

    public List readCurricularCoursesBySemesterAndYearAndBranch(Integer semester, Integer year,
            IBranch branch) throws ExcepcaoPersistencia;

    public List readCurricularCoursesBySemesterAndYearAndBranchAndNoBranch(Integer semester,
            Integer year, IBranch branch) throws ExcepcaoPersistencia;

    public List readCurricularCoursesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia;

    public List readAllCurricularCoursesByBranch(IBranch branch) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllCurricularCoursesBySemester(Integer semester /*
                                                                     * ,
                                                                     * IStudentCurricularPlan
                                                                     * studentCurricularPlan
                                                                     */
    ) throws ExcepcaoPersistencia;

    public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

    public List readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(
            IDegreeCurricularPlan degreeCurricularPlan, Boolean basic) throws ExcepcaoPersistencia;

    /**
     * @param string
     * @param degreeCurricularPlan
     * @return
     */
    public List readbyCourseCodeAndDegreeCurricularPlan(String curricularCourseCode,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    public List readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(String courseCode,
            DegreeType degreeType, DegreeCurricularPlanState degreeCurricularPlanState)
            throws ExcepcaoPersistencia;

    public List readbyCourseNameAndDegreeCurricularPlan(String curricularCourseName,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    public List readAllCurricularCoursesByDegreeCurricularPlanAndBranchAndSemester(
            IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, Integer semester)
            throws ExcepcaoPersistencia;

    public List readCurricularCoursesByDegreeCurricularPlanAndMandatoryAttribute(
            IDegreeCurricularPlan degreeCurricularPlan, Boolean mandatory) throws ExcepcaoPersistencia;

    public List readAllByDegreeCurricularPlanAndType(IDegreeCurricularPlan degreeCurricularPlan,
            CurricularCourseType curricularCourseType) throws ExcepcaoPersistencia;

    /**
     * @param curso
     * @param executionYear
     */
    public List readExecutedCurricularCoursesByDegreeAndExecutionYear(IDegree curso,
            IExecutionYear executionYear) throws ExcepcaoPersistencia;

    /**
     * @param curso
     * @param year
     * @param executionYear
     * @return
     */
    public List readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(IDegree curso, Integer year,
            IExecutionYear executionYear) throws ExcepcaoPersistencia;
}