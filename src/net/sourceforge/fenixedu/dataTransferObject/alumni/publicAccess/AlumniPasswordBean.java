package net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniRequestType;
import net.sourceforge.fenixedu.domain.DomainReference;

public class AlumniPasswordBean implements Serializable {

    private DomainReference<Alumni> alumni;
    private String contactEmail;
    private String documentIdNumber;
    private String fullName;
    private YearMonthDay dateOfBirthYearMonthDay;
    private String districtOfBirth;
    private String districtSubdivisionOfBirth;
    private String parishOfBirth;
    private String socialSecurityNumber;
    private String nameOfFather;
    private String nameOfMother;
    private String password;
    private String passwordConfirmation;
    private AlumniRequestType requestType;

    public AlumniPasswordBean(Alumni alumni) {
	setAlumni(alumni);
	setPassword("");
	setPasswordConfirmation("");
    }

    public AlumniPasswordBean(Alumni alumni, AlumniRequestType requestType) {
	this(alumni);
	setRequestType(requestType);
    }

    public Alumni getAlumni() {
	return (this.alumni != null) ? this.alumni.getObject() : null;
    }

    public void setAlumni(Alumni alumni) {
	this.alumni = new DomainReference<Alumni>(alumni);
    }

    public String getContactEmail() {
	return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
	this.contactEmail = contactEmail;
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public YearMonthDay getDateOfBirthYearMonthDay() {
	return dateOfBirthYearMonthDay;
    }

    public void setDateOfBirthYearMonthDay(YearMonthDay dateOfBirthYearMonthDay) {
	this.dateOfBirthYearMonthDay = dateOfBirthYearMonthDay;
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

    public void setParishOfBirth(String parishOfBirth) {
	this.parishOfBirth = parishOfBirth;
    }
    
    public String getParishOfBirth() {
	return parishOfBirth;
    }
    
    public String getSocialSecurityNumber() {
	return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
	this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getNameOfFather() {
	return nameOfFather;
    }

    public void setNameOfFather(String nameOfFather) {
	this.nameOfFather = nameOfFather;
    }

    public String getNameOfMother() {
	return nameOfMother;
    }

    public void setNameOfMother(String nameOfMother) {
	this.nameOfMother = nameOfMother;
    }
    
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getPasswordConfirmation() {
	return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
	this.passwordConfirmation = passwordConfirmation;
    }

    public AlumniRequestType getRequestType() {
	return requestType;
    }

    public void setRequestType(AlumniRequestType requestType) {
	this.requestType = requestType;
    }
}
