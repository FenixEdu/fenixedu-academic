package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditPersonalStudentInfo implements IService {

    public InfoPerson run(InfoPerson newInfoPerson) throws ExcepcaoPersistencia, ExcepcaoInexistente {

        ISuportePersistente sp = null;
        Person person = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        person = (Person) sp.getIPessoaPersistente().readByOID(Person.class,
                newInfoPerson.getIdInternal());

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        // Get new Country
        Country country = sp.getIPersistentCountry().readCountryByNationality(
                newInfoPerson.getInfoPais().getNationality());
        
        // Change personal Information
        person.edit(newInfoPerson, country);

        InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);

        return infoPerson;
    }
}