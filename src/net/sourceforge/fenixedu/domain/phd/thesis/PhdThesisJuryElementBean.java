package net.sourceforge.fenixedu.domain.phd.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

public class PhdThesisJuryElementBean implements Serializable {

    private static final long serialVersionUID = -5365333247731361583L;

    private String name;
    private String qualification;
    private String category;
    private String institution;
    private String address;
    private String phone;
    private String email;
    private Person person;

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

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public boolean isInternal() {
	return getPerson() != null;
    }

}
