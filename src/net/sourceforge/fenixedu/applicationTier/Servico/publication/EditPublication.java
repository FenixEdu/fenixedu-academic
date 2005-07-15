package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationAuthor;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditPublication implements IService {

    public void run(InfoPublication infoPublication) throws FenixServiceException {

        try {
            final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            final IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            final IPersistentAuthorship persistentAuthorship = sp.getIPersistentAuthorship();
            final IPersistentPublicationType persistentPublicationType = sp
                    .getIPersistentPublicationType();

            final IPublication publication = (IPublication) persistentPublication.readByOID(
                    Publication.class, infoPublication.getIdInternal());

            final IPublicationType publicationType = (IPublicationType) persistentPublicationType
                    .readByOID(PublicationType.class, infoPublication.getInfoPublicationType()
                            .getIdInternal());

            persistentPublication.simpleLockWrite(publication);

            infoPublication.copyToDomain(infoPublication, publication);
            publication.setType(publicationType);

            final List<InfoAuthor> infoAuthors = new ArrayList<InfoAuthor>();
            final List<InfoPublicationAuthor> infoPublicationAuthors = infoPublication.getInfoPublicationAuthors();
            for (InfoPublicationAuthor infoPublicationAuthor : infoPublicationAuthors) {
                infoAuthors.add(infoPublicationAuthor.getInfoAuthor());
            }

            // Call the InsertInexistentAuthors Service to insert the inexistent
            //authors as external persons
            final InsertInexistentAuthors ia = new InsertInexistentAuthors();
            final List<IPerson> authors = ia.run(infoAuthors);

            // Call the DeleteAuthorships Service to delete in the DB the
            // Authorships
            final DeleteAuthorships da = new DeleteAuthorships();
            da.run(infoPublication.getIdInternal());

            // Create the authorships
            int i = 1;
            for (final Iterator iterator = authors.iterator(); iterator.hasNext(); i++) {
                final IPerson author = (IPerson) iterator.next();
                final IAuthorship authorship = new Authorship();
                persistentAuthorship.simpleLockWrite(authorship);

                authorship.setAuthor(author);
                authorship.setPublication(publication);
                authorship.setOrder(new Integer(i));
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}