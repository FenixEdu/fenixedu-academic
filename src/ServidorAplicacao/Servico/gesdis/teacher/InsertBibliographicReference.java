/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoExecutionCourse;
import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InsertBibliographicReference implements IServico {

	private static InsertBibliographicReference service =
		new InsertBibliographicReference();

	public static InsertBibliographicReference getService() {
		return service;
	}

	private InsertBibliographicReference() {
	}

	public final String getNome() {
		return "InsertBibliographicReference";
	}	

	public Boolean run(
		InfoExecutionCourse infoExecutionCourse,
		String title,
		String authors,
		String reference,
		String year,
		Boolean optional)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentBibliographicReference persistentBibliographicReference = null;
			IPersistentExecutionYear persistentExecutionYear = null; 	
			IPersistentExecutionPeriod persistentExecutionPeriod = null;				
			persistentBibliographicReference =
				sp.getIPersistentBibliographicReference();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IBibliographicReference newBibliographicReference =
				new BibliographicReference();			
			persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			persistentExecutionYear = sp.getIPersistentExecutionYear();			
			IExecutionYear executionYear = 
				persistentExecutionYear.readExecutionYearByName(infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear());
			IExecutionPeriod executionPeriod = 
				persistentExecutionPeriod.readByNameAndExecutionYear(infoExecutionCourse.getInfoExecutionPeriod().getName(), executionYear);
			IDisciplinaExecucao executionCourse = 
				persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(), 
					executionPeriod); 						
			newBibliographicReference.setExecutionCourse(executionCourse);
			newBibliographicReference.setTitle(title);
			newBibliographicReference.setAuthors(authors);
			newBibliographicReference.setReference(reference);
			newBibliographicReference.setYear(year);
			newBibliographicReference.setOptional(optional);
			persistentBibliographicReference.lockWrite(
				newBibliographicReference);											
		} 
		catch (ExistingPersistentException e) {throw new ExistingServiceException(e);} 
		catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
			}
			return new Boolean(true);
	}
}
