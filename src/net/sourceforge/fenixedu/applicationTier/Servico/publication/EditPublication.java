package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationAuthor;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditPublication implements IService {

    public void run(InfoPublication infoPublication) throws FenixServiceException {

        try {
            final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            final IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            final IPersistentPublicationType persistentPublicationType = sp
                    .getIPersistentPublicationType();

            final IPublication publication = (IPublication) persistentPublication.readByOID(
                    Publication.class, infoPublication.getIdInternal());

            final IPublicationType publicationType = (IPublicationType) persistentPublicationType
                    .readByOID(PublicationType.class, infoPublication.getInfoPublicationType()
                            .getIdInternal());

            
            final List<InfoAuthor> infoAuthors = new ArrayList<InfoAuthor>();
            final List<InfoPublicationAuthor> infoPublicationAuthors = infoPublication.getInfoPublicationAuthors();
            for (InfoPublicationAuthor infoPublicationAuthor : infoPublicationAuthors) {
                infoAuthors.add(infoPublicationAuthor.getInfoAuthor());
            }

            // Call the InsertInexistentAuthors Service to insert the inexistent
            //authors as external persons
            final InsertInexistentAuthors ia = new InsertInexistentAuthors();
            final List<IPerson> authors = ia.run(infoAuthors);

            
            
            publication.edit(infoPublication,publicationType,authors);
            
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}