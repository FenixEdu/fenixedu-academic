/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author João Mota
 * 
 *  
 */
public class ResponsibleForOJB extends ObjectFenixOJB implements IPersistentResponsibleFor
{

    /**
	 *  
	 */
    public ResponsibleForOJB()
    {
        super();
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + ResponsibleFor.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IResponsibleFor readByTeacherAndExecutionCourse(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        try
        {
            IResponsibleFor responsibleFor = null;
            String oqlQuery = "select responsibleFor from " + ResponsibleFor.class.getName()
                    + " where teacher.teacherNumber = $1" + " and executionCourse.sigla = $2"
                    + " and executionCourse.executionPeriod.name = $3"
                    + " and executionCourse.executionPeriod.executionYear.year = $4";

            query.create(oqlQuery);
            query.bind(teacher.getTeacherNumber());
            query.bind(executionCourse.getSigla());
            query.bind(executionCourse.getExecutionPeriod().getName());
            query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                responsibleFor = (IResponsibleFor) result.get(0);
            return responsibleFor;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        try
        {

            String oqlQuery = "select responsibleFor from " + ResponsibleFor.class.getName()
                    + " where teacher.teacherNumber = $1";

            query.create(oqlQuery);
            query.bind(teacher.getTeacherNumber());

            List result = (List) query.execute();
            lockRead(result);

            return result;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourse.getIdInternal());
        return queryList(ResponsibleFor.class, crit);
    }

    public void delete(IResponsibleFor responsibleFor) throws ExcepcaoPersistencia
    {
        super.delete(responsibleFor);
    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        String oqlQuery = "select all from " + ResponsibleFor.class.getName();
        super.deleteAll(oqlQuery);
    }

    public void lockWrite(IResponsibleFor responsibleFor)
            throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IResponsibleFor responsibleForFromDB = null;

        // If there is nothing to write, simply return.
        if (responsibleFor == null)
            return;

        // Read responsibleFor from database.
        responsibleForFromDB = this.readByTeacherAndExecutionCourse(responsibleFor.getTeacher(),
                responsibleFor.getExecutionCourse());

        // If responsibleFor is not in database, then write it.
        if (responsibleForFromDB == null)
            super.lockWrite(responsibleFor);

        // else If the responsibleFor is mapped to the database, then write any existing changes.
        else if ((responsibleFor instanceof ResponsibleFor)
                && ((Professorship) responsibleForFromDB).getIdInternal()
                        .equals(((Professorship) responsibleFor).getIdInternal()))
        {
            super.lockWrite(responsibleFor);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionCoursePB(Dominio.ITeacher,
	 *          Dominio.IDisciplinaExecucao)
	 */
    public IResponsibleFor readByTeacherAndExecutionCoursePB(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());

        return (IResponsibleFor) queryObject(ResponsibleFor.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionPeriod(Dominio.ITeacher)
	 */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(ResponsibleFor.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionYear(Dominio.ITeacher,
	 *          Dominio.IExecutionYear)
	 */
    public List readByTeacherAndExecutionYear(ITeacher teacher, IExecutionYear executionYear)
            throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionYear
                .getIdInternal());
        return queryList(ResponsibleFor.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionCourseIds(Dominio.ITeacher, java.util.List)
     */
    public List readByTeacherAndExecutionCourseIds(ITeacher teacher, List executionCourseIds) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addIn("executionCourse.idInternal", executionCourseIds);
        return queryList(ResponsibleFor.class, criteria);
    }

}