/*
 * Created on 25-Oct-2004
 *
 * @author Carlos Pereira & Francisco Passos
 * 
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Servico.publication;

import DataBeans.publication.InfoPublication;
import Dominio.publication.IPublication;
import Dominio.publication.Publication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;

/**
 * @author Carlos Pereira & Francisco Passos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReadPublicationByInternalId implements IServico{

	public String getNome() {
		
		return "ReadPublicationByInternalId";
	}
	
	public InfoPublication run(Integer internalId) throws FenixServiceException{
		InfoPublication infoPublication = new InfoPublication();
		ISuportePersistente sp;
		try {
			sp = SuportePersistenteOJB.getInstance();
		
		IPersistentPublication persistentPublication = sp.getIPersistentPublication();

		IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,internalId);
		infoPublication.copyFromDomain(publication);
		
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
			
		}
		return infoPublication;
	}
	
}
