/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class CreateGroupProperties implements IServico {

	private ISuportePersistente persistentSupport = null;
	private IPersistentGroupProperties persistentGroupProperties = null;

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

	private void checkIfGroupPropertiestExists(String name, IDisciplinaExecucao executionCourse)
		throws FenixServiceException {

		IGroupProperties groupProperties = null;
		persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();

		try {
			groupProperties =
				persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(executionCourse, name);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		if (groupProperties != null)
			throw new ExistingServiceException();
	}
	/**
	 * Executes the service.
	 */
	public boolean run(Integer executionCourseCode, InfoGroupProperties infoGroupProperties)
		throws FenixServiceException {
			
		IDisciplinaExecucao executionCourse = null;
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentGroupProperties persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();

			executionCourse =(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode), false);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		checkIfGroupPropertiestExists(infoGroupProperties.getName(),executionCourse);

		try {
			IGroupProperties newGroupProperties = Cloner.copyInfoGroupProperties2IGroupProperties(infoGroupProperties);
			persistentGroupProperties.lockWrite(newGroupProperties);
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}
