package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertExternalPerson extends Service {

    public ExternalPerson run(String name, String sex, String address, Integer institutionID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia {
        ExternalPerson storedExternalPerson = persistentSupport.getIPersistentExternalPerson()
                .readByNameAndAddressAndInstitutionID(name, address, institutionID);

        if (storedExternalPerson != null)
            throw new ExistingServiceException(
                    "error.exception.commons.externalPerson.existingExternalPerson");

        Institution institutionLocation = (Institution) persistentObject.readByOID(
                Institution.class, institutionID);

        // generate new identification number
        String lastDocumentIdNumber = persistentSupport.getIPersistentExternalPerson().readLastDocumentIdNumber();
        int nextID = Integer.parseInt(lastDocumentIdNumber) + 1;
        String documentIdNumber = String.valueOf(nextID);

        return DomainFactory.makeExternalPerson(name, Gender.valueOf(sex), address, phone, mobile,
                homepage, email, documentIdNumber, institutionLocation);
    }

}
