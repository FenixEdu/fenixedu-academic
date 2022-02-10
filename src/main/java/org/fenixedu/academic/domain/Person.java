/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.accounting.AcademicEvent;
import org.fenixedu.academic.domain.accounting.CustomEvent;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventTemplate;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.accounting.ServiceAgreement;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeEvent;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.DegreeCandidacy;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPerson;
import org.fenixedu.academic.domain.candidacyProcess.over23.Over23IndividualCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.standalone.StandaloneIndividualCandidacy;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.documents.AnnualIRSDeclarationDocument;
import org.fenixedu.academic.domain.documents.GeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.IdDocument;
import org.fenixedu.academic.domain.person.IdDocumentTypeObject;
import org.fenixedu.academic.domain.person.MaritalStatus;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.phd.alert.PhdAlertMessage;
import org.fenixedu.academic.domain.phd.candidacy.PHDProgramCandidacy;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.json.JsonUtils;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.i18n.LocalizedString.Builder;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import org.fenixedu.messaging.core.domain.Sender;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.YearMonthDay;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.ist.fenixframework.Atomic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fenixedu.academic.predicate.AccessControl.check;

public class Person extends Person_Base {

    private static final Integer MAX_VALIDATION_REQUESTS = 5;

    private IdDocument getIdDocument() {
        final Iterator<IdDocument> documentIterator = getIdDocumentsSet().iterator();
        return documentIterator.hasNext() ? documentIterator.next() : null;
    }

    @Override
    public void setUser(User user) {
        super.setUser(user);
        if (getProfile() != null) {
            getProfile().setAvatarUrl(
                    CoreConfiguration.getConfiguration().applicationUrl() + "/user/photo/" + getUser().getUsername());
        }
    }

    @Override
    public LocalizedString getPartyName() {
        Builder builder = new LocalizedString.Builder();
        for (Locale locale : CoreConfiguration.supportedLocales()) {
            builder.with(locale, getName());
        }
        return builder.build();
    }

    @Override
    public String getName() {
        return getProfile().getFullName();
    }

    /**
     * @deprecated Use {@link UserProfile#getGivenNames()}
     */
    @Deprecated
    public String getGivenNames() {
        return getProfile().getGivenNames();
    }

    /**
     * @deprecated Use {@link UserProfile#getFamilyNames()}
     */
    @Deprecated
    public String getFamilyNames() {
        return getProfile().getFamilyNames();
    }

