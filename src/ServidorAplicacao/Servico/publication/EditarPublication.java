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
import Dominio.publication.Publication;
import Dominio.publication.PublicationAuthor;
import Dominio.publication.PublicationType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPublicationAuthor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author Carlos Pereira & Francisco Passos
 */
public class EditarPublication implements IService {


    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia, FenixServiceException {

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