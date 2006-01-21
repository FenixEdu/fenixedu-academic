package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

public class ReadPublicationType extends Service {

    public InfoPublicationType run(Integer publicationTypeId) throws ExcepcaoPersistencia  {
            IPersistentPublicationType persistentPublicationType = persistentSupport.getIPersistentPublicationType();

            PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, publicationTypeId);
            
            return InfoPublicationType.newInfoFromDomain(publicationType);

    }
    
}