/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorPersistente.OJB.degree.finalProject;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionYear;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.degree.finalProject.TeacherDegreeFinalProjectStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudentOJB
    extends ObjectFenixOJB
    implements IPersistentTeacherDegreeFinalProjectStudent
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent#readByTeacher(Dominio.ITeacher)
	 */
    public List readByTeacherAndExecutionYear(ITeacher teacher, IExecutionYear executionYear) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
		criteria.addEqualTo("keyExecutionYear", executionYear.getIdInternal());
        return queryList(TeacherDegreeFinalProjectStudent.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent#readByUnique(Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent)
     */
    public ITeacherDegreeFinalProjectStudent readByUnique(ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyTeacher", teacherDegreeFinalProjectStudent.getTeacher().getIdInternal());
		criteria.addEqualTo("keyExecutionYear", teacherDegreeFinalProjectStudent.getExecutionYear().getIdInternal());
		criteria.addEqualTo("keyStudent", teacherDegreeFinalProjectStudent.getStudent().getIdInternal());		
		return (ITeacherDegreeFinalProjectStudent) queryObject(TeacherDegreeFinalProjectStudent.class, criteria);

        
    }

}
