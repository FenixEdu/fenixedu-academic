/*
 * Created on 2/Fev/2004
 *
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTutor;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
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

	
	public List run(
		Integer executionDegreeId, Integer tutorNumber) throws FenixServiceException{
		if(tutorNumber == null){
			throw new FenixServiceException("error.tutor.impossibleOperation");
		}
		
		List infoTutorStudents = null;
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
			
			List tutorStudents = persistentTutor.readStudentsByTeacher(teacher);
			if(tutorStudents == null || tutorStudents.size() <= 0){
				return infoTutorStudents;
			}
		
			//Clone
			infoTutorStudents = new ArrayList();
			ListIterator iterator = tutorStudents.listIterator();
			while (iterator.hasNext())
			{
				ITutor tutor = (ITutor) iterator.next();
				InfoTutor infoTutor = Cloner.copyITutor2InfoTutor(tutor);
				
				infoTutorStudents.add(infoTutor);
			}			
		}
		catch (ExcepcaoPersistencia e)
		{			
			e.printStackTrace();
			throw new FenixServiceException("error.tutor.impossibleOperation");
		}
				
		return infoTutorStudents;
	}
}
