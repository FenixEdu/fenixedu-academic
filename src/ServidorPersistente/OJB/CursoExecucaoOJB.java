/*
 * CursoExecucaoOJB.java
 *
 * Created on 2 de Novembro de 2002, 21:17
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  rpfi
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
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
        // else If the cursoExecucao is mapped to the database, then write any existing changes.
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
        try
        {
            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            Iterator iterator = result.iterator();
            while (iterator.hasNext())
            {
                delete((ICursoExecucao) iterator.next());
            }
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    /**
     * 
     * @see ServidorPersistente.ICursoExecucaoPersistente#readBySigla(String)
     * @deprecated 
     */
    public ICursoExecucao readBySigla(String sigla) throws ExcepcaoPersistencia
    {
        try
        {
            ICursoExecucao cursoExecucao = null;
            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            oqlQuery += " where curso.sigla = $1";
            query.create(oqlQuery);
            query.bind(sigla);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                cursoExecucao = (ICursoExecucao) result.get(0);
            return cursoExecucao;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    /**
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYear(Dominio.IExecutionYear)
     */
    public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia
    {
        //PersistenceBroker broker = ((HasBroker) odmg.currentTransaction()).getBroker();

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addOrderBy("KEY_DEGREE_CURRICULAR_PLAN", true);

        return queryList(CursoExecucao.class, criteria);
    }

    /**
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByDegreeAndExecutionYear(Dominio.ICurso, Dominio.IExecutionYear)
     */
    public ICursoExecucao readByDegreeCurricularPlanAndExecutionYear(
        IDegreeCurricularPlan degreeCurricularPlan,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            oqlQuery += " where executionYear.year = $1"
                + " and curricularPlan.name = $2 "
                + " and curricularPlan.degree.sigla = $3";
            query.create(oqlQuery);

            query.bind(executionYear.getYear());
            query.bind(degreeCurricularPlan.getName());
            query.bind(degreeCurricularPlan.getDegree().getSigla());

            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
            {
                return (ICursoExecucao) result.get(0);
            } else
            {
                return null;
            }

        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }
    }

    /**
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByDegreeAndExecutionYear(Dominio.ICurso, Dominio.IExecutionYear)
     */
    public ICursoExecucao readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
        String degreeInitials,
        String nameDegreeCurricularPlan,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            oqlQuery += " where executionYear.year = $1"
                + " and curricularPlan.name = $2 "
                + " and curricularPlan.degree.sigla = $3";
            query.create(oqlQuery);

            query.bind(executionYear.getYear());
            query.bind(nameDegreeCurricularPlan);
            query.bind(degreeInitials);

            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                return (ICursoExecucao) result.get(0);
            else
                return null;
        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }
    }

    public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia
    {
        try
        {

            //			Criteria criteria = new Criteria();
            //			
            //			criteria.addEqualTo("executionYear.year",executionYear);
            //			criteria.addEqualTo("curricularPlan.degree.tipoCurso",new Integer(TipoCurso.MESTRADO));
            //			System.out.println("Execution ...");			
            //			List result = queryList(CursoExecucao.class, criteria);
            //			System.out.println("Done !");			
            //			return result;

            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            oqlQuery += " where executionYear.year = $1" + " and curricularPlan.degree.tipoCurso = $2";
            query.create(oqlQuery);

            query.bind(executionYear);
            query.bind(new Integer(TipoCurso.MESTRADO));

            List result = (List) query.execute();
            lockRead(result);

            return result;
        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }

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
        //		try {
        //			String oqlQuery = "select all from " + CursoExecucao.class.getName() + " where executionYear.year = $1" + " and curricularPlan.degree.sigla = $2";
        //			query.create(oqlQuery);
        //
        //			query.bind(executionYear.getYear());
        //			query.bind(degreeCode);
        //
        //			List result = (List) query.execute();
        //			lockRead(result);
        //			if (result.size() != 0)
        //				return (ICursoExecucao) result.get(0);
        //			return null;
        //		} catch (QueryException e) {
        //			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        //		}
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
        try
        {

            String oqlQuery =
                "select all from "
                    + CursoExecucao.class.getName()
                    + " where coordinator.teacherNumber = $1"
                    + " order by curricularPlan.degree.nome, executionYear.year desc";

            query.create(oqlQuery);

            query.bind(teacher.getTeacherNumber());

            List result = (List) query.execute();
            lockRead(result);
            if (result.size() == 0)
                return null;
            return result;
        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }

    }

    /* (non-Javadoc)
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYearAndDegreeType(Dominio.IExecutionYear, Util.TipoCurso)
     */
    public List readByExecutionYearAndDegreeType(IExecutionYear executionYear, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.year", executionYear.getYear());
        criteria.addEqualTo("curricularPlan.degree.tipoCurso", degreeType);
        criteria.addOrderBy("KEY_DEGREE_CURRICULAR_PLAN", true);
        return queryList(CursoExecucao.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYearAndDegreeCurricularPlans(Dominio.IDisciplinaExecucao, java.util.Collection)
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
        try
        {
            String oqlQuery =
                "select all from "
                    + CursoExecucao.class.getName()
                    + " where curricularPlan.idInternal = $1";

            query.create(oqlQuery);

            query.bind(degreeCurricularPlan.getIdInternal());

            List result = (List) query.execute();
            lockRead(result);
            //			if (result.size() == 0)
            //				return null;
            return result;
        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }

    }

    public ICursoExecucao readbyDegreeCurricularPlanID(Integer degreeCurricularPlanID)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery =
                "select all from "
                    + CursoExecucao.class.getName()
                    + " where curricularPlan.idInternal = $1";

            query.create(oqlQuery);

            query.bind(degreeCurricularPlanID);

            List result = (List) query.execute();
            lockRead(result);
            if (result.size() == 0)
                return null;
            return (ICursoExecucao) result.get(0);
        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }
    }

    public ICursoExecucao readByDegreeCodeAndDegreeCurricularPlanName(String code, String name)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery =
                "select all from "
                    + CursoExecucao.class.getName()
                    + " where curricularPlan.degree.sigla = $1"
                    + " and curricularPlan.name = $2";
            query.create(oqlQuery);

            query.bind(code);
            query.bind(name);

            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                return (ICursoExecucao) result.get(0);
            return null;
        } catch (QueryException e)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }
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
}
