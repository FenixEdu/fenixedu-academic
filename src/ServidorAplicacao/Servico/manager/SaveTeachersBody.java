/*
 * Created on 23/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class SaveTeachersBody implements IServico {

	private static SaveTeachersBody service = new SaveTeachersBody();

	/**
	 * The singleton access method of this class.
	 */
	public static SaveTeachersBody getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private SaveTeachersBody() {
	}

	/**
	 * Service name
	 */
	public final String getNome() {
		return "SaveTeachersBody";
	}

	/**
	 * Executes the service.
	 */

	public void run(Integer[] responsiblesIds, Integer[] teachersIds, Integer executionCourseId) throws FenixServiceException {

		try {
			
				List responsibleTeachersIds = Arrays.asList(responsiblesIds);
				List professorShipTeachersIds = Arrays.asList(teachersIds);
			
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
				IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseId), false);

				// if person doesn´t teach, can´t be responsible either
				Iterator iter = responsibleTeachersIds.iterator();
				Integer id;
				while(iter.hasNext()) {
					id = (Integer) iter.next();
					if(!professorShipTeachersIds.contains(id))
						responsibleTeachersIds.remove(id);
				}

				// saving the new responsibles
				
				// transform responsibles into teachersIds
				List oldResponsibles = sp.getIPersistentResponsibleFor().readByExecutionCourse(executionCourse);
				Iterator iterator = oldResponsibles.iterator();
				Integer teacherId;
				IResponsibleFor responsibleFor;
				List oldResponsibleTeachersIds = new ArrayList();
				IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
				while(iterator.hasNext()) {
					responsibleFor = (IResponsibleFor) iterator.next();
					teacherId = responsibleFor.getTeacher().getIdInternal();
					oldResponsibleTeachersIds.add(teacherId);
				}
				
				// remove old responsibles
				Iterator oldIterator = oldResponsibleTeachersIds.iterator();
				while(oldIterator.hasNext()) {
					id = (Integer) oldIterator.next();
					if(!responsibleTeachersIds.contains(id))
						persistentResponsibleFor.deleteByOID(ResponsibleFor.class, ((IResponsibleFor) oldResponsibles.get(oldResponsibleTeachersIds.indexOf(id))).getIdInternal());
				}
				
				// add new responsibles
				Iterator newIterator = responsibleTeachersIds.iterator();
				IResponsibleFor responsibleForToWrite;
				ITeacher teacher;
				while(newIterator.hasNext()) {
					id = (Integer) newIterator.next();
					if(!oldResponsibleTeachersIds.contains(id)) {
						responsibleForToWrite = new ResponsibleFor();
						responsibleForToWrite.setExecutionCourse(executionCourse);
						teacher = (ITeacher) sp.getIPersistentTeacher().readByOId(new Teacher(id), false);
						responsibleForToWrite.setTeacher(teacher);
						persistentResponsibleFor.lockWrite(responsibleForToWrite);
					}
						
				}
				
				// to be done: lecturers
						
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}