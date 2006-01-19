package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.applicationTier.IService;

public class ReadPublicationByID implements IService {

    public InfoPublication run(Integer internalId, IUserView userView)
    		throws ExcepcaoPersistencia {
        InfoPublication infoPublication;
        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublication persistentPublication = sp.getIPersistentPublication();

        Publication publication = (Publication) persistentPublication.readByOID(Publication.class,
                internalId);
        infoPublication = InfoPublication.newInfoFromDomain(publication);

        return infoPublication;
    }


}