package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationTypeWithAttributesAndSubtypes;
import net.sourceforge.fenixedu.domain.research.result.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllPublicationTypes extends Service {

    public List<InfoPublicationType> run(String user) throws ExcepcaoPersistencia {
        List<PublicationType> publicationTypeList = rootDomainObject.getPublicationTypes();

        List<InfoPublicationType> result = new ArrayList<InfoPublicationType>();
        for (PublicationType publicationType : publicationTypeList) {
        	result.add(InfoPublicationTypeWithAttributesAndSubtypes.newInfoFromDomain(publicationType));
		}

        return result;
    }
    
}
