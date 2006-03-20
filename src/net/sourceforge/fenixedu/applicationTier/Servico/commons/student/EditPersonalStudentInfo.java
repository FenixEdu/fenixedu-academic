package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditPersonalStudentInfo extends Service {

    public InfoPerson run(InfoPerson newInfoPerson) throws ExcepcaoPersistencia, FenixServiceException {
        
        final Person person = (Person) persistentObject.readByOID(Person.class, newInfoPerson.getIdInternal());
        if (person == null) {
            throw new FenixServiceException("error.noPerson");
        }
        person.edit(newInfoPerson, Country.readCountryByNationality(newInfoPerson.getInfoPais().getNationality()));
        return InfoPersonWithInfoCountry.newInfoFromDomain(person);
    }
}