/**
 * Created on 09-Nov-2004
 *
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a>& <a
 *         href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class InsertPublication implements IService {

    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        IPersistentPublicationAuthor persistentPublicationAuthor = sp.getIPersistentPublicationAuthor();
        IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

        //Write the authors to the AUTHOR table
        List infoAuthorsList = infoPublication.getInfoPublicationAuthors();

        //Call the InsertAuthors Service to insert in the DB the
        // PublicationAuthors
        final InsertPublicationAuthors ipa = new InsertPublicationAuthors();
        final List authors = ipa.insertAuthors(infoAuthorsList);

        //Write the Publication
        IPublication publication = new Publication();
        infoPublication.copyToDomain(infoPublication,publication);
        
        persistentPublication.simpleLockWrite(publication);

        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());
        publication.setType(publicationType);

        int i = 1;
        for (final Iterator iterator = authors.iterator(); iterator.hasNext(); i++) {
            final IAuthor author = (IAuthor) iterator.next();
            final IPublicationAuthor publicationAuthor = new PublicationAuthor();
            persistentPublicationAuthor.simpleLockWrite(publicationAuthor);

            publicationAuthor.setAuthor(author);
            author.getAuthorPublications().add(publicationAuthor);
            publicationAuthor.setPublication(publication);
            publication.getPublicationAuthors().add(publicationAuthor);

            publicationAuthor.setOrder(new Integer(i));
        }

    }

}