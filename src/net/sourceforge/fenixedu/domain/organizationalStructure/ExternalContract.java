package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.EMPLOYEE_CONTRACT);
	checkParameters(person, beginDate, endDate, institution, accountabilityType);
	setBeginDate(beginDate);
	setAccountabilityType(accountabilityType);
	setChildParty(person);
	setParentParty(institution);
    }

    private void checkParameters(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit,
	    AccountabilityType accountabilityType) {
	if (unit == null) {
	    throw new DomainException("error.contract.no.unit");
	}
	if (person == null) {
	    throw new DomainException("error.contract.no.employee");
	}
	if (beginDate == null) {
	    throw new DomainException("error.contract.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.contract.endDateBeforeBeginDate");
	}
	if (accountabilityType == null) {
	    throw new DomainException("error.contract.no.accountabilityType");
	}
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
	    String email, Unit institution) {

	if (!externalPersonsAlreadyExists(name, address, institution)) {
	    this.getPerson().edit(name, address, phone, mobile, homepage, email);
	    this.setParentParty(institution);
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

    /***********************************************************************
         * PRIVATE METHODS *
         **********************************************************************/

    private boolean externalPersonsAlreadyExists(String name, String address, Unit institution) {
	for (ExternalContract externalPerson : readAllExternalContracts()) {
	    if (externalPerson.hasPerson()) {
		Person person = externalPerson.getPerson();
		if (((person.getNome() != null && person.getNome().equalsIgnoreCase(name)) || ((person
			.getNome() == null || person.getNome().equals("")) && name.equals("")))
			&& ((person.getAddress() != null && person.getAddress()
				.equalsIgnoreCase(address)) || ((person.getAddress() == null || person
				.getAddress().equals("")) && address.equals("")))
			&& externalPerson.getInstitutionUnit().equals(institution)
			&& !externalPerson.equals(this))

		    return true;
	    }
	}
	return false;
    }

    /***********************************************************************
         * OTHER METHODS *
         **********************************************************************/

    public static List<ExternalContract> readAllExternalContracts() {
	List<ExternalContract> accountabilities = new ArrayList<ExternalContract>();
	for (Accountability accountability : RootDomainObject.getInstance().getAccountabilitys()) {
	    if (accountability instanceof ExternalContract) {
		accountabilities.add((ExternalContract) accountability);
	    }
	}
	return accountabilities;
    }

    public static List<ExternalContract> readByPersonName(String name) {
	final String nameToMatch = (name == null) ? null : name.replaceAll("%", ".*").toLowerCase();
	List<ExternalContract> allExternalPersons = new ArrayList<ExternalContract>();
	for (ExternalContract externalPerson : readAllExternalContracts()) {
	    if (externalPerson.hasPerson()
		    && externalPerson.getPerson().getName().toLowerCase().matches(nameToMatch)) {
		allExternalPersons.add(externalPerson);
	    }
	}
	return allExternalPersons;
    }

    public static ExternalContract readByPersonNameAddressAndInstitutionID(String name, String address,
	    Integer institutionID) {
	for (ExternalContract externalPerson : readAllExternalContracts()) {
	    if (externalPerson.hasPerson() && externalPerson.getPerson().getName().equals(name)
		    && externalPerson.getInstitutionUnit().equals(institutionID)
		    && externalPerson.getPerson().getAddress().equals(address)) {
		return externalPerson;
	    }
	}
	return null;
    }

    public static List<ExternalContract> readByIDs(Collection<Integer> accountabilityIDs) {
	List<ExternalContract> externalPersons = new ArrayList<ExternalContract>();
	if (accountabilityIDs == null || accountabilityIDs.isEmpty()) {
	    return externalPersons;
	}
	for (ExternalContract externalPerson : readAllExternalContracts()) {
	    if (accountabilityIDs.contains(externalPerson.getIdInternal())) {
		externalPersons.add(externalPerson);
	    }
	}
	return externalPersons;
    }

}
