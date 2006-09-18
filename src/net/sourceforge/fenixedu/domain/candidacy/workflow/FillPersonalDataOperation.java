package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ContactsForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.FiliationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.FillPersonalDataWelcomeForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.InquiryAboutYieldingPersonalDataForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.OriginInformationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.PersonalInformationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceApplianceInquiryForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceInformationForm;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

public class FillPersonalDataOperation extends CandidacyOperation {

    private PersonalInformationForm personalInformationForm;

    private FiliationForm filiationForm;

    private ResidenceInformationForm residenceInformationForm;

    private ContactsForm contactsForm;

    private InquiryAboutYieldingPersonalDataForm inquiryAboutYieldingPersonalDataForm;

    private OriginInformationForm originInformationForm;

    private ResidenceApplianceInquiryForm residenceApplianceInquiryForm;

    public FillPersonalDataOperation(final Set<RoleType> roleTypes, final DegreeCandidacy degreeCandidacy) {
	super(roleTypes, degreeCandidacy);

	addForm(new FillPersonalDataWelcomeForm());

	setPersonalInformationForm(PersonalInformationForm.createFromPerson(getPerson()));
	addForm(getPersonalInformationForm());

	setFiliationForm(FiliationForm.createFromPerson(getPerson()));
	addForm(getFiliationForm());

	setResidenceInformationForm(ResidenceInformationForm.createFromPerson(getPerson()));
	addForm(getResidenceInformationForm());

	setContactsForm(ContactsForm.createFromPerson(getPerson()));
	addForm(getContactsForm());

	setInquiryAboutYieldingPersonalDataForm(new InquiryAboutYieldingPersonalDataForm());
	addForm(getInquiryAboutYieldingPersonalDataForm());

	setOriginInformationForm(new OriginInformationForm());
	addForm(getOriginInformationForm());

	setResidenceApplianceInquiryForm(new ResidenceApplianceInquiryForm());
	addForm(getResidenceApplianceInquiryForm());

    }

    public DegreeCandidacy getDegreeCandidacy() {
	return (DegreeCandidacy) getCandidacy();
    }

    private Person getPerson() {
	return getDegreeCandidacy().getPerson();
    }

    public FiliationForm getFiliationForm() {
	return filiationForm;
    }

    public void setFiliationForm(FiliationForm filiationForm) {
	this.filiationForm = filiationForm;
    }

    public ContactsForm getContactsForm() {
	return contactsForm;
    }

    public void setContactsForm(ContactsForm fillContactsForm) {
	this.contactsForm = fillContactsForm;
    }

    public InquiryAboutYieldingPersonalDataForm getInquiryAboutYieldingPersonalDataForm() {
	return inquiryAboutYieldingPersonalDataForm;
    }

    public void setInquiryAboutYieldingPersonalDataForm(
	    InquiryAboutYieldingPersonalDataForm fillInquiryAboutYieldingPersonalDataForm) {
	this.inquiryAboutYieldingPersonalDataForm = fillInquiryAboutYieldingPersonalDataForm;
    }

    public OriginInformationForm getOriginInformationForm() {
	return originInformationForm;
    }

    public void setOriginInformationForm(OriginInformationForm fillOriginInformationForm) {
	this.originInformationForm = fillOriginInformationForm;
    }

    public ResidenceInformationForm getResidenceInformationForm() {
	return residenceInformationForm;
    }

    public void setResidenceInformationForm(ResidenceInformationForm fillResidenceInformationForm) {
	this.residenceInformationForm = fillResidenceInformationForm;
    }

    public PersonalInformationForm getPersonalInformationForm() {
	return personalInformationForm;
    }

    public void setPersonalInformationForm(PersonalInformationForm personalInformationForm) {
	this.personalInformationForm = personalInformationForm;
    }

    public ResidenceApplianceInquiryForm getResidenceApplianceInquiryForm() {
	return residenceApplianceInquiryForm;
    }

    public void setResidenceApplianceInquiryForm(
	    ResidenceApplianceInquiryForm fillResidenceApplianceInquiryForm) {
	this.residenceApplianceInquiryForm = fillResidenceApplianceInquiryForm;
    }

    @Override
    public final CandidacyOperationType getType() {
	return CandidacyOperationType.FILL_PERSONAL_DATA;
    }

    @Override
    public final boolean isInput() {
	return true;

    }

    @Override
    public boolean isAuthorized(Person person) {
	return (super.isAuthorized(person) && getCandidacy().getPerson() == person) ? true : false;
    }

