package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class EditExternalPerson {

    @Service
    public static void run(String externalPersonID, String name, String address, String institutionID, String phone,
            String mobile, String homepage, String email) throws FenixServiceException, DomainException {

        ExternalContract storedExternalPerson = (ExternalContract) FenixFramework.getDomainObject(externalPersonID);
        if (storedExternalPerson == null) {
            throw new NonExistingServiceException("error.exception.externalPerson.nonExistingExternalPsrson");
        }

        Unit storedInstitution = (Unit) FenixFramework.getDomainObject(institutionID);
        storedExternalPerson.edit(name, address, phone, mobile, homepage, email, storedInstitution);
    }

}