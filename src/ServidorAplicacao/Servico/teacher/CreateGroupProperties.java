/*
 * Created on 28/Jul/2003
 *
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

	private void checkIfGroupPropertiestExists(String name, IDisciplinaExecucao executionCourse)
		throws FenixServiceException {
			
		IGroupProperties groupProperties =null;	
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
			persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();

			groupProperties =persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(executionCourse, name);

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
		
		System.out.println("<-------ENTRA NO SERVICO ");
				
		IDisciplinaExecucao executionCourse = null;
		try {
				
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			executionCourse =(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode), false);
			
			checkIfGroupPropertiestExists(infoGroupProperties.getName(),executionCourse);
			
			IPersistentGroupProperties persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
			infoGroupProperties.setInfoExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
			
			IGroupProperties newGroupProperties = Cloner.copyInfoGroupProperties2IGroupProperties(infoGroupProperties);	
			newGroupProperties.setExecutionCourse(executionCourse);
			
			persistentGroupProperties.lockWrite(newGroupProperties);
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		System.out.println("<-------SAI DO SERVICO ");
		return true;
	}
}
