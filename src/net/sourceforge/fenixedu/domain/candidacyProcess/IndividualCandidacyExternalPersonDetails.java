package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.YearMonthDay;

public class IndividualCandidacyExternalPersonDetails extends IndividualCandidacyExternalPersonDetails_Base {
    public IndividualCandidacyExternalPersonDetails(IndividualCandidacy candidacy, IndividualCandidacyProcessBean bean) {
	super();
	setCandidacy(candidacy);
	setInternalized(Boolean.FALSE);
	// TODO: fill in necessary stuff
    }

    @Override
    public boolean isInternal() {
	return false;
    }

    @Override
    public void edit(PersonBean personBean) {
	// TODO Auto-generated method stub
    }

    @Override
    public void ensurePersonInternalization() {
	// TODO Auto-generated method stub
	// creates an internal person, this is called just before the
	// Registration is created.
	setInternalized(Boolean.TRUE);
    }

    @Override
    public Student getStudent() {
	return hasPerson() ? getPerson().getStudent() : null;
    }

    @Override
    public Country getCountry() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public YearMonthDay getDateOfBirthYearMonthDay() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PhysicalAddress getDefaultPhysicalAddress() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getDocumentIdNumber() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public YearMonthDay getEmissionDateOfDocumentIdYearMonthDay() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getEmissionLocationOfDocumentId() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public YearMonthDay getExpirationDateOfDocumentIdYearMonthDay() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Gender getGender() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IDDocumentType getIdDocumentType() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public MaritalStatus getMaritalStatus() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getSocialSecurityNumber() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setCountry(Country country) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setDateOfBirthYearMonthDay(YearMonthDay birthday) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setDocumentIdNumber(String documentIdNumber) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay date) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setEmissionLocationOfDocumentId(String location) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay date) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setGender(Gender gender) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setIdDocumentType(IDDocumentType type) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setMaritalStatus(MaritalStatus status) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setName(String name) {
	// TODO Auto-generated method stub

    }

    @Override
    public void setSocialSecurityNumber(String number) {
	// TODO Auto-generated method stub

    }

}
