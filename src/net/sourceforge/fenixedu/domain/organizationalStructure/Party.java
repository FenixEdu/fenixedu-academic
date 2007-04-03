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

import net.sourceforge.fenixedu.domain.Country;
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
	if (name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.unit.empty.name");
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

    public Collection<? extends Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentPartyClass)) {
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
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getClass().equals(childPartyClass)) {
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
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
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
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    protected Collection<? extends Party> getChildParties(PartyTypeEnum type, Class<? extends Party> clazz) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getType() == type
		    && accountability.getChildParty().getClass().equals(clazz)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilitiesByParentClass(
	    Class<? extends Party> parentClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByChildClass(
	    Class<? extends Party> childClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getClass().equals(childClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByAccountabilityClass(
	    Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    protected void delete() {	
	for (; !getAccounts().isEmpty(); getAccounts().get(0).delete());
	for (; hasAnyPartyContacts(); getPartyContacts().get(0).delete());
	removePartyType();	
	removeRootDomainObject();
	deleteDomainObject();
    }

    public static Set<Party> readContributors() {
	ComparatorChain chain = new ComparatorChain();
	Comparator caseInsensitiveName = new Comparator<Party>() {
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
    
    public List<EventEditionParticipation> getEventEditionParticipationsForScope(ScopeType type) {
	List<EventEditionParticipation> participations = new ArrayList<EventEditionParticipation>();
	for (Participation participation : getParticipations()) {
	    if (participation.isEventEditionParticipation()) {
		if (type == null
			|| (type != null && type.equals(((EventEditionParticipation) participation).getEventEdition()
				.getEvent().getLocationType()))) {
		    participations.add((EventEditionParticipation) participation);
		}

	    }
	}
	return participations;
    }
    
    public List<EventEditionParticipation> getAllEventEditionParticipations() {
	return getEventEditionParticipationsForScope(null);
    }
    
    public List<EventParticipation> getEventParticipationsForScope(ScopeType type) {
	List<EventParticipation> participations = new ArrayList<EventParticipation>();
	for (Participation participation : getParticipations()) {
	    if (participation.isEventParticipation()) {
		if (type == null
			|| (type != null && type.equals(((EventParticipation) participation).getEvent()
				.getLocationType()))) {
		    participations.add((EventParticipation) participation);
		}

	    }
	}
	return participations;
    }

    public List<EventParticipation> getAllEventParticipations() {
	return getEventParticipationsForScope(null);
    }

    public Set<Event> getAssociatedEvents(ScopeType locationType) {
	Set<Event> events = new HashSet<Event>();
	for (EventParticipation participation : getAllEventParticipations()) {
	    if ((locationType != null && locationType.equals(participation.getEvent().getLocationType()))
		    || locationType == null) {
		events.add(participation.getEvent());
	    }
	}
	return events;
    }

    public Set<Event> getAssociatedEvents() {
	return getAssociatedEvents(null);
    }

    public Set<EventEdition> getAssociatedEventEditions() {
	Set<EventEdition> eventEditions = new HashSet<EventEdition>();
	for (EventEditionParticipation participation : getAllEventEditionParticipations()) {
	    eventEditions.add(participation.getEventEdition());
	}
	return eventEditions;
    }

    public Set<EventEdition> getAssociatedEventEditions(ScopeType type) {
	Set<EventEdition> editions = new HashSet<EventEdition>();
	for (EventEditionParticipation participation : getAllEventEditionParticipations()) {
	    if (type == null
		    || (type != null && type.equals(participation.getEventEdition().getEvent()
			    .getLocationType()))) {
		editions.add(participation.getEventEdition());
	    }
	}
	return editions;
    }
    public List<ScientificJournalParticipation> getAllScientificJournalParticiationsForScope(
	    ScopeType type) {
	List<ScientificJournalParticipation> participations = new ArrayList<ScientificJournalParticipation>();
	for (Participation participation : getParticipations()) {
	    if (participation.isScientificJournaltParticipation()) {
		if (type == null
			|| (type != null && type.equals(((ScientificJournalParticipation) participation)
				.getScientificJournal().getLocationType()))) {
		    participations.add((ScientificJournalParticipation) participation);
		}
	    }
	}
	return participations;
    }

    public List<ScientificJournalParticipation> getAllScientificJournalParticipations() {
	return getAllScientificJournalParticiationsForScope(null);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ScopeType locationType) {
	Set<ScientificJournal> journals = new HashSet<ScientificJournal>();
	for (ScientificJournalParticipation participation : getAllScientificJournalParticipations()) {
	    if (locationType == null
		    || (locationType != null && locationType.equals(participation.getScientificJournal()
			    .getLocationType()))) {
		journals.add(participation.getScientificJournal());
	    }
	}
	return journals;
    }

    public Set<ScientificJournal> getAssociatedScientificJournals() {
	return getAssociatedScientificJournals(null);
    }

    public List<JournalIssueParticipation> getAllJournalIssueParticipations() {
	List<JournalIssueParticipation> issueParticipations = new ArrayList<JournalIssueParticipation>();
	for (Participation participation : this.getParticipations()) {
	    if (participation.isJournalIssueParticipation()) {
		issueParticipations.add((JournalIssueParticipation) participation);
	    }
	}
	return issueParticipations;
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ScopeType locationType) {
	Set<JournalIssue> issues = new HashSet<JournalIssue>();
	for (JournalIssueParticipation participation : this.getAllJournalIssueParticipations()) {
	    if(locationType==null || (locationType!=null && locationType.equals(participation.getJournalIssue().getLocationType()))) {
		issues.add(participation.getJournalIssue());
	    }
	}
	return issues;
	
    }
    
    public Set<JournalIssue> getAssociatedJournalIssues() {
	return getAssociatedJournalIssues(null);
    }

    public List<CooperationParticipation> getAllCooperationParticipations() {
	List<CooperationParticipation> cooperationParticipations = new ArrayList<CooperationParticipation>();
	for (Participation participation : this.getParticipations()) {
	    if (participation.isCooperationParticipation()) {
		cooperationParticipations.add((CooperationParticipation) participation);
	    }
	}
	return cooperationParticipations;
    }

    public Set<Cooperation> getAssociatedCooperations() {
	Set<Cooperation> cooperations = new HashSet<Cooperation>();
	for (CooperationParticipation participation : getAllCooperationParticipations()) {
	    cooperations.add(participation.getCooperation());
	}
	return cooperations;
    }
    
    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz, final PartyContactType type) {
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
    
    public WebAddress getDefaultWebAddress() {
	return (WebAddress) getDefaultPartyContact(WebAddress.class);
    }
    
    public List<WebAddress> getWebAddresses() {
	return (List<WebAddress>) getPartyContacts(WebAddress.class);
    }
    
    public PhysicalAddress getDefaultPhysicalAddress() {
	return (PhysicalAddress) getDefaultPartyContact(PhysicalAddress.class);
    }
    
    public List<PhysicalAddress> getPhysicalAddresses() {
	return (List<PhysicalAddress>) getPartyContacts(PhysicalAddress.class);
    }
    
    public Phone getDefaultPhone() {
	return (Phone) getDefaultPartyContact(Phone.class);
    }
    
    public List<Phone> getPhones() {
	return (List<Phone>) getPartyContacts(Phone.class);
    }
    
    public MobilePhone getDefaultMobilePhone() {
	return (MobilePhone) getDefaultPartyContact(MobilePhone.class);
    }
    
    public List<MobilePhone> getMobilePhones() {
	return (List<MobilePhone>) getPartyContacts(MobilePhone.class);
    }
    
    public EmailAddress getDefaultEmailAddress() {
	return (EmailAddress) getDefaultPartyContact(EmailAddress.class);
    }
    
    public List<EmailAddress> getEmailAddresses() {
	return (List<EmailAddress>) getPartyContacts(EmailAddress.class);
    }

    private PhysicalAddress getOrCreateDefaultPhysicalAddress() {
	final PhysicalAddress physicalAdress = getDefaultPhysicalAddress();
	return physicalAdress != null ? physicalAdress : PartyContact.createDefaultPersonalPhysicalAddress(this);
    }
    
    protected void updateDefaultPhysicalAddress(final PhysicalAddressData data) {
	getOrCreateDefaultPhysicalAddress().edit(data);
    }
    
    protected PhysicalAddress updateDefaultPhysicalAddress() {
	return getOrCreateDefaultPhysicalAddress();
    }
    
    private WebAddress getOrCreateDefaultWebAddress() {
	final WebAddress webAddress = getDefaultWebAddress();
	return webAddress != null ? webAddress : PartyContact.createDefaultPersonalWebAddress(this);
    }
    
    protected void updateDefaultWebAddress(final String url) {
	getOrCreateDefaultWebAddress().edit(url);
    }
    
    private Phone getOrCreateDefaultPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone : (Phone) PartyContact.createDefaultPersonalPhone(this);
    }
    
    protected void updateDefaultPhone(final String number) {
	getOrCreateDefaultPhone().edit(number);
    }
    
    private MobilePhone getOrCreateDefaultMobilePhone() {
	final MobilePhone mobilePhone = getDefaultMobilePhone();
	return mobilePhone != null ? mobilePhone : (MobilePhone) PartyContact.createDefaultPersonalMobilePhone(this);
    }
    
    protected void updateDefaultMobilePhone(final String number) {
	getOrCreateDefaultMobilePhone().edit(number);
    }
    
    /* ~~~~~~~~~~~~~~~~~~~~~
     * PartyContacts
     * ~~~~~~~~~~~~~~~~~~~~~
     * These methods are used to support current functionality (physicalAddress, webAddress, ... - limited to one for each type)
     * after interface changes we must edit contacts in another way and remove the following methods
     * 
     */
    
    public String getAddress() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAddress() : null;
    }
    
    public void setAddress(String address) {
	getOrCreateDefaultPhysicalAddress().setAddress(address);
    }
    
    public String getAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaCode() : null;
    }
    
    public void setAreaCode(String areaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaCode(areaCode);
    }
    
    public String getAreaOfAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaOfAreaCode() : null;
    }
    
    public void setAreaOfAreaCode(String areaOfAreaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaOfAreaCode(areaOfAreaCode);
    }
    
    public String getArea() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getArea() : null;
    }
    
    public void setArea(String area) {
	getOrCreateDefaultPhysicalAddress().setArea(area);
    }
    
    public String getParishOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getParishOfResidence() : null;
    }
    
    public void setParishOfResidence(String parishOfResidence) {
	getOrCreateDefaultPhysicalAddress().setParishOfResidence(parishOfResidence);
    }
    
    public String getDistrictSubdivisionOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictSubdivisionOfResidence() : null;
    }
    
    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	getOrCreateDefaultPhysicalAddress().setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
    }
    
    public String getDistrictOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictOfResidence() : null;
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
        return webAddress != null ? webAddress.getUrl() : null;
    }
    
    public void setWebAddress(String webAddress) {
	updateDefaultWebAddress(webAddress);
    }
    
    public String getPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone.getNumber() : null;
    }
    
    public void setPhone(String phone) {
	updateDefaultPhone(phone);
    }
    
    public String getMobile() {
	final MobilePhone phone = getDefaultMobilePhone();
	return phone != null ? phone.getNumber() : null;
    }
    
    public void setMobile(String mobile) {
	updateDefaultMobilePhone(mobile);
    }
    
    private EmailAddress getPersonalEmailAddress() {
	final List<EmailAddress> partyContacts = (List<EmailAddress>) getPartyContacts(EmailAddress.class, PartyContactType.PERSONAL);
	return partyContacts.isEmpty() ? null : (EmailAddress) partyContacts.get(0); // actually exists only one
    }

    public String getEmail() {
	final EmailAddress emailAddress = getPersonalEmailAddress();
	return emailAddress != null ? emailAddress.getValue() : null;
    }
    
    public void setEmail(String email) {
	final EmailAddress emailAddress = getPersonalEmailAddress();
	if (emailAddress == null) {
	    PartyContact.createEmailAddress(this, PartyContactType.PERSONAL, true, false, email);
	} else {
	    emailAddress.setValue(email);
	}
    }
    
    /* ~~~~~~~~~~~~~~~~~~~~~
     * End: PartyContacts
     * ~~~~~~~~~~~~~~~~~~~~~
     */
}
