/**
 * Created on 09-Nov-2004
 *
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * *
 * 
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a>& <a
 *         href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class InsertPublicationAuthors implements IService {

    /**
     * This method inserts a list of authors in the Database. For that it checks
     * if the authors already exists or not.
     * 
     * @param infoAuthorsList
     *            the list of InfoAuthors that will be converted to authors and
     *            inserted in the DB.
     * @throws ExcepcaoPersistencia
     */
    public void run(final List infoAuthorsList) throws ExcepcaoPersistencia {
        insertAuthors(infoAuthorsList);
    }

    public List insertAuthors(final List infoAuthorsList) throws ExcepcaoPersistencia {
        final List authors = new ArrayList(infoAuthorsList.size());
        for (final Iterator iterator = infoAuthorsList.iterator(); iterator.hasNext();) {
            final InfoAuthor infoAuthor = (InfoAuthor) iterator.next();
            final IAuthor author = insertAuthor(infoAuthor);
            authors.add(author);
        }
        return authors;
    }

    // Method that is responsible for inserting a single author in the DB
    private IAuthor insertAuthor(final InfoAuthor infoAuthor) throws ExcepcaoPersistencia {
        IAuthor author = null;
        ISuportePersistente sp = null;
        IPersistentAuthor persistentAuthor = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentAuthor = sp.getIPersistentAuthor();
        // We have to be aware if the author is a known person or not,
        // because if he isn't then, the keyperson fiel will be NULL
        if (infoAuthor.getKeyPerson() == null) {
            // The author is an external person
            List authorsList = persistentAuthor.readAuthorsBySubName(infoAuthor.getAuthor());
            Iterator it1 = authorsList.iterator();
            while (it1.hasNext()) {
                IAuthor tempAuthor = (Author) it1.next();
                // Where we'll consider that the set (Author,Organization) is
                // unique
                // althought it isn't that way in the database
                if (identicalAuthors(tempAuthor, infoAuthor)) {
                    author = tempAuthor;
                    break;
                }
            }
        } else {
            // The author is an internal person
            author = persistentAuthor.readAuthorByKeyPerson(infoAuthor.getKeyPerson());
        }

        if (author == null) {
            author = InfoAuthor.newDomainFromInfo(infoAuthor);
            persistentAuthor.lockWrite(author);
        }

        return author;
    }

    // Method that checks if an IAuthor and an InfoAuthor are identical, that
    // is,
    // have the same name and organization
    private boolean identicalAuthors(IAuthor author, InfoAuthor infoAuthor) {
        if (author.getOrganization() != null && infoAuthor.getOrganization() != null) {
            if (author.getAuthor().equals(infoAuthor.getAuthor())
                    && author.getOrganization().equals(infoAuthor.getOrganization()))
                return true;
            return false;
        }
        if (author.getOrganization() == null && infoAuthor.getOrganization() == null)
            if (author.getAuthor().equals(infoAuthor.getAuthor()))
                return true;
        return false;
    }

}