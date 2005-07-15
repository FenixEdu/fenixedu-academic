package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
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

public class InsertPublication implements IService {

    public void run(InfoPublication infoPublication) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentAuthorship persistentAuthorship = sp.getIPersistentAuthorship();
            IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

            // Write the authors to the AUTHOR table
            List infoAuthorsList = infoPublication.getInfoPublicationAuthors();

            // Call the InsertAuthors Service to insert in the DB the
            // PublicationAuthors
            final InsertInexistentAuthors iia = new InsertInexistentAuthors();
            final List authors = iia.run(infoAuthorsList);

            // Write the Publication
            IPublication publication = new Publication();
            infoPublication.copyToDomain(infoPublication, publication);

            persistentPublication.simpleLockWrite(publication);

            IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());
            publication.setType(publicationType);

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