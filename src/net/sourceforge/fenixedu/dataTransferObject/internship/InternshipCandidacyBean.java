package net.sourceforge.fenixedu.dataTransferObject.internship;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.internship.LanguageKnowledgeLevel;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.person.Gender;

import org.joda.time.LocalDate;

/**
 * @author Pedro Santos (pmrsa)
 */
public class InternshipCandidacyBean implements Serializable {
    private static final long serialVersionUID = -4963748520642734496L;

    public enum StudentYear {
	FIRST, SECOND;
    }

    private DomainReference<AcademicalInstitutionUnit> university;
    private String studentNumber;
    private StudentYear studentYear;
    private String degree;
    private String name;
    private Gender gender;

    private String street;
    private String area;
    private String areaCode;

    private String email;
    private String telephone;
    private String mobilePhone;

    private LocalDate birthday;
    private String parishOfBirth;
    private DomainReference<Country> countryOfBirth;

    private String documentIdNumber;
    private String emissionLocationOfDocumentId;
    private LocalDate emissionDateOfDocumentId;
    private LocalDate expirationDateOfDocumentId;

    private String passportIdNumber;
    private String emissionLocationOfPassport;
    private LocalDate emissionDateOfPassport;
    private LocalDate expirationDateOfPassport;

    private DomainReference<Country> firstDestination;
    private DomainReference<Country> secondDestination;
    private DomainReference<Country> thirdDestination;

    private LanguageKnowledgeLevel english;
    private LanguageKnowledgeLevel french;
    private LanguageKnowledgeLevel spanish;
    private LanguageKnowledgeLevel german;

    private Boolean previousCandidacy;

    public InternshipCandidacyBean() {
	setCountryOfBirth(Country.readByTwoLetterCode("PT"));
    }

    public Country getCountryOfBirth() {
	return (this.countryOfBirth != null) ? this.countryOfBirth.getObject() : null;
    }

    public void setCountryOfBirth(Country countryOfBirth) {
	this.countryOfBirth = (countryOfBirth != null) ? new DomainReference<Country>(countryOfBirth) : null;
    }

    public String getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
	this.studentNumber = studentNumber;
    }

    public StudentYear getStudentYear() {
	return studentYear;
    }

    public void setStudentYear(StudentYear studentYear) {
	this.studentYear = studentYear;
    }

    public String getDegree() {
	return degree;
    }

    public void setDegree(String degree) {
	this.degree = degree;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Gender getGender() {
	return gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
    }

    public String getArea() {
	return area;
    }

    public void setArea(String area) {
	this.area = area;
    }

    public String getAreaCode() {
	return areaCode;
    }

    public void setAreaCode(String areaCode) {
	this.areaCode = areaCode;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getTelephone() {
	return telephone;
    }

    public void setTelephone(String telephone) {
	this.telephone = telephone;
    }

    public String getMobilePhone() {
	return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
    }

    public LocalDate getBirthday() {
	return birthday;
    }

    public void setBirthday(LocalDate birthday) {
	this.birthday = birthday;
    }

    public String getParishOfBirth() {
	return parishOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
	this.parishOfBirth = parishOfBirth;
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

    public String getEmissionLocationOfDocumentId() {
	return emissionLocationOfDocumentId;
    }

    public void setEmissionLocationOfDocumentId(String emissionLocationOfDocumentId) {
	this.emissionLocationOfDocumentId = emissionLocationOfDocumentId;
    }

    public LocalDate getEmissionDateOfDocumentId() {
	return emissionDateOfDocumentId;
    }

    public void setEmissionDateOfDocumentId(LocalDate emissionDateOfDocumentId) {
	this.emissionDateOfDocumentId = emissionDateOfDocumentId;
    }

    public LocalDate getExpirationDateOfDocumentId() {
	return expirationDateOfDocumentId;
    }

    public void setExpirationDateOfDocumentId(LocalDate expirationDateOfDocumentId) {
	this.expirationDateOfDocumentId = expirationDateOfDocumentId;
    }

    public String getPassportIdNumber() {
	return passportIdNumber;
    }

    public void setPassportIdNumber(String passportIdNumber) {
	this.passportIdNumber = passportIdNumber;
    }

    public String getEmissionLocationOfPassport() {
	return emissionLocationOfPassport;
    }

    public void setEmissionLocationOfPassport(String emissionLocationOfPassport) {
	this.emissionLocationOfPassport = emissionLocationOfPassport;
    }

    public LocalDate getEmissionDateOfPassport() {
	return emissionDateOfPassport;
    }

    public void setEmissionDateOfPassport(LocalDate emissionDateOfPassport) {
	this.emissionDateOfPassport = emissionDateOfPassport;
    }

    public LocalDate getExpirationDateOfPassport() {
	return expirationDateOfPassport;
    }

    public void setExpirationDateOfPassport(LocalDate expirationDateOfPassport) {
	this.expirationDateOfPassport = expirationDateOfPassport;
    }

    public Country getFirstDestination() {
	return (this.firstDestination != null) ? this.firstDestination.getObject() : null;
    }

    public void setFirstDestination(Country country) {
	this.firstDestination = (country != null) ? new DomainReference<Country>(country) : null;
    }

    public Country getSecondDestination() {
	return (this.secondDestination != null) ? this.secondDestination.getObject() : null;
    }

    public void setSecondDestination(Country country) {
	this.secondDestination = (country != null) ? new DomainReference<Country>(country) : null;
    }

    public Country getThirdDestination() {
	return (this.thirdDestination != null) ? this.thirdDestination.getObject() : null;
    }

    public void setThirdDestination(Country country) {
	this.thirdDestination = (country != null) ? new DomainReference<Country>(country) : null;
    }

    public LanguageKnowledgeLevel getEnglish() {
	return english;
    }

    public void setEnglish(LanguageKnowledgeLevel english) {
	this.english = english;
    }

    public LanguageKnowledgeLevel getFrench() {
	return french;
    }

    public void setFrench(LanguageKnowledgeLevel french) {
	this.french = french;
    }

    public LanguageKnowledgeLevel getSpanish() {
	return spanish;
    }

    public void setSpanish(LanguageKnowledgeLevel spanish) {
	this.spanish = spanish;
    }

    public LanguageKnowledgeLevel getGerman() {
	return german;
    }

    public void setGerman(LanguageKnowledgeLevel german) {
	this.german = german;
    }

    public Boolean getPreviousCandidacy() {
	return previousCandidacy;
    }

    public void setPreviousCandidacy(Boolean previousCandidacy) {
	this.previousCandidacy = previousCandidacy;
    }

    public AcademicalInstitutionUnit getUniversity() {
	return (this.university != null) ? this.university.getObject() : null;
    }

    public void setUniversity(AcademicalInstitutionUnit university) {
	this.university = (university != null) ? new DomainReference<AcademicalInstitutionUnit>(university) : null;
    }
}
