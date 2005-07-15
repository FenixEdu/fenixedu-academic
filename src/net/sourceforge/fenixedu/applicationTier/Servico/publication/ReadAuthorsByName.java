package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Ricardo G?es
 *
 */
public class ReadAuthorsByName implements IService {

    public List<InfoAuthor> run(IUserView userView, String stringtoSearch) throws ExcepcaoPersistencia {

        IPessoaPersistente persistentPerson = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPessoaPersistente();

        String names[] = stringtoSearch.split(" ");
        StringBuffer authorName = new StringBuffer("%");

        for (int i = 0; i <= names.length - 1; i++) {
            authorName.append(names[i]);
            authorName.append("%");
        }
        List<IPerson> authors = persistentPerson.readPersonsBySubName(authorName.toString());

        IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
        authors.remove(person);
        
        List<InfoAuthor> infoAuthors = new ArrayList(authors.size());

        for (IPerson author : authors) {
            InfoAuthor infoAuthor = new InfoAuthor();
            infoAuthor.copyFromDomain(author);
            infoAuthors.add(infoAuthor);
        }
        
        return infoAuthors;

    }
}