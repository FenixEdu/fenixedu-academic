/*
 * CursoExecucaoOJB.java
 * 
 * Created on 2 de Novembro de 2002, 21:17
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author rpfi
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

public class CursoExecucaoOJB extends PersistentObjectOJB implements IPersistentExecutionDegree {

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(ExecutionDegree.class, null);
    }

    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        return queryList(ExecutionDegree.class, criteria, "KEY_DEGREE_CURRICULAR_PLAN", true);
    }

    public ExecutionDegree readByDegreeCurricularPlanAndExecutionYear(String degreeCurricularPlanName,
            String degreeCurricularPlanAcronym, String year) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", year);
        criteria.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlanName);
        criteria.addEqualTo("degreeCurricularPlan.degree.sigla", degreeCurricularPlanAcronym);
        return (ExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    public ExecutionDegree readByDegreeCurricularPlanIDAndExecutionYear(Integer degreeCurricularPlanID,
            String executionYear) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanID);

        return (ExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", DegreeType.MASTER_DEGREE);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByDegreeAndExecutionYear(Integer degreeOID, String year, CurricularStage curricularStage) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", year);
        criteria.addEqualTo("degreeCurricularPlan.curricularStage", curricularStage);
        criteria.addEqualTo("degreeCurricularPlan.degree.idInternal", degreeOID);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByTeacher(Integer teacherOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("coordinatorsList.teacher.idInternal", teacherOID);
        QueryByCriteria queryByCriteria = new QueryByCriteria(ExecutionDegree.class, criteria, false);
        queryByCriteria.addOrderBy("degreeCurricularPlan.degree.nome", true);
        queryByCriteria.addOrderBy("executionYear.year", false);
        return queryList(queryByCriteria);
    }

    public List readByExecutionYearAndDegreeType(String year, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", year);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
        return queryList(ExecutionDegree.class, criteria, "keyDegreeCurricularPlan", true);
    }

    public List readByDegreeCurricularPlan(Integer degreeCurricularPlanOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanOID);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readExecutionsDegreesByDegree(Integer degreeOID, CurricularStage curricularStage) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.curricularStage", curricularStage);
        criteria.addEqualTo("degreeCurricularPlan.degree.idInternal", degreeOID);

        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByExecutionCourseAndByTeacher(Integer executionCourseOID, Integer teacherOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "degreeCurricularPlan.curricularCourses.associatedExecutionCourses.idInternal",
                executionCourseOID);
        criteria.addEqualTo("coordinatorsList.teacher.idInternal", teacherOID);
        return queryList(ExecutionDegree.class, criteria, true);
    }

    public ExecutionDegree readByDegreeCurricularPlanNameAndExecutionYear(String name,
            Integer executionYearOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyExecutionYear", executionYearOID);
        criteria.addLike("degreeCurricularPlan.name", name);

        return (ExecutionDegree) queryObject(ExecutionDegree.class, criteria);

    }

    public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", DegreeType.DEGREE);
        criteria.addEqualTo("executionYear.state", new PeriodState(PeriodState.CURRENT));
        return queryList(ExecutionDegree.class, criteria);
    }

    public ExecutionDegree readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
            Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanID);
        criteria.addEqualTo("executionYear.idInternal", executionYearID);
        return (ExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    public List readByExecutionYearOID(Integer executionYearOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.idInternal", executionYearOID);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readListByDegreeNameAndExecutionYearAndDegreeType(String name, Integer executionYearOID,
            DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionYear", executionYearOID);
        criteria.addLike("degreeCurricularPlan.degree.nome", name);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
        return queryList(ExecutionDegree.class, criteria);
    }
}