/*
 * Created on 30/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteExecutionCourses implements IServico {

	private static DeleteExecutionCourses service = new DeleteExecutionCourses();

	public static DeleteExecutionCourses getService() {
		return service;
	}

	private DeleteExecutionCourses() {
	}

	public final String getNome() {
		return "DeleteExecutionCourses";
	}
	
	// delete a set of execution courses
	public List run(List internalIds) throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			ITurnoPersistente persistentShift = sp.getITurnoPersistente();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();

			Iterator iter = internalIds.iterator();
			List shifts, attends;
			Integer internalId;
			List undeletedExecutionCoursesCodes = new ArrayList();

			while(iter.hasNext()) {
				internalId = (Integer) iter.next();
				IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(internalId), false);
				if(executionCourse != null) {
					shifts = persistentShift.readByExecutionCourse(executionCourse);
					if(!shifts.isEmpty())
						undeletedExecutionCoursesCodes.add((String) executionCourse.getSigla());
					else {
						attends = persistentAttend.readByExecutionCourse(executionCourse);
						if(attends.isEmpty())
							persistentExecutionCourse.deleteExecutionCourse(executionCourse);
						else
							undeletedExecutionCoursesCodes.add((String) executionCourse.getSigla());
					}				
				}	
			}
			
			return undeletedExecutionCoursesCodes;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}