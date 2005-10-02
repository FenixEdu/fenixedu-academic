package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class Qualification extends Qualification_Base {
	
	public Qualification() {
		super();
	}
	
	public Qualification(IPerson person, ICountry country, InfoQualification infoQualification) {
		if(person == null)
			throw new DomainException("The person should not be null!");
		this.setPerson(person);
		if(country != null)
			this.setCountry(country);
		setBasicProperties(infoQualification);
	}

    public String toString() {
        String result = "Qualification :\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - School : " + getSchool();
        result += "\n  - Title : " + getTitle();
        result += "\n  - Mark : " + getMark();
        result += "\n  - Person : " + getPerson();
        result += "\n  - Last Modication Date : " + getLastModificationDate();
        result += "\n  - Branch : " + getBranch();
        result += "\n  - Specialization Area : " + getSpecializationArea();
        result += "\n  - Degree Recognition : " + getDegreeRecognition();
        result += "\n  - Equivalence School : " + getEquivalenceSchool();
        result += "\n  - Equivalence Date : " + getEquivalenceDate();
        result += "\n  - Country : " + (getCountry() != null ? getCountry().getName() : "");

        return result;
    }
	
	public void delete() {
		removePerson();
		removeCountry();
		super.deleteDomainObject();
	}
	
	public void edit(InfoQualification infoQualification, ICountry country) {
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
