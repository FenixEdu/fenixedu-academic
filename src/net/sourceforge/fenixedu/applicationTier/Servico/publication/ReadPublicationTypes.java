package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationTypeWithAttributesAndSubtypes;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPublicationTypes implements IService {

    public List run(String user) throws FenixServiceException {
        try {

            ISuportePersistente persistentSuport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();

            IPersistentPublicationType persistentPublicationType = persistentSuport
                    .getIPersistentPublicationType();
            List publicationTypeList = (List) persistentPublicationType.readAll(PublicationType.class);

            List result = (List) CollectionUtils.collect(publicationTypeList, new Transformer() {
                public Object transform(Object o) {
                    IPublicationType publicationType = (IPublicationType) o;
                    return InfoPublicationTypeWithAttributesAndSubtypes
                            .newInfoFromDomain(publicationType);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}