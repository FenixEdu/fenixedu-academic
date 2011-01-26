package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.NationalityTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.BirthdayMismatchException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.MultiplePersonFoundByDocumentIdException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonNotFoundException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonSearchByNameMismatchException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PossiblePersonCandidatesException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.SocialSecurityNumberMismatchException;
import net.sourceforge.fenixedu.util.StringFormatter;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class PhdMigrationIndividualPersonalData extends PhdMigrationIndividualPersonalData_Base {

    private transient Integer phdStudentNumber;
    private transient String identificationNumber;
    private transient String socialSecurityNumber;
    private transient String fullName;
    private transient String familyName;
    private transient LocalDate dateOfBirth;
    private transient Gender gender;
    private transient Country nationality;

    // Address
    private transient String parishOfResidence;
    private transient String districtSubdivisionOfResidence;
    private transient String districtOfResidence;

    private transient String fatherName;
    private transient String motherName;

    private transient String address;
    private transient String areaCode;
    private transient String area;
    private transient String areaOfAreaCode;

    // -- Address

    private transient String contactNumber;
    private transient String otherContactNumber;
    private transient String profession;
    private transient String workPlace;
    private transient String email;

    private PhdMigrationIndividualPersonalData() {
	super();
    }

    protected PhdMigrationIndividualPersonalData(String data) {
	setData(data);
    }

    public void parseAndSetNumber() {
	parse();

	setNumber(phdStudentNumber);
    }

    private String parseSocialSecurityNumber(String socialSecurityNumber) {
	if (socialSecurityNumber.matches("/--+|0+/")) {
	    return null;
	}

	return socialSecurityNumber;
    }

    public void parse() {
	String[] fields = getData().split("\t");

	try {
	    try {
		phdStudentNumber = Integer.valueOf(fields[0].trim());
	    } catch (NumberFormatException e) {
		throw new IncompleteFieldsException("processNumber");
	    }
	    identificationNumber = fields[1].trim();
	    socialSecurityNumber = parseSocialSecurityNumber(fields[2].trim());
	    fullName = StringFormatter.prettyPrint(fields[3].trim());
	    familyName = StringFormatter.prettyPrint(fields[4].trim());
	    dateOfBirth = ConversionUtilities.parseDate(fields[5].trim());
	    gender = ConversionUtilities.parseGender(fields[6].trim());
	    nationality = NationalityTranslator.translate(fields[7].trim());

	    // Address
	    parishOfResidence = fields[8].trim();
	    districtSubdivisionOfResidence = fields[9].trim();
	    districtOfResidence = fields[10].trim();

	    fatherName = fields[11].trim();
	    motherName = fields[12].trim();

	    address = fields[13].trim();
	    areaCode = fields[14].trim();
	    area = fields[15].trim();
	    areaOfAreaCode = area;

	    // -- Address

	    contactNumber = fields[16].trim();
	    otherContactNumber = fields[17].trim();
	    profession = fields[18].trim();
	    workPlace = fields[19].trim();
	    email = fields[20].trim();

	} catch (NoSuchElementException e) {
	    throw new IncompleteFieldsException("Not enough fields");
	}
    }

    public Person getPerson() {
	// Get by identification number
	final Collection<Person> personSet = Person.readByDocumentIdNumber(identificationNumber);
	final Collection<Person> personNamesSet = Person.readPersonsByName(StringNormalizer.normalize(fullName));

	if (personSet.isEmpty() && personNamesSet.isEmpty()) {
	    throw new PersonNotFoundException();
	}

	if (personSet.size() > 1) {
	    throw new MultiplePersonFoundByDocumentIdException(identificationNumber);
	}

	if (personSet.isEmpty()) {
	    checkPossibleCandidates(personNamesSet);
	}

	checkPersonByIdDocument(personSet, personNamesSet);

	Person person = personSet.iterator().next();
	if (!StringUtils.isEmpty(socialSecurityNumber) && !StringUtils.isEmpty(person.getSocialSecurityNumber())
		&& socialSecurityNumber.equals(person.getSocialSecurityNumber())) {
	    return person;
	}

	if (!StringUtils.isEmpty(socialSecurityNumber) && !StringUtils.isEmpty(person.getSocialSecurityNumber())) {
	    throw new SocialSecurityNumberMismatchException("Original: " + socialSecurityNumber + " Differs from: "
		    + person.getSocialSecurityNumber());
	}

	if (!person.getDateOfBirthYearMonthDay().isEqual(dateOfBirth)) {
	    throw new BirthdayMismatchException("Original: " + dateOfBirth + " Differs from: "
		    + person.getDateOfBirthYearMonthDay());
	}

	return person;
    }

    private Person checkPersonByIdDocument(final Collection<Person> personSet, final Collection<Person> personNamesSet) {
	Person possiblePerson = personSet.iterator().next();

	for (Person person : personNamesSet) {
	    if (person == possiblePerson) {
		return possiblePerson;
	    }
	}

	throw new PersonSearchByNameMismatchException(new HashSet<Person>(personNamesSet));
    }

    private void checkPossibleCandidates(final Collection<Person> personNamesSet) {
	Set<Person> possiblePersonSet = new HashSet<Person>();
	for (Person person : personNamesSet) {

	    if (!person.getSocialSecurityNumber().equals(socialSecurityNumber)) {
		continue;
	    }

	    if (!person.getDateOfBirthYearMonthDay().isEqual(dateOfBirth)) {
		continue;
	    }

	    possiblePersonSet.add(person);
	}

	if (!possiblePersonSet.isEmpty()) {
	    throw new PossiblePersonCandidatesException(possiblePersonSet);
	}

	throw new PersonNotFoundException();
    }

    public boolean isPersonRegisteredOnFenix() {
	try {
	    return getPerson() != null;
	} catch (PersonNotFoundException e) {
	    return false;
	}
    }

    public boolean isSocialSecurityNumberEqual() {
	return getPerson().getSocialSecurityNumber().equals(socialSecurityNumber);
    }

    private static String readGivenName(String fullName, String familyName) {
	return fullName.substring(0, fullName.indexOf(familyName)).trim();
    }

    public PersonBean getPersonBean() {
	PersonBean bean = new PersonBean();

	if (isPersonRegisteredOnFenix()) {
	    bean.setPerson(getPerson());
	    return bean;
	}

	bean.setAddress(address);
	bean.setArea(area);
	bean.setAreaCode(areaCode);
	bean.setParishOfResidence(parishOfResidence);
	bean.setDistrictOfResidence(districtOfResidence);
	bean.setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);

	bean.setPhone(contactNumber);
	bean.setWorkPhone(otherContactNumber);
	bean.setProfession(profession);
	bean.setEmail(email);

	bean.setFatherName(fatherName);
	bean.setMotherName(motherName);
	bean.setIdDocumentType(IDDocumentType.OTHER);
	bean.setDocumentIdNumber(identificationNumber);
	bean.setSocialSecurityNumber(socialSecurityNumber);

	bean.setGivenNames(readGivenName(fullName, familyName));
	bean.setName(fullName);
	bean.setFamilyNames(familyName);

	bean.setDateOfBirth(new YearMonthDay(dateOfBirth.getYear(), dateOfBirth.getMonthOfYear(), dateOfBirth.getDayOfMonth()));
	bean.setGender(gender);
	bean.setNationality(nationality);

	return bean;
    }

}
