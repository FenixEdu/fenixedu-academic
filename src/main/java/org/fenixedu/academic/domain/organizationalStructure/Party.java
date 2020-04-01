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
/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;

public abstract class Party extends Party_Base implements Comparable<Party> {

    static final public Comparator<Party> COMPARATOR_BY_NAME = new Comparator<Party>() {
        @Override
        public int compare(final Party o1, final Party o2) {
            return Collator.getInstance().compare(o1.getName(), o2.getName());
        }
    };

    static final public Comparator<Party> COMPARATOR_BY_SUBPARTY = new Comparator<Party>() {
        @Override
        public int compare(final Party o1, final Party o2) {
            if ((o1 instanceof Person) && (o2 instanceof Unit)) {
                return 1;
            } else if ((o1 instanceof Unit) && (o2 instanceof Person)) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    static final public Comparator<Party> COMPARATOR_BY_NAME_AND_ID = new Comparator<Party>() {
        @Override
        public int compare(final Party o1, final Party o2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(Party.COMPARATOR_BY_NAME);
            comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

            return comparatorChain.compare(o1, o2);
        }
    };

    static final public Comparator<Party> COMPARATOR_BY_SUBPARTY_AND_NAME_AND_ID = new Comparator<Party>() {
        @Override
        public int compare(final Party o1, final Party o2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(Party.COMPARATOR_BY_SUBPARTY);
            comparatorChain.addComparator(Party.COMPARATOR_BY_NAME);
            comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
            return comparatorChain.compare(o1, o2);
        }
    };

    public abstract String getPartyPresentationName();

    public abstract LocalizedString getPartyName();

    public abstract String getName();

    public Party() {
        super();
        setRootDomainObject(Bennu.getInstance());
        createAccount(AccountType.INTERNAL);
        createAccount(AccountType.EXTERNAL);
    }

    @Deprecated
    @Override
    final public Country getNationality() {
        return getCountry();
    }

    @Deprecated
    @Override
    public void setNationality(final Country country) {
        setCountry(country);
    }

    public Country getCountry() {
        return super.getNationality();
    }

    public void setCountry(final Country country) {
        super.setNationality(country);
    }

    public Account createAccount(AccountType accountType) {
        checkAccountsFor(accountType);
        return new Account(accountType, this);
    }

    private void checkAccountsFor(AccountType accountType) {
        if (getAccountBy(accountType) != null) {
            throw new DomainException("error.party.accounts.accountType.already.exist");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Party> Set<T> getPartysSet(Class<T> input) {
        Set<T> partySet = new HashSet<T>();

        for (Party party : Bennu.getInstance().getPartysSet()) {
            if (input.isAssignableFrom(party.getClass())) {
                partySet.add((T) party);
            }
        }

        return partySet;
    }

    public Account getAccountBy(AccountType accountType) {
        for (final Account account : getAccountsSet()) {
            if (account.getAccountType() == accountType) {
                return account;
            }
        }
        return null;
    }

    public Account getInternalAccount() {
        return getAccountBy(AccountType.INTERNAL);
    }

    public Account getExternalAccount() {
        return getAccountBy(AccountType.EXTERNAL);
    }

    public PartyTypeEnum getType() {
        return getPartyType() != null ? getPartyType().getType() : null;
    }

    public void setType(PartyTypeEnum partyTypeEnum) {
        if (partyTypeEnum != null) {
            PartyType partyType = PartyType.readPartyTypeByType(partyTypeEnum);
            if (partyType == null) {
                throw new DomainException("error.Party.unknown.partyType");
            }
            setPartyType(partyType);
        } else {
            setPartyType(null);
        }
    }

    public Collection<? extends Party> getCurrentParentParties(AccountabilityTypeEnum accountabilityTypeEnum,
            Class<? extends Party> parentPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.isActive() && accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
                result.add(accountability.getParentParty());
            }
        }
        return result;
    }

    public Collection<? extends Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum,
            Class<? extends Party> parentPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
                result.add(accountability.getParentParty());
            }
        }
        return result;
    }

    public Collection<? extends Party> getParentParties(Class<? extends Party> parentPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
                result.add(accountability.getParentParty());
            }
        }
        return result;
    }

    public Collection<? extends Party> getParentPartiesByDates(AccountabilityTypeEnum accountabilityTypeEnum,
            Class<? extends Party> parentPartyClass, DateTime dateTime) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())
                    && accountability.getBeginDate().toDateTimeAtMidnight().isBefore(dateTime)) {
                if (accountability.getEndDate() == null) {
                    result.add(accountability.getParentParty());
                }
                if (accountability.getEndDate() != null
                        && accountability.getEndDate().plusDays(1).toDateTimeAtMidnight().minusMillis(1).isAfter(dateTime)) {
                    result.add(accountability.getParentParty());
                }
            }
        }
        return result;
    }

    public Collection<? extends Party> getParentParties(List<AccountabilityTypeEnum> accountabilityTypeEnums,
            Class<? extends Party> parentPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
                    && parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
                result.add(accountability.getParentParty());
            }
        }
        return result;
    }

    public Collection<Unit> getParentUnits() {
        return (Collection<Unit>) getParentParties(Unit.class);
    }

    public Collection<Unit> getParentUnits(String accountabilityTypeEnum) {
        return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.valueOf(accountabilityTypeEnum), Unit.class);
    }

    public Collection<Unit> getCurrentParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
        return (Collection<Unit>) getCurrentParentParties(accountabilityTypeEnum, Unit.class);
    }

    public Collection<Unit> getParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
        return (Collection<Unit>) getParentParties(accountabilityTypeEnum, Unit.class);
    }

    public Collection<Unit> getParentUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        return (Collection<Unit>) getParentParties(accountabilityTypeEnums, Unit.class);
    }

    public Collection<Unit> getSubUnits() {
        return (Collection<Unit>) getChildParties(Unit.class);
    }

    public Collection<? extends Party> getChildParties(Class<? extends Party> childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    public Collection<? extends Party> getChildParties(AccountabilityTypeEnum accountabilityTypeEnum,
            Class<? extends Party> childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    public Collection<? extends Party> getActiveChildParties(AccountabilityTypeEnum accountabilityTypeEnum,
            Class<? extends Party> childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.isActive() && accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    public Collection<? extends Party> getChildParties(List<AccountabilityTypeEnum> accountabilityTypeEnums,
            Class<? extends Party> childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
                    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    protected Collection<? extends Party> getChildParties(PartyTypeEnum type, Class<? extends Party> childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getChildParty().getType() == type
                    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum,
            Class<? extends Accountability> accountabilityClass) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && accountabilityClass.isAssignableFrom(accountability.getClass())) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(Class<? extends Accountability> accountabilityClass,
            AccountabilityTypeEnum... types) {
        final Set<Accountability> result = new HashSet<Accountability>();

        for (final Accountability accountability : getChildsSet()) {
            AccountabilityTypeEnum accountabilityType = accountability.getAccountabilityType().getType();

            if (!isOneOfTypes(accountabilityType, types)) {
                continue;
            }

            if (!accountabilityClass.isAssignableFrom(accountability.getClass())) {
                continue;
            }

            result.add(accountability);
        }

        return result;
    }

    private boolean isOneOfTypes(AccountabilityTypeEnum type, AccountabilityTypeEnum[] possibilities) {
        for (AccountabilityTypeEnum t : possibilities) {
            if (t == type) {
                return true;
            }
        }

        return false;
    }

    public Collection<? extends Accountability> getParentAccountabilitiesByParentClass(Class<? extends Party> parentClass) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getParentsSet()) {
            if (parentClass.isAssignableFrom(accountability.getParentParty().getClass())) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByChildClass(Class<? extends Party> childClass) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getChildsSet()) {
            if (childClass.isAssignableFrom(accountability.getChildParty().getClass())) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByAccountabilityClass(
            Class<? extends Accountability> accountabilityClass) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountabilityClass.isAssignableFrom(accountability.getClass())) {
                result.add(accountability);
            }
        }
        return result;
    }

    protected void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        for (; !getAccountsSet().isEmpty(); getAccountsSet().iterator().next().delete()) {
            ;
        }
        for (; !getPartyContactsSet().isEmpty(); getPartyContactsSet().iterator().next().deleteWithoutCheckRules()) {
            ;
        }

        if (getPartySocialSecurityNumber() != null) {
            getPartySocialSecurityNumber().delete();
        }

        super.setNationality(null);
        setPartyType(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    public static Party readByContributorNumber(String contributorNumber) {
        return PartySocialSecurityNumber.readPartyBySocialSecurityNumber(contributorNumber);
    }

    public String getSocialSecurityNumber() {
        final PartySocialSecurityNumber partySocialSecurityNumber = getPartySocialSecurityNumber();
        return partySocialSecurityNumber != null ? partySocialSecurityNumber.getSocialSecurityNumber() : null;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        socialSecurityNumber = StringUtils.trimToNull(socialSecurityNumber);
        if (socialSecurityNumber != null && !StringUtils.isBlank(socialSecurityNumber)) {
            if (!PartySocialSecurityNumber.isValid(socialSecurityNumber)) {
                throw new DomainException("label.person.details.vatNumber.invalid", socialSecurityNumber);
            }

            if (getPartySocialSecurityNumber() != null
                    && socialSecurityNumber.equals(getPartySocialSecurityNumber().getSocialSecurityNumber())) {
                return;
            }
            if (getPartySocialSecurityNumber() != null) {
                getPartySocialSecurityNumber().setSocialSecurityNumber(socialSecurityNumber);
            } else {
                new PartySocialSecurityNumber(this, socialSecurityNumber);
            }
        } else {
            final PartySocialSecurityNumber partySocialSecurityNumber = getPartySocialSecurityNumber();
            if (partySocialSecurityNumber != null) {
                partySocialSecurityNumber.delete();
            }
        }
    }

    public boolean isPerson() {
        return false;
    }

    public boolean isUnit() {
        return false;
    }

    public boolean isDepartmentUnit() {
        return false;
    }

    public boolean isCompetenceCourseGroupUnit() {
        return false;
    }

    public boolean isScientificAreaUnit() {
        return false;
    }

    public boolean isAdministrativeOfficeUnit() {
        return false;
    }

    public boolean isDegreeUnit() {
        return false;
    }

    public boolean isAcademicalUnit() {
        return false;
    }

    public boolean isSchoolUnit() {
        return false;
    }

    public boolean isUniversityUnit() {
        return false;
    }

    public boolean isPlanetUnit() {
        return false;
    }

    public boolean isCountryUnit() {
        return false;
    }

    public boolean isSectionUnit() {
        return false;
    }

    public boolean isAggregateUnit() {
        return false;
    }

    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
        return false;
    }

    public boolean hasDepartment() {
        return false;
    }

    public boolean verifyNameEquality(String[] nameWords) {
        if (nameWords == null) {
            return true;
        }
        if (getName() != null) {
            String[] personNameWords = StringNormalizer.normalize(getName()).trim().split(" ");
            int j, i;
            for (i = 0; i < nameWords.length; i++) {
                if (!nameWords[i].equals("")) {
                    for (j = 0; j < personNameWords.length; j++) {
                        if (personNameWords[j].equals(nameWords[i])) {
                            break;
                        }
                    }
                    if (j == personNameWords.length) {
                        return false;
                    }
                }
            }
            if (i == nameWords.length) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPartyContact(final Class<? extends PartyContact> clazz, final PartyContactType type, final String value) {
        final List<? extends PartyContact> allPartyContacts = getPartyContacts(clazz, type);
        for (PartyContact contact : allPartyContacts) {
            if (contact.hasValue(value)) {
                return true;
            }
        }
        return false;
    }

    public List<? extends PartyContact> getAllPartyContacts(final Class<? extends PartyContact> clazz, final PartyContactType type) {
        final List<PartyContact> result = new ArrayList<PartyContact>();
        for (final PartyContact contact : getPartyContactsSet()) {
            if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)) {
                result.add(contact);
            }
        }
        return result;
    }

    public List<? extends PartyContact> getAllPartyContacts(final Class<? extends PartyContact> clazz) {
        return getAllPartyContacts(clazz, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T extends PartyContact> Stream<T> getPartyContactStream(final Class<T> clazz, final PartyContactType type) {
        final Stream<PartyContact> stream = getPartyContactsSet().stream();
        return (Stream) stream.filter(c -> clazz.isAssignableFrom(c.getClass()) && (type == null || c.getType() == type) && c.isActiveAndValid());
    }

    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz, final PartyContactType type) {
        final List<PartyContact> result = new ArrayList<PartyContact>();
        for (final PartyContact contact : getPartyContactsSet()) {
            if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)
                    && contact.isActiveAndValid()) {
                result.add(contact);
            }
        }
        return result;
    }

    public List<? extends PartyContact> getPendingOrValidPartyContacts(final Class<? extends PartyContact> clazz,
            final PartyContactType type) {
        final List<PartyContact> result = new ArrayList<PartyContact>();
        for (final PartyContact contact : getPartyContactsSet()) {
            if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)
                    && (contact.isActiveAndValid() || contact.waitsValidation())) {
                result.add(contact);
            }
        }
        return result;
    }

    public List<? extends PartyContact> getPendingOrValidPartyContacts(final Class<? extends PartyContact> clazz) {
        return getPendingOrValidPartyContacts(clazz, null);
    }

    public List<? extends PartyContact> getPendingPartyContacts(final Class<? extends PartyContact> clazz,
            final PartyContactType type) {
        final List<PartyContact> result = new ArrayList<PartyContact>();
        for (final PartyContact contact : getPartyContactsSet()) {
            if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)
                    && contact.waitsValidation()) {
                result.add(contact);
            }
        }
        return result;
    }

    public List<? extends PartyContact> getAllPendingPartyContacts() {
        final List<PartyContact> result = new ArrayList<PartyContact>();
        for (final PartyContact contact : getPartyContactsSet()) {
            if (contact.waitsValidation()) {
                result.add(contact);
            }
        }
        return result;
    }

    public <T extends PartyContact> Stream<T> getPartyContactStream(final Class<T> clazz) {
        return getPartyContactStream(clazz, null);
    }

    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz) {
        return getPartyContacts(clazz, null);
    }

    public List<? extends PartyContact> getPendingPartyContacts(final Class<? extends PartyContact> clazz) {
        return getPendingPartyContacts(clazz, null);
    }

    public boolean hasPendingPartyContacts(final Class<? extends PartyContact> clazz) {
        return getPendingPartyContacts(clazz, null).size() > 0;
    }

    public boolean hasPendingPartyContacts() {
        return getAllPendingPartyContacts().size() > 0;
    }

    public boolean hasAnyPartyContact(final Class<? extends PartyContact> clazz, final PartyContactType type) {
        for (final PartyContact contact : getPartyContactsSet()) {
            if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)
                    && contact.isActiveAndValid()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyPartyContact(final Class<? extends PartyContact> clazz) {
        return hasAnyPartyContact(clazz, null);
    }

    public PartyContact getDefaultPartyContact(final Class<? extends PartyContact> clazz) {
        for (final PartyContact contact : getPartyContactsSet()) {
            if (clazz.isAssignableFrom(contact.getClass()) && contact.isDefault() && contact.isActiveAndValid()) {
                return contact;
            }
        }
        return null;
    }

    public boolean hasDefaultPartyContact(final Class<? extends PartyContact> clazz) {
        return getDefaultPartyContact(clazz) != null;
    }

    public PartyContact getInstitutionalPartyContact(final Class<? extends PartyContact> clazz) {
        List<EmailAddress> institutionals =
                (List<EmailAddress>) getPartyContacts(EmailAddress.class, PartyContactType.INSTITUTIONAL);
        return institutionals.isEmpty() ? null : institutionals.iterator().next();
    }

    public boolean hasInstitutionalPartyContact(final Class<? extends PartyContact> clazz) {
        return getInstitutionalPartyContact(clazz) != null;
    }

    /*
     * WebAddress
     */
    public List<WebAddress> getWebAddresses() {
        return (List<WebAddress>) getPartyContacts(WebAddress.class);
    }

    public List<WebAddress> getPendingWebAddresses() {
        return (List<WebAddress>) getPendingPartyContacts(WebAddress.class);
    }

    public boolean hasDefaultWebAddress() {
        return hasDefaultPartyContact(WebAddress.class);
    }

    public WebAddress getDefaultWebAddress() {
        return (WebAddress) getDefaultPartyContact(WebAddress.class);
    }

    public String getDefaultWebAddressUrl() {
        return hasDefaultWebAddress() ? getDefaultWebAddress().getUrl() : StringUtils.EMPTY;
    }

    public void setDefaultWebAddressUrl(final String url) {
        if (hasDefaultWebAddress()) {
            getDefaultWebAddress().edit(url);
        } else {
            WebAddress.createWebAddress(this, url, PartyContactType.PERSONAL, true);
        }
    }

    /**
     * @deprecated {@link #getDefaultWebAddressUrl()}
     */
    @Deprecated
    public String getWebAddress() {
        return getDefaultWebAddressUrl();
    }

    /**
     * @deprecated {@link #setDefaultWebAddressUrl(String)}
     */
    @Deprecated
    public void setWebAddress(String webAddress) {
        setDefaultWebAddressUrl(webAddress);
    }

    /*
     * Phone
     */
    public List<Phone> getPhones() {
        return (List<Phone>) getPartyContacts(Phone.class);
    }

    public List<Phone> getPendingPhones() {
        return (List<Phone>) getPendingPartyContacts(Phone.class);
    }

    public List<Phone> getPendingOrValidPhones() {
        return (List<Phone>) getPendingOrValidPartyContacts(Phone.class);
    }

    public boolean hasDefaultPhone() {
        return hasDefaultPartyContact(Phone.class);
    }

    public Phone getDefaultPhone() {
        return (Phone) getDefaultPartyContact(Phone.class);
    }

    public String getDefaultPhoneNumber() {
        return hasDefaultPhone() ? getDefaultPhone().getNumber() : StringUtils.EMPTY;
    }

    public void setDefaultPhoneNumber(final String number) {
        setDefaultPhoneNumber(number, false);
    }

    public void setDefaultPhoneNumber(final String number, boolean valid) {
        final Phone defaultPhone;
        if (hasDefaultPhone()) {
            defaultPhone = getDefaultPhone();
            defaultPhone.edit(number);
        } else {
            defaultPhone = Phone.createPhone(this, number, PartyContactType.PERSONAL, true);
            for (final PartyContact contact : getPartyContactsSet()) {
                if (contact != defaultPhone && contact.isPhone() && !contact.isActiveAndValid()) {
                    contact.delete();
                }
            }
        }

        if (valid) {
            defaultPhone.setValid();
        }
    }

    /**
     * This should not be used because assumes that there is only one work phone.
     */
    @Deprecated
    public void setWorkPhoneNumber(final String number) {
        if (hasAnyPartyContact(Phone.class, PartyContactType.WORK)) {
            ((Phone) getPartyContacts(Phone.class, PartyContactType.WORK).iterator().next()).edit(number);
        } else {
            Phone.createPhone(this, number, PartyContactType.WORK, false);
        }
    }

    /**
     * @deprecated {@link #getDefaultPhoneNumber()}
     */
    @Deprecated
    public String getPhone() {
        return getDefaultPhoneNumber();
    }

    /**
     * @deprecated {@link #setDefaultPhoneNumber(String)}
     */
    @Deprecated
    public void setPhone(String phone) {
        setDefaultPhoneNumber(phone);
    }

    // Currently, a Person can only have one WorkPhone (so use get(0) -
    // after
    // interface updates remove these methods)
    public Phone getPersonWorkPhone() {
        final List<Phone> partyContacts = (List<Phone>) getPartyContacts(Phone.class, PartyContactType.WORK);
        // actually exists only one
        return partyContacts.isEmpty() ? null : (Phone) partyContacts.iterator().next();
    }

    @Deprecated
    public String getWorkPhone() {
        final Phone workPhone = getPersonWorkPhone();
        return workPhone != null ? workPhone.getNumber() : null;
    }

    @Deprecated
    public void setWorkPhone(String workPhone) {
        setWorkPhoneNumber(workPhone);
    }

    /*
     * MobilePhone
     */
    public List<MobilePhone> getMobilePhones() {
        return (List<MobilePhone>) getPartyContacts(MobilePhone.class);
    }

    public List<MobilePhone> getPendingMobilePhones() {
        return (List<MobilePhone>) getPendingPartyContacts(MobilePhone.class);
    }

    public List<MobilePhone> getPendingOrValidMobilePhones() {
        return (List<MobilePhone>) getPendingOrValidPartyContacts(MobilePhone.class);
    }

    public boolean hasDefaultMobilePhone() {
        return hasDefaultPartyContact(MobilePhone.class);
    }

    public MobilePhone getDefaultMobilePhone() {
        return (MobilePhone) getDefaultPartyContact(MobilePhone.class);
    }

    public String getDefaultMobilePhoneNumber() {
        return hasDefaultMobilePhone() ? getDefaultMobilePhone().getNumber() : StringUtils.EMPTY;
    }

    public void setDefaultMobilePhoneNumber(final String number) {
        setDefaultMobilePhoneNumber(number, false);
    }

    public void setDefaultMobilePhoneNumber(final String number, final boolean valid) {
        MobilePhone mobilePhone;
        if (hasDefaultMobilePhone()) {
            mobilePhone = getDefaultMobilePhone();
            mobilePhone.edit(number);
        } else {
            mobilePhone = MobilePhone.createMobilePhone(this, number, PartyContactType.PERSONAL, true);
            for (final PartyContact contact : getPartyContactsSet()) {
                if (contact != mobilePhone && contact.isMobile() && !contact.isActiveAndValid()) {
                    contact.delete();
                }
            }
        }

        if (valid) {
            mobilePhone.setValid();
        }
    }

    /**
     * @deprecated  {@link getDefaultMobilePhoneNumber}
     */
    @Deprecated
    public String getMobile() {
        return getDefaultMobilePhoneNumber();
    }

    /**
     * @deprecated  {@link setDefaultMobilePhoneNumber}
     */
    @Deprecated
    public void setMobile(String mobile) {
        setDefaultMobilePhoneNumber(mobile);
    }

    /*
     * EmailAddress
     */
    public Stream<EmailAddress> getEmailAddressStream() {
        return getPartyContactStream(EmailAddress.class);
    }

    /**
     * @deprecated  Use {@link getEmailAddressStream} instead
     */
    @Deprecated
    public List<EmailAddress> getEmailAddresses() {
        return (List<EmailAddress>) getPartyContacts(EmailAddress.class);
    }

    public List<EmailAddress> getPendingEmailAddresses() {
        return (List<EmailAddress>) getPendingPartyContacts(EmailAddress.class);
    }

    public List<EmailAddress> getPendingOrValidEmailAddresses() {
        return (List<EmailAddress>) getPendingOrValidPartyContacts(EmailAddress.class);
    }

    public boolean hasDefaultEmailAddress() {
        return hasDefaultPartyContact(EmailAddress.class);
    }

    public EmailAddress getDefaultEmailAddress() {
        return (EmailAddress) getDefaultPartyContact(EmailAddress.class);
    }

    public boolean hasInstitutionalEmailAddress() {
        return hasInstitutionalPartyContact(EmailAddress.class);
    }

    public EmailAddress getInstitutionalEmailAddress() {
        return (EmailAddress) getInstitutionalPartyContact(EmailAddress.class);
    }

    public EmailAddress getInstitutionalOrDefaultEmailAddress() {
        return hasInstitutionalEmailAddress() ? getInstitutionalEmailAddress() : getDefaultEmailAddress();
    }

    public String getDefaultEmailAddressValue() {
        return hasDefaultEmailAddress() ? getDefaultEmailAddress().getValue() : StringUtils.EMPTY;
    }

    public void setDefaultEmailAddressValue(final String email) {
        setDefaultEmailAddressValue(email, false, false);
    }

    public void setDefaultEmailAddressValue(final String email, final boolean valid) {
        setDefaultEmailAddressValue(email, valid, false);
    }

    public void setDefaultEmailAddressValue(final String email, final boolean valid, final boolean visibleToPublic) {
        if (!StringUtils.isEmpty(email)) {
            final EmailAddress emailAddress;
            if (hasDefaultEmailAddress()) {
                emailAddress = getDefaultEmailAddress();
                emailAddress.edit(email);
            } else {
                emailAddress = EmailAddress.createEmailAddress(this, email, PartyContactType.PERSONAL, true);
                for (final PartyContact contact : getPartyContactsSet()) {
                    if (contact != emailAddress && contact.isEmailAddress() && !contact.isActiveAndValid()) {
                        contact.delete();
                    }
                }
            }
            emailAddress.setVisibleToPublic(visibleToPublic);
            if (valid) {
                emailAddress.setValid();
            }
        }
    }

    public String getInstitutionalEmailAddressValue() {
        return hasInstitutionalEmailAddress() ? getInstitutionalEmailAddress().getValue() : StringUtils.EMPTY;
    }

    public void setInstitutionalEmailAddressValue(final String email) {
        if (hasInstitutionalEmailAddress()) {
            getInstitutionalEmailAddress().setValue(email);
        } else {
            EmailAddress emailAddress = EmailAddress.createEmailAddress(this, email, PartyContactType.INSTITUTIONAL, false);
            emailAddress.setValid();
        }
    }

    public String getInstitutionalOrDefaultEmailAddressValue() {
        EmailAddress email = getInstitutionalOrDefaultEmailAddress();
        return (email != null ? email.getValue() : StringUtils.EMPTY);
    }

    /**
     * @deprecated  {@link #getDefaultEmailAddressValue()}
     */
    @Deprecated
    public String getEmail() {
        return getDefaultEmailAddressValue();
    }

    /**
     * @deprecated {@link #setDefaultEmailAddressValue(String)}
     */
    @Deprecated
    public void setEmail(String email) {
        setDefaultEmailAddressValue(email);
    }

    /*
     * PhysicalAddress
     */
    public List<PhysicalAddress> getPhysicalAddresses() {
        return (List<PhysicalAddress>) getPartyContacts(PhysicalAddress.class);
    }

    public List<PhysicalAddress> getPendingPhysicalAddresses() {
        return (List<PhysicalAddress>) getPendingPartyContacts(PhysicalAddress.class);
    }

    public List<PhysicalAddress> getPendingOrValidPhysicalAddresses() {
        return (List<PhysicalAddress>) getPendingOrValidPartyContacts(PhysicalAddress.class);
    }

    public boolean hasDefaultPhysicalAddress() {
        return hasDefaultPartyContact(PhysicalAddress.class);
    }

    public PhysicalAddress getDefaultPhysicalAddress() {
        return (PhysicalAddress) getDefaultPartyContact(PhysicalAddress.class);
    }

    public void setDefaultPhysicalAddressData(final PhysicalAddressData data) {
        setDefaultPhysicalAddressData(data, false);
    }

    public void setDefaultPhysicalAddressData(final PhysicalAddressData data, final boolean valid) {
        PhysicalAddress defaultPhysicalAddress;
        if (hasDefaultPhysicalAddress()) {
            defaultPhysicalAddress = getDefaultPhysicalAddress();
            defaultPhysicalAddress.edit(data);
        } else {
            defaultPhysicalAddress = PhysicalAddress.createPhysicalAddress(this, data, PartyContactType.PERSONAL, true);
            for (final PartyContact contact : getPartyContactsSet()) {
                if (contact != defaultPhysicalAddress && contact.isPhysicalAddress() && !contact.isActiveAndValid()) {
                    contact.delete();
                }
            }
        }
        if (valid) {
            defaultPhysicalAddress.setValid();
        }
    }

    private PhysicalAddress getOrCreateDefaultPhysicalAddress() {
        final PhysicalAddress physicalAdress = getDefaultPhysicalAddress();
        return physicalAdress != null ? physicalAdress : PhysicalAddress.createPhysicalAddress(this, null,
                PartyContactType.PERSONAL, true);
    }

    public String getAddress() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getAddress() : StringUtils.EMPTY;
    }

    public void setAddress(String address) {
        getOrCreateDefaultPhysicalAddress().setAddress(address);
    }

    public String getAreaCode() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getAreaCode() : StringUtils.EMPTY;
    }

    public void setAreaCode(String areaCode) {
        getOrCreateDefaultPhysicalAddress().setAreaCode(areaCode);
    }

    public String getAreaOfAreaCode() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getAreaOfAreaCode() : StringUtils.EMPTY;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        getOrCreateDefaultPhysicalAddress().setAreaOfAreaCode(areaOfAreaCode);
    }

    public String getPostalCode() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getPostalCode() : StringUtils.EMPTY;
    }

    public String getArea() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getArea() : StringUtils.EMPTY;
    }

    public void setArea(String area) {
        getOrCreateDefaultPhysicalAddress().setArea(area);
    }

    public String getParishOfResidence() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getParishOfResidence() : StringUtils.EMPTY;
    }

    public void setParishOfResidence(String parishOfResidence) {
        getOrCreateDefaultPhysicalAddress().setParishOfResidence(parishOfResidence);
    }

    public String getDistrictSubdivisionOfResidence() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getDistrictSubdivisionOfResidence() : StringUtils.EMPTY;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
        getOrCreateDefaultPhysicalAddress().setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
    }

    public String getDistrictOfResidence() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getDistrictOfResidence() : StringUtils.EMPTY;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
        getOrCreateDefaultPhysicalAddress().setDistrictOfResidence(districtOfResidence);
    }

    public Country getCountryOfResidence() {
        return hasDefaultPhysicalAddress() ? getDefaultPhysicalAddress().getCountryOfResidence() : null;
    }

    public void setCountryOfResidence(Country countryOfResidence) {
        getOrCreateDefaultPhysicalAddress().setCountryOfResidence(countryOfResidence);
    }

    @Override
    public int compareTo(Party party) {
        return COMPARATOR_BY_NAME.compare(this, party);
    }

    public void logCreateContact(PartyContact contact) {
    }

    public void logEditContact(PartyContact contact, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact,
            String newValue) {
    }

    public void logDeleteContact(PartyContact contact) {
    }

    public void logValidContact(PartyContact contact) {
    }

    public void logRefuseContact(PartyContact contact) {
    }

}
