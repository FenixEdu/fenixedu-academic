
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePublication extends Service {

	public void run(final Integer publicationId) throws ExcepcaoPersistencia {
        final Publication publication = (Publication) persistentObject.readByOID(Publication.class, publicationId);
        publication.delete();
	}

}