package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertPublication extends Service {

    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia, FenixServiceException {
        final List infoAuthorsList = infoPublication.getInfoPublicationAuthors();

        final List<Person> authors = new InsertInexistentAuthors().run(infoAuthorsList);

        final PublicationType publicationType = rootDomainObject.readPublicationTypeByOID(infoPublication.getInfoPublicationType().getIdInternal());
        
        new Publication(infoPublication,publicationType,authors);
    }
    
}