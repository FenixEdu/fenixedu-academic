package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Collection;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.NationalityTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.MultiplePersonFoundException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonNotFoundException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonSearchByNameMismatchException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.SocialSecurityNumberMismatchException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

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
    private transient String subSubArea;
    private transient String subArea;
    private transient String area;
    private transient String fatherName;
    private transient String motherName;
    private transient String address;
    private transient String zipCode;
    private transient String locality;
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

    public void parse() {
	String[] fields = getData().split("\t");

	try {
	    phdStudentNumber = Integer.valueOf(fields[0].trim());

	    identificationNumber = fields[1].trim();
	    socialSecurityNumber = fields[2].trim();
	    fullName = fields[3].trim();
	    familyName = fields[4].trim();
	    dateOfBirth = ConversionUtilities.parseLocalDate(fields[5].trim());
	    gender = ConversionUtilities.parseGender(fields[6].trim());
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

	} catch (NoSuchElementException e) {
	    throw new IncompleteFieldsException();
	}
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

	if (!StringUtils.isEmpty(socialSecurityNumber) && socialSecurityNumber.equals(person.getSocialSecurityNumber())) {
	    throw new SocialSecurityNumberMismatchException();
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
