/**
 * Created on 09-Nov-2004
 *
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 * 
 */
package ServidorAplicacao.Servico.publication;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.publication.IPublicationAuthor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPublicationAuthor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 *  * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a>& <a
 *         href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a> 
 */
public class DeletePublicationAuthors implements IService {

    public DeletePublicationAuthors() {
    }

    public String getName() {
        return "DeletePublicationAuthors";
    }

    /**
     * This method deletes all the PublicationAuthors for a given publication in the Database.
     * @param publicationId the id of the publication.
     * @throws FenixServiceException 
     * @throws ExcepcaoPersistencia
     */
	public void run(Integer publicationId) throws FenixServiceException, ExcepcaoPersistencia {
	    IPublicationAuthor publicationAuthor = null;
	    ISuportePersistente sp = null;
	    IPersistentPublicationAuthor persistentPublicationAuthor = null;
	    
	    sp = SuportePersistenteOJB.getInstance();
        persistentPublicationAuthor = sp.getIPersistentPublicationAuthor();
        
        persistentPublicationAuthor.deleteAllByPublicationID(publicationId);
	}

}