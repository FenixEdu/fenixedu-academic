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
package org.fenixedu.academic.domain.contacts;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonInformationLog;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class PartyContact extends PartyContact_Base {

    public interface ContactResolver<T extends PartyContact> {
        public String getPresentationValue(T partyContact);
    }

    private static final Map<Class<? extends PartyContact>, ContactResolver<? extends PartyContact>> DEFAULT_RESOLVERS =
            new HashMap<>();

    private static ContactResolver<? extends PartyContact> getResolver(final Class<?> class1) {
        synchronized (DEFAULT_RESOLVERS) {
            return DEFAULT_RESOLVERS.get(class1);
        }
    }

    public static void setResolver(final Class<? extends PartyContact> class1, final ContactResolver<?> contactResolver) {
        synchronized (DEFAULT_RESOLVERS) {
            DEFAULT_RESOLVERS.put(class1, contactResolver);
        }
    }

    public static Comparator<PartyContact> COMPARATOR_BY_TYPE = new Comparator<PartyContact>() {
        @Override
        public int compare(final PartyContact contact, final PartyContact otherContact) {
            int result = contact.getType().compareTo(otherContact.getType());
            return result == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(contact, otherContact) : result;
        }
    };

    protected PartyContact() {
        super();
        setContactRoot(ContactRoot.getInstance());
        setVisibleToPublic(Boolean.FALSE);
        setVisibleToStudents(Boolean.FALSE);
        setVisibleToStaff(Boolean.FALSE);
        setLastModifiedDate(new DateTime());
        setActive(true);
        setDefaultContact(false);
    }

    protected void init(final Party party, final PartyContactType type, final boolean defaultContact) {

        checkParameters(party, type);

        super.setParty(party);
        super.setType(type);

        if (type.equals(PartyContactType.INSTITUTIONAL)) {
            setVisibleToPublic();
        } else {
            setVisibleToStudents(type == PartyContactType.WORK);
            setVisibleToStaff(type == PartyContactType.WORK);
        }
        setDefaultContactInformation(defaultContact);
        setLastModifiedDate(new DateTime());
    }

    protected void init(final Party party, final PartyContactType type, final boolean visibleToPublic,
            final boolean visibleToStudents, final boolean visibleToStaff, final boolean defaultContact) {
        checkParameters(party, type);
        super.setParty(party);
        super.setType(type);
        setVisibleToPublic(new Boolean(visibleToPublic));
        setVisibleToStudents(new Boolean(visibleToStudents));
        setVisibleToStaff(new Boolean(visibleToStaff));
        setDefaultContactInformation(defaultContact);
        setLastModifiedDate(new DateTime());
    }

    private void setVisibleToPublic() {
        super.setVisibleToPublic(Boolean.TRUE);
        super.setVisibleToStudents(Boolean.TRUE);
        super.setVisibleToStaff(Boolean.TRUE);
    }

    @Override
    public void setVisibleToPublic(final Boolean visibleToPublic) {
        super.setVisibleToPublic(visibleToPublic);
        if (visibleToPublic.booleanValue()) {
            super.setVisibleToStudents(Boolean.TRUE);
            super.setVisibleToStaff(Boolean.TRUE);
        }
    }

    @Override
    public void setVisibleToStudents(final Boolean visibleToStudents) {
        super.setVisibleToStudents(visibleToStudents);
        if (!visibleToStudents.booleanValue()) {
            super.setVisibleToPublic(Boolean.FALSE);
        }
    }

    @Override
    public void setVisibleToStaff(final Boolean visibleToStaff) {
        super.setVisibleToStaff(visibleToStaff);
        if (!visibleToStaff.booleanValue()) {
            super.setVisibleToPublic(Boolean.FALSE);
        }
    }

    public boolean isVisible() {
        if (getVisibleToPublic()) {
            return true;
        }
        if (getVisibleToStudents() && ContactRoot.getInstance().getStudentVisibility().isMember(Authenticate.getUser())) {
            return true;
        }
        if (getVisibleToStaff() && ContactRoot.getInstance().getStaffVisibility().isMember(Authenticate.getUser())) {
            return true;
        }
        if (ContactRoot.getInstance().getManagementVisibility().isMember(Authenticate.getUser())) {
            return true;
        }
        return false;
    }

    public void setDefaultContactInformation(final boolean defaultContact) {
        if (!isActiveAndValid()) {
            if (getPartyContactValidation() != null) {
                getPartyContactValidation().setToBeDefault(defaultContact);
            }
        } else {
            if (defaultContact) {
                changeToDefault();
            } else {
                final List<PartyContact> partyContacts = (List<PartyContact>) getParty().getPartyContacts(getClass());
                if (partyContacts.isEmpty() || partyContacts.size() == 1) {
                    super.setDefaultContact(Boolean.TRUE);
                } else {
                    setAnotherContactAsDefault();
                    super.setDefaultContact(Boolean.FALSE);
                }
            }
        }
    }

    public void changeToDefault() {
        final PartyContact defaultPartyContact = getParty().getDefaultPartyContact(getClass());
        if (defaultPartyContact != null && defaultPartyContact != this) {
            defaultPartyContact.setDefaultContact(Boolean.FALSE);
        }
        super.setDefaultContact(Boolean.TRUE);
    }

    private void checkParameters(final Party party, final PartyContactType type) {
        if (party == null) {
            throw new DomainException("error.contacts.PartyContact.party.cannot.be.null");
        }
        if (type == null) {
            throw new DomainException("error.contacts.PartyContact.contactType.cannot.be.null");
        }
    }

    public void edit(final PartyContactType type, final boolean defaultContact) {
        checkParameters(getParty(), type);
        setType(type);
        setDefaultContactInformation(defaultContact);
    }

    public String getPresentationValue() {
        final ContactResolver resolver = getResolver(getClass());
        return resolver == null ? null : resolver.getPresentationValue(this);
    }

    public boolean isDefault() {
        return hasDefaultContactValue() && getDefaultContact().booleanValue();
    }

    private boolean hasDefaultContactValue() {
        return getDefaultContact() != null;
    }

    public boolean isInstitutionalType() {
        return getType() == PartyContactType.INSTITUTIONAL;
    }

    @Deprecated
    public boolean isWorkType() {
        return getType() == PartyContactType.WORK;
    }

    @Deprecated
    public boolean isPersonalType() {
        return getType() == PartyContactType.PERSONAL;
    }

    public boolean isWebAddress() {
        return false;
    }

    public boolean isPhysicalAddress() {
        return false;
    }

    public boolean isEmailAddress() {
        return false;
    }

    public boolean isPhone() {
        return false;
    }

    public boolean isMobile() {
        return false;
    }

    @Atomic
    public void deleteWithoutCheckRules() {
        processDelete();
    }

    @Atomic
    public void delete() {
        if (isActiveAndValid()) {
            checkRulesToDelete();
        }
        processDelete();
    }

    protected void processDelete() {

        if (isActiveAndValid()) {
            setAnotherContactAsDefault();
        }

        if (getActive()) {
            setActive(false);
            setLastModifiedDate(new DateTime());
            setCurrentPartyContact(null);
            setPrevPartyContact(null);
            if (getPartyContactValidation() != null) {
                final PartyContactValidation validation = getPartyContactValidation();
                if (validation.getContactRoot() != null) {
                    validation.setContactRoot(null);
                }
            }
        } else {
            if (getPartyContactValidation() != null) {
                getPartyContactValidation().delete();
            }
            setParty(null);
            setContactRoot(null);
            setCurrentPartyContact(null);
            setPrevPartyContact(null);
            deleteDomainObject();
        }

    }

    protected void checkRulesToDelete() {
        // nothing to check
    }

    public void setAnotherContactAsDefault() {
        if (isDefault()) {
            final List<PartyContact> contacts = (List<PartyContact>) getParty().getPartyContacts(getClass());
            if (!contacts.isEmpty() && contacts.size() > 1) {
                contacts.remove(this);
                contacts.iterator().next().setDefaultContact(Boolean.TRUE);
            }
        }
    }

    public String getPartyContactTypeString() {
        return getType().name();
    }

    public static Set<PartyContact> readPartyContactsOfType(final Class<? extends PartyContact>... contactClasses) {
        Set<PartyContact> contacts = new HashSet<PartyContact>();

        for (Class<? extends PartyContact> clazz : contactClasses) {
            contacts.addAll(getAllInstancesOf(clazz));
        }

        return contacts;
    }

    // getActive() isValid() Result
    // 0 0 Refused
    // 0 1 Deleted when validation was needed
    // 1 0 Requires validation
    // 1 1 Valid

    public boolean isValid() {
        return getPartyContactValidation() == null || getPartyContactValidation().isValid();
    }

    public boolean isActiveAndValid() {
        return getActive() && isValid();
    }

    public boolean waitsValidation() {
        return getActive() && !isValid();
    }

    public void triggerValidationProcess() {
        if (getPartyContactValidation() != null) {
            getPartyContactValidation().triggerValidationProcess();
        }
    }

    public void triggerValidationProcessIfNeeded() {
        if (getPartyContactValidation() != null) {
            getPartyContactValidation().triggerValidationProcessIfNeeded();
        }
    }

    public abstract boolean hasValue(String value);

    public void setValid() {
        if (getPartyContactValidation() != null) {
            getPartyContactValidation().setValid();
        }
    }

    public boolean isValidationCodeGenerated() {
        if (getPartyContactValidation() != null) {
            return getPartyContactValidation().getToken() != null;
        }
        return false;
    }

    /***************************************************************************
     * DomainOperationLog: logic is all common code placed on parent class
     * (PartyContact) while child class executes parent code with specific child
     * class info (args)
     **************************************************************************/

    public void logCreate(final Person person) {
    }

    protected void logCreateAux(final Person person, final String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        final String descriptionKey;
        if (waitsValidation()) {
            descriptionKey = "log.personInformation.contact.generic.create.need.valid";
        } else {
            descriptionKey = "log.personInformation.contact.generic.create";
        }

        PersonInformationLog.createLog(person, Bundle.MESSAGING, descriptionKey, infoLabel, this.getPresentationValue(),
                personViewed);
    }

    public void logEdit(final Person person, final boolean propertiesChanged, final boolean valueChanged,
            final boolean createdNewContact, final String newValue) {
    }

    protected void logEditAux(final Person person, final boolean propertiesChanged, final boolean valueChanged,
            final boolean createdNewContact, final String newValue, final String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        boolean oldValueDiffersFromNew = false;
        if (valueChanged) {
            if (getPrevPartyContact() != null) {
                String prevPartyContactPresentationValue = getPrevPartyContact().getPresentationValue();
                oldValueDiffersFromNew =
                        prevPartyContactPresentationValue == null ? getPresentationValue() != null : prevPartyContactPresentationValue
                                .compareTo(getPresentationValue()) != 0;
            }
        }

        if (propertiesChanged && !valueChanged) {
            // only properties were changed
            if (getPrevPartyContact() != null) {
                // editing a contact with pending changes (replacing changes)
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.edit.need.valid.newEdit", infoLabel, this.getPresentationValue(),
                        personViewed);
            } else {
                // editing an existing contact with no pending changes
                if (isValid()) {
                    PersonInformationLog.createLog(person, Bundle.MESSAGING, "log.personInformation.contact.generic.edit",
                            infoLabel, this.getPresentationValue(), personViewed);

                } else {
                    // editing an existing pending contact (creation)
                    PersonInformationLog.createLog(person, Bundle.MESSAGING,
                            "log.personInformation.contact.generic.create.need.valid.edited", infoLabel,
                            this.getPresentationValue(), personViewed);
                }
            }
        } else if (valueChanged) {
            // value or physical address was changed
            if (getPrevPartyContact() != null) {
                if (getPrevPartyContact().isValid()) {
                    // editing a valid existing contact
                    if (oldValueDiffersFromNew && createdNewContact) {
                        // new value differs from old, and a new temporary
                        // contact was created
                        PersonInformationLog.createLog(person, Bundle.MESSAGING,
                                "log.personInformation.contact.generic.edit.need.valid.values", infoLabel,
                                getPrevPartyContact().getPresentationValue(), newValue, personViewed);
                    } else if (createdNewContact) {
                        // only a new temporary contact was created
                        PersonInformationLog.createLog(person, Bundle.MESSAGING,
                                "log.personInformation.contact.generic.edit.need.valid", infoLabel, this.getPresentationValue(),
                                personViewed);
                    } else if (oldValueDiffersFromNew) {
                        // only the values differ (temp contact already exists)
                        PersonInformationLog.createLog(person, Bundle.MESSAGING,
                                "log.personInformation.contact.generic.edit.need.valid.values.newEdit", infoLabel,
                                getPrevPartyContact().getPresentationValue(), newValue, personViewed);
                    } else {
                        // only physical address details changed (temp contact
                        // already exists)
                        PersonInformationLog.createLog(person, Bundle.MESSAGING,
                                "log.personInformation.contact.generic.edit.need.valid.newEdit", infoLabel,
                                this.getPresentationValue(), personViewed);
                    }
                } else {
                    PersonInformationLog.createLog(person, Bundle.MESSAGING,
                            "log.personInformation.contact.generic.edit.need.valid.values.newEdit", infoLabel,
                            this.getPresentationValue(), newValue, personViewed);
                }
            } else {
                // pending creation value was changed
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.create.need.valid.values.edited", infoLabel,
                        this.getPresentationValue(), newValue, personViewed);
            }
        }
    }

    public void logDelete(final Person person) {
    }

    public void logDeleteAux(final Person person, final String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        if (isValid()) {
            // it is valid, so it is not a pending change or creation
            PersonInformationLog.createLog(person, Bundle.MESSAGING, "log.personInformation.contact.generic.remove", infoLabel,
                    this.getPresentationValue(), personViewed);
        } else {
            if (getPrevPartyContact() == null) {
                // no previous contact = new contact being created
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.create.need.valid.canceled", infoLabel,
                        this.getPresentationValue(), personViewed);
            } else {
                // has previous contact = edit
                String oldValue = getPrevPartyContact().getPresentationValue();
                if (oldValue.compareTo(getPresentationValue()) == 0) {
                    // previous value is the same
                    PersonInformationLog.createLog(person, Bundle.MESSAGING,
                            "log.personInformation.contact.generic.edit.need.valid.canceled", infoLabel,
                            this.getPresentationValue(), personViewed);
                } else {
                    // previous is different, display previous value
                    PersonInformationLog.createLog(person, Bundle.MESSAGING,
                            "log.personInformation.contact.generic.edit.need.valid.values.canceled", infoLabel,
                            getPrevPartyContact().getPresentationValue(), this.getPresentationValue(), personViewed);
                }
            }
        }
    }

    public void logValid(final Person person) {
    }

    public void logValidAux(final Person person, final String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        if (getPrevPartyContact() == null) {
            // no previous contact = new contact being created
            PersonInformationLog.createLog(person, Bundle.MESSAGING,
                    "log.personInformation.contact.generic.create.need.valid.accepted", infoLabel, this.getPresentationValue(),
                    personViewed);
        } else {
            // has previous contact = edit
            String oldValue = getPrevPartyContact().getPresentationValue();
            if (oldValue.compareTo(getPresentationValue()) == 0) {
                // previous value is the same
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.edit.need.valid.accepted", infoLabel, this.getPresentationValue(),
                        personViewed);
            } else {
                // previous is different, display previous value
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.edit.need.valid.values.accepted", infoLabel,
                        getPrevPartyContact().getPresentationValue(), this.getPresentationValue(), personViewed);
            }
        }
    }

    public void logRefuse(final Person person) {
    }

    public void logRefuseAux(final Person person, final String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        if (getPrevPartyContact() == null) {
            // no previous contact = new contact being created
            PersonInformationLog.createLog(person, Bundle.MESSAGING,
                    "log.personInformation.contact.generic.create.need.valid.rejected", infoLabel, this.getPresentationValue(),
                    personViewed);
        } else {
            // has previous contact = edit
            String oldValue = getPrevPartyContact().getPresentationValue();
            if (oldValue.compareTo(getPresentationValue()) == 0) {
                // previous value is the same
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.edit.need.valid.rejected", infoLabel, this.getPresentationValue(),
                        personViewed);
            } else {
                // previous is different, display previous value
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.edit.need.valid.values.rejected", infoLabel,
                        getPrevPartyContact().getPresentationValue(), this.getPresentationValue(), personViewed);
            }
        }
    }

    private static Set<PartyContact> getAllInstancesOf(final Class<? extends PartyContact> type) {
        return ContactRoot.getInstance().getPartyContactsSet().stream().filter(type::isInstance).collect(Collectors.toSet());
    }

    public boolean isToBeValidated() {
        return true;
    }
}
