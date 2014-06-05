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
package net.sourceforge.fenixedu.domain.contacts;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonInformationLog;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public abstract class PartyContact extends PartyContact_Base {

    public static Comparator<PartyContact> COMPARATOR_BY_TYPE = new Comparator<PartyContact>() {
        @Override
        public int compare(PartyContact contact, PartyContact otherContact) {
            int result = contact.getType().compareTo(otherContact.getType());
            return (result == 0) ? DomainObjectUtil.COMPARATOR_BY_ID.compare(contact, otherContact) : result;
        }
    };

    protected PartyContact() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        setVisibleToPublic(Boolean.FALSE);
        setVisibleToStudents(Boolean.FALSE);
        setVisibleToTeachers(Boolean.FALSE);
        setVisibleToEmployees(Boolean.FALSE);
        setVisibleToAlumni(Boolean.FALSE);
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
        } else if (party.isPerson() && !((Person) party).hasRole(RoleType.TEACHER)
                && !((Person) party).hasRole(RoleType.EMPLOYEE)) {
            setVisibleToStudents(type == PartyContactType.WORK);
            setVisibleToAlumni(type == PartyContactType.WORK);
            setVisibleToTeachers(type == PartyContactType.WORK);
            setVisibleToEmployees(type == PartyContactType.WORK);
        } else {
            setVisibleToTeachers(type == PartyContactType.WORK);
            setVisibleToEmployees(type == PartyContactType.WORK);
        }
        setDefaultContactInformation(defaultContact);
        setLastModifiedDate(new DateTime());
    }

    protected void init(final Party party, final PartyContactType type, final boolean visibleToPublic,
            final boolean visibleToStudents, final boolean visibleToTeachers, final boolean visibleToEmployees,
            final boolean visibleToAlumni, final boolean defaultContact) {
        checkParameters(party, type);
        super.setParty(party);
        super.setType(type);
        setVisibleToPublic(new Boolean(visibleToPublic));
        setVisibleToStudents(new Boolean(visibleToStudents));
        setVisibleToTeachers(new Boolean(visibleToTeachers));
        setVisibleToEmployees(new Boolean(visibleToEmployees));
        setVisibleToAlumni(new Boolean(visibleToAlumni));
        setDefaultContactInformation(defaultContact);
        setLastModifiedDate(new DateTime());
    }

    private void setVisibleToPublic() {
        super.setVisibleToPublic(Boolean.TRUE);
        super.setVisibleToStudents(Boolean.TRUE);
        super.setVisibleToTeachers(Boolean.TRUE);
        super.setVisibleToEmployees(Boolean.TRUE);
        super.setVisibleToAlumni(Boolean.TRUE);
    }

    @Override
    public void setVisibleToPublic(Boolean visibleToPublic) {
        super.setVisibleToPublic(visibleToPublic);
        if (visibleToPublic.booleanValue()) {
            super.setVisibleToStudents(Boolean.TRUE);
            super.setVisibleToTeachers(Boolean.TRUE);
            super.setVisibleToEmployees(Boolean.TRUE);
            super.setVisibleToAlumni(Boolean.TRUE);
        }
    }

    @Override
    public void setVisibleToStudents(Boolean visibleToStudents) {
        super.setVisibleToStudents(visibleToStudents);
        if (!visibleToStudents.booleanValue()) {
            super.setVisibleToPublic(Boolean.FALSE);
        }
    }

    @Override
    public void setVisibleToTeachers(Boolean visibleToTeachers) {
        super.setVisibleToTeachers(visibleToTeachers);
        if (!visibleToTeachers.booleanValue()) {
            super.setVisibleToPublic(Boolean.FALSE);
        }
    }

    @Override
    public void setVisibleToEmployees(Boolean visibleToEmployees) {
        super.setVisibleToEmployees(visibleToEmployees);
        if (!visibleToEmployees.booleanValue()) {
            super.setVisibleToPublic(Boolean.FALSE);
        }
    }

    public void setDefaultContactInformation(final boolean defaultContact) {
        if (!isActiveAndValid()) {
            if (hasPartyContactValidation()) {
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

    public abstract String getPresentationValue();

    public boolean isDefault() {
        return hasDefaultContactValue() && getDefaultContact().booleanValue();
    }

    private boolean hasDefaultContactValue() {
        return getDefaultContact() != null;
    }

    public boolean isInstitutionalType() {
        return getType() == PartyContactType.INSTITUTIONAL;
    }

    public boolean isWorkType() {
        return getType() == PartyContactType.WORK;
    }

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
        check(this, RolePredicates.PARTY_CONTACT_PREDICATE);
        processDelete();
    }

    @Atomic
    public void delete() {
        check(this, RolePredicates.PARTY_CONTACT_PREDICATE);
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
            if (hasPartyContactValidation()) {
                final PartyContactValidation validation = getPartyContactValidation();
                if (validation.hasBennu()) {
                    validation.setRootDomainObject(null);
                }
            }
        }

        // setResearcher(null);
        // setParty(null);
        // setRootDomainObject(null);
        // super.deleteDomainObject();
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

    public static Set<PartyContact> readPartyContactsOfType(Class<? extends PartyContact>... contactClasses) {
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
        return !hasPartyContactValidation() || getPartyContactValidation().isValid();
    }

    public boolean isActiveAndValid() {
        return getActive() && isValid();
    }

    public boolean waitsValidation() {
        return getActive() && !isValid();
    }

    public void triggerValidationProcess() {
        if (hasPartyContactValidation()) {
            getPartyContactValidation().triggerValidationProcess();
        }
    }

    public void triggerValidationProcessIfNeeded() {
        if (hasPartyContactValidation()) {
            getPartyContactValidation().triggerValidationProcessIfNeeded();
        }
    }

    public abstract boolean hasValue(String value);

    public void setValid() {
        if (hasPartyContactValidation()) {
            getPartyContactValidation().setValid();
        }
    }

    public boolean isValidationCodeGenerated() {
        if (hasPartyContactValidation()) {
            return getPartyContactValidation().getToken() != null;
        }
        return false;
    }

    /***************************************************************************
     * DomainOperationLog: logic is all common code placed on parent class
     * (PartyContact) while child class executes parent code with specific child
     * class info (args)
     **************************************************************************/

    public void logCreate(Person person) {
    }

    protected void logCreateAux(Person person, String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        final String descriptionKey;
        if (waitsValidation()) {
            descriptionKey = "log.personInformation.contact.generic.create.need.valid";
        } else {
            descriptionKey = "log.personInformation.contact.generic.create";
        }

        PersonInformationLog.createLog(person, Bundle.MESSAGING, descriptionKey, infoLabel,
                this.getPresentationValue(), personViewed);
    }

    public void logEdit(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact, String newValue) {
    }

    protected void logEditAux(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact,
            String newValue, String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        boolean oldValueDiffersFromNew = false;
        if (valueChanged) {
            if (hasPrevPartyContact()) {
                oldValueDiffersFromNew = getPrevPartyContact().getPresentationValue().compareTo(getPresentationValue()) != 0;
            }
        }

        if (propertiesChanged && !valueChanged) {
            // only properties were changed
            if (hasPrevPartyContact()) {
                // editing a contact with pending changes (replacing changes)
                PersonInformationLog.createLog(person, Bundle.MESSAGING,
                        "log.personInformation.contact.generic.edit.need.valid.newEdit", infoLabel, this.getPresentationValue(),
                        personViewed);
            } else {
                // editing an existing contact with no pending changes
                if (isValid()) {
                    PersonInformationLog.createLog(person, Bundle.MESSAGING,
                            "log.personInformation.contact.generic.edit", infoLabel, this.getPresentationValue(), personViewed);

                } else {
                    // editing an existing pending contact (creation)
                    PersonInformationLog.createLog(person, Bundle.MESSAGING,
                            "log.personInformation.contact.generic.create.need.valid.edited", infoLabel,
                            this.getPresentationValue(), personViewed);
                }
            }
        } else if (valueChanged) {
            // value or physical address was changed
            if (hasPrevPartyContact()) {
                if (getPrevPartyContact().isValid()) {
                    // editing a valid existing contact
                    if (oldValueDiffersFromNew && createdNewContact) {
                        // new value differs from old, and a new temporary
                        // contact was created
                        PersonInformationLog.createLog(person, Bundle.MESSAGING,
                                "log.personInformation.contact.generic.edit.need.valid.values", infoLabel, getPrevPartyContact()
                                        .getPresentationValue(), newValue, personViewed);
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

    public void logDelete(Person person) {
    }

    public void logDeleteAux(Person person, String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        if (isValid()) {
            // it is valid, so it is not a pending change or creation
            PersonInformationLog.createLog(person, Bundle.MESSAGING,
                    "log.personInformation.contact.generic.remove", infoLabel, this.getPresentationValue(), personViewed);
        } else {
            if (!hasPrevPartyContact()) {
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

    public void logValid(Person person) {
    }

    public void logValidAux(Person person, String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        if (!hasPrevPartyContact()) {
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
                        "log.personInformation.contact.generic.edit.need.valid.values.accepted", infoLabel, getPrevPartyContact()
                                .getPresentationValue(), this.getPresentationValue(), personViewed);
            }
        }
    }

    public void logRefuse(Person person) {
    }

    public void logRefuseAux(Person person, String typeKey) {
        final String infoLabel = BundleUtil.getString(Bundle.APPLICATION, typeKey);
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(person);

        if (!hasPrevPartyContact()) {
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
                        "log.personInformation.contact.generic.edit.need.valid.values.rejected", infoLabel, getPrevPartyContact()
                                .getPresentationValue(), this.getPresentationValue(), personViewed);
            }
        }
    }

    @Deprecated
    public boolean hasCurrentPartyContact() {
        return getCurrentPartyContact() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasPartyContactValidation() {
        return getPartyContactValidation() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasVisibleToPublic() {
        return getVisibleToPublic() != null;
    }

    @Deprecated
    public boolean hasVisibleToEmployees() {
        return getVisibleToEmployees() != null;
    }

    @Deprecated
    public boolean hasVisibleToTeachers() {
        return getVisibleToTeachers() != null;
    }

    @Deprecated
    public boolean hasVisibleToAlumni() {
        return getVisibleToAlumni() != null;
    }

    @Deprecated
    public boolean hasDefaultContact() {
        return getDefaultContact() != null;
    }

    @Deprecated
    public boolean hasVisibleToStudents() {
        return getVisibleToStudents() != null;
    }

    @Deprecated
    public boolean hasPrevPartyContact() {
        return getPrevPartyContact() != null;
    }

    private static Set<PartyContact> getAllInstancesOf(Class<? extends PartyContact> type) {
        return Sets.<PartyContact> newHashSet(Iterables.filter(Bennu.getInstance().getPartyContactsSet(), type));
    }
}
