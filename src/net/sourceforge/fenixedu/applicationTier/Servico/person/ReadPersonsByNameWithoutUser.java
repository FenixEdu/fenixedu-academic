package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPersonsByNameWithoutUser implements IService {

    public List<InfoPerson> run(IUserView userView, String stringtoSearch) throws ExcepcaoPersistencia {

        IPessoaPersistente persistentPerson = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPessoaPersistente();

        String names[] = stringtoSearch.split(" ");
        StringBuffer authorName = new StringBuffer("%");

        for (int i = 0; i <= names.length - 1; i++) {
            authorName.append(names[i]);
            authorName.append("%");
        }
        List<IPerson> persons = persistentPerson.readPersonsBySubName(authorName.toString());

        IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
        persons.remove(person);
        
        List<InfoPerson> infoPersons = new ArrayList(persons.size());

        for (IPerson individualPerson : persons) {
            infoPersons.add(InfoPerson.newInfoFromDomain(individualPerson));
        }
        
        return infoPersons;

    }
}