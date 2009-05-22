package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

import org.joda.time.YearMonthDay;

public class Qualification extends Qualification_Base {

    static public Comparator<Qualification> COMPARATOR_BY_YEAR = new Comparator<Qualification>() {
	@Override
	public int compare(Qualification o1, Qualification o2) {
	    int year1 = o1.getDateYearMonthDay().getYear();
	    int year2 = o2.getDateYearMonthDay().getYear();
	    return year1 < year2 ? -1 : (year1 == year2 ? 0 : 1);
	}
    };

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
	setSchool(precedentDegreeInformation.getInstitution() == null ? null : precedentDegreeInformation.getInstitution()
		.getName());
	setDegree(precedentDegreeInformation.getDegreeDesignation() == null ? null : precedentDegreeInformation
		.getDegreeDesignation());
	setDateYearMonthDay(precedentDegreeInformation.getConclusionYear() == null ? null : new YearMonthDay(
		precedentDegreeInformation.getConclusionYear(), 1, 1));
	setCountry(precedentDegreeInformation.getCountry() == null ? null : precedentDegreeInformation.getCountry());
    }

    public Qualification(final Person person, final QualificationBean bean) {
	this();

	check(person, "error.Qualification.invalid.person");

	setPerson(person);
	setType(bean.getType());
	setSchool(bean.getSchool());
	setDegree(bean.getDegree());
	setDateYearMonthDay(new YearMonthDay(Integer.valueOf(bean.getYear()), 1, 1));
	setMark(bean.getMark());
    }

    @Override
    public void setPerson(Person person) {
	/*
	 * 21/04/2009 - A Qualification may be associated with a
	 * IndividualCandidacy. So the person may be null
	 */
	/*
	 * if (person == null) { throw new
	 * DomainException("The person should not be null!"); }
	 */
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
