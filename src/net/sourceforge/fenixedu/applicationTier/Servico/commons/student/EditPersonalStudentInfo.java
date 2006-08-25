package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditPersonalStudentInfo extends Service {

    public InfoPerson run(InfoPersonEditor newInfoPerson) throws ExcepcaoPersistencia, FenixServiceException {
        final Person person = (Person) rootDomainObject.readPartyByOID(newInfoPerson.getIdInternal());
        if (person == null) {
            throw new FenixServiceException("error.editPersonalStudentInfo.noPerson");
        }
        person.edit(newInfoPerson, Country.readCountryByNationality(newInfoPerson.getInfoPais().getNationality()));
        return InfoPerson.newInfoFromDomain(person);
    }
}
