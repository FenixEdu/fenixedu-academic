/**
 * DisciplinaExecucaoOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.ISite;
import Dominio.ITurno;
import Dominio.Professorship;
import Dominio.Site;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class DisciplinaExecucaoOJB extends ObjectFenixOJB implements IDisciplinaExecucaoPersistente
{

    public DisciplinaExecucaoOJB()
    {
    }

    public boolean apagarTodasAsDisciplinasExecucao()
    {
        try
        {
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            super.deleteAll(oqlQuery);
            return true;
        } catch (ExcepcaoPersistencia ex)
        {
            return false;
        }
    }

    public void escreverDisciplinaExecucao(IDisciplinaExecucao executionCourseToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IDisciplinaExecucao executionCourseFromDB = null;

        // If there is nothing to write, simply return.
        if (executionCourseToWrite == null)
            return;

        // Read execution course from database.
        executionCourseFromDB =
            this.readByExecutionCourseInitialsAndExecutionPeriod(
                executionCourseToWrite.getSigla(),
                executionCourseToWrite.getExecutionPeriod());

        // If execution course is not in database, then write it.
        if (executionCourseFromDB == null)
            super.lockWrite(executionCourseToWrite);
        // else If the execution course is mapped to the database, then write any existing changes.
        else if (
            (executionCourseToWrite instanceof DisciplinaExecucao)
                && ((DisciplinaExecucao) executionCourseFromDB).getIdInternal().equals(
                    ((DisciplinaExecucao) executionCourseToWrite).getIdInternal()))
        {
            super.lockWrite(executionCourseToWrite);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

    // TODO : Write test for this method
    public List readAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IDisciplinaExecucao readBySiglaAndAnoLectivoAndSiglaLicenciatura(
        String sigla,
        String anoLectivo,
        String siglaLicenciatura)
        throws ExcepcaoPersistencia
    {
        try
        {
            IDisciplinaExecucao disciplinaExecucao = null;
            String oqlQuery = "select disciplinaExecucao from " + DisciplinaExecucao.class.getName();
            oqlQuery += " where sigla = $1";
            oqlQuery += " and executionPeriod.executionYear.year = $2";
            oqlQuery += " and associatedCurricularCourses.degreeCurricularPlan.degree.sigla = $3";
            query.create(oqlQuery);
            query.bind(sigla);
            query.bind(anoLectivo);
            query.bind(siglaLicenciatura);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                disciplinaExecucao = (IDisciplinaExecucao) result.get(0);
            return disciplinaExecucao;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    /**
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(java.lang.Integer, Dominio.IExecutionPeriod, java.lang.String)
     */
    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(
        Integer curricularYear,
        IExecutionPeriod executionPeriod,
        ICursoExecucao executionDegree)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo(
            "associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
            curricularYear);
        criteria.addEqualTo(
            "associatedCurricularCourses.scopes.curricularSemester.semester",
            executionPeriod.getSemester());
        criteria.addEqualTo(
            "associatedCurricularCourses.degreeCurricularPlan.name",
            executionDegree.getCurricularPlan().getName());
        criteria.addEqualTo(
            "associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
            executionDegree.getCurricularPlan().getDegree().getSigla());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

        List executionCourseList = queryList(DisciplinaExecucao.class, criteria, true);
        return executionCourseList;
    }

    /**
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionCourseInitials(java.lang.String)
     */
    public IDisciplinaExecucao readByExecutionCourseInitialsAndExecutionPeriod(
        String courseInitials,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        criteria.addEqualTo("sigla", courseInitials);
        return (IDisciplinaExecucao) queryObject(DisciplinaExecucao.class, criteria);
    }

    public void deleteExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            oqlQuery += " where executionPeriod.name = $1 "
                + " and executionPeriod.executionYear.year = $2 "
                + " and sigla = $3 ";
            query.create(oqlQuery);

            query.bind(executionCourse.getExecutionPeriod().getName());
            query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
            query.bind(executionCourse.getSigla());

            List result = (List) query.execute();
            lockRead(result);

            if (!result.isEmpty())
            {
                IDisciplinaExecucao executionCourseTemp = (IDisciplinaExecucao) result.get(0);
                // Delete All Attends

                List attendsTemp =
                    SuportePersistenteOJB
                        .getInstance()
                        .getIFrequentaPersistente()
                        .readByExecutionCourse(
                        executionCourseTemp);
                Iterator iterator = attendsTemp.iterator();
                while (iterator.hasNext())
                {
                    SuportePersistenteOJB.getInstance().getIFrequentaPersistente().delete(
                        (IFrequenta) iterator.next());
                }

                // Delete All Shifts
                List shiftsTemp =
                    SuportePersistenteOJB.getInstance().getITurnoPersistente().readByExecutionCourse(
                        executionCourseTemp);
                iterator = shiftsTemp.iterator();
                while (iterator.hasNext())
                {
                    SuportePersistenteOJB.getInstance().getITurnoPersistente().delete(
                        (ITurno) iterator.next());
                }
                super.delete(executionCourseTemp);
            }

        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(DisciplinaExecucao.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriod(Dominio.IExecutionPeriod, Util.TipoCurso)
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod, TipoCurso curso)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.tipoCurso", curso);
        return queryList(DisciplinaExecucao.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(Dominio.IExecutionPeriod, Dominio.ICursoExecucao, Dominio.ICurricularYear, java.lang.String)
     */
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
        IExecutionPeriod executionPeriod,
        ICursoExecucao executionDegree,
        ICurricularYear curricularYear,
        String executionCourseName)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        if (executionCourseName != null && !executionCourseName.equals(""))
        {
            criteria.addLike("nome", executionCourseName);
        }
        if (curricularYear != null)
        {
            criteria.addEqualTo(
                "associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                curricularYear.getIdInternal());
        }

        if (executionDegree != null)
        {
            criteria.addEqualTo(
                "associatedCurricularCourses.scopes.curricularCourse.degreeCurricularPlan.idInternal",
                executionDegree.getCurricularPlan().getIdInternal());
        }
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());

        List temp = queryList(DisciplinaExecucao.class, criteria, true);

        return temp;
        //		return queryList(DisciplinaExecucao.class, criteria, true);
    }

    //	returns a list of teachers in charge ids
    public List readExecutionCourseTeachers(Integer executionCourseId) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyExecutionCourse", executionCourseId);

        return queryList(Professorship.class, criteria);

    }

    public void lockWrite(IDisciplinaExecucao executionCourseToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IDisciplinaExecucao executionCourseFromDB = null;

        // If there is nothing to write, simply return.
        if (executionCourseToWrite == null)
        {
            return;
        }

        // Read ExecutionCourse from database.
        executionCourseFromDB =
            this.readByExecutionCourseInitialsAndExecutionPeriod(
                executionCourseToWrite.getSigla(),
                executionCourseToWrite.getExecutionPeriod());

        // If ExecutionCourse is not in database, then write it.
        if (executionCourseFromDB == null)
        {
            super.lockWrite(executionCourseToWrite);
            // else If the ExecutionCourse is mapped to the database, then write any existing changes.
        } else if (
            (executionCourseToWrite instanceof DisciplinaExecucao)
                && ((DisciplinaExecucao) executionCourseFromDB).getIdInternal().equals(
                    ((DisciplinaExecucao) executionCourseToWrite).getIdInternal()))
        {
            super.lockWrite(executionCourseToWrite);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

    public Boolean readSite(Integer executionCourseId) throws ExcepcaoPersistencia
    {
        Boolean result = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseId);

        ISite site = (ISite) queryObject(Site.class, criteria);
        if (site == null)
            result = new Boolean(false);
        else
            result = new Boolean(true);
        return result;
    }

    public IDisciplinaExecucao readbyCurricularCourseAndExecutionPeriod(
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());

        return (IDisciplinaExecucao) queryObject(DisciplinaExecucao.class, criteria);

    }

}
