/*
 * ICursoExecucaoPersistente.java
 * 
 * Created on 2 de Novembro de 2002, 21:14
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author rpfi
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface IPersistentExecutionDegree extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia;

    public IExecutionDegree readByDegreeCurricularPlanAndExecutionYear(String degreeCurricularPlanName,
            String degreeCurricularPlanAcronym, String year) throws ExcepcaoPersistencia;

    public IExecutionDegree readByDegreeCurricularPlanIDAndExecutionYear(Integer degreeCurricularPlanID,
            String executionYear) throws ExcepcaoPersistencia;

    public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia;

    public List readByDegreeAndExecutionYear(Integer degreeOID, String year) throws ExcepcaoPersistencia;

    public List readByTeacher(Integer teacherOID) throws ExcepcaoPersistencia;

    public List readByExecutionYearAndDegreeType(Integer executionYearOID, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlan(Integer degreeCurricularPlanOID) throws ExcepcaoPersistencia;

    public List readExecutionsDegreesByDegree(Integer degreeOID) throws ExcepcaoPersistencia;

    public List readByExecutionCourseAndByTeacher(Integer executionCourseOID, Integer teacherOID)
            throws ExcepcaoPersistencia;

    public IExecutionDegree readByDegreeCurricularPlanNameAndExecutionYear(String name,
            Integer executionYearOID) throws ExcepcaoPersistencia;

    public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia;

    public IExecutionDegree readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
            Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia;

    public List readByExecutionYearOID(Integer executionYearOID) throws ExcepcaoPersistencia;
}