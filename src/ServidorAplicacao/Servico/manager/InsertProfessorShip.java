/*
 * Created on 22/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoProfessorship;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertProfessorShip implements IServico {

	private static InsertProfessorShip service = new InsertProfessorShip();

	public static InsertProfessorShip getService() {
		return service;
	}

	private InsertProfessorShip() {
	}

	public final String getNome() {
		return "InsertProfessorShip";
	}
	

	public void run(InfoProfessorship infoProfessorShip) throws FenixServiceException {
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				Integer executionCourseId = infoProfessorShip.getInfoExecutionCourse().getIdInternal();
				IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
				IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(new ExecutionCourse(executionCourseId), false);
				
				if(executionCourse == null)
					throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
				
				Integer teacherNumber = infoProfessorShip.getInfoTeacher().getTeacherNumber();
				IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
				ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
				
				if(teacher == null)
					throw new NonExistingServiceException("message.non.existing.teacher", null);
				
				IPersistentProfessorship persistentProfessorShip = persistentSuport.getIPersistentProfessorship();
				
				IProfessorship professorShip = new Professorship();
				professorShip.setExecutionCourse(executionCourse);
				professorShip.setTeacher(teacher);						

				persistentProfessorShip.lockWrite(professorShip);
					
		} catch (ExistingPersistentException e) {
			return;
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}