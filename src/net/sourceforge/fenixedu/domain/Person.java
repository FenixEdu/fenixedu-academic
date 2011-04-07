package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationFromUniqueCardDTO;
import net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accounting.AcademicEvent;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreement;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPerson;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacy;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem;
import net.sourceforge.fenixedu.domain.cardGeneration.Category;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.documents.AnnualIRSDeclarationDocument;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.IdDocument;
import net.sourceforge.fenixedu.domain.person.IdDocumentTypeObject;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.domain.research.Researcher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StringFormatter;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.smtp.EmailSender;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Person extends Person_Base {

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

    @Override
    public String getName() {
	return super.getPartyName().getPreferedContent();
    }

    @Override
    public void setName(String name) {

	if (name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.person.empty.name");
	}

	String formattedName = StringFormatter.prettyPrint(name);

	MultiLanguageString partyName = super.getPartyName();
	partyName = partyName == null ? new MultiLanguageString() : partyName;
	partyName.setContent(Language.getDefaultLanguage(), formattedName);

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

    private boolean checkIfDocumentNumberIdAndDocumentIdTypeExists(final String documentIDNumber,
	    final IDDocumentType documentType) {
	Person person = readByDocumentIdNumberAndIdDocumentType(documentIDNumber, documentType);
	return person != null && !person.equals(this);
    }

    final public String getValidatedName() {
	return StringFormatter.prettyPrint(getName());
    }

    public Person() {
	super();
	setMaritalStatus(MaritalStatus.UNKNOWN);
	createLoginIdentificationAndUserIfNecessary();
	setIsPassInKerberos(Boolean.FALSE);
	setAvailablePhoto(Boolean.FALSE);
    }

    /**
     * 
     * @deprecated use Person(PersonBean personBean)
     * @see Person(PersonBean personBean)
     */
    @Deprecated
    public Person(InfoPersonEditor personToCreate, Country country) {

	super();
	if (personToCreate.getIdInternal() != null) {
	    throw new DomainException("error.person.existentPerson");
	}

	createLoginIdentificationAndUserIfNecessary();
	setProperties(personToCreate);
	setCountry(country);
	setIsPassInKerberos(Boolean.FALSE);
    }

    public Person(final String name, final String identificationDocumentNumber, final IDDocumentType identificationDocumentType,
	    final Gender gender) {

	this();
	setName(name);
	setGender(gender);
	setMaritalStatus(MaritalStatus.SINGLE);
	setIdentification(identificationDocumentNumber, identificationDocumentType);
    }

    public Person(final PersonBean personBean) {
	super();

	setProperties(personBean);

	if (personBean.createLoginIdentificationAndUserIfNecessary()) {
	    createLoginIdentificationAndUserIfNecessary();
	    setIsPassInKerberos(Boolean.FALSE);
	}

	PhysicalAddress.createPhysicalAddress(this, personBean.getPhysicalAddressData(), PartyContactType.PERSONAL, true);
	Phone.createPhone(this, personBean.getPhone(), PartyContactType.PERSONAL, true);
	MobilePhone.createMobilePhone(this, personBean.getMobile(), PartyContactType.PERSONAL, true);
	EmailAddress.createEmailAddress(this, personBean.getEmail(), PartyContactType.PERSONAL, true);
	WebAddress.createWebAddress(this, personBean.getWebAddress(), PartyContactType.PERSONAL, true);
    }

    public Person(final IndividualCandidacyPersonalDetails candidacyPersonalDetails) {
	this();

	this.setCountry(candidacyPersonalDetails.getCountry());
	this.setDateOfBirthYearMonthDay(candidacyPersonalDetails.getDateOfBirthYearMonthDay());
	this.setDocumentIdNumber(candidacyPersonalDetails.getDocumentIdNumber());
	this.setExpirationDateOfDocumentIdYearMonthDay(candidacyPersonalDetails.getExpirationDateOfDocumentIdYearMonthDay());
	this.setGender(candidacyPersonalDetails.getGender());
	this.setIdDocumentType(candidacyPersonalDetails.getIdDocumentType());
	this.setName(candidacyPersonalDetails.getName());
	this.setSocialSecurityNumber(candidacyPersonalDetails.getSocialSecurityNumber());

	PhysicalAddressData physicalAddressData = new PhysicalAddressData(candidacyPersonalDetails.getAddress(),
		candidacyPersonalDetails.getAreaCode(), "", candidacyPersonalDetails.getArea(), "", "", "",
		candidacyPersonalDetails.getCountryOfResidence());
	PhysicalAddress.createPhysicalAddress(this, physicalAddressData, PartyContactType.PERSONAL, true);
	Phone.createPhone(this, candidacyPersonalDetails.getTelephoneContact(), PartyContactType.PERSONAL, true);
	EmailAddress.createEmailAddress(this, candidacyPersonalDetails.getEmail(), PartyContactType.PERSONAL, true);
    }

    private Person(final String name, final Gender gender, final PhysicalAddressData data, final String phone,
	    final String mobile, final String homepage, final String email, final String documentIDNumber,
	    final IDDocumentType documentType) {

	this();

	setName(name);
	setGender(gender);
	setIdentification(documentIDNumber, documentType);

	PhysicalAddress.createPhysicalAddress(this, data, PartyContactType.PERSONAL, true);
	Phone.createPhone(this, phone, PartyContactType.PERSONAL, true);
	MobilePhone.createMobilePhone(this, mobile, PartyContactType.PERSONAL, true);
	EmailAddress.createEmailAddress(this, email, PartyContactType.PERSONAL, true);
	WebAddress.createWebAddress(this, homepage, PartyContactType.PERSONAL, true);
    }

    static public Person createExternalPerson(final String name, final Gender gender, final PhysicalAddressData data,
	    final String phone, final String mobile, final String homepage, final String email, final String documentIdNumber,
	    final IDDocumentType documentType) {
	return new Person(name, gender, data, phone, mobile, homepage, email, documentIdNumber, documentType);
    }

    public Person(final String name, final Gender gender, final String documentIDNumber, final IDDocumentType documentType) {

	this();
	setName(name);
	setGender(gender);
	setIdentification(documentIDNumber, documentType);
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

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_OR_GRANT_OWNER_MANAGER_PREDICATE")
    public Person edit(PersonBean personBean) {
	setProperties(personBean);
	setDefaultPhysicalAddressData(personBean.getPhysicalAddressData());
	setDefaultPhoneNumber(personBean.getPhone());
	setDefaultMobilePhoneNumber(personBean.getMobile());
	setDefaultWebAddressUrl(personBean.getWebAddress());
	setDefaultEmailAddressValue(personBean.getEmail());
	return this;
    }

    public Person editByPublicCandidate(PersonBean personBean) {
	setName(personBean.getName());
	setGender(personBean.getGender());
	setIdentification(personBean.getDocumentIdNumber(), personBean.getIdDocumentType());
	setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
	setSocialSecurityNumber(personBean.getSocialSecurityNumber());
	setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
	setCountry(personBean.getNationality());
	setDefaultPhysicalAddressData(personBean.getPhysicalAddressData());
	setDefaultPhoneNumber(personBean.getPhone());
	setDefaultEmailAddressValue(personBean.getEmail());

	return this;
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_OR_GRANT_OWNER_MANAGER_PREDICATE")
    public Person edit(IndividualCandidacyPersonalDetails candidacyExternalDetails) {
	this.setCountry(candidacyExternalDetails.getCountry());

	this.setDateOfBirthYearMonthDay(candidacyExternalDetails.getDateOfBirthYearMonthDay());
	this.setIdentification(candidacyExternalDetails.getDocumentIdNumber(), candidacyExternalDetails.getIdDocumentType());
	this.setExpirationDateOfDocumentIdYearMonthDay(candidacyExternalDetails.getExpirationDateOfDocumentIdYearMonthDay());
	this.setGender(candidacyExternalDetails.getGender());
	this.setName(candidacyExternalDetails.getName());
	this.setSocialSecurityNumber(candidacyExternalDetails.getSocialSecurityNumber());

	PhysicalAddressData physicalAddressData = new PhysicalAddressData(candidacyExternalDetails.getAddress(),
		candidacyExternalDetails.getAreaCode(), this.getDefaultPhysicalAddress().getAreaOfAreaCode(),
		candidacyExternalDetails.getArea(), this.getDefaultPhysicalAddress().getParishOfResidence(), this
			.getDefaultPhysicalAddress().getDistrictSubdivisionOfResidence(), this.getDefaultPhysicalAddress()
			.getDistrictOfResidence(), candidacyExternalDetails.getCountryOfResidence());
	setDefaultPhysicalAddressData(physicalAddressData);
	setDefaultPhoneNumber(candidacyExternalDetails.getTelephoneContact());
	setDefaultEmailAddressValue(candidacyExternalDetails.getEmail());

	return this;
    }

    public void editFromBean(PersonInformationFromUniqueCardDTO personDTO) throws ParseException {
	final String dateFormat = "dd MM yyyy";

	if (!StringUtils.isEmpty(personDTO.getName())) {
	    setName(personDTO.getName());
	}
	if (!StringUtils.isEmpty(personDTO.getGender())) {
	    setGender(personDTO.getGender().equalsIgnoreCase("m") ? Gender.MALE : Gender.FEMALE);
	}
	if (!StringUtils.isEmpty(personDTO.getDocumentIdEmissionLocation())) {
	    setEmissionLocationOfDocumentId(personDTO.getDocumentIdEmissionLocation());
	}
	if (!StringUtils.isEmpty(personDTO.getDocumentIdEmissionDate())) {
	    setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(DateFormatUtil.parse(dateFormat, personDTO
		    .getDocumentIdEmissionDate())));
	}
	if (!StringUtils.isEmpty(personDTO.getDocumentIdExpirationDate())) {
	    setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(DateFormatUtil.parse(dateFormat, personDTO
		    .getDocumentIdExpirationDate())));
	}
	if (!StringUtils.isEmpty(personDTO.getFiscalNumber())) {
	    setSocialSecurityNumber(personDTO.getFiscalNumber());
	}
	if (!StringUtils.isEmpty(personDTO.getBirthDate())) {
	    setDateOfBirthYearMonthDay(YearMonthDay.fromDateFields(DateFormatUtil.parse(dateFormat, personDTO.getBirthDate())));
	}
	if (!StringUtils.isEmpty(personDTO.getNationality())) {
	    setNationality(Country.readByThreeLetterCode(personDTO.getNationality()));
	}
	if (!StringUtils.isEmpty(personDTO.getMotherName())) {
	    setNameOfMother(personDTO.getMotherName());
	}
	if (!StringUtils.isEmpty(personDTO.getFatherName())) {
	    setNameOfFather(personDTO.getFatherName());
	}

	if (personDTO.getPhoto() != null) {
	    setPersonalPhoto(new Photograph(ContentType.JPG, new ByteArray(personDTO.getPhoto()), PhotoType.INSTITUTIONAL));
	}

	final PhysicalAddressData physicalAddress = new PhysicalAddressData();
	physicalAddress.setAddress(personDTO.getAddress());
	physicalAddress.setAreaCode(personDTO.getPostalCode());
	physicalAddress.setAreaOfAreaCode(personDTO.getPostalArea());
	physicalAddress.setArea(personDTO.getLocality());
	physicalAddress.setParishOfResidence(personDTO.getParish());
	physicalAddress.setDistrictSubdivisionOfResidence(personDTO.getMunicipality());
	physicalAddress.setDistrictOfResidence(personDTO.getDistrict());
	physicalAddress.setCountryOfResidence(Country.readByTwoLetterCode(personDTO.getCountry()));

	if (!physicalAddress.isEmpty()) {
	    setDefaultPhysicalAddressData(physicalAddress);
	}

    }

    public void edit(String name, String address, String phone, String mobile, String homepage, String email) {
	setName(name);
	setAddress(address);
	setDefaultPhoneNumber(phone);
	setDefaultMobilePhoneNumber(mobile);
	setDefaultEmailAddressValue(email);
	setDefaultWebAddressUrl(homepage);
    }

    public void editPersonalData(String documentIdNumber, IDDocumentType documentType, String personName,
	    String socialSecurityNumber) {
	setName(personName);
	setIdentification(documentIdNumber, documentType);
	setSocialSecurityNumber(socialSecurityNumber);
    }

    public void editPersonWithExternalData(final PersonBean personBean) {
	editPersonWithExternalData(personBean, false);
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
    @Deprecated
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

    public String getUserAliass() {
	return getUser().getAliass();
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

    public Login createLoginIdentificationAndUserIfNecessary() {
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

	if (newPassword.equals("")) {
	    throw new DomainException("error.person.invalidEmptyPassword");
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
	if (!hasPersonRoles(personRoles)) {
	    super.addPersonRoles(personRoles);
	}
    }

    @Service
    public void addPersonRoleByRoleTypeService(RoleType roleType) {
	this.addPersonRoleByRoleType(roleType);
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

    @Override
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

    public Boolean getIsExamCoordinatorInCurrentYear() {
	ExamCoordinator examCoordinator = this.getExamCoordinatorForGivenExecutionYear(ExecutionYear.readCurrentExecutionYear());
	return (examCoordinator == null) ? false : true;
    }

    public List<VigilantGroup> getVisibleVigilantGroups(ExecutionYear executionYear) {

	Set<VigilantGroup> groups = new HashSet<VigilantGroup>();

	Employee employee = this.getEmployee();
	if (employee != null) {
	    Department department = employee.getLastDepartmentWorkingPlace(executionYear.getBeginDateYearMonthDay(),
		    executionYear.getEndDateYearMonthDay());
	    if (department != null) {
		groups.addAll(department.getVigilantGroupsForGivenExecutionYear(executionYear));
	    }
	} else {
	    for (VigilantWrapper vigilantWrapper : this.getVigilantWrapperForExecutionYear(executionYear)) {
		groups.add(vigilantWrapper.getVigilantGroup());
	    }
	}

	return new ArrayList<VigilantGroup>(groups);
    }

    public List<VigilantWrapper> getVigilantWrapperForExecutionYear(ExecutionYear executionYear) {
	List<VigilantWrapper> wrappers = new ArrayList<VigilantWrapper>();
	for (VigilantWrapper wrapper : getVigilantWrappers()) {

	    if (wrapper.getExecutionYear() == executionYear) {
		wrappers.add(wrapper);
	    }
	}

	return wrappers;
    }

    public List<VigilantGroup> getVigilantGroupsForExecutionYear(ExecutionYear executionYear) {
	List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
	for (VigilantWrapper wrapper : getVigilantWrappers()) {
	    VigilantGroup group = wrapper.getVigilantGroup();
	    if (group.getExecutionYear().equals(executionYear)) {
		groups.add(group);
	    }
	}
	return groups;
    }

    public boolean isAllowedToSpecifyUnavailablePeriod() {
	DateTime currentDate = new DateTime();
	List<VigilantGroup> groupsForYear = getVigilantGroupsForExecutionYear(ExecutionYear.readCurrentExecutionYear());
	for (VigilantGroup group : groupsForYear) {
	    if (group.canSpecifyUnavailablePeriodIn(currentDate)) {
		return true;
	    }
	}
	return false;
    }

    public List<Vigilancy> getVigilanciesForYear(ExecutionYear executionYear) {
	List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
	for (VigilantWrapper vigilantWrapper : this.getVigilantWrappers()) {
	    if (vigilantWrapper.getExecutionYear().equals(executionYear)) {
		vigilancies.addAll(vigilantWrapper.getVigilancies());
	    }
	}
	return vigilancies;
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

    public double getVigilancyPointsForGivenYear(ExecutionYear executionYear) {
	List<VigilantWrapper> vigilants = this.getVigilantWrapperForExecutionYear(executionYear);
	if (vigilants.isEmpty())
	    return 0;
	else {
	    double points = 0;
	    for (VigilantWrapper vigilant : vigilants) {
		points += vigilant.getPoints();
	    }
	    return points;
	}
    }

    public double getTotalVigilancyPoints() {
	List<VigilantWrapper> vigilants = this.getVigilantWrappers();

	double points = 0;
	for (VigilantWrapper vigilant : vigilants) {
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

	setDefaultPhysicalAddressData(infoPerson.getPhysicalAddressData());
	setDefaultWebAddressUrl(infoPerson.getEnderecoWeb());
	setDefaultPhoneNumber(infoPerson.getTelefone());
	setDefaultMobilePhoneNumber(infoPerson.getTelemovel());
	setDefaultEmailAddressValue(infoPerson.getEmail());

	setWorkPhoneNumber(infoPerson.getWorkPhone());

	setDistrictSubdivisionOfBirth(infoPerson.getConcelhoNaturalidade());
	if (infoPerson.getDataEmissaoDocumentoIdentificacao() != null) {
	    setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay
		    .fromDateFields(infoPerson.getDataEmissaoDocumentoIdentificacao()));
	}
	if (infoPerson.getDataValidadeDocumentoIdentificacao() != null) {
	    setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(infoPerson
		    .getDataValidadeDocumentoIdentificacao()));
	}
	setDistrictOfBirth(infoPerson.getDistritoNaturalidade());

	setMaritalStatus((infoPerson.getMaritalStatus() == null) ? MaritalStatus.UNKNOWN : infoPerson.getMaritalStatus());
	setParishOfBirth(infoPerson.getFreguesiaNaturalidade());
	setEmissionLocationOfDocumentId(infoPerson.getLocalEmissaoDocumentoIdentificacao());

	if (infoPerson.getNascimento() != null) {
	    setDateOfBirthYearMonthDay(YearMonthDay.fromDateFields(infoPerson.getNascimento()));
	}
	setNameOfMother(infoPerson.getNomeMae());
	setNameOfFather(infoPerson.getNomePai());
	setSocialSecurityNumber(infoPerson.getNumContribuinte());

	setProfession(infoPerson.getProfissao());
	setGender(infoPerson.getSexo());

	setAvailableEmail(infoPerson.getAvailableEmail() != null ? infoPerson.getAvailableEmail() : Boolean.TRUE);
	setAvailableWebSite(infoPerson.getAvailableWebSite() != null ? infoPerson.getAvailableWebSite() : Boolean.TRUE);
	setAvailablePhoto(Boolean.TRUE);
    }

    private void updateProperties(InfoPersonEditor infoPerson) {
	setName(valueToUpdateIfNewNotNull(getName(), infoPerson.getNome()));
	setIdentification(valueToUpdateIfNewNotNull(getDocumentIdNumber(), infoPerson.getNumeroDocumentoIdentificacao()),
		(IDDocumentType) valueToUpdateIfNewNotNull(getIdDocumentType(), infoPerson.getTipoDocumentoIdentificacao()));

	setFiscalCode(valueToUpdateIfNewNotNull(getFiscalCode(), infoPerson.getCodigoFiscal()));

	setEmissionDateOfDocumentIdYearMonthDay(infoPerson.getDataEmissaoDocumentoIdentificacao() != null ? YearMonthDay
		.fromDateFields(infoPerson.getDataEmissaoDocumentoIdentificacao()) : getEmissionDateOfDocumentIdYearMonthDay());
	setEmissionLocationOfDocumentId(valueToUpdateIfNewNotNull(getEmissionLocationOfDocumentId(), infoPerson
		.getLocalEmissaoDocumentoIdentificacao()));
	setExpirationDateOfDocumentIdYearMonthDay(infoPerson.getDataValidadeDocumentoIdentificacao() != null ? YearMonthDay
		.fromDateFields(infoPerson.getDataValidadeDocumentoIdentificacao()) : getExpirationDateOfDocumentIdYearMonthDay());

	MaritalStatus maritalStatus = (MaritalStatus) valueToUpdateIfNewNotNull(getMaritalStatus(), infoPerson.getMaritalStatus());
	setMaritalStatus((maritalStatus == null) ? MaritalStatus.UNKNOWN : maritalStatus);

	setDateOfBirthYearMonthDay(infoPerson.getNascimento() != null ? YearMonthDay.fromDateFields(infoPerson.getNascimento())
		: getDateOfBirthYearMonthDay());
	setParishOfBirth(valueToUpdateIfNewNotNull(getParishOfBirth(), infoPerson.getFreguesiaNaturalidade()));
	setDistrictSubdivisionOfBirth(valueToUpdateIfNewNotNull(getDistrictSubdivisionOfBirth(), infoPerson
		.getConcelhoNaturalidade()));
	setDistrictOfBirth(valueToUpdateIfNewNotNull(getDistrictOfBirth(), infoPerson.getDistritoNaturalidade()));

	setNameOfMother(valueToUpdateIfNewNotNull(getNameOfMother(), infoPerson.getNomeMae()));
	setNameOfFather(valueToUpdateIfNewNotNull(getNameOfFather(), infoPerson.getNomePai()));
	setSocialSecurityNumber(valueToUpdateIfNewNotNull(getSocialSecurityNumber(), infoPerson.getNumContribuinte()));
	setProfession(valueToUpdateIfNewNotNull(getProfession(), infoPerson.getProfissao()));
	setGender((Gender) valueToUpdateIfNewNotNull(getGender(), infoPerson.getSexo()));

	PhysicalAddressData data = new PhysicalAddressData();
	data.setAddress(valueToUpdateIfNewNotNull(getAddress(), infoPerson.getMorada()));
	data.setAreaCode(valueToUpdateIfNewNotNull(getAreaCode(), infoPerson.getCodigoPostal()));
	data.setAreaOfAreaCode(valueToUpdateIfNewNotNull(getAreaOfAreaCode(), infoPerson.getLocalidadeCodigoPostal()));
	data.setArea(valueToUpdateIfNewNotNull(getArea(), infoPerson.getLocalidade()));
	data.setParishOfResidence(valueToUpdateIfNewNotNull(getParishOfResidence(), infoPerson.getFreguesiaMorada()));
	data.setDistrictSubdivisionOfResidence(valueToUpdateIfNewNotNull(getDistrictSubdivisionOfResidence(), infoPerson
		.getConcelhoMorada()));
	data.setDistrictOfResidence(valueToUpdateIfNewNotNull(getDistrictOfResidence(), infoPerson.getDistritoMorada()));
	data.setCountryOfResidence(getCountryOfResidence());
	setDefaultPhysicalAddressData(data);

	if (!hasAnyPartyContact(Phone.class)) {
	    Phone.createPhone(this, infoPerson.getTelefone(), PartyContactType.PERSONAL, true);
	    Phone.createPhone(this, infoPerson.getWorkPhone(), PartyContactType.WORK, true);
	}
	if (!hasAnyPartyContact(MobilePhone.class)) {
	    MobilePhone.createMobilePhone(this, infoPerson.getTelemovel(), PartyContactType.PERSONAL, false);
	}
	if (!hasAnyPartyContact(EmailAddress.class) && EmailSender.emailAddressFormatIsValid(infoPerson.getEmail())) {
	    EmailAddress.createEmailAddress(this, infoPerson.getEmail(), PartyContactType.PERSONAL, false);
	}
	if (!hasAnyPartyContact(WebAddress.class) && !StringUtils.isEmpty(infoPerson.getEnderecoWeb())) {
	    WebAddress.createWebAddress(this, infoPerson.getEnderecoWeb(), PartyContactType.PERSONAL, false);
	}
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
	setGivenNames(personBean.getGivenNames());
	setFamilyNames(personBean.getFamilyNames());
	if ((!StringUtils.isEmpty(getGivenNames()) || !StringUtils.isEmpty(getFamilyNames()))
		&& !getName().equals(getGivenNames() + " " + getFamilyNames())) {
	    throw new DomainException("error.person.splittedNamesDoNotMatch");
	}
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

	setEidentifier(personBean.getEidentifier());
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

    @Service
    public void removeRoleByTypeService(final RoleType roleType) {
	removeRoleByType(roleType);
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

    public Collection<PersonFunction> getAllActivePersonFunctions(FunctionType functionType) {
	Set<PersonFunction> personFunctions = new HashSet<PersonFunction>();
	for (PersonFunction personFunction : getActivePersonFunctions()) {
	    if (personFunction.getFunction().isOfFunctionType(functionType)) {
		personFunctions.add(personFunction);
	    }
	}
	return personFunctions;
    }

    public Collection<PersonFunction> getPersonFunctions() {
	return (Collection<PersonFunction>) getParentAccountabilities(AccountabilityTypeEnum.MANAGEMENT_FUNCTION,
		PersonFunction.class);
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
	return getPersonFuntions(AccountabilityTypeEnum.MANAGEMENT_FUNCTION, begin, end);
    }

    public List<PersonFunction> getPersonFunctions(Unit unit, boolean includeSubUnits, Boolean active, Boolean virtual) {
	return getPersonFunctions(unit, includeSubUnits, active, virtual, AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
    }

    public boolean hasActivePersonFunction(FunctionType functionType, Unit unit) {
	YearMonthDay currentDate = new YearMonthDay();
	for (PersonFunction personFunction : (Collection<PersonFunction>) getParentAccountabilities(
		AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)) {
	    if (personFunction.getUnit().equals(unit) && personFunction.getFunction().getFunctionType() == functionType
		    && personFunction.isActive(currentDate)) {
		return true;
	    }
	}
	return false;
    }

    public Collection<PersonFunction> getPersonFunctions(AccountabilityTypeEnum accountabilityTypeEnum) {
	return (Collection<PersonFunction>) getParentAccountabilities(accountabilityTypeEnum, PersonFunction.class);
    }

    public List<PersonFunction> getPersonFuntions(AccountabilityTypeEnum accountabilityTypeEnum, YearMonthDay begin,
	    YearMonthDay end) {
	List<PersonFunction> result = new ArrayList<PersonFunction>();
	for (Accountability accountability : (Collection<PersonFunction>) getParentAccountabilities(accountabilityTypeEnum,
		PersonFunction.class)) {
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
     * @param unit
     *            filter all PersonFunctions to this unit, or <code>null</code>
     *            for all PersonFunctions
     * @param includeSubUnits
     *            if even subunits of the given unit are considered
     * @param active
     *            the state of the function, <code>null</code> for all
     *            PersonFunctions
     */
    public List<PersonFunction> getPersonFunctions(Unit unit, boolean includeSubUnits, Boolean active, Boolean virtual,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	List<PersonFunction> result = new ArrayList<PersonFunction>();

	Collection<Unit> allSubUnits = Collections.emptyList();
	if (includeSubUnits) {
	    allSubUnits = unit.getAllSubUnits();
	}

	YearMonthDay today = new YearMonthDay();

	for (PersonFunction personFunction : getPersonFunctions(accountabilityTypeEnum)) {
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

    public List<PersonFunction> getPersonFunctions(Party party, boolean includeSubUnits, Boolean active, Boolean virtual,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	if (party.isUnit()) {
	    return getPersonFunctions((Unit) party, includeSubUnits, active, virtual, AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
	}
	List<PersonFunction> result = new ArrayList<PersonFunction>();

	YearMonthDay today = new YearMonthDay();
	for (PersonFunction personFunction : getPersonFunctions(accountabilityTypeEnum)) {
	    if (active != null && (personFunction.isActive(today) == !active)) {
		continue;
	    }
	    if (virtual != null && (personFunction.getFunction().isVirtual() == !virtual)) {
		continue;
	    }
	    if (personFunction.getParentParty().isPerson()) {
		Person functionPerson = (Person) personFunction.getParentParty();
		if (party == null || functionPerson.equals(party)) {
		    result.add(personFunction);
		}
	    }
	}

	return result;
    }

    public boolean hasFunctionType(FunctionType functionType, AccountabilityTypeEnum accountabilityTypeEnum) {
	for (PersonFunction accountability : getPersonFunctions(null, false, true, false, accountabilityTypeEnum)) {
	    if (accountability.getFunction().getFunctionType() == functionType) {
		return true;
	    }
	}
	return false;
    }

    public PersonFunction addPersonFunction(Function function, YearMonthDay begin, YearMonthDay end, Double credits) {
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
    @Override
    public void delete() {

	if (!canBeDeleted()) {
	    throw new DomainException("error.person.cannot.be.deleted");
	}
	if (getPersonalPhotoEvenIfRejected() != null) {
	    getPersonalPhotoEvenIfRejected().delete();
	}
	if (hasParkingParty()) {
	    getParkingParty().delete();
	}
	if (hasAssociatedPersonAccount()) {
	    getAssociatedPersonAccount().delete();
	}
	if (hasHomepage()) {
	    getHomepage().delete();
	}

	getPersonRoles().clear();
	if (hasUser()) {
	    getUser().delete();
	}

	getPersonRoleOperationLog().clear();
	getGivenRoleOperationLog().clear();

	if (hasStudent()) {
	    getStudent().delete();
	}
	if (hasPersonName()) {
	    getPersonName().delete();
	}

	getBookmarkedBoards().clear();
	getManageableDepartmentCredits().clear();
	getThesisEvaluationParticipants().clear();

	for (; !getIdDocumentsSet().isEmpty(); getIdDocumentsSet().iterator().next().delete())
	    ;
	for (; !getScientificCommissions().isEmpty(); getScientificCommissions().iterator().next().delete())
	    ;

	removeNationality();
	removeCountryOfBirth();

	if (hasResearcher()) {
	    getResearcher().delete();
	}

	super.delete();
    }

    private boolean canBeDeleted() {
	return !hasAnyChilds() && !hasAnyParents() && !hasAnyDomainObjectActionLogs() && !hasAnyExportGroupingReceivers()
		&& !hasAnyPersistentGroups() && !hasAnyPersonSpaceOccupations() && !hasAnyPunctualRoomsOccupationComments()
		&& !hasAnyVehicleAllocations() && !hasAnyPunctualRoomsOccupationRequests()
		&& !hasAnyPunctualRoomsOccupationRequestsToProcess() && !hasAnyAssociatedQualifications()
		&& !hasAnyAssociatedAlteredCurriculums() && !hasAnyEnrolmentEvaluations() && !hasAnyExportGroupingSenders()
		&& !hasAnyResponsabilityTransactions() && !hasAnyMasterDegreeCandidates() && !hasAnyGuides()
		&& !hasAnyProjectAccesses() && !hasEmployee() && !hasTeacher() && !hasGrantOwner() && !hasAnyPayedGuides()
		&& !hasAnyPayedReceipts() && !hasParking() && !hasAnyResearchInterests() && !hasAnyProjectParticipations()
		&& !hasAnyParticipations() && !hasAnyBoards() && !hasAnyPersonFunctions()
		&& (!hasHomepage() || getHomepage().isDeletable()) && !hasLibraryCard() && !hasAnyAcademicServiceRequests()
		&& !hasAnyCardGenerationEntries() && !hasAnyInternalParticipants() && !hasAnyCreatedQualifications()
		&& !hasAnyCreateJobs();
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
	    if (Boolean.TRUE.equals(contract.getExternalContract())) {
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
	    if (newRole != null && person != null && !person.hasPersonRoles(newRole)) {
		addRoleOperationLog(person, newRole, RoleOperationType.ADD);
	    }
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
	    if (person != null && roleToBeRemoved != null && person.hasRole(roleToBeRemoved.getRoleType())) {
		removeDependencies(person, roleToBeRemoved);
		addRoleOperationLog(person, roleToBeRemoved, RoleOperationType.REMOVE);
	    }
	}

	@Override
	public void afterRemove(Role removedRole, Person person) {
	    if (person != null && removedRole != null) {
		person.removeAlias(removedRole);
		person.updateIstUsername();
	    }
	}

	private void addRoleOperationLog(Person person, Role role, RoleOperationType operationType) {
	    Person whoGranted = AccessControl.getPerson();
	    new RoleOperationLog(role, person, whoGranted, operationType);
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
		addRoleIfNotPresent(person, RoleType.EMPLOYEE);
		break;

	    case RESEARCHER:
		addRoleIfNotPresent(person, RoleType.PERSON);
		new Researcher(person);
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
		// removeRoleIfPresent(person, RoleType.PROJECTS_MANAGER);
		// removeRoleIfPresent(person,
		// RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
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

    public MasterDegreeCandidate getMasterDegreeCandidateByExecutionDegree(final ExecutionDegree executionDegree) {
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
	    if (candidacy instanceof DegreeCandidacy && candidacy.isActive()) {
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
	    if (candidacy instanceof StudentCandidacy && candidacy.isActive()) {
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

    public Collection<Invitation> getInvitationsOrderByDate() {
	Set<Invitation> invitations = new TreeSet<Invitation>(Invitation.CONTRACT_COMPARATOR_BY_BEGIN_DATE);
	invitations
		.addAll((Collection<Invitation>) getParentAccountabilities(AccountabilityTypeEnum.INVITATION, Invitation.class));
	return invitations;
    }

    public List<Invitation> getActiveInvitations() {
	YearMonthDay today = new YearMonthDay();
	List<Invitation> invitations = new ArrayList<Invitation>();
	for (Accountability accoutAccountability : getParentAccountabilities(AccountabilityTypeEnum.INVITATION, Invitation.class)) {
	    if (((Invitation) accoutAccountability).isActive(today)) {
		invitations.add((Invitation) accoutAccountability);
	    }
	}
	return invitations;
    }

    public boolean isInvited(YearMonthDay date) {
	for (Invitation invitation : (Collection<Invitation>) getParentAccountabilities(AccountabilityTypeEnum.INVITATION,
		Invitation.class)) {
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

    public static Person readByDocumentIdNumberAndDateOfBirth(final String documentIdNumber, final YearMonthDay dateOfBirth) {
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
    public static List<Person> readPersonsByName(final String name, final Integer startIndex, final Integer numberOfElementsInSpan) {
	final Collection<Person> personsList = readPersonsByName(name, Integer.MAX_VALUE);
	if (startIndex != null && numberOfElementsInSpan != null && !personsList.isEmpty()) {
	    int finalIndex = Math.min(personsList.size(), startIndex + numberOfElementsInSpan);
	    final List<Person> result = new ArrayList<Person>(finalIndex - startIndex);
	    final Iterator<Person> iter = personsList.iterator();
	    for (int i = 0; i <= finalIndex && iter.hasNext(); i++) {
		final Person person = iter.next();
		if (i >= startIndex) {
		    result.add(person);
		}
	    }
	    return result;
	}
	return Collections.EMPTY_LIST;
    }

    public static Integer countAllByName(final String name) {
	return readPersonsByName(name, Integer.MAX_VALUE).size();
    }

    public static Collection<Person> readPersonsByName(final String name, final int size) {
	return findPerson(name.replace('%', ' '), size);
    }

    public static Collection<Person> findPerson(final String name, final int size) {
	final Collection<Person> people = new ArrayList<Person>();
	for (final PersonName personName : PersonName.findPerson(name, size)) {
	    people.add(personName.getPerson());
	}
	return people;
    }

    public static Collection<Person> readPersonsByName(final String name) {
	return findPerson(name.replace('%', ' '));
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

    public static List<Person> readPersonsByRoleType(RoleType roleType) {
	return new ArrayList<Person>(Role.getRoleByRoleType(roleType).getAssociatedPersonsSet());
    }

    public static Collection<Person> readPersonsByNameAndRoleType(final String name, RoleType roleType) {
	final Collection<Person> people = findPerson(name);
	for (final Iterator<Person> iter = people.iterator(); iter.hasNext();) {
	    final Person person = iter.next();
	    if (!person.hasRole(roleType)) {
		iter.remove();
	    }
	}
	return people;
    }

    public SortedSet<StudentCurricularPlan> getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName() {
	final SortedSet<StudentCurricularPlan> studentCurricularPlans = new TreeSet<StudentCurricularPlan>(
		StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME);
	for (final Registration registration : getStudentsSet()) {
	    final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
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
		StudentCurricularPlan lastStudent = registration.getLastStudentCurricularPlan();
		if (lastStudent != null) {
		    studentCurricularPlans.add(lastStudent);
		}
	    }
	}
	return studentCurricularPlans;
    }

    public List<ProjectAccess> readProjectAccessesByCoordinator(final Integer coordinatorCode, Boolean it) {
	final List<ProjectAccess> result = new ArrayList<ProjectAccess>();

	for (final ProjectAccess projectAccess : getProjectAccessesSet()) {
	    if (projectAccess.getKeyProjectCoordinator().equals(coordinatorCode) && projectAccess.getItProject().equals(it)) {
		if (!projectAccess.getProjectAccessInterval().containsNow()) {
		    continue;
		}
		result.add(projectAccess);
	    }
	}
	return result;
    }

    public Set<Attends> getCurrentAttends() {
	final Set<Attends> attends = new HashSet<Attends>();
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

    public Set<Attends> getCurrentAttendsPlusSpecialSeason() {
	final Set<Attends> attends = new HashSet<Attends>();
	for (final Registration registration : getStudentsSet()) {
	    for (final Attends attend : registration.getAssociatedAttendsSet()) {
		final ExecutionCourse executionCourse = attend.getExecutionCourse();
		final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
		if (executionSemester.getState().equals(PeriodState.CURRENT)) {
		    attends.add(attend);
		} else if (attend.getEnrolment() != null && attend.getEnrolment().isSpecialSeason()) {
		    if (executionSemester.getNextExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
			attends.add(attend);
		    }
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
	    return getIstUsername() != null;
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

	@Override
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
	if (findPersonFactory.getInstitutionalNumber() != null) {

	    Teacher teacher = Teacher.readByNumber(findPersonFactory.getInstitutionalNumber());
	    if (teacher != null) {
		people.add(teacher.getPerson());
	    }

	    Employee employee = Employee.readByNumber(findPersonFactory.getInstitutionalNumber());
	    if (employee != null) {
		people.add(employee.getPerson());
	    }

	    for (Registration registration : Registration.readByNumber(findPersonFactory.getInstitutionalNumber())) {
		people.add(registration.getPerson());
	    }

	}
	return people;
    }

    private Set<Event> getEventsFromType(Class<? extends Event> clazz) {
	Set<Event> events = new HashSet<Event>();

	for (Event event : getEventsSet()) {
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

    public Set<Event> getNotPayedEventsPayableOn(AdministrativeOffice administrativeOffice, Class eventClass,
	    boolean withInstallments) {
	final Set<Event> result = new HashSet<Event>();

	for (final Event event : getEventsFromType(eventClass)) {
	    if (event.isOpen() && event.hasInstallments() == withInstallments
		    && isPayableOnAdministrativeOffice(administrativeOffice, event)) {
		result.add(event);
	    }
	}

	return result;
    }

    public Set<Event> getNotPayedEventsPayableOn(AdministrativeOffice administrativeOffice, boolean withInstallments) {
	return getNotPayedEventsPayableOn(administrativeOffice, AcademicEvent.class, withInstallments);
    }

    public Set<Event> getNotPayedEventsPayableOn(AdministrativeOffice administrativeOffice) {
	final Set<Event> result = new HashSet<Event>();
	for (final Event event : getAcademicEvents()) {
	    if (event.isOpen() && isPayableOnAdministrativeOffice(administrativeOffice, event)) {
		result.add(event);
	    }
	}

	return result;
    }

    private boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice, final Event event) {
	return ((administrativeOffice == null) || (event.isPayableOnAdministrativeOffice(administrativeOffice)));
    }

    public List<Event> getPayedEvents(Class eventClass) {
	final List<Event> result = new ArrayList<Event>();
	for (final Event event : getEventsFromType(eventClass)) {
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
	final List<Event> result = new ArrayList<Event>();
	for (final Event event : getAcademicEvents()) {
	    if (!event.isCancelled() && event.hasAnyPayments()) {
		result.add(event);
	    }
	}

	return result;
    }

    public Set<Entry> getPaymentsWithoutReceipt() {
	return getPaymentsWithoutReceiptByAdministrativeOffice(null);
    }

    public Set<Entry> getPaymentsWithoutReceiptByAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	final Set<Entry> result = new HashSet<Entry>();

	for (final Event event : getAcademicEvents()) {
	    if (!event.isCancelled() && isPayableOnAdministrativeOffice(administrativeOffice, event)) {
		result.addAll(event.getEntriesWithoutReceipt());
	    }
	}

	return result;
    }

    public Set<Entry> getPayments(Class eventClass) {
	final Set<Entry> result = new HashSet<Entry>();
	for (final Event event : getEventsFromType(eventClass)) {
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

    public Set<? extends Event> getEventsByEventTypes(final EventType... eventTypes) {
	return getEventsByEventTypes(Arrays.asList(eventTypes));
    }

    public Set<? extends Event> getEventsByEventTypes(final Collection<EventType> eventTypes) {

	final Set<Event> result = new HashSet<Event>();

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

    public Set<? extends Event> getEventsByEventTypeAndClass(final EventType eventType, final Class clazz) {
	final Set<Event> result = new HashSet<Event>();

	for (final Event event : getAcademicEvents()) {
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

    public boolean hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(final ExecutionYear executionYear) {
	return hasInsuranceEventFor(executionYear) || hasAdministrativeOfficeFeeInsuranceEventFor(executionYear);
    }

    public Set<InsuranceEvent> getNotCancelledInsuranceEvents() {
	final Set<InsuranceEvent> result = new HashSet<InsuranceEvent>();

	for (final Event event : getEventsByEventType(EventType.INSURANCE)) {
	    final InsuranceEvent specificEvent = (InsuranceEvent) event;
	    if (!specificEvent.isCancelled()) {
		result.add(specificEvent);
	    }
	}

	return result;
    }

    public Set<InsuranceEvent> getNotCancelledInsuranceEventsUntil(final ExecutionYear executionYear) {
	final Set<InsuranceEvent> result = new HashSet<InsuranceEvent>();

	for (final Event event : getEventsByEventType(EventType.INSURANCE)) {
	    final InsuranceEvent specificEvent = (InsuranceEvent) event;
	    if (!specificEvent.isCancelled() && specificEvent.getExecutionYear().isBeforeOrEquals(executionYear)) {
		result.add(specificEvent);
	    }
	}

	return result;
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

    public Set<AdministrativeOfficeFeeAndInsuranceEvent> getNotCancelledAdministrativeOfficeFeeAndInsuranceEvents(
	    final AdministrativeOffice office) {
	final Set<AdministrativeOfficeFeeAndInsuranceEvent> result = new HashSet<AdministrativeOfficeFeeAndInsuranceEvent>();

	for (final Event event : getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE)) {
	    final AdministrativeOfficeFeeAndInsuranceEvent specificEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
	    if (!specificEvent.isCancelled() && specificEvent.getAdministrativeOffice() == office) {
		result.add(specificEvent);
	    }
	}

	return result;
    }

    public Set<AdministrativeOfficeFeeAndInsuranceEvent> getNotCancelledAdministrativeOfficeFeeAndInsuranceEventsUntil(
	    final AdministrativeOffice office, final ExecutionYear executionYear) {
	final Set<AdministrativeOfficeFeeAndInsuranceEvent> result = new HashSet<AdministrativeOfficeFeeAndInsuranceEvent>();

	for (final Event event : getEventsByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE)) {
	    final AdministrativeOfficeFeeAndInsuranceEvent specificEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
	    if (!specificEvent.isCancelled() && specificEvent.getAdministrativeOffice() == office
		    && specificEvent.getExecutionYear().isBeforeOrEquals(executionYear)) {
		result.add(specificEvent);
	    }
	}

	return result;
    }

    public AdministrativeOfficeFeeAndInsuranceEvent getAdministrativeOfficeFeeInsuranceEventFor(final ExecutionYear executionYear) {
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

    public Set<GratuityEvent> getGratuityEvents() {
	return (Set<GratuityEvent>) getEventsByEventTypes(EventType.getGratuityEventTypes());
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

	Person externalPerson = createExternalPerson(contributorName, Gender.MALE, data, null, null, null, null, String
		.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);
	externalPerson.setSocialSecurityNumber(contributorNumber);

	new ExternalContract(externalPerson, RootDomainObject.getInstance().getExternalInstitutionUnit(), new YearMonthDay(),
		null);

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
	    if (professorship.getExecutionCourse().getExecutionPeriod() == ExecutionSemester.readActualExecutionSemester()) {
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
		if (attends.getExecutionCourse().isLecturedIn(ExecutionSemester.readActualExecutionSemester())) {
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
		    && !teacher.isMonitor(ExecutionSemester.readActualExecutionSemester())) {
		return PartyClassification.TEACHER;
	    }
	}
	final Employee employee = getEmployee();
	if (employee != null && employee.getAssiduousness() != null && employee.getAssiduousness().getCurrentStatus() != null
		&& employee.getAssiduousness().getCurrentStatus().getState() == AssiduousnessState.ACTIVE) {
	    return PartyClassification.EMPLOYEE;
	}
	final GrantOwner grantOwner = getGrantOwner();
	if (grantOwner != null && grantOwner.hasCurrentContract()) {
	    return PartyClassification.GRANT_OWNER;
	}
	if (isPersonResearcher() && employee != null) {
	    return PartyClassification.RESEARCHER;
	}
	final Student student = getStudent();
	if (student != null) {
	    final DegreeType degreeType = student.getMostSignificantDegreeType();
	    if (degreeType != null) {
		if (hasActiveDegree(student, degreeType)) {
		    return PartyClassification.getClassificationByDegreeType(degreeType);
		}
	    }
	}
	return PartyClassification.PERSON;
    }

    private boolean hasActiveDegree(Student student, DegreeType degreeType) {
	for (final Registration registration : student.getRegistrationsSet()) {
	    if (registration.getDegreeType() == degreeType) {
		for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		    final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
		    if (degreeCurricularPlan.getExecutionDegreeByYear(ExecutionYear.readCurrentExecutionYear()) != null) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public Set<Career> getCareersByType(CareerType type) {
	return getCareersByTypeAndInterval(type, null);
    }

    public Set<Career> getCareersByTypeAndInterval(CareerType type, Interval intersecting) {
	Set<Career> careers = new HashSet<Career>();
	for (Career career : getAssociatedCareersSet()) {
	    if (type == null || (type.equals(CareerType.PROFESSIONAL) && career instanceof ProfessionalCareer)
		    || (type.equals(CareerType.TEACHING) && career instanceof TeachingCareer)) {
		if (intersecting == null || career.getInterval().overlaps(intersecting)) {
		    careers.add(career);
		}
	    }
	}
	return careers;
    }

    public static class PersonBeanFactoryEditor extends PersonBean implements FactoryExecutor {
	public PersonBeanFactoryEditor(final Person person) {
	    super(person);
	}

	@Override
	public Object execute() {
	    getPerson().edit(this);
	    return null;
	}
    }

    public static class ExternalPersonBeanFactoryCreator extends ExternalPersonBean implements FactoryExecutor {
	public ExternalPersonBeanFactoryCreator() {
	    super();
	}

	@Override
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

	private boolean matchesAnyCriteriaField(final String[] nameValues, final String string, final String stringFromPerson) {
	    return isSpecified(string) && areNamesPresent(stringFromPerson, nameValues);
	}

	public SortedSet<Person> search() {
	    final SortedSet<Person> people = new TreeSet<Person>(Party.COMPARATOR_BY_NAME_AND_ID);
	    if (isSpecified(name)) {
		people.addAll(findPerson(name));
	    }
	    if (isSpecified(documentIdNumber)) {
		for (final IdDocument idDocument : RootDomainObject.getInstance().getIdDocumentsSet()) {
		    final String[] documentIdNumberValues = documentIdNumber == null ? null : StringNormalizer.normalize(
			    documentIdNumber).toLowerCase().split("\\p{Space}+");
		    if (matchesAnyCriteriaField(documentIdNumberValues, documentIdNumber, idDocument.getValue())) {
			people.add(idDocument.getPerson());
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
	    final String normalizedName = StringNormalizer.normalize(name.replace('-', ' ')).toLowerCase();
	    final String normalizedPersonName = StringNormalizer.normalize(getName().replace('-', ' ')).toLowerCase();

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
	    if (string.length() == xpto.length() && string.hashCode() == xpto.hashCode() && string.equals(xpto)) {
		return true;
	    }
	}
	return false;
    }

    public String getHomepageWebAddress() {
	if (hasHomepage() && getHomepage().isHomepageActivated())
	    return "/homepage/" + getUsername();
	if (isDefaultWebAddressVisible() && getDefaultWebAddress().hasUrl())
	    return getDefaultWebAddress().getUrl();
	return null;
    }

    @Deprecated
    public boolean hasAvailableWebSite() {
	return getAvailableWebSite() != null && getAvailableWebSite().booleanValue();
    }

    public Collection<ExecutionDegree> getCoordinatedExecutionDegrees(DegreeCurricularPlan degreeCurricularPlan) {
	Set<ExecutionDegree> result = new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
	for (Coordinator coordinator : getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
		result.add(coordinator.getExecutionDegree());
	    }
	}
	return result;
    }

    public boolean isCoordinatorFor(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear) {
	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionYear) {
		return executionDegree.getCoordinatorByTeacher(this) != null;
	    }
	}
	return false;
    }

    public boolean isResponsibleOrCoordinatorFor(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	final Teacher teacher = getTeacher();
	return (teacher != null && teacher.isResponsibleFor(curricularCourse, executionSemester))
		|| isCoordinatorFor(curricularCourse.getDegreeCurricularPlan(), executionSemester.getExecutionYear());
    }

    private final static List<DegreeType> degreeTypesForIsMasterDegreeOrBolonhaMasterDegreeCoordinator = Arrays
	    .asList(new DegreeType[] { DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE });

    public boolean isMasterDegreeOrBolonhaMasterDegreeCoordinatorFor(ExecutionYear executionYear) {
	return isCoordinatorFor(executionYear, degreeTypesForIsMasterDegreeOrBolonhaMasterDegreeCoordinator);

    }

    private final static List<DegreeType> degreeTypesForisDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegreeCoordinatorFor = Arrays
	    .asList(new DegreeType[] { DegreeType.DEGREE, DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE });

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegreeCoordinatorFor(ExecutionYear executionYear) {
	return isCoordinatorFor(executionYear, degreeTypesForisDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegreeCoordinatorFor);

    }

    public boolean isCoordinatorFor(ExecutionYear executionYear, List<DegreeType> degreeTypes) {
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

    public boolean hasServiceAgreementFor(final ServiceAgreementTemplate serviceAgreementTemplate) {
	return getServiceAgreementFor(serviceAgreementTemplate) != null;
    }

    public boolean isHomePageAvailable() {
	return hasHomepage() && getHomepage().getActivated();
    }

    public boolean isAdministrativeOfficeEmployee() {
	return getEmployee() != null && getEmployee().getAdministrativeOffice() != null;
    }

    public List<PunctualRoomsOccupationRequest> getPunctualRoomsOccupationRequestsOrderByMoreRecentComment() {
	List<PunctualRoomsOccupationRequest> result = new ArrayList<PunctualRoomsOccupationRequest>();
	result.addAll(getPunctualRoomsOccupationRequests());
	if (!result.isEmpty()) {
	    Collections.sort(result, PunctualRoomsOccupationRequest.COMPARATOR_BY_MORE_RECENT_COMMENT_INSTANT);
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
	    boolean teacher = false, employee = false, researcher = false;

	    List<Role> roles = new ArrayList<Role>(getPersonRolesSet());
	    Collections.sort(roles, Role.COMPARATOR_BY_ROLE_TYPE);

	    ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources");

	    for (final Role personRole : roles) {

		if (personRole.getRoleType() == RoleType.TEACHER) {
		    mainRoles.add(bundle.getString(personRole.getRoleType().toString()));
		    teacher = true;

		} else if (personRole.getRoleType() == RoleType.STUDENT) {
		    mainRoles.add(bundle.getString(personRole.getRoleType().toString()));

		} else if (personRole.getRoleType() == RoleType.GRANT_OWNER) {
		    mainRoles.add(bundle.getString(personRole.getRoleType().toString()));
		} else if (!teacher && personRole.getRoleType() == RoleType.EMPLOYEE) {
		    employee = true;
		} else if (personRole.getRoleType() == RoleType.RESEARCHER) {
		    mainRoles.add(bundle.getString(personRole.getRoleType().toString()));
		    researcher = true;
		} else if (personRole.getRoleType() == RoleType.ALUMNI) {
		    mainRoles.add(bundle.getString(personRole.getRoleType().toString()));
		}
	    }
	    if ((employee && !teacher && !researcher)) {
		mainRoles.add(0, bundle.getString(RoleType.EMPLOYEE.toString()));
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

    public static Collection<Person> findPerson(final String name) {
	final Collection<Person> people = new ArrayList<Person>();
	for (final PersonName personName : PersonName.findPerson(name, Integer.MAX_VALUE)) {
	    people.add(personName.getPerson());
	}
	return people;
    }

    public static Collection<Person> findInternalPerson(final String name) {
	final Collection<Person> people = new ArrayList<Person>();
	for (final PersonName personName : PersonName.findInternalPerson(name, Integer.MAX_VALUE)) {
	    people.add(personName.getPerson());
	}
	return people;
    }

    public static Collection<Person> findInternalPersonByNameAndRole(final String name, final RoleType roleType) {
	final Role role = Role.getRoleByRoleType(roleType);
	return CollectionUtils.select(findInternalPerson(name), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((Person) arg0).hasPersonRoles(role);
	    }

	});
    }

    public static Collection<Person> findInternalPersonMatchingFirstAndLastName(final String completeName) {
	if (completeName != null) {
	    String[] splittedName = completeName.split(" ");
	    return splittedName.length > 0 ? findInternalPerson(splittedName[0] + " " + splittedName[splittedName.length - 1])
		    : Collections.EMPTY_LIST;
	}
	return Collections.EMPTY_LIST;
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
	if (!StringUtils.isEmpty(documentIDValue)) {
	    for (final IdDocument idDocument : IdDocument.find(documentIDValue)) {
		people.add(idDocument.getPerson());
	    }
	}
	return people;
    }

    public static Person readPersonByEmailAddress(final String email) {
	final EmailAddress emailAddress = EmailAddress.find(email);
	return (emailAddress != null && emailAddress.getParty().isPerson()) ? (Person) emailAddress.getParty() : null;
    }

    public String getUnitText() {
	if (getEmployee() != null && getEmployee().getLastWorkingPlace() != null) {
	    return getEmployee().getLastWorkingPlace().getNameWithAcronym();
	} else if (hasExternalContract()) {
	    return getExternalContract().getInstitutionUnit().getPresentationNameWithParents();
	}
	return "";
    }

    public Set<Thesis> getOrientedOrCoorientedThesis(ExecutionYear year) {
	Set<Thesis> thesis = new HashSet<Thesis>();
	for (ThesisEvaluationParticipant participant : getThesisEvaluationParticipants()) {
	    if (participant.getThesis().getEnrolment().getExecutionYear().equals(year)
		    && (participant.getType() == ThesisParticipationType.ORIENTATOR || participant.getType() == ThesisParticipationType.COORIENTATOR)) {
		thesis.add(participant.getThesis());
	    }
	}
	return thesis;
    }

    public List<ThesisEvaluationParticipant> getThesisEvaluationParticipants(ExecutionSemester executionSemester) {
	ArrayList<ThesisEvaluationParticipant> participants = new ArrayList<ThesisEvaluationParticipant>();

	for (ThesisEvaluationParticipant participant : this.getThesisEvaluationParticipants()) {
	    if (participant.getThesis().getEnrolment().getExecutionYear().equals(executionSemester.getExecutionYear())) {
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

    @Override
    public List<TSDProcess> getTSDProcesses() {
	Department department = hasTeacher() ? getTeacher().getCurrentWorkingDepartment() : null;
	return department == null ? Collections.EMPTY_LIST : (List<TSDProcess>) CollectionUtils.select(department
		.getTSDProcesses(), new Predicate() {
	    @Override
	    public boolean evaluate(Object arg0) {
		TSDProcess tsd = (TSDProcess) arg0;
		return tsd.hasAnyPermission(Person.this);
	    }
	});
    }

    public List<TSDProcess> getTSDProcesses(ExecutionSemester period) {
	Department department = hasTeacher() ? getTeacher().getCurrentWorkingDepartment() : null;
	return department == null ? Collections.EMPTY_LIST : (List<TSDProcess>) CollectionUtils.select(department
		.getTSDProcessesByExecutionPeriod(period), new Predicate() {
	    @Override
	    public boolean evaluate(Object arg0) {
		TSDProcess tsd = (TSDProcess) arg0;
		return tsd.hasAnyPermission(Person.this);
	    }
	});
    }

    public List<TSDProcess> getTSDProcesses(ExecutionYear year) {
	Department department = hasTeacher() ? getTeacher().getCurrentWorkingDepartment() : null;
	return department == null ? Collections.EMPTY_LIST : (List<TSDProcess>) CollectionUtils.select(department
		.getTSDProcessesByExecutionYear(year), new Predicate() {
	    @Override
	    public boolean evaluate(Object arg0) {
		TSDProcess tsd = (TSDProcess) arg0;
		return tsd.hasAnyPermission(Person.this);
	    }
	});
    }

    public List<ResearchUnit> getWorkingResearchUnits() {
	List<ResearchUnit> units = new ArrayList<ResearchUnit>();
	Collection<? extends Accountability> parentAccountabilities = getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);

	YearMonthDay currentDate = new YearMonthDay();
	for (Accountability accountability : parentAccountabilities) {
	    if (accountability.isActive(currentDate)) {
		units.add((ResearchUnit) accountability.getParentParty());
	    }
	}

	return units;
    }

    public List<ResearchUnit> getWorkingResearchUnitsAndParents() {
	Set<ResearchUnit> baseUnits = new HashSet<ResearchUnit>();
	for (ResearchUnit unit : getWorkingResearchUnits()) {
	    baseUnits.add(unit);
	    for (Unit parentUnit : unit.getAllActiveParentUnits(new YearMonthDay())) {
		if (parentUnit.isResearchUnit()) {
		    baseUnits.add((ResearchUnit) parentUnit);
		}
	    }
	}
	return new ArrayList<ResearchUnit>(baseUnits);
    }

    public Set<Unit> getAssociatedResearchOrDepartmentUnits() {
	Set<Unit> units = new HashSet<Unit>();
	Set<Accountability> parentAccountabilities = new HashSet<Accountability>();

	parentAccountabilities.addAll(getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT));
	parentAccountabilities.addAll(getParentAccountabilities(AccountabilityTypeEnum.WORKING_CONTRACT));

	for (Accountability accountability : parentAccountabilities) {
	    Unit unit = getActiveAncestorUnitFromAccountability(accountability);
	    if (unit != null) {
		units.add(unit);
	    }
	}

	return units;
    }

    private Unit getActiveAncestorUnitFromAccountability(Accountability accountability) {
	YearMonthDay currentDate = new YearMonthDay();
	if (!accountability.isActive(currentDate)) {
	    return null;
	}

	Unit parentUnit = (Unit) accountability.getParentParty();
	if (isResearchDepartmentScientificOrSectionUnitType(parentUnit)) {
	    return parentUnit;
	}

	for (Unit grandParentUnit : parentUnit.getParentUnits()) {
	    if (isResearchDepartmentScientificOrSectionUnitType(grandParentUnit))
		return grandParentUnit;
	}

	return null;
    }

    private boolean isResearchDepartmentScientificOrSectionUnitType(Unit unit) {
	return unit.isResearchUnit() || unit.isDepartmentUnit() || unit.isScientificAreaUnit() || unit.isSectionUnit();
    }

    // FIXME Anil : This method is identical to getWorkingResearchUnitNames
    public String getAssociatedResearchOrDepartmentUnitsNames() {
	String names = "";
	Set<Unit> units = getAssociatedResearchOrDepartmentUnits();
	int length = units.size();
	for (Unit unit : units) {
	    names += unit.getName();
	    if (--length > 0) {
		names += ", ";
	    }
	}
	return names;
    }

    public String getWorkingResearchUnitNames() {

	String names = "";
	List<ResearchUnit> units = getWorkingResearchUnits();
	int length = units.size();
	for (ResearchUnit unit : units) {
	    names += unit.getName();
	    if (--length > 0) {
		names += ", ";
	    }
	}
	return names;
    }

    public boolean isExternalPerson() {
	return !hasActiveInternalContract() && (hasExternalContract() || hasExternalResearchContract());
    }

    private boolean hasActiveInternalContract() {
	Collection<EmployeeContract> contracts = (Collection<EmployeeContract>) getParentAccountabilities(
		AccountabilityTypeEnum.WORKING_CONTRACT, EmployeeContract.class);

	YearMonthDay currentDate = new YearMonthDay();
	for (EmployeeContract employeeContract : contracts) {
	    if (employeeContract.isActive(currentDate)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isPhotoAvailableToCurrentUser() {
	if (isPhotoPubliclyAvailable()) {
	    return true;
	}
	Person requester = AccessControl.getPerson();
	if (requester != null) {
	    if (requester.equals(this)) {
		return true;
	    }
	    if (requester.hasRole(RoleType.MANAGER) || requester.hasRole(RoleType.DIRECTIVE_COUNCIL)) {
		return true;
	    }
	    if (requester.hasRole(RoleType.EXTERNAL_SUPERVISOR)) {
		for (RegistrationProtocol registrationProtocol : requester.getRegistrationProtocolsSet()) {
		    for (Registration registration : this.getStudent().getRegistrationsSet()) {
			if (registration.getRegistrationProtocol() == registrationProtocol) {
			    return true;
			}
		    }
		}
	    }
	    if (this.hasRole(RoleType.STUDENT)
		    && (requester.hasRole(RoleType.TEACHER) || requester.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE))) {
		return true;
	    }
	    if (requester.hasRole(RoleType.STUDENT) || requester.hasRole(RoleType.ALUMNI) || requester.hasRole(RoleType.EMPLOYEE)) {
		return getAvailablePhoto();
	    }
	}
	return false;
    }

    @Override
    public Photograph getPersonalPhoto() {
	Photograph photo = super.getPersonalPhoto();
	if (photo == null)
	    return null;
	do {
	    if (photo.getState() == PhotoState.APPROVED)
		return photo;
	    photo = photo.getPrevious();
	} while (photo != null);
	return null;
    }

    public Photograph getPersonalPhotoEvenIfPending() {
	Photograph photo = super.getPersonalPhoto();
	if (photo == null)
	    return null;
	do {
	    if (photo.getState() != PhotoState.REJECTED && photo.getState() != PhotoState.USER_REJECTED)
		return photo;
	    photo = photo.getPrevious();
	} while (photo != null);
	return null;
    }

    public Photograph getPersonalPhotoEvenIfRejected() {
	return super.getPersonalPhoto();
    }

    @Override
    public void setPersonalPhoto(Photograph photo) {
	if (super.getPersonalPhoto() != null)
	    photo.setPrevious(super.getPersonalPhoto());
	super.setPersonalPhoto(photo);
    }

    public List<Photograph> getPhotographHistory() {
	LinkedList<Photograph> history = new LinkedList<Photograph>();
	for (Photograph photo = super.getPersonalPhoto(); photo != null; photo = photo.getPrevious()) {
	    history.addFirst(photo);
	}
	return history;
    }

    public boolean isPhotoPubliclyAvailable() {
	Boolean availablePhoto = getAvailablePhoto();
	if (availablePhoto == null || !availablePhoto) {
	    return false;
	}

	if (!isHomePageAvailable()) {
	    return false;
	}

	Boolean showPhotoInHomepage = getHomepage().getShowPhoto();
	return showPhotoInHomepage != null && showPhotoInHomepage;
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
    public void setAvailableEmail(Boolean available) {
	if (getDefaultEmailAddress() != null)
	    getDefaultEmailAddress().setVisibleToPublic(available);
    }

    @Deprecated
    public Boolean getAvailableWebSite() {
	return isDefaultWebAddressVisible();
    }

    @Deprecated
    public void setAvailableWebSite(Boolean available) {
	if (getDefaultWebAddress() != null)
	    getDefaultWebAddress().setVisibleToPublic(available);
    }

    public List<UnitFile> getUploadedFiles(Unit unit) {
	List<UnitFile> files = new ArrayList<UnitFile>();
	for (UnitFile file : getUploadedFiles()) {
	    if (file.getUnit().equals(unit)) {
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
	for (PersonFunction personFunction : getActivePersonFunctions()) {
	    if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
		return personFunction;
	    }
	}
	return null;
    }

    public List<PersonFunction> getAllGGAEDelegatePersonFunctions() {
	List<PersonFunction> result = new ArrayList<PersonFunction>();
	for (PersonFunction personFunction : getPersonFunctions()) {
	    if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
		result.add(personFunction);
	    }
	}
	return result;
    }

    public boolean isPersonResearcher() {
	return getPersonRole(RoleType.RESEARCHER) != null;
    }

    public boolean isPedagogicalCouncilMember() {
	return getPersonRole(RoleType.PEDAGOGICAL_COUNCIL) != null;
    }

    public Integer getMostSignificantNumber() {
	if (getPartyClassification().equals(PartyClassification.TEACHER)) {
	    return getTeacher().getTeacherNumber();
	}
	if (getPartyClassification().equals(PartyClassification.EMPLOYEE)) {
	    return getEmployee().getEmployeeNumber();
	}
	if (getPartyClassification().equals(PartyClassification.RESEARCHER) && getEmployee() != null) {
	    return getEmployee().getEmployeeNumber();
	}
	if (getStudent() != null) {
	    return getStudent().getNumber();
	    // DegreeType degreeType =
	    // getStudent().getMostSignificantDegreeType();
	    // Collection<Registration> registrations = getStudent()
	    // .getRegistrationsByDegreeType(degreeType);
	    // for (Registration registration : registrations) {
	    // StudentCurricularPlan scp =
	    // registration.getActiveStudentCurricularPlan();
	    // if (scp != null) {
	    // return getStudent().getNumber();
	    // }
	    // }
	}
	if (getPartyClassification().equals(PartyClassification.GRANT_OWNER)) {
	    return getGrantOwner().getNumber();
	}
	return 0;
    }

    public List<Space> getActivePersonSpaces() {
	List<Space> result = new ArrayList<Space>();
	Set<PersonSpaceOccupation> personSpaceOccupationsSet = getPersonSpaceOccupationsSet();
	YearMonthDay current = new YearMonthDay();
	for (PersonSpaceOccupation personSpaceOccupation : personSpaceOccupationsSet) {
	    if (personSpaceOccupation.contains(current)) {
		result.add(personSpaceOccupation.getSpace());
	    }
	}
	return result;
    }

    public AdministrativeOffice getEmployeeAdministrativeOffice() {
	return hasEmployee() ? getEmployee().getAdministrativeOffice() : null;
    }

    public Campus getEmployeeCampus() {
	return hasEmployee() ? getEmployee().getCurrentCampus() : null;
    }

    public Collection<Forum> getForuns(final ExecutionSemester executionSemester) {
	Collection<Forum> foruns = new HashSet<Forum>();
	if (getTeacher() != null) {
	    foruns.addAll(getTeacher().getForuns(executionSemester));
	}

	if (getStudent() != null) {
	    foruns.addAll(getStudent().getForuns(executionSemester));
	}

	for (ForumSubscription forumSubscription : getForumSubscriptionsSet()) {
	    foruns.add(forumSubscription.getForum());
	}

	return foruns;
    }

    public Set<CardGenerationProblem> getCardGenerationProblems(final CardGenerationBatch cardGenerationBatch) {
	final Set<CardGenerationProblem> cardGenerationProblems = new HashSet<CardGenerationProblem>();
	for (final CardGenerationProblem cardGenerationProblem : getCardGenerationProblemsSet()) {
	    if (cardGenerationProblem.getCardGenerationBatch() == cardGenerationBatch) {
		cardGenerationProblems.add(cardGenerationProblem);
	    }
	}
	return cardGenerationProblems;
    }

    public int getNumberOfCardGenerationEntries() {
	int result = 0;
	for (final CardGenerationEntry cardGenerationEntry : getCardGenerationEntriesSet()) {
	    result++;
	}
	return result;
    }

    private boolean hasValidIndividualCandidacy(final Class<? extends IndividualCandidacy> clazz,
	    final ExecutionInterval executionInterval) {
	for (final IndividualCandidacyPersonalDetails candidacyDetails : getIndividualCandidacies()) {
	    IndividualCandidacy candidacy = candidacyDetails.getCandidacy();
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
	List<Formation> formations = new ArrayList<Formation>();
	for (Qualification qualification : getAssociatedQualifications()) {
	    if (qualification instanceof Formation) {
		formations.add((Formation) qualification);
	    }
	}
	return formations;
    }

    public Qualification getLastQualification() {
	return hasAnyAssociatedQualifications() ? Collections
		.max(getAssociatedQualifications(), Qualification.COMPARATOR_BY_YEAR) : null;
    }

    public boolean hasGratuityOrAdministrativeOfficeFeeAndInsuranceDebtsFor(final ExecutionYear executionYear) {
	for (final AnnualEvent annualEvent : getAnnualEventsFor(executionYear)) {
	    if (annualEvent instanceof GratuityEvent || annualEvent instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
		if (annualEvent.isOpen()) {
		    return true;
		}
	    }
	}

	return false;

    }

    public Set<AnnualIRSDeclarationDocument> getAnnualIRSDocuments() {
	final Set<AnnualIRSDeclarationDocument> result = new HashSet<AnnualIRSDeclarationDocument>();

	for (final GeneratedDocument each : getAddressedDocument()) {
	    if (each instanceof AnnualIRSDeclarationDocument) {
		result.add((AnnualIRSDeclarationDocument) each);
	    }
	}

	return result;
    }

    public AnnualIRSDeclarationDocument getAnnualIRSDocumentFor(final Integer year) {
	for (final AnnualIRSDeclarationDocument each : getAnnualIRSDocuments()) {
	    if (each.getYear().compareTo(year) == 0) {
		return each;
	    }
	}

	return null;

    }

    public boolean hasAnnualIRSDocumentFor(final Integer year) {
	return getAnnualIRSDocumentFor(year) != null;
    }

    public Person getIncompatibleVigilantPerson() {
	return getIncompatiblePerson() != null ? getIncompatiblePerson() : getIncompatibleVigilant();
    }

    public void setIncompatibleVigilantPerson(Person person) {
	setIncompatibleVigilant(person);
	setIncompatiblePerson(null);
    }

    public void removeIncompatibleVigilantPerson() {
	setIncompatibleVigilant(null);
	setIncompatiblePerson(null);
    }

    public List<UnavailablePeriod> getUnavailablePeriodsForGivenYear(ExecutionYear executionYear) {
	List<UnavailablePeriod> unavailablePeriods = this.getUnavailablePeriods();
	List<UnavailablePeriod> unavailablePeriodsForGivenYear = new ArrayList<UnavailablePeriod>();
	for (UnavailablePeriod unavailablePeriod : unavailablePeriods) {
	    if (unavailablePeriod.getBeginDate().getYear() == executionYear.getBeginCivilYear()
		    || unavailablePeriod.getBeginDate().getYear() == executionYear.getEndCivilYear()) {
		unavailablePeriodsForGivenYear.add(unavailablePeriod);
	    }
	}
	return unavailablePeriodsForGivenYear;
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
	    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;

	    if (administrativeOfficeFeeAndInsuranceEvent instanceof PastAdministrativeOfficeFeeAndInsuranceEvent) {
		if (event.isInDebt()) {
		    return true;
		}
	    }

	}

	return false;
    }

    public boolean hasAnyResidencePaymentsInDebtForPreviousYear() {
	int previousYear = new LocalDate().minusYears(1).getYear();

	for (final Event event : getResidencePaymentEvents()) {
	    final ResidenceEvent residenceEvent = (ResidenceEvent) event;
	    if (residenceEvent.isFor(previousYear) && !residenceEvent.isCancelled() && !residenceEvent.isPayed()) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasCoordinationExecutionDegreeReportsToAnswer() {
	return !getCoordinationExecutionDegreeReportsToAnswer().isEmpty();
    }

    public Collection<ExecutionDegree> getCoordinationExecutionDegreeReportsToAnswer() {

	Collection<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	InquiryResponsePeriod responsePeriod = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.COORDINATOR);
	if (responsePeriod != null) {
	    for (Coordinator coordinator : getCoordinators()) {
		if (coordinator.isResponsible()
			&& !coordinator.getExecutionDegree().getDegreeType().isThirdCycle()
			&& coordinator.getExecutionDegree().getExecutionYear().getExecutionPeriods().contains(
				responsePeriod.getExecutionPeriod())) {
		    CoordinatorExecutionDegreeCoursesReport report = coordinator.getExecutionDegree()
			    .getExecutionDegreeCoursesReports(responsePeriod.getExecutionPeriod());
		    if (report == null || report.isEmpty()) {
			result.add(coordinator.getExecutionDegree());
		    }
		}

	    }
	}
	return result;
    }

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
	return (Professorship) CollectionUtils.find(getProfessorships(), new Predicate() {
	    @Override
	    public boolean evaluate(Object arg0) {
		Professorship professorship = (Professorship) arg0;
		return professorship.getExecutionCourse() == executionCourse;
	    }
	});
    }

    public List<Professorship> getProfessorshipsByExecutionSemester(final ExecutionSemester executionSemester) {
	List<Professorship> professorships = new ArrayList<Professorship>();
	for (Professorship professorship : getProfessorships()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod() == executionSemester) {
		professorships.add(professorship);
	    }
	}
	return professorships;
    }

    public void updateResponsabilitiesFor(Integer executionYearId, List<Integer> executionCourses)
	    throws MaxResponsibleForExceed, InvalidCategory {

	if (executionYearId == null || executionCourses == null)
	    throw new NullPointerException();

	boolean responsible;
	for (final Professorship professorship : this.getProfessorships()) {
	    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
	    if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearId)) {
		responsible = executionCourses.contains(executionCourse.getIdInternal());
		if (!professorship.getResponsibleFor().equals(Boolean.valueOf(responsible)) && this.getTeacher() != null) {
		    ResponsibleForValidator.getInstance().validateResponsibleForList(this.getTeacher(), executionCourse,
			    professorship);
		    professorship.setResponsibleFor(responsible);
		}
	    }
	}
    }

    @SuppressWarnings("unchecked")
    public List<Professorship> getResponsableProfessorships() {
	List<Professorship> result = new ArrayList<Professorship>();
	for (Professorship professorship : getProfessorships()) {
	    if (professorship.isResponsibleFor()) {
		result.add(professorship);
	    }
	}
	return result;
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
	return (getProfessorshipByExecutionCourse(executionCourse) != null);
    }

    public Set<PhdAlertMessage> getUnreadedPhdAlertMessages() {
	final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

	for (final PhdAlertMessage message : getPhdAlertMessages()) {
	    if (!message.isReaded()) {
		result.add(message);
	    }
	}

	return result;
    }

    public Boolean hasCardGenerationEntryMatchingLine(String line) {
	for (final CardGenerationEntry cardGenerationEntry : this.getCardGenerationEntriesSet()) {
	    if (cardGenerationEntry.matches(line)) {
		return true;
	    }
	}
	return false;
    }

    public Boolean hasCardGenerationEntryLine(String line) {
	for (final CardGenerationEntry cardGenerationEntry : this.getCardGenerationEntriesSet()) {
	    final Category category = cardGenerationEntry.getCategory();
	    return category == Category.CODE_73 || category == Category.CODE_83 || category == Category.CODE_96
		    || cardGenerationEntry.getNormalizedLine().substring(0, 262).equals(line.substring(0, 262));
	}
	return false;
    }

    public boolean isPhdStudent() {
	return hasAnyPhdIndividualProgramProcesses();
    }

    public RegistrationProtocol getOnlyRegistrationProtocol() {
	if (getRegistrationProtocolsCount() == 1) {
	    return getRegistrationProtocols().get(0);
	}
	return null;
    }

    @Service
    public void transferEventsAndAccounts(Person sourcePerson) {
	if (!AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
	    throw new DomainException("permission.denied");
	}

	if (sourcePerson.getInternalAccount() != null) {
	    for (final Entry entry : sourcePerson.getInternalAccount().getEntries()) {
		this.getInternalAccount().transferEntry(entry);
		this.getEvents().add(entry.getAccountingTransaction().getEvent());
	    }

	}

	if (sourcePerson.getExternalAccount() != null) {
	    for (final Entry entry : sourcePerson.getExternalAccount().getEntries()) {
		this.getExternalAccount().transferEntry(entry);
		this.getEvents().add(entry.getAccountingTransaction().getEvent());
	    }
	}
    }

    public Professorship isResponsibleFor(final ExecutionCourse executionCourse) {
	for (final Professorship professorship : getProfessorshipsSet()) {
	    if (professorship.getResponsibleFor() && professorship.getExecutionCourse() == executionCourse) {
		return professorship;
	    }
	}
	return null;
    }

    public boolean hasTeachingInquiriesToAnswer() {
	return !getExecutionCoursesWithTeachingInquiriesToAnswer().isEmpty();
    }

    public Collection<ExecutionCourse> getExecutionCoursesWithTeachingInquiriesToAnswer() {
	Collection<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	TeacherInquiryTemplate currentTemplate = TeacherInquiryTemplate.getCurrentTemplate();
	if (currentTemplate != null) {
	    for (final Professorship professorship : getProfessorships(currentTemplate.getExecutionPeriod())) {
		boolean isToAnswer = hasToAnswerTeacherInquiry(professorship);
		if (isToAnswer
			&& ((!professorship.hasInquiryTeacherAnswer() || professorship.getInquiryTeacherAnswer()
				.hasRequiredQuestionsToAnswer(currentTemplate))
				|| professorship.getInquiryTeacherAnswer().getQuestionAnswers().isEmpty() || professorship
				.hasMandatoryCommentsToMake())) {
		    result.add(professorship.getExecutionCourse());
		}
	    }
	}
	return result;
    }

    public boolean hasToAnswerTeacherInquiry(Professorship professorship) {
	Teacher teacher = getTeacher();
	boolean mandatoryTeachingService = false;
	if (teacher != null && teacher.isTeacherCareerCategory(professorship.getExecutionCourse().getExecutionPeriod())) {
	    mandatoryTeachingService = true;
	}

	boolean isToAnswer = false;
	if (!professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()
		&& professorship.getExecutionCourse().getAvailableForInquiries()) {
	    isToAnswer = true;
	    if (mandatoryTeachingService) {
		isToAnswer = false;
		for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServices()) {
		    if (degreeTeachingService.getPercentage() >= 20) {
			isToAnswer = true;
			break;
		    }
		}
	    }
	}
	return isToAnswer;
    }

    public boolean hasToAnswerRegentInquiry(Professorship professorship) {
	return !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()
		&& professorship.getExecutionCourse().getAvailableForInquiries();
    }

    public List<Professorship> getProfessorships(ExecutionSemester executionSemester) {
	List<Professorship> professorships = new ArrayList<Professorship>();
	for (Professorship professorship : getProfessorshipsSet()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().equals(executionSemester)) {
		professorships.add(professorship);
	    }
	}
	return professorships;
    }

    public List<Professorship> getProfessorships(ExecutionYear executionYear) {
	List<Professorship> professorships = new ArrayList<Professorship>();
	for (Professorship professorship : getProfessorshipsSet()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(executionYear)) {
		professorships.add(professorship);
	    }
	}
	return professorships;
    }

    public boolean teachesAny(final List<ExecutionCourse> executionCourses) {
	for (final Professorship professorship : getProfessorshipsSet()) {
	    if (executionCourses.contains(professorship.getExecutionCourse())) {
		return true;
	    }
	}
	return false;
    }

    public boolean isTeacherEvaluationCoordinatorCouncilMember() {
	final Content content = AbstractDomainObject.fromOID(2482491971449l);
	if (content != null) {
	    final UnitSite site = (UnitSite) content;
	    return site.getManagersSet().contains(AccessControl.getPerson());
	}
	return false;
    }

    public EmailAddress getEmailAddressForSendingEmails() {
	final EmailAddress defaultEmailAddress = getDefaultEmailAddress();
	if (defaultEmailAddress != null) {
	    return defaultEmailAddress;
	}
	final EmailAddress institutionalEmailAddress = getInstitutionalEmailAddress();
	if (institutionalEmailAddress != null) {
	    return institutionalEmailAddress;
	}
	for (final PartyContact partyContact : getPartyContactsSet()) {
	    if (partyContact.isEmailAddress()) {
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

    public String getWorkingPlaceCostCenter() {
	final Employee employee = getEmployee();
	final Unit unit = employee == null ? null : employee.getCurrentWorkingPlace();
	final Integer costCenterCode = unit == null ? null : unit.getCostCenterCode();
	return costCenterCode == null ? null : costCenterCode.toString();
    }

    public String getEmployeeRoleDescription() {
	final RoleType roleType = getMostImportantRoleType(RoleType.TEACHER, RoleType.RESEARCHER, RoleType.EMPLOYEE,
		RoleType.GRANT_OWNER);
	if (roleType == RoleType.RESEARCHER && !hasRole(RoleType.EMPLOYEE)) {
	    return "EXTERNAL_RESEARCH_PERSONNEL";
	}
	return roleType == null ? null : roleType.name();
    }

    // Temp method used for mission system.
    public String getWorkingPlaceForAnyRoleType() {
	if (hasRole(RoleType.TEACHER) || hasRole(RoleType.EMPLOYEE) || hasRole(RoleType.RESEARCHER)) {
	    return getWorkingPlaceCostCenter();
	}
	if (hasRole(RoleType.RESEARCHER)) {
	    final Collection<? extends Accountability> accountabilities = getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
	    final YearMonthDay currentDate = new YearMonthDay();
	    for (final Accountability accountability : accountabilities) {
		if (accountability.isActive(currentDate)) {
		    final Unit unit = (Unit) accountability.getParentParty();
		    final Integer costCenterCode = unit.getCostCenterCode();
		    if (costCenterCode != null) {
			return costCenterCode.toString();
		    }
		}
	    }
	}
	final GrantOwner grantOwner = getGrantOwner();
	if (grantOwner != null) {
	    final YearMonthDay today = new YearMonthDay();
	    for (final GrantContract grantContract : grantOwner.getGrantContracts()) {
		for (final GrantContractRegime grantContractRegime : grantContract.getContractRegimes()) {
		    if (!today.isBefore(grantContractRegime.getDateBeginContractYearMonthDay())
			    && grantContractRegime.getDateEndContractYearMonthDay() != null
			    && !today.isAfter(grantContractRegime.getDateEndContractYearMonthDay())) {
			final GrantCostCenter grantCostCenter = grantContract.getGrantCostCenter();
			final Person person = grantOwner.getPerson();
			if (grantCostCenter != null && person != null) {
			    final String costCenterDesignation = grantCostCenter.getNumber();
			    if (costCenterDesignation != null && !costCenterDesignation.isEmpty()) {
				return costCenterDesignation;
			    }
			}
		    }
		}
	    }
	}
	return null;
    }

    private RoleType getMostImportantRoleType(final RoleType... roleTypes) {
	for (final RoleType roleType : roleTypes) {
	    if (hasRole(roleType)) {
		return roleType;
	    }
	}
	return null;
    }

    public String readAllTeacherInformation() {
	return readAllInformation(RoleType.TEACHER);
    }

    public String readAllResearcherInformation() {
	return readAllInformation(RoleType.RESEARCHER, RoleType.TEACHER);
    }

    public String readAllEmployeeInformation() {
	return readAllInformation(RoleType.EMPLOYEE, RoleType.RESEARCHER, RoleType.TEACHER);
    }

    public String readAllGrantOwnerInformation() {
	final StringBuilder result = new StringBuilder();
	final YearMonthDay today = new YearMonthDay();
	for (final GrantOwner grantOwner : RootDomainObject.getInstance().getGrantOwnersSet()) {
	    final String costCenterDesignation = getCostCenterForGrantOwner(today, grantOwner);
	    if (costCenterDesignation != null) {
		if (result.length() > 0) {
		    result.append('|');
		}
		final Person person = grantOwner.getPerson();
		result.append(person.getUsername());
		result.append(':');
		result.append(RoleType.GRANT_OWNER.name());
		result.append(':');
		result.append(costCenterDesignation);
	    }
	}
	return result.toString();
    }

    private String getCostCenterForGrantOwner(final YearMonthDay today, final GrantOwner grantOwner) {
	for (final GrantContract grantContract : grantOwner.getGrantContracts()) {
	    for (final GrantContractRegime grantContractRegime : grantContract.getContractRegimes()) {
		if (!today.isBefore(grantContractRegime.getDateBeginContractYearMonthDay())
			&& grantContractRegime.getDateEndContractYearMonthDay() != null
			&& !today.isAfter(grantContractRegime.getDateEndContractYearMonthDay())) {
		    final GrantCostCenter grantCostCenter = grantContract.getGrantCostCenter();
		    final Person person = grantOwner.getPerson();
		    if (grantCostCenter != null && person != null) {
			final String costCenterDesignation = grantCostCenter.getNumber();
			if (costCenterDesignation != null && !costCenterDesignation.isEmpty()) {
			    return costCenterDesignation;
			}
		    }
		}
	    }
	}
	return null;
    }

    protected static String readAllInformation(final RoleType roleType, final RoleType... exclusionRoleTypes) {
	final Role role = Role.getRoleByRoleType(roleType);
	final StringBuilder result = new StringBuilder();
	for (final Person person : role.getAssociatedPersonsSet()) {
	    if (!hasAnyRole(person, exclusionRoleTypes)) {
		final String costCenter = person.getWorkingPlaceCostCenter();
		if (costCenter != null && !costCenter.isEmpty()) {
		    if (result.length() > 0) {
			result.append('|');
		    }
		    result.append(person.getUsername());
		    result.append(':');
		    result.append(roleType.name());
		    result.append(':');
		    result.append(costCenter);
		}
	    }
	}
	return result.toString();
    }

    public static String readAllExternalResearcherInformation() {
	final RoleType roleType = RoleType.RESEARCHER;
	final RoleType[] exclusionRoleTypes = new RoleType[] { RoleType.TEACHER };

	final Role role = Role.getRoleByRoleType(roleType);
	final StringBuilder result = new StringBuilder();
	for (final Person person : role.getAssociatedPersonsSet()) {
	    if (!hasAnyRole(person, exclusionRoleTypes)) {
		final Collection<? extends Accountability> accountabilities = person
			.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
		final YearMonthDay currentDate = new YearMonthDay();
		for (final Accountability accountability : accountabilities) {
		    if (accountability.isActive(currentDate)) {
			final Unit unit = (Unit) accountability.getParentParty();
			final Integer costCenterCode = unit.getCostCenterCode();
			if (costCenterCode != null) {
			    if (result.length() > 0) {
				result.append('|');
			    }
			    result.append(person.getUsername());
			    result.append(':');
			    result.append(roleType.name());
			    result.append(':');
			    result.append(costCenterCode);
			}
		    }
		}
	    }
	}
	return result.toString();
    }

    private static boolean hasAnyRole(final Person person, final RoleType[] roleTypes) {
	for (final RoleType roleType : roleTypes) {
	    if (person.hasRole(roleType)) {
		return true;
	    }
	}
	return false;
    }

    public boolean areContactsRecent(Class<? extends PartyContact> contactClass, int daysNotUpdated) {
	List<? extends PartyContact> partyContacts = getPartyContacts(contactClass);
	boolean isUpdated = false;
	for (PartyContact partyContact : partyContacts) {
	    if (partyContact.getLastModifiedDate() == null) {
		isUpdated = isUpdated || false;
	    } else {
		DateTime lastModifiedDate = partyContact.getLastModifiedDate();
		DateTime now = new DateTime();
		Months months = Months.monthsBetween(lastModifiedDate, now);
		if (months.getMonths() > daysNotUpdated) {
		    isUpdated = isUpdated || false;
		} else {
		    isUpdated = isUpdated || true;
		}
	    }
	}
	return isUpdated;
    }

    @Override
    @Deprecated
    public String getFiscalCode() {
	return super.getFiscalCode();
    }

    @Override
    @Deprecated
    public void setFiscalCode(String value) {
	super.setFiscalCode(value);
    }

    @ConsistencyPredicate
    public final boolean namesCorrectlyPartitioned() {
	if (StringUtils.isEmpty(getGivenNames()) && StringUtils.isEmpty(getFamilyNames())) {
	    return true;
	}
	if (StringUtils.isEmpty(getGivenNames())) {
	    return false;
	}
	if (StringUtils.isEmpty(getFamilyNames())) {
	    return false;
	}
	return (getGivenNames() + " " + getFamilyNames()).equals(getName());
    }

    public ArrayList<RoleOperationLog> getPersonRoleOperationLogArrayListOrderedByDate() {
	return orderRoleOperationLogSetByValue(this.getPersonRoleOperationLogSet(), "logDate");
    }

    public ArrayList<RoleOperationLog> getGivenRoleOperationLogArrayListOrderedByDate() {
	return orderRoleOperationLogSetByValue(this.getGivenRoleOperationLogSet(), "logDate");
    }

    private ArrayList<RoleOperationLog> orderRoleOperationLogSetByValue(Set<RoleOperationLog> roleOperationLogSet, String value) {
	ArrayList<RoleOperationLog> roleOperationLogList = new ArrayList<RoleOperationLog>(roleOperationLogSet);
	Collections.sort(roleOperationLogList, new ReverseComparator(new BeanComparator(value)));
	return roleOperationLogList;
    }

    public boolean insideSpace(Space space) {
	for (SpaceAttendances attendance : space.getAttendances()) {
	    if (attendance.getPersonIstUsername().equals(this.getIstUsername())) {
		return true;
	    }
	}
	return false;
    }

    public static Person readPersonByLibraryCardNumber(String cardNumber) {
	for (LibraryCard card : RootDomainObject.getInstance().getLibraryCards()) {
	    if (card.getCardNumber() != null && card.getCardNumber().equals(cardNumber)) {
		return card.getPerson();
	    }
	}
	return null;
    }
}
