/*
 * Created on 27/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class CreateSiteInExecutionCourse  implements IServico {

	private static CreateSiteInExecutionCourse service = new CreateSiteInExecutionCourse();

	public static CreateSiteInExecutionCourse getService() {
		return service;
	}

	private CreateSiteInExecutionCourse() {
	}

	public final String getNome() {
		return "CreateSiteInExecutionCourse";
	}
	

	public void run(Integer executionCourseId) throws FenixServiceException {
		
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
								
			IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
			IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
			IExecutionCourse executionCourseToRead = new ExecutionCourse();
			executionCourseToRead.setIdInternal(executionCourseId);
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourseToRead, false);
				
			if(executionCourse == null)
				throw new NonExistingServiceException("message.non.existing.execution.course", null);
	
			ISite site = new Site();
			site.setExecutionCourse(executionCourse);
			
			persistentSite.lockWrite(site);
					
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
