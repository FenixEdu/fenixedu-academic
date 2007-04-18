package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

import org.joda.time.YearMonthDay;

public class InsertExternalPerson extends Service {

    public ExternalContract run(String name, String sex, String address, Integer institutionID,
	    String phone, String mobile, String homepage, String email) throws FenixServiceException {

	final ExternalContract storedExternalContract = ExternalContract
		.readByPersonNameAddressAndInstitutionID(name, address, institutionID);
	if (storedExternalContract != null)
	    throw new ExistingServiceException(
		    "error.exception.commons.ExternalContract.existingExternalContract");

	final Unit institutionLocation = (Unit) rootDomainObject.readPartyByOID(institutionID);
	Person externalPerson = Person.createExternalPerson(name, Gender.valueOf(sex),
		new PhysicalAddressData().setAddress(address), phone, mobile, homepage, email, String
			.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);

	return new ExternalContract(externalPerson, institutionLocation, new YearMonthDay(), null);
    }

    public ExternalContract run(String personName, String organizationName) {
	final Unit organization = Unit.createNewNoOfficialExternalInstitution(organizationName);
	Person externalPerson = Person.createExternalPerson(personName, Gender.MALE, null, null, null,
		null, null, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);

	return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }

    public ExternalContract run(String personName, String organizationName, Country country) {
        final Unit organization = Unit.createNewNoOfficialExternalInstitution(organizationName, country);
        Person externalPerson = Person.createExternalPerson(personName, Gender.MALE, null, null, null,
                null, null, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);

        return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }
    
    public ExternalContract run(String personName, Unit organization) throws FenixServiceException {
	return run(personName, null, organization);
    }

    public ExternalContract run(String personName, String email, Unit organization)
	    throws FenixServiceException {
	ExternalContract storedExternalContract = null;
	storedExternalContract = ExternalContract.readByPersonNameAddressAndInstitutionID(personName,
		null, organization.getIdInternal());
	if (storedExternalContract != null)
	    throw new ExistingServiceException(
		    "error.exception.commons.ExternalContract.existingExternalContract");

	Person externalPerson = Person.createExternalPerson(personName, Gender.MALE, null, null, null,
		null, email, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);
	return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }
}
