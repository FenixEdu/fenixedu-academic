/*
 * ICursoExecucaoPersistente.java
 * 
 * Created on 2 de Novembro de 2002, 21:14
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author rpfi
 */
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface IPersistentExecutionDegree extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;
    
    /**
     * @param cursoExecucao
     * @throws ExcepcaoPersistencia
     */
    public void delete(IExecutionDegree cursoExecucao) throws ExcepcaoPersistencia;

    /**
     * Method readByExecutionYear.
     * 
     * @param executionYear
     * @return List
     */
    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia;

    /**
     * @param degree
     * @param executionYear
     * @return IExecutionDegree
     */
    public IExecutionDegree readByDegreeCurricularPlanAndExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param degreeCurricularPlanID
     * @param executionYearID
     * @return
     * @throws ExcepcaoPersistencia
     */
    public IExecutionDegree readByDegreeCurricularPlanIDAndExecutionYear(
            Integer degreeCurricularPlanID, String executionYear)
            throws ExcepcaoPersistencia;
    
    /**
     * @param degreeInitials
     * @param nameDegreeCurricularPlan
     * @param executionYear
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public IExecutionDegree readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
            String degreeInitials, String nameDegreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia;

    /**
     * @param degree
     * @param executionYear
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    /**
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
    public List readByExecutionYearAndDegreeType(IExecutionYear executionYear, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    /**
     * @param executionCourse
     * @param degreeCurricularPlans
     */
    public List readByExecutionYearAndDegreeCurricularPlans(IExecutionYear executionYear,
            Collection degreeCurricularPlans) throws ExcepcaoPersistencia;

    /**
     * @param degreeCurricularPlan
     * @return The List Of executions for this degree
     * @throws ExcepcaoPersistencia
     */
    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia;

    /**
     * @param degreeCurricularPlanID
     * @return IExecutionDegree This method assumes thar there's only one
     *         Execution Degree for each Degree Curricular Plan. This is the
     *         case with the Master Degrees
     */
    public IExecutionDegree readbyDegreeCurricularPlanID(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia;

    /**
     * @param code
     * @param name
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public IExecutionDegree readByDegreeCodeAndDegreeCurricularPlanName(String code, String name)
            throws ExcepcaoPersistencia;

    public List readListByDegreeNameAndExecutionYear(String name, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readListByDegreeNameAndExecutionYearAndDegreeType(String name,
            IExecutionYear executionYear, DegreeType degreeType) throws ExcepcaoPersistencia;

    /**
     * @param executionCourse
     * @param degree
     */
    public List readExecutionsDegreesByDegree(IDegree degree) throws ExcepcaoPersistencia;

    /**
     * @param executionCourse
     * @param teacher
     * @return The list of Execution Degrees for this Execution Course and one
     *         of the responsibles is Teacher
     * @throws ExcepcaoPersistencia
     */
    public List readByExecutionCourseAndByTeacher(IExecutionCourse executionCourse, ITeacher teacher)
            throws ExcepcaoPersistencia;

    public List readByDegreeAndExecutionYearList(String degreeCode, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    /**
     * @param name
     * @param executionYear
     * @return The Execution Degree for this Execution Year and Degree
     *         Curricular Plan name
     * @throws ExcepcaoPersistencia
     */
    public IExecutionDegree readByDegreeCurricularPlanNameAndExecutionYear(String name,
            IExecutionYear executionYear) throws ExcepcaoPersistencia;

    public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia;

    public List readByExecutionYearOIDAndDegreeType(Integer executionYearOID, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    public List readExecutionDegreesbyDegreeCurricularPlanID(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia;

    public IExecutionDegree readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
            Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia;

    public List readByExecutionYearOID(Integer executionYearOID) throws ExcepcaoPersistencia;
}