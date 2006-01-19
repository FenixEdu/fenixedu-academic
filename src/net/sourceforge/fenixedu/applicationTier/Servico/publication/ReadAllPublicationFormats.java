package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationFormat;
import net.sourceforge.fenixedu.domain.publication.PublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadAllPublicationFormats extends Service {

    public List<InfoPublicationFormat> run(int publicationTypeId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationFormat persistentPublicationFormat = persistentSuport
                .getIPersistentPublicationFormat();

        List<PublicationFormat> publicationFormatList = (List<PublicationFormat>)persistentPublicationFormat.readAll(PublicationFormat.class);

        List<InfoPublicationFormat> result = new ArrayList<InfoPublicationFormat>();
        
        for(PublicationFormat publicationFormat : publicationFormatList) {
            result.add(InfoPublicationFormat.newInfoFromDomain(publicationFormat));
        }
        
        return result;
    }
}