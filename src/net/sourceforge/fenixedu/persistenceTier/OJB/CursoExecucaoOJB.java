/*
 * CursoExecucaoOJB.java
 * 
 * Created on 2 de Novembro de 2002, 21:17
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author rpfi
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

public class CursoExecucaoOJB extends PersistentObjectOJB implements IPersistentExecutionDegree {

    public void delete(IExecutionDegree executionDegree) throws ExcepcaoPersistencia {

        super.delete(executionDegree);

    }
    
    public List readAll() throws ExcepcaoPersistencia{
        return queryList(ExecutionDegree.class, null);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List result = queryList(ExecutionDegree.class, criteria);
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            delete((IExecutionDegree) iterator.next());
        }
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionDegree#readByExecutionYear(String)
     */
    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        return queryList(ExecutionDegree.class, criteria, "KEY_DEGREE_CURRICULAR_PLAN", true);
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionDegree#readByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public IExecutionDegree readByDegreeCurricularPlanAndExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        return readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan
                .getDegree().getSigla(), degreeCurricularPlan.getName(), executionYear);
    }

    /**
     * 
     * @param degreeCurricularPlanID
     * @param executionYearID
     * @return
     * @throws ExcepcaoPersistencia
     */
    public IExecutionDegree readByDegreeCurricularPlanIDAndExecutionYear(
            Integer degreeCurricularPlanID, String executionYear)
            throws ExcepcaoPersistencia {
        
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanID);
        
        return (IExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionDegree#readByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public IExecutionDegree readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
            String degreeInitials, String nameDegreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("degreeCurricularPlan.name", nameDegreeCurricularPlan);
        criteria.addEqualTo("degreeCurricularPlan.degree.sigla", degreeInitials);
        return (IExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", TipoCurso.MESTRADO_OBJ);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByDegreeAndExecutionYearList(String degreeCode, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("degreeCurricularPlan.degree.sigla", degreeCode);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("degreeCurricularPlan.degree.idInternal", degree.getIdInternal());
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("coordinatorsList.teacher.teacherNumber", teacher.getTeacherNumber());
        QueryByCriteria queryByCriteria = new QueryByCriteria(ExecutionDegree.class, criteria, false);
        queryByCriteria.addOrderBy("degreeCurricularPlan.degree.nome", true);
        queryByCriteria.addOrderBy("executionYear.year", false);
        return queryList(queryByCriteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYearAndDegreeType(Dominio.IExecutionYear,
     *      Util.TipoCurso)
     */
    public List readByExecutionYearAndDegreeType(IExecutionYear executionYear, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
        return queryList(ExecutionDegree.class, criteria, "keyCurricularPlan", true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYearAndDegreeCurricularPlans(Dominio.IDisciplinaExecucao,
     *      java.util.Collection)
     */
    public List readByExecutionYearAndDegreeCurricularPlans(IExecutionYear executionYear,
            Collection degreeCurricularPlans) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("academicYear", executionYear.getIdInternal());

        Collection degreeCurricularPlansIds = CollectionUtils.collect(degreeCurricularPlans,
                new Transformer() {

                    public Object transform(Object input) {
                        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) input;
                        return degreeCurricularPlan.getIdInternal();
                    }
                });

        criteria.addIn("keyCurricularPlan", degreeCurricularPlansIds);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        return queryList(ExecutionDegree.class, criteria);
    }

    /**
     * FIXME: This method doesn't make sense... should return a list.
     */
    public IExecutionDegree readbyDegreeCurricularPlanID(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanID);
        return (IExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    /**
     * FIXME: This method doesn't make sense... should return a list
     */
    public IExecutionDegree readByDegreeCodeAndDegreeCurricularPlanName(String code, String name)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.degree.sigla", code);
        criteria.addEqualTo("degreeCurricularPlan.name", name);

        return (IExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    public List readExecutionsDegreesByDegree(IDegree degree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("degreeCurricularPlan.degree.idInternal", degree.getIdInternal());

        return queryList(ExecutionDegree.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionCourse(Dominio.IExecutionCourse)
     */
    public List readByExecutionCourseAndByTeacher(IExecutionCourse executionCourse, ITeacher teacher)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.curricularCourses.associatedExecutionCourses.idInternal",
                executionCourse.getIdInternal());
        criteria.addEqualTo("coordinatorsList.teacher.idInternal", teacher.getIdInternal());
        return queryList(ExecutionDegree.class, criteria, true);
    }

    public IExecutionDegree readByDegreeCurricularPlanNameAndExecutionYear(String name,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("academicYear", executionYear.getIdInternal());
        criteria.addLike("degreeCurricularPlan.name", name);

        return (IExecutionDegree) queryObject(ExecutionDegree.class, criteria);

    }

    public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", new TipoCurso(TipoCurso.LICENCIATURA));
        criteria.addEqualTo("executionYear.state", new PeriodState(PeriodState.CURRENT));
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByExecutionYearOIDAndDegreeType(Integer executionYearOID, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.idInternal", executionYearOID);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
        return queryList(ExecutionDegree.class, criteria, "keyCurricularPlan", true);
    }

    public List readExecutionDegreesbyDegreeCurricularPlanID(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanID);
        return queryList(ExecutionDegree.class, criteria);
    }

    public IExecutionDegree readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
            Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanID);
        criteria.addEqualTo("executionYear.idInternal", executionYearID);
        return (IExecutionDegree) queryObject(ExecutionDegree.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionDegree#readListByDegreeNameAndExecutionYear(java.lang.String,
     *      Dominio.IExecutionYear)
     */
    public List readListByDegreeNameAndExecutionYear(String name, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("academicYear", executionYear.getIdInternal());
        criteria.addLike("degreeCurricularPlan.degree.nome", name);
        return queryList(ExecutionDegree.class, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionDegree#readListByDegreeNameAndExecutionYearAndDegreeType(java.lang.String,
     *      Dominio.IExecutionYear, Util.TipoCurso)
     */
    public List readListByDegreeNameAndExecutionYearAndDegreeType(String name,
            IExecutionYear executionYear, TipoCurso degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("academicYear", executionYear.getIdInternal());
        criteria.addLike("degreeCurricularPlan.degree.nome", name);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
        return queryList(ExecutionDegree.class, criteria);
    }

    public List readByExecutionYearOID(Integer executionYearOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.idInternal", executionYearOID);
        return queryList(ExecutionDegree.class, criteria);
    }
}