/*
 * Created on 2/Fev/2004
 *
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTeacherWithPerson;
import DataBeans.InfoTutor;
import DataBeans.InfoTutorWithInfoStudent;
import Dominio.IDepartment;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class ReadStudentsByTutor implements IService
{

	public ReadStudentsByTutor()
	{

	}

	/*
	 * This service returns a list with size two:
	 * 		first element is infoTeacher that is tutor
	 * 		second element is the list of students that this tutor tutorizes
	 * 
	 */
	
	public List run(
		Integer executionDegreeId, Integer tutorNumber) throws FenixServiceException{
		if(tutorNumber == null){
			throw new FenixServiceException("error.tutor.impossibleOperation");
		}
		
		List teacherAndStudentsList = null;
		ISuportePersistente sp = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			
			IPersistentTutor persistentTutor = sp.getIPersistentTutor();
			
			ITeacher teacher = new Teacher();
			teacher.setTeacherNumber(tutorNumber);
			
			IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
			IDepartment department = persistentDepartment.readByTeacher(teacher);
			
			//Now only LEEC's teachers can be tutor 
			if(!department.getCode().equals(new String("21"))){
				throw new FenixServiceException("error.tutor.tutor.notLEEC");
			}

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacherDB = persistentTeacher.readByNumber(tutorNumber);
			
			teacherAndStudentsList = new ArrayList();
			teacherAndStudentsList.add(InfoTeacherWithPerson.newInfoFromDomain(teacherDB));

			List tutorStudents = persistentTutor.readStudentsByTeacher(teacher);
			if(tutorStudents == null || tutorStudents.size() <= 0){
				return teacherAndStudentsList;
			}
		
			//Clone
			List infoTutorStudents = new ArrayList();
			ListIterator iterator = tutorStudents.listIterator();
			while (iterator.hasNext())
			{
				ITutor tutor = (ITutor) iterator.next();
				//CLONER
				//InfoTutor infoTutor = Cloner.copyITutor2InfoTutor(tutor);
				InfoTutor infoTutor = InfoTutorWithInfoStudent.newInfoFromDomain(tutor);
				infoTutorStudents.add(infoTutor);
			}			
			teacherAndStudentsList.add(infoTutorStudents);
		}
		catch (ExcepcaoPersistencia e)
		{			
			e.printStackTrace();
			throw new FenixServiceException("error.tutor.impossibleOperation");
		}
				
		return teacherAndStudentsList;
	}
}
