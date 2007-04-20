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
	setPerson(person);
	if (country != null) {
	    setCountry(country);
	}	
	setBasicProperties(infoQualification);
    }

    public Qualification(Person person, PrecedentDegreeInformation precedentDegreeInformation) {
	this();	
	setPerson(person);
	setMark(precedentDegreeInformation.getConclusionGrade() == null ? null : precedentDegreeInformation.getConclusionGrade());
	setSchool(precedentDegreeInformation.getInstitution() == null ? null : precedentDegreeInformation.getInstitution().getName());
	setDegree(precedentDegreeInformation.getDegreeDesignation() == null ? null	: precedentDegreeInformation.getDegreeDesignation());
	setDateYearMonthDay(precedentDegreeInformation.getConclusionYear() == null ? null : new YearMonthDay(precedentDegreeInformation.getConclusionYear(), 1, 1));
	setCountry(precedentDegreeInformation.getCountry() == null ? null : precedentDegreeInformation.getCountry());
    }

    @Override
    public void setPerson(Person person) {
	if (person == null) {
	    throw new DomainException("The person should not be null!");
	}
	super.setPerson(person);
    }
    
    public void delete() {	
	super.setPerson(null);
	removeCountry();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void edit(InfoQualification infoQualification, Country country) {	
	setBasicProperties(infoQualification);
	if (country == null) {
	    removeCountry();
	} else {
	    setCountry(country);
	}
    }
    
    private void setBasicProperties(InfoQualification infoQualification) {
	setBranch(infoQualification.getBranch());
	setDate(infoQualification.getDate());
	if (getDate() != null && !getDate().equals("")) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(getDate());
	    setYear(String.valueOf(calendar.get(Calendar.YEAR)));
	} else {
	    setYear(null);
	}
	setDegree(infoQualification.getDegree());
	setDegreeRecognition(infoQualification.getDegreeRecognition());
	setEquivalenceDate(infoQualification.getEquivalenceDate());
	setEquivalenceSchool(infoQualification.getEquivalenceSchool());
	setMark(infoQualification.getMark());
	setSchool(infoQualification.getSchool());
	setSpecializationArea(infoQualification.getSpecializationArea());
	setTitle(infoQualification.getTitle());
    }
}
