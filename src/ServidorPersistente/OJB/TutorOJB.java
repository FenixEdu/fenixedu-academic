/*
 * Created on 2/Fev/2004
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.Tutor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTutor;

/**
 * @author Tânia Pousão
 *  
 */
public class TutorOJB extends ObjectFenixOJB implements IPersistentTutor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentTutor#readTeachersByStudent(Dominio.IStudent)
	 */
	public List readTeachersByStudent(IStudent student) throws ExcepcaoPersistencia
	{		
		Criteria criteria = new Criteria();
		if (student != null && student.getIdInternal() != null)
		{
			criteria.addEqualTo("student.idInternal", student.getIdInternal());
		}
		if (student != null && student.getNumber() != null)
		{
			criteria.addEqualTo("student.number", student.getNumber());
		}
		return queryList(Tutor.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentTutor#readStudentsByTeacher(Dominio.ITeacher)
	 */
	public List readStudentsByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		if (teacher != null && teacher.getIdInternal() != null)
		{
			criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		}
		if (teacher != null && teacher.getTeacherNumber() != null)
		{
			criteria.addEqualTo("teacher.number", teacher.getTeacherNumber());
		}
		return queryList(Tutor.class, criteria);
	}

}
