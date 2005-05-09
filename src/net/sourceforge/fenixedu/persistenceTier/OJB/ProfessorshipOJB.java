/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 * 
 *  
 */
public class ProfessorshipOJB extends PersistentObjectOJB implements IPersistentProfessorship {

    /**
     *  
     */
    public ProfessorshipOJB() {
        super();
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Professorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentProfessorship#readByTeacherIDAndExecutionCourseID(Dominio.ITeacher,
     *      Dominio.IDisciplinaExecucao)
     */
    public IProfessorship readByTeacherIDAndExecutionCourseID(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        return (IProfessorship) queryObject(Professorship.class, criteria);
    }

    public IProfessorship readByTeacherAndExecutionCourse(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
        criteria.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        criteria.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());
        return (IProfessorship) queryObject(Professorship.class, criteria);
    }

    public IProfessorship readByTeacherAndExecutionCoursePB(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        return (IProfessorship) queryObject(Professorship.class, criteria);

    }

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
        return queryList(Professorship.class, criteria);
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourse.getIdInternal());
        return queryList(Professorship.class, crit);
    }

    public void delete(IProfessorship professorship) throws ExcepcaoPersistencia {
        super.delete(professorship);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List result = queryList(Professorship.class, criteria);
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            delete((IProfessorship) iterator.next());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentProfessorship#readByTeacherAndTypeOfDegree(Dominio.ITeacher,
     *      Util.TipoCurso)
     */
    public List readByTeacherAndTypeOfDegree(ITeacher teacher, DegreeType degreeType)
            throws ExcepcaoPersistencia {
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
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(Professorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTeacher#readByExecutionDegree(Dominio.IExecutionDegree)
     */
    public List readByExecutionDegreesAndBasic(List executionDegrees, Boolean basic)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List executionDegreesIds = (List) CollectionUtils.collect(executionDegrees, new Transformer() {
            public Object transform(Object o) {
                return ((IExecutionDegree) o).getDegreeCurricularPlan().getIdInternal();
            }
        });
        criteria.addIn("executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegreesIds);
        if (executionDegrees != null && !executionDegrees.isEmpty()) {
            criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal",
                    ((IExecutionDegree) executionDegrees.get(0)).getExecutionYear().getIdInternal());
        }
        criteria.addEqualTo("executionCourse.associatedCurricularCourses.basic", basic);
        return queryList(Professorship.class, criteria, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentProfessorship#readByExecutionDegree(Dominio.IExecutionDegree)
     */
    public List readByExecutionDegree(IExecutionDegree executionDegree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegree.getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionDegree
                .getExecutionYear().getIdInternal());
        return queryList(Professorship.class, criteria, true);
    }

    
    
	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentProfessorship#readByExecutionDegreeAndExecutionPeriod(Dominio.IExecutionDegree, Dominio.IExecutionPeriod)
	 */
	public List readByExecutionDegreeAndExecutionPeriod(
			IExecutionDegree executionDegree, IExecutionPeriod executionPeriod)
			throws ExcepcaoPersistencia {
		
		Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegree.getDegreeCurricularPlan().getIdInternal());
        /*criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionDegree
                .getExecutionYear().getIdInternal());*/
        criteria.addEqualTo("executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());

        return queryList(Professorship.class, criteria, true);
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentProfessorship#readByExecutionDegreeAndBasic(Dominio.IExecutionDegree,
     *      java.lang.Boolean)
     */
    public List readByExecutionDegreeAndBasic(IExecutionDegree executionDegree, Boolean basic)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegree.getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionDegree
                .getExecutionYear().getIdInternal());
        criteria.addEqualTo("executionCourse.associatedCurricularCourses.basic", basic);
        return queryList(Professorship.class, criteria, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentProfessorship#readByTeacherAndExecutionYear(Dominio.ITeacher,
     *      Dominio.IExecutionYear)
     */
    public List readByTeacherAndExecutionYear(ITeacher teacher, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionYear
                .getIdInternal());
        return queryList(Professorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentProfessorship#readByExecutionDegrees(java.util.List)
     */
    public List readByExecutionDegrees(List executionDegrees) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List executionDegreesIds = (List) CollectionUtils.collect(executionDegrees, new Transformer() {
            public Object transform(Object o) {
                return ((IExecutionDegree) o).getDegreeCurricularPlan().getIdInternal();
            }
        });
        criteria.addIn("executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegreesIds);
        if (executionDegrees != null && !executionDegrees.isEmpty()) {
            criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal",
                    ((IExecutionDegree) executionDegrees.get(0)).getExecutionYear().getIdInternal());
        }
        return queryList(Professorship.class, criteria, true);
    }

    public IProfessorship readByTeacherIDandExecutionCourseID(Integer teacherID,
            Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);
        criteria.addEqualTo("keyTeacher", teacherID);
        return (IProfessorship) queryObject(Professorship.class, criteria);
    }
}