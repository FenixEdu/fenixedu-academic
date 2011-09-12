package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.IdentificationDocumentExtraDigit;
import net.sourceforge.fenixedu.domain.IdentificationDocumentSeriesNumber;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ContactsForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.FiliationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.FillPersonalDataWelcomeForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.HouseholdInformationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.InquiryAboutYieldingPersonalDataForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.OriginInformationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.PersonalInformationForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceApplianceInquiryForm;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceInformationForm;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

public class FillPersonalDataOperation extends CandidacyOperation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PersonalInformationForm personalInformationForm;

    private FiliationForm filiationForm;

    private HouseholdInformationForm householdInformationForm;

    private ResidenceInformationForm residenceInformationForm;

    private ContactsForm contactsForm;

    private InquiryAboutYieldingPersonalDataForm inquiryAboutYieldingPersonalDataForm;

    private OriginInformationForm originInformationForm;

    private ResidenceApplianceInquiryForm residenceApplianceInquiryForm;

    public FillPersonalDataOperation(final Set<RoleType> roleTypes, final StudentCandidacy studentCandidacy) {
	super(roleTypes, studentCandidacy);

	addForm(new FillPersonalDataWelcomeForm());

	setPersonalInformationForm(PersonalInformationForm.createFromPerson(getPerson()));
	addForm(getPersonalInformationForm());

	setFiliationForm(FiliationForm.createFromPerson(getPerson()));
	addForm(getFiliationForm());

	setHouseholdInformationForm(new HouseholdInformationForm());
	addForm(getHouseholdInformationForm());

	setResidenceInformationForm(ResidenceInformationForm.createFromPerson(getPerson()));
	addForm(getResidenceInformationForm());

	setContactsForm(ContactsForm.createFromPerson(getPerson()));
	addForm(getContactsForm());

	setInquiryAboutYieldingPersonalDataForm(new InquiryAboutYieldingPersonalDataForm());
	addForm(getInquiryAboutYieldingPersonalDataForm());

	setOriginInformationForm(OriginInformationForm.createFrom(studentCandidacy));
	addForm(getOriginInformationForm());

	setResidenceApplianceInquiryForm(new ResidenceApplianceInquiryForm());
	addForm(getResidenceApplianceInquiryForm());

    }

    public StudentCandidacy getStudentCandidacy() {
	return (StudentCandidacy) getCandidacy();
    }

    private Person getPerson() {
	return getStudentCandidacy().getPerson();
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

    public void setResidenceApplianceInquiryForm(ResidenceApplianceInquiryForm fillResidenceApplianceInquiryForm) {
	this.residenceApplianceInquiryForm = fillResidenceApplianceInquiryForm;
    }

    public HouseholdInformationForm getHouseholdInformationForm() {
	return householdInformationForm;
    }

    public void setHouseholdInformationForm(HouseholdInformationForm householdInformationForm) {
	this.householdInformationForm = householdInformationForm;
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
	fillPersonalInformation();
	fillFiliation();
	fillHouseholdInformation();
	fillResidenceInformation();
	fillContacts();
	fillPersonalDataAuthorizationChoice();
	fillOriginInformation();
	fillResidenceAppliance();
    }

    private void fillHouseholdInformation() {
	getStudentCandidacy().setFatherProfessionalCondition(getHouseholdInformationForm().getFatherProfessionalCondition());
	getStudentCandidacy().setFatherProfessionType(getHouseholdInformationForm().getFatherProfessionType());
	getStudentCandidacy().setFatherSchoolLevel(getHouseholdInformationForm().getFatherSchoolLevel());
	getStudentCandidacy().setMotherProfessionalCondition(getHouseholdInformationForm().getMotherProfessionalCondition());
	getStudentCandidacy().setMotherProfessionType(getHouseholdInformationForm().getMotherProfessionType());
	getStudentCandidacy().setMotherSchoolLevel(getHouseholdInformationForm().getMotherSchoolLevel());
	getStudentCandidacy().setSpouseProfessionalCondition(getHouseholdInformationForm().getSpouseProfessionalCondition());
	getStudentCandidacy().setSpouseProfessionType(getHouseholdInformationForm().getSpouseProfessionType());
	getStudentCandidacy().setSpouseSchoolLevel(getHouseholdInformationForm().getSpouseSchoolLevel());
    }

    private void fillResidenceAppliance() {
	getStudentCandidacy().setApplyForResidence(getResidenceApplianceInquiryForm().isToApplyForResidence());
	getStudentCandidacy().setNotesAboutResidenceAppliance(
		getResidenceApplianceInquiryForm().getNotesAboutApplianceForResidence());
    }

    protected void fillOriginInformation() {
	final PrecedentDegreeInformation precedentDegreeInformation = getStudentCandidacy().getPrecedentDegreeInformation();
	precedentDegreeInformation.setConclusionGrade(getOriginInformationForm().getConclusionGrade());
	precedentDegreeInformation.setDegreeDesignation(getOriginInformationForm().getDegreeDesignation());
	precedentDegreeInformation.setSchoolLevel(getOriginInformationForm().getSchoolLevel());
	if (getOriginInformationForm().getSchoolLevel() == SchoolLevelType.OTHER) {
	    precedentDegreeInformation.setOtherSchoolLevel(getOriginInformationForm().getOtherSchoolLevel());
	}

	Unit institution = getOriginInformationForm().getInstitution();
	if (institution == null) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(getOriginInformationForm().getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewNoOfficialExternalInstitution(getOriginInformationForm().getInstitutionName());
	    }
	}
	precedentDegreeInformation.setInstitution(institution);
	precedentDegreeInformation.setConclusionYear(getOriginInformationForm().getConclusionYear());
	precedentDegreeInformation.setCountry(getOriginInformationForm().getCountryWhereFinishedPrecedentDegree());

	getStudentCandidacy().setNumberOfCandidaciesToHigherSchool(
		getOriginInformationForm().getNumberOfCandidaciesToHigherSchool());
	getStudentCandidacy().setNumberOfFlunksOnHighSchool(getOriginInformationForm().getNumberOfFlunksOnHighSchool());
	getStudentCandidacy().setHighSchoolType(getOriginInformationForm().getHighSchoolType());

    }

    protected void fillPersonalDataAuthorizationChoice() {
	getStudentCandidacy().setStudentPersonalDataAuthorizationChoice(
		getInquiryAboutYieldingPersonalDataForm().getPersonalDataAuthorizationChoice());
    }

    protected void fillContacts() {
	final Person person = getStudentCandidacy().getPerson();

	person.setEmail(getContactsForm().getEmail());
	if (person.hasDefaultEmailAddress()) {
	    person.getDefaultEmailAddress().setVisibleToPublic(getContactsForm().isEmailAvailable());
	}

	person.setDefaultPhoneNumber(getContactsForm().getPhoneNumber());

	person.setDefaultWebAddressUrl(getContactsForm().getWebAddress());
	if (person.hasDefaultWebAddress()) {
	    person.getDefaultWebAddress().setVisibleToPublic(getContactsForm().isHomepageAvailable());
	}

	person.setDefaultMobilePhoneNumber(getContactsForm().getMobileNumber());
    }

    protected void fillResidenceInformation() {

	getStudentCandidacy().setCountryOfResidence(getResidenceInformationForm().getCountryOfResidence());
	getStudentCandidacy()
		.setDistrictSubdivisionOfResidence(getResidenceInformationForm().getDistrictSubdivisionOfResidence());
	getStudentCandidacy().setDislocatedFromPermanentResidence(
		getResidenceInformationForm().getDislocatedFromPermanentResidence());

	if (getResidenceInformationForm().getDislocatedFromPermanentResidence()) {
	    getStudentCandidacy().setSchoolTimeDistrictSubDivisionOfResidence(
		    getResidenceInformationForm().getSchoolTimeDistrictSubdivisionOfResidence());
	}

	final Person person = getStudentCandidacy().getPerson();

	setDefaultAddress(person);
	setSchoolTimeAddress(person);

    }

    private void setSchoolTimeAddress(final Person person) {
	if (getResidenceInformationForm().getDislocatedFromPermanentResidence()
		&& getResidenceInformationForm().isSchoolTimeAddressComplete()) {
	    final PhysicalAddressData physicalAddressData = new PhysicalAddressData(getResidenceInformationForm()
		    .getSchoolTimeAddress(), getResidenceInformationForm().getSchoolTimeAreaCode(), getResidenceInformationForm()
		    .getSchoolTimeAreaOfAreaCode(), getResidenceInformationForm().getSchoolTimeArea(),
		    getResidenceInformationForm().getSchoolTimeParishOfResidence(), getResidenceInformationForm()
			    .getSchoolTimeDistrictSubdivisionOfResidence().getName(), getResidenceInformationForm()
			    .getSchoolTimeDistrictSubdivisionOfResidence().getDistrict().getName(), Country.readDefault());
	    PhysicalAddress.createPhysicalAddress(person, physicalAddressData, PartyContactType.PERSONAL, false);
	}
    }

    private void setDefaultAddress(final Person person) {
	final PhysicalAddressData physicalAddressData = new PhysicalAddressData(getResidenceInformationForm().getAddress(),
		getResidenceInformationForm().getAreaCode(), getResidenceInformationForm().getAreaOfAreaCode(),
		getResidenceInformationForm().getArea(), getResidenceInformationForm().getParishOfResidence(),
		getResidenceInformationForm().getDistrictSubdivisionOfResidence().getName(), getResidenceInformationForm()
			.getDistrictSubdivisionOfResidence().getDistrict().getName(), getResidenceInformationForm()
			.getCountryOfResidence());
	person.setDefaultPhysicalAddressData(physicalAddressData);
    }

    protected void fillFiliation() {
	final Person person = getStudentCandidacy().getPerson();
	person.setNameOfFather(getFiliationForm().getFatherName());
	person.setNameOfMother(getFiliationForm().getMotherName());

	person.setDistrictOfBirth(getFiliationForm().getDistrictOfBirth());
	person.setDistrictSubdivisionOfBirth(getFiliationForm().getDistrictSubdivisionOfBirth());
	person.setParishOfBirth(getFiliationForm().getParishOfBirth());

	person.setDateOfBirthYearMonthDay(getFiliationForm().getDateOfBirth());
	person.setCountry(getFiliationForm().getNationality());
	person.setCountryOfBirth(getFiliationForm().getCountryOfBirth());
    }

    protected void fillPersonalInformation() {

	getStudentCandidacy().setGrantOwnerType(getPersonalInformationForm().getGrantOwnerType());
	getStudentCandidacy().setGrantOwnerProvider(getPersonalInformationForm().getGrantOwnerProvider());
	getStudentCandidacy().setProfessionalCondition(getPersonalInformationForm().getProfessionalCondition());
	getStudentCandidacy().setProfessionType(getPersonalInformationForm().getProfessionType());
	getStudentCandidacy().setMaritalStatus(getPersonalInformationForm().getMaritalStatus());

	final Person person = getStudentCandidacy().getPerson();
	person.setEmissionDateOfDocumentIdYearMonthDay(getPersonalInformationForm().getDocumentIdEmissionDate());
	person.setExpirationDateOfDocumentIdYearMonthDay(getPersonalInformationForm().getDocumentIdExpirationDate());
	person.setEmissionLocationOfDocumentId(getPersonalInformationForm().getDocumentIdEmissionLocation());
	person.setProfession(getPersonalInformationForm().getProfession());
	person.setSocialSecurityNumber(getPersonalInformationForm().getSocialSecurityNumber());
	person.setIdDocumentType(getPersonalInformationForm().getIdDocumentType());
	person.setMaritalStatus(getPersonalInformationForm().getMaritalStatus());
	person.setIdentificationDocumentSeriesNumber(getPersonalInformationForm().getIdentificationDocumentSeriesNumber());
	person.setIdentificationDocumentExtraDigit(getPersonalInformationForm().getIdentificationDocumentExtraDigit());
    }

}