package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Qualification extends Qualification_Base {

    public Qualification(){
        super();
    }
    
    public Qualification(Person person, Country country, InfoQualification infoQualification) {
        if(person == null)
            throw new DomainException("The person should not be null!");
        this.setPerson(person);
        if(country != null)
            this.setCountry(country);
        setBasicProperties(infoQualification);
    }
    
	public void delete() {
		removePerson();
		removeCountry();
		super.deleteDomainObject();
	}
	
	public void edit(InfoQualification infoQualification, Country country) {
		//The country can be null
		this.setBasicProperties(infoQualification);
		if(country == null)
			removeCountry();
		else
			this.setCountry(country);
	}
	
	/* PRIVATE METHODS */
	private void setBasicProperties(InfoQualification infoQualification) {
        this.setBranch(infoQualification.getBranch());
        this.setDate(infoQualification.getDate());
        if(this.getDate() != null && !this.getDate().equals("")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.getDate());
            this.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
        }else{
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