    @Override
    protected void internalExecute() {
	final Person person = getDegreeCandidacy().getPerson();
	fillPersonalInformation(person);
	fillFiliation(person);
	fillResidenceInformation(person);
	fillContacts(person);
	fillPersonalDataAuthorizationChoice();
	fillOriginInformation();
	fillResidenceAppliance();
    }

    private void fillResidenceAppliance() {
	getDegreeCandidacy().setApplyForResidence(
		getResidenceApplianceInquiryForm().isToApplyForResidence());
	getDegreeCandidacy().setNotesAboutResidenceAppliance(
		getResidenceApplianceInquiryForm().getNotesAboutApplianceForResidence());
    }

    protected void fillOriginInformation() {
	final PrecedentDegreeInformation precedentDegreeInformation = getDegreeCandidacy()
		.getPrecedentDegreeInformation();
	precedentDegreeInformation.setConclusionGrade(getOriginInformationForm().getConclusionGrade());
	precedentDegreeInformation.setDegreeDesignation(getOriginInformationForm()
		.getDegreeDesignation());

	Unit institution = getOriginInformationForm().getInstitution();
	if (institution == null) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(getOriginInformationForm()
		    .getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewExternalInstitution(getOriginInformationForm()
			.getInstitutionName());
	    }
	}
	precedentDegreeInformation.setInstitution(institution);
	precedentDegreeInformation.setConclusionYear(getOriginInformationForm().getConclusionYear());
	precedentDegreeInformation.setCountry(getOriginInformationForm()
		.getCountryWhereFinishedPrecedentDegree());
    }

    protected void fillPersonalDataAuthorizationChoice() {
	getDegreeCandidacy().setStudentPersonalDataAuthorizationChoice(
		getInquiryAboutYieldingPersonalDataForm().getPersonalDataAuthorizationChoice());
    }

    protected void fillContacts(final Person person) {
	person.setEmail(getContactsForm().getEmail());
	person.setWebAddress(getContactsForm().getWebAddress());
	person.setMobile(getContactsForm().getMobileNumber());
	person.setPhone(getContactsForm().getPhoneNumber());
    }

    protected void fillResidenceInformation(final Person person) {
	person.setAddress(getResidenceInformationForm().getAddress());
	person.setArea(getResidenceInformationForm().getArea());
	person.setAreaCode(getResidenceInformationForm().getAreaCode());
	person.setAreaOfAreaCode(getResidenceInformationForm().getAreaOfAreaCode());
	person.setDistrictOfResidence(getResidenceInformationForm().getDistrictOfResidence());
	person.setDistrictSubdivisionOfResidence(getResidenceInformationForm()
		.getDistrictSubdivisionOfResidence());
	person.setParishOfResidence(getResidenceInformationForm().getParishOfResidence());
	person.setCountryOfResidence(getResidenceInformationForm().getCountryOfResidence());
    }

    protected void fillFiliation(final Person person) {
	person.setDistrictOfBirth(getFiliationForm().getDistrictOfBirth());
	person.setDistrictSubdivisionOfBirth(getFiliationForm().getDistrictSubdivisionOfBirth());
	person.setNameOfFather(getFiliationForm().getFatherName());
	person.setNameOfMother(getFiliationForm().getMotherName());
	person.setParishOfBirth(getFiliationForm().getParishOfBirth());
	person.setDateOfBirthYearMonthDay(getFiliationForm().getDateOfBirth());
	person.setNationality(getFiliationForm().getNationality());
	person.setDistrictSubdivisionOfBirth(getFiliationForm().getDistrictSubdivisionOfBirth());
	person.setCountryOfBirth(getFiliationForm().getCountryOfBirth());
    }

    protected void fillPersonalInformation(final Person person) {
	person.setEmissionDateOfDocumentIdYearMonthDay(getPersonalInformationForm()
		.getDocumentIdEmissionDate());
	person.setExpirationDateOfDocumentIdYearMonthDay(getPersonalInformationForm()
		.getDocumentIdExpirationDate());
	person.setEmissionLocationOfDocumentId(getPersonalInformationForm()
		.getDocumentIdEmissionLocation());
	person.setProfession(getPersonalInformationForm().getProfession());
	person.setSocialSecurityNumber(getPersonalInformationForm().getSocialSecurityNumber());
	person.setIdDocumentType(getPersonalInformationForm().getIdDocumentType());
	person.setMaritalStatus(getPersonalInformationForm().getMaritalStatus());
    }
}