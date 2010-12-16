package net.sourceforge.fenixedu.domain.phd.migration.common;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.MultiplePersonFoundException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonNotFoundException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonSearchByNameMismatchException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import pt.utl.ist.fenix.tools.loaders.IFileLine;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class PersonalData implements IFileLine {

    private Integer administrativeStudentNumber;
    private String identificationNumber;
    private String socialSecurityNumber;
    private String fullName;
    private String familyName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Country nationality;
    private String subSubArea;
    private String subArea;
    private String area;
    private String fatherName;
    private String motherName;
    private String address;
    private String zipCode;
    private String locality;
    private String contactNumber;
    private String otherContactNumber;
    private String profession;
    private String workPlace;
    private String email;

    @Override
    public boolean fillWithFileLineData(String data) {
	String[] fields = data.split("\t");

	try {
	    administrativeStudentNumber = Integer.valueOf(fields[0].trim());
	    identificationNumber = fields[1].trim();
	    socialSecurityNumber = fields[2].trim();
	    fullName = fields[3].trim();
	    familyName = fields[4].trim();
	    dateOfBirth = parseDate(fields[5].trim());
	    gender = parseGender(fields[6].trim());
	    nationality = NationalityTranslator.translate(fields[7].trim());
	    subSubArea = fields[8].trim();
	    subArea = fields[9].trim();
	    area = fields[10].trim();
	    fatherName = fields[11].trim();
	    motherName = fields[12].trim();
	    address = fields[13].trim();
	    zipCode = fields[14].trim();
	    locality = fields[15].trim();
	    contactNumber = fields[16].trim();
	    otherContactNumber = fields[17].trim();
	    profession = fields[18].trim();
	    workPlace = fields[19].trim();
	    email = fields[20].trim();

	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    private static LocalDate parseDate(String value) {
	return DateTimeFormat.forPattern("ddMMyyyy").parseDateTime(value).toLocalDate();
    }

    private static Gender parseGender(String value) {
	if ("M".equals(value)) {
	    return Gender.MALE;
	}

	if ("F".equals(value)) {
	    return Gender.FEMALE;
	}

	return null;
    }

    @Override
    public String getUniqueKey() {
	return identificationNumber;
    }

    public Person getPerson() {
	// Get by identification number
	Collection<Person> personSet = Person.readByDocumentIdNumber(identificationNumber);

	if (personSet.isEmpty()) {
	    throw new PersonNotFoundException();
	}

	if (personSet.size() > 1) {
	    throw new MultiplePersonFoundException();
	}

	Person person = personSet.iterator().next();

	if (Person.readPersonsByName(StringNormalizer.normalize(person.getName())).size() != 1) {
	    throw new PersonSearchByNameMismatchException();
	}

	return person;
    }

    public boolean isPersonRegisteredOnFenix() {
	return getPerson() != null;
    }

    public boolean isSocialSecurityNumberEqual() {
	return getPerson().getSocialSecurityNumber().equals(socialSecurityNumber);
    }

    public Person createPerson() {
	return null;
    }

}
