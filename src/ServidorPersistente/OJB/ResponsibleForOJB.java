/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResponsibleFor;

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
        Criteria criteria = new Criteria();
        return queryList(ResponsibleFor.class, criteria);
    }

    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
            executionDegree.getCurricularPlan().getIdInternal());
        criteria.addEqualTo(
            "executionCourse.executionPeriod.executionYear.idInternal",
            executionDegree.getExecutionYear().getIdInternal());
        return queryList(ResponsibleFor.class, criteria);
    }

    public IResponsibleFor readByTeacherAndExecutionCourse(
        ITeacher teacher,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
        criteria.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        criteria.addEqualTo(
            "executionCourse.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        criteria.addEqualTo(
            "executionCourse.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());
        return (IResponsibleFor) queryObject(ResponsibleFor.class, criteria);
    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
        return queryList(ResponsibleFor.class, criteria);
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
        Criteria criteria = new Criteria();
        List result = queryList(ResponsibleFor.class, criteria);
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
        {
            delete((IResponsibleFor) iterator.next());
        }
    }

    

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionCoursePB(Dominio.ITeacher,
	 *      Dominio.IDisciplinaExecucao)
	 */
    public IResponsibleFor readByTeacherAndExecutionCoursePB(
        ITeacher teacher,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
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
        criteria.addEqualTo(
            "executionCourse.executionPeriod.idInternal",
            executionPeriod.getIdInternal());
        return queryList(ResponsibleFor.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionYear(Dominio.ITeacher,
	 *      Dominio.IExecutionYear)
	 */
    public List readByTeacherAndExecutionYear(ITeacher teacher, IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo(
            "executionCourse.executionPeriod.executionYear.idInternal",
            executionYear.getIdInternal());
        return queryList(ResponsibleFor.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentResponsibleFor#readByTeacherAndExecutionCourseIds(Dominio.ITeacher,
	 *      java.util.List)
	 */
    public List readByTeacherAndExecutionCourseIds(ITeacher teacher, List executionCourseIds)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addIn("executionCourse.idInternal", executionCourseIds);
        return queryList(ResponsibleFor.class, criteria);
    }

}