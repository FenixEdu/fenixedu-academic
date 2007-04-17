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
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
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
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public abstract class Party extends Party_Base {

    public Party() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
	createAccount(AccountType.INTERNAL);
	createAccount(AccountType.EXTERNAL);
    }

    @Override
    public void setName(String name) {
	if (StringUtils.isEmpty(name)) {
	    throw new DomainException("error.party.empty.name");
	}
	super.setName(name);
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

    public Collection<? extends Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Party> parentPartyClass) {
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

    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
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

    public Collection<? extends Accountability> getChildAccountabilitiesByAccountabilityClass(Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    protected void delete() {
	for (; !getAccounts().isEmpty(); getAccounts().get(0).delete())
	    ;
	for (; hasAnyPartyContacts(); getPartyContacts().get(0).delete())
	    ;
	removePartyType();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public static Set<Party> readContributors() {
	ComparatorChain chain = new ComparatorChain();
	Comparator<Party> caseInsensitiveName = new Comparator<Party>() {
	    public int compare(Party party, Party otherParty) {
		Collator collator = Collator.getInstance();
		return collator.compare(party.getName().toLowerCase(), otherParty.getName().toLowerCase());
	    }
	};

	chain.addComparator(caseInsensitiveName);
	chain.addComparator(new BeanComparator("socialSecurityNumber"));

	Set<Party> result = new TreeSet<Party>(chain);
	for (final Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party.getSocialSecurityNumber() != null && !party.getSocialSecurityNumber().equals("")) {
		result.add(party);
	    }
	}

	return result;
    }

    public static Party readByContributorNumber(String contributorNumber) {
	if (contributorNumber != null) {
	    for (final Party party : RootDomainObject.getInstance().getPartys()) {
		if (party.getSocialSecurityNumber() != null
			&& party.getSocialSecurityNumber().equalsIgnoreCase(contributorNumber)) {
		    return party;
		}
	    }
	}
	return null;
    }

    public void editContributor(String contributorName, String contributorNumber, String contributorAddress,
	    String areaCode, String areaOfAreaCode, String area, String parishOfResidence,
	    String districtSubdivisionOfResidence, String districtOfResidence) {

	final Party existing = Party.readByContributorNumber(contributorNumber);
	if (existing != null && existing != this) {
	    throw new DomainException("PARTY.editContributor.existing.contributor.number");
	}

	setName(contributorName);
	setSocialSecurityNumber(contributorNumber);
	setAddress(contributorAddress);
	setAddress(contributorAddress);
	setAreaCode(areaCode);
	setAreaOfAreaCode(areaOfAreaCode);
	setArea(area);
	setParishOfResidence(parishOfResidence);
	setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	setDistrictOfResidence(districtOfResidence);
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
    
    public boolean hasAdministrativeOffice() {
	return false;
    }
    
    public boolean hasDegree() {
	return false;
    }
    
    public boolean hasDepartment() {
	return false;
    }
    
    public abstract ParkingPartyClassification getPartyClassification();

    public boolean verifyNameEquality(String[] nameWords) {
	if (nameWords == null) {
	    return true;
	}
	if (getName() != null) {
	    String[] personNameWords = getName().trim().split(" ");
	    StringNormalizer.normalize(personNameWords);
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

    private List<? extends Participation> filterParticipationsByYear(
	    List<? extends Participation> participations, ExecutionYear begin, ExecutionYear end) {
	List<Participation> participationsForInterval = new ArrayList<Participation>();
	for (Participation participation : participations) {
	    Integer year = participation.getCivilYear();
	    if (year == null
		    || (begin == null || begin.isBeforeCivilYear(year) || begin.belongsToCivilYear(year))
		    && (end == null || end.isAfterCivilYear(year) || end.belongsToCivilYear(year))) {
		participationsForInterval.add(participation);
	    }
	}
	return participationsForInterval;
    }

    private List<? extends Participation> filterParticipationsByType(Class<? extends Participation> clazz,
	    ScopeType scopeType) {
	List<Participation> participations = new ArrayList<Participation>();
	for (Participation participation : getParticipations()) {
	    if (participation.getClass().equals(clazz)
		    && (scopeType == null || participation.scopeMatches(scopeType))) {
		participations.add(participation);
	    }
	}
	return participations;
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ScopeType type, ExecutionYear begin,
	    ExecutionYear end) {
	return (List<EventEditionParticipation>) filterParticipationsByYear(
		getEventEditionParticipations(type), begin, end);
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ExecutionYear begin,
	    ExecutionYear end) {
	return (List<EventEditionParticipation>) filterParticipationsByYear(getEventEditionParticipations(),
		begin, end);
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ScopeType type) {
	return (List<EventEditionParticipation>) filterParticipationsByType(EventEditionParticipation.class,
		type);
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

    public List<EventParticipation> getEventParticipations(ScopeType type, ExecutionYear begin,
	    ExecutionYear end) {
	return (List<EventParticipation>) filterParticipationsByYear(getEventParticipations(type), begin, end);
    }

    public List<EventParticipation> getEventParticipation(ExecutionYear begin, ExecutionYear end) {
	return (List<EventParticipation>) filterParticipationsByYear(getEventParticipations(), begin, end);
    }

    public List<EventParticipation> getEventParticipations() {
	return getEventParticipations(null);
    }

    public Set<Event> getAssociatedEvents(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	Set<Event> events = new HashSet<Event>();
	for (EventParticipation participation : getEventParticipations(type, begin, end)) {
	    events.add(participation.getEvent());
	}
	return events;
    }

    public Set<Event> getAssociatedEvents(ExecutionYear begin, ExecutionYear end) {
	return getAssociatedEvents(null, begin, end);
    }

    public Set<Event> getAssociatedEvents(ScopeType type) {
	return getAssociatedEvents(type, null, null);
    }

    public Set<Event> getAssociatedEvents() {
	return getAssociatedEvents(null);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ScopeType type,
	    ExecutionYear begin, ExecutionYear end) {
	return (List<ScientificJournalParticipation>) filterParticipationsByYear(
		getScientificJournalParticipations(type), begin, end);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ExecutionYear begin,
	    ExecutionYear end) {
	return (List<ScientificJournalParticipation>) filterParticipationsByYear(
		getScientificJournalParticipations(), begin, end);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ScopeType type) {
	return (List<ScientificJournalParticipation>) filterParticipationsByType(
		ScientificJournalParticipation.class, type);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations() {
	return getScientificJournalParticipations(null);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ScopeType type, ExecutionYear begin,
	    ExecutionYear end) {
	Set<ScientificJournal> journals = new HashSet<ScientificJournal>();
	for (ScientificJournalParticipation participation : getScientificJournalParticipations(type, begin,
		end)) {
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

    public List<JournalIssueParticipation> getJournalIssueParticipations(ScopeType type, ExecutionYear begin,
	    ExecutionYear end) {
	return (List<JournalIssueParticipation>) filterParticipationsByYear(
		getJournalIssueParticipations(type), begin, end);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ExecutionYear begin,
	    ExecutionYear end) {
	return (List<JournalIssueParticipation>) filterParticipationsByYear(getJournalIssueParticipations(),
		begin, end);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ScopeType type) {
	return (List<JournalIssueParticipation>) filterParticipationsByType(JournalIssueParticipation.class,
		type);
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
	return (List<CooperationParticipation>) filterParticipationsByYear(getCooperationParticipations(),
		begin, end);
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

    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz,
	    final PartyContactType type) {
	final List<PartyContact> result = new ArrayList<PartyContact>();
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)) {
		result.add(contact);
	    }
	}
	return result;
    }

    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz) {
	return getPartyContacts(clazz, null);
    }

    public boolean hasAnyPartyContact(final Class<? extends PartyContact> clazz, final PartyContactType type) {
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && contact.getType() == type) {
		return true;
	    }
	}
	return false;
    }

    public PartyContact getDefaultPartyContact(final Class<? extends PartyContact> clazz) {
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && contact.isDefault()) {
		return contact;
	    }
	}
	return null;
    }

    public boolean hasDefaultPartyContact(final Class<? extends PartyContact> clazz) {
	return getDefaultPartyContact(clazz) != null;
    }
    
    /*
     * WebAddress
     */
    public WebAddress getDefaultWebAddress() {
	return (WebAddress) getDefaultPartyContact(WebAddress.class);
    }

    public boolean hasDefaultWebAddress() {
	return hasDefaultPartyContact(WebAddress.class);
    }
    
    public List<WebAddress> getWebAddresses() {
	return (List<WebAddress>) getPartyContacts(WebAddress.class);
    }

    private WebAddress getOrCreateDefaultWebAddress() {
	final WebAddress webAddress = getDefaultWebAddress();
	return webAddress != null ? webAddress : PartyContact.createDefaultPersonalWebAddress(this);
    }

    protected WebAddress createDefaultWebAddress(final String url) {
	return (!StringUtils.isEmpty(url)) ? PartyContact.createDefaultPersonalWebAddress(this, url) : null;
    }
    
    public void updateDefaultWebAddress(final String url) {
	getOrCreateDefaultWebAddress().edit(url);
    }
    
    /*
     * Phone
     */
    public Phone getDefaultPhone() {
	return (Phone) getDefaultPartyContact(Phone.class);
    }
    
    public boolean hasDefaultPhone() {
	return hasDefaultPartyContact(Phone.class);
    }

    public List<Phone> getPhones() {
	return (List<Phone>) getPartyContacts(Phone.class);
    }

    private Phone getOrCreateDefaultPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone : (Phone) PartyContact.createDefaultPersonalPhone(this);
    }

    protected Phone createDefaultPhone(final String number) {
	return (!StringUtils.isEmpty(number)) ? PartyContact.createDefaultPersonalPhone(this, number) : null;
    }
    
    protected void updateDefaultPhone(final String number) {
	getOrCreateDefaultPhone().edit(number);
    }
    
    /*
     * MobilePhone
     */
    public MobilePhone getDefaultMobilePhone() {
	return (MobilePhone) getDefaultPartyContact(MobilePhone.class);
    }
    
    public boolean hasDefaultMobilePhone() {
	return hasDefaultPartyContact(MobilePhone.class);
    }

    public List<MobilePhone> getMobilePhones() {
	return (List<MobilePhone>) getPartyContacts(MobilePhone.class);
    }
    
    private MobilePhone getOrCreateDefaultMobilePhone() {
	final MobilePhone mobilePhone = getDefaultMobilePhone();
	return mobilePhone != null ? mobilePhone : (MobilePhone) PartyContact.createDefaultPersonalMobilePhone(this);
    }

    protected MobilePhone createDefaultMobilePhone(final String number) {
	return (!StringUtils.isEmpty(number)) ? PartyContact.createDefaultPersonalMobilePhone(this, number) : null;
    }
    
    public void updateDefaultMobilePhone(final String number) {
	getOrCreateDefaultMobilePhone().edit(number);
    }
    
    /*
     * EmailAddress
     */
    protected EmailAddress createDefaultEmailAddress(final String value) {
	return (!StringUtils.isEmpty(value)) ? PartyContact.createDefaultPersonalEmailAddress(this, value) : null;
    }
    
    private EmailAddress getOrCreateDefaultEmailAddress() {
	final EmailAddress emailAddress = getDefaultEmailAddress();
	return emailAddress != null ? emailAddress : PartyContact.createDefaultPersonalEmailAddress(this);
    }
    
    public void updateDefaultEmailAddress(final String email) {
	getOrCreateDefaultEmailAddress().edit(email);
    }
    
    public EmailAddress getDefaultEmailAddress() {
	return (EmailAddress) getDefaultPartyContact(EmailAddress.class);
    }

    public List<EmailAddress> getEmailAddresses() {
	return (List<EmailAddress>) getPartyContacts(EmailAddress.class);
    }
    
    public String getEmail() {
	final EmailAddress emailAddress = getDefaultEmailAddress();
	return emailAddress != null ? emailAddress.getValue() : StringUtils.EMPTY;
    }
    
    public void setEmail(String email) {
	getOrCreateDefaultEmailAddress().edit(email);
    }
    
    /*
     * PhysicalAddress
     */
    protected PhysicalAddress createDefaultPhysicalAddress(final PhysicalAddressData data) {
	return (data != null) ? PartyContact.createDefaultPersonalPhysicalAddress(this, data) : null;
    }
    
    protected void updateDefaultPhysicalAddress(final PhysicalAddressData data) {
	getOrCreateDefaultPhysicalAddress().edit(data);
    }

    private PhysicalAddress getOrCreateDefaultPhysicalAddress() {
	final PhysicalAddress physicalAdress = getDefaultPhysicalAddress();
	return physicalAdress != null ? physicalAdress : PartyContact.createDefaultPersonalPhysicalAddress(this);
    }
    
    public PhysicalAddress getDefaultPhysicalAddress() {
	return (PhysicalAddress) getDefaultPartyContact(PhysicalAddress.class);
    }
    
    public boolean hasDefaultPhysicalAddress() {
	return hasDefaultPartyContact(PhysicalAddress.class);
    }

    public List<PhysicalAddress> getPhysicalAddresses() {
	return (List<PhysicalAddress>) getPartyContacts(PhysicalAddress.class);
    }
    
    public String getAddress() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAddress() : StringUtils.EMPTY;
    }

    public void setAddress(String address) {
	getOrCreateDefaultPhysicalAddress().setAddress(address);
    }

    public String getAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaCode() : StringUtils.EMPTY;
    }

    public void setAreaCode(String areaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaCode(areaCode);
    }

    public String getAreaOfAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaOfAreaCode() : StringUtils.EMPTY;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaOfAreaCode(areaOfAreaCode);
    }

    public String getArea() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getArea() : StringUtils.EMPTY;
    }

    public void setArea(String area) {
	getOrCreateDefaultPhysicalAddress().setArea(area);
    }

    public String getParishOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getParishOfResidence() : StringUtils.EMPTY;
    }

    public void setParishOfResidence(String parishOfResidence) {
	getOrCreateDefaultPhysicalAddress().setParishOfResidence(parishOfResidence);
    }

    public String getDistrictSubdivisionOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictSubdivisionOfResidence() : StringUtils.EMPTY;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	getOrCreateDefaultPhysicalAddress().setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
    }

    public String getDistrictOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictOfResidence() : StringUtils.EMPTY;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
	getOrCreateDefaultPhysicalAddress().setDistrictOfResidence(districtOfResidence);
    }

    public Country getCountryOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getCountryOfResidence() : null;
    }

    public void setCountryOfResidence(Country countryOfResidence) {
	getOrCreateDefaultPhysicalAddress().setCountryOfResidence(countryOfResidence);
    }

    public String getWebAddress() {
	final WebAddress webAddress = getDefaultWebAddress();
	return webAddress != null ? webAddress.getUrl() : StringUtils.EMPTY;
    }

    public void setWebAddress(String webAddress) {
	updateDefaultWebAddress(webAddress);
    }

    public String getPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone.getNumber() : StringUtils.EMPTY;
    }

    public void setPhone(String phone) {
	updateDefaultPhone(phone);
    }

    public String getMobile() {
	final MobilePhone phone = getDefaultMobilePhone();
	return phone != null ? phone.getNumber() : StringUtils.EMPTY;
    }

    public void setMobile(String mobile) {
	updateDefaultMobilePhone(mobile);
    }

}
