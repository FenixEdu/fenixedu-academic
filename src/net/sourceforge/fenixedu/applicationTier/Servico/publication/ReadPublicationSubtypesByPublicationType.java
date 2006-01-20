package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationSubtype;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationSubtypeWithPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationSubtype;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.applicationTier.Service;


public class ReadPublicationSubtypesByPublicationType extends Service {

    public List<InfoPublicationSubtype> run(int publicationTypeId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSupport
                .getIPersistentPublicationType();
        PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List<InfoPublicationSubtype> result = new ArrayList<InfoPublicationSubtype>();
        if (publicationType != null) {
            List<PublicationSubtype> publicationSubtypeList = publicationType.getSubtypes();

            for (PublicationSubtype publicationSubtype : publicationSubtypeList) {
                result.add(InfoPublicationSubtypeWithPublicationType.newInfoFromDomain(publicationSubtype));
            }
        }

        return result;
    }
}