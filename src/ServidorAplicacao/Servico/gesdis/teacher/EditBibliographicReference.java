/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoBibliographicReference;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EditBibliographicReference implements IServico {	   

	private static EditBibliographicReference service = new EditBibliographicReference();    
	public static EditBibliographicReference getService() {
		return service;
	}
    
	private EditBibliographicReference() { }    
	public final String getNome() {
		return "EditBibliographicReference";
	}    
	    
	public Boolean run(InfoExecutionCourse infoExecutionCourse,
					InfoBibliographicReference infoBibliographicReferenceOld, 
					InfoBibliographicReference infoBibliographicReferenceNew 
					)
	throws FenixServiceException {
		try {       		
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();		 
		IPersistentBibliographicReference persistentBibliographicReference= null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;
		IPersistentExecutionYear persistentExecutionYear = null;		
		IBibliographicReference bibliographicReference = null;		
		IBibliographicReference bibliographicReferenceNew = null;
		IDisciplinaExecucao executionCourse = null;
						
		persistentExecutionYear = sp.getIPersistentExecutionYear();
		persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
		persistentBibliographicReference = sp.getIPersistentBibliographicReference();
							
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear());					
		InfoExecutionPeriod infoExecutionPeriod = infoExecutionCourse.getInfoExecutionPeriod();		
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(infoExecutionPeriod.getName(),executionYear);						
		executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),executionPeriod);				 				
		bibliographicReference = persistentBibliographicReference.readBibliographicReference(executionCourse,infoBibliographicReferenceOld.getTitle(),infoBibliographicReferenceOld.getAuthors(),infoBibliographicReferenceOld.getReference(),infoBibliographicReferenceOld.getYear());						
								
		bibliographicReference.setAuthors(infoBibliographicReferenceNew.getAuthors());
		bibliographicReference.setTitle(infoBibliographicReferenceNew.getTitle());
		bibliographicReference.setReference(infoBibliographicReferenceNew.getReference());
		bibliographicReference.setYear(infoBibliographicReferenceNew.getYear());
		bibliographicReference.setOptional(infoBibliographicReferenceNew.getOptional());
		persistentBibliographicReference.lockWrite(bibliographicReference);	
		}
		catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}	
		return new Boolean(true);
	}	
}
