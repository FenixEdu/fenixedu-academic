package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

import org.joda.time.YearMonthDay;

public class Qualification extends Qualification_Base {

    public Qualification() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Qualification(Person person, Country country, InfoQualification infoQualification) {
	this();
	if (person == null)
	    throw new DomainException("The person should not be null!");
	this.setPerson(person);
	if (country != null)
	    this.setCountry(country);
	setBasicProperties(infoQualification);
    }

    public Qualification(Person person, PrecedentDegreeInformation precedentDegreeInformation) {
	this();
	if (person == null) {
	    throw new DomainException("The person should not be null!");
	}
	this.setPerson(person);

	this.setMark(precedentDegreeInformation.getConclusionGrade() == null ? null
		: precedentDegreeInformation.getConclusionGrade());
	this.setSchool(precedentDegreeInformation.getInstitution() == null ? null
		: precedentDegreeInformation.getInstitution().getName());
	this.setDegree(precedentDegreeInformation.getDegreeDesignation() == null ? null
		: precedentDegreeInformation.getDegreeDesignation());
	this.setDateYearMonthDay(precedentDegreeInformation.getConclusionYear() == null ? null
		: new YearMonthDay(precedentDegreeInformation.getConclusionYear(), 1, 1));
	this.setCountry(precedentDegreeInformation.getCountry() == null ? null
		: precedentDegreeInformation.getCountry());
    }

    public void delete() {
	removePerson();
	removeCountry();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void edit(InfoQualification infoQualification, Country country) {
	// The country can be null
	this.setBasicProperties(infoQualification);
	if (country == null)
	    removeCountry();
	else
	    this.setCountry(country);
    }

    /* PRIVATE METHODS */
    private void setBasicProperties(InfoQualification infoQualification) {
	this.setBranch(infoQualification.getBranch());
	this.setDate(infoQualification.getDate());
	if (this.getDate() != null && !this.getDate().equals("")) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(this.getDate());
	    this.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
	} else {
	    this.setYear(null);
	}
	this.setDegree(infoQualification.getDegree());
	this.setDegreeRecognition(infoQualification.getDegreeRecognition());
	this.setEquivalenceDate(infoQualification.getEquivalenceDate());
	this.setEquivalenceSchool(infoQualification.getEquivalenceSchool());
	this.setMark(infoQualification.getMark());
	this.setSchool(infoQualification.getSchool());
	this.setSpecializationArea(infoQualification.getSpecializationArea());
	this.setTitle(infoQualification.getTitle());
    }
}
