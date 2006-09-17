package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.YearMonthDay;

public class FiliationForm extends Form {

    private static final Integer DEFAULT_COUNTRY_ID = Integer.valueOf(1);

    private YearMonthDay dateOfBirth;

    private DomainReference<Country> nationality;

    private String parishOfBirth;

    private String districtSubdivisionOfBirth;

    private String districtOfBirth;

    private String fatherName;

    private String motherName;

    private DomainReference<Country> countryOfBirth;

    public FiliationForm() {
	super();
    }

    public static FiliationForm createFromPerson(final Person person) {
	final Country nationality = (person.hasRole(RoleType.EMPLOYEE)) ? person.getNationality()
		: RootDomainObject.getInstance().readCountryByOID(DEFAULT_COUNTRY_ID);

	return new FiliationForm(person.getDateOfBirthYearMonthDay(), person.getDistrictOfBirth(),
		person.getDistrictSubdivisionOfBirth(), person.getNameOfFather(), person
			.getNameOfMother(), nationality, person.getParishOfBirth(), RootDomainObject
			.getInstance().readCountryByOID(DEFAULT_COUNTRY_ID));
    }

    private FiliationForm(YearMonthDay dateOfBirth, String districtOfBirth,
	    String districtSubdivisionOfBirth, String fatherName, String motherName,
	    Country nationality, String parishOfBirth, Country countryOfBirth) {
	this();
	this.dateOfBirth = dateOfBirth;
	this.districtOfBirth = districtOfBirth;
	this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
	this.fatherName = fatherName;
	this.motherName = motherName;
	setNationality(nationality);
	this.parishOfBirth = parishOfBirth;
	setCountryOfBirth(countryOfBirth);
    }

    public YearMonthDay getDateOfBirth() {
	return dateOfBirth;
    }

    public void setDateOfBirth(YearMonthDay dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
    }

    public String getDistrictOfBirth() {
	return districtOfBirth;
    }

    public void setDistrictOfBirth(String districtOfBirth) {
	this.districtOfBirth = districtOfBirth;
    }

    public String getDistrictSubdivisionOfBirth() {
	return districtSubdivisionOfBirth;
    }

    public void setDistrictSubdivisionOfBirth(String districtSubdivisionOfBirth) {
	this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
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

    public Country getNationality() {
	return (this.nationality != null) ? this.nationality.getObject() : null;
    }

    public void setNationality(Country nationality) {
	this.nationality = (nationality != null) ? new DomainReference<Country>(nationality) : null;
    }

    public String getParishOfBirth() {
	return parishOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
	this.parishOfBirth = parishOfBirth;
    }

    public Country getCountryOfBirth() {
	return (this.countryOfBirth != null) ? this.countryOfBirth.getObject() : null;
    }

    public void setCountryOfBirth(Country countryOfBirth) {
	this.countryOfBirth = (countryOfBirth != null) ? new DomainReference<Country>(countryOfBirth)
		: null;
    }

    @Override
    public List<LabelFormatter> validate() {
	return Collections.EMPTY_LIST;
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.filiationForm";
    }
}