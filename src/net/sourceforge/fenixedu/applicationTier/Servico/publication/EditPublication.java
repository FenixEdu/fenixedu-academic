package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationAuthor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

public class EditPublication extends Service {

    public void run(InfoPublication infoPublication) throws FenixServiceException, ExcepcaoPersistencia {
        final IPersistentPublication persistentPublication = persistentSupport.getIPersistentPublication();
        final IPersistentPublicationType persistentPublicationType = persistentSupport.getIPersistentPublicationType();

        final Publication publication = (Publication) persistentPublication.readByOID(
                Publication.class, infoPublication.getIdInternal());

        final PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());

        final List<InfoAuthor> infoAuthors = new ArrayList<InfoAuthor>();
        final List<InfoPublicationAuthor> infoPublicationAuthors = infoPublication
                .getInfoPublicationAuthors();
        for (InfoPublicationAuthor infoPublicationAuthor : infoPublicationAuthors) {
            infoAuthors.add(infoPublicationAuthor.getInfoAuthor());
        }

        // Call the InsertInexistentAuthors Service to insert the inexistent
        // authors as external persons
        final InsertInexistentAuthors ia = new InsertInexistentAuthors();
        final List<Person> authors = ia.run(infoAuthors);

        publication.edit(infoPublication, publicationType, authors);
    }

}