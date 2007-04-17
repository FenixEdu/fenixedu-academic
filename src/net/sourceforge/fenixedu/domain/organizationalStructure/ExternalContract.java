package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.PersonName;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class ExternalContract extends ExternalContract_Base {

    private ExternalContract() {
	super();
    }

    /***********************************************************************
         * BUSINESS SERVICES *
         **********************************************************************/

    public ExternalContract(Person person, Unit institution, YearMonthDay beginDate, YearMonthDay endDate) {
	this();
	
	checkIfPersonAlreadyIsExternalPerson(person);
	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.EMPLOYEE_CONTRACT);

	setAccountabilityType(accountabilityType);
	setChildParty(person);
	setParentParty(institution);
	setOccupationInterval(beginDate, endDate);
    
        PersonName personName = person.getPersonName();
        if (personName != null) {
            personName.setIsExternalPerson(true);
        }
    }  

    private void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.contract.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.contract.endDateBeforeBeginDate");
	}
	setBeginDate(beginDate);
	setEndDate(endDate);
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
	    String email, Unit institution) {

	if (!externalPersonsAlreadyExists(name, address, institution)) {
	    getPerson().edit(name, address, phone, mobile, homepage, email);
	    setParentParty(institution);
	} else {
	    throw new DomainException("error.exception.externalPerson.existingExternalPerson");
	}
    }

    public void delete() {
	Person person = getPerson();
	super.delete();
	person.delete();
    }

    public Person getPerson() {
	return (Person) getChildParty();
    }

    public Unit getInstitutionUnit() {
	return (Unit) getParentParty();
    }

    public boolean hasPerson() {
	return getChildParty() != null;
    }

    public boolean hasInstitutionUnit() {
	return getParentParty() != null;
    }

    private void checkIfPersonAlreadyIsExternalPerson(Person person) {
	if(person.hasExternalPerson()) {
	    throw new DomainException("error.externalContract.person.already.is.externalPerson");
	}	
    }
    
    private boolean externalPersonsAlreadyExists(final String name, final String address, final Unit institution) {
	
	for (final Accountability accountability : RootDomainObject.getInstance().getAccountabilitys()) {
	    if(accountability instanceof ExternalContract) {
		final ExternalContract externalPerson = (ExternalContract) accountability;
		if (externalPerson.hasPerson()) {
		    final Person person = externalPerson.getPerson();
		    
		    if (isNameCorrect(person, name) && isAddressFieldCorrect(person, address)
			    && externalPerson.getInstitutionUnit() == institution
			    && externalPerson != this) {
			return true;
		    }
		}
	    }
	}
	return false;
    }
    
    private boolean isNameCorrect(final Person person, final String name) {
	return (person.getName() != null && person.getName().equalsIgnoreCase(name))
		|| (StringUtils.isEmpty(person.getName()) && name.length() == 0);
    }
    
    private boolean isAddressFieldCorrect(final Person person, final String address) {
	final String personAddress = person.getAddress();
	return (personAddress != null && personAddress.equalsIgnoreCase(address))
		|| (StringUtils.isEmpty(personAddress) && address.length() == 0);
    }

    public static List<ExternalContract> readByPersonName(String name) {
	List<ExternalContract> allExternalPersons = new ArrayList<ExternalContract>();
	final String nameToMatch = (name == null) ? null : name.replaceAll("%", ".*").toLowerCase();	
	for (Accountability accountability : RootDomainObject.getInstance().getAccountabilitys()) {
	    if(accountability instanceof ExternalContract) {
		ExternalContract externalPerson = (ExternalContract) accountability;
		if (externalPerson.hasPerson() && externalPerson.getPerson().getName().toLowerCase().matches(nameToMatch)) {
		    allExternalPersons.add(externalPerson);	
		}
	    }
	}	
	return allExternalPersons;
    }

    public static ExternalContract readByPersonNameAddressAndInstitutionID(final String name, final String address, final Integer institutionID) {
	for (Accountability accountability : RootDomainObject.getInstance().getAccountabilitys()) {
	    if(accountability instanceof ExternalContract) {
		ExternalContract externalPerson = (ExternalContract) accountability;
		if (externalPerson.hasPerson() && externalPerson.getPerson().getName().equals(name)
			&& externalPerson.getInstitutionUnit().getIdInternal().equals(institutionID)
			&& externalPerson.getPerson().hasDefaultPhysicalAddress()
			&& externalPerson.getPerson().getDefaultPhysicalAddress().getAddress().equals(address)) {
		    return externalPerson;
		}
	    }	    
	}
	return null;
    }

    public static List<ExternalContract> readByIDs(Collection<Integer> accountabilityIDs) {
	List<ExternalContract> externalPersons = new ArrayList<ExternalContract>();
	if (accountabilityIDs == null || accountabilityIDs.isEmpty()) {
	    return externalPersons;
	}
	for (Accountability accountability : RootDomainObject.getInstance().getAccountabilitys()) {
	    if(accountability instanceof ExternalContract) {
		ExternalContract externalPerson = (ExternalContract) accountability;	
		if (accountabilityIDs.contains(externalPerson.getIdInternal())) {
		    externalPersons.add(externalPerson);
		}
	    }
	}
	return externalPersons;
    }

}
