package net.sourceforge.fenixedu.domain.phd.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class PhdThesisJuryElementBean implements Serializable {

    private static final long serialVersionUID = -5365333247731361583L;

    private String name;
    private String qualification;
    private String category;
    private String institution;
    private String address;
    private String phone;
    private String email;

    private boolean president;
    private boolean reporter;

    private PhdThesisJuryElementType juryType;
    private PersonName personName;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getQualification() {
	return qualification;
    }

    public void setQualification(String qualification) {
	this.qualification = qualification;
    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    public String getInstitution() {
	return institution;
    }

    public void setInstitution(String institution) {
	this.institution = institution;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public PersonName getPersonName() {
	return personName;
    }

    public void setPersonName(PersonName personName) {
	this.personName = personName;
    }

    public Person getPerson() {
	return getPersonName().getPerson();
    }

    public boolean isPresident() {
	return president;
    }

    public void setPresident(boolean president) {
	this.president = president;
    }

    public boolean isReporter() {
	return reporter;
    }

    public void setReporter(boolean reporter) {
	this.reporter = reporter;
    }

    public PhdThesisJuryElementType getJuryType() {
	return juryType;
    }

    public void setJuryType(PhdThesisJuryElementType juryType) {
	this.juryType = juryType;
    }

    public boolean isInternal() {
	return getJuryType() == PhdThesisJuryElementType.INTERNAL;
    }

    static public enum PhdThesisJuryElementType {
	INTERNAL, EXTERNAL;

	public String getName() {
	    return name();
	}
    }
}
