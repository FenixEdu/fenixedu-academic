/*
 * Created on 28/Jul/2003
 *
*/
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 *
 */

public class CreateGroupProperties implements IServico {

	private static CreateGroupProperties service = new CreateGroupProperties();

	/**
	 * The singleton access method of this class.
	 */
	public static CreateGroupProperties getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private CreateGroupProperties() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "CreateGroupProperties";
	}

	/**
	 * Executes the service.
	 */
	public boolean run(Integer executionCourseCode, InfoGroupProperties infoGroupProperties) throws FenixServiceException {

		IExecutionCourse executionCourse = null;
		try {

			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentGroupProperties persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();

			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(new ExecutionCourse(executionCourseCode), false);

			infoGroupProperties.setInfoExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));

			IGroupProperties newGroupProperties = Cloner.copyInfoGroupProperties2IGroupProperties(infoGroupProperties);

			persistentGroupProperties.lockWrite(newGroupProperties);

		} catch (ExistingPersistentException excepcaoPersistencia) {
			throw new ExistingServiceException(excepcaoPersistencia);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}
