/**
 * Created on 09-Nov-2004
 *
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 * 
 */
package ServidorAplicacao.Servico.publication;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.publication.InfoPublication;
import Dominio.publication.IAuthor;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationAuthor;
import Dominio.publication.IPublicationType;
import Dominio.publication.PublicationAuthor;
import Dominio.publication.PublicationType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPublicationAuthor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a>& <a
 *         href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class InsertPublication implements IService {

    public void run(InfoPublication infoPublication) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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

        IPublication publication = InfoPublication.newDomainFromInfo(infoPublication);
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