package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.publication.InfoAuthor;
import Dominio.IPessoa;
import Dominio.publication.IAuthor;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentAuthor;

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