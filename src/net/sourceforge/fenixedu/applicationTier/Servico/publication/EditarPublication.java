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
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Carlos Pereira & Francisco Passos
 */
public class EditarPublication implements IService {


    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        final IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        final IPersistentPublicationAuthor persistentPublicationAuthor = sp.getIPersistentPublicationAuthor();
        final IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

        final IPublication  publication = (IPublication) persistentPublication.readByOID(
                    Publication.class, infoPublication.getIdInternal());

        final IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());

        persistentPublication.simpleLockWrite(publication);

        infoPublication.copyToDomain(infoPublication, publication);
        publication.setType(publicationType);

        final List infoAuthors = infoPublication.getInfoPublicationAuthors();
        
        //Call the DeleteAuthors Service to delete in the DB the
        // PublicationAuthors
        final DeletePublicationAuthors dpa = new DeletePublicationAuthors();
        dpa.run(infoPublication.getIdInternal());

        //Call the InsertAuthors Service to insert in the DB the
        // PublicationAuthors
        final InsertPublicationAuthors ipa = new InsertPublicationAuthors();
        final List authors = ipa.insertAuthors(infoAuthors);

        int i=1;
        for (final Iterator iterator = authors.iterator(); iterator.hasNext(); i++) {
            final IAuthor author = (IAuthor) iterator.next();
            final IPublicationAuthor publicationAuthor = new PublicationAuthor();
            
            publicationAuthor.setAuthor(author);
            author.getAuthorPublications().add(publicationAuthor);
            publicationAuthor.setPublication(publication);
            publication.getPublicationAuthors().add(publicationAuthor);

            publicationAuthor.setOrder(new Integer(i));
            persistentPublicationAuthor.simpleLockWrite(publicationAuthor);
        }

//        publication.setAuthors(InfoPublication.copyAuthorsFromInfo(infoPublication));
//        
//        //Write the authors to the AUTHOR table
//        List infoAuthorsList = infoPublication.getInfoPublicationAuthors();



        
        
//            //Write the authors to the AUTHOR table
//            List infoAuthorsList = infoPublication.getInfoPublicationAuthors();
//            //Call the DeleteAuthors Service to delete in the DB the
//            // PublicationAuthors
//            DeletePublicationAuthors dpa = new DeletePublicationAuthors();
//            dpa.run(infoPublication.getIdInternal());
//
//            //Call the InsertAuthors Service to insert in the DB the
//            // PublicationAuthors
//            InsertPublicationAuthors ipa = new InsertPublicationAuthors();
//            ipa.run(infoAuthorsList);
//
//            //Write the Publication
//            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//            IPersistentPublication persistentPublication = sp.getIPersistentPublication();
//
//            IPublication publication;
//            try {
//                publication = (IPublication) persistentPublication.readByOID(
//                        Publication.class, infoPublication.getIdInternal());
//            } catch (ExcepcaoPersistencia e) {
//                throw new ExcepcaoInexistente(e);
//            }
//
//            persistentPublication.lockWrite(publication);
//            
//            infoPublication.copyToDomain(infoPublication, publication);
//            publication.setAuthors(InfoPublication.copyAuthorsFromInfo(infoPublication));
    }

}