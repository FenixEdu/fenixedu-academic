/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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
import net.sourceforge.fenixedu.domain.student.PersonalIngressionData;
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
        PersonalIngressionData personalData = getOrCreatePersonalIngressionData(getStudentCandidacy());
        personalData.setFatherProfessionalCondition(getHouseholdInformationForm().getFatherProfessionalCondition());
        personalData.setFatherProfessionType(getHouseholdInformationForm().getFatherProfessionType());
        personalData.setFatherSchoolLevel(getHouseholdInformationForm().getFatherSchoolLevel());
        personalData.setMotherProfessionalCondition(getHouseholdInformationForm().getMotherProfessionalCondition());
        personalData.setMotherProfessionType(getHouseholdInformationForm().getMotherProfessionType());
        personalData.setMotherSchoolLevel(getHouseholdInformationForm().getMotherSchoolLevel());
    }

    private void fillResidenceAppliance() {
        getStudentCandidacy().setApplyForResidence(getResidenceApplianceInquiryForm().isToApplyForResidence());
        getStudentCandidacy().setNotesAboutResidenceAppliance(
                getResidenceApplianceInquiryForm().getNotesAboutApplianceForResidence());
    }

    protected void fillOriginInformation() {
        final PrecedentDegreeInformation precedentDegreeInformation = getStudentCandidacy().getPrecedentDegreeInformation();
        final PersonalIngressionData personalData = getOrCreatePersonalIngressionData(precedentDegreeInformation);

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
        precedentDegreeInformation.setCountry(getOriginInformationForm().getCountryWhereFinishedPreviousCompleteDegree());

        personalData.setHighSchoolType(getOriginInformationForm().getHighSchoolType());

    }

    protected void fillPersonalDataAuthorizationChoice() {
        getStudentCandidacy().setStudentPersonalDataAuthorizationChoice(
                getInquiryAboutYieldingPersonalDataForm().getPersonalDataAuthorizationChoice());
        getStudentCandidacy().setStudentPersonalDataStudentsAssociationAuthorization(
                getInquiryAboutYieldingPersonalDataForm().getPersonalDataAuthorizationForStudentsAssociation());
    }

    protected void fillContacts() {
        final Person person = getStudentCandidacy().getPerson();
        person.setDefaultEmailAddressValue(getContactsForm().getEmail(), false, getContactsForm().isEmailAvailable());

        person.setDefaultPhoneNumber(getContactsForm().getPhoneNumber());

        person.setDefaultWebAddressUrl(getContactsForm().getWebAddress());
        if (person.hasDefaultWebAddress()) {
            person.getDefaultWebAddress().setVisibleToPublic(getContactsForm().isHomepageAvailable());
        }

        person.setDefaultMobilePhoneNumber(getContactsForm().getMobileNumber());
    }

    protected void fillResidenceInformation() {
        PersonalIngressionData personalData = getOrCreatePersonalIngressionData(getStudentCandidacy());
        personalData.setCountryOfResidence(getResidenceInformationForm().getCountryOfResidence());
        personalData.setDistrictSubdivisionOfResidence(getResidenceInformationForm().getDistrictSubdivisionOfResidence());
        personalData.setDislocatedFromPermanentResidence(getResidenceInformationForm().getDislocatedFromPermanentResidence());

        if (getResidenceInformationForm().getDislocatedFromPermanentResidence()) {
            personalData.setSchoolTimeDistrictSubDivisionOfResidence(getResidenceInformationForm()
                    .getSchoolTimeDistrictSubdivisionOfResidence());
        }

        final Person person = getStudentCandidacy().getPerson();

        setDefaultAddress(person);
        setSchoolTimeAddress(person);

    }

    private void setSchoolTimeAddress(final Person person) {
        if (getResidenceInformationForm().getDislocatedFromPermanentResidence()
                && getResidenceInformationForm().isSchoolTimeAddressComplete()) {
            final PhysicalAddressData physicalAddressData =
                    new PhysicalAddressData(getResidenceInformationForm().getSchoolTimeAddress(), getResidenceInformationForm()
                            .getSchoolTimeAreaCode(), getResidenceInformationForm().getSchoolTimeAreaOfAreaCode(),
                            getResidenceInformationForm().getSchoolTimeArea(), getResidenceInformationForm()
                                    .getSchoolTimeParishOfResidence(), getResidenceInformationForm()
                                    .getSchoolTimeDistrictSubdivisionOfResidence().getName(), getResidenceInformationForm()
                                    .getSchoolTimeDistrictSubdivisionOfResidence().getDistrict().getName(), Country.readDefault());
            final PhysicalAddress address =
                    PhysicalAddress.createPhysicalAddress(person, physicalAddressData, PartyContactType.PERSONAL, false);
            address.setValid();
        }
    }

    private void setDefaultAddress(final Person person) {
        String districtSubdivisionOfResidence =
                getResidenceInformationForm().getDistrictSubdivisionOfResidence() != null ? getResidenceInformationForm()
                        .getDistrictSubdivisionOfResidence().getName() : null;
        String districtOfResidence =
                getResidenceInformationForm().getDistrictSubdivisionOfResidence().getDistrict() != null ? getResidenceInformationForm()
                        .getDistrictSubdivisionOfResidence().getDistrict().getName() : null;
        final PhysicalAddressData physicalAddressData =
                new PhysicalAddressData(getResidenceInformationForm().getAddress(), getResidenceInformationForm().getAreaCode(),
                        getResidenceInformationForm().getAreaOfAreaCode(), getResidenceInformationForm().getArea(),
                        getResidenceInformationForm().getParishOfResidence(), districtSubdivisionOfResidence,
                        districtOfResidence, getResidenceInformationForm().getCountryOfResidence());
        person.setDefaultPhysicalAddressData(physicalAddressData, true);
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
        PersonalIngressionData personalData = getOrCreatePersonalIngressionData(getStudentCandidacy());
        personalData.setGrantOwnerType(getPersonalInformationForm().getGrantOwnerType());
        personalData.setGrantOwnerProvider(getPersonalInformationForm().getGrantOwnerProvider());
        personalData.setProfessionalCondition(getPersonalInformationForm().getProfessionalCondition());
        personalData.setProfessionType(getPersonalInformationForm().getProfessionType());
        personalData.setMaritalStatus(getPersonalInformationForm().getMaritalStatus());

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

    private PersonalIngressionData getOrCreatePersonalIngressionData(StudentCandidacy studentCandidacy) {
        return getOrCreatePersonalIngressionData(studentCandidacy.getPrecedentDegreeInformation());
    }

    private PersonalIngressionData getOrCreatePersonalIngressionData(PrecedentDegreeInformation precedentInformation) {
        PersonalIngressionData personalData = null;
        personalData = precedentInformation.getPersonalIngressionData();
        if (personalData == null) {
            personalData =
                    getPerson().getStudent().getPersonalIngressionDataByExecutionYear(ExecutionYear.readCurrentExecutionYear());
            if (personalData != null) {
                //if the student already has a PID it will have another PDI associated, it's necessary to add the new PDI
                personalData.addPrecedentDegreesInformations(precedentInformation);
            } else {
                personalData = new PersonalIngressionData(ExecutionYear.readCurrentExecutionYear(), precedentInformation);
            }
        }
        return personalData;
    }
}