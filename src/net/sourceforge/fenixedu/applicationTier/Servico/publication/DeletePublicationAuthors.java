/**
 * Created on 09-Nov-2004
 *
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
     * @throws ExcepcaoPersistencia
     */
	public void run(Integer publicationId) throws ExcepcaoPersistencia {
	    ISuportePersistente sp = null;
	    IPersistentPublicationAuthor persistentPublicationAuthor = null;
	    
	    sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentPublicationAuthor = sp.getIPersistentPublicationAuthor();
        
        persistentPublicationAuthor.deleteAllByPublicationID(publicationId);
	}

}