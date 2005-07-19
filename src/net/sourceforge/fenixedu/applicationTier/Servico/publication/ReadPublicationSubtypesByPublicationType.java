package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationSubtype;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationSubtypeWithPublicationType;
import net.sourceforge.fenixedu.domain.publication.IPublicationSubtype;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;


public class ReadPublicationSubtypesByPublicationType implements IService {

    public List<InfoPublicationSubtype> run(int publicationTypeId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSuport
                .getIPersistentPublicationType();
        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List<IPublicationSubtype> publicationSubtypeList = publicationType.getSubtypes();

        List<InfoPublicationSubtype> result = (List<InfoPublicationSubtype>) CollectionUtils.collect(publicationSubtypeList, new Transformer() {
            public Object transform(Object o) {
                return InfoPublicationSubtypeWithPublicationType.newInfoFromDomain((IPublicationSubtype) o);                    
            }
        });

        return result;
    }
}