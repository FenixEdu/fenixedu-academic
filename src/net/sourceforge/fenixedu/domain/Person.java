package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.domain.sms.SmsDeliveryType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Person extends Person_Base {

    final static Comparator PERSON_SENTSMS_COMPARATOR_BY_SENT_DATE = new BeanComparator("sendDate");
    static {
        Role.PersonRole.addListener(new PersonRoleListener());
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public String getNome() {
        return super.getName();
    }

    public void setNome(String name) {
        super.setName(name);
    }

    public Person() {
        super();
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    public Person(InfoPerson personToCreate, Country country) {

        super();
        if (personToCreate.getIdInternal() != null) {
            throw new DomainException("error.person.existentPerson");
        }

        checkConditionsToCreateNewPerson(personToCreate.getNumeroDocumentoIdentificacao(),
                personToCreate.getTipoDocumentoIdentificacao(), this);

        createUserAndLoginEntity(personToCreate.getUsername());

        setProperties(personToCreate);
        setPais(country);
        setIsPassInKerberos(Boolean.FALSE);
    }

    private void createUserAndLoginEntity(String username) {
        new Login(new User(this), username);
    }

    public Person(String name, String identificationDocumentNumber,
            IDDocumentType identificationDocumentType, Gender gender, String username) {

        super();
        checkConditionsToCreateNewPerson(identificationDocumentNumber, identificationDocumentType, this);

        createUserAndLoginEntity(username);

        setNome(name);
        setDocumentIdNumber(identificationDocumentNumber);
        setIdDocumentType(identificationDocumentType);
        setGender(gender);
        setAvailableEmail(Boolean.FALSE);
        setAvailableWebSite(Boolean.FALSE);
        setAvailablePhoto(Boolean.FALSE);
        setMaritalStatus(MaritalStatus.SINGLE);
        setIsPassInKerberos(Boolean.FALSE);
    }

    private Person(ExternalPerson externalPerson, String name, Gender gender, String address, String areaCode, String areaOfAreaCode, String area,
            String parishOfResidence, String districtSubdivisionOfResidence, String districtOfResidence, String phone, String mobile,
            String homepage, String email, String documentIDNumber, IDDocumentType documentType) {

        super();
        checkConditionsToCreateNewPerson(documentIDNumber, documentType, this);

        setExternalPerson(externalPerson);
        
        setNome(name);
        setGender(gender);
        setAddress(address);
        setAreaCode(areaCode);
        setAreaOfAreaCode(areaOfAreaCode);
        setArea(area);
        setParishOfResidence(parishOfResidence);
        setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
        setDistrictOfResidence(districtOfResidence);
        setPhone(phone);
        setMobile(mobile);
        setWebAddress(homepage);
        setEmail(email);
        setDocumentIdNumber(documentIDNumber);
        setIdDocumentType(documentType);
        setAvailableEmail(Boolean.FALSE);
        setAvailableWebSite(Boolean.FALSE);
        setAvailablePhoto(Boolean.FALSE);
        setMaritalStatus(MaritalStatus.UNKNOWN);
    }

    protected static Person createExternalPerson(ExternalPerson externalPerson, String name,
            Gender gender, String address, String areaCode, String areaOfAreaCode, String area,
            String parishOfResidence, String districtSubdivisionOfResidence, String districtOfResidence,
            String phone, String mobile, String homepage, String email, String documentIdNumber,
            IDDocumentType documentType) {

        return new Person(externalPerson, name, gender, address, areaCode, areaOfAreaCode, area,
                parishOfResidence, districtSubdivisionOfResidence, districtOfResidence, phone, mobile,
                homepage, email, documentIdNumber, documentType);
    }

    public Person(String username, String name, Gender gender, String address, String phone,
            String mobile, String homepage, String email, String documentIDNumber,
            IDDocumentType documentType) {

        super();
        checkConditionsToCreateNewPerson(documentIDNumber, documentType, this);

        createUserAndLoginEntity(username);

        setNome(name);
        setGender(gender);
        setAddress(address);
        setPhone(phone);
        setMobile(mobile);
        setWebAddress(homepage);
        setEmail(email);
        setDocumentIdNumber(documentIDNumber);
        setIdDocumentType(documentType);
        setAvailableEmail(Boolean.FALSE);
        setAvailableWebSite(Boolean.FALSE);
        setAvailablePhoto(Boolean.FALSE);
        setMaritalStatus(MaritalStatus.UNKNOWN);
        setIsPassInKerberos(Boolean.FALSE);
    }

    public void edit(InfoPerson personToEdit, Country country) {
        checkConditionsToCreateNewPerson(personToEdit.getNumeroDocumentoIdentificacao(), personToEdit
                .getTipoDocumentoIdentificacao(), this);

        setProperties(personToEdit);
        if (country != null) {
            setPais(country);
        }
    }

    public void update(InfoPerson updatedPersonalData, Country country) {
        checkConditionsToCreateNewPerson(updatedPersonalData.getNumeroDocumentoIdentificacao(),
                updatedPersonalData.getTipoDocumentoIdentificacao(), this);
        updateProperties(updatedPersonalData);
        if (country != null) {
            setPais(country);
        }
        // setPais((Country) valueToUpdate(getPais(), country));
    }

    public void editPersonalContactInformation(InfoPerson personToEdit) {
        setMobile(personToEdit.getTelemovel());
        setWorkPhone(personToEdit.getWorkPhone());
        setEmail(personToEdit.getEmail());
        setAvailableEmail(personToEdit.getAvailableEmail());
        setWebAddress(personToEdit.getEnderecoWeb());
        setAvailableWebSite(personToEdit.getAvailableWebSite());
        setAvailablePhoto(personToEdit.getAvailablePhoto());
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
            String email) {
        setNome(name);
        setAddress(address);
        setPhone(phone);
        setMobile(mobile);
        setWebAddress(homepage);
        setEmail(email);
    }

    private void checkConditionsToCreateNewPerson(final String documentIDNumber,
            final IDDocumentType documentType, Person thisPerson) {

        if (documentIDNumber != null
                && documentType != null
                && checkIfDocumentNumberIdAndDocumentIdTypeExists(documentIDNumber, documentType,
                        thisPerson)) {

            throw new DomainException("error.person.existent.docIdAndType");
        }
    }

    private Login getLoginIdentification() {
        User personUser = getUser();
        if (personUser != null) {
            return personUser.readUserLoginIdentification();
        }
        return null;
    }

    public String getUsername() {
        Login login = getLoginIdentification();
        return (login != null) ? login.getUsername() : null;
    }

    public void setUsername(String username) {
        getLoginIdentification().setUsername(username);
    }

    public String getPassword() {
        Login login = getLoginIdentification();
        return (login != null) ? login.getPassword() : null;
    }

    public void setPassword(String password) {
        getLoginIdentification().setPassword(password);
    }

    public void setIsPassInKerberos(Boolean isPassInKerberos) {
        getLoginIdentification().setIsPassInKerberos(isPassInKerberos);
    }

    public Boolean getIsPassInKerberos() {
        Login login = getLoginIdentification();
        return (login != null) ? login.getIsPassInKerberos() : null;
    }

    public void setIstUsername(String istUsername) {
        getUser().setUserUId(istUsername);
    }

    public String getIstUsername() {
        return (getUser() != null) ? getUser().getUserUId() : null;
    }

    public void changeUsername(String newUsername) {
        if (newUsername == null || newUsername.equals("")) {
            throw new DomainException("error.person.nullOrEmptyUsername");
        }

        if (getUser() == null) {
            throw new DomainException("error.person.unExistingUser");
        }

        if (Login.checkIfUsernameExists(newUsername, this.getLoginIdentification())) {
            throw new DomainException("error.person.existingUsername");
        }

        setUsername(newUsername);
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

        if (getFiscalCode() != null && getCodigoFiscal().equalsIgnoreCase(newPassword)) {
            throw new DomainException("error.person.invalidFiscalCodePassword");
        }

        if (getSocialSecurityNumber() != null && getNumContribuinte().equalsIgnoreCase(newPassword)) {
            throw new DomainException("error.person.invalidTaxPayerPassword");
        }

        setPassword(PasswordEncryptor.encryptPassword(newPassword));
    }

    public void updateUsername() {
        this.setUsername(UsernameUtils.updateUsername(this));
    }

    public void updateIstUsername() {
        this.setIstUsername(UsernameUtils.updateIstUsername(this));
    }

    public Role getPersonRole(RoleType roleType) {

        for (Role role : this.getPersonRoles()) {
            if (role.getRoleType().equals(roleType)) {
                return role;
            }
        }
        return null;
    }

    public void addPersonRoleByRoleType(RoleType roleType) {
        if (!this.hasRole(roleType)) {
            this.addPersonRoles(Role.getRoleByRoleType(roleType));
        }
    }

    public Boolean hasRole(final RoleType roleType) {
        for (final Role role : this.getPersonRoles()) {
            if (role.getRoleType() == roleType) {
                return true;
            }
        }
        return false;
    }

    public Student getStudentByType(DegreeType degreeType) {
        for (Student student : this.getStudents()) {
            if (student.getDegreeType().equals(degreeType)) {
                return student;
            }
        }
        return null;
    }

    public String getNacionalidade() {
        return this.getPais().getNationality();
    }

    public List<ResultPublication> getResultPublications() {
        List<ResultPublication> resultPublications = new ArrayList<ResultPublication>();
        Result result = null;
        for (ResultParticipation resultParticipation : getResultParticipations()) {
            result = resultParticipation.getResult();
            // filter only publication participations
            if (result instanceof ResultPublication) {
                resultPublications.add((ResultPublication) result);
            }
        }
        return resultPublications;
    }

    public List<ResultParticipation> getPersonParticipationsWithPublications() {

        List<ResultParticipation> publicationParticipations = new ArrayList<ResultParticipation>();
        for (ResultParticipation resultParticipation : getResultParticipations()) {
            Result result = resultParticipation.getResult();
            // filter only publication authorships
            if (result instanceof ResultPublication) {
                publicationParticipations.add(resultParticipation);
            }
        }
        return publicationParticipations;
    }

    public List<ResultParticipation> getPersonParticipationsWithPatents() {

        List<ResultParticipation> patentParticipations = new ArrayList<ResultParticipation>();
        for (ResultParticipation resultParticipation : getResultParticipations()) {
            Result result = resultParticipation.getResult();
            // filter only patent authorships
            if (result instanceof ResultPatent) {
                patentParticipations.add(resultParticipation);
            }
        }
        return patentParticipations;
    }

    @Override
    public List<Advisory> getAdvisories() {
        Date currentDate = Calendar.getInstance().getTime();
        List<Advisory> result = new ArrayList<Advisory>();
        for (Advisory advisory : super.getAdvisories()) {
            if (advisory.getExpires() == null || advisory.getExpires().after(currentDate)) {
                result.add(advisory);
            }
        }
        return result;
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private void setProperties(InfoPerson infoPerson) {

        setNome(infoPerson.getNome());

        if (infoPerson.getNumeroDocumentoIdentificacao() != null)
            setDocumentIdNumber(infoPerson.getNumeroDocumentoIdentificacao());
        if (infoPerson.getTipoDocumentoIdentificacao() != null)
            setIdDocumentType(infoPerson.getTipoDocumentoIdentificacao());

        setFiscalCode(infoPerson.getCodigoFiscal());
        setAreaCode(infoPerson.getCodigoPostal());
        setDistrictSubdivisionOfResidence(infoPerson.getConcelhoMorada());
        setDistrictSubdivisionOfBirth(infoPerson.getConcelhoNaturalidade());
        setEmissionDateOfDocumentId(infoPerson.getDataEmissaoDocumentoIdentificacao());
        setExpirationDateOfDocumentId(infoPerson.getDataValidadeDocumentoIdentificacao());
        setDistrictOfResidence(infoPerson.getDistritoMorada());
        setDistrictOfBirth(infoPerson.getDistritoNaturalidade());
        setEmail(infoPerson.getEmail());
        setWebAddress(infoPerson.getEnderecoWeb());
        setMaritalStatus((infoPerson.getMaritalStatus() == null) ? MaritalStatus.UNKNOWN : infoPerson
                .getMaritalStatus());
        setParishOfResidence(infoPerson.getFreguesiaMorada());
        setParishOfBirth(infoPerson.getFreguesiaNaturalidade());
        setEmissionLocationOfDocumentId(infoPerson.getLocalEmissaoDocumentoIdentificacao());
        setArea(infoPerson.getLocalidade());
        setAreaOfAreaCode(infoPerson.getLocalidadeCodigoPostal());
        setAddress(infoPerson.getMorada());
        setDateOfBirth(infoPerson.getNascimento());
        setNameOfMother(infoPerson.getNomeMae());
        setNameOfFather(infoPerson.getNomePai());
        setSocialSecurityNumber(infoPerson.getNumContribuinte());

        setProfession(infoPerson.getProfissao());
        setGender(infoPerson.getSexo());
        setPhone(infoPerson.getTelefone());
        setMobile(infoPerson.getTelemovel());

        // Generate person's Password
        if (getPassword() == null)
            setPassword(PasswordEncryptor.encryptPassword(GeneratePassword.getInstance()
                    .generatePassword(this)));

        setAvailableEmail(infoPerson.getAvailableEmail());
        setAvailablePhoto(Boolean.TRUE);
        setAvailableWebSite(infoPerson.getAvailableWebSite());
        setWorkPhone(infoPerson.getWorkPhone());
    }

    private void updateProperties(InfoPerson infoPerson) {

        setNome(valueToUpdateIfNewNotNull(getNome(), infoPerson.getNome()));
        setDocumentIdNumber(valueToUpdateIfNewNotNull(getDocumentIdNumber(), infoPerson
                .getNumeroDocumentoIdentificacao()));
        setIdDocumentType((IDDocumentType) valueToUpdateIfNewNotNull(getIdDocumentType(), infoPerson
                .getTipoDocumentoIdentificacao()));
        setFiscalCode(valueToUpdateIfNewNotNull(getFiscalCode(), infoPerson.getCodigoFiscal()));
        setAreaCode(valueToUpdateIfNewNotNull(getAreaCode(), infoPerson.getCodigoPostal()));
        setDistrictSubdivisionOfResidence(valueToUpdateIfNewNotNull(getDistrictSubdivisionOfResidence(),
                infoPerson.getConcelhoMorada()));
        setDistrictSubdivisionOfBirth(valueToUpdateIfNewNotNull(getDistrictSubdivisionOfBirth(),
                infoPerson.getConcelhoNaturalidade()));
        setEmissionDateOfDocumentId((Date) valueToUpdateIfNewNotNull(getEmissionDateOfDocumentId(),
                infoPerson.getDataEmissaoDocumentoIdentificacao()));
        setExpirationDateOfDocumentId((Date) valueToUpdateIfNewNotNull(getExpirationDateOfDocumentId(),
                infoPerson.getDataValidadeDocumentoIdentificacao()));
        setDistrictOfResidence(valueToUpdateIfNewNotNull(getDistrictOfResidence(), infoPerson
                .getDistritoMorada()));
        setDistrictOfBirth(valueToUpdateIfNewNotNull(getDistrictOfBirth(), infoPerson
                .getDistritoNaturalidade()));
        setEmail(valueToUpdate(getEmail(), infoPerson.getEmail()));
        setWebAddress(valueToUpdate(getWebAddress(), infoPerson.getEnderecoWeb()));
        MaritalStatus maritalStatus = (MaritalStatus) valueToUpdateIfNewNotNull(getMaritalStatus(),
                infoPerson.getMaritalStatus());
        setMaritalStatus((maritalStatus == null) ? MaritalStatus.UNKNOWN : maritalStatus);
        setParishOfResidence(valueToUpdateIfNewNotNull(getParishOfResidence(), infoPerson
                .getFreguesiaMorada()));
        setParishOfBirth(valueToUpdateIfNewNotNull(getParishOfBirth(), infoPerson
                .getFreguesiaNaturalidade()));
        setEmissionLocationOfDocumentId(valueToUpdateIfNewNotNull(getEmissionLocationOfDocumentId(),
                infoPerson.getLocalEmissaoDocumentoIdentificacao()));
        setArea(valueToUpdateIfNewNotNull(getArea(), infoPerson.getLocalidade()));
        setAreaOfAreaCode(valueToUpdateIfNewNotNull(getAreaOfAreaCode(), infoPerson
                .getLocalidadeCodigoPostal()));
        setAddress(valueToUpdateIfNewNotNull(getAddress(), infoPerson.getMorada()));
        setDateOfBirth((Date) valueToUpdateIfNewNotNull(getDateOfBirth(), infoPerson.getNascimento()));
        setNameOfMother(valueToUpdateIfNewNotNull(getNameOfMother(), infoPerson.getNomeMae()));
        setNameOfFather(valueToUpdateIfNewNotNull(getNameOfFather(), infoPerson.getNomePai()));
        setSocialSecurityNumber(valueToUpdateIfNewNotNull(getSocialSecurityNumber(), infoPerson
                .getNumContribuinte()));
        setProfession(valueToUpdateIfNewNotNull(getProfession(), infoPerson.getProfissao()));
        setGender((Gender) valueToUpdateIfNewNotNull(getGender(), infoPerson.getSexo()));
        setPhone(valueToUpdate(getPhone(), infoPerson.getTelefone()));
        setMobile(valueToUpdate(getMobile(), infoPerson.getTelemovel()));
        // setAvailableEmail((Boolean) valueToUpdate(getAvailableEmail(),
        // infoPerson.getAvailableEmail()));
        // setAvailablePhoto((Boolean) valueToUpdate(getAvailablePhoto(),
        // infoPerson.getAvailablePhoto()));
        // setAvailableWebSite((Boolean) valueToUpdate(getAvailableWebSite(),
        // infoPerson
        // .getAvailableWebSite()));
        if (!StringUtils.isNumeric(getWorkPhone())) {
            setWorkPhone(infoPerson.getWorkPhone());
        } else {
            setWorkPhone(valueToUpdate(getWorkPhone(), infoPerson.getWorkPhone()));
        }

        // setPassword(valueToUpdate(getPassword(),
        // PasswordEncryptor.encryptPassword(GeneratePassword
        // .getInstance().generatePassword(this))));

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
        List<PersonFunction> activeFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : getPersonFunctions()) {
            if (personFunction.isActive(new YearMonthDay())) {
                activeFunctions.add(personFunction);
            }
        }
        return activeFunctions;
    }

    public List<PersonFunction> getInactivePersonFunctions() {
        List<PersonFunction> inactiveFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : getPersonFunctions()) {
            if (!personFunction.isActive(new YearMonthDay())) {
                inactiveFunctions.add(personFunction);
            }
        }
        return inactiveFunctions;
    }

    public List<Function> getActiveInherentPersonFunctions() {
        List<Function> inherentFunctions = new ArrayList<Function>();
        for (PersonFunction accountability : getActivePersonFunctions()) {
            inherentFunctions.addAll(accountability.getFunction().getInherentFunctions());
        }
        return inherentFunctions;
    }

    public boolean containsActivePersonFunction(Function function) {
        for (PersonFunction personFunction : getActivePersonFunctions()) {
            if (personFunction.getFunction().equals(function)) {
                return true;
            }
        }
        return false;
    }

    public List<PersonFunction> getPersonFuntions(YearMonthDay begin, YearMonthDay end) {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (Accountability accountability : getPersonFunctions()) {
            if (accountability.belongsToPeriod(begin, end)) {
                result.add((PersonFunction) accountability);
            }
        }
        return result;
    }

    public List<PersonFunction> getPersonFunctions() {
        return new ArrayList(getParentAccountabilities(AccountabilityTypeEnum.MANAGEMENT_FUNCTION,
                PersonFunction.class));
            }

    public boolean hasFunctionType(FunctionType functionType) {
        for (PersonFunction accountability : getActivePersonFunctions()) {
            if (accountability.getFunction().getFunctionType() == functionType) {
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

        getPersonRoles().clear();
        getManageableDepartmentCredits().clear();
        getAdvisories().clear();
        removeCms();
        removePais();
        getUser().delete();
        super.delete();
    }

    private boolean canBeDeleted() {
        if (getStudentsCount() > 0) {
            return false;
        }
        if (getSentSmsCount() > 0) {
            return false;
        }
        if (getExportGroupingReceiversCount() > 0) {
            return false;
        }
        if (getPersonFunctions().size() > 0) {
            return false;
        }
        if (getAssociatedQualificationsCount() > 0) {
            return false;
        }
        if (getAssociatedAlteredCurriculumsCount() > 0) {
            return false;
        }
        if (getEnrolmentEvaluationsCount() > 0) {
            return false;
        }
        if (getExportGroupingSendersCount() > 0) {
            return false;
        }
        if (getEditedWebSiteItemsCount() > 0) {
            return false;
        }
        if (getResponsabilityTransactionsCount() > 0) {
            return false;
        }
        if (getMasterDegreeCandidatesCount() > 0) {
            return false;
        }
        if (getGuidesCount() > 0) {
            return false;
        }
        if (getProjectAccessesCount() > 0) {
            return false;
        }
        if (getPersonAuthorshipsCount() > 0) {
            return false;
        }
        if (getEmployee() != null) {
            return false;
        }
        if (getExternalPerson() != null) {
            return false;
        }
        if (getTeacher() != null) {
            return false;
        }
        if (getAssociatedPersonAccount() != null) {
            return false;
        }
        if (getGrantOwner() != null) {
            return false;
        }
        return true;
    }

    private static class PersonRoleListener extends dml.runtime.RelationAdapter<Role, Person> {
        /**
         * This method is called transparently to the programmer when he adds a
         * role a person. This method's responsabilities are: to verify if the
         * person allready has the role being added; to verify if the person
         * meets the prerequisites to add this new role; to update the username;
         * to actually add the role.
         */
        @Override
        public void beforeAdd(Role role, Person person) {
            // verify if the person already has the role being inserted
            if (!person.hasRole(role.getRoleType())) {

                // verify role dependencies and throw a DomainException in case
                // they
                // aren't met.
                if (!verifiesDependencies(person, role)) {
                    throw new DomainException("error.person.addingInvalidRole", role.getRoleType()
                            .toString());
                }
            }
        }

        @Override
        public void afterAdd(Role role, Person person) {
            if (role.getRoleType().equals(RoleType.TEACHER)) {
                person.addPersonRoles(Role.getRoleByRoleType(RoleType.RESEARCHER));
            }
            person.updateUsername();
            person.updateIstUsername();
        }

        /**
         * This method is called transparently to the programmer when he removes
         * a role from a person This method's responsabilities are: to actually
         * remove the role; to remove all dependencies existant from the
         * recently removed role; to update the username.
         * 
         */
        @Override
        public void beforeRemove(Role removedRole, Person person) {
            if (person != null) {
                if (removedRole != null && person.hasRole(removedRole.getRoleType())) {
                    // Remove role dependencies
                    removeDependencies(person, removedRole);
                }
            }
        }

        @Override
        public void afterRemove(Role removedRole, Person person) {
            // Update person's username according to the removal of the role
            person.updateUsername();
            person.updateIstUsername();
        }

        private static Boolean verifiesDependencies(Person person, Role role) {
            switch (role.getRoleType()) {
            case COORDINATOR:
            case DIRECTIVE_COUNCIL:
            case SEMINARIES_COORDINATOR:
            case RESEARCHER:
                return person.hasRole(RoleType.TEACHER);
            case DEGREE_ADMINISTRATIVE_OFFICE:
            case DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER:
            case DEPARTMENT_CREDITS_MANAGER:
            case GRANT_OWNER_MANAGER:
            case MASTER_DEGREE_ADMINISTRATIVE_OFFICE:
            case TREASURY:
            case CREDITS_MANAGER:
            case DEPARTMENT_ADMINISTRATIVE_OFFICE:
                return person.hasRole(RoleType.EMPLOYEE);
            case DELEGATE:
                return person.hasRole(RoleType.STUDENT);
            case MASTER_DEGREE_CANDIDATE:
            case CANDIDATE:
                return true;
            case PERSON:
                return true;
            default:
                return person.hasRole(RoleType.PERSON);
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
                removeRoleIfPresent(person, RoleType.TIME_TABLE_MANAGER);
                removeRoleIfPresent(person, RoleType.WEBSITE_MANAGER);
                removeRoleIfPresent(person, RoleType.FIRST_TIME_STUDENT);
                break;

            case TEACHER:
                removeRoleIfPresent(person, RoleType.COORDINATOR);
                removeRoleIfPresent(person, RoleType.DIRECTIVE_COUNCIL);
                removeRoleIfPresent(person, RoleType.SEMINARIES_COORDINATOR);
                removeRoleIfPresent(person, RoleType.RESEARCHER);
                // removeRoleIfPresent(person, RoleType.EMPLOYEE);
                break;

            case EMPLOYEE:
                removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
                removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
                removeRoleIfPresent(person, RoleType.DEPARTMENT_CREDITS_MANAGER);
                removeRoleIfPresent(person, RoleType.GRANT_OWNER_MANAGER);
                removeRoleIfPresent(person, RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
                removeRoleIfPresent(person, RoleType.TREASURY);
                removeRoleIfPresent(person, RoleType.CREDITS_MANAGER);
                removeRoleIfPresent(person, RoleType.DEPARTMENT_MEMBER);
                break;

            case STUDENT:
                removeRoleIfPresent(person, RoleType.DELEGATE);
                break;
            }
        }

        private static void removeRoleIfPresent(Person person, RoleType roleType) {
            final Role tmpRole = person.getPersonRole(roleType);
            if (tmpRole != null) {
                person.getPersonRoles().remove(tmpRole);
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
        int count = 0;
        for (final SentSms sentSms : this.getSentSmsSet()) {
            if (sentSms.getDeliveryType() != SmsDeliveryType.NOT_SENT_TYPE
                    && (sentSms.getSendDate().after(startDate) || sentSms.getSendDate()
                            .equals(startDate)) && sentSms.getSendDate().before(endDate)) {

                count++;
            }
        }
        return count;
    }

    public Student readStudentByDegreeType(DegreeType degreeType) {
        for (final Student student : this.getStudents()) {
            if (student.getDegreeType().equals(degreeType)) {
                return student;
            }
        }
        return null;
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

    @Deprecated
    public String getCodigoFiscal() {
        return super.getFiscalCode();
    }

    @Deprecated
    public String getCodigoPostal() {
        return super.getAreaCode();
    }

    @Deprecated
    public String getConcelhoMorada() {
        return super.getDistrictSubdivisionOfResidence();
    }

    @Deprecated
    public String getConcelhoNaturalidade() {
        return super.getDistrictSubdivisionOfBirth();
    }

    @Deprecated
    public Date getDataEmissaoDocumentoIdentificacao() {
        return super.getEmissionDateOfDocumentId();
    }

    @Deprecated
    public Date getDataValidadeDocumentoIdentificacao() {
        return super.getExpirationDateOfDocumentId();
    }

    @Deprecated
    public String getDistritoMorada() {
        return super.getDistrictOfResidence();
    }

    @Deprecated
    public String getDistritoNaturalidade() {
        return super.getDistrictOfBirth();
    }

    @Deprecated
    public String getEnderecoWeb() {
        return super.getWebAddress();
    }

    @Deprecated
    public String getFreguesiaMorada() {
        return super.getParishOfResidence();
    }

    @Deprecated
    public String getFreguesiaNaturalidade() {
        return super.getParishOfBirth();
    }

    @Deprecated
    public String getLocalEmissaoDocumentoIdentificacao() {
        return super.getEmissionLocationOfDocumentId();
    }

    @Deprecated
    public String getLocalidade() {
        return super.getArea();
    }

    @Deprecated
    public String getLocalidadeCodigoPostal() {
        return super.getAreaOfAreaCode();
    }

    @Deprecated
    public String getMorada() {
        return super.getAddress();
    }

    @Deprecated
    public Date getNascimento() {
        return super.getDateOfBirth();
    }

    @Deprecated
    public String getNomeMae() {
        return super.getNameOfMother();
    }

    @Deprecated
    public String getNomePai() {
        return super.getNameOfFather();
    }

    @Deprecated
    public String getNumContribuinte() {
        return super.getSocialSecurityNumber();
    }

    @Deprecated
    public String getNumeroDocumentoIdentificacao() {
        return super.getDocumentIdNumber();
    }

    @Deprecated
    public String getProfissao() {
        return super.getProfession();
    }

    @Deprecated
    public String getTelefone() {
        return super.getPhone();
    }

    @Deprecated
    public String getTelemovel() {
        return super.getMobile();
    }

    @Deprecated
    public void setCodigoFiscal(String codigoFiscal) {
        super.setFiscalCode(codigoFiscal);
    }

    @Deprecated
    public void setCodigoPostal(String codigoPostal) {
        super.setAreaCode(codigoPostal);
    }

    @Deprecated
    public void setConcelhoMorada(String concelhoMorada) {
        super.setDistrictSubdivisionOfResidence(concelhoMorada);
    }

    @Deprecated
    public void setConcelhoNaturalidade(String concelhoNaturalidade) {
        super.setDistrictSubdivisionOfBirth(concelhoNaturalidade);
    }

    @Deprecated
    public void setDataEmissaoDocumentoIdentificacao(Date dataEmissaoDocumentoIdentificacao) {
        super.setEmissionDateOfDocumentId(dataEmissaoDocumentoIdentificacao);
    }

    @Deprecated
    public void setDataValidadeDocumentoIdentificacao(Date dataValidadeDocumentoIdentificacao) {
        super.setExpirationDateOfDocumentId(dataValidadeDocumentoIdentificacao);
    }

    @Deprecated
    public void setDistritoMorada(String distritoMorada) {
        super.setDistrictOfResidence(distritoMorada);
    }

    @Deprecated
    public void setDistritoNaturalidade(String distritoNaturalidade) {
        super.setDistrictOfBirth(distritoNaturalidade);
    }

    @Deprecated
    public void setEnderecoWeb(String enderecoWeb) {
        super.setWebAddress(enderecoWeb);
    }

    @Deprecated
    public void setFreguesiaMorada(String freguesiaMorada) {
        super.setParishOfResidence(freguesiaMorada);
    }

    @Deprecated
    public void setFreguesiaNaturalidade(String freguesiaNaturalidade) {
        super.setParishOfBirth(freguesiaNaturalidade);
    }

    @Deprecated
    public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao) {
        super.setEmissionLocationOfDocumentId(localEmissaoDocumentoIdentificacao);
    }

    @Deprecated
    public void setLocalidade(String localidade) {
        super.setArea(localidade);
    }

    @Deprecated
    public void setLocalidadeCodigoPostal(String localidadeCodigoPostal) {
        super.setAreaOfAreaCode(localidadeCodigoPostal);
    }

    @Deprecated
    public void setMorada(String morada) {
        super.setAddress(morada);
    }

    @Deprecated
    public void setNascimento(Date nascimento) {
        super.setDateOfBirth(nascimento);
    }

    @Deprecated
    public void setNomeMae(String nomeMae) {
        super.setNameOfMother(nomeMae);
    }

    @Deprecated
    public void setNomePai(String nomePai) {
        super.setNameOfFather(nomePai);
    }

    @Deprecated
    public void setNumContribuinte(String numContribuinte) {
        this.setSocialSecurityNumber(numContribuinte);
    }

    @Deprecated
    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
        super.setDocumentIdNumber(numeroDocumentoIdentificacao);
    }

    @Deprecated
    public void setProfissao(String profissao) {
        super.setProfession(profissao);
    }

    @Deprecated
    public void setTelefone(String telefone) {
        super.setPhone(telefone);
    }

    @Deprecated
    public void setTelemovel(String telemovel) {
        super.setMobile(telemovel);
    }
    
    @Override
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        if (Party.readByContributorNumber(socialSecurityNumber) != null) {
            throw new DomainException("PERSON.createContributor.existing.contributor.number");
        }
        super.setSocialSecurityNumber(socialSecurityNumber);
    }
    

    // -------------------------------------------------------------
    // static methods
    // -------------------------------------------------------------

    public static boolean checkIfDocumentNumberIdAndDocumentIdTypeExists(final String documentIDNumber,
            final IDDocumentType documentType, Person thisPerson) {

        for (final Person person : Person.readAllPersons()) {
            if (!person.equals(thisPerson) && person.getDocumentIdNumber().equals(documentIDNumber)
                    && person.getIdDocumentType().equals(documentType)) {
                return true;
            }
        }
        return false;
    }

    public static Person readPersonByUsername(final String username) {
        final Login login = Login.readLoginByUsername(username);
        User user = null;
        if (login != null) {
            user = login.getUser();
        }
        return (user != null) ? user.getPerson() : null;
    }

    public static Person readPersonByIstUsername(final String istUsername) {
        final User user = User.readUserByUserUId(istUsername);
        return (user != null) ? user.getPerson() : null;
    }

    public static Collection<Person> readByDocumentIdNumber(final String documentIdNumber) {
        Collection<Person> result = new ArrayList<Person>();
        for (final Person person : Person.readAllPersons()) {
            if (person.getDocumentIdNumber().equalsIgnoreCase(documentIdNumber)) {
                result.add(person);
            }
        }
        return result;
    }

    public static Person readByDocumentIdNumberAndIdDocumentType(final String documentIdNumber,
            final IDDocumentType idDocumentType) {
        for (final Person person : Person.readAllPersons()) {
            if (person.getDocumentIdNumber().equalsIgnoreCase(documentIdNumber)
                    && person.getIdDocumentType() == idDocumentType) {
                return person;
            }
        }
        return null;
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
            for (final Person person : Person.readAllPersons()) {
                if (person.getName().toLowerCase().matches(nameToMatch)) {
                    result.add(person);
                }
            }
        }
        return result;
    }

    public static List<Person> readAllPersons() {
        List<Person> allPersons = new ArrayList<Person>();
        for (Party party : RootDomainObject.getInstance().getPartys()) {
            if (party instanceof Person) {
                allPersons.add((Person) party);
            }
        }
        return allPersons;
    }

    public SortedSet<StudentCurricularPlan> getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName() {
        final SortedSet<StudentCurricularPlan> studentCurricularPlans = new TreeSet<StudentCurricularPlan>(
                StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME);
        for (final Student student : getStudentsSet()) {
            final StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
            if (studentCurricularPlan != null) {
                studentCurricularPlans.add(studentCurricularPlan);
            }
        }
        return studentCurricularPlans;
    }

    public SortedSet<StudentCurricularPlan> getCompletedStudentCurricularPlansSortedByDegreeTypeAndDegreeName() {
        final SortedSet<StudentCurricularPlan> studentCurricularPlans = new TreeSet<StudentCurricularPlan>(
                StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME);
        for (final Student student : getStudentsSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : student
                    .getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.CONCLUDED) {
                    studentCurricularPlans.add(studentCurricularPlan);
                }
            }
        }
        return studentCurricularPlans;
    }

    public List<ProjectAccess> readProjectAccessesByCoordinator(Integer coordinatorCode) {
        List<ProjectAccess> result = new ArrayList<ProjectAccess>();
        Date currentDate = Calendar.getInstance().getTime();
        for (ProjectAccess projectAccess : getProjectAccessesSet()) {
            if (projectAccess.getKeyProjectCoordinator().equals(coordinatorCode)) {
                if (!DateFormatUtil.isBefore("yyyy/MM/dd", currentDate, projectAccess.getBegin())
                        && !DateFormatUtil.isAfter("yyyy/MM/dd", currentDate, projectAccess.getEnd())) {
                    result.add(projectAccess);
                }
            }
        }
        return result;
    }

    public Set<Attends> getCurrentAttends() {
        final Set<Attends> attends = new HashSet<Attends>();
        for (final Student student : getStudentsSet()) {
            for (final Attends attend : student.getAssociatedAttendsSet()) {
                final ExecutionCourse executionCourse = attend.getDisciplinaExecucao();
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
            this.setIstUsername(UsernameUtils.updateIstUsername(this));
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
        for (final Student student : getStudents()) {
            if (student.getNumber().equals(institutionalNumber)) {
                return true;
            }
        }
        return false;
    }

    public Set<Event> getNotPayedEvents() {
        return getNotPayedEventsPayableOnAdministrativeOffice(null);
    }

    public Set<Event> getNotPayedEventsPayableOnAdministrativeOffice(
            AdministrativeOffice administrativeOffice) {
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

    public Set<Entry> getPaymentsWithoutReceipt() {
        return getPaymentsWithoutReceiptByAdministrativeOffice(null);
    }

    public Set<Entry> getPaymentsWithoutReceiptByAdministrativeOffice(
            AdministrativeOffice administrativeOffice) {
        final Set<Entry> result = new HashSet<Entry>();

        for (final Event event : getEventsSet()) {
            if (isPayableOnAdministrativeOffice(administrativeOffice, event)) {
                result.addAll(event.getEntriesWithoutReceipt());
            }
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

    public static Party createContributor(String contributorName, String contributorNumber,
            String contributorAddress, String areaCode, String areaOfAreaCode, String area,
            String parishOfResidence, String districtSubdivisionOfResidence, String districtOfResidence) {
        
        ExternalPerson externalPerson = new ExternalPerson(
                contributorName, 
                Gender.MALE, 
                contributorAddress,
                areaCode, 
                areaOfAreaCode, 
                area, 
                parishOfResidence, 
                districtSubdivisionOfResidence, 
                districtOfResidence, 
                null, 
                null, 
                null, 
                null, 
                String.valueOf(System.currentTimeMillis()), 
                null);
        externalPerson.getPerson().setSocialSecurityNumber(contributorNumber);
        
        return externalPerson.getPerson();
    }

}
