package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationFormat;
import net.sourceforge.fenixedu.domain.research.result.PublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllPublicationFormats extends Service {

    public List<InfoPublicationFormat> run(int publicationTypeId) throws ExcepcaoPersistencia {
        List<InfoPublicationFormat> result = new ArrayList<InfoPublicationFormat>();
        
        List<PublicationFormat> publicationFormatList = rootDomainObject.getPublicationFormats();
        for(PublicationFormat publicationFormat : publicationFormatList) {
            result.add(InfoPublicationFormat.newInfoFromDomain(publicationFormat));
        }
        
        return result;
    }
    
}