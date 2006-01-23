package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditExternalPerson extends Service {

    public void run(Integer externalPersonID, String name, String address, Integer institutionID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        ExternalPerson storedExternalPerson = (ExternalPerson) persistentObject.readByOID(ExternalPerson.class, externalPersonID);

        if (storedExternalPerson == null)
            throw new NonExistingServiceException(
                    "error.exception.externalPerson.nonExistingExternalPsrson");

        List<ExternalPerson> allExternalPersons = (List<ExternalPerson>) persistentSupport
                .getIPersistentExternalPerson().readAll(ExternalPerson.class);

        Institution storedInstitution = (Institution) persistentObject.readByOID(
                Institution.class, institutionID);

        storedExternalPerson.edit(name, address, phone, mobile, homepage, email, storedInstitution,
                allExternalPersons);
    }
}