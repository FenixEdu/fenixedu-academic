package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.AlumniRequestType;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;

import org.joda.time.YearMonthDay;

public class AlumniIdentityCheckRequestBean implements Serializable {

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
    private AlumniRequestType requestType;
    
    public AlumniIdentityCheckRequestBean(AlumniRequestType requestType) {
	setRequestType(requestType);
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
    
    public AlumniRequestType getRequestType() {
	return requestType;
    }

    public void setRequestType(AlumniRequestType requestType) {
	this.requestType = requestType;
    }

}
