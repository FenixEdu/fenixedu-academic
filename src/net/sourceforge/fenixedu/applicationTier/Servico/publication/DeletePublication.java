/*
 * Created on Jun 11, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */
public class DeletePublication implements IService {

	public DeletePublication() {
	}

	public void run(final Integer publicationId) throws ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		
		IPersistentPublication persistentPublication = sp.getIPersistentPublication();

		persistentPublication.deleteByOID(Publication.class,publicationId);
		
	}
}