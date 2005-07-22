package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationTypeWithAttributesAndSubtypes;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllPublicationTypes implements IService {

    public List<InfoPublicationType> run(String user) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSuport
                .getIPersistentPublicationType();
        List<IPublicationType> publicationTypeList = (List<IPublicationType>) persistentPublicationType.readAll(PublicationType.class);

        List<InfoPublicationType> result = new ArrayList<InfoPublicationType>();
        for (IPublicationType publicationType : publicationTypeList) {
        	result.add(InfoPublicationTypeWithAttributesAndSubtypes.newInfoFromDomain(publicationType));
		}

        return result;
    }
}