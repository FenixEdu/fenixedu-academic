package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPublicationByInternalId implements IService {

    public InfoPublication run(Integer internalId, IUserView userView)
    		throws ExcepcaoPersistencia {
        InfoPublication infoPublication;
        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublication persistentPublication = sp.getIPersistentPublication();

        IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
                internalId);
        infoPublication = InfoPublication.newInfoFromDomain(publication);

        return infoPublication;
    }


}