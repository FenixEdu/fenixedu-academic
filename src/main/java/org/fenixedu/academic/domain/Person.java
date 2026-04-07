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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.organizationalStructure.PartyType;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.MaritalStatus;
import org.fenixedu.academic.domain.person.identificationDocument.IdentificationDocument;
import org.fenixedu.academic.domain.person.identificationDocument.IdentificationDocumentType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.i18n.LocalizedString.Builder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.base.Strings;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.ist.fenixframework.Atomic;

public class Person extends Person_Base {

    private static final Integer MAX_VALIDATION_REQUESTS = 5;

    public static final String PERSON_CREATE_SIGNAL = "academic.person.create";

    public IdentificationDocument getDefaultIdentificationDocument() {
        final Iterator<IdentificationDocument> documentIterator = getIdentificationDocumentsSet().iterator();
        return documentIterator.hasNext() ? documentIterator.next() : null;
    }

    @Override
    public void setUser(final User user) {
        super.setUser(user);
        if (getProfile() != null) {
            getProfile().setAvatarUrl("/api/user/photo/" + getUser().getUsername());
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
     *             But still used in academic-treasury-base module
     */
    @Deprecated
    public String getGivenNames() {
        return getProfile().getGivenNames();
    }

    /**
     * @deprecated Use {@link UserProfile#getFamilyNames()}
     *             But still used in academic-treasury-base module
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

        IdentificationDocument identificationDocument = getDefaultIdentificationDocument();
        if (identificationDocument == null) {
            IdentificationDocument.create(this, documentIdNumber, null);
        } else {
            identificationDocument.setValue(documentIdNumber);
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

        IdentificationDocument identificationDocument = getDefaultIdentificationDocument();
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findIdentificationDocumentType(idDocumentType);
        if (identificationDocument == null) {
            IdentificationDocument.create(this, null, identificationDocumentType);
        } else {
            identificationDocument.setIdentificationDocumentType(identificationDocumentType);
        }

        logSetterNullEnum("log.personInformation.edit.generalTemplate.personalId", getIdDocumentType(), idDocumentType,
                "label.documentIdType");
        super.setIdDocumentType(idDocumentType);
    }

    public void setIdentificationDocument(final String documentNumber,
            final IdentificationDocumentType identificationDocumentType) {
        if (documentNumber == null || Strings.isNullOrEmpty(documentNumber)) {
            throw new DomainException("error.person.empty.documentIdNumber");
        }
        if (identificationDocumentType == null) {
            throw new DomainException("error.person.empty.idDocumentType");
        }

        IDDocumentType idDocumentType = IdentificationDocumentType.findIDDocumentType(identificationDocumentType);

        IdentificationDocument identificationDocument = getDefaultIdentificationDocument();
        if (identificationDocument == null) {
            IdentificationDocument.create(this, documentNumber, identificationDocumentType);
        } else {
            identificationDocument.setValue(documentNumber);
            identificationDocument.setIdentificationDocumentType(identificationDocumentType);
        }

        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getDocumentIdNumber(), documentNumber,
                "label.documentNumber");
        logSetterNullEnum("log.personInformation.edit.generalTemplate.personalId", getIdDocumentType(), idDocumentType,
                "label.documentIdType");
        super.setDocumentIdNumber(documentNumber);
        super.setIdDocumentType(idDocumentType);
    }

    public void setIdentification(String documentIdNumber, final IdentificationDocumentType identificationDocumentType) {
        if (StringUtils.isNotBlank(documentIdNumber) && identificationDocumentType != null) {
            String trimmedDocumentIdNumber = documentIdNumber.trim();
            findByDocumentIdentification(trimmedDocumentIdNumber, identificationDocumentType).stream()
                    .filter(person -> !person.equals(this)).findAny().ifPresent(p -> {
                        throw new DomainException("error.person.existent.docIdAndType");
                    });

            setIdentificationDocument(trimmedDocumentIdNumber, identificationDocumentType);
        }
    }

    public void setGivenNames(final String newGivenNames) {
        UserProfile profile = getProfile();
        profile.changeName(newGivenNames, profile.getFamilyNames(), profile.getDisplayName());
    }

    public String getDisplayName() {
        return getProfile().getDisplayName();
    }

    public void setDisplayName(final String newDisplayName) {
        UserProfile profile = getProfile();
        try {
            profile.changeName(profile.getGivenNames(), profile.getFamilyNames(), newDisplayName);
        } catch (BennuCoreDomainException ex) {
            throw new DomainException("error.invalid.displayName", ex.getLocalizedMessage());
        }
    }

    public void setFamilyNames(final String newFamilyNames) {
        UserProfile profile = getProfile();
        profile.changeName(profile.getGivenNames(), newFamilyNames, profile.getDisplayName());
    }

    /**
     * Creates a new Person, associated to the given {@link UserProfile}.
     *
     * Note that this constructor does NOT create a {@link User}.
     *
     * @param profile
     *            The profile to associate with the created person.
     */
    public Person(final UserProfile profile) {
        this(profile, true);
    }

    private Person(final UserProfile profile, final boolean emitSignal) {

        super();
        setProfile(profile);
        if (profile.getUser() != null) {
            setUser(profile.getUser());
        }
        setMaritalStatus(MaritalStatus.UNKNOWN);
        if (emitSignal) {
            Signal.emit(Person.PERSON_CREATE_SIGNAL, new DomainObjectEvent<Person>(this));
        }
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
        this(personBean, false, true);
    }

    public Person(final PersonBean personBean, final boolean validateEmail) {
        this(personBean, validateEmail, true);
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
    private Person(final PersonBean personBean, final boolean validateEmail, final boolean emitSignal) {
        this(new UserProfile(personBean.getGivenNames(), personBean.getFamilyNames(), null, personBean.getEmail(),
                Locale.getDefault()), false);

        setProperties(personBean);

        if (personBean.isPhysicalAddressDataNotEmpty()) {
            PhysicalAddress.createPhysicalAddress(this, personBean.getPhysicalAddressData(), PartyContactType.PERSONAL, true);
        }

        Phone.createPhone(this, personBean.getPhone(), PartyContactType.PERSONAL, true);
        MobilePhone.createMobilePhone(this, personBean.getMobile(), PartyContactType.PERSONAL, true);
        final EmailAddress emailAddress =
                EmailAddress.createEmailAddress(this, personBean.getEmail(), PartyContactType.PERSONAL, true);
        if (validateEmail) {
            emailAddress.setValid();
        }
        WebAddress.createWebAddress(this, personBean.getWebAddress(), PartyContactType.PERSONAL, true);
        if (emitSignal) {
            Signal.emit(Person.PERSON_CREATE_SIGNAL, new DomainObjectEvent<Person>(this));
        }
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
        setProperties(personBean);
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
        setIdentification(personBean.getDocumentIdNumber(), personBean.getIdentificationDocumentType());
        setIdentificationDocumentSeriesNumber(personBean.getIdentificationDocumentSeriesNumber());
        setEmissionLocationOfDocumentId(personBean.getDocumentIdEmissionLocation());
        setEmissionDateOfDocumentIdYearMonthDay(personBean.getDocumentIdEmissionDate());
        setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
        setEidentifier(personBean.getEidentifier());
        setHealthCardNumber(personBean.getHealthCardNumber());

        // filiation
        setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
        setCountry(personBean.getNationality());
        setSecondNationality(personBean.getSecondNationality());
        setParishOfBirth(personBean.getParishOfBirth());
        setDistrictSubdivisionOfBirth(personBean.getDistrictSubdivisionOfBirth());
        setDistrictOfBirth(personBean.getDistrictOfBirth());
        setCountryOfBirth(personBean.getCountryOfBirth());
        setNameOfMother(personBean.getMotherName());
        setNameOfFather(personBean.getFatherName());

        if (getPersonIdentificationDocumentExtraInfo(IdentificationDocumentExtraDigit.class) != null) {
            getPersonIdentificationDocumentExtraInfo(IdentificationDocumentExtraDigit.class).clearValue();
        }

        if (getPersonIdentificationDocumentExtraInfo(IdentificationDocumentSeriesNumber.class) != null) {
            getPersonIdentificationDocumentExtraInfo(IdentificationDocumentSeriesNumber.class).clearValue();
        }

        if (IdentificationDocumentType.IDENTITY_CARD_CODE.equals(
                getDefaultIdentificationDocument().getIdentificationDocumentType().getCode())) {
            setIdentificationDocumentSeriesNumber(personBean.getIdentificationDocumentSeriesNumber());
        }
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
        for (PartyContact partyContact : getPartyContactsSet()) {
            partyContact.setActive(Boolean.FALSE);
            partyContact.delete();
        }

        while (getPersonalPhotoEvenIfRejected() != null) {
            getPersonalPhotoEvenIfRejected().delete();
        }

        if (getStudent() != null) {
            getStudent().delete();
        }

        if (getTeacher() != null) {
            getTeacher().delete();
        }

        getIdentificationDocumentsSet().forEach(IdentificationDocument::delete);

        for (; !getVaccineAdministrationsSet().isEmpty(); getVaccineAdministrationsSet().iterator().next().delete()) {
            ;
        }

        for (PersonInformationLog log : getPersonInformationLogsSet()) {
            log.delete();
        }

        for (PersonIdentificationDocumentExtraInfo extraInfo : getPersonIdentificationDocumentExtraInfoSet()) {
            extraInfo.delete();
        }

        if (getPersonalPhoto() != null) {
            getPersonalPhoto().delete();
        }

        getIdentifiersSet().forEach(id -> id.delete());

        final UserProfile profile = getProfile();
        if (profile != null) {
            super.setProfile(null);
            if (getUser() == null) {
                profile.delete();
            }
        }

        final User user = getUser();
        if (user != null) {
            super.setUser(null);
            //also deletes profile
            user.delete();
        }

        super.setCountry(null);
        super.setSecondNationality(null);
        super.setCountryOfBirth(null);
        super.delete();
    }

    @Override
    protected void checkForDeletionBlockers(final Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!(getChildsSet().isEmpty() && getParentsSet().isEmpty() && getAssociatedQualificationsSet().isEmpty()
                && getEnrolmentEvaluationsSet().isEmpty() && getCreatedQualificationsSet().isEmpty()
                && getCreateJobsSet().isEmpty())) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
        }
    }

    @Override
    public void setDisableSendEmails(final Boolean disableSendEmails) {
        super.setDisableSendEmails(disableSendEmails);
        getProfile().setEmail(getEmailForSendingEmails());
    }

    public static Person readPersonByUsername(final String username) {
        final User user = User.findByUsername(username);
        return user == null ? null : user.getPerson();
    }

    public static Stream<Person> findByDocumentIdentification(String documentValue) {
        return IdentificationDocument.find(documentValue).map(IdentificationDocument::getPerson);
    }

    public static Optional<Person> findByDocumentIdentification(final String documentIdNumber,
            final IdentificationDocumentType identificationDocumentType) {
        return IdentificationDocument.find(documentIdNumber, identificationDocumentType).map(IdentificationDocument::getPerson);
    }

    public static Collection<Person> findByDateOfBirth(final YearMonthDay dateOfBirth, final Collection<Person> persons) {
        final List<Person> result = new ArrayList<Person>();
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
        return getStudent() != null ? getStudent().getRegistrationsSet() : Collections.<Registration> emptySet();
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
        return getStudent() != null ? getStudent().getRegistrationsSet() : Collections.EMPTY_SET;
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
        return getAvailableWebSite() != null && getAvailableWebSite().booleanValue();
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
            return splittedName.length > 0 ? findPerson(
                    splittedName[0] + " " + splittedName[splittedName.length - 1]) : Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;
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
        if (getPhotoAvailable()) {
            return true;
        }

        final User currentUser = Authenticate.getUser();
        return currentUser != null && PermissionService.hasAccess("PERSON_PHOTOS_ACCESS", currentUser);
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
        final LinkedList<Photograph> history = new LinkedList<Photograph>();
        for (Photograph photo = super.getPersonalPhoto(); photo != null; photo = photo.getPrevious()) {
            history.addFirst(photo);
        }
        return history;
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

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
        return getProfessorshipsSet().stream().filter(prof -> prof.getExecutionCourse().equals(executionCourse)).findAny()
                .orElse(null);
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
        return getProfessorshipByExecutionCourse(executionCourse) != null;
    }

    public List<Professorship> getProfessorships(final ExecutionInterval executionInterval) {
        final List<Professorship> professorships = new ArrayList<Professorship>();
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionInterval().equals(executionInterval)) {
                professorships.add(professorship);
            }
        }
        return professorships;
    }

    public List<Professorship> getProfessorships(final ExecutionYear executionYear) {
        final List<Professorship> professorships = new ArrayList<Professorship>();
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionInterval().getExecutionYear().equals(executionYear)) {
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

    private static Function<Person, EmailAddress> customEmailAddressForSendingEmailsProvider = null;

    public static void registerCustomEmailAddressForSendingEmailsProvider(Function<Person, EmailAddress> customProvider) {
        customEmailAddressForSendingEmailsProvider = customProvider;
    }

    public EmailAddress getEmailAddressForSendingEmails() {
        final Boolean disableSendEmails = getDisableSendEmails();
        if (disableSendEmails != null && disableSendEmails.booleanValue()) {
            return null;
        }

        if (customEmailAddressForSendingEmailsProvider != null) {
            return customEmailAddressForSendingEmailsProvider.apply(this);
        }

        boolean firstInstitutional = Installation.getInstance().getForceSendingEmailsToInstituitionAddress();

        final EmailAddress emailAddress = firstInstitutional ? Optional.ofNullable(getInstitutionalEmailAddress())
                .orElseGet(() -> getDefaultEmailAddress()) : Optional.ofNullable(getDefaultEmailAddress())
                        .orElseGet(() -> getInstitutionalEmailAddress());
        if (emailAddress != null) {
            return emailAddress;
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
        if (IdentificationDocumentType.IDENTITY_CARD_CODE.equals(
                getDefaultIdentificationDocument().getIdentificationDocumentType().getCode())) {
            String seriesNumber = getIdentificationDocumentSeriesNumberValue();
            String extraDigit = getIdentificationDocumentExtraDigitValue();
            if (StringUtils.isNotBlank(seriesNumber)) {
                return seriesNumber;
            } else if (StringUtils.isNotBlank(extraDigit)) {
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
            if (info.getClass() == clazz && (result == null
                    || result.getRegisteredInSystemTimestamp().isBefore(info.getRegisteredInSystemTimestamp()))) {
                result = info;
            }
        }
        return result == null ? null : result;
    }

    public void setIdentificationDocumentSeriesNumber(final String identificationDocumentSeriesNumber) {
        if (!StringUtils.isEmpty(identificationDocumentSeriesNumber) && IdentificationDocumentType.IDENTITY_CARD_CODE.equals(
                getDefaultIdentificationDocument().getIdentificationDocumentType().getCode())) {
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

    private void logSetter(final String keyTypeOfData, final String oldValue, final String newValue, final String keyLabel) {

        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(this);
        if (oldValue.compareTo(newValue) != 0) {
            String infoLabel = BundleUtil.getString(Bundle.APPLICATION, keyLabel);
            String typeOfData = BundleUtil.getString(Bundle.MESSAGING, keyTypeOfData);
            PersonInformationLog.createLog(this, Bundle.MESSAGING, "log.personInformation.edit.generalTemplate", typeOfData,
                    infoLabel, personViewed, oldValue);
        }
    }

    private void logSetterNullString(final String keyInfoType, final String oldValue, final String newValue,
            final String keyLabel) {
        String argNew, argOld;
        argOld = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.APPLICATION, "label.empty"), oldValue);
        argNew = valueToUpdateIfNewNotNull(BundleUtil.getString(Bundle.APPLICATION, "label.empty"), newValue);
        logSetter(keyInfoType, argOld, argNew, keyLabel);
    }

    private void logSetterNullYearMonthDay(final String keyInfoType, final YearMonthDay oldValue, final YearMonthDay newValue,
            final String keyLabel) {
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

    private void logSetterNullEnum(final String keyInfoType, final IPresentableEnum oldValue, final IPresentableEnum newValue,
            final String keyLabel) {
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
    public void setGender(final Gender arg) {
        logSetterNullEnum("log.personInformation.edit.generalTemplate.personalData", getGender(), arg, "label.gender");
        super.setGender(arg);
    }

    @Override
    public void setProfession(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalData", getProfession(), arg, "label.occupation");
        super.setProfession(arg);
    }

    @Override
    public void setMaritalStatus(final MaritalStatus arg) {
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
    public void setEmissionLocationOfDocumentId(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getEmissionLocationOfDocumentId(), arg,
                "label.documentIdEmissionLocation");
        super.setEmissionLocationOfDocumentId(arg);

        final IdentificationDocument identificationDocument = getDefaultIdentificationDocument();
        if (identificationDocument != null) {
            identificationDocument.syncEmissionLocationOfDocumentIdFromPerson(arg);
        }
    }

    @Override
    public void setEmissionDateOfDocumentIdYearMonthDay(final YearMonthDay arg) {
        logSetterNullYearMonthDay("log.personInformation.edit.generalTemplate.personalId",
                getEmissionDateOfDocumentIdYearMonthDay(), arg, "label.documentIdEmissionDate");
        super.setEmissionDateOfDocumentIdYearMonthDay(arg);

        final IdentificationDocument identificationDocument = getDefaultIdentificationDocument();
        if (identificationDocument != null) {
            identificationDocument.syncEmissionDateOfDocumentIdYearMonthDayFromPerson(arg);
        }
    }

    @Override
    public void setExpirationDateOfDocumentIdYearMonthDay(final YearMonthDay arg) {
        logSetterNullYearMonthDay("log.personInformation.edit.generalTemplate.personalId",
                getExpirationDateOfDocumentIdYearMonthDay(), arg, "label.documentIdExpirationDate");
        super.setExpirationDateOfDocumentIdYearMonthDay(arg);

        final IdentificationDocument identificationDocument = getDefaultIdentificationDocument();
        if (identificationDocument != null) {
            identificationDocument.syncExpirationDateOfDocumentIdYearMonthDayFromPerson(arg);
        }
    }

    public void syncEmissionLocationOfDocumentIdFromIdentificationDocument(final String arg) {
        super.setEmissionLocationOfDocumentId(arg);
    }

    public void syncEmissionDateOfDocumentIdYearMonthDayFromIdentificationDocument(final YearMonthDay arg) {
        super.setEmissionDateOfDocumentIdYearMonthDay(arg);
    }

    public void syncExpirationDateOfDocumentIdYearMonthDayFromIdentificationDocument(final YearMonthDay arg) {
        super.setExpirationDateOfDocumentIdYearMonthDay(arg);
    }

    @Override
    public void setEidentifier(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.personalId", getEidentifier(), arg, "label.eidentifier");
        super.setEidentifier(arg);
    }

    @Override
    public void setDateOfBirthYearMonthDay(final YearMonthDay arg) {
        logSetterNullYearMonthDay("log.personInformation.edit.generalTemplate.filiation", getDateOfBirthYearMonthDay(), arg,
                "label.dateOfBirth");
        super.setDateOfBirthYearMonthDay(arg);
    }

    // Nationality
    @Override
    public void setCountry(final Country arg) {
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
    public void setParishOfBirth(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getParishOfBirth(), arg,
                "label.parishOfBirth");
        super.setParishOfBirth(arg);
    }

    @Override
    public void setDistrictSubdivisionOfBirth(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getDistrictSubdivisionOfBirth(), arg,
                "label.districtSubdivisionOfBirth");
        super.setDistrictSubdivisionOfBirth(arg);
    }

    @Override
    public void setDistrictOfBirth(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getDistrictOfBirth(), arg,
                "label.districtOfBirth");
        super.setDistrictOfBirth(arg);
    }

    // Not to be confused with Nationality
    @Override
    public void setCountryOfBirth(final Country arg) {
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
    public void setNameOfMother(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getNameOfMother(), arg, "label.nameOfMother");
        super.setNameOfMother(arg);
    }

    @Override
    public void setNameOfFather(final String arg) {
        logSetterNullString("log.personInformation.edit.generalTemplate.filiation", getNameOfFather(), arg, "label.nameOfFather");
        super.setNameOfFather(arg);
    }

    @Override
    public void logCreateContact(final PartyContact contact) {
        contact.logCreate(this);
    }

    @Override
    public void logEditContact(final PartyContact contact, final boolean propertiesChanged, final boolean valueChanged,
            final boolean createdNewContact, final String newValue) {
        contact.logEdit(this, propertiesChanged, valueChanged, createdNewContact, newValue);
    }

    @Override
    public void logDeleteContact(final PartyContact contact) {
        contact.logDelete(this);
    }

    @Override
    public void logValidContact(final PartyContact contact) {
        contact.logValid(this);
    }

    @Override
    public void logRefuseContact(final PartyContact contact) {
        contact.logRefuse(this);
    }

    public static Group convertToUserGroup(final Collection<Person> persons) {
        return Group.users(persons.stream().map(Person::getUser).filter(Objects::nonNull));
    }

    @Override
    public PartyTypeEnum getType() {
        return PartyTypeEnum.PERSON;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("error.person.partyType.cannotChange");
    }

    @Override
    public PartyType getPartyType() {
        return PartyType.of(getType()).orElse(null);
    }

    @Override
    public void setPartyType(PartyType partyType) {
        throw new DomainException("error.person.partyType.cannotChange");
    }
}
