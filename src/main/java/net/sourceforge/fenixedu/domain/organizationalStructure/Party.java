/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.ResearchInterest.ResearchInterestComparator;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

    public abstract void setName(String name);

    public abstract String getName();

    public Party() {
        super();
        setRootDomainObject(Bennu.getInstance());
        createAccount(AccountType.INTERNAL);
        createAccount(AccountType.EXTERNAL);
    }

    @Override
    public void setPartyName(MultiLanguageString partyName) {
        if (partyName == null || partyName.isEmpty()) {
            throw new DomainException("error.Party.empty.partyName");
        }
        super.setPartyName(partyName);
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
        if (!canBeDeleted()) {
            throw new DomainException("error.party.cannot.be.deleted");
        }

        for (; !getAccounts().isEmpty(); getAccounts().iterator().next().delete()) {
            ;
        }
        for (; hasAnyPartyContacts(); getPartyContacts().iterator().next().deleteWithoutCheckRules()) {
            ;
        }

        if (hasPartySocialSecurityNumber()) {
            getPartySocialSecurityNumber().delete();
        }

        setNationality(null);
        setPartyType(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    private boolean canBeDeleted() {
        return !hasAnyResourceResponsibility() && !hasAnyVehicleAllocations() && !hasAnyPayedReceipts();
    }

    public static Party readByContributorNumber(String contributorNumber) {
        return PartySocialSecurityNumber.readPartyBySocialSecurityNumber(contributorNumber);
    }

    public String getSocialSecurityNumber() {
        return hasPartySocialSecurityNumber() ? getPartySocialSecurityNumber().getSocialSecurityNumber() : null;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        socialSecurityNumber = StringUtils.trimToNull(socialSecurityNumber);
        if (socialSecurityNumber != null && !StringUtils.isBlank(socialSecurityNumber)) {
            if (hasPartySocialSecurityNumber()
                    && socialSecurityNumber.equals(getPartySocialSecurityNumber().getSocialSecurityNumber())) {
                return;
            }

            final Party party = PartySocialSecurityNumber.readPartyBySocialSecurityNumber(socialSecurityNumber);
            if (party != null && party != this) {
                throw new DomainException("error.party.existing.contributor.number");
            } else {
                if (hasPartySocialSecurityNumber()) {
                    getPartySocialSecurityNumber().setSocialSecurityNumber(socialSecurityNumber);
                } else {
                    new PartySocialSecurityNumber(this, socialSecurityNumber);
                }
            }
        }
    }

    public void editContributor(String contributorName, String contributorNumber, String contributorAddress, String areaCode,
            String areaOfAreaCode, String area, String parishOfResidence, String districtSubdivisionOfResidence,
            String districtOfResidence) {
        setName(contributorName);
        setSocialSecurityNumber(contributorNumber);
        setDefaultPhysicalAddressData(new PhysicalAddressData(contributorAddress, areaCode, areaOfAreaCode, area,
                parishOfResidence, districtSubdivisionOfResidence, districtOfResidence, null));
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

    public boolean isResearchUnit() {
        return false;
    }

    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
        return false;
    }

    public boolean hasAdministrativeOffice() {
        return false;
    }

    public boolean hasDegree() {
        return false;
    }

    public boolean hasDepartment() {
        return false;
    }

    @Deprecated
    public abstract PartyClassification getPartyClassification();

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

    private List<? extends Participation> filterParticipationsByYear(List<? extends Participation> participations,
            ExecutionYear begin, ExecutionYear end) {
        List<Participation> participationsForInterval = new ArrayList<Participation>();
        for (Participation participation : participations) {
            Integer year = participation.getCivilYear();
            if (year == null || (begin == null || begin.isBeforeCivilYear(year) || begin.belongsToCivilYear(year))
                    && (end == null || end.isAfterCivilYear(year) || end.belongsToCivilYear(year))) {
                participationsForInterval.add(participation);
            }
        }
        return participationsForInterval;
    }

    private List<? extends Participation> filterParticipationsByType(Class<? extends Participation> clazz, ScopeType scopeType) {
        List<Participation> participations = new ArrayList<Participation>();
        for (Participation participation : getParticipations()) {
            if (participation.getClass().equals(clazz) && (scopeType == null || participation.scopeMatches(scopeType))) {
                participations.add(participation);
            }
        }
        return participations;
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        return (List<EventEditionParticipation>) filterParticipationsByYear(getEventEditionParticipations(type), begin, end);
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ExecutionYear begin, ExecutionYear end) {
        return (List<EventEditionParticipation>) filterParticipationsByYear(getEventEditionParticipations(), begin, end);
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ScopeType type) {
        return (List<EventEditionParticipation>) filterParticipationsByType(EventEditionParticipation.class, type);
    }

    public List<EventEditionParticipation> getEventEditionParticipations() {
        return getEventEditionParticipations(null);
    }

    public List<EventParticipation> getEventParticipations(ScopeType type) {
        return (List<EventParticipation>) filterParticipationsByType(EventParticipation.class, type);
    }

    public Set<EventEdition> getAssociatedEventEditions(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        Set<EventEdition> eventEditions = new HashSet<EventEdition>();
        for (EventEditionParticipation participation : getEventEditionParticipations(type, begin, end)) {
            eventEditions.add(participation.getEventEdition());
        }
        return eventEditions;
    }

    public Set<EventEdition> getAssociatedEventEditions(ExecutionYear begin, ExecutionYear end) {
        return getAssociatedEventEditions(null, begin, end);
    }

    public Set<EventEdition> getAssociatedEventEditions() {
        return getAssociatedEventEditions(null);
    }

    public Set<EventEdition> getAssociatedEventEditions(ScopeType type) {
        return getAssociatedEventEditions(type, null, null);
    }

    public List<EventParticipation> getEventParticipations(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        return (List<EventParticipation>) filterParticipationsByYear(getEventParticipations(type), begin, end);
    }

    public List<EventParticipation> getEventParticipation(ExecutionYear begin, ExecutionYear end) {
        return (List<EventParticipation>) filterParticipationsByYear(getEventParticipations(), begin, end);
    }

    public List<EventParticipation> getEventParticipations() {
        return getEventParticipations(null);
    }

    public Set<ResearchEvent> getAssociatedEvents(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        Set<ResearchEvent> events = new HashSet<ResearchEvent>();
        for (EventParticipation participation : getEventParticipations(type, begin, end)) {
            events.add(participation.getEvent());
        }
        return events;
    }

    public Set<ResearchEvent> getAssociatedEvents(ExecutionYear begin, ExecutionYear end) {
        return getAssociatedEvents(null, begin, end);
    }

    public Set<ResearchEvent> getAssociatedEvents(ScopeType type) {
        return getAssociatedEvents(type, null, null);
    }

    public Set<ResearchEvent> getAssociatedEvents() {
        return getAssociatedEvents(null);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ScopeType type, ExecutionYear begin,
            ExecutionYear end) {
        return (List<ScientificJournalParticipation>) filterParticipationsByYear(getScientificJournalParticipations(type), begin,
                end);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ExecutionYear begin, ExecutionYear end) {
        return (List<ScientificJournalParticipation>) filterParticipationsByYear(getScientificJournalParticipations(), begin, end);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ScopeType type) {
        return (List<ScientificJournalParticipation>) filterParticipationsByType(ScientificJournalParticipation.class, type);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations() {
        return getScientificJournalParticipations(null);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        Set<ScientificJournal> journals = new HashSet<ScientificJournal>();
        for (ScientificJournalParticipation participation : getScientificJournalParticipations(type, begin, end)) {
            journals.add(participation.getScientificJournal());
        }
        return journals;
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ExecutionYear begin, ExecutionYear end) {
        return getAssociatedScientificJournals(null, begin, end);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ScopeType type) {
        return getAssociatedScientificJournals(type, null, null);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals() {
        return getAssociatedScientificJournals(null);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        return (List<JournalIssueParticipation>) filterParticipationsByYear(getJournalIssueParticipations(type), begin, end);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ExecutionYear begin, ExecutionYear end) {
        return (List<JournalIssueParticipation>) filterParticipationsByYear(getJournalIssueParticipations(), begin, end);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ScopeType type) {
        return (List<JournalIssueParticipation>) filterParticipationsByType(JournalIssueParticipation.class, type);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations() {
        return getJournalIssueParticipations(null);
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ScopeType type, ExecutionYear begin, ExecutionYear end) {
        Set<JournalIssue> issues = new HashSet<JournalIssue>();
        for (JournalIssueParticipation participation : this.getJournalIssueParticipations(type, begin, end)) {
            issues.add(participation.getJournalIssue());
        }
        return issues;
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ExecutionYear begin, ExecutionYear end) {
        return getAssociatedJournalIssues(null, begin, end);
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ScopeType locationType) {
        return getAssociatedJournalIssues(locationType, null, null);
    }

    public Set<JournalIssue> getAssociatedJournalIssues() {
        return getAssociatedJournalIssues(null);
    }

    public List<CooperationParticipation> getCooperationParticipations(ExecutionYear begin, ExecutionYear end) {
        return (List<CooperationParticipation>) filterParticipationsByYear(getCooperationParticipations(), begin, end);
    }

    public List<CooperationParticipation> getCooperationParticipations() {
        List<CooperationParticipation> cooperationParticipations = new ArrayList<CooperationParticipation>();
        for (Participation participation : this.getParticipations()) {
            if (participation.isCooperationParticipation()) {
                cooperationParticipations.add((CooperationParticipation) participation);
            }
        }
        return cooperationParticipations;
    }

    public Set<Cooperation> getAssociatedCooperations(ExecutionYear begin, ExecutionYear end) {
        Set<Cooperation> cooperations = new HashSet<Cooperation>();
        for (CooperationParticipation participation : getCooperationParticipations(begin, end)) {
            cooperations.add(participation.getCooperation());
        }
        return cooperations;
    }

    public Set<Cooperation> getAssociatedCooperations() {
        return getAssociatedCooperations(null, null);
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
     * @use {@link #getDefaultWebAddressUrl()}
     */
    @Deprecated
    public String getWebAddress() {
        return getDefaultWebAddressUrl();
    }

    /**
     * @use {@link #setDefaultWebAddressUrl(String)}
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
     * @use {@link #getDefaultPhoneNumber()}
     */
    @Deprecated
    public String getPhone() {
        return getDefaultPhoneNumber();
    }

    /**
     * @use {@link #setDefaultPhoneNumber(String)}
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
        }

        if (valid) {
            mobilePhone.setValid();
        }
    }

    /**
     * @use {@link getDefaultMobilePhoneNumber}
     */
    @Deprecated
    public String getMobile() {
        return getDefaultMobilePhoneNumber();
    }

    /**
     * @use {@link setDefaultMobilePhoneNumber}
     */
    @Deprecated
    public void setMobile(String mobile) {
        setDefaultMobilePhoneNumber(mobile);
    }

    /*
     * EmailAddress
     */
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
     * @use {@link #getDefaultEmailAddressValue()}
     */
    @Deprecated
    public String getEmail() {
        return getDefaultEmailAddressValue();
    }

    /**
     * @use {@link #setDefaultEmailAddressValue(String)}
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

    protected List<ResearchResultPublication> filterArticlesWithType(List<ResearchResultPublication> publications,
            ScopeType locationType) {
        List<ResearchResultPublication> publicationsOfType = new ArrayList<ResearchResultPublication>();
        for (ResearchResultPublication publication : publications) {
            Article article = (Article) publication;
            if (article.getScope().equals(locationType)) {
                publicationsOfType.add(publication);
            }
        }
        return publicationsOfType;
    }

    protected List<ResearchResultPublication> filterInproceedingsWithType(List<ResearchResultPublication> publications,
            ScopeType locationType) {
        List<ResearchResultPublication> publicationsOfType = new ArrayList<ResearchResultPublication>();
        for (ResearchResultPublication publication : publications) {
            Inproceedings inproceedings = (Inproceedings) publication;
            if (inproceedings.getScope().equals(locationType)) {
                publicationsOfType.add(publication);
            }
        }
        return publicationsOfType;
    }

    protected List<ResearchResultPublication> filterResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz, List<ResearchResultPublication> publications) {
        return (List<ResearchResultPublication>) CollectionUtils.select(publications, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return clazz.equals(arg0.getClass());
            }
        });
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz) {
        return filterResultPublicationsByType(clazz, getResearchResultPublications());
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz, ExecutionYear executionYear) {
        return filterResultPublicationsByType(clazz, getResearchResultPublicationsByExecutionYear(executionYear));
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz, ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear) {
        return filterResultPublicationsByType(clazz,
                getResearchResultPublicationsByExecutionYear(firstExecutionYear, lastExecutionYear));
    }

    public List<ResearchResultPublication> getResearchResultPublicationsByExecutionYear(ExecutionYear executionYear) {

        List<ResearchResultPublication> publicationsForExecutionYear = new ArrayList<ResearchResultPublication>();
        for (ResearchResultPublication publication : getResearchResultPublications()) {
            if (publication.getYear() == null || executionYear.belongsToCivilYear(publication.getYear())) {
                publicationsForExecutionYear.add(publication);
            }
        }
        return publicationsForExecutionYear;
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByExecutionYear(ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear) {

        List<ResearchResultPublication> publicationsForExecutionYear = new ArrayList<ResearchResultPublication>();

        if (firstExecutionYear == null) {
            firstExecutionYear = ExecutionYear.readFirstExecutionYear();
        }
        if (lastExecutionYear == null || lastExecutionYear.isBefore(firstExecutionYear)) {
            lastExecutionYear = ExecutionYear.readLastExecutionYear();
        }

        for (ResearchResultPublication publication : getResearchResultPublications()) {
            if (publication.getYear() == null
                    || ((firstExecutionYear.isBeforeCivilYear(publication.getYear()) || firstExecutionYear
                            .belongsToCivilYear(publication.getYear())) && (lastExecutionYear.isAfterCivilYear(publication
                            .getYear()) || lastExecutionYear.belongsToCivilYear(publication.getYear())))) {
                publicationsForExecutionYear.add(publication);
            }

        }

        return publicationsForExecutionYear;
    }

    public List<Prize> getPrizes(ExecutionYear executionYear) {
        List<Prize> prizes = new ArrayList<Prize>();
        for (Prize prize : this.getPrizes()) {
            if (executionYear.belongsToCivilYear(prize.getYear())) {
                prizes.add(prize);
            }
        }
        return prizes;
    }

    public abstract List<ResearchResultPublication> getResearchResultPublications();

    public List<ResearchResultPublication> getBooks() {
        return this.getResearchResultPublicationsByType(Book.class);
    }

    public List<ResearchResultPublication> getBooks(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Book.class, executionYear);
    }

    public List<ResearchResultPublication> getBooks(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Book.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getInbooks() {
        return this.getResearchResultPublicationsByType(BookPart.class);
    }

    public List<ResearchResultPublication> getInbooks(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(BookPart.class, executionYear);
    }

    public List<ResearchResultPublication> getInbooks(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(BookPart.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType) {
        return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class), locationType);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, ExecutionYear executionYear) {
        return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class, executionYear), locationType);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear) {
        return filterArticlesWithType(
                this.getResearchResultPublicationsByType(Article.class, firstExecutionYear, lastExecutionYear), locationType);
    }

    public List<ResearchResultPublication> getArticles() {
        return this.getResearchResultPublicationsByType(Article.class);
    }

    public List<ResearchResultPublication> getArticles(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Article.class, executionYear);
    }

    public List<ResearchResultPublication> getArticles(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Article.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType) {
        return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class), locationType);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, ExecutionYear executionYear) {
        return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class, executionYear),
                locationType);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear) {
        return filterInproceedingsWithType(
                this.getResearchResultPublicationsByType(Inproceedings.class, firstExecutionYear, lastExecutionYear),
                locationType);
    }

    public List<ResearchResultPublication> getInproceedings() {
        return this.getResearchResultPublicationsByType(Inproceedings.class);
    }

    public List<ResearchResultPublication> getInproceedings(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Inproceedings.class, executionYear);
    }

    public List<ResearchResultPublication> getInproceedings(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Inproceedings.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getProceedings() {
        return this.getResearchResultPublicationsByType(Proceedings.class);
    }

    public List<ResearchResultPublication> getProceedings(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Proceedings.class, executionYear);
    }

    public List<ResearchResultPublication> getProceedings(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Proceedings.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getTheses() {
        return this.getResearchResultPublicationsByType(Thesis.class);
    }

    public List<ResearchResultPublication> getTheses(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Thesis.class, executionYear);
    }

    public List<ResearchResultPublication> getTheses(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Thesis.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getManuals() {
        return this.getResearchResultPublicationsByType(Manual.class);
    }

    public List<ResearchResultPublication> getManuals(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Manual.class, executionYear);
    }

    public List<ResearchResultPublication> getManuals(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Manual.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getTechnicalReports() {
        return ResearchResultPublication.sort(this.getResearchResultPublicationsByType(TechnicalReport.class));
    }

    public List<ResearchResultPublication> getTechnicalReports(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(TechnicalReport.class, executionYear);
    }

    public List<ResearchResultPublication> getTechnicalReports(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(TechnicalReport.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getOtherPublications() {
        return this.getResearchResultPublicationsByType(OtherPublication.class);
    }

    public List<ResearchResultPublication> getOtherPublications(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(OtherPublication.class, executionYear);
    }

    public List<ResearchResultPublication> getOtherPublications(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(OtherPublication.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getUnstructureds() {
        return this.getResearchResultPublicationsByType(Unstructured.class);
    }

    public List<ResearchResultPublication> getUnstructureds(ExecutionYear executionYear) {
        return this.getResearchResultPublicationsByType(Unstructured.class, executionYear);
    }

    public List<ResearchResultPublication> getUnstructureds(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
        return this.getResearchResultPublicationsByType(Unstructured.class, firstExecutionYear, lastExecutionYear);
    }

    public static String readAllResearchInterests() {
        JSONArray result = new JSONArray();
        for (Party party : Bennu.getInstance().getPartysSet()) {
            if (party instanceof Person && ((Person) party).getUsername() != null) {
                Person person = (Person) party;
                if (person.hasAnyResearchInterests()) {
                    JSONObject jsonPerson = new JSONObject();
                    jsonPerson.put("istId", person.getUsername());
                    SortedSet<ResearchInterest> sorted = new TreeSet<ResearchInterest>(new ResearchInterestComparator());
                    sorted.addAll(party.getResearchInterestsSet());
                    JSONArray interestsArray = new JSONArray();
                    for (ResearchInterest interest : sorted) {
                        JSONObject jsonInterest = new JSONObject();
                        for (Language langage : interest.getInterest().getAllLanguages()) {
                            jsonInterest.put(langage.toString(), interest.getInterest().getContent(langage));
                        }
                        interestsArray.add(jsonInterest);
                    }
                    jsonPerson.put("interests", interestsArray);
                    result.add(jsonPerson);
                }
            }
        }
        return result.toJSONString();
    }

    //
    // Site
    //

    public abstract Site getSite();

    protected abstract Site createSite();

    /**
     * Initializes the party's site. This method ensures that if the party has a site then no other is created and that site is
     * returned. Nevertheless if the party does not have a site, it is asked to create one by calling {@link #createSite()}. This
     * allows each specific party to create the appropriate site.
     * 
     * @return the newly created site or, if this party already contains a site, the currently existing one(publication.getYear())
     */
    public Site initializeSite() {
        Site site = getSite();

        if (site != null) {
            return site;
        } else {
            return createSite();
        }
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.contacts.PartyContact> getPartyContacts() {
        return getPartyContactsSet();
    }

    @Deprecated
    public boolean hasAnyPartyContacts() {
        return !getPartyContactsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.VehicleAllocation> getVehicleAllocations() {
        return getVehicleAllocationsSet();
    }

    @Deprecated
    public boolean hasAnyVehicleAllocations() {
        return !getVehicleAllocationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.documents.GeneratedDocument> getAddressedDocument() {
        return getAddressedDocumentSet();
    }

    @Deprecated
    public boolean hasAnyAddressedDocument() {
        return !getAddressedDocumentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.project.ProjectParticipation> getProjectParticipations() {
        return getProjectParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyProjectParticipations() {
        return !getProjectParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Accountability> getChilds() {
        return getChildsSet();
    }

    @Deprecated
    public boolean hasAnyChilds() {
        return !getChildsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.ResourceResponsibility> getResourceResponsibility() {
        return getResourceResponsibilitySet();
    }

    @Deprecated
    public boolean hasAnyResourceResponsibility() {
        return !getResourceResponsibilitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Account> getAccounts() {
        return getAccountsSet();
    }

    @Deprecated
    public boolean hasAnyAccounts() {
        return !getAccountsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard> getBoards() {
        return getBoardsSet();
    }

    @Deprecated
    public boolean hasAnyBoards() {
        return !getBoardsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Event> getEvents() {
        return getEventsSet();
    }

    @Deprecated
    public boolean hasAnyEvents() {
        return !getEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup> getPersistentAccessGroup() {
        return getPersistentAccessGroupSet();
    }

    @Deprecated
    public boolean hasAnyPersistentAccessGroup() {
        return !getPersistentAccessGroupSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.Participation> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityExternalScholarshipExemption> getPhdGratuityExternalScholarshipExemption() {
        return getPhdGratuityExternalScholarshipExemptionSet();
    }

    @Deprecated
    public boolean hasAnyPhdGratuityExternalScholarshipExemption() {
        return !getPhdGratuityExternalScholarshipExemptionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Invitation> getInvitationAccountabilities() {
        return getInvitationAccountabilitiesSet();
    }

    @Deprecated
    public boolean hasAnyInvitationAccountabilities() {
        return !getInvitationAccountabilitiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewPermissionUnit> getPermissionUnits() {
        return getPermissionUnitsSet();
    }

    @Deprecated
    public boolean hasAnyPermissionUnits() {
        return !getPermissionUnitsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.ResearchInterest> getResearchInterests() {
        return getResearchInterestsSet();
    }

    @Deprecated
    public boolean hasAnyResearchInterests() {
        return !getResearchInterestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.Prize> getPrizes() {
        return getPrizesSet();
    }

    @Deprecated
    public boolean hasAnyPrizes() {
        return !getPrizesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Accountability> getParents() {
        return getParentsSet();
    }

    @Deprecated
    public boolean hasAnyParents() {
        return !getParentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Guide> getPayedGuides() {
        return getPayedGuidesSet();
    }

    @Deprecated
    public boolean hasAnyPayedGuides() {
        return !getPayedGuidesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Receipt> getPayedReceipts() {
        return getPayedReceiptsSet();
    }

    @Deprecated
    public boolean hasAnyPayedReceipts() {
        return !getPayedReceiptsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingPartyHistory> getParkingPartyHistories() {
        return getParkingPartyHistoriesSet();
    }

    @Deprecated
    public boolean hasAnyParkingPartyHistories() {
        return !getParkingPartyHistoriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasParkingParty() {
        return getParkingParty() != null;
    }

    @Deprecated
    public boolean hasPartyType() {
        return getPartyType() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPartySocialSecurityNumber() {
        return getPartySocialSecurityNumber() != null;
    }

    @Deprecated
    public boolean hasQuestionBank() {
        return getQuestionBank() != null;
    }

    @Deprecated
    public boolean hasPartyName() {
        return getPartyName() != null;
    }

    @Deprecated
    public boolean hasPartyImportRegister() {
        return getPartyImportRegister() != null;
    }

    @Deprecated
    public boolean hasNationality() {
        return getNationality() != null;
    }

    @Deprecated
    public boolean hasBennuExternalScholarshipProvider() {
        return getRootDomainObjectExternalScholarshipProvider() != null;
    }

}