    @Override
    public void setDocumentIdNumber(final String documentIdNumber) {
        if (documentIdNumber == null || Strings.isNullOrEmpty(documentIdNumber)) {
            throw new DomainException("error.person.empty.documentIdNumber");
        }
        IdDocument idDocument = getIdDocument();
        if (idDocument == null) {
            idDocument = new IdDocument(this, documentIdNumber, (IdDocumentTypeObject) null);
        } else {
            idDocument.setValue(documentIdNumber);
        }
        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getDocumentIdNumber(), documentIdNumber,
                "label.documentNumber");
        super.setDocumentIdNumber(documentIdNumber);
    }

    @Override
    public void setIdDocumentType(final IDDocumentType idDocumentType) {
        if (idDocumentType == null) {
            throw new DomainException("error.person.empty.idDocumentType");
        }
        IdDocument idDocument = getIdDocument();
        if (idDocument == null) {
            idDocument = new IdDocument(this, null, idDocumentType);
        } else {
            idDocument.setIdDocumentType(idDocumentType);
        }

        logSetterNullEnum("log.personInformation.edit.generalTemplate.personalId", getIdDocumentType(), idDocumentType,
                "label.documentIdType");
        super.setIdDocumentType(idDocumentType);
    }

    public void setIdentification(String documentIdNumber, final IDDocumentType idDocumentType) {
        documentIdNumber = StringUtils.trimToNull(documentIdNumber);
        if (documentIdNumber != null && idDocumentType != null
                && checkIfDocumentNumberIdAndDocumentIdTypeExists(documentIdNumber, idDocumentType)) {
            throw new DomainException("error.person.existent.docIdAndType");
        }
        setDocumentIdNumber(documentIdNumber);
        setIdDocumentType(idDocumentType);
    }

    public void setIdentificationAndNames(String documentIdNumber, final IDDocumentType idDocumentType, final String givenNames,
            final String familyNames) {
        getProfile().changeName(givenNames, familyNames, null);
        setIdentification(documentIdNumber, idDocumentType);
    }

    public void setGivenNames(String newGivenNames) {
        UserProfile profile = getProfile();
        profile.changeName(newGivenNames, profile.getFamilyNames(), profile.getDisplayName());
    }

    public String getDisplayName() {
        return getProfile().getDisplayName();
    }

    public void setDisplayName(String newDisplayName) {
        UserProfile profile = getProfile();
        try {
            profile.changeName(profile.getGivenNames(), profile.getFamilyNames(), newDisplayName);
        } catch (BennuCoreDomainException ex) {
            throw new DomainException("error.invalid.displayName", ex.getLocalizedMessage());
        }
    }

    public void setFamilyNames(String newFamilyNames) {
        UserProfile profile = getProfile();
        profile.changeName(profile.getGivenNames(), newFamilyNames, profile.getDisplayName());
    }

    public void setNames(String newGivenName, String newFamilyName, String newDisplayName) {
        UserProfile profile = getProfile();
        try {
            profile.changeName(newGivenName, newFamilyName, newDisplayName);
        } catch (BennuCoreDomainException ex) {
            throw new DomainException("error.invalid.displayName", ex.getLocalizedMessage());
        }
    }

    private boolean checkIfDocumentNumberIdAndDocumentIdTypeExists(final String documentIDNumber,
            final IDDocumentType documentType) {
        final Person person = readByDocumentIdNumberAndIdDocumentType(documentIDNumber, documentType);
        return person != null && !person.equals(this);
    }

    final public String getValidatedName() {
        return StringFormatter.prettyPrint(getName());
    }

    /**
     * Creates a new Person, associated to the given {@link UserProfile}.
     * 
     * Note that this constructor does NOT create a {@link User}.
     * 
     * @param profile
     *            The profile to associate with the created person.
     */
    public Person(UserProfile profile) {
        super();
        setProfile(profile);
        if (profile.getUser() != null) {
            setUser(profile.getUser());
        }
        setMaritalStatus(MaritalStatus.UNKNOWN);
    }

    /**
     * Creates a new Person and its correspondent {@link UserProfile}, using the data provided
     * in the parameter bean.
     * 
     * Note that this constructor does NOT create a {@link User}.
     * 
     * @param personBean
     *            The bean containing information about the person to be created.
     */
    public Person(final PersonBean personBean) {
        this(personBean, false);
    }

    /**
     * Creates a new Person and its correspondent {@link UserProfile}, using the data provided
     * in the parameter bean. It also allows the caller to specify whether the email is to be automatically validated by the
     * system.
     * 
     * Note that this constructor does NOT create a {@link User}.
     * 
     * @param personBean
     *            The bean containing information about the person to be created.
     * @param validateEmail
     *            Whether to automatically validate the given email.
     */
    public Person(final PersonBean personBean, final boolean validateEmail) {
        this(new UserProfile(personBean.getGivenNames(), personBean.getFamilyNames(), null, personBean.getEmail(),
                Locale.getDefault()));

        setProperties(personBean);

        PhysicalAddress.createPhysicalAddress(this, personBean.getPhysicalAddressData(), PartyContactType.PERSONAL, true);
        Phone.createPhone(this, personBean.getPhone(), PartyContactType.PERSONAL, true);
        MobilePhone.createMobilePhone(this, personBean.getMobile(), PartyContactType.PERSONAL, true);
        final EmailAddress emailAddress =
                EmailAddress.createEmailAddress(this, personBean.getEmail(), PartyContactType.PERSONAL, true);
        if (validateEmail) {
            emailAddress.setValid();
        }
        WebAddress.createWebAddress(this, personBean.getWebAddress(), PartyContactType.PERSONAL, true);
    }

    /**
     * Creates a new Person and its correspondent {@link UserProfile} and optionally a {@link User}, using the data provided in
     * the personal details.
     * 
     * @param candidacyPersonalDetails
     *            The personal details containing information about the person to be created.
     * @param createUser true if a user is to be created, false otherwise.
     */
    public Person(final IndividualCandidacyPersonalDetails candidacyPersonalDetails, boolean createUser) {
        this(new UserProfile(candidacyPersonalDetails.getGivenNames(), candidacyPersonalDetails.getFamilyNames(), null,
                candidacyPersonalDetails.getEmail(), Locale.getDefault()));
        if (createUser) {
            setUser(new User(getProfile()));
        }

        this.setCountry(candidacyPersonalDetails.getCountry());
        this.setDateOfBirthYearMonthDay(candidacyPersonalDetails.getDateOfBirthYearMonthDay());
        this.setDocumentIdNumber(candidacyPersonalDetails.getDocumentIdNumber());
        this.setExpirationDateOfDocumentIdYearMonthDay(candidacyPersonalDetails.getExpirationDateOfDocumentIdYearMonthDay());
        this.setGender(candidacyPersonalDetails.getGender());
        this.setIdDocumentType(candidacyPersonalDetails.getIdDocumentType());
        this.setSocialSecurityNumber(candidacyPersonalDetails.getSocialSecurityNumber());

        final PhysicalAddressData physicalAddressData =
                new PhysicalAddressData(candidacyPersonalDetails.getAddress(), candidacyPersonalDetails.getAreaCode(), "",
                        candidacyPersonalDetails.getArea(), "", "", "", candidacyPersonalDetails.getCountryOfResidence());
        PhysicalAddress.createPhysicalAddress(this, physicalAddressData, PartyContactType.PERSONAL, true);
        Phone.createPhone(this, candidacyPersonalDetails.getTelephoneContact(), PartyContactType.PERSONAL, true);
        EmailAddress.createEmailAddress(this, candidacyPersonalDetails.getEmail(), PartyContactType.PERSONAL, true);
    }

    /**
     * Creates a new Person and its correspondent {@link UserProfile} and {@link User}, using the data provided in the personal
     * details.
     * 
     * @param candidacyPersonalDetails
     *            The personal details containing information about the person to be created.
     */
    public Person(final IndividualCandidacyPersonalDetails candidacyPersonalDetails) {
        this(candidacyPersonalDetails, true);
    }

    public void ensureOpenUserAccount() {
        if (getUser() == null) {
            setUser(new User(getProfile()));
        }
        getUser().openLoginPeriod();
    }

    public void ensureUserAccount() {
        if (getUser() == null) {
            setUser(new User(getProfile()));
        }
    }

    public Person editPersonalInformation(final PersonBean personBean) {
        check(this, AcademicPredicates.EDIT_STUDENT_PERSONAL_DATA);
        setProperties(personBean);
        return this;
    }

    public Person editByPublicCandidate(final PersonBean personBean) {
        getProfile().changeName(personBean.getGivenNames(), personBean.getFamilyNames(), null);
        setGender(personBean.getGender());
        setIdentification(personBean.getDocumentIdNumber(), personBean.getIdDocumentType());
        setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
        setSocialSecurityNumber(personBean.getSocialSecurityNumber());
        setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
        setCountry(personBean.getNationality());
        setDefaultPhysicalAddressData(personBean.getPhysicalAddressData());
        setDefaultPhoneNumber(personBean.getPhone());
        setDefaultEmailAddressValue(personBean.getEmail(), true);
        setDefaultMobilePhoneNumber(personBean.getMobile());
        return this;
    }

    public Person edit(final IndividualCandidacyPersonalDetails candidacyExternalDetails) {
        this.setCountry(candidacyExternalDetails.getCountry());

        this.setDateOfBirthYearMonthDay(candidacyExternalDetails.getDateOfBirthYearMonthDay());
        this.setIdentification(candidacyExternalDetails.getDocumentIdNumber(), candidacyExternalDetails.getIdDocumentType());
        this.setExpirationDateOfDocumentIdYearMonthDay(candidacyExternalDetails.getExpirationDateOfDocumentIdYearMonthDay());
        this.setGender(candidacyExternalDetails.getGender());
        getProfile().changeName(candidacyExternalDetails.getGivenNames(), candidacyExternalDetails.getFamilyNames(), null);
        this.setSocialSecurityNumber(candidacyExternalDetails.getSocialSecurityNumber());

        final PhysicalAddressData physicalAddressData =
                new PhysicalAddressData(candidacyExternalDetails.getAddress(), candidacyExternalDetails.getAreaCode(),
                        getAreaOfAreaCode(), candidacyExternalDetails.getArea(), getParishOfResidence(),
                        getDistrictSubdivisionOfResidence(), getDistrictOfResidence(),
                        candidacyExternalDetails.getCountryOfResidence());
        setDefaultPhysicalAddressData(physicalAddressData);
        setDefaultPhoneNumber(candidacyExternalDetails.getTelephoneContact());
        setDefaultEmailAddressValue(candidacyExternalDetails.getEmail());

        return this;
    }

    public Person editPersonWithExternalData(final PersonBean personBean, final boolean updateExistingContacts) {

        setProperties(personBean);
        setDefaultPhysicalAddressData(personBean.getPhysicalAddressData());

        if (updateExistingContacts) {
            setDefaultPhoneNumber(personBean.getPhone());
            setDefaultMobilePhoneNumber(personBean.getMobile());
            setDefaultWebAddressUrl(personBean.getWebAddress());
            setDefaultEmailAddressValue(personBean.getEmail());
        } else {
            Phone.createPhone(this, personBean.getPhone(), PartyContactType.PERSONAL, !hasDefaultPhone());
            MobilePhone.createMobilePhone(this, personBean.getMobile(), PartyContactType.PERSONAL, !hasDefaultMobilePhone());
            EmailAddress.createEmailAddress(this, personBean.getEmail(), PartyContactType.PERSONAL, !hasDefaultEmailAddress());
            WebAddress.createWebAddress(this, personBean.getWebAddress(), PartyContactType.PERSONAL, !hasDefaultWebAddress());
        }

        return this;
    }

    public String getUsername() {
        User user = getUser();
        return user == null ? null : user.getUsername();
    }

    public Registration getStudentByType(final DegreeType degreeType) {
        for (final Registration registration : this.getStudents()) {
            if (registration.getDegreeType() == degreeType) {
                return registration;
            }
        }
        return null;
    }

    private String valueToUpdateIfNewNotNull(final String actualValue, final String newValue) {

        if (newValue == null || newValue.length() == 0) {
            return actualValue;
        }
        return newValue;

    }

    private Object valueToUpdateIfNewNotNull(final Object actualValue, final Object newValue) {

        if (newValue == null) {
            return actualValue;
        }
        return newValue;

    }

    private void setProperties(final PersonBean personBean) {

        getProfile().changeName(personBean.getGivenNames(), personBean.getFamilyNames(), null);

        setGender(personBean.getGender());
        setProfession(personBean.getProfession());
        setMaritalStatus(personBean.getMaritalStatus());

        // identification
        setIdentification(personBean.getDocumentIdNumber(), personBean.getIdDocumentType());
        setIdentificationDocumentSeriesNumber(personBean.getIdentificationDocumentSeriesNumber());
        setEmissionLocationOfDocumentId(personBean.getDocumentIdEmissionLocation());
        setEmissionDateOfDocumentIdYearMonthDay(personBean.getDocumentIdEmissionDate());
        setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
        setSocialSecurityNumber(personBean.getSocialSecurityNumber());
        setEidentifier(personBean.getEidentifier());

        // filiation
        setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
        setCountry(personBean.getNationality());
        setParishOfBirth(personBean.getParishOfBirth());
        setDistrictSubdivisionOfBirth(personBean.getDistrictSubdivisionOfBirth());
        setDistrictOfBirth(personBean.getDistrictOfBirth());
        setCountryOfBirth(personBean.getCountryOfBirth());
        setNameOfMother(personBean.getMotherName());
        setNameOfFather(personBean.getFatherName());
    }

    /**
     * @return a group that only contains this person
     */
    public Group getPersonGroup() {
        return this.getUser().groupOf();
    }

    /**
     * 
     * IMPORTANT: This method is evil and should NOT be used! You are NOT God!
     * 
     **/
    @Override
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        if (getPersonalPhotoEvenIfRejected() != null) {
            getPersonalPhotoEvenIfRejected().delete();
        }
        if (getAssociatedPersonAccount() != null) {
            getAssociatedPersonAccount().delete();
        }

        if (getStudent() != null) {
            getStudent().delete();
        }

        if (super.getSender() != null) {
            final Sender sender = super.getSender();
            setSender(null);
            sender.delete();
        }

        getThesisEvaluationParticipantsSet().clear();

        for (; !getIdDocumentsSet().isEmpty(); getIdDocumentsSet().iterator().next().delete()) {
            ;
        }
        for (; !getScientificCommissionsSet().isEmpty(); getScientificCommissionsSet().iterator().next().delete()) {
            ;
        }

        super.setCountry(null);
        super.setCountryOfBirth(null);
        setProfile(null);
        super.setUser(null);
        super.delete();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!(getPartyContactsSet().isEmpty() && getChildsSet().isEmpty() && getParentsSet().isEmpty()
                && getExportGroupingReceiversSet().isEmpty() && getAssociatedQualificationsSet().isEmpty()
                && getAssociatedAlteredCurriculumsSet().isEmpty() && getEnrolmentEvaluationsSet().isEmpty()
                && getExportGroupingSendersSet().isEmpty() && getResponsabilityTransactionsSet().isEmpty()
                && getGuidesSet().isEmpty() && getTeacher() == null && getInternalParticipantsSet().isEmpty()
                && getCreatedQualificationsSet().isEmpty() && getCreateJobsSet().isEmpty())) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
        }
    }

    public boolean getDisableSendEmails(){
        return MessagingSystem.getInstance().isOptedOut(getUser());
    }

    public void setDisableSendEmails(boolean disableSendEmails) {
        if (disableSendEmails){
            MessagingSystem.getInstance().optOut(getUser());
        }
        else {
            MessagingSystem.getInstance().optIn(getUser());
        }
        getProfile().setEmail(getEmailForSendingEmails());
    }

    public boolean hasDegreeCandidacyForExecutionDegree(final ExecutionDegree executionDegree) {
        for (final Candidacy candidacy : this.getCandidaciesSet()) {
            if (candidacy instanceof DegreeCandidacy && candidacy.isActive()) {
                final DegreeCandidacy degreeCandidacy = (DegreeCandidacy) candidacy;
                if (degreeCandidacy.getExecutionDegree().equals(executionDegree)) {
                    return true;
                }
            }
        }
        return false;
    }

    public StudentCandidacy getStudentCandidacyForExecutionDegree(final ExecutionDegree executionDegree) {
        for (final Candidacy candidacy : this.getCandidaciesSet()) {
            if (candidacy instanceof StudentCandidacy && candidacy.isActive()) {
                if (candidacy instanceof PHDProgramCandidacy) {
                    continue;
                }

                final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
                if (studentCandidacy.getExecutionDegree().equals(executionDegree)) {
                    return studentCandidacy;
                }
            }
        }
        return null;
    }

    public boolean hasStudentCandidacyForExecutionDegree(final ExecutionDegree executionDegree) {
        return getStudentCandidacyForExecutionDegree(executionDegree) != null;
    }

    public static Person readPersonByUsername(final String username) {
        final User user = User.findByUsername(username);
        return user == null ? null : user.getPerson();
    }

    public static Collection<Person> readByDocumentIdNumber(final String documentIdNumber) {
        final Collection<Person> result = new HashSet<>();
        for (final IdDocument idDocument : IdDocument.find(documentIdNumber)) {
            result.add(idDocument.getPerson());
        }
        return result;
    }

    public static Person readByDocumentIdNumberAndIdDocumentType(final String documentIdNumber,
            final IDDocumentType idDocumentType) {
        final IdDocument document = IdDocument.findFirst(documentIdNumber, idDocumentType);
        return document == null ? null : document.getPerson();
    }

    public static Collection<Person> findByDateOfBirth(final YearMonthDay dateOfBirth, final Collection<Person> persons) {
        final List<Person> result = new ArrayList<>();
        for (final Person person : persons) {
            if (person.getDateOfBirthYearMonthDay() == null || person.getDateOfBirthYearMonthDay().equals(dateOfBirth)) {
                result.add(person);
            }
        }
        return result;
    }

    public static Stream<Person> findPersonStream(final String name, final int size) {
        return UserProfile.searchByName(name, size).map(p -> p.getPerson()).filter(Objects::nonNull);
    }

    public static Collection<Person> findPerson(final String name, final int size) {
        return findPersonStream(name, size).collect(Collectors.toSet());
    }

    public static Collection<Person> readPersonsByNameAndRoleType(final String name, final RoleType roleType) {
        final Collection<Person> people = findPerson(name);
        people.removeIf(person -> !roleType.isMember(person.getUser()));
        return people;
    }

    public SortedSet<StudentCurricularPlan> getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName() {
        final SortedSet<StudentCurricularPlan> studentCurricularPlans =
                new TreeSet<>(StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME);
        for (final Registration registration : getStudentsSet()) {
            final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
            if (studentCurricularPlan != null) {
                studentCurricularPlans.add(studentCurricularPlan);
            }
        }
        return studentCurricularPlans;
    }

    public Set<Attends> getCurrentAttends() {
        final Set<Attends> attends = new HashSet<>();
        for (final Registration registration : getStudentsSet()) {
            for (final Attends attend : registration.getAssociatedAttendsSet()) {
                final ExecutionCourse executionCourse = attend.getExecutionCourse();
                final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
                if (executionSemester.getState().equals(PeriodState.CURRENT)) {
                    attends.add(attend);
                }
            }
        }
        return attends;
    }

    private Set<Event> getEventsFromType(final Class<? extends Event> clazz) {
        final Set<Event> events = new HashSet<>();

        for (final Event event : getEventsSet()) {
            if (clazz.isAssignableFrom(event.getClass())) {
                events.add(event);
            }
        }

        return events;
    }

    public Set<Event> getAcademicEvents() {
        return getEventsFromType(AcademicEvent.class);
    }

    public Set<Event> getResidencePaymentEvents() {
        return getEventsFromType(ResidenceEvent.class);
    }

    public Set<Event> getNotPayedEventsPayableOn(final AdministrativeOffice administrativeOffice, final Class eventClass,
            final boolean withInstallments) {
        final Set<Event> result = new HashSet<>();

        Set<Event> events = getEventsFromType(eventClass);
        for (final Event event : events) {
            if (event.isOpen() && event.hasInstallments() == withInstallments
                    && isPayableOnAnyOfAdministrativeOffices(Collections.singleton(administrativeOffice), event)) {
                result.add(event);
            }
        }

        return result;
    }

    public Set<Event> getNotPayedEventsPayableOn(final AdministrativeOffice administrativeOffice, final boolean withInstallments) {
        return getNotPayedEventsPayableOn(administrativeOffice, AcademicEvent.class, withInstallments);
    }

    public Set<Event> getNotPayedEvents() {
        final Set<Event> result = new HashSet<>();
        for (final Event event : getAcademicEvents()) {
            if (event.isOpen()) {
                result.add(event);
            }
        }

        return result;
    }

    private boolean isPayableOnAnyOfAdministrativeOffices(final Set<AdministrativeOffice> administrativeOffices, final Event event) {

        if (administrativeOffices == null) {
            return true;
        }

        for (final AdministrativeOffice administrativeOffice : administrativeOffices) {
            if (administrativeOffice == null || event.isPayableOnAdministrativeOffice(administrativeOffice)) {
                return true;
            }
        }

        return false;
    }

    public List<Event> getPayedEvents(final Class eventClass) {
        final List<Event> result = new ArrayList<>();
        Set<Event> events = getEventsFromType(eventClass);
        for (final Event event : events) {
            if (event.isClosed()) {
                result.add(event);
            }
        }

        return result;
    }

    public List<Event> getPayedEvents() {
        return getPayedEvents(AcademicEvent.class);
    }

    public List<Event> getEventsWithPayments() {
        final List<Event> result = new ArrayList<>();
        for (final Event event : getAcademicEvents()) {
            if (!event.isCancelled() && event.hasAnyPayments()) {
                result.add(event);
            }
        }

        return result;
    }

    public Set<Entry> getPaymentsWithoutReceipt() {
        return getPaymentsWithoutReceiptByAdministrativeOffices(null);
    }

    public Set<Entry> getPaymentsWithoutReceiptByAdministrativeOffices(final Set<AdministrativeOffice> administrativeOffices) {
        final Set<Entry> result = new HashSet<>();

        for (final Event event : getAcademicEvents()) {
            if (!event.isCancelled() && isPayableOnAnyOfAdministrativeOffices(administrativeOffices, event)) {
                result.addAll(event.getEntriesWithoutReceipt());
            }
        }

        return result;
    }

    public Set<Entry> getPayments(final Class eventClass) {
        final Set<Entry> result = new HashSet<>();
        Set<Event> events = getEventsFromType(eventClass);
        for (final Event event : events) {
            if (!event.isCancelled()) {
                result.addAll(event.getPositiveEntries());
            }
        }
        return result;
    }

    public Set<Entry> getPayments() {
        return getPayments(AcademicEvent.class);
    }

    public Money getTotalPaymentsAmountWithAdjustment() {
        Money total = new Money(0);
        for (final Entry entry : getPayments(AcademicEvent.class)) {
            total = total.add(entry.getAmountWithAdjustment());
        }
        return total;
    }

    public Set<? extends Event> getEventsByEventTypes(final Collection<EventType> eventTypes) {

        final Set<Event> result = new HashSet<>();

        for (final EventType eventType : eventTypes) {
            for (final Event event : getAcademicEvents()) {
                if (!event.isCancelled() && event.getEventType() == eventType) {
                    result.add(event);
                }
            }
        }

        return result;

    }

    public Set<? extends Event> getEventsByEventType(final EventType eventType) {
        return getEventsByEventTypeAndClass(eventType, null);
    }

    public Set<? extends Event> getEventsByEventTypeAndClass(final EventType eventType, final Class<? extends Event> clazz) {
        final Set<Event> result = new HashSet<>();

        for (final Event event : getEventsSet()) {
            if (!event.isCancelled() && event.getEventType() == eventType && (clazz == null || event.getClass().equals(clazz))) {
                result.add(event);
            }
        }

        return result;
    }

    public Set<AnnualEvent> getAnnualEventsFor(final ExecutionYear executionYear) {

        return getEventsSet().stream()
                .filter(event -> event instanceof AnnualEvent)
                .map(event -> (AnnualEvent) event)
                .filter(annualEvent -> annualEvent.isFor(executionYear) && !annualEvent.isCancelled())
                .collect(Collectors.toSet());
    }

    public boolean hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(final ExecutionYear executionYear) {
        return hasInsuranceEventFor(executionYear) || hasAdministrativeOfficeFeeInsuranceEventFor(executionYear);
    }

    public Set<Event> getNotCancelledInsuranceEvents() {
        final Stream<InsuranceEvent> insuranceEventStream = getEventsByEventType(EventType.INSURANCE).stream()
                .map(event -> (InsuranceEvent) event)
                .filter(specificEvent -> !specificEvent.isCancelled());

        final Stream<CustomEvent> customEventStream = getEventsByEventType(EventType.CUSTOM).stream()
                .map(CustomEvent.class::cast)
                .filter(event -> !event.isCancelled())
                .filter(event -> EventTemplate.Type.INSURANCE.name().equals(JsonUtils.get(event.getConfigObject(), "type")));

        return Streams.concat(insuranceEventStream, customEventStream).collect(Collectors.toSet());
    }

    public Event getInsuranceEventFor(final ExecutionYear executionYear) {
        final Stream<InsuranceEvent> insuranceEventStream = getEventsByEventType(EventType.INSURANCE).stream()
                .map(event -> (InsuranceEvent) event)
                .filter(insuranceEvent -> !insuranceEvent.isCancelled())
                .filter(insuranceEvent -> insuranceEvent.isFor(executionYear));
        final Stream<CustomEvent> customEventStream = getEventsByEventType(EventType.CUSTOM).stream()
                .map(CustomEvent.class::cast)
                .filter(event -> !event.isCancelled())
                .filter(event -> EventTemplate.Type.INSURANCE.name().equals(JsonUtils.get(event.getConfigObject(), "type")))
                .filter(event -> {
                    final ExecutionYear insuranceYear = JsonUtils.toDomainObject(event.getConfigObject(), "executionYear");
                    return executionYear == insuranceYear;
                });
        return Streams.concat(insuranceEventStream, customEventStream).findAny().orElse(null);
    }

    public boolean hasInsuranceEventFor(final ExecutionYear executionYear) {
        return getInsuranceEventFor(executionYear) != null;
    }

    public Stream<Event> getInsuranceEventsUntil(final ExecutionYear executionYear) {
        final Stream<InsuranceEvent> insuranceEventStream = getEventsByEventType(EventType.INSURANCE).stream()
                .map(event -> (InsuranceEvent) event)
                .filter(event -> !event.isCancelled())
                .filter(insuranceEvent -> !insuranceEvent.getExecutionYear().isAfter(executionYear));
        final Stream<CustomEvent> customEventStream = getEventsByEventType(EventType.CUSTOM).stream()
                .map(CustomEvent.class::cast)
                .filter(event -> !event.isCancelled())
                .filter(event -> EventTemplate.Type.INSURANCE.name().equals(JsonUtils.get(event.getConfigObject(), "type")))
                .filter(event -> {
                    final ExecutionYear eventExecutionYear = JsonUtils.toDomainObject(event.getConfigObject(), "executionYear");
                    return !eventExecutionYear.isAfter(executionYear);
                });
        return Streams.concat(insuranceEventStream, customEventStream);
    }

    public boolean hasAnyInsuranceDebtUntil(final ExecutionYear executionYear) {
        return getInsuranceEventsUntil(executionYear)
                .anyMatch(insuranceEvent -> insuranceEvent.isInDebt());
    }

    public boolean hasAnyGratuityDebtUntil(final ExecutionYear executionYear) {
        final boolean anyMatch = getEventsSet().stream()
                .filter(event -> event.isGratuity())
                .filter(gratuityEvent -> !gratuityEvent.executionYearOf().isAfter(executionYear))
                .anyMatch(event -> event.isInDebt());
        if (anyMatch) {
            return true;
        } else {
            return getEventsByEventType(EventType.CUSTOM).stream()
                    .map(CustomEvent.class::cast)
                    .filter(event -> !event.isCancelled())
                    .filter(event -> EventTemplate.Type.TUITION.name().equals(JsonUtils.get(event.getConfigObject(), "type")))
                    .anyMatch(event -> {
                        final ExecutionYear eventExecutionYear = JsonUtils.toDomainObject(event.getConfigObject(), "executionYear");
                        return !eventExecutionYear.isAfter(executionYear) && event.isInDebt();
                    });
        }
    }

    public Set<AdministrativeOfficeFeeAndInsuranceEvent> getNotCancelledAdministrativeOfficeFeeAndInsuranceEvents(
            final AdministrativeOffice office) {
        return getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE).stream()
                .map(event -> (AdministrativeOfficeFeeAndInsuranceEvent) event)
                .filter(specificEvent -> !specificEvent.isCancelled())
                .filter(specificEvent -> specificEvent.getAdministrativeOffice() == office)
                .collect(Collectors.toSet());
    }

    public Set<AdministrativeOfficeFeeAndInsuranceEvent> getNotCancelledAdministrativeOfficeFeeAndInsuranceEventsUntil(
            final AdministrativeOffice office, final ExecutionYear executionYear) {
        return getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE).stream()
                .map(event -> (AdministrativeOfficeFeeAndInsuranceEvent) event)
                .filter(specificEvent -> !specificEvent.isCancelled())
                .filter(specificEvent -> specificEvent.getAdministrativeOffice() == office)
                .filter(specificEvent -> specificEvent.getExecutionYear().isBeforeOrEquals(executionYear))
                .collect(Collectors.toSet());
    }

    public AdministrativeOfficeFeeAndInsuranceEvent getAdministrativeOfficeFeeInsuranceEventFor(final ExecutionYear executionYear) {
        return getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE).stream()
                .map(event -> (AdministrativeOfficeFeeAndInsuranceEvent) event)
                .filter(administrativeOfficeFeeAndInsuranceEvent -> !administrativeOfficeFeeAndInsuranceEvent.isCancelled())
                .filter(administrativeOfficeFeeAndInsuranceEvent -> administrativeOfficeFeeAndInsuranceEvent.isFor(executionYear))
                .findAny().orElse(null);
    }

    public boolean hasAdministrativeOfficeFeeInsuranceEventFor(final ExecutionYear executionYear) {
        return getAdministrativeOfficeFeeInsuranceEventFor(executionYear) != null || (hasInsuranceEventFor(executionYear) && hasAdministrativeOfficeFeeEventFor(executionYear));
    }

    private boolean hasAdministrativeOfficeFeeEventFor(final ExecutionYear executionYear) {
        final boolean anyMatch = getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE).stream()
                .map(event -> (AdministrativeOfficeFeeEvent) event)
                .filter(administrativeOfficeFeeEvent -> !administrativeOfficeFeeEvent.isCancelled())
                .anyMatch(administrativeOfficeFeeEvent -> administrativeOfficeFeeEvent.isFor(executionYear));

        if (anyMatch) {
            return true;
        } else {
            return getEventsByEventType(EventType.CUSTOM).stream()
                    .map(CustomEvent.class::cast)
                    .filter(event -> !event.isCancelled())
                    .filter(event -> EventTemplate.Type.ADMIN_FEES.name().equals(JsonUtils.get(event.getConfigObject(), "type")))
                    .anyMatch(event -> {
                        final String executionYearID = event.getConfigObject().get("executionYear").getAsString();
                        return executionYear.getExternalId().equals(executionYearID);
                    });
        }
    }

    public Stream<Event> getAdministrativeOfficeFeeEventsUntil(final ExecutionYear executionYear) {
        final Stream<AdministrativeOfficeFeeEvent> eventStream = getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE).stream()
                .filter(event -> !event.isCancelled())
                .map(event -> (AdministrativeOfficeFeeEvent) event)
                .filter(administrativeOfficeFeeEvent -> !administrativeOfficeFeeEvent.getExecutionYear().isAfter(executionYear));

        final Stream<CustomEvent> customEventStream = getEventsByEventType(EventType.CUSTOM).stream()
                .map(CustomEvent.class::cast)
                .filter(event -> !event.isCancelled())
                .filter(event -> EventTemplate.Type.ADMIN_FEES.name().equals(JsonUtils.get(event.getConfigObject(), "type")))
                .filter(event -> {
                    final ExecutionYear eventExecutionYear = JsonUtils.toDomainObject(event.getConfigObject(), "executionYear");
                    return !eventExecutionYear.isAfter(executionYear);
                });
        return Streams.concat(eventStream, customEventStream);
    }

    public boolean hasAnyAdministrativeOfficeFeeDebtUntil(final ExecutionYear executionYear) {
        return getAdministrativeOfficeFeeEventsUntil(executionYear)
                    .anyMatch(event -> event.isInDebt());
    }

    public Set<Event> getEventsSupportingPaymentByOtherParties() {
        return getEventsSet().stream()
                .filter(event -> !event.isCancelled())
                .filter(Event::isOtherPartiesPaymentsSupported)
                .collect(Collectors.toSet());
    }

    public List<PaymentCode> getPaymentCodesBy(final PaymentCodeType paymentCodeType) {
        return getPaymentCodesSet().stream().filter(paymentCode -> paymentCode.getType() == paymentCodeType).collect(Collectors.toList());
    }

    public PaymentCode getPaymentCodeBy(final String code) {
        return getPaymentCodesSet().stream().filter(paymentCode -> paymentCode.getCode().equals(code)).findFirst().orElse(null);
    }

    public List<Event> getEventsWithExemptionAppliable() {
        return getEventsSet().stream().filter(e -> !e.isCancelled()).collect(Collectors.toList());
    }

    public Money getMaxDeductableAmountForLegalTaxes(final EventType eventType, final int civilYear) {
        Money result = Money.ZERO;
        for (final Event event : (Set<Event>) getEventsByEventType(eventType)) {
            result = result.add(event.getMaxDeductableAmountForLegalTaxes(civilYear));
        }

        return result;
    }

    public Set<Receipt> getReceiptsByAdministrativeOffices(final Set<AdministrativeOffice> administrativeOffices) {
        final Set<Receipt> result = new HashSet<>();
        for (final Receipt receipt : getReceiptsSet()) {
            for (final AdministrativeOffice administrativeOffice : administrativeOffices) {
                if (receipt.isFromAdministrativeOffice(administrativeOffice)) {
                    result.add(receipt);
                }
            }
        }

        return result;
    }

    @Override
    final public boolean isPerson() {
        return true;
    }

    final public boolean isFemale() {
        return getGender() == Gender.FEMALE;
    }

    final public boolean isMale() {
        return getGender() == Gender.MALE;
    }

    @Deprecated
    public Set<Registration> getStudents() {
        return getStudent() != null ? getStudent().getRegistrationsSet() : Collections.emptySet();
    }

    @Deprecated
    public boolean hasAnyStudents() {
        return getStudentsCount() > 0;
    }

    @Deprecated
    public int getStudentsCount() {
        return getStudent() != null ? getStudent().getRegistrationsSet().size() : 0;
    }

    @Deprecated
    public Set<Registration> getStudentsSet() {
        return getStudent() != null ? getStudent().getRegistrationsSet() : Collections.emptySet();
    }

    public SortedSet<String> getOrganizationalUnitsPresentation() {
        final SortedSet<String> organizationalUnits = new TreeSet<>();
        for (final Accountability accountability : getParentsSet()) {
            if (isOrganizationalUnitsForPresentation(accountability)) {
                final Party party = accountability.getParentParty();
                organizationalUnits.add(party.getName());
            }
        }
        if (getStudent() != null) {
            for (final Registration registration : getStudent().getRegistrationsSet()) {
                if (registration.isActive()) {
                    final DegreeCurricularPlan degreeCurricularPlan = registration.getLastDegreeCurricularPlan();
                    if (degreeCurricularPlan != null) {
                        final Degree degree = degreeCurricularPlan.getDegree();
                        organizationalUnits.add(degree.getPresentationName());
                    }
                }
            }
        }
        return organizationalUnits;
    }

    private boolean isOrganizationalUnitsForPresentation(final Accountability accountability) {
        final AccountabilityType accountabilityType = accountability.getAccountabilityType();
        final AccountabilityTypeEnum accountabilityTypeEnum = accountabilityType.getType();
        return accountabilityTypeEnum == AccountabilityTypeEnum.WORKING_CONTRACT;
    }

    @Deprecated
    public String getNickname() {
        return getProfile().getDisplayName();
    }

    public String getHomepageWebAddress() {
        if (isDefaultWebAddressVisible() && getDefaultWebAddress().hasUrl()) {
            return getDefaultWebAddress().getUrl();
        }
        return null;
    }

    @Deprecated
    public boolean hasAvailableWebSite() {
        return getAvailableWebSite() != null && getAvailableWebSite();
    }

    public Collection<ExecutionDegree> getCoordinatedExecutionDegrees(final DegreeCurricularPlan degreeCurricularPlan) {
        final Set<ExecutionDegree> result = new TreeSet<>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        for (final Coordinator coordinator : getCoordinatorsSet()) {
            if (coordinator.getExecutionDegree().getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
                result.add(coordinator.getExecutionDegree());
            }
        }
        return result;
    }

    public boolean isCoordinatorFor(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return executionDegree.getCoordinatorByTeacher(this) != null;
            }
        }
        return false;
    }

    public boolean isResponsibleOrCoordinatorFor(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        final Teacher teacher = getTeacher();
        return teacher != null && teacher.isResponsibleFor(curricularCourse, executionSemester)
                || isCoordinatorFor(curricularCourse.getDegreeCurricularPlan(), executionSemester.getExecutionYear());
    }

    public boolean isCoordinatorFor(final ExecutionYear executionYear, final List<DegreeType> degreeTypes) {
        for (final Coordinator coordinator : getCoordinatorsSet()) {
            final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
            if (executionDegree != null && executionDegree.getExecutionYear() == executionYear
                    && degreeTypes.contains(executionDegree.getDegree().getDegreeType())) {
                return true;
            }
        }

        return false;

    }

    public ServiceAgreement getServiceAgreementFor(final ServiceAgreementTemplate serviceAgreementTemplate) {
        for (final ServiceAgreement serviceAgreement : getServiceAgreementsSet()) {
            if (serviceAgreement.getServiceAgreementTemplate() == serviceAgreementTemplate) {
                return serviceAgreement;
            }
        }
        return null;
    }

    public String getFirstAndLastName() {
        final String[] name = getName().split(" ");
        return name[0] + " " + name[name.length - 1];
    }

    public static Collection<Person> findPerson(final String name) {
        return findPerson(name, Integer.MAX_VALUE);
    }

    public static Collection<Person> findPersonMatchingFirstAndLastName(final String completeName) {
        if (completeName != null) {
            final String[] splittedName = completeName.split(" ");
            return splittedName.length > 0 ? findPerson(splittedName[0] + " " + splittedName[splittedName.length - 1]) : Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;
    }

    public static Collection<Person> findPersonByDocumentID(final String documentIDValue) {
        final Collection<Person> people = new ArrayList<>();
        if (!StringUtils.isEmpty(documentIDValue)) {
            for (final IdDocument idDocument : IdDocument.find(documentIDValue)) {
                people.add(idDocument.getPerson());
            }
        }
        return people;
    }

    public static Person readPersonByEmailAddress(final String email) {
        final EmailAddress emailAddress = EmailAddress.find(email);
        return emailAddress != null && emailAddress.getParty().isPerson() ? (Person) emailAddress.getParty() : null;
    }

    public boolean hasEmailAddress(final String email) {
        for (final PartyContact partyContact : getPartyContactsSet()) {
            if (partyContact.isEmailAddress()) {
                final EmailAddress emailAddress = (EmailAddress) partyContact;
                if (emailAddress.hasValue(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPhotoAvailableToCurrentUser() {
        return isPhotoAvailableToPerson(AccessControl.getPerson());
    }

    public boolean isPhotoAvailableToPerson(Person requester) {
        if (isPhotoPubliclyAvailable()) {
            return true;
        }
        return requester != null && RoleType.PERSON.isMember(requester.getUser());
    }

    @Override
    public Photograph getPersonalPhoto() {
        Photograph photo = super.getPersonalPhoto();
        if (photo == null) {
            return null;
        }
        do {
            if (photo.getState() == PhotoState.APPROVED) {
                return photo;
            }
            photo = photo.getPrevious();
        } while (photo != null);
        return null;
    }

    public Photograph getPersonalPhotoEvenIfPending() {
        Photograph photo = super.getPersonalPhoto();
        if (photo == null) {
            return null;
        }

        do {
            if (photo.getState() != PhotoState.REJECTED && photo.getState() != PhotoState.USER_REJECTED) {
                return photo;
            }

            photo = photo.getPrevious();
        } while (photo != null);

        return null;
    }

    public Photograph getPersonalPhotoEvenIfRejected() {
        return super.getPersonalPhoto();
    }

    @Override
    public void setPersonalPhoto(final Photograph photo) {
        if (super.getPersonalPhoto() != null) {
            photo.setPrevious(super.getPersonalPhoto());
        }
        super.setPersonalPhoto(photo);
        if (photo != null) {
            photo.logCreate(this);
        }
    }

    public List<Photograph> getPhotographHistory() {
        final LinkedList<Photograph> history = new LinkedList<>();
        for (Photograph photo = super.getPersonalPhoto(); photo != null; photo = photo.getPrevious()) {
            history.addFirst(photo);
        }
        return history;
    }

    public boolean isPhotoPubliclyAvailable() {
        return getPhotoAvailable();
    }

    public boolean isDefaultEmailVisible() {
        return getDefaultEmailAddress() == null ? false : getDefaultEmailAddress().getVisibleToPublic();
    }

    public boolean isDefaultWebAddressVisible() {
        return getDefaultWebAddress() == null ? false : getDefaultWebAddress().getVisibleToPublic();
    }

    @Deprecated
    public Boolean getAvailableEmail() {
        return isDefaultEmailVisible();
    }

    @Deprecated
    public void setAvailableEmail(final Boolean available) {
        if (getDefaultEmailAddress() != null) {
            getDefaultEmailAddress().setVisibleToPublic(available);
        }
    }

    @Deprecated
    public Boolean getAvailableWebSite() {
        return isDefaultWebAddressVisible();
    }

    @Deprecated
    public void setAvailableWebSite(final Boolean available) {
        if (getDefaultWebAddress() != null) {
            getDefaultWebAddress().setVisibleToPublic(available);
        }
    }

    public String getPresentationName() {
        final String username = getUsername();
        return username == null ? getName() : getName() + " (" + getUsername() + ")";
    }

    @Override
    public String getPartyPresentationName() {
        return getPresentationName();
    }

    private boolean hasValidIndividualCandidacy(final Class<? extends IndividualCandidacy> clazz,
            final ExecutionInterval executionInterval) {
        for (final IndividualCandidacyPersonalDetails candidacyDetails : getIndividualCandidaciesSet()) {
            final IndividualCandidacy candidacy = candidacyDetails.getCandidacy();
            if (!candidacy.isCancelled() && candidacy.getClass().equals(clazz) && candidacy.isFor(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasValidOver23IndividualCandidacy(final ExecutionInterval executionInterval) {
        return hasValidIndividualCandidacy(Over23IndividualCandidacy.class, executionInterval);
    }

    public boolean hasValidSecondCycleIndividualCandidacy(final ExecutionInterval executionInterval) {
        return hasValidIndividualCandidacy(SecondCycleIndividualCandidacy.class, executionInterval);
    }

    public boolean hasValidDegreeCandidacyForGraduatedPerson(final ExecutionInterval executionInterval) {
        return hasValidIndividualCandidacy(DegreeCandidacyForGraduatedPerson.class, executionInterval);
    }

    public boolean hasValidStandaloneIndividualCandidacy(final ExecutionInterval executionInterval) {
        return hasValidIndividualCandidacy(StandaloneIndividualCandidacy.class, executionInterval);
    }

    public List<Formation> getFormations() {
        final List<Formation> formations = new ArrayList<>();
        for (final Qualification qualification : getAssociatedQualificationsSet()) {
            if (qualification instanceof Formation) {
                formations.add((Formation) qualification);
            }
        }
        return formations;
    }

    public Qualification getLastQualification() {
        return !getAssociatedQualificationsSet().isEmpty() ? Collections.max(getAssociatedQualificationsSet(),
                Qualification.COMPARATOR_BY_YEAR) : null;
    }

    public Set<AnnualIRSDeclarationDocument> getAnnualIRSDocuments() {
        final Set<AnnualIRSDeclarationDocument> result = new HashSet<>();

        for (final GeneratedDocument each : getAddressedDocumentSet()) {
            if (each instanceof AnnualIRSDeclarationDocument) {
                result.add((AnnualIRSDeclarationDocument) each);
            }
        }

        return result;
    }

    public AnnualIRSDeclarationDocument getAnnualIRSDocumentFor(final Integer year) {
        for (final AnnualIRSDeclarationDocument each : getAnnualIRSDocuments()) {
            if (each.getYear().equals(year)) {
                return each;
            }
        }

        return null;

    }

    public boolean hasAnnualIRSDocumentFor(final Integer year) {
        return getAnnualIRSDocumentFor(year) != null;
    }

    public boolean hasAnyAdministrativeOfficeFeeAndInsuranceEventInDebt() {
        for (final Event event : getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyPastAdministrativeOfficeFeeAndInsuranceEventInDebt() {
        for (final Event event : getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE)) {
            final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                    (AdministrativeOfficeFeeAndInsuranceEvent) event;

            if (administrativeOfficeFeeAndInsuranceEvent instanceof PastAdministrativeOfficeFeeAndInsuranceEvent) {
                if (event.isInDebt()) {
                    return true;
                }
            }

        }

        return false;
    }

    public boolean hasAnyResidencePaymentsInDebtForPreviousYear() {
        final int previousYear = new LocalDate().minusYears(1).getYear();

        for (final Event event : getResidencePaymentEvents()) {
            final ResidenceEvent residenceEvent = (ResidenceEvent) event;
            if (residenceEvent.isFor(previousYear) && !residenceEvent.isCancelled() && !residenceEvent.isPayed()) {
                return true;
            }
        }
        return false;
    }

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
        return getProfessorshipsSet().stream().filter(prof -> prof.getExecutionCourse().equals(executionCourse)).findAny()
                .orElse(null);
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
        return getProfessorshipByExecutionCourse(executionCourse) != null;
    }

    public Set<PhdAlertMessage> getUnreadedPhdAlertMessages() {
        final Set<PhdAlertMessage> result = new HashSet<>();

        for (final PhdAlertMessage message : getPhdAlertMessagesSet()) {
            if (!message.isReaded()) {
                result.add(message);
            }
        }

        return result;
    }

    public boolean isPhdStudent() {
        return !getPhdIndividualProgramProcessesSet().isEmpty();
    }

    public RegistrationProtocol getOnlyRegistrationProtocol() {
        if (getRegistrationProtocolsSet().size() == 1) {
            return getRegistrationProtocolsSet().iterator().next();
        }
        return null;
    }

    public List<Professorship> getProfessorships(final ExecutionSemester executionSemester) {
        final List<Professorship> professorships = new ArrayList<>();
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod().equals(executionSemester)) {
                professorships.add(professorship);
            }
        }
        return professorships;
    }

    public List<Professorship> getProfessorships(final ExecutionYear executionYear) {
        final List<Professorship> professorships = new ArrayList<>();
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                professorships.add(professorship);
            }
        }
        return professorships;
    }

    public boolean teachesAny(final Collection<ExecutionCourse> executionCourses) {
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (executionCourses.contains(professorship.getExecutionCourse())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTeacherEvaluationCoordinatorCouncilMember() {
        PersistentGroup group = Bennu.getInstance().getTeacherEvaluationCoordinatorCouncil();
        return group != null ? group.isMember(Authenticate.getUser()) : false;
    }

    public EmailAddress getEmailAddressForSendingEmails() {
        if (getDisableSendEmails()) {
            return null;
        }
        final EmailAddress defaultEmailAddress = getDefaultEmailAddress();
        if (defaultEmailAddress != null) {
            return defaultEmailAddress;
        }
        final EmailAddress institutionalEmailAddress = getInstitutionalEmailAddress();
        if (institutionalEmailAddress != null) {
            return institutionalEmailAddress;
        }
        for (final PartyContact partyContact : getPartyContactsSet()) {
            if (partyContact.isEmailAddress() && partyContact.isActiveAndValid() && partyContact.isValid()) {
                final EmailAddress otherEmailAddress = (EmailAddress) partyContact;
                return otherEmailAddress;
            }
        }
        return null;
    }

    public String getEmailForSendingEmails() {
        final EmailAddress emailAddress = getEmailAddressForSendingEmails();
        return emailAddress == null ? null : emailAddress.getValue();
    }

    public boolean areContactsRecent(final Class<? extends PartyContact> contactClass, final int daysNotUpdated) {
        final List<? extends PartyContact> partyContacts = getPartyContacts(contactClass);
        boolean isUpdated = false;
        for (final PartyContact partyContact : partyContacts) {
            if (partyContact.getLastModifiedDate() == null) {
                isUpdated = isUpdated || false;
            } else {
                final DateTime lastModifiedDate = partyContact.getLastModifiedDate();
                final DateTime now = new DateTime();
                final Months months = Months.monthsBetween(lastModifiedDate, now);
                if (months.getMonths() > daysNotUpdated) {
                    isUpdated = isUpdated || false;
                } else {
                    isUpdated = isUpdated || true;
                }
            }
        }
        return isUpdated;
    }

    /**
     * Use socialSecurityNumber instead
     */
    @Override
    @Deprecated
    public String getFiscalCode() {
        return super.getFiscalCode();
    }

    @Override
    @Deprecated
    public void setFiscalCode(final String value) {
        super.setFiscalCode(value);
    }

    public static Person findByUsername(final String username) {
        final User user = User.findByUsername(username);
        return user == null ? null : user.getPerson();
    }

    /**
     * This method gets the identification document series number.
     * The value is ignored if the document is not an identity card.
     */
    public String getIdentificationDocumentSeriesNumber() {
        if (getIdDocumentType() == IDDocumentType.IDENTITY_CARD) {
        	String seriesNumber = getIdentificationDocumentSeriesNumberValue();
        	String extraDigit = getIdentificationDocumentExtraDigitValue();
        	if (seriesNumber != null) {
        		return seriesNumber;
        	} else if (extraDigit != null) {
        		return extraDigit;
        	}
        }
        return "";
    }

    public String getIdentificationDocumentExtraDigitValue() {
        final PersonIdentificationDocumentExtraInfo result =
                getPersonIdentificationDocumentExtraInfo(IdentificationDocumentExtraDigit.class);
        return result != null ? result.getValue() : null;
    }

    public String getIdentificationDocumentSeriesNumberValue() {
        final PersonIdentificationDocumentExtraInfo result =
                getPersonIdentificationDocumentExtraInfo(IdentificationDocumentSeriesNumber.class);
        return result != null ? result.getValue() : null;
    }

    public PersonIdentificationDocumentExtraInfo getPersonIdentificationDocumentExtraInfo(final Class clazz) {
        PersonIdentificationDocumentExtraInfo result = null;
        for (final PersonIdentificationDocumentExtraInfo info : getPersonIdentificationDocumentExtraInfoSet()) {
            if (info.getClass() == clazz
                    && (result == null || result.getRegisteredInSystemTimestamp().isBefore(info.getRegisteredInSystemTimestamp()))) {
                result = info;
            }
        }
        return result == null ? null : result;
    }

    public void setIdentificationDocumentSeriesNumber(final String identificationDocumentSeriesNumber) {
        if (!StringUtils.isEmpty(identificationDocumentSeriesNumber) && getIdDocumentType() == IDDocumentType.IDENTITY_CARD) {
            if (identificationDocumentSeriesNumber.trim().length() == 1) {
                final PersonIdentificationDocumentExtraInfo personIdentificationDocumentExtraInfo =
                        getPersonIdentificationDocumentExtraInfo(IdentificationDocumentExtraDigit.class);
                if (personIdentificationDocumentExtraInfo == null) {
                    new IdentificationDocumentExtraDigit(this, identificationDocumentSeriesNumber);
                } else {
                    personIdentificationDocumentExtraInfo.setValue(identificationDocumentSeriesNumber);
                }
            } else {
                final PersonIdentificationDocumentExtraInfo personIdentificationDocumentExtraInfo =
                        getPersonIdentificationDocumentExtraInfo(IdentificationDocumentSeriesNumber.class);
                if (personIdentificationDocumentExtraInfo == null) {
                    new IdentificationDocumentSeriesNumber(this, identificationDocumentSeriesNumber);
                } else {
                    personIdentificationDocumentExtraInfo.setValue(identificationDocumentSeriesNumber);
                }
            }
        }
    }

    public void setIdentificationDocumentExtraDigit(final String identificationDocumentExtraDigit) {
        if (!StringUtils.isEmpty(identificationDocumentExtraDigit)) {
            final PersonIdentificationDocumentExtraInfo personIdentificationDocumentExtraInfo =
                    getPersonIdentificationDocumentExtraInfo(IdentificationDocumentExtraDigit.class);
            if (personIdentificationDocumentExtraInfo == null) {
                new IdentificationDocumentExtraDigit(this, identificationDocumentExtraDigit);
            } else {
                personIdentificationDocumentExtraInfo.setValue(identificationDocumentExtraDigit);
            }
        }
    }

    @Override
    @Atomic
    public void setNumberOfValidationRequests(final Integer numberOfValidationRequests) {
        super.setNumberOfValidationRequests(numberOfValidationRequests);
    }

    public boolean getCanValidateContacts() {
        final DateTime now = new DateTime();
        final DateTime requestDate = getLastValidationRequestDate();
        if (requestDate == null || getNumberOfValidationRequests() == null) {
            return true;
        }
        final DateTime plus30 = requestDate.plusDays(30);
        if (now.isAfter(plus30) || now.isEqual(plus30)) {
            setNumberOfValidationRequests(0);
        }
        return getNumberOfValidationRequests() <= MAX_VALIDATION_REQUESTS;
    }

    @Atomic
    public void incValidationRequest() {
        getCanValidateContacts();
        Integer numberOfValidationRequests = getNumberOfValidationRequests();
        numberOfValidationRequests = numberOfValidationRequests == null ? 0 : numberOfValidationRequests;
        if (numberOfValidationRequests <= MAX_VALIDATION_REQUESTS) {
            setNumberOfValidationRequests(numberOfValidationRequests + 1);
            setLastValidationRequestDate(new DateTime());
        }
    }

    @Override
    public Integer getNumberOfValidationRequests() {
        final Integer numberOfValidationRequests = super.getNumberOfValidationRequests();
        if (numberOfValidationRequests == null) {
            return 0;
        }
        return numberOfValidationRequests;
    }

    public boolean isOptOutAvailable() {
        return BooleanUtils.isTrue(getDisableSendEmails()) || MessagingSystem.getInstance().isOptOutAvailable(this.getUser());
    }

    @Deprecated
    public java.util.Date getDateOfBirth() {
        final org.joda.time.YearMonthDay ymd = getDateOfBirthYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public java.util.Date getEmissionDateOfDocumentId() {
        final org.joda.time.YearMonthDay ymd = getEmissionDateOfDocumentIdYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public java.util.Date getExpirationDateOfDocumentId() {
        final org.joda.time.YearMonthDay ymd = getExpirationDateOfDocumentIdYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    /*********************************
     * LOGGING METHODS AND OVERRIDES *
     ********************************/

    private void logSetter(String keyTypeOfData, String oldValue, String newValue, String keyLabel) {

        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(this);
        if (oldValue.compareTo(newValue) != 0) {
            String infoLabel = BundleUtil.getString(Bundle.APPLICATION, keyLabel);
            String typeOfData = BundleUtil.getString(Bundle.MESSAGING, keyTypeOfData);
            PersonInformationLog.createLog(this, Bundle.MESSAGING, "log.personInformation.edit.generalTemplate", typeOfData,
                    infoLabel, personViewed, oldValue);
        }
    }

    private void logSetterNullString(String keyInfoType, String oldValue, String newValue, String keyLabel) {
        String argNew, argOld;
        argOld = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.APPLICATION, "label.empty"), oldValue);
        argNew = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.APPLICATION, "label.empty"), newValue);
        logSetter(keyInfoType, argOld, argNew, keyLabel);
    }

    private void logSetterNullYearMonthDay(String keyInfoType, YearMonthDay oldValue, YearMonthDay newValue, String keyLabel) {
        Object argNew, argOld;
        String strNew, strOld;
        argOld = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.HTML, "text.dateEmpty"), oldValue);
        argNew = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.HTML, "text.dateEmpty"), newValue);

        if (argOld instanceof YearMonthDay) {
            strOld = ((YearMonthDay) argOld).toString("yyyy/MM/dd");
        } else {
            strOld = (String) argOld;
        }

        if (argNew instanceof YearMonthDay) {
            strNew = ((YearMonthDay) argNew).toString("yyyy/MM/dd");
        } else {
            strNew = (String) argNew;
        }
        logSetter(keyInfoType, strOld, strNew, keyLabel);
    }

    private void logSetterNullEnum(String keyInfoType, IPresentableEnum oldValue, IPresentableEnum newValue, String keyLabel) {
        Object argNew, argOld;
        String strNew, strOld;
        argOld = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.APPLICATION, "label.empty"), oldValue);
        argNew = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.APPLICATION, "label.empty"), newValue);

        if (argOld instanceof Enum) {
            strOld = ((IPresentableEnum) argOld).getLocalizedName();
        } else {
            strOld = (String) argOld;
        }

        if (argNew instanceof Enum) {
            strNew = ((IPresentableEnum) argNew).getLocalizedName();
        } else {
            strNew = (String) argNew;
        }
        logSetter(keyInfoType, strOld, strNew, keyLabel);
    }

    @Override
    public void setGender(Gender arg) {
        logSetterNullEnum("log.personInformation.edit.generalTemplate.personalData", getGender(), arg, "label.gender");
        super.setGender(arg);
    }

    @Override
    public void setProfession(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalData", getProfession(), arg, "label.occupation");
        super.setProfession(arg);
    }

    @Override
    public void setMaritalStatus(MaritalStatus arg) {
        // avmc: logic here is different: null value is converted to UNKNOWN
        MaritalStatus argToSet;
        if (arg != null) {
            argToSet = arg;
        } else {
            argToSet = MaritalStatus.UNKNOWN;
        }
        logSetterNullEnum("log.personInformation.edit.generalTemplate.personalData", getMaritalStatus(), argToSet,
                "label.maritalStatus");
        super.setMaritalStatus(argToSet);
    }

    @Override
    public void setEmissionLocationOfDocumentId(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getEmissionLocationOfDocumentId(), arg,
                "label.documentIdEmissionLocation");
        super.setEmissionLocationOfDocumentId(arg);
    }

    @Override
    public void setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay arg) {
        logSetterNullYearMonthDay("log.personInformation.edit.generalTemplate.personalId",
                getEmissionDateOfDocumentIdYearMonthDay(), arg, "label.documentIdEmissionDate");
        super.setEmissionDateOfDocumentIdYearMonthDay(arg);
    }

    @Override
    public void setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay arg) {
        logSetterNullYearMonthDay("log.personInformation.edit.generalTemplate.personalId",
                getExpirationDateOfDocumentIdYearMonthDay(), arg, "label.documentIdExpirationDate");
        super.setExpirationDateOfDocumentIdYearMonthDay(arg);
    }

    @Override
    public void setSocialSecurityNumber(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getSocialSecurityNumber(), arg,
                "label.socialSecurityNumber");
        if (arg != null) {
            arg = arg.toUpperCase();
        }
        super.setSocialSecurityNumber(arg);
    }

    @Override
    public void setEidentifier(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getEidentifier(), arg, "label.eidentifier");
        super.setEidentifier(arg);
    }

    @Override
    public void setDateOfBirthYearMonthDay(YearMonthDay arg) {
        logSetterNullYearMonthDay("log.personInformation.edit.generalTemplate.filiation", getDateOfBirthYearMonthDay(), arg,
                "label.dateOfBirth");
        super.setDateOfBirthYearMonthDay(arg);
    }

    // Nationality
    @Override
    public void setCountry(Country arg) {
        String argNew, argOld;

        if (getCountry() != null) {
            if (getCountry().getCountryNationality() != null) {
                argOld = getCountry().getCountryNationality().getContent();
            } else {
                argOld = getCountry().getName();
            }
        } else {
            argOld = BundleUtil.getString(Bundle.APPLICATION, "label.empty");
        }

        if (arg != null) {
            if (arg.getCountryNationality() != null) {
                argNew = arg.getCountryNationality().getContent();
            } else {
                argNew = arg.getName();
            }
        } else {
            argNew = BundleUtil.getString(Bundle.APPLICATION, "label.empty");
        }
        super.setCountry(arg);
        logSetter("log.personInformation.edit.generalTemplate.filiation", argOld, argNew, "label.nationality");
    }

    @Override
    public void setParishOfBirth(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getParishOfBirth(), arg,
                "label.parishOfBirth");
        super.setParishOfBirth(arg);
    }

    @Override
    public void setDistrictSubdivisionOfBirth(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getDistrictSubdivisionOfBirth(), arg,
                "label.districtSubdivisionOfBirth");
        super.setDistrictSubdivisionOfBirth(arg);
    }

    @Override
    public void setDistrictOfBirth(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getDistrictOfBirth(), arg,
                "label.districtOfBirth");
        super.setDistrictOfBirth(arg);
    }

    // Not to be confused with Nationality
    @Override
    public void setCountryOfBirth(Country arg) {
        String argNew, argOld;

        if (getCountryOfBirth() != null) {
            argOld = getCountryOfBirth().getName();
        } else {
            argOld = BundleUtil.getString(Bundle.APPLICATION, "label.empty");
        }

        if (arg != null) {
            argNew = arg.getName();
        } else {
            argNew = BundleUtil.getString(Bundle.APPLICATION, "label.empty");
        }
        super.setCountryOfBirth(arg);
        logSetter("log.personInformation.edit.generalTemplate.filiation", argOld, argNew, "label.countryOfBirth");
    }

    @Override
    public void setNameOfMother(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getNameOfMother(), arg, "label.nameOfMother");
        super.setNameOfMother(arg);
    }

    @Override
    public void setNameOfFather(String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getNameOfFather(), arg, "label.nameOfFather");
        super.setNameOfFather(arg);
    }

    @Override
    public void logCreateContact(PartyContact contact) {
        contact.logCreate(this);
    }

    @Override
    public void logEditContact(PartyContact contact, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact,
            String newValue) {
        contact.logEdit(this, propertiesChanged, valueChanged, createdNewContact, newValue);
    }

    @Override
    public void logDeleteContact(PartyContact contact) {
        contact.logDelete(this);
    }

    @Override
    public void logValidContact(PartyContact contact) {
        contact.logValid(this);
    }

    @Override
    public void logRefuseContact(PartyContact contact) {
        contact.logRefuse(this);
    }

    public static Group convertToUserGroup(Collection<Person> persons) {
        return Group.users(persons.stream().map(Person::getUser).filter(Objects::nonNull));
    }

    @Override
    public Sender getSender() {
        return Optional.ofNullable(super.getSender()).orElseGet(this::buildDefaultSender);
    }

    @Atomic
    private Sender buildDefaultSender() {
        Sender sender = Sender
                .from(Installation.getInstance().getInstituitionalEmailAddress("noreply"))
                .as(createFromName())
                .replyTo(getDefaultEmailAddressValue())
                .members(getPersonGroup())
                .build();
        setSender(sender);
        return sender;
    }

    private String createFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), getName());
    }

    public PhysicalAddress getFiscalAddress() {
        return getPartyContactsSet().stream()
                .filter(pc -> pc instanceof PhysicalAddress)
                .map(pc -> (PhysicalAddress) pc)
                .sorted(PhysicalAddress.COMPARATOR_BY_RELEVANCE)
                .findFirst().orElse(null);
    }

    public Country getVATCountry() {
        if (getSocialSecurityNumber() != null && getSocialSecurityNumber().length() >= 2) {
            final String countryCode = getSocialSecurityNumber().substring(0, 2);
            return Country.readByTwoLetterCode(countryCode);
        }
        final Country country = getNationality();
        return country == null || country.getCode()
                .equals(Bennu.getInstance().getInstitutionUnit().getNationality().getCode()) ? null : country;
    }
}
