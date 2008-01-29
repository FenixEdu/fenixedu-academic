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

    public static class ServiceArguments {
	private Unit unit;
	private String personName;
	
	public ServiceArguments(String personName, Unit unit) {
	    this.unit = unit;
	    this.personName = personName;
	}

	public String getPersonName() {
	    return personName;
	}

	public Unit getUnit() {
	    return unit;
	}
	
	
    }
    
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

    public ExternalContract run(ServiceArguments arguments)
            throws FenixServiceException {
        
	ExternalContract storedExternalContract = null;
        String personName = arguments.getPersonName();
	Unit organization = arguments.getUnit();
	storedExternalContract = ExternalContract.readByPersonNameAddressAndInstitutionID(personName,
                null, organization.getIdInternal());
        if (storedExternalContract != null)
            throw new ExistingServiceException(
                    "error.exception.commons.ExternalContract.existingExternalContract");

        Person externalPerson = Person.createExternalPerson(personName, Gender.MALE, null, null, null,
                null, null, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);
        return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }

    public ExternalContract run(String name, String organizationName, String phone, String mobile,
            String email) throws FenixServiceException {

        final Unit organization = Unit.createNewNoOfficialExternalInstitution(organizationName);
        Person externalPerson = Person.createExternalPerson(name, Gender.MALE, null, phone, mobile,
                null, email, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);

        return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }

    public ExternalContract run(String name, Integer institutionID, String phone, String mobile,
            String email) throws FenixServiceException {

        final Unit institutionLocation = (Unit) rootDomainObject.readPartyByOID(institutionID);
        Person externalPerson = Person.createExternalPerson(name, Gender.MALE, null, phone, mobile,
                null, email, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);

        return new ExternalContract(externalPerson, institutionLocation, new YearMonthDay(), null);
    }
}
