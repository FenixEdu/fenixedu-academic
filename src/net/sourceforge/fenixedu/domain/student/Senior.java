/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class Senior extends Senior_Base {

    public Senior(final Registration registration) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRegistration(registration);
    }
    
    public void delete() {
	if(isEmpty()) {
	    super.setStudent(null);
	    removeRootDomainObject();	 
	    deleteDomainObject();
	} else {
	    throw new DomainException("error.Senior.not.empty");
	}
    }
    
    public boolean isEmpty() {
	return getExpectedDegreeAverageGrade() == null 
	&& getExpectedDegreeTerminationDateTime() == null
	&& (getExtracurricularActivities() == null || StringUtils.isEmpty(getExtracurricularActivities().trim())) 
	&& (getInformaticsSkills() == null || StringUtils.isEmpty(getInformaticsSkills().trim()))
	&& (getLanguageSkills() == null || StringUtils.isEmpty(getLanguageSkills().trim()))
	&& (getProfessionalExperience() == null || StringUtils.isEmpty(getProfessionalExperience().trim()))
	&& (getProfessionalInterests() == null || StringUtils.isEmpty(getProfessionalInterests().trim()))
	&& (getSpecialtyField() == null || StringUtils.isEmpty(getSpecialtyField().trim()));		
    }

    public void setExpectedDegreeTerminationYearMonthDay(YearMonthDay date) {	
	setExpectedDegreeTerminationDateTime(date != null ? date.toDateTimeAtMidnight() : null);	
    }

    public YearMonthDay getExpectedDegreeTerminationYearMonthDay() {
	return getExpectedDegreeTerminationDateTime() != null ? getExpectedDegreeTerminationDateTime()
		.toYearMonthDay() : null;
    }

    @Override
    @Deprecated
    public void setStudent(final Registration registration) {
	this.setRegistration(registration);
    }
    
    public void setRegistration(final Registration registration) {
	if (registration == null) {
	    throw new DomainException("error.senior.empty.senior");
	}
	
	super.setStudent(registration);
    }
    
    @Override
    @Deprecated
    public Registration getStudent() {
        return this.getRegistration();
    }
    
    public Registration getRegistration() {
        return super.getStudent();
    }
    
    public Person getPerson() {
	return getRegistration().getPerson();
    }
    
    @Override
    public void setExpectedDegreeAverageGrade(Integer expectedDegreeAverageGrade) {
	setLastModificationDateDateTime(new DateTime());
	super.setExpectedDegreeAverageGrade(expectedDegreeAverageGrade);
    }

    @Override
    public void setExpectedDegreeTerminationDateTime(DateTime expectedDegreeTerminationDateTime) {
	setLastModificationDateDateTime(new DateTime());
	super.setExpectedDegreeTerminationDateTime(expectedDegreeTerminationDateTime);
    }

    @Override
    public void setExtracurricularActivities(String extracurricularActivities) {
	setLastModificationDateDateTime(new DateTime());
	super.setExtracurricularActivities(extracurricularActivities);
    }

    @Override
    public void setInformaticsSkills(String informaticsSkills) {
	setLastModificationDateDateTime(new DateTime());
	super.setInformaticsSkills(informaticsSkills);
    }

    @Override
    public void setLanguageSkills(String languageSkills) {
	setLastModificationDateDateTime(new DateTime());
	super.setLanguageSkills(languageSkills);
    }

    @Override
    public void setProfessionalExperience(String professionalExperience) {
	setLastModificationDateDateTime(new DateTime());
	super.setProfessionalExperience(professionalExperience);
    }

    @Override
    public void setProfessionalInterests(String professionalInterests) {
	setLastModificationDateDateTime(new DateTime());
	super.setProfessionalInterests(professionalInterests);
    }

    @Override
    public void setSpecialtyField(String specialtyField) {
	setLastModificationDateDateTime(new DateTime());
	super.setSpecialtyField(specialtyField);
    }

    public boolean isSenior(ExecutionYear executionYear) {
	return getExpectedDegreeTerminationYearMonthDay().isAfter(executionYear.getBeginDateYearMonthDay());
    }
}
