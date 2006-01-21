
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

public class DeletePublication extends Service {

	public void run(final Integer publicationId) throws ExcepcaoPersistencia {
        final IPersistentPublication persistentPublication = persistentSupport.getIPersistentPublication();

        final Publication publication = (Publication) persistentPublication.readByOID(Publication.class, publicationId);
        publication.delete();
        
	}

}