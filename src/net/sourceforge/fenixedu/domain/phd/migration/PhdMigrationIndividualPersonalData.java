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

    private transient PhdMigrationIndividualPersonalDataBean personalBean;

    private PhdMigrationIndividualPersonalData() {
	super();
    }

    protected PhdMigrationIndividualPersonalData(String data) {
	setData(data);
    }

    public class PhdMigrationIndividualPersonalDataBean {
	private transient String data;

	private transient Integer phdStudentNumber;
	private transient String identificationNumber;
	private transient String socialSecurityNumber;
	private transient String fullName;
	private transient String familyName;
	private transient LocalDate dateOfBirth;
	private transient Gender gender;
	private transient Country nationality;

	private transient String parishOfResidence;
	private transient String districtSubdivisionOfResidence;
	private transient String districtOfResidence;

	private transient String fatherName;
	private transient String motherName;

	private transient String address;
	private transient String areaCode;
	private transient String area;
	private transient String areaOfAreaCode;

	private transient String contactNumber;
	private transient String otherContactNumber;
	private transient String profession;
	private transient String workPlace;
	private transient String email;

	public PhdMigrationIndividualPersonalDataBean(String data) {
	    setData(data);
	    parse();
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

	public String getData() {
	    return data;
	}

	public void setData(String data) {
	    this.data = data;
	}

	public Integer getPhdStudentNumber() {
	    return phdStudentNumber;
	}

	public void setPhdStudentNumber(Integer phdStudentNumber) {
	    this.phdStudentNumber = phdStudentNumber;
	}

	public String getIdentificationNumber() {
	    return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
	    this.identificationNumber = identificationNumber;
	}

	public String getSocialSecurityNumber() {
	    return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
	    this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getFullName() {
	    return fullName;
	}

	public void setFullName(String fullName) {
	    this.fullName = fullName;
	}

	public String getFamilyName() {
	    return familyName;
	}

	public void setFamilyName(String familyName) {
	    this.familyName = familyName;
	}

	public LocalDate getDateOfBirth() {
	    return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
	    this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
	    return gender;
	}

	public void setGender(Gender gender) {
	    this.gender = gender;
	}

	public Country getNationality() {
	    return nationality;
	}

	public void setNationality(Country nationality) {
	    this.nationality = nationality;
	}

	public String getParishOfResidence() {
	    return parishOfResidence;
	}

	public void setParishOfResidence(String parishOfResidence) {
	    this.parishOfResidence = parishOfResidence;
	}

	public String getDistrictSubdivisionOfResidence() {
	    return districtSubdivisionOfResidence;
	}

	public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	    this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
	}

	public String getDistrictOfResidence() {
	    return districtOfResidence;
	}

	public void setDistrictOfResidence(String districtOfResidence) {
	    this.districtOfResidence = districtOfResidence;
	}

	public String getFatherName() {
	    return fatherName;
	}

	public void setFatherName(String fatherName) {
	    this.fatherName = fatherName;
	}

	public String getMotherName() {
	    return motherName;
	}

	public void setMotherName(String motherName) {
	    this.motherName = motherName;
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public String getAreaCode() {
	    return areaCode;
	}

	public void setAreaCode(String areaCode) {
	    this.areaCode = areaCode;
	}

	public String getArea() {
	    return area;
	}

	public void setArea(String area) {
	    this.area = area;
	}

	public String getAreaOfAreaCode() {
	    return areaOfAreaCode;
	}

	public void setAreaOfAreaCode(String areaOfAreaCode) {
	    this.areaOfAreaCode = areaOfAreaCode;
	}

	public String getContactNumber() {
	    return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
	    this.contactNumber = contactNumber;
	}

	public String getOtherContactNumber() {
	    return otherContactNumber;
	}

	public void setOtherContactNumber(String otherContactNumber) {
	    this.otherContactNumber = otherContactNumber;
	}

	public String getProfession() {
	    return profession;
	}

	public void setProfession(String profession) {
	    this.profession = profession;
	}

	public String getWorkPlace() {
	    return workPlace;
	}

	public void setWorkPlace(String workPlace) {
	    this.workPlace = workPlace;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

    }

    public boolean hasPersonalBean() {
	return personalBean != null;
    }

    public PhdMigrationIndividualPersonalDataBean getPersonalBean() {
	if (hasPersonalBean()) {
	    return personalBean;
	}

	personalBean = new PhdMigrationIndividualPersonalDataBean(getData());
	return personalBean;
    }

    public void parse() {
	getPersonalBean();
    }

    public void parseAndSetNumber() {
	final PhdMigrationIndividualPersonalDataBean personalBean = getPersonalBean();
	setNumber(personalBean.getPhdStudentNumber());
    }

    private String parseSocialSecurityNumber(String socialSecurityNumber) {
	if (socialSecurityNumber.matches("/--+|0+/")) {
	    return null;
	}

	return socialSecurityNumber;
    }

    public Person getPerson() {
	// Get by identification number
	final Collection<Person> personSet = Person.readByDocumentIdNumber(getPersonalBean().getIdentificationNumber());
	final Collection<Person> personNamesSet = Person.readPersonsByName(StringNormalizer.normalize(getPersonalBean()
		.getFullName()));

	if (personSet.isEmpty() && personNamesSet.isEmpty()) {
	    throw new PersonNotFoundException();
	}

	if (personSet.size() > 1) {
	    throw new MultiplePersonFoundByDocumentIdException(getPersonalBean().getIdentificationNumber());
	}

	if (personSet.isEmpty()) {
	    checkPossibleCandidates(personNamesSet);
	}

	checkPersonByIdDocument(personSet, personNamesSet);

	Person person = personSet.iterator().next();
	if (!StringUtils.isEmpty(getPersonalBean().getSocialSecurityNumber())
		&& !StringUtils.isEmpty(person.getSocialSecurityNumber())
		&& getPersonalBean().getSocialSecurityNumber().equals(person.getSocialSecurityNumber())) {
	    return person;
	}

	if (!StringUtils.isEmpty(getPersonalBean().getSocialSecurityNumber())
		&& !StringUtils.isEmpty(person.getSocialSecurityNumber())) {
	    throw new SocialSecurityNumberMismatchException("Original: " + getPersonalBean().getSocialSecurityNumber()
		    + " Differs from: "
		    + person.getSocialSecurityNumber());
	}

	if (person.getDateOfBirthYearMonthDay() == null
		|| !person.getDateOfBirthYearMonthDay().isEqual(getPersonalBean().getDateOfBirth())) {
	    throw new BirthdayMismatchException("Original: " + getPersonalBean().getDateOfBirth() + " Differs from: "
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

	    if (StringUtils.isEmpty(person.getSocialSecurityNumber())) {
		continue;
	    }

	    if (!person.getSocialSecurityNumber().equals(getPersonalBean().getSocialSecurityNumber())) {
		continue;
	    }

	    if (person.getDateOfBirthYearMonthDay() == null) {
		continue;
	    }

	    if (!person.getDateOfBirthYearMonthDay().isEqual(getPersonalBean().getDateOfBirth())) {
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
	return getPerson().getSocialSecurityNumber().equals(getPersonalBean().getSocialSecurityNumber());
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

	final PhdMigrationIndividualPersonalDataBean personalBean = getPersonalBean();

	bean.setAddress(personalBean.getAddress());
	bean.setArea(personalBean.getArea());
	bean.setAreaCode(personalBean.getAreaCode());
	bean.setParishOfResidence(personalBean.getParishOfResidence());
	bean.setDistrictOfResidence(personalBean.getDistrictOfResidence());
	bean.setDistrictSubdivisionOfResidence(personalBean.getDistrictSubdivisionOfResidence());

	bean.setPhone(personalBean.getContactNumber());
	bean.setWorkPhone(personalBean.getOtherContactNumber());
	bean.setProfession(personalBean.getProfession());
	bean.setEmail(personalBean.getEmail());

	bean.setFatherName(personalBean.getFatherName());
	bean.setMotherName(personalBean.getMotherName());
	bean.setIdDocumentType(IDDocumentType.OTHER);
	bean.setDocumentIdNumber(personalBean.getIdentificationNumber());
	bean.setSocialSecurityNumber(personalBean.getSocialSecurityNumber());

	bean.setGivenNames(readGivenName(personalBean.getFullName(), personalBean.getFamilyName()));
	bean.setName(personalBean.getFullName());
	bean.setFamilyNames(personalBean.getFamilyName());

	bean.setDateOfBirth(new YearMonthDay(personalBean.getDateOfBirth().getYear(), personalBean.getDateOfBirth()
		.getMonthOfYear(), personalBean.getDateOfBirth().getDayOfMonth()));
	bean.setGender(personalBean.getGender());
	bean.setNationality(personalBean.getNationality());

	return bean;
    }

}
