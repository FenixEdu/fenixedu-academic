package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountryAndAdvisories;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPersonByUsername extends Service {

    public InfoPerson run(String username) throws ExcepcaoInexistente, ExcepcaoPersistencia {
        Person person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername(username);

        if (person == null)
            throw new ExcepcaoInexistente("Unknown Person !!");

        return InfoPersonWithInfoCountryAndAdvisories.newInfoFromDomain(person);
    }
}