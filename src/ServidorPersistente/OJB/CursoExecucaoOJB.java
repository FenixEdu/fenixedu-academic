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

import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class CursoExecucaoOJB extends ObjectFenixOJB implements ICursoExecucaoPersistente
{

    public void lockWrite(ICursoExecucao cursoExecucaoToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ICursoExecucao cursoExecucaoFromDB = null;

        // If there is nothing to write, simply return.
        if (cursoExecucaoToWrite == null)
            return;

        // Read cursoExecucao from database.
        cursoExecucaoFromDB =
            this.readByDegreeCurricularPlanAndExecutionYear(
                cursoExecucaoToWrite.getCurricularPlan(),
                cursoExecucaoToWrite.getExecutionYear());

        // If cursoExecucao is not in database, then write it.
        if (cursoExecucaoFromDB == null)
            super.lockWrite(cursoExecucaoToWrite);
        // else If the cursoExecucao is mapped to the database, then write any
        // existing changes.
        else if (
            (cursoExecucaoToWrite instanceof CursoExecucao)
                && ((CursoExecucao) cursoExecucaoFromDB).getIdInternal().equals(
                    ((CursoExecucao) cursoExecucaoToWrite).getIdInternal()))
        {
            super.lockWrite(cursoExecucaoToWrite);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

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
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readBySigla(String)
	 * @deprecated
	 */
    public ICursoExecucao readBySigla(String sigla) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.degree.sigla", sigla);
        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);
    }

    /**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYear(Dominio.IExecutionYear)
	 */
    public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia
    {
        //PersistenceBroker broker = ((HasBroker)
        // odmg.currentTransaction()).getBroker();

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addOrderBy("KEY_DEGREE_CURRICULAR_PLAN", true);

        return queryList(CursoExecucao.class, criteria);
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
        criteria.addEqualTo("executionYear.year ", executionYear.getYear());
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

    public ICursoExecucao readByDegreeCodeAndExecutionYear(
        String degreeCode,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year ", executionYear.getYear());
        criteria.addEqualTo("curricularPlan.degree.sigla", degreeCode);
        return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);
    }

    public List readByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year ", executionYear.getYear());
        criteria.addEqualTo("curricularPlan.degree.idInternal", degree.getIdInternal());
        return queryList(CursoExecucao.class, criteria);
    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("coordinatorsList.teacher.teacherNumber", teacher.getTeacherNumber());
        criteria.addOrderBy("curricularPlan.degree.nome", true);
        criteria.addOrderBy("executionYear.year", false);
        return queryList(CursoExecucao.class, criteria);
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
        criteria.addOrderBy("keyCurricularPlan", true);
        return queryList(CursoExecucao.class, criteria);
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
}
