/*
 * Created on 3/Fev/2004
 *
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.Tutor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *
 */
public class InsertTutorShipWithOneStudent implements IService
{
	public InsertTutorShipWithOneStudent()
	{

	}

	public Object run(Integer teacherNumber, Integer studentNumber) throws FenixServiceException{
		if(teacherNumber == null || studentNumber == null){
			throw new FenixServiceException("error.tutor.impossibleOperation");
		}
		
		ISuportePersistente sp = null;
		Boolean result = Boolean.FALSE;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			
			//teacher
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
			if(teacher == null){
				throw new NonExistingServiceException();
			}			
			
			//student
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
			if(student == null){
				throw new NonExistingServiceException();
			}
			
			
			IPersistentTutor persistentTutor = sp.getIPersistentTutor();
			ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);
			if(tutor == null){
				tutor = new Tutor();
				tutor.setTeacher(teacher);
				tutor.setStudent(student);
			
				persistentTutor.simpleLockWrite(tutor);
			}
			
			result = Boolean.TRUE;
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new  FenixServiceException("error.tutor.associateOneStudent");
		}
		
		
		return result;
	}
}
