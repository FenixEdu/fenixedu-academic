package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreement;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearcherContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.IdDocument;
import net.sourceforge.fenixedu.domain.person.IdDocumentTypeObject;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.domain.sms.SmsDeliveryType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StringFormatter;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class Person extends Person_Base {

    final static Comparator<SentSms> PERSON_SENTSMS_COMPARATOR_BY_SENT_DATE = new BeanComparator(
    "sendDate");
    static {
	Role.PersonRole.addListener(new PersonRoleListener());
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    private IdDocument getIdDocument() {
	final Iterator<IdDocument> documentIterator = getIdDocumentsSet().iterator();
	return documentIterator.hasNext() ? documentIterator.next() : null;
    }

    @Override
    public void setPartyName(MultiLanguageString partyName) {
        throw new UnsupportedOperationException();
    }
       
    public String getName() {
	return super.getPartyName().getPreferedContent();
    }
    
    @Override
    public void setName(String name) {
	
	if(name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.person.empty.name");
	}
	
	String formattedName = StringFormatter.prettyPrint(name);
	    
	MultiLanguageString partyName = super.getPartyName();
	partyName = partyName == null ? new MultiLanguageString() : partyName;
	partyName.setContent(LanguageUtils.getSystemLanguage(), formattedName);
	
	super.setPartyName(partyName);
	
	PersonName personName = getPersonName();
	personName = personName == null ? new PersonName(this) : personName;	
	personName.setName(formattedName);
    }
    
    @Override
    public void setDocumentIdNumber(String documentIdNumber) {
	if (documentIdNumber == null || StringUtils.isEmpty(documentIdNumber.trim())) {
	    throw new DomainException("error.person.empty.documentIdNumber");
	}
	IdDocument idDocument = getIdDocument();
	if (idDocument == null) {
	    idDocument = new IdDocument(this, documentIdNumber, (IdDocumentTypeObject) null);
	} else {
	    idDocument.setValue(documentIdNumber);
	}
	super.setDocumentIdNumber(documentIdNumber);
    }

    @Override
    public void setIdDocumentType(IDDocumentType idDocumentType) {
	if (idDocumentType == null) {
	    throw new DomainException("error.person.empty.idDocumentType");
	}
	IdDocument idDocument = getIdDocument();
	if (idDocument == null) {
	    idDocument = new IdDocument(this, null, idDocumentType);
	} else {
	    idDocument.setIdDocumentType(idDocumentType);
	}
	super.setIdDocumentType(idDocumentType);
    }

    public void setIdentification(String documentIdNumber, IDDocumentType idDocumentType) {
	if (documentIdNumber != null && idDocumentType != null
		&& checkIfDocumentNumberIdAndDocumentIdTypeExists(documentIdNumber, idDocumentType)) {
	    throw new DomainException("error.person.existent.docIdAndType");
	}
	setDocumentIdNumber(documentIdNumber);
	setIdDocumentType(idDocumentType);
    }

    private boolean checkIfDocumentNumberIdAndDocumentIdTypeExists(final String documentIDNumber, final IDDocumentType documentType) {
	Person person = readByDocumentIdNumberAndIdDocumentType(documentIDNumber, documentType);
	return person != null && !person.equals(this);        
    }

    final public String getValidatedName() {
	return StringFormatter.personNameValidator(getName(), LanguageUtils.getLanguage());
    }

    public Person() {
	super();
	setMaritalStatus(MaritalStatus.UNKNOWN);
	setIsPassInKerberos(Boolean.FALSE);
	setAvailableEmail(Boolean.FALSE);
	setAvailableWebSite(Boolean.FALSE);
	setAvailablePhoto(Boolean.FALSE);
    }

    /**
     * 
     * @deprecated use Person(PersonBean personBean)
     * @see Person(PersonBean personBean)
     */
    public Person(InfoPersonEditor personToCreate, Country country) {

	super();
	if (personToCreate.getIdInternal() != null) {
	    throw new DomainException("error.person.existentPerson");
	}

	createUserAndLoginEntity();
	setProperties(personToCreate);
	setCountry(country);
	setIsPassInKerberos(Boolean.FALSE);
    }

    public Person(final String name, final String identificationDocumentNumber,
	    final IDDocumentType identificationDocumentType, final Gender gender) {

	this();
	setName(name);
	setGender(gender);
	setMaritalStatus(MaritalStatus.SINGLE);
	setIdentification(identificationDocumentNumber, identificationDocumentType);
	createUserAndLoginEntity();
    }

    public Person(final PersonBean personBean) {
	super();

	setProperties(personBean);
	setIsPassInKerberos(Boolean.FALSE);

	createUserAndLoginEntity();
	createDefaultPhysicalAddress(personBean.getPhysicalAddressData());
	createDefaultPhone(personBean.getPhone());
	createDefaultMobilePhone(personBean.getMobile());
	createDefaultWebAddress(personBean.getWebAddress());
	createDefaultEmailAddress(personBean.getEmail());
    }

    private void createUserAndLoginEntity() {
	new Login(new User(this));
    }

    private Person(final String name, final Gender gender, final PhysicalAddressData data,
	    final String phone, final String mobile, final String homepage, final String email,
	    final String documentIDNumber, final IDDocumentType documentType) {

	this();

	setName(name);
	setGender(gender);
	setIdentification(documentIDNumber, documentType);

	createDefaultPhysicalAddress(data);
	createDefaultPhone(phone);
	createDefaultMobilePhone(mobile);
	createDefaultWebAddress(homepage);
	createDefaultEmailAddress(email);
    }

    static public Person createExternalPerson(final String name, final Gender gender,
	    final PhysicalAddressData data, final String phone, final String mobile,
	    final String homepage, final String email, final String documentIdNumber,
	    final IDDocumentType documentType) {
	return new Person(name, gender, data, phone, mobile, homepage, email, documentIdNumber,
		documentType);
    }

    public Person(final String name, final Gender gender, final String documentIDNumber,
	    final IDDocumentType documentType) {

	this();
	setName(name);
	setGender(gender);
	setIdentification(documentIDNumber, documentType);
	createUserAndLoginEntity();
    }

    public Person(PersonBean creator, boolean createExternalPerson) {
	this(creator);
	if (createExternalPerson) {
	    getPersonRolesSet().clear();
	    final User user = getUser();
	    final Login login = (Login) user.getIdentificationsSet().iterator().next();
	    login.setEndDateDateTime(login.getBeginDateDateTime());
	    login.setActive(Boolean.FALSE);
	}
    }

    public void edit(PersonBean personBean) {
	setProperties(personBean);
	updateDefaultPhysicalAddress(personBean.getPhysicalAddressData());
	updateDefaultPhone(personBean.getPhone());
	updateDefaultMobilePhone(personBean.getMobile());
	updateDefaultWebAddress(personBean.getWebAddress());
	updateDefaultEmailAddress(personBean.getEmail());
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
	    String email) {
	setName(name);
	updateDefaultPhysicalAddress(new PhysicalAddressData().setAddress(address));
	updateDefaultPhone(phone);
	updateDefaultMobilePhone(mobile);
	updateDefaultEmailAddress(email);
	updateDefaultWebAddress(homepage);
    }

    public void editPersonalData(String documentIdNumber, IDDocumentType documentType,
	    String personName, String socialSecurityNumber) {

	setName(personName);
	setIdentification(documentIdNumber, documentType);
	setSocialSecurityNumber(socialSecurityNumber);
    }

    public void editPersonWithExternalData(PersonBean personBean) {
	setProperties(personBean);		
	updateDefaultPhysicalAddress(personBean.getPhysicalAddressData());
	if(!hasDefaultPhone()) {
	    updateDefaultPhone(personBean.getPhone());
	}
	if(!hasDefaultMobilePhone()) {
	    updateDefaultMobilePhone(personBean.getMobile());
	}
	if(!hasDefaultWebAddress()) {
	    updateDefaultWebAddress(personBean.getWebAddress());
	}
	if(!hasDefaultEmailAddress()) {
	    updateDefaultEmailAddress(personBean.getEmail());
	}
    } 

    @Deprecated
    public void update(InfoPersonEditor updatedPersonalData, Country country) {
	updateProperties(updatedPersonalData);
	if (country != null) {
	    setCountry(country);
	}
    }

    /**
     * 
     * @deprecated use edit(PersonBean personBean)
     * @see edit(PersonBean personBean)
     */
    public void edit(InfoPersonEditor personToEdit, Country country) {
	setProperties(personToEdit);
	if (country != null) {
	    setCountry(country);
	}
    }

    public Login getLoginIdentification() {
	User personUser = getUser();
	return (personUser == null) ? null : personUser.readUserLoginIdentification();
    }

    public Set<LoginAlias> getLoginAliasOrderByImportance() {
	Login login = getLoginIdentification();
	return (login != null) ? login.getLoginAliasOrderByImportance() : new HashSet<LoginAlias>();
    }

    public Set<LoginAlias> getLoginAlias() {
	Login login = getLoginIdentification();
	return (login != null) ? login.getAliasSet() : new HashSet<LoginAlias>();
    }

    public boolean hasUsername(String username) {
	Login login = getLoginIdentification();
	return (login != null) ? login.hasUsername(username) : false;
    }

    public String getUsername() {
	Login login = getLoginIdentification();
	return (login != null) ? login.getUsername() : null;
    }

    public void setUsername(RoleType roleType) {
	Login login = createLoginIdentificationAndUserIfNecessary();
	login.setUsername(roleType);
    }

    public String getPassword() {
	Login login = getLoginIdentification();
	return (login != null) ? login.getPassword() : null;
    }

    public void setPassword(String password) {
	createLoginIdentificationAndUserIfNecessary().setPassword(password);
    }

    public void setIsPassInKerberos(Boolean isPassInKerberos) {
	createLoginIdentificationAndUserIfNecessary().setIsPassInKerberos(isPassInKerberos);
    }

    public Boolean getIsPassInKerberos() {
	Login login = getLoginIdentification();
	return (login != null) ? login.getIsPassInKerberos() : null;
    }

    public void setIstUsername() {
	createLoginIdentificationAndUserIfNecessary().setUserUID();
    }

    private Login createLoginIdentificationAndUserIfNecessary() {
	Login login = getLoginIdentification();
	if (login == null) {
	    User user = getUser();
	    if (user == null) {
		user = new User(this);
	    }
	    login = new Login(getUser());
	}
	return login;
    }

    public String getIstUsername() {
	return (getUser() != null) ? getUser().getUserUId() : null;
    }

    public void changeUsername(RoleType roleType) {
	setUsername(roleType);
    }

    public void changePassword(String oldPassword, String newPassword) {

	if (newPassword == null) {
	    throw new DomainException("error.person.invalidNullPassword");
	}

	if (getUser() == null) {
	    throw new DomainException("error.person.unExistingUser");
	}

	if (!PasswordEncryptor.areEquals(getPassword(), oldPassword)) {
	    throw new DomainException("error.person.invalidExistingPassword");
	}

	if (PasswordEncryptor.areEquals(getPassword(), newPassword)) {
	    throw new DomainException("error.person.invalidSamePassword");
	}

	if (newPassword.equals("")) {
	    throw new DomainException("error.person.invalidEmptyPassword");
	}

	if (getDocumentIdNumber().equalsIgnoreCase(newPassword)) {
	    throw new DomainException("error.person.invalidIDPassword");
	}

	if (getFiscalCode() != null && getFiscalCode().equalsIgnoreCase(newPassword)) {
	    throw new DomainException("error.person.invalidFiscalCodePassword");
	}

	if (getSocialSecurityNumber() != null && getSocialSecurityNumber().equalsIgnoreCase(newPassword)) {
	    throw new DomainException("error.person.invalidTaxPayerPassword");
	}

	setPassword(PasswordEncryptor.encryptPassword(newPassword));
    }

    public void addAlias(Role role) {
	setUsername(role.getRoleType());
    }

    public void removeAlias(Role removedRole) {
	Login loginIdentification = getLoginIdentification();
	if (loginIdentification != null) {
	    loginIdentification.removeAlias(removedRole.getRoleType());
	}
    }

    public void updateIstUsername() {
	setIstUsername();
    }

    public Role getPersonRole(RoleType roleType) {

	for (Role role : this.getPersonRoles()) {
	    if (role.getRoleType().equals(roleType)) {
		return role;
	    }
	}
	return null;
    }

    @Override    
    public void addPersonRoles(Role personRoles) {
	if(!hasPersonRoles(personRoles)) {
	    super.addPersonRoles(personRoles);
	}
    }
    
    public void addPersonRoleByRoleType(RoleType roleType) {
	    this.addPersonRoles(Role.getRoleByRoleType(roleType));
	}

    public Boolean hasRole(final RoleType roleType) {
	for (final Role role : this.getPersonRoles()) {
	    if (role.getRoleType() == roleType) {
		return true;
	    }
	}
	return false;
    }

    public Registration getStudentByType(DegreeType degreeType) {
	for (Registration registration : this.getStudents()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		return registration;
	    }
	}
	return null;
    }

    // FIXME: Remove as soon as possible.
    @Deprecated
    public Registration getStudentByUsername() {
	Login loginIdentification = getLoginIdentification();
	if (loginIdentification != null) {
	    List<LoginAlias> loginAlias = loginIdentification.getRoleLoginAlias(RoleType.STUDENT);
	    for (final Registration registration : this.getStudents()) {
		for (LoginAlias alias : loginAlias) {
		    if (alias.getAlias().contains(registration.getNumber().toString())) {
			return registration;
		    }
		}
	    }
	}
	return null;
    }

    public List<ResearchResultPublication> getResearchResultPublications() {
	List<ResearchResultPublication> resultPublications = new ArrayList<ResearchResultPublication>();
	ResearchResult result = null;
	for (ResultParticipation resultParticipation : this.getResultParticipations()) {
	    result = resultParticipation.getResult();
	    // filter only publication participations
	    if (result instanceof ResearchResultPublication) {
		resultPublications.add((ResearchResultPublication) result);
	    }
	}
	return resultPublications;
    }

    public List<ResearchResultPatent> getResearchResultPatents() {
	List<ResearchResultPatent> resultPatents = new ArrayList<ResearchResultPatent>();
	ResearchResult result = null;
	for (ResultParticipation resultParticipation : this.getResultParticipations()) {
	    result = resultParticipation.getResult();
	    // filter only patent participations
	    if (result instanceof ResearchResultPatent) {
		resultPatents.add((ResearchResultPatent) result);
	    }
	}
	return resultPatents;
    }

    public List<ResearchResultPatent> getResearchResultPatentsByExecutionYear(ExecutionYear executionYear) {
	List<ResearchResultPatent> resultPatents = new ArrayList<ResearchResultPatent>();
	for (ResearchResultPatent patent : getResearchResultPatents()) {
	    if (executionYear.belongsToCivilYear(patent.getApprovalYear())) {
		resultPatents.add(patent);
	    }
	}
	return resultPatents;
    }

    @Override
    public List<Advisory> getAdvisories() {
	final DateTime currentDate = new DateTime();
	final List<Advisory> result = new ArrayList<Advisory>();
	for (final Advisory advisory : super.getAdvisories()) {
	    if (advisory.getExpiresDateTime() == null
		    || advisory.getExpiresDateTime().isAfter(currentDate)) {
		result.add(advisory);
	    }
	}
	return result;
    }

    public Boolean getIsExamCoordinatorInCurrentYear() {
	ExamCoordinator examCoordinator = this.getExamCoordinatorForGivenExecutionYear(ExecutionYear
		.readCurrentExecutionYear());
	return (examCoordinator == null) ? false : true;
    }

    public Vigilant getVigilantForGivenExecutionYear(ExecutionYear executionYear) {
	List<Vigilant> vigilants = this.getVigilants();
	for (Vigilant vigilant : vigilants) {
	    if (vigilant.getExecutionYear().equals(executionYear)) {
		return vigilant;

	    }
	}

	return null;
    }

    public Vigilant getLatestVigilant() {
	List<Vigilant> vigilants = new ArrayList<Vigilant>(this.getVigilants());
	Collections.sort(vigilants, new ReverseComparator(new BeanComparator("vigilant.executionYear")));
	return vigilants.get(0);
    }

    public ExamCoordinator getExamCoordinatorForGivenExecutionYear(ExecutionYear executionYear) {
	List<ExamCoordinator> examCoordinators = this.getExamCoordinators();
	for (ExamCoordinator examCoordinator : examCoordinators) {
	    if (examCoordinator.getExecutionYear().equals(executionYear)) {
		return examCoordinator;
	    }
	}
	return null;
    }

    public Boolean isExamCoordinatorForVigilantGroup(VigilantGroup group) {
	ExamCoordinator coordinator = getExamCoordinatorForGivenExecutionYear(group.getExecutionYear());
	return (coordinator == null) ? Boolean.FALSE : group.getExamCoordinators().contains(coordinator);
    }

    public ExamCoordinator getCurrentExamCoordinator() {
	return getExamCoordinatorForGivenExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    public Vigilant getCurrentVigilant() {
	return getVigilantForGivenExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    public double getVigilancyPointsForGivenYear(ExecutionYear executionYear) {
	Vigilant vigilant = this.getVigilantForGivenExecutionYear(executionYear);
	if (vigilant == null)
	    return 0;
	else
	    return vigilant.getPoints();
    }

    public double getTotalVigilancyPoints() {
	List<Vigilant> vigilants = this.getVigilants();

	double points = 0;
	for (Vigilant vigilant : vigilants) {
	    points += vigilant.getPoints();
	}
	return points;
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private void setProperties(InfoPersonEditor infoPerson) {

	setName(infoPerson.getNome());
	setIdentification(infoPerson.getNumeroDocumentoIdentificacao(), infoPerson.getTipoDocumentoIdentificacao());
	setFiscalCode(infoPerson.getCodigoFiscal());

	updateDefaultPhysicalAddress(infoPerson.getPhysicalAddressData());
	updateDefaultWebAddress(infoPerson.getEnderecoWeb());
	updateDefaultPhone(infoPerson.getTelefone());
	updateDefaultMobilePhone(infoPerson.getTelemovel());
	updateDefaultEmailAddress(infoPerson.getEmail());

	setWorkPhone(infoPerson.getWorkPhone());

	setDistrictSubdivisionOfBirth(infoPerson.getConcelhoNaturalidade());
	setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(infoPerson.getDataEmissaoDocumentoIdentificacao()));
	setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(infoPerson.getDataValidadeDocumentoIdentificacao()));
	setDistrictOfBirth(infoPerson.getDistritoNaturalidade());

	setMaritalStatus((infoPerson.getMaritalStatus() == null) ? MaritalStatus.UNKNOWN : infoPerson.getMaritalStatus());
	setParishOfBirth(infoPerson.getFreguesiaNaturalidade());
	setEmissionLocationOfDocumentId(infoPerson.getLocalEmissaoDocumentoIdentificacao());

	setDateOfBirthYearMonthDay(YearMonthDay.fromDateFields(infoPerson.getNascimento()));
	setNameOfMother(infoPerson.getNomeMae());
	setNameOfFather(infoPerson.getNomePai());
	setSocialSecurityNumber(infoPerson.getNumContribuinte());

	setProfession(infoPerson.getProfissao());
	setGender(infoPerson.getSexo());

	// Generate person's Password
	if (getPassword() == null) {
	    setPassword(PasswordEncryptor.encryptPassword(GeneratePassword.getInstance().generatePassword(this)));
	}

	setAvailableEmail(infoPerson.getAvailableEmail() != null ? infoPerson.getAvailableEmail() : Boolean.TRUE);
	setAvailableWebSite(infoPerson.getAvailableWebSite() != null ? infoPerson.getAvailableWebSite()	: Boolean.TRUE);
	setAvailablePhoto(Boolean.TRUE);
    }

    private void updateProperties(InfoPersonEditor infoPerson) {

	setName(valueToUpdateIfNewNotNull(getName(), infoPerson.getNome()));
	setIdentification(valueToUpdateIfNewNotNull(getDocumentIdNumber(), infoPerson
		.getNumeroDocumentoIdentificacao()), (IDDocumentType) valueToUpdateIfNewNotNull(
			getIdDocumentType(), infoPerson.getTipoDocumentoIdentificacao()));

	setFiscalCode(valueToUpdateIfNewNotNull(getFiscalCode(), infoPerson.getCodigoFiscal()));

	setAddress(valueToUpdateIfNewNotNull(getAddress(), infoPerson.getMorada()));
	setAreaCode(valueToUpdateIfNewNotNull(getAreaCode(), infoPerson.getCodigoPostal()));
	setAreaOfAreaCode(valueToUpdateIfNewNotNull(getAreaOfAreaCode(), infoPerson
		.getLocalidadeCodigoPostal()));
	setArea(valueToUpdateIfNewNotNull(getArea(), infoPerson.getLocalidade()));
	setParishOfResidence(valueToUpdateIfNewNotNull(getParishOfResidence(), infoPerson
		.getFreguesiaMorada()));
	setDistrictSubdivisionOfResidence(valueToUpdateIfNewNotNull(getDistrictSubdivisionOfResidence(),
		infoPerson.getConcelhoMorada()));
	setDistrictOfResidence(valueToUpdateIfNewNotNull(getDistrictOfResidence(), infoPerson
		.getDistritoMorada()));

	setEmissionDateOfDocumentIdYearMonthDay(infoPerson.getDataEmissaoDocumentoIdentificacao() != null ? YearMonthDay
		.fromDateFields(infoPerson.getDataEmissaoDocumentoIdentificacao())
		: getEmissionDateOfDocumentIdYearMonthDay());
	setEmissionLocationOfDocumentId(valueToUpdateIfNewNotNull(getEmissionLocationOfDocumentId(),
		infoPerson.getLocalEmissaoDocumentoIdentificacao()));
	setExpirationDateOfDocumentIdYearMonthDay(infoPerson.getDataValidadeDocumentoIdentificacao() != null ? YearMonthDay
		.fromDateFields(infoPerson.getDataValidadeDocumentoIdentificacao())
		: getExpirationDateOfDocumentIdYearMonthDay());

	MaritalStatus maritalStatus = (MaritalStatus) valueToUpdateIfNewNotNull(getMaritalStatus(),
		infoPerson.getMaritalStatus());
	setMaritalStatus((maritalStatus == null) ? MaritalStatus.UNKNOWN : maritalStatus);

	setDateOfBirthYearMonthDay(infoPerson.getNascimento() != null ? YearMonthDay
		.fromDateFields(infoPerson.getNascimento()) : getDateOfBirthYearMonthDay());
	setParishOfBirth(valueToUpdateIfNewNotNull(getParishOfBirth(), infoPerson
		.getFreguesiaNaturalidade()));
	setDistrictSubdivisionOfBirth(valueToUpdateIfNewNotNull(getDistrictSubdivisionOfBirth(),
		infoPerson.getConcelhoNaturalidade()));
	setDistrictOfBirth(valueToUpdateIfNewNotNull(getDistrictOfBirth(), infoPerson
		.getDistritoNaturalidade()));

	setNameOfMother(valueToUpdateIfNewNotNull(getNameOfMother(), infoPerson.getNomeMae()));
	setNameOfFather(valueToUpdateIfNewNotNull(getNameOfFather(), infoPerson.getNomePai()));
	setSocialSecurityNumber(valueToUpdateIfNewNotNull(getSocialSecurityNumber(), infoPerson
		.getNumContribuinte()));
	setProfession(valueToUpdateIfNewNotNull(getProfession(), infoPerson.getProfissao()));
	setGender((Gender) valueToUpdateIfNewNotNull(getGender(), infoPerson.getSexo()));

	setWebAddress(valueToUpdate(getWebAddress(), infoPerson.getEnderecoWeb()));
	setEmail(valueToUpdate(getEmail(), infoPerson.getEmail()));
	setPhone(valueToUpdate(getPhone(), infoPerson.getTelefone()));
	setMobile(valueToUpdate(getMobile(), infoPerson.getTelemovel()));
	if (!StringUtils.isNumeric(getWorkPhone())) {
	    setWorkPhone(infoPerson.getWorkPhone());
	} else {
	    setWorkPhone(valueToUpdate(getWorkPhone(), infoPerson.getWorkPhone()));
	}
    }

    private String valueToUpdate(String actualValue, String newValue) {

	if (actualValue == null || actualValue.length() == 0) {
	    return newValue;
	}
	return actualValue;

    }

    private String valueToUpdateIfNewNotNull(String actualValue, String newValue) {

	if (newValue == null || newValue.length() == 0) {
	    return actualValue;
	}
	return newValue;

    }

    private Object valueToUpdateIfNewNotNull(Object actualValue, Object newValue) {

	if (newValue == null) {
	    return actualValue;
	}
	return newValue;

    }

    private void setProperties(final PersonBean personBean) {

	setName(personBean.getName());
	setGender(personBean.getGender());
	setIdentification(personBean.getDocumentIdNumber(), personBean.getIdDocumentType());
	setEmissionLocationOfDocumentId(personBean.getDocumentIdEmissionLocation());
	setEmissionDateOfDocumentIdYearMonthDay(personBean.getDocumentIdEmissionDate());
	setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
	setSocialSecurityNumber(personBean.getSocialSecurityNumber());
	setProfession(personBean.getProfession());
	setMaritalStatus(personBean.getMaritalStatus());

	setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
	setCountry(personBean.getNationality());
	setParishOfBirth(personBean.getParishOfBirth());
	setDistrictSubdivisionOfBirth(personBean.getDistrictSubdivisionOfBirth());
	setDistrictOfBirth(personBean.getDistrictOfBirth());
	setCountryOfBirth(personBean.getCountryOfBirth());
	setNameOfMother(personBean.getMotherName());
	setNameOfFather(personBean.getFatherName());

	setAvailableEmail(personBean.isEmailAvailable());
	setAvailablePhoto(personBean.isPhotoAvailable());
	setAvailableWebSite(personBean.isHomepageAvailable());
    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

    public String getSlideName() {
	return "/photos/person/P" + getIdInternal();
    }

    public String getSlideNameForCandidateDocuments() {
	return "/candidateDocuments/person/P" + getIdInternal();
    }

    public void removeRoleByType(final RoleType roleType) {
	final Role role = getPersonRole(roleType);
	if (role != null) {
	    removePersonRoles(role);
	}
    }

    public void indicatePrivledges(final Set<Role> roles) {
	getPersonRoles().retainAll(roles);
	getPersonRoles().addAll(roles);
    }

    public List<PersonFunction> getActivePersonFunctions() {
    	return getPersonFunctions(null, false, true, false);
    }

    public List<PersonFunction> getInactivePersonFunctions() {
    	return getPersonFunctions(null, false, false, false);
    }

    public List<Function> getActiveInherentPersonFunctions() {
	List<Function> inherentFunctions = new ArrayList<Function>();
	for (PersonFunction accountability : getActivePersonFunctions()) {
	    inherentFunctions.addAll(accountability.getFunction().getInherentFunctions());
	}
	return inherentFunctions;
    }

    /**
	 * The main difference between this method and
	 * {@link #getActivePersonFunctions()} is that person functions with a
	 * virtual function are also included. This method also collects person
	 * functions from the given unit and all subunits.
	 * 
	 * @see Function#isVirtual()
	 */
	public List<PersonFunction> getAllActivePersonFunctions(Unit unit) {
		return getPersonFunctions(unit, true, true, null);
	}
    
    public boolean containsActivePersonFunction(Function function) {
	for (PersonFunction personFunction : getActivePersonFunctions()) {
	    if (personFunction.getFunction().equals(function)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyPersonFunctions() {
	return !getPersonFunctions().isEmpty();
    }

    public Collection<PersonFunction> getPersonFunctions() {
	return (Collection<PersonFunction>) getParentAccountabilities(
		AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class);
    }

	public Collection<PersonFunction> getPersonFunctions(Function function) {
		Collection<PersonFunction> personFunctions = getPersonFunctions();
		
		Iterator<PersonFunction> iterator = personFunctions.iterator();
		while (iterator.hasNext()) {
			PersonFunction element = iterator.next();
			
			if (element.getFunction() == function) {
				continue;
			}
			
			iterator.remove();
		}
		
		return personFunctions;
	}
    
    public List<PersonFunction> getPersonFuntions(YearMonthDay begin, YearMonthDay end) {
	List<PersonFunction> result = new ArrayList<PersonFunction>();
	for (Accountability accountability : (Collection<PersonFunction>) getParentAccountabilities(
		AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)) {
	    if (accountability.belongsToPeriod(begin, end)) {
		result.add((PersonFunction) accountability);
	    }
	}
	return result;
    }

    public List<PersonFunction> getPersonFunctions(Unit unit) {
    	return getPersonFunctions(unit, false, null, null);
    }

    /**
	 * Filters all parent PersonFunction accountabilities and returns all the
	 * PersonFunctions that selection indicated in the parameters.
	 * 
	 * @param unit filter all PersonFunctions to this unit, or <code>null</code> for all PersonFunctions
	 * @param includeSubUnits if even subunits of the given unit are considered
	 * @param active the state of the function, <code>null</code> for all PersonFunctions 
	 */
    public List<PersonFunction> getPersonFunctions(Unit unit, boolean includeSubUnits, Boolean active, Boolean virtual) {
    	List<PersonFunction> result = new ArrayList<PersonFunction>();

    	Collection<Unit> allSubUnits = Collections.emptyList();
    	if (includeSubUnits) {
    		allSubUnits = unit.getAllSubUnits();
    	}
    	
		YearMonthDay today = new YearMonthDay();

		for (PersonFunction personFunction : getPersonFunctions()) {
			if (active != null && (personFunction.isActive(today) == !active)) {
				continue;
			}
			
			if (virtual != null && (personFunction.getFunction().isVirtual() == !virtual)) {
				continue;
			}
			
    	    Unit functionUnit = personFunction.getUnit();
			if (unit == null || functionUnit.equals(unit) || (includeSubUnits && allSubUnits.contains(functionUnit))) {
				result.add(personFunction);
			}
    	}
		
    	return result;
    }

    public boolean hasFunctionType(FunctionType functionType) {
	for (PersonFunction accountability : getActivePersonFunctions()) {
	    if (accountability.getFunction().getFunctionType() == functionType) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasActivePersonFunction(FunctionType functionType, Unit unit) {
	YearMonthDay currentDate = new YearMonthDay();
	for (PersonFunction personFunction : (Collection<PersonFunction>) getParentAccountabilities(
		AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)) {
	    if (personFunction.getUnit().equals(unit)
		    && personFunction.getFunction().getFunctionType() == functionType
		    && personFunction.isActive(currentDate)) {
		return true;
	    }
	}
	return false;
    }

    public PersonFunction addPersonFunction(Function function, YearMonthDay begin, YearMonthDay end,
	    Double credits) {
	return new PersonFunction(function.getUnit(), this, function, begin, end, credits);
    }

    /**
     * @return a group that only contains this person
     */
    public PersonGroup getPersonGroup() {
	return new PersonGroup(this);
    }

    /**
     * 
     * IMPORTANT: This method is evil and should NOT be used! You are NOT God!
     * 
     * 
     * @return true if the person have been deleted, false otherwise
     */
    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.person.cannot.be.deleted");
	}
	if (hasPersonalPhoto()) {
	    getPersonalPhoto().delete();
	}
	if (hasParkingParty()) {
	    getParkingParty().delete();
	}
	if (hasAssociatedPersonAccount()) {
	    getAssociatedPersonAccount().delete();
	}
	if (hasHomepage()) { // check if can delete made in #canBeDeleted()
	    getHomepage().delete();
	}
	if (hasUser()) {
	    getUser().delete();
	}

	getPersonRoles().clear();
	getManageableDepartmentCredits().clear();
	getAdvisories().clear();

	if (hasPersonName()) {
	    getPersonName().delete();
	}

	getBookmarkedBoards().clear();
	getPersonRoles().clear();
	getManageableDepartmentCredits().clear();
	getAdvisories().clear();

	for (; !getIdDocumentsSet().isEmpty(); getIdDocumentsSet().iterator().next().delete())
	    ;

	removeNationality();
	removeCountryOfBirth();

	if (hasStudent()) {
	    getStudent().delete();
	}
	
	for (; !getThesisEvaluationParticipants().isEmpty(); getThesisEvaluationParticipants()
	.iterator().next().delete())
	    ;

	while (hasAnyScientificCommissions()) {
	    getScientificCommissions().iterator().next().delete();
	}

	super.delete();
    }
    
    private boolean canBeDeleted() {	    	 
	return !hasAnyChilds() && !hasAnyParents() && !hasAnyDomainObjectActionLogs()
	&& !hasAnySentSms() && !hasAnyExportGroupingReceivers() 				
	&& !hasAnyPersistentGroups() && !hasAnyPersonSpaceOccupations()
	&& !hasAnyPunctualRoomsOccupationComments() && !hasAnyVehicleAllocations()
	&& !hasAnyPunctualRoomsOccupationRequests() && !hasAnyPunctualRoomsOccupationRequestsToProcess()				
	&& !hasAnyAssociatedQualifications() && !hasAnyAssociatedAlteredCurriculums()
	&& !hasAnyEnrolmentEvaluations() && !hasAnyExportGroupingSenders()
	&& !hasAnyResponsabilityTransactions() && !hasAnyMasterDegreeCandidates()
	&& !hasAnyGuides() && !hasAnyProjectAccesses() && !hasAnyPersonAuthorships()
	&& !hasEmployee() && !hasTeacher() && !hasGrantOwner() && !hasAnyPayedGuides()
	&& !hasAnyPayedReceipts() && !hasParking() && !hasAnyResearchInterests()
	&& !hasAnyProjectParticipations() && !hasAnyParticipations() && !hasAnyBoards()
	&& !hasAnyPersonFunctions() && (!hasHomepage() || getHomepage().canBeDeleted());
    }

    private boolean hasParking() {
	if (hasParkingParty()) {
	    return getParkingParty().hasAnyVehicles();
	}
	return false;
    }

    public ExternalContract getExternalContract() {
	Collection<ExternalContract> externalContracts = (Collection<ExternalContract>) getParentAccountabilities(
		AccountabilityTypeEnum.WORKING_CONTRACT, ExternalContract.class);

	Iterator<ExternalContract> iter = externalContracts.iterator();
	return iter.hasNext() ? externalContracts.iterator().next() : null;
    }

    public boolean hasExternalContract() {
	return getExternalContract() != null;
    }

    public ResearchContract getExternalResearchContract() {
	Collection<ResearchContract> externalContracts = (Collection<ResearchContract>) getParentAccountabilities(
		AccountabilityTypeEnum.RESEARCH_CONTRACT, ResearchContract.class);

	Iterator<ResearchContract> iter = externalContracts.iterator();
	if (iter.hasNext()) {
	    ResearchContract contract = externalContracts.iterator().next();
	    if (contract.getExternalContract()) {
		return contract;
	    }
	}
	return null;
    }

    public boolean hasExternalResearchContract() {
	return getExternalResearchContract() != null;
    }

    private static class PersonRoleListener extends dml.runtime.RelationAdapter<Role, Person> {

	@Override
	public void beforeAdd(Role newRole, Person person) {
	    // Do nothing!!
	}

	@Override
	public void afterAdd(Role insertedRole, Person person) {
	    if (person != null && insertedRole != null) {
		addDependencies(insertedRole, person);
		person.addAlias(insertedRole);
		person.updateIstUsername();
	    }
	}

	@Override
	public void beforeRemove(Role roleToBeRemoved, Person person) {
	    if (person != null && roleToBeRemoved != null
		    && person.hasRole(roleToBeRemoved.getRoleType())) {
		removeDependencies(person, roleToBeRemoved);
	    }
	}

	@Override
	public void afterRemove(Role removedRole, Person person) {
	    if (person != null && removedRole != null) {
		person.removeAlias(removedRole);
		person.updateIstUsername();
	    }
	}

	private void addDependencies(Role role, Person person) {
	    switch (role.getRoleType()) {

	    case PERSON:
		addRoleIfNotPresent(person, RoleType.MESSAGING);
		break;

	    case TEACHER:
		addRoleIfNotPresent(person, RoleType.PERSON);
		addRoleIfNotPresent(person, RoleType.EMPLOYEE);
		addRoleIfNotPresent(person, RoleType.RESEARCHER);
		addRoleIfNotPresent(person, RoleType.DEPARTMENT_MEMBER);
		break;

	    case DELEGATE:
	    case OPERATOR:
	    case RESEARCHER:
	    case GEP:
	    case MANAGER:
	    case WEBSITE_MANAGER:
	    case RESOURCE_ALLOCATION_MANAGER:
	    case RESOURCE_MANAGER:
	    case EMPLOYEE:
	    case STUDENT:
	    case ALUMNI:
	    case GRANT_OWNER:
	    case SPACE_MANAGER_SUPER_USER:
	    case SPACE_MANAGER:
		addRoleIfNotPresent(person, RoleType.PERSON);
		break;

	    case DIRECTIVE_COUNCIL:
	    case SEMINARIES_COORDINATOR:
	    case COORDINATOR:
	    case DEGREE_ADMINISTRATIVE_OFFICE:
	    case DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER:
	    case MASTER_DEGREE_ADMINISTRATIVE_OFFICE:
	    case DEPARTMENT_CREDITS_MANAGER:
	    case GRANT_OWNER_MANAGER:
	    case TREASURY:
	    case CREDITS_MANAGER:
	    case EXAM_COORDINATOR:
	    case DEPARTMENT_ADMINISTRATIVE_OFFICE:
	    case PERSONNEL_SECTION:
	    case PROJECTS_MANAGER:
	    case INSTITUCIONAL_PROJECTS_MANAGER:
		addRoleIfNotPresent(person, RoleType.EMPLOYEE);
		break;

	    default:
		break;
	    }
	}

	private static void removeDependencies(Person person, Role removedRole) {
	    switch (removedRole.getRoleType()) {
	    case PERSON:
		removeRoleIfPresent(person, RoleType.TEACHER);
		removeRoleIfPresent(person, RoleType.EMPLOYEE);
		removeRoleIfPresent(person, RoleType.STUDENT);
		removeRoleIfPresent(person, RoleType.GEP);
		removeRoleIfPresent(person, RoleType.GRANT_OWNER);
		removeRoleIfPresent(person, RoleType.MANAGER);
		removeRoleIfPresent(person, RoleType.OPERATOR);
		removeRoleIfPresent(person, RoleType.RESOURCE_ALLOCATION_MANAGER);
		removeRoleIfPresent(person, RoleType.WEBSITE_MANAGER);
		removeRoleIfPresent(person, RoleType.MESSAGING);
		removeRoleIfPresent(person, RoleType.ALUMNI);
		removeRoleIfPresent(person, RoleType.SPACE_MANAGER);
		removeRoleIfPresent(person, RoleType.SPACE_MANAGER_SUPER_USER);
		break;

	    case TEACHER:
		removeRoleIfPresent(person, RoleType.EMPLOYEE);
		removeRoleIfPresent(person, RoleType.RESEARCHER);
		removeRoleIfPresent(person, RoleType.DEPARTMENT_MEMBER);
		removeRoleIfPresent(person, RoleType.DELEGATE);
		break;

	    case EMPLOYEE:
		removeRoleIfPresent(person, RoleType.SEMINARIES_COORDINATOR);
		removeRoleIfPresent(person, RoleType.RESEARCHER);
		removeRoleIfPresent(person, RoleType.GRANT_OWNER_MANAGER);
		removeRoleIfPresent(person, RoleType.SEMINARIES_COORDINATOR);
		removeRoleIfPresent(person, RoleType.DIRECTIVE_COUNCIL);
		removeRoleIfPresent(person, RoleType.COORDINATOR);
		removeRoleIfPresent(person, RoleType.CREDITS_MANAGER);
		removeRoleIfPresent(person, RoleType.TREASURY);
		removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
		removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
		removeRoleIfPresent(person, RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		removeRoleIfPresent(person, RoleType.DEPARTMENT_CREDITS_MANAGER);
		removeRoleIfPresent(person, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
		removeRoleIfPresent(person, RoleType.PERSONNEL_SECTION);
		removeRoleIfPresent(person, RoleType.PROJECTS_MANAGER);
		removeRoleIfPresent(person, RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
		break;

	    case STUDENT:
		removeRoleIfPresent(person, RoleType.DELEGATE);
		break;

	    default:
		break;
	    }
	}

	private static void removeRoleIfPresent(Person person, RoleType roleType) {
	    if (person.hasRole(roleType)) {
		person.removeRoleByType(roleType);
	    }
	}

	private static void addRoleIfNotPresent(Person person, RoleType roleType) {
	    if (!person.hasRole(roleType)) {
		person.addPersonRoleByRoleType(roleType);
	    }
	}
    }

    public SortedSet<SentSms> getSentSmsSortedBySendDate() {
	final SortedSet<SentSms> sentSmsSortedBySendDate = new TreeSet<SentSms>(new ReverseComparator(
		PERSON_SENTSMS_COMPARATOR_BY_SENT_DATE));
	sentSmsSortedBySendDate.addAll(this.getSentSmsSet());
	return sentSmsSortedBySendDate;
    }

    public int countSentSmsBetween(final Date startDate, final Date endDate) {
	final DateTime start = new DateTime(startDate);
	final DateTime end = new DateTime(endDate);

	int count = 0;
	for (final SentSms sentSms : this.getSentSmsSet()) {
	    if (sentSms.getDeliveryType() != SmsDeliveryType.NOT_SENT_TYPE
		    && (sentSms.getSendDateDateTime().isAfter(start) || sentSms.getSendDateDateTime()
			    .equals(start)) && sentSms.getSendDateDateTime().isBefore(end)) {
		count++;
	    }
	}
	return count;
    }

    @Deprecated
    public Registration readStudentByDegreeType(DegreeType degreeType) {
	for (final Registration registration : this.getStudents()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		return registration;
	    }
	}
	return null;
    }
    
    public Registration readRegistrationByDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	return getStudent().readRegistrationByDegreeCurricularPlan(degreeCurricularPlan);
    }

    public MasterDegreeCandidate getMasterDegreeCandidateByExecutionDegree(
	    final ExecutionDegree executionDegree) {
	for (final MasterDegreeCandidate masterDegreeCandidate : this.getMasterDegreeCandidatesSet()) {
	    if (masterDegreeCandidate.getExecutionDegree() == executionDegree) {
		return masterDegreeCandidate;
	    }
	}
	return null;
    }

    public DFACandidacy getDFACandidacyByExecutionDegree(final ExecutionDegree executionDegree) {
	for (final Candidacy candidacy : this.getCandidaciesSet()) {
	    if (candidacy instanceof DFACandidacy) {
		final DFACandidacy dfaCandidacy = (DFACandidacy) candidacy;
		if (dfaCandidacy.getExecutionDegree().equals(executionDegree)) {
		    return dfaCandidacy;
		}
	    }
	}
	return null;
    }

    public DegreeCandidacy getDegreeCandidacyByExecutionDegree(final ExecutionDegree executionDegree) {
	for (final Candidacy candidacy : this.getCandidaciesSet()) {
	    if (candidacy instanceof DegreeCandidacy) {
		final DegreeCandidacy degreeCandidacy = (DegreeCandidacy) candidacy;
		if (degreeCandidacy.getExecutionDegree().equals(executionDegree)) {
		    return degreeCandidacy;
		}
	    }
	}
	return null;
    }

    public List<DegreeCandidacy> getDegreeCandidaciesFor(final ExecutionYear executionYear,
	    final CandidacySituationType candidacySituationType) {

	final List<DegreeCandidacy> result = new ArrayList<DegreeCandidacy>();
	for (final Candidacy candidacy : this.getCandidaciesSet()) {
	    if (candidacy instanceof DegreeCandidacy) {
		final DegreeCandidacy degreeCandidacy = (DegreeCandidacy) candidacy;
		if (degreeCandidacy.getActiveCandidacySituation().getCandidacySituationType() == candidacySituationType
			&& degreeCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {

		    result.add((DegreeCandidacy) candidacy);
		}
	    }
	}

	return result;
    }

    public boolean hasDegreeCandidacyForExecutionDegree(ExecutionDegree executionDegree) {
	return (getDegreeCandidacyByExecutionDegree(executionDegree) != null);
    }

    public StudentCandidacy getStudentCandidacyForExecutionDegree(ExecutionDegree executionDegree) {
	for (final Candidacy candidacy : this.getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (studentCandidacy.getExecutionDegree().equals(executionDegree)) {
		    return studentCandidacy;
		}
	    }
	}
	return null;
    }

    public boolean hasStudentCandidacyForExecutionDegree(ExecutionDegree executionDegree) {
	return (getStudentCandidacyForExecutionDegree(executionDegree) != null);
    }

    public String getPostalCode() {
	final StringBuilder result = new StringBuilder();
	result.append(getDefaultPhysicalAddress().getAreaCode());
	result.append(" ");
	result.append(getDefaultPhysicalAddress().getAreaOfAreaCode());
	return result.toString();
    }

    @Override
    public void setSocialSecurityNumber(String socialSecurityNumber) {
	if (!StringUtils.isEmpty(socialSecurityNumber)) {
	    final Party existingContributor = Party.readByContributorNumber(socialSecurityNumber);
	    if (existingContributor != null && existingContributor != this) {
		throw new DomainException("PERSON.createContributor.existing.contributor.number");
	    }
	}
	super.setSocialSecurityNumber(socialSecurityNumber);
    }

    public Collection<Invitation> getInvitationsOrderByDate() {
	Set<Invitation> invitations = new TreeSet<Invitation>(Invitation.CONTRACT_COMPARATOR_BY_BEGIN_DATE);
	invitations.addAll((Collection<Invitation>) getParentAccountabilities(
		AccountabilityTypeEnum.INVITATION, Invitation.class));
	return invitations;
    }

    public boolean isInvited(YearMonthDay date) {
	for (Invitation invitation : (Collection<Invitation>) getParentAccountabilities(
		AccountabilityTypeEnum.INVITATION, Invitation.class)) {
	    if (invitation.isActive(date)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyInvitation() {
	return !getParentAccountabilities(AccountabilityTypeEnum.INVITATION, Invitation.class).isEmpty();
    }

    // -------------------------------------------------------------
    // static methods
    // -------------------------------------------------------------

    public static Person readPersonByUsernameWithOpenedLogin(final String username) {
	final Login login = Login.readLoginByUsername(username);
	final User user = login == null ? null : (login.isOpened()) ? login.getUser() : null;
	return user == null ? null : user.getPerson();
    }

    public static Person readPersonByUsername(final String username) {
	final Login login = Login.readLoginByUsername(username);
	final User user = login == null ? null : login.getUser();
	return user == null ? null : user.getPerson();
    }

    public static Person readPersonByIstUsername(final String istUsername) {
	final User user = User.readUserByUserUId(istUsername);
	return user == null ? null : user.getPerson();
    }

    public static Collection<Person> readByDocumentIdNumber(final String documentIdNumber) {
	Collection<Person> result = new ArrayList<Person>();
	for (final IdDocument idDocument : IdDocument.find(documentIdNumber)) {
	    result.add(idDocument.getPerson());
	}
	return result;
    }

    public static Person readByDocumentIdNumberAndIdDocumentType(final String documentIdNumber,
	    final IDDocumentType idDocumentType) {
	for (final IdDocument idDocument : IdDocument.find(documentIdNumber)) {
	    if (idDocument.getIdDocumentType().getValue() == idDocumentType) {
		return idDocument.getPerson();
	    }
	}
	return null;
    }

    public static Person readByDocumentIdNumberAndDateOfBirth(final String documentIdNumber,
	    final YearMonthDay dateOfBirth) {
	for (final IdDocument idDocument : IdDocument.find(documentIdNumber)) {
	    final Person person = idDocument.getPerson();
	    if (person.getDateOfBirthYearMonthDay().equals(dateOfBirth)) {
		return person;
	    }
	}
	return null;
    }
    
    public static Collection<Person> findByDateOfBirth(final YearMonthDay dateOfBirth, final Collection<Person> persons) {
	List<Person> result = new ArrayList<Person>();
	for (Person person : persons) {
	    if (person.getDateOfBirthYearMonthDay() == null || person.getDateOfBirthYearMonthDay().equals(dateOfBirth)) {
		result.add(person);
	    }
	}
	return result;
    }    

    // used by grant owner
    public static List<Person> readPersonsByName(final String name, final Integer startIndex,
	    final Integer numberOfElementsInSpan) {
	final List<Person> personsList = readPersonsByName(name);
	if (startIndex != null && numberOfElementsInSpan != null && !personsList.isEmpty()) {
	    int finalIndex = Math.min(personsList.size(), startIndex + numberOfElementsInSpan);
	    return personsList.subList(startIndex, finalIndex);
	}
	return Collections.EMPTY_LIST;
    }

    public static Integer countAllByName(final String name) {
	return Integer.valueOf(readPersonsByName(name).size());
    }

    public static List<Person> readPersonsByName(final String name) {
	final List<Person> result = new ArrayList<Person>();
	if (name != null) {
	    final String nameToMatch = name.replaceAll("%", ".*").toLowerCase();
	    for (final Party party : RootDomainObject.getInstance().getPartysSet()) {
	        if (party.isPerson()) {
	            final Person person = (Person) party;
	            if (person.getName().toLowerCase().matches(nameToMatch)) {
	                result.add(person);
	            }
	        }
	    }
	}
	return result;
    }

    public static List<Person> readAllPersons() {
	List<Person> allPersons = new ArrayList<Person>();
	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party.isPerson()) {
		allPersons.add((Person) party);
	    }
	}
	return allPersons;
    }

    public static List<Person> readAllExternalPersons() {
	List<Person> allPersons = new ArrayList<Person>();
	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party.isPerson() && ((Person) party).hasExternalContract()) {
		allPersons.add((Person) party);
	    }
	}
	return allPersons;
    }

    public SortedSet<StudentCurricularPlan> getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName() {
	final SortedSet<StudentCurricularPlan> studentCurricularPlans = new TreeSet<StudentCurricularPlan>(
		StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME);
	for (final Registration registration : getStudentsSet()) {
	    final StudentCurricularPlan studentCurricularPlan = registration
	    .getActiveStudentCurricularPlan();
	    if (studentCurricularPlan != null) {
		studentCurricularPlans.add(studentCurricularPlan);
	    }
	}
	return studentCurricularPlans;
    }

    public SortedSet<StudentCurricularPlan> getCompletedStudentCurricularPlansSortedByDegreeTypeAndDegreeName() {
	final SortedSet<StudentCurricularPlan> studentCurricularPlans = new TreeSet<StudentCurricularPlan>(
		StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME);

	for (final Registration registration : getStudentsSet()) {
	    if (registration.isConcluded()) {
		StudentCurricularPlan lastStudent = registration
		.getLastStudentCurricularPlan();
		if (lastStudent != null) {
		    studentCurricularPlans.add(lastStudent);
		}
	    }
	}
	return studentCurricularPlans;
    }

    public List<ProjectAccess> readProjectAccessesByCoordinator(final Integer coordinatorCode) {

	final YearMonthDay now = new YearMonthDay();
	final List<ProjectAccess> result = new ArrayList<ProjectAccess>();

	for (final ProjectAccess projectAccess : getProjectAccessesSet()) {
	    if (projectAccess.getKeyProjectCoordinator().equals(coordinatorCode)) {
		if (!now.isBefore(projectAccess.getBeginDateTime().toYearMonthDay())
			&& !now.isAfter(projectAccess.getEndDateTime().toYearMonthDay())) {
		    result.add(projectAccess);
		}
	    }
	}
	return result;
    }

    public Set<Attends> getCurrentAttends() {
	final Set<Attends> attends = new HashSet<Attends>();
	for (final Registration registration : getStudentsSet()) {
	    for (final Attends attend : registration.getAssociatedAttendsSet()) {
		final ExecutionCourse executionCourse = attend.getExecutionCourse();
		final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
		if (executionPeriod.getState().equals(PeriodState.CURRENT)) {
		    attends.add(attend);
		}
	    }
	}
	return attends;
    }

    public boolean hasIstUsername() {
	if (this.getIstUsername() != null) {
	    return true;
	}
	if (UsernameUtils.shouldHaveUID(this)) {
	    setIstUsername();
	    return true;
	}
	return false;
    }

    public static class FindPersonFactory implements Serializable, FactoryExecutor {
	private Integer institutionalNumber;

	public Integer getInstitutionalNumber() {
	    return institutionalNumber;
	}

	public void setInstitutionalNumber(Integer institutionalNumber) {
	    this.institutionalNumber = institutionalNumber;
	}

	transient Set<Person> people = null;

	public FindPersonFactory execute() {
	    people = Person.findPerson(this);
	    return this;
	}

	public Set<Person> getPeople() {
	    return people;
	}
    }

    public static Set<Person> findPerson(final FindPersonFactory findPersonFactory) {
	final Set<Person> people = new HashSet<Person>();
	for (final Party party : RootDomainObject.getInstance().getPartysSet()) {
	    if (party instanceof Person) {
		final Person person = (Person) party;
		if (findPersonFactory.getInstitutionalNumber() != null) {
		    if (person.getTeacher() != null
			    && person.getTeacher().getTeacherNumber().equals(
				    findPersonFactory.getInstitutionalNumber())) {
			people.add(person);
		    } else if (person.getEmployee() != null
			    && person.getEmployee().getEmployeeNumber().equals(
				    findPersonFactory.getInstitutionalNumber())) {
			people.add(person);
		    } else if (person.hasStudentWithNumber(findPersonFactory.getInstitutionalNumber())) {
			people.add(person);
		    }
		}
	    }
	}
	return people;
    }

    private boolean hasStudentWithNumber(final Integer institutionalNumber) {
	for (final Registration registration : getStudents()) {
	    if (registration.getNumber().equals(institutionalNumber)) {
		return true;
	    }
	}
	return false;
    }

    public Set<Event> getNotPayedEventsPayableOn(AdministrativeOffice administrativeOffice,
	    boolean withInstallments) {
	final Set<Event> result = new HashSet<Event>();

	for (final Event event : getEventsSet()) {
	    if (event.isOpen() && event.hasInstallments() == withInstallments
		    && isPayableOnAdministrativeOffice(administrativeOffice, event)) {
		result.add(event);
	    }
	}

	return result;
    }

    public Set<Event> getNotPayedEventsPayableOn(AdministrativeOffice administrativeOffice) {
	final Set<Event> result = new HashSet<Event>();
	for (final Event event : getEventsSet()) {
	    if (event.isOpen() && isPayableOnAdministrativeOffice(administrativeOffice, event)) {
		result.add(event);
	    }
	}

	return result;
    }

    private boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice,
	    final Event event) {
	return ((administrativeOffice == null) || (event
		.isPayableOnAdministrativeOffice(administrativeOffice)));
    }

    public List<Event> getPayedEvents() {
	final List<Event> result = new ArrayList<Event>();
	for (final Event event : getEventsSet()) {
	    if (event.isClosed()) {
		result.add(event);
	    }
	}

	return result;
    }

    public List<Event> getEventsWithPayments() {
	final List<Event> result = new ArrayList<Event>();
	for (final Event event : getEventsSet()) {
	    if (!event.isCancelled() && event.hasAnyPayments()) {
		result.add(event);
	    }
	}

	return result;
    }

    public Set<Entry> getPaymentsWithoutReceipt() {
	return getPaymentsWithoutReceiptByAdministrativeOffice(null);
    }

    public Set<Entry> getPaymentsWithoutReceiptByAdministrativeOffice(
	    AdministrativeOffice administrativeOffice) {
	final Set<Entry> result = new HashSet<Entry>();

	for (final Event event : getEventsSet()) {
	    if (!event.isCancelled() && isPayableOnAdministrativeOffice(administrativeOffice, event)) {
		result.addAll(event.getEntriesWithoutReceipt());
	    }
	}

	return result;
    }

    public Set<Entry> getPayments() {
	final Set<Entry> result = new HashSet<Entry>();
	for (final Event event : getEventsSet()) {
	    if (!event.isCancelled()) {
		result.addAll(event.getPositiveEntries());
	    }
	}
	return result;
    }

    public Set<? extends Event> getEventsByEventType(final EventType eventType) {
	return getEventsByEventTypeAndClass(eventType, null);
    }

    public Set<? extends Event> getEventsByEventTypeAndClass(final EventType eventType, final Class clazz) {
	final Set<Event> result = new HashSet<Event>();

	for (final Event event : getEventsSet()) {
	    if (!event.isCancelled() && event.getEventType() == eventType && (clazz == null || event.getClass().equals(clazz))) {
		result.add(event);
	    }
	}

	return result;
    }

    public Set<AnnualEvent> getAnnualEventsFor(final ExecutionYear executionYear) {
	final Set<AnnualEvent> result = new HashSet<AnnualEvent>();
	for (final Event event : getEventsSet()) {
	    if (event instanceof AnnualEvent) {
		final AnnualEvent annualEvent = (AnnualEvent) event;
		if (annualEvent.isFor(executionYear) && !annualEvent.isCancelled()) {
		    result.add(annualEvent);
		}
	    }
	}

	return result;
    }

    public Set<AnnualEvent> getOpenAnnualEventsFor(final ExecutionYear executionYear) {
	final Set<AnnualEvent> result = new HashSet<AnnualEvent>();
	for (final Event event : getEventsSet()) {
	    if (event instanceof AnnualEvent) {
		final AnnualEvent annualEvent = (AnnualEvent) event;
		if (annualEvent.isFor(executionYear) && annualEvent.isOpen()) {
		    result.add(annualEvent);
		}
	    }
	}

	return result;
    }

    public boolean hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(
	    final ExecutionYear executionYear) {
	return hasInsuranceEventFor(executionYear)
	|| hasAdministrativeOfficeFeeInsuranceEventFor(executionYear);
    }

    public InsuranceEvent getInsuranceEventFor(final ExecutionYear executionYear) {
	for (final Event event : getEventsByEventType(EventType.INSURANCE)) {
	    final InsuranceEvent insuranceEvent = (InsuranceEvent) event;
	    if (!insuranceEvent.isCancelled() && insuranceEvent.isFor(executionYear)) {
		return insuranceEvent;
	    }
	}

	return null;

    }

    public boolean hasInsuranceEventFor(final ExecutionYear executionYear) {
	return getInsuranceEventFor(executionYear) != null;
    }

    public AdministrativeOfficeFeeAndInsuranceEvent getAdministrativeOfficeFeeInsuranceEventFor(
	    final ExecutionYear executionYear) {
	for (final Event event : getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE)) {
	    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
	    if (!administrativeOfficeFeeAndInsuranceEvent.isCancelled()
		    && administrativeOfficeFeeAndInsuranceEvent.isFor(executionYear)) {
		return administrativeOfficeFeeAndInsuranceEvent;
	    }
	}

	return null;
    }

    public boolean hasAdministrativeOfficeFeeInsuranceEventFor(final ExecutionYear executionYear) {
	return getAdministrativeOfficeFeeInsuranceEventFor(executionYear) != null;
    }

    public Set<Event> getEventsSupportingPaymentByOtherParties() {
	final Set<Event> result = new HashSet<Event>();
	for (final Event event : getEventsSet()) {
	    if (!event.isCancelled() && event.isOtherPartiesPaymentsSupported()) {
		result.add(event);
	    }
	}

	return result;
    }

    public ImprovementOfApprovedEnrolmentEvent getNotPayedImprovementOfApprovedEnrolmentEvent() {
	for (final Event event : getEventsByEventType(EventType.IMPROVEMENT_OF_APPROVED_ENROLMENT)) {
	    if (!event.isPayed()) {
		return (ImprovementOfApprovedEnrolmentEvent) event;
	    }
	}

	return null;
    }

    public Set<GratuityEvent> getGratuityEvents() {
	return (Set<GratuityEvent>) getEventsByEventType(EventType.GRATUITY);
    }

    public List<Event> getEventsWithExemptionAppliable() {
	final List<Event> result = new ArrayList<Event>();
	for (final Event event : getEventsSet()) {
	    if (!event.isCancelled() && event.isExemptionAppliable()) {
		result.add(event);
	    }
	}

	return result;
    }

    public Money getMaxDeductableAmountForLegalTaxes(final EventType eventType, final int civilYear) {
	Money result = Money.ZERO;
	for (final Event event : (Set<Event>) getEventsByEventType(eventType)) {
	    result = result.add(event.getMaxDeductableAmountForLegalTaxes(civilYear));
	}

	return result;
    }

    public Set<Receipt> getReceiptsByAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	final Set<Receipt> result = new HashSet<Receipt>();
	for (final Receipt receipt : getReceipts()) {
	    if (receipt.isFromAdministrativeOffice(administrativeOffice)) {
		result.add(receipt);
	    }
	}

	return result;
    }

    static public Party createContributor(final String contributorName, final String contributorNumber,
	    final PhysicalAddressData data) {

	if (Party.readByContributorNumber(contributorNumber) != null) {
	    throw new DomainException("EXTERNAL_PERSON.createContributor.existing.contributor.number");
	}

	Person externalPerson = createExternalPerson(contributorName, Gender.MALE, data, null, null,
		null, null, String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);
	externalPerson.setSocialSecurityNumber(contributorNumber);

	new ExternalContract(externalPerson,
		RootDomainObject.getInstance().getExternalInstitutionUnit(), new YearMonthDay(), null);

	return externalPerson;
    }

    public Collection<AnnouncementBoard> getCurrentExecutionCoursesAnnouncementBoards() {
	final Collection<AnnouncementBoard> result = new HashSet<AnnouncementBoard>();
	result.addAll(getTeacherCurrentExecutionCourseAnnouncementBoards());
	result.addAll(getStudentCurrentExecutionCourseAnnouncementBoards());
	return result;
    }

    private Collection<AnnouncementBoard> getTeacherCurrentExecutionCourseAnnouncementBoards() {
	if (!hasTeacher()) {
	    return Collections.emptyList();
	}
	final Collection<AnnouncementBoard> result = new HashSet<AnnouncementBoard>();
	for (final Professorship professorship : getTeacher().getProfessorships()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod() == ExecutionPeriod
		    .readActualExecutionPeriod()) {
		final AnnouncementBoard board = professorship.getExecutionCourse().getBoard();
		if (board != null && board.hasReaderOrWriter(this)) {
		    result.add(board);
		}
	    }
	}
	return result;
    }

    private Collection<AnnouncementBoard> getStudentCurrentExecutionCourseAnnouncementBoards() {
	if (!hasStudent()) {
	    return Collections.emptyList();
	}
	final Collection<AnnouncementBoard> result = new HashSet<AnnouncementBoard>();
	for (final Registration registration : getStudent().getRegistrationsSet()) {
	    for (final Attends attends : registration.getAssociatedAttendsSet()) {
		if (attends.getExecutionCourse().isLecturedIn(
			ExecutionPeriod.readActualExecutionPeriod())) {
		    final AnnouncementBoard board = attends.getExecutionCourse().getBoard();
		    if (board != null && board.hasReaderOrWriter(this)) {
			result.add(board);
		    }
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
    public List<Registration> getStudents() {
	return hasStudent() ? getStudent().getRegistrations() : Collections.EMPTY_LIST;
    }

    @Deprecated
    public boolean hasAnyStudents() {
	return getStudentsCount() > 0;
    }

    @Deprecated
    public int getStudentsCount() {
	return hasStudent() ? getStudent().getRegistrationsCount() : 0;
    }

    @Deprecated
    public Set<Registration> getStudentsSet() {
	return hasStudent() ? getStudent().getRegistrationsSet() : Collections.EMPTY_SET;
    }

    @Override
    public PartyClassification getPartyClassification() {
        final Teacher teacher = getTeacher();
        if (teacher != null) {
            if (teacher.getCurrentWorkingDepartment() != null
                    && !teacher.isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                return PartyClassification.TEACHER;
            }
        }        
        final Employee employee = getEmployee();
        if (employee != null && employee.getCurrentWorkingContract() != null
                && (teacher == null || teacher.getCurrentWorkingDepartment() == null)) {
            return PartyClassification.EMPLOYEE;
        }
        if (isResearcher()) {
            return PartyClassification.RESEARCHER;
        }
        final GrantOwner grantOwner = getGrantOwner();
        if (grantOwner != null && grantOwner.hasCurrentContract()) {
            return PartyClassification.GRANT_OWNER;
        }
        final Student student = getStudent();
        if (student != null) {
            final DegreeType degreeType = student.getMostSignificantDegreeType();
            if (degreeType != null) {
                return PartyClassification.getClassificationByDegreeType(degreeType);
            }
        }
        return PartyClassification.PERSON;
    }

    public static class PersonBeanFactoryEditor extends PersonBean implements FactoryExecutor {
	public PersonBeanFactoryEditor(final Person person) {
	    super(person);
	}

	public Object execute() {
	    getPerson().edit(this);
	    return null;
	}
    }

    public static class ExternalPersonBeanFactoryCreator extends ExternalPersonBean implements
    FactoryExecutor {
	public ExternalPersonBeanFactoryCreator() {
	    super();
	}

	public Object execute() {
	    final Person person = new Person(this, true);
	    Unit unit = getUnit();
	    if (unit == null) {
		unit = Unit.findFirstUnitByName(getUnitName());
		if (unit == null) {
		    throw new DomainException("error.unit.does.not.exist");
		}
	    }
	    new ExternalContract(person, unit, new YearMonthDay(), null);
	    return person;
	}
    }

    public static class AnyPersonSearchBean implements Serializable {
	String name;

	String documentIdNumber;

	IDDocumentType idDocumentType;

	public String getDocumentIdNumber() {
	    return documentIdNumber;
	}

	public void setDocumentIdNumber(String documentIdNumber) {
	    this.documentIdNumber = documentIdNumber;
	}

	public IDDocumentType getIdDocumentType() {
	    return idDocumentType;
	}

	public void setIdDocumentType(IDDocumentType idDocumentType) {
	    this.idDocumentType = idDocumentType;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	private boolean matchesAnyCriteria(final String[] nameValues,
		final String[] documentIdNumberValues, final Person person) {
	    return matchesAnyCriteriaField(nameValues, name, person.getName())
	    || matchesAnyCriteriaField(documentIdNumberValues, documentIdNumber, person
		    .getDocumentIdNumber());
	}

	private boolean matchesAnyCriteriaField(final String[] nameValues, final String string,
		final String stringFromPerson) {
	    return isSpecified(string) && areNamesPresent(stringFromPerson, nameValues);
	}

	public SortedSet<Person> search() {
	    final String[] nameValues = name == null ? null : StringNormalizer.normalize(name)
		    .toLowerCase().split("\\p{Space}+");
	    final String[] documentIdNumberValues = documentIdNumber == null ? null : StringNormalizer
		    .normalize(documentIdNumber).toLowerCase().split("\\p{Space}+");

	    final SortedSet<Person> people = new TreeSet<Person>(Party.COMPARATOR_BY_NAME_AND_ID);
	    for (final Party party : RootDomainObject.getInstance().getPartysSet()) {
		if (party.isPerson()) {
		    final Person person = (Person) party;
		    if (matchesAnyCriteria(nameValues, documentIdNumberValues, person)) {
			people.add(person);
		    }
		}
	    }
	    return people;
	}

	public SortedSet<Person> getSearch() {
	    return search();
	}

	public boolean getHasBeenSubmitted() {
	    return isSpecified(name) || isSpecified(documentIdNumber);
	}

	private boolean isSpecified(final String string) {
	    return string != null && string.length() > 0;
	}

	private boolean areNamesPresent(String name, String[] searchNameParts) {
	    String nameNormalized = StringNormalizer.normalize(name).toLowerCase();
	    for (int i = 0; i < searchNameParts.length; i++) {
		String namePart = searchNameParts[i];
		if (!nameNormalized.contains(namePart)) {
		    return false;
		}
	    }
	    return true;
	}
    }

    public Registration getRegistration(ExecutionCourse executionCourse) {
	return executionCourse.getRegistration(this);
    }

    public SortedSet<String> getOrganizationalUnitsPresentation() {
	final SortedSet<String> organizationalUnits = new TreeSet<String>();
	for (final Accountability accountability : getParentsSet()) {
	    if (isOrganizationalUnitsForPresentation(accountability)) {
		final Party party = accountability.getParentParty();
		organizationalUnits.add(party.getName());
	    }
	}
	if (getStudent() != null) {
	    for (final Registration registration : getStudent().getRegistrationsSet()) {
		if (registration.isActive()) {
		    final DegreeCurricularPlan degreeCurricularPlan = registration
			    .getLastDegreeCurricularPlan();
		    final Degree degree = degreeCurricularPlan.getDegree();
		    organizationalUnits.add(degree.getPresentationName());
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

    @Override
    public String getNickname() {
	final String nickname = super.getNickname();
	return nickname == null ? getName() : nickname;
    }

    @Override
    public void setNickname(String nickname) {
	if (!validNickname(nickname)) {
	    throw new DomainException("error.invalid.nickname");
	}
	super.setNickname(nickname);
    }

    private static final Set<String> namePartsToIgnore = new HashSet<String>(5);
    static {
	namePartsToIgnore.add("de");
	namePartsToIgnore.add("da");
	namePartsToIgnore.add("do");
	namePartsToIgnore.add("a");
	namePartsToIgnore.add("e");
	namePartsToIgnore.add("i");
	namePartsToIgnore.add("o");
	namePartsToIgnore.add("u");
    }

    private boolean validNickname(final String name) {
	if (name != null && name.length() > 0) {
	    final String normalizedName = StringNormalizer.normalize(name.replace('-', ' '))
	    .toLowerCase();
	    final String normalizedPersonName = StringNormalizer.normalize(getName().replace('-', ' '))
	    .toLowerCase();

	    final String[] nameParts = normalizedName.split(" ");
	    final String[] personNameParts = normalizedPersonName.split(" ");
	    int matches = 0;
	    for (final String namePart : nameParts) {
		if (!contains(personNameParts, namePart)) {
		    return false;
		}
		if (!namePartsToIgnore.contains(namePart)) {
		    matches++;
		}
	    }
	    if (matches >= 2) {
		return true;
	    }
	}
	return false;
    }

    private boolean contains(final String[] strings, final String xpto) {
	if (xpto == null) {
	    return false;
	}
	for (final String string : strings) {
	    if (string.length() == xpto.length() && string.hashCode() == xpto.hashCode()
		    && string.equals(xpto)) {
		return true;
	    }
	}
	return false;
    }

    public String getHomepageWebAddress() {
	if (hasHomepage() && getHomepage().isHomepageActivated()) {
	    return "/homepage/" + getUsername();
	}
	if (hasAvailableWebSite() && hasDefaultWebAddress() && getDefaultWebAddress().hasUrl()) {
	    return getDefaultWebAddress().getUrl();
	}
	return null;
    }

    public boolean hasAvailableWebSite() {
	return getAvailableWebSite() != null && getAvailableWebSite().booleanValue();
    }

    public Collection<ExecutionDegree> getCoordinatedExecutionDegrees(
	    DegreeCurricularPlan degreeCurricularPlan) {
	Set<ExecutionDegree> result = new TreeSet<ExecutionDegree>(new BeanComparator("executionYear"));
	for (Coordinator coordinator : getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
		result.add(coordinator.getExecutionDegree());
	    }
	}
	return result;
    }

    public boolean isCoordinatorFor(DegreeCurricularPlan degreeCurricularPlan,
	    ExecutionYear executionYear) {
	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionYear) {
		return executionDegree.getCoordinatorByTeacher(this) != null;
	    }
	}
	return false;
    }

    public boolean isResponsibleOrCoordinatorFor(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {
	final Teacher teacher = getTeacher();
	return (teacher != null && teacher.isResponsibleFor(curricularCourse, executionPeriod))
	|| isCoordinatorFor(curricularCourse.getDegreeCurricularPlan(), executionPeriod
		.getExecutionYear());
    }

    public boolean isMasterDegreeOrBolonhaMasterDegreeCoordinatorFor(ExecutionYear executionYear) {
	return isCoordinatorFor(executionYear, Arrays.asList(new DegreeType[] {
		DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE }));

    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegreeCoordinatorFor(
	    ExecutionYear executionYear) {
	return isCoordinatorFor(executionYear, Arrays.asList(new DegreeType[] { DegreeType.DEGREE,
		DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE }));

    }

    public boolean isCoordinatorFor(ExecutionYear executionYear, List<DegreeType> degreeTypes) {
	for (final Coordinator coordinator : getCoordinatorsSet()) {
	    if (coordinator.hasExecutionDegree()
		    && coordinator.getExecutionDegree().getExecutionYear() == executionYear
		    && degreeTypes
		    .contains(coordinator.getExecutionDegree().getDegree().getDegreeType())) {
		return true;
	    }
	}

	return false;

    }

    public ServiceAgreement getServiceAgreementFor(
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	for (final ServiceAgreement serviceAgreement : getServiceAgreementsSet()) {
	    if (serviceAgreement.getServiceAgreementTemplate() == serviceAgreementTemplate) {
		return serviceAgreement;
	    }
	}
	return null;
    }

    public boolean hasServiceAgreementFor(final ServiceAgreementTemplate serviceAgreementTemplate) {
	return getServiceAgreementFor(serviceAgreementTemplate) != null;
    }

    public boolean isHomePageAvailable() {
	return hasHomepage() && getHomepage().getActivated();
    }

    public static Collection<Person> searchPersons(String[] personName) {
	Collection<Person> result = new ArrayList<Person>();
	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party.isPerson() && party.verifyNameEquality(personName)) {
		result.add((Person) party);
	    }
	}

	return result;
    }

    public boolean isAdministrativeOfficeEmployee() {
	return getEmployee() != null && getEmployee().getAdministrativeOffice() != null;
    }

    public List<PunctualRoomsOccupationRequest> getPunctualRoomsOccupationRequestsOrderByMoreRecentComment() {
	List<PunctualRoomsOccupationRequest> result = new ArrayList<PunctualRoomsOccupationRequest>();
	result.addAll(getPunctualRoomsOccupationRequests());
	if (!result.isEmpty()) {
	    Collections.sort(result,
		    PunctualRoomsOccupationRequest.COMPARATOR_BY_MORE_RECENT_COMMENT_INSTANT);
	}
	return result;
    }

    public List<PunctualRoomsOccupationRequest> getPunctualRoomsOccupationRequestsToProcessOrderByDate() {
	List<PunctualRoomsOccupationRequest> result = new ArrayList<PunctualRoomsOccupationRequest>();
	for (PunctualRoomsOccupationRequest request : getPunctualRoomsOccupationRequestsToProcess()) {
	    if (!request.getCurrentState().equals(RequestState.RESOLVED)) {
		result.add(request);
	    }
	}

	if (!result.isEmpty()) {
	    Collections.sort(result, PunctualRoomsOccupationRequest.COMPARATOR_BY_INSTANT);
	}
	return result;
    }

    public String getFirstAndLastName() {
	String[] name = getName().split(" ");
	return name[0] + " " + name[name.length - 1];
    }

    private List<String> getImportantRoles(final List<String> mainRoles) {

	if (getPersonRolesCount() != 0) {
	    boolean teacher = false, employee = false;

	    for (final Role personRole : getPersonRolesSet()) {

		if (personRole.getRoleType() == RoleType.TEACHER) {
		    mainRoles.add("Docente");
		    teacher = true;

		} else if (personRole.getRoleType() == RoleType.STUDENT) {
		    mainRoles.add("Aluno");

		} else if (personRole.getRoleType() == RoleType.GRANT_OWNER) {
		    mainRoles.add("Bolseiro");
		} else if (!teacher && personRole.getRoleType() == RoleType.EMPLOYEE) {
		    employee = true;
		}
	    }
	    if (employee && !teacher) {
		mainRoles.add("Funcionário");
	    }
	}
	return mainRoles;
    }

    public List<String> getMainRoles() {
	return getImportantRoles(new ArrayList<String>());
    }

    public String getMostImportantAlias() {
	final Login login = getLoginIdentification();
	return (login != null) ? login.getMostImportantAlias() : "";
    }

    // Currently, a Person can only have one WorkPhone (so use get(0) -
    // after
    // interface updates remove these methods)
    public Phone getPersonWorkPhone() {
	final List<Phone> partyContacts = (List<Phone>) getPartyContacts(Phone.class,
		PartyContactType.WORK);
	// actually exists only one
	return partyContacts.isEmpty() ? null : (Phone) partyContacts.get(0);
    }

    @Deprecated
    public String getWorkPhone() {
	final Phone workPhone = getPersonWorkPhone();
	return workPhone != null ? workPhone.getNumber() : null;
    }

    @Deprecated
    public void setWorkPhone(String workPhone) {
	final Phone phone = getPersonWorkPhone();
	if (phone == null) {
	    if (!StringUtils.isEmpty(workPhone)) {
		PartyContact.createPhone(this, PartyContactType.INSTITUTIONAL, true, false, workPhone);
	    }
	} else {
	    phone.setNumber(workPhone);
	}
    }

    public String getEmail() {
	return hasInstitutionalEmail() ? getInstitutionalEmail() : super.getEmail();
    }

    public String getInstitutionalEmail() {
	final EmailAddress institutionalEmailAddress = getInstitutionalEmailAddress();
	return institutionalEmailAddress != null ? institutionalEmailAddress.getValue() : null;
    }

    public void updateInstitutionalEmail(final String institutionalEmailString) {
	final EmailAddress institutionalEmailAddress = getInstitutionalEmailAddress();
	if (institutionalEmailAddress == null) {
	    PartyContact.createEmailAddress(this, PartyContactType.INSTITUTIONAL, true, false,
		    institutionalEmailString);
	} else {
	    institutionalEmailAddress.setValue(institutionalEmailString);
	}
    }

    // Currently, a Person can only have one InstitutionalEmailAddress (so
    // use
    // get(0) method)
    private EmailAddress getInstitutionalEmailAddress() {
	final List<EmailAddress> partyContacts = (List<EmailAddress>) getPartyContacts(
		EmailAddress.class, PartyContactType.INSTITUTIONAL);
	// actually exists only one (protected in domain)
	return partyContacts.isEmpty() ? null : (EmailAddress) partyContacts.get(0);
    }

    public Boolean getHasInstitutionalEmail() {
	return Boolean.valueOf(hasInstitutionalEmail());
    }

    public boolean hasInstitutionalEmail() {
	final String institutionalEmail = getInstitutionalEmail();
	return institutionalEmail != null && institutionalEmail.length() > 0;
    }

    public static Collection<Person> findInternalPerson(final String name) {
	final Collection<Person> people = new ArrayList<Person>();
	for (final PersonName personName : PersonName.findInternalPerson(name, Integer.MAX_VALUE)) {
	    people.add(personName.getPerson());
	}
	return people;
    }
    
    public static Collection<Person> findInternalPersonMatchingFirstAndLastName(final String completeName) {
	String[] splittedName = completeName.split(" ");
	return splittedName.length > 0 ? findInternalPerson(splittedName[0] + " " + splittedName[splittedName.length - 1])
		: Collections.EMPTY_LIST;
    }
    
    public static Collection<Person> findExternalPerson(final String name) {
	final Collection<Person> people = new ArrayList<Person>();
	for (final PersonName personName : PersonName.findExternalPerson(name, Integer.MAX_VALUE)) {
	    people.add(personName.getPerson());
	}
	return people;
    }

    public static Collection<Person> findPersonByDocumentID(final String documentIDValue) {
	final Collection<Person> people = new ArrayList<Person>();
	for (final IdDocument idDocument : IdDocument.find(documentIDValue)) {
	    people.add(idDocument.getPerson());
	}
	return people;
    }

    public static Person readPersonByEmailAddress(final String email) {
	final EmailAddress emailAddress = EmailAddress.find(email);
	return (emailAddress != null && emailAddress.getParty().isPerson()) ? (Person) emailAddress
		.getParty() : null;
    }

    public String getUnitText() {
	if (getEmployee() != null && getEmployee().getLastWorkingPlace() != null) {
	    return getEmployee().getLastWorkingPlace().getNameWithAcronym();
	} else if (hasExternalContract()) {
	    return getExternalContract().getInstitutionUnit().getPresentationNameWithParents();
	}
	return "";
    }

    public List<ThesisEvaluationParticipant> getThesisEvaluationParticipants(
	    ExecutionPeriod executionPeriod) {
	ArrayList<ThesisEvaluationParticipant> participants = new ArrayList<ThesisEvaluationParticipant>();

	for (ThesisEvaluationParticipant participant : this.getThesisEvaluationParticipants()) {
	    if (participant.getThesis().getEnrolment().getExecutionYear().equals(
		    executionPeriod.getExecutionYear())) {
		participants.add(participant);
	    }
	}
	Collections.sort(participants, ThesisEvaluationParticipant.COMPARATOR_BY_STUDENT_NUMBER);
	return participants;
    }

    public Set<Proposal> findFinalDegreeWorkProposals() {
	final Set<Proposal> proposals = new HashSet<Proposal>();
	proposals.addAll(getAssociatedProposalsByCoorientatorSet());
	proposals.addAll(getAssociatedProposalsByOrientatorSet());
	return proposals;
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributions() {
	Department department = hasTeacher() ? getTeacher().getCurrentWorkingDepartment() : null;
	return department == null ? Collections.EMPTY_LIST
		: (List<TeacherServiceDistribution>) CollectionUtils.select(department
			.getTeacherServiceDistributions(), new Predicate() {
		    public boolean evaluate(Object arg0) {
			TeacherServiceDistribution tsd = (TeacherServiceDistribution) arg0;
			return tsd.hasAnyPermission(Person.this);
		    }
		});
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributions(ExecutionPeriod period) {
	Department department = hasTeacher() ? getTeacher().getCurrentWorkingDepartment() : null;
	return department == null ? Collections.EMPTY_LIST
		: (List<TeacherServiceDistribution>) CollectionUtils.select(department
			.getTeacherServiceDistributionsByExecutionPeriod(period), new Predicate() {
		    public boolean evaluate(Object arg0) {
			TeacherServiceDistribution tsd = (TeacherServiceDistribution) arg0;
			return tsd.hasAnyPermission(Person.this);
		    }
		});
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributions(ExecutionYear year) {
	Department department = hasTeacher() ? getTeacher().getCurrentWorkingDepartment() : null;
	return department == null ? Collections.EMPTY_LIST
		: (List<TeacherServiceDistribution>) CollectionUtils.select(department
			.getTeacherServiceDistributionsByExecutionYear(year), new Predicate() {
		    public boolean evaluate(Object arg0) {
			TeacherServiceDistribution tsd = (TeacherServiceDistribution) arg0;
			return tsd.hasAnyPermission(Person.this);
		    }
		});
    }

    public List<ResearchUnit> getWorkingResearchUnits() {
	List<ResearchUnit> units = new ArrayList<ResearchUnit>();
	Collection<? extends Accountability> parentAccountabilities = 
	    getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
	
	YearMonthDay currentDate = new YearMonthDay();
	for (Accountability accountability : parentAccountabilities) {
	    if(accountability.isActive(currentDate)) {
		units.add((ResearchUnit) accountability.getParentParty());
	}
	}
	
	return units;
    }

    public String getWorkingResearchUnitNames() {

	String names = "";
	List<ResearchUnit> units = getWorkingResearchUnits();
	int length = units.size();
	for(ResearchUnit unit : units) {
	    names += unit.getName();
	    if (--length > 0) {
		names += ", ";
	    }
	}
	return names;
    }

    public boolean isExternalPerson() {
	return hasExternalContract() || hasExternalResearchContract();
    }

    public boolean isPhotoPubliclyAvailable() {
	Boolean availablePhoto = getAvailablePhoto();
	if (availablePhoto == null || !availablePhoto) {
	    return false;
	}

	if (! isHomePageAvailable()) {
	    return false;
	}

	Boolean showPhotoInHomepage = getHomepage().getShowPhoto();
	return showPhotoInHomepage != null && showPhotoInHomepage;
    }

    public boolean isEmailPubliclyAvailable() {
	Boolean availableEmail = getAvailableEmail();
	if (availableEmail == null || !availableEmail) {
	    return false;
	}

	if (! isHomePageAvailable()) {
	    return false;
	}

	Boolean showEmailInHomepage = getHomepage().getShowEmail();
	return showEmailInHomepage != null && showEmailInHomepage;
    }

    public List<UnitFile> getUploadedFiles(Unit unit) {
	List<UnitFile> files = new ArrayList<UnitFile>();
	for(UnitFile file : getUploadedFiles()) {
	    if(file.getUnit().equals(unit)) {
		files.add(file);
	    }
	}
	return files;
    }

    public String getPresentationName() {
	return getName() + " (" + getUsername() + ")";
    }

    @Override
    public String getPartyPresentationName() {
	return getPresentationName();
    }

    @Override
    public Homepage getSite() {
    	return getHomepage();
    }
    
    @Override
    protected Homepage createSite() {
    	return new Homepage(this);
    }

    @Override
    public Homepage initializeSite() {
    	return (Homepage) super.initializeSite();
    }
    
    public PersonFunction getActiveGGAEDelegatePersonFunction() {
    	for(PersonFunction personFunction : getActivePersonFunctions()) {
    		if(personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
    			return personFunction;
    		}
    	}
    	return null;
    }
    
    public List<PersonFunction> getAllGGAEDelegatePersonFunctions() {
    	List<PersonFunction> result = new ArrayList<PersonFunction>();
    	for(PersonFunction personFunction : getPersonFunctions()) {
    		if(personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
    			result.add(personFunction);
    		}
    	}
    	return result;
    }

    public boolean isResearcher() {
        return getPersonRole(RoleType.RESEARCHER) != null &&
            (getWorkingResearchUnits().isEmpty() || 
                    !getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT, ResearcherContract.class).isEmpty());       
    }

    public Integer getMostSignificantNumber() {
        if(getPartyClassification().equals(PartyClassification.TEACHER)) {
            return getTeacher().getTeacherNumber();
        }
        if (getPartyClassification().equals(PartyClassification.EMPLOYEE)){
            return getEmployee().getEmployeeNumber();
        }
        if (getPartyClassification().equals(PartyClassification.RESEARCHER) && getEmployee() != null){
            return getEmployee().getEmployeeNumber();
        }
        if (getStudent() != null) {
            DegreeType degreeType = getStudent().getMostSignificantDegreeType();
            Collection<Registration> registrations = getStudent()
                    .getRegistrationsByDegreeType(degreeType);
            for (Registration registration : registrations) {
                StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                if (scp != null) {
                    return getStudent().getNumber();
                }
            }
        }
        if (getPartyClassification().equals(PartyClassification.GRANT_OWNER)) {                
            return getGrantOwner().getNumber();
        }        
        return 0;
    }
}