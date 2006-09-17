package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.YearMonthDay;

public class PersonalInformationForm extends Form {

    // Filiation
    private String name; // read only

    private String username; // read only

    private Gender gender; // read only

    private String documentIdNumber; // read only

    private IDDocumentType idDocumentType;

    private String documentIdEmissionLocation;

    private YearMonthDay documentIdEmissionDate;

    private YearMonthDay documentIdExpirationDate;

    private String socialSecurityNumber;

    private String profession;

    private MaritalStatus maritalStatus;

    public PersonalInformationForm() {
	super();
    }

    private PersonalInformationForm(YearMonthDay documentIdEmissionDate,
	    String documentIdEmissionLocation, YearMonthDay documentIdExpirationDate,
	    String documentIdNumber, IDDocumentType documentType, Gender gender,
	    MaritalStatus maritalStatus, String name, String profession, String socialSecurityNumber,
	    String username) {
	this();
	this.documentIdEmissionDate = documentIdEmissionDate;
	this.documentIdEmissionLocation = documentIdEmissionLocation;
	this.documentIdExpirationDate = documentIdExpirationDate;
	this.documentIdNumber = documentIdNumber;
	this.idDocumentType = documentType;
	this.gender = gender;
	this.maritalStatus = maritalStatus;
	this.name = name;
	this.profession = profession;
	this.socialSecurityNumber = socialSecurityNumber;
	this.username = username;
    }

    public static PersonalInformationForm createFromPerson(final Person person) {
	return new PersonalInformationForm(person.getEmissionDateOfDocumentIdYearMonthDay(), person
		.getEmissionLocationOfDocumentId(), person.getExpirationDateOfDocumentIdYearMonthDay(),
		person.getDocumentIdNumber(), person.getIdDocumentType(), person.getGender(), person
			.getMaritalStatus(), person.getName(), person.getProfession(), person
			.getSocialSecurityNumber(), person.getUsername());
    }

    public YearMonthDay getDocumentIdEmissionDate() {
	return documentIdEmissionDate;
    }

    public void setDocumentIdEmissionDate(YearMonthDay documentIdEmissionDate) {
	this.documentIdEmissionDate = documentIdEmissionDate;
    }

    public String getDocumentIdEmissionLocation() {
	return documentIdEmissionLocation;
    }

    public void setDocumentIdEmissionLocation(String documentIdEmissionLocation) {
	this.documentIdEmissionLocation = documentIdEmissionLocation;
    }

    public YearMonthDay getDocumentIdExpirationDate() {
	return documentIdExpirationDate;
    }

    public void setDocumentIdExpirationDate(YearMonthDay documentIdExpirationDate) {
	this.documentIdExpirationDate = documentIdExpirationDate;
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

    public IDDocumentType getIdDocumentType() {
	return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType documentType) {
	this.idDocumentType = documentType;
    }

    public Gender getGender() {
	return gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
	return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
	this.maritalStatus = maritalStatus;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getProfession() {
	return profession;
    }

    public void setProfession(String profession) {
	this.profession = profession;
    }

    public String getSocialSecurityNumber() {
	return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
	this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.personalInformationForm";
    }

    @Override
    public List<LabelFormatter> validate() {
	return Collections.EMPTY_LIST;
    }

}