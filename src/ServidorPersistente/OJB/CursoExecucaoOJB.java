/*
 * CursoExecucaoOJB.java
 * 
 * Created on 2 de Novembro de 2002, 21:17
 */

package ServidorPersistente.OJB;

/**
 * @author rpfi
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import Util.PeriodState;
import Util.TipoCurso;

public class CursoExecucaoOJB extends ObjectFenixOJB implements ICursoExecucaoPersistente
{

   
    public void delete(ICursoExecucao executionDegree) throws ExcepcaoPersistencia
    {

        super.delete(executionDegree);

    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        List result = queryList(CursoExecucao.class, criteria);
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
        {
            delete((ICursoExecucao) iterator.next());
        }
    }

   
    /**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYear(String)
	 */
    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        return queryList(CursoExecucao.class, criteria, "KEY_DEGREE_CURRICULAR_PLAN", true);
    }

    /**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByDegreeAndExecutionYear(Dominio.ICurso,
	 *      Dominio.IExecutionYear)
	 */
    public ICursoExecucao readByDegreeCurricularPlanAndExecutionYear(
        IDegreeCurricularPlan degreeCurricularPlan,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        return readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
            degreeCurricularPlan.getDegree().getSigla(),
            degreeCurricularPlan.getName(),
            executionYear);
    }

    /**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByDegreeAndExecutionYear(Dominio.ICurso,
	 *      Dominio.IExecutionYear)
	 */
    public ICursoExecucao readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
        String degreeInitials,
        String nameDegreeCurricularPlan,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("curricularPlan.name", nameDegreeCurricularPlan);
        criteria.addEqualTo("curricularPlan.degree.sigla", degreeInitials);
        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);
    }

    public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear);
        criteria.addEqualTo("curricularPlan.degree.tipoCurso", TipoCurso.MESTRADO_OBJ);
        return queryList(CursoExecucao.class, criteria);
    }

    

	public List readByDegreeAndExecutionYearList(String degreeCode, IExecutionYear executionYear)
		   throws ExcepcaoPersistencia
	   {
		   Criteria criteria = new Criteria();
		   criteria.addEqualTo("executionYear.year", executionYear.getYear());
		   criteria.addEqualTo("curricularPlan.degree.sigla", degreeCode);
		   return queryList(CursoExecucao.class, criteria);
	   }

    public List readByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("curricularPlan.degree.idInternal", degree.getIdInternal());
        return queryList(CursoExecucao.class, criteria);
    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("coordinatorsList.teacher.teacherNumber", teacher.getTeacherNumber());
        QueryByCriteria queryByCriteria = new QueryByCriteria(CursoExecucao.class, criteria, false);
        queryByCriteria.addOrderBy("curricularPlan.degree.nome", true);
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
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("curricularPlan.degree.tipoCurso", degreeType);
        return queryList(CursoExecucao.class, criteria, "keyCurricularPlan", true);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYearAndDegreeCurricularPlans(Dominio.IDisciplinaExecucao,
	 *      java.util.Collection)
	 */
    public List readByExecutionYearAndDegreeCurricularPlans(
        IExecutionYear executionYear,
        Collection degreeCurricularPlans)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("academicYear", executionYear.getIdInternal());

        Collection degreeCurricularPlansIds =
            CollectionUtils.collect(degreeCurricularPlans, new Transformer()
        {

            public Object transform(Object input)
            {
                IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) input;
                return degreeCurricularPlan.getIdInternal();
            }
        });

        criteria.addIn("keyCurricularPlan", degreeCurricularPlansIds);
        return queryList(CursoExecucao.class, criteria);
    }

    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        return queryList(CursoExecucao.class, criteria);
    }

    /**
	 * FIXME: This method doesn't make sense... should return a list.
	 */
    public ICursoExecucao readbyDegreeCurricularPlanID(Integer degreeCurricularPlanID)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularPlan.idInternal", degreeCurricularPlanID);
        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);
    }

    /**
	 * FIXME: This method doesn't make sense... should return a list
	 */
    public ICursoExecucao readByDegreeCodeAndDegreeCurricularPlanName(String code, String name)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularPlan.degree.sigla", code);
        criteria.addEqualTo("curricularPlan.name", name);

        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);
    }

    public ICursoExecucao readByDegreeNameAndExecutionYear(String name, IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("academicYear", executionYear.getIdInternal());
        criteria.addLike("curricularPlan.degree.nome", name);

        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);

    }

    public ICursoExecucao readByDegreeNameAndExecutionYearAndDegreeType(
        String name,
        IExecutionYear executionYear,
        TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("academicYear", executionYear.getIdInternal());
        criteria.addLike("curricularPlan.degree.nome", name);
        criteria.addEqualTo("curricularPlan.degree.tipoCurso", degreeType);
        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);

    }

    public List readExecutionsDegreesByDegree(ICurso degree) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("curricularPlan.degree.idInternal", degree.getIdInternal());

        return queryList(CursoExecucao.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionCourse(Dominio.IExecutionCourse)
	 */
    public List readByExecutionCourseAndByTeacher(IExecutionCourse executionCourse, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "curricularPlan.curricularCourses.associatedExecutionCourses.idInternal",
            executionCourse.getIdInternal());
        criteria.addEqualTo("coordinatorsList.teacher.idInternal", teacher.getIdInternal());
        return queryList(CursoExecucao.class, criteria, true);
    }
    
	public ICursoExecucao readByDegreeCurricularPlanNameAndExecutionYear(String name, IExecutionYear executionYear)
    throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();

    	criteria.addEqualTo("academicYear", executionYear.getIdInternal());
    	criteria.addLike("curricularPlan.name", name);

    	return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);

    }
	public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularPlan.degree.tipoCurso", new TipoCurso(TipoCurso.LICENCIATURA));
		criteria.addEqualTo("executionYear.state", new PeriodState(PeriodState.CURRENT));
		return queryList(CursoExecucao.class, criteria);
	}
    public List readByExecutionYearOIDAndDegreeType(Integer executionYearOID, TipoCurso degreeType)
    	throws ExcepcaoPersistencia
	{
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("executionYear.idInternal", executionYearOID);
    	criteria.addEqualTo("curricularPlan.degree.tipoCurso", degreeType);
    	return queryList(CursoExecucao.class, criteria, "keyCurricularPlan", true);
	}

}
