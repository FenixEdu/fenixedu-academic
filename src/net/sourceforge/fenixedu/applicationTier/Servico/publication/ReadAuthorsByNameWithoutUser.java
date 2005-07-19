package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonsByNameWithoutUser;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAuthorsByNameWithoutUser implements IService {

    public List<InfoAuthor> run(IUserView userView, String stringtoSearch) throws ExcepcaoPersistencia {

        List<InfoPerson> infoPersons = new ReadPersonsByNameWithoutUser().run(userView, stringtoSearch);
        
        List<InfoAuthor> infoAuthors = new ArrayList(infoPersons.size());
        
        for (InfoPerson infoPerson : infoPersons) {
            InfoAuthor infoAuthor = new InfoAuthor();
            infoAuthor.setAuthor(infoPerson.getNome());
            infoAuthor.setIdInternal(infoPerson.getIdInternal());
            infoAuthor.setInfoPessoa(infoPerson);
            
            infoAuthors.add(infoAuthor);
        }
        
        return infoAuthors;
        
    }
}