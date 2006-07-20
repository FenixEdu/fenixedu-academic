package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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

        return new ExternalPerson(name, Gender.valueOf(sex), address, null, null, null, null, null,
                null, phone, mobile, homepage, email, String.valueOf(System.currentTimeMillis()),
                institutionLocation);
    }

    
    /**
     * This method will always create an ExternalPerson and an Unit
     * @param personName - Name of the ExternalPerson to be created
     * @param organizationName - Name of the Unit to be created and associated with the ExternalPerson
     * @return the newly created ExternalPerson
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException 
     */
    public ExternalPerson run(String personName, String organizationName) {
        final Unit organization = Unit.createNewExternalInstitution(organizationName);
        return new ExternalPerson(personName, Gender.MALE, null, null, null, null, null, null, null,
                null, null, null, null, String.valueOf(System.currentTimeMillis()), organization);
    }
    
    
  
    /**
     * This method will create an ExternalPerson and associate an existing Unit to it
     * @param personName - Name of the ExternalPerson to be created
     * @param organization - The unit that the newly created will be associated with
     * @return the newly created ExternalPerson
     * @throws FenixServiceException If there is already an externalPerson with the same name in the same unit
     * @throws ExcepcaoPersistencia
     */
    public ExternalPerson run(String personName, Unit organization) throws FenixServiceException {

        ExternalPerson storedExternalPerson = null;

        storedExternalPerson = ExternalPerson.readByNameAndAddressAndInstitutionID(personName,
                null, organization.getIdInternal());   
        
        if (storedExternalPerson != null)
            throw new ExistingServiceException("error.exception.commons.externalPerson.existingExternalPerson");

        return new ExternalPerson(personName, Gender.MALE, null, null, null, null, null, null, null,
                null, null, null, null, String.valueOf(System.currentTimeMillis()), organization);
    }
}
