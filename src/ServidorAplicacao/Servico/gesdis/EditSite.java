/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis;

import DataBeans.util.*;
import DataBeans.gesdis.InfoSite;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
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
		IDisciplinaExecucao executionCourse = siteOld.getExecutionCourse();		
		siteOld = persistentSite.readByExecutionCourse(executionCourse);
		
		
		siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
		siteOld.setMail(infoSiteNew.getMail());																	
		
		persistentSite.lockWrite(siteOld);					
		}
		catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}	
		return new Boolean(true);
	}	
}
