package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPublicationByID extends Service {

    public InfoPublication run(Integer internalId, IUserView userView) throws ExcepcaoPersistencia {
        Publication publication = (Publication) persistentObject.readByOID(Publication.class, internalId);

        return InfoPublication.newInfoFromDomain(publication);
    }

}
