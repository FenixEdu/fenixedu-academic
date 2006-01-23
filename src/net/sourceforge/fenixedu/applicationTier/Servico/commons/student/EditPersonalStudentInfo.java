package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditPersonalStudentInfo extends Service {

    public InfoPerson run(InfoPerson newInfoPerson) throws ExcepcaoPersistencia, ExcepcaoInexistente {
        Person person = (Person) persistentObject.readByOID(Person.class, newInfoPerson.getIdInternal());

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        // Get new Country
        Country country = persistentSupport.getIPersistentCountry().readCountryByNationality(
                newInfoPerson.getInfoPais().getNationality());
        
        // Change personal Information
        person.edit(newInfoPerson, country);

        InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);

        return infoPerson;
    }
}