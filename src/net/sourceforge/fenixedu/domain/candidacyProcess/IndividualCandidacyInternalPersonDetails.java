package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.YearMonthDay;

public class IndividualCandidacyInternalPersonDetails extends IndividualCandidacyInternalPersonDetails_Base {
    public IndividualCandidacyInternalPersonDetails(IndividualCandidacy candidacy, Person person) {
	super();
	setCandidacy(candidacy);
	setPerson(person);
    }

    @Override
    public boolean isInternal() {
	return true;
    }

    @Override
    public void edit(PersonBean personBean) {
	getPerson().edit(personBean);
    }

    @Override
    public void ensurePersonInternalization() {
	// Nothing to do since the candidacy was started by an internal person.
    }

    @Override
    public Student getStudent() {
	return getPerson().getStudent();
    }

    @Override
    public String getName() {
	return getPerson().getName();
    }

    @Override
    public void setName(String name) {
	getPerson().setName(name);
    }

    @Override
    public String getDocumentIdNumber() {
	return getPerson().getDocumentIdNumber();
    }

    @Override
    public void setDocumentIdNumber(String documentIdNumber) {
	getPerson().setDocumentIdNumber(documentIdNumber);
    }

    @Override
    public Country getCountry() {
	return getPerson().getCountry();
    }

    @Override
    public void setCountry(Country country) {
	getPerson().setCountry(country);
    }

    @Override
    public YearMonthDay getDateOfBirthYearMonthDay() {
	return getPerson().getDateOfBirthYearMonthDay();
    }

    @Override
    public void setDateOfBirthYearMonthDay(YearMonthDay birthday) {
	getPerson().setDateOfBirthYearMonthDay(birthday);
    }

    @Override
    public PhysicalAddress getDefaultPhysicalAddress() {
	return getPerson().getDefaultPhysicalAddress();
    }

    @Override
    public YearMonthDay getEmissionDateOfDocumentIdYearMonthDay() {
	return getPerson().getEmissionDateOfDocumentIdYearMonthDay();
    }

    @Override
    public void setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay date) {
	getPerson().setEmissionDateOfDocumentIdYearMonthDay(date);
    }

    @Override
    public String getEmissionLocationOfDocumentId() {
	return getPerson().getEmissionLocationOfDocumentId();
    }

    @Override
    public void setEmissionLocationOfDocumentId(String location) {
	getPerson().setEmissionLocationOfDocumentId(location);
    }

    @Override
    public YearMonthDay getExpirationDateOfDocumentIdYearMonthDay() {
	return getPerson().getExpirationDateOfDocumentIdYearMonthDay();
    }

    @Override
    public void setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay date) {
	getPerson().setExpirationDateOfDocumentIdYearMonthDay(date);
    }

    @Override
    public Gender getGender() {
	return getPerson().getGender();
    }

    @Override
    public void setGender(Gender gender) {
	getPerson().setGender(gender);
    }

    @Override
    public IDDocumentType getIdDocumentType() {
	return getPerson().getIdDocumentType();
    }

    @Override
    public void setIdDocumentType(IDDocumentType type) {
	getPerson().setIdDocumentType(type);
    }

    @Override
    public MaritalStatus getMaritalStatus() {
	return getPerson().getMaritalStatus();
    }

    @Override
    public void setMaritalStatus(MaritalStatus status) {
	getPerson().setMaritalStatus(status);
    }

    @Override
    public String getSocialSecurityNumber() {
	return getPerson().getSocialSecurityNumber();
    }

    @Override
    public void setSocialSecurityNumber(String number) {
	getPerson().setSocialSecurityNumber(number);
    }
}
