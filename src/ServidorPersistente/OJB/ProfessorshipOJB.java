/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

/**
 * @author João Mota
 * 
 *  
 */
public class ProfessorshipOJB extends ObjectFenixOJB implements IPersistentProfessorship
{

    /**
	 *  
	 */
    public ProfessorshipOJB()
    {
        super();
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(Professorship.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentProfessorship#readByTeacherIDAndExecutionCourseID(Dominio.ITeacher,
	 *      Dominio.IDisciplinaExecucao)
	 */
    public IProfessorship readByTeacherIDAndExecutionCourseID(
        ITeacher teacher,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        return (IProfessorship) queryObject(Professorship.class, criteria);
    }

    public IProfessorship readByTeacherAndExecutionCourse(
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
        return (IProfessorship) queryObject(Professorship.class, criteria);
    }

    public IProfessorship readByTeacherAndExecutionCoursePB(
        ITeacher teacher,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        return (IProfessorship) queryObject(Professorship.class, criteria);

    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
        return queryList(Professorship.class, criteria);
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourse.getIdInternal());
        return queryList(Professorship.class, crit);
    }

    public void delete(IProfessorship professorship) throws ExcepcaoPersistencia
    {
        super.delete(professorship);
    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        List result = queryList(Professorship.class, criteria);
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
        {
            delete((IProfessorship) iterator.next());
        }
    }

    public void lockWrite(IProfessorship professorship)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IProfessorship professorshipFromDB = null;

        // If there is nothing to write, simply return.
        if (professorship == null)
            return;

        // Read professorship from database.
        professorshipFromDB =
            this.readByTeacherAndExecutionCourse(
                professorship.getTeacher(),
                professorship.getExecutionCourse());

        // If professorship is not in database, then write it.
        if (professorshipFromDB == null)
            super.lockWrite(professorship);

        // else If the professorship is mapped to the database, then write any existing changes.
        else if (
            (professorship instanceof Professorship)
                && ((Professorship) professorshipFromDB).getIdInternal().equals(
                    ((Professorship) professorship).getIdInternal()))
        {
            super.lockWrite(professorship);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentProfessorship#readByTeacherAndTypeOfDegree(Dominio.ITeacher,
	 *      Util.TipoCurso)
	 */
    public List readByTeacherAndTypeOfDegree(ITeacher teacher, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo(
            "executionCourse.associatedCurricularCourses.degreeCurricularPlan.degree.tipoCurso",
            degreeType);
        return queryList(Professorship.class, criteria, true);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentProfessorship#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
	 *      Dominio.IExecutionPeriod)
	 */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo(
            "executionCourse.executionPeriod.idInternal",
            executionPeriod.getIdInternal());
        return queryList(Professorship.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentTeacher#readByExecutionDegree(Dominio.ICursoExecucao)
	 */
    public List readByExecutionDegrees(List executionDegrees) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        List executionDegreesIds = (List) CollectionUtils.collect(executionDegrees, new Transformer()
        {
            public Object transform(Object o)
            {
                return ((ICursoExecucao) o).getCurricularPlan().getIdInternal();
            }
        });
        criteria.addIn(
            "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
            executionDegreesIds);
        return queryList(Professorship.class, criteria, true);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentProfessorship#readByExecutionDegree(Dominio.ICursoExecucao)
	 */
    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
            executionDegree.getCurricularPlan().getIdInternal());
        criteria.addEqualTo(
            "executionCourse.executionPeriod.executionYear.idInternal",
            executionDegree.getExecutionYear().getIdInternal());
        return queryList(Professorship.class, criteria, true);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentProfessorship#readByTeacherAndExecutionYear(Dominio.ITeacher,
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
        return queryList(Professorship.class, criteria);
    }
}