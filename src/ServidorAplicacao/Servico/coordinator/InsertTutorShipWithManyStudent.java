/*
 * Created on 3/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

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
public class InsertTutorShipWithManyStudent implements IService
{

	public InsertTutorShipWithManyStudent()
	{

	}

	public Object run(Integer teacherNumber, Integer studentNumberFirst, Integer studentNumberSecond) throws FenixServiceException
	{
		if (teacherNumber == null || studentNumberFirst == null || studentNumberSecond == null)
		{
			throw new FenixServiceException("error.tutor.impossibleOperation");
		}

		ISuportePersistente sp = null;
		List studentsErrors = new ArrayList(); 
		try
		{
			sp = SuportePersistenteOJB.getInstance();

			//teacher
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
			if (teacher == null)
			{
				throw new NonExistingServiceException();
			}

			//students in the range [studentNumberFirst, studentNumberSecond]
			for (int i = studentNumberFirst.intValue(); i <= studentNumberSecond.intValue(); i++)
			{
				IPersistentStudent persistentStudent = sp.getIPersistentStudent();
				Integer studentNumber = new Integer(i);
				IStudent student =
				persistentStudent.readStudentByNumberAndDegreeType(
						studentNumber,
						TipoCurso.LICENCIATURA_OBJ);
				if (student == null)
				{
					//student doesn't exists...
					studentsErrors.add(studentNumber);
					continue;
				}

				IPersistentTutor persistentTutor = sp.getIPersistentTutor();
				ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);
				if (tutor == null)
				{
					tutor = new Tutor();
					tutor.setTeacher(teacher);
					tutor.setStudent(student);

					persistentTutor.simpleLockWrite(tutor);
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("error.tutor.associateManyStudent");
		}
		
		return studentsErrors;
	}
}
