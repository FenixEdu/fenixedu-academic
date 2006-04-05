package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertExternalPerson extends Service {

    public ExternalPerson run(String name, String sex, String address, Integer institutionID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia {

        ExternalPerson storedExternalPerson = ExternalPerson.readByNameAndAddressAndInstitutionID(name,
                address, institutionID);

        if (storedExternalPerson != null)
            throw new ExistingServiceException(
                    "error.exception.commons.externalPerson.existingExternalPerson");

        Unit institutionLocation = (Unit) rootDomainObject.readPartyByOID(institutionID);

        return DomainFactory.makeExternalPerson(name, Gender.valueOf(sex), address, phone, mobile,
                homepage, email, String.valueOf(System.currentTimeMillis()), institutionLocation);
    }

}
