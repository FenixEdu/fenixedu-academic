package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Ricardo G?es
 *
 */
public class ReadAuthorsByName implements IService {

    public List run(String stringtoSearch) throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentAuthor persistentAuthor = SuportePersistenteOJB.getInstance().getIPersistentAuthor();

        String names[] = stringtoSearch.split(" ");
        StringBuffer authorName = new StringBuffer("%");

        for (int i = 0; i <= names.length - 1; i++) {
            authorName.append(names[i]);
            authorName.append("%");
        }
        List authors = persistentAuthor.readAuthorsBySubName(authorName.toString());

        List infoAuthors = new ArrayList(authors.size());

        for (Iterator iter = authors.iterator(); iter.hasNext();) {
            IAuthor author = (IAuthor) iter.next();
            InfoAuthor infoAuthor = new InfoAuthor();
            infoAuthor.copyFromDomain(author);
            infoAuthors.add(infoAuthor);
        }

        return infoAuthors;

    }
}