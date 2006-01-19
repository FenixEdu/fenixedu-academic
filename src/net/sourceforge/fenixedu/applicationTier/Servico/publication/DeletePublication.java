
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.applicationTier.IService;

public class DeletePublication implements IService {

	public void run(final Integer publicationId) throws ExcepcaoPersistencia {
		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentPublication persistentPublication = sp.getIPersistentPublication();

        final Publication publication = (Publication) persistentPublication.readByOID(Publication.class, publicationId);
        publication.delete();
        
	}

}