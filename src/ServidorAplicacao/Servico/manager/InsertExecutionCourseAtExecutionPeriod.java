/*
 * Created on 24/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionCourse;
import Dominio.ExecutionCourse;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertExecutionCourseAtExecutionPeriod implements IServico {

	private static InsertExecutionCourseAtExecutionPeriod service = new InsertExecutionCourseAtExecutionPeriod();

	public static InsertExecutionCourseAtExecutionPeriod getService() {
		return service;
	}

	private InsertExecutionCourseAtExecutionPeriod() {
	}

	public final String getNome() {
		return "InsertExecutionCourseAtExecutionPeriod";
	}
	

	public void run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
		IExecutionCourse executionCourse = new ExecutionCourse();
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
								
			IPersistentExecutionPeriod persistentExecutionPeriod = persistentSuport.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOId(new ExecutionPeriod(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()), false);
				
			if(executionPeriod == null)
				throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
			
			IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
			IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
				
			executionCourse.setNome(infoExecutionCourse.getNome());
			executionCourse.setExecutionPeriod(executionPeriod);
			executionCourse.setSigla(infoExecutionCourse.getSigla());
			executionCourse.setLabHours(infoExecutionCourse.getLabHours());
			executionCourse.setPraticalHours(infoExecutionCourse.getPraticalHours());
			executionCourse.setTheoPratHours(infoExecutionCourse.getTheoPratHours());
			executionCourse.setTheoreticalHours(infoExecutionCourse.getTheoreticalHours());
			executionCourse.setComment(infoExecutionCourse.getComment());
	
			ISite site = new Site();
			site.setExecutionCourse(executionCourse);
			
			persistentExecutionCourse.lockWrite(executionCourse);
			persistentSite.lockWrite(site);
					
		} catch (ExistingPersistentException existingException) {
			throw new ExistingServiceException("A disciplina execução com sigla:"+ executionCourse.getSigla(), existingException); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
