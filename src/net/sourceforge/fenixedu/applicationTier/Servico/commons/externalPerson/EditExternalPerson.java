package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditExternalPerson extends Service {

    public void run(Integer externalPersonID, String name, String address, Integer institutionID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        ExternalContract storedExternalPerson = (ExternalContract) rootDomainObject.readAccountabilityByOID(externalPersonID);
        if (storedExternalPerson == null) {
            throw new NonExistingServiceException("error.exception.externalPerson.nonExistingExternalPsrson");
        }
      
        Unit storedInstitution = (Unit) rootDomainObject.readPartyByOID(institutionID);
        storedExternalPerson.edit(name, address, phone, mobile, homepage, email, storedInstitution);
    }
    
}
