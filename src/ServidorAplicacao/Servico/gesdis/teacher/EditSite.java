/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EditSite implements IServico {	   

	private static EditSite service = new EditSite();    
	public static EditSite getService() {
		return service;
	}
    
	private EditSite() { }    
	public final String getNome() {
		return "EditSite";
	}    
	    
	public Boolean run(InfoSite infoSiteOld,					 
					InfoSite infoSiteNew
					)
	throws FenixServiceException {
		IPersistentSite persistentSite = null;
		try {       		
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();		 
		persistentSite = sp.getIPersistentSite();		
		ISite siteOld = Cloner.copyInfoSite2ISite(infoSiteOld);
		IExecutionCourse executionCourse = siteOld.getExecutionCourse();		
		siteOld = persistentSite.readByExecutionCourse(executionCourse);
		
		
		siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
		siteOld.setMail(infoSiteNew.getMail());	
		siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
		siteOld.setIntroduction(infoSiteNew.getIntroduction());																
		
		persistentSite.lockWrite(siteOld);					
		}
		catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}	
		return new Boolean(true);
	}	
}
