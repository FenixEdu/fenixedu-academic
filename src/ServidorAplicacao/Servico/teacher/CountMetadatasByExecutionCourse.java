/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */

public class CountMetadatasByExecutionCourse implements IService {
	public CountMetadatasByExecutionCourse() {
	}

	public Integer run(Integer executionCourseId) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB
					.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
					.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
					.readByOID(ExecutionCourse.class, executionCourseId);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}

			return new Integer(persistentSuport.getIPersistentMetadata()
					.countByExecutionCourse(executionCourse));

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}