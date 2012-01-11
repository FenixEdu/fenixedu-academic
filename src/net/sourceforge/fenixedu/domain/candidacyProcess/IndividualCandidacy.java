package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacy;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {

    protected IndividualCandidacy() {
	super();
	super.setWhenCreated(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());

    }

    protected Person init(final IndividualCandidacyProcessBean bean, final IndividualCandidacyProcess process) {
	/*
	 * 31/03/2009 - Now the person may be created inside init() method
	 * 
	 * 06/04/2009 - All subclasses share the code below. So the
	 * checkParameters() is now abstract
	 */

	/*
	 * 08/05/2009 - Now all candidacies are external (even made in academic
	 * administrative office)
	 */
	/*
	 * 06/07/2009 - Due to payments the applications will be created with an
	 * associated person. This person might be created or associated with an
	 * existing.
	 */
	Person person = null;
	if (bean.getInternalPersonCandidacy().booleanValue()) {
	    person = bean.getOrCreatePersonFromBean();
	}

	checkParameters(person, process, bean);
	bean.getPersonBean().setPerson(person);

	IndividualCandidacyPersonalDetails.createDetails(this, bean);
	setCandidacyProcess(process);
	setCandidacyDate(bean.getCandidacyDate());
	setState(IndividualCandidacyState.STAND_BY);
	editObservations(bean);
	setUtlStudent(bean.getUtlStudent());

	return person;
    }

    /**
     * 06/04/2009 All subclasses of IndividualCandidacy call a checkParameters()
     * in their constructor. The arguments of checkParameters varies from
     * subclass to subclass but they come from Person,
     * IndividualCandidacyProcess and IndividualCandidacyProcessBean
     * 
     * @param person
     * @param process
     * @param bean
     */
    protected abstract void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean);

    protected void checkParameters(final Person person, final IndividualCandidacyProcess process, final LocalDate candidacyDate) {
	/*
	 * 31/03/2009 - The candidacy will not be associated with a Person if it
	 * is submited externally (not in administrative office)
	 * 
	 * 
	 * if (person == null) { throw new
	 * DomainException("error.IndividualCandidacy.invalid.person"); }
	 */

	if (process == null) {
	    throw new DomainException("error.IndividualCandidacy.invalid.process");
	}
	if (candidacyDate == null || !process.hasOpenCandidacyPeriod(candidacyDate.toDateTimeAtCurrentTime())) {
	    throw new DomainException("error.IndividualCandidacy.invalid.candidacyDate", process.getCandidacyStart().toString(
		    DateFormatUtil.DEFAULT_DATE_FORMAT), process.getCandidacyEnd().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
	}
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.IndividualCandidacy.cannot.modify.when.created");
    }

    public boolean hasAnyPayment() {
	return hasEvent() && getEvent().hasAnyPayments();
    }

    public void editPersonalCandidacyInformation(final PersonBean personBean) {
	getPersonalDetails().edit(personBean);
    }

    public void editPersonalCandidacyInformationPublic(final PersonBean personBean) {
	getPersonalDetails().editPublic(personBean);
    }

    public void cancel(final Person person) {
	checkRulesToCancel();
	setState(IndividualCandidacyState.CANCELLED);
	setResponsible(person.getUsername());
	if (hasEvent()) {
	    getEvent().cancel("IndividualCandidacy.canceled");
	}
    }

    public void reject(final Person person) {
	setState(IndividualCandidacyState.REJECTED);
	setResponsible(person.getUsername());
    }

    public void revertToStandBy(final Person person) {
	setState(IndividualCandidacyState.STAND_BY);
	setResponsible(person.getUsername());
    }

    protected void checkRulesToCancel() {
	if (hasEvent() && hasAnyPayment()) {
	    throw new DomainException("error.IndividualCandidacy.cannot.cancel.candidacy.with.payments");
	}
    }

    public Person getResponsiblePerson() {
	return Person.readPersonByUsername(getResponsible());
    }

    public boolean isInStandBy() {
	return getState() == IndividualCandidacyState.STAND_BY;
    }

    public boolean isAccepted() {
	return getState() == IndividualCandidacyState.ACCEPTED;
    }

    public boolean isCancelled() {
	return getState() == IndividualCandidacyState.CANCELLED;
    }

    public boolean isRejected() {
	return getState() == IndividualCandidacyState.REJECTED;
    }

    public boolean isDebtPayed() {
	return !hasEvent() || (hasEvent() && getEvent().isClosed());
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
	return hasCandidacyProcess() && getCandidacyProcess().isFor(executionInterval);
    }

    protected boolean isCandidacyResultStateValid(final IndividualCandidacyState state) {
	return state == IndividualCandidacyState.ACCEPTED || state == IndividualCandidacyState.REJECTED;
    }

    protected void createPrecedentDegreeInformation(final IndividualCandidacyProcessWithPrecedentDegreeInformationBean processBean) {
	final CandidacyPrecedentDegreeInformationBean bean = processBean.getPrecedentDegreeInformation();
	/*
	 * 31/03/2009 - The candidacy may be submited in a public area (by a
	 * possible student) and in that case the candidacy may not be
	 * associated with a student which may be a person. In the case above
	 * the precedent degree information will be external even if the
	 * candidate has a degree of this institution
	 */
	if (processBean.isExternalPrecedentDegreeType() || !processBean.getInternalPersonCandidacy()) {
	    createExternalPrecedentDegreeInformation(bean);
	} else {
	    final StudentCurricularPlan studentCurricularPlan = processBean.getPrecedentStudentCurricularPlan();
	    if (studentCurricularPlan == null) {
		throw new DomainException("error.IndividualCandidacy.invalid.precedentDegreeInformation");
	    }
	    createInstitutionPrecedentDegreeInformation(studentCurricularPlan);
	}
    }

    private Unit getOrCreateInstitution(final CandidacyPrecedentDegreeInformationBean bean) {
	if (bean.getInstitution() != null) {
	    return bean.getInstitution();
	}

	if (bean.getInstitutionName() == null || bean.getInstitutionName().isEmpty()) {
	    throw new DomainException("error.ExternalPrecedentDegreeCandidacy.invalid.institution.name");
	}

	final Unit unit = Unit.findFirstExternalUnitByName(bean.getInstitutionName());
	return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(bean.getInstitutionName());
    }

    protected ExternalPrecedentDegreeInformation createExternalPrecedentDegreeInformation(
	    final CandidacyPrecedentDegreeInformationBean bean) {
	return new ExternalPrecedentDegreeInformation(this, bean.getDegreeDesignation(), bean.getConclusionDate(),
		getOrCreateInstitution(bean), bean.getConclusionGrade(), bean.getCountry());
    }

    protected void createInstitutionPrecedentDegreeInformation(final StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.isBolonhaDegree()) {
	    final CycleType cycleType;
	    if (studentCurricularPlan.hasConcludedAnyInternalCycle()) {
		cycleType = studentCurricularPlan.getLastConcludedCycleCurriculumGroup().getCycleType();
	    } else {
		cycleType = studentCurricularPlan.getLastOrderedCycleCurriculumGroup().getCycleType();
	    }
	    new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan, cycleType);
	} else {
	    new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan);
	}
    }

    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration",
		    degreeCurricularPlan.getPresentationName());
	}

	if (hasActiveRegistration(degreeCurricularPlan)) {
	    final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
	    setRegistration(registration);
	    return registration;
	}

	getPersonalDetails().ensurePersonInternalization();
	return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
	    final CycleType cycleType, final Ingression ingression) {

	final Registration registration = new Registration(person, degreeCurricularPlan, cycleType);
	registration.setEntryPhase(EntryPhase.FIRST_PHASE);
	registration.setIngression(ingression);
	registration.editStartDates(getStartDate(), registration.getHomologationDate(), registration.getStudiesStartDate());
	setRegistration(registration);

	// person.addPersonRoleByRoleType(RoleType.PERSON);
	// person.addPersonRoleByRoleType(RoleType.STUDENT);

	return registration;
    }

    protected boolean hasActiveRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
	return getPersonalDetails().hasStudent()
		&& getPersonalDetails().getStudent().hasActiveRegistrationFor(degreeCurricularPlan);
    }

    protected YearMonthDay getStartDate() {
	final ExecutionInterval interval = getCandidacyExecutionInterval();
	return interval.isCurrent() ? new YearMonthDay() : interval.getBeginDateYearMonthDay();
    }

    public Student getStudent() {
	return getPersonalDetails().getStudent();
    }

    public boolean hasStudent() {
	return getStudent() != null;
    }

    protected ExecutionInterval getCandidacyExecutionInterval() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    protected boolean personHasDegree(final Person person, final Degree selectedDegree) {

	if (!person.hasStudent()) {
	    return false;
	}

	List<Registration> registrationsFor = getNotCanceledRegistrationsFor(person, selectedDegree);
	if (registrationsFor.isEmpty()) {
	    return false;
	}

	if ((registrationsFor.size() == 1) && registrationsFor.get(0) == getRegistration()) {
	    return false;
	}

	return true;
    }

    private List<Registration> getNotCanceledRegistrationsFor(final Person person, final Degree selectedDegree) {
	List<Registration> registrationsFor = person.getStudent().getRegistrationsFor(selectedDegree);
	List<Registration> notCanceledRegistrationsForDegree = new ArrayList<Registration>();

	for (Registration registration : registrationsFor) {
	    if (registration.isCanceled()) {
		continue;
	    }

	    notCanceledRegistrationsForDegree.add(registration);
	}

	return notCanceledRegistrationsForDegree;
    }

    protected boolean personHasOneOfDegrees(final Person person, final Set<Degree> selectedDegrees) {
	if (!person.hasStudent()) {
	    return false;
	}

	for (Degree degree : selectedDegrees) {
	    if (person.getStudent().hasActiveRegistrationFor(degree)) {
		return true;
	    }
	}

	return false;
    }

    public CandidacyInformationBean getCandidacyInformationBean() {
	final CandidacyInformationBean bean = new CandidacyInformationBean();

	bean.setRegistration(getRegistration());
	bean.setIndividualCandidacy(this);

	bean.setCountryOfResidence(getCountryOfResidence());
	bean.setDistrictSubdivisionOfResidence(getDistrictSubdivisionOfResidence());
	bean.setSchoolTimeDistrictSubdivisionOfResidence(getSchoolTimeDistrictSubDivisionOfResidence());
	bean.setDislocatedFromPermanentResidence(getDislocatedFromPermanentResidence());

	bean.setGrantOwnerType(getGrantOwnerType());
	bean.setGrantOwnerProvider(getGrantOwnerProvider());
	bean.setHighSchoolType(getHighSchoolType());
	bean.setMaritalStatus(getMaritalStatus());
	bean.setProfessionType(getProfessionType());
	bean.setProfessionalCondition(getProfessionalCondition());

	bean.setMotherSchoolLevel(getMotherSchoolLevel());
	bean.setMotherProfessionType(getMotherProfessionType());
	bean.setMotherProfessionalCondition(getMotherProfessionalCondition());

	bean.setFatherSchoolLevel(getFatherSchoolLevel());
	bean.setFatherProfessionType(getFatherProfessionType());
	bean.setFatherProfessionalCondition(getFatherProfessionalCondition());

	if (hasPrecedentDegreeInformation()) {
	    getPrecedentDegreeInformation().fill(bean);
	}

	for (final IndividualCandidacyDocumentFile document : getDocuments()) {
	    bean.addDocumentFile(document);
	}

	return bean;
    }

    public void editCandidacyInformation(final CandidacyInformationBean bean) {
	editMainCandidacyInformation(bean);

	if (!this.getClass().equals(StandaloneIndividualCandidacy.class))
	    editPrecedentDegreeInformation(bean);
    }

    public void editMissingCandidacyInformation(final CandidacyInformationBean bean) {
	editMainCandidacyInformation(bean);
	editMissingPrecedentDegreeInformation(bean);
    }

    private void editPrecedentDegreeInformation(final CandidacyInformationBean bean) {
	if (!hasPrecedentDegreeInformation()) {
	    new ExternalPrecedentDegreeInformation().setCandidacy(this);
	}
	getPrecedentDegreeInformation().edit(bean);
    }

    private void editMissingPrecedentDegreeInformation(final CandidacyInformationBean bean) {
	if (!hasPrecedentDegreeInformation()) {
	    new ExternalPrecedentDegreeInformation().setCandidacy(this);
	}
	getPrecedentDegreeInformation().editMissingInformation(bean);
    }

    public void editObservations(final IndividualCandidacyProcessBean bean) {
	this.setObservations(bean.getObservations());
    }

    private void editMainCandidacyInformation(final CandidacyInformationBean bean) {
	setCountryOfResidence(bean.getCountryOfResidence());
	setDistrictSubdivisionOfResidence(bean.getDistrictSubdivisionOfResidence());
	setSchoolTimeDistrictSubDivisionOfResidence(bean.getSchoolTimeDistrictSubdivisionOfResidence());
	setDislocatedFromPermanentResidence(bean.getDislocatedFromPermanentResidence());

	setGrantOwnerType(bean.getGrantOwnerType());
	setGrantOwnerProvider(bean.getGrantOwnerProvider());
	setHighSchoolType(bean.getHighSchoolType());
	setMaritalStatus(bean.getMaritalStatus());
	setProfessionType(bean.getProfessionType());
	setProfessionalCondition(bean.getProfessionalCondition());

	setMotherSchoolLevel(bean.getMotherSchoolLevel());
	setMotherProfessionType(bean.getMotherProfessionType());
	setMotherProfessionalCondition(bean.getMotherProfessionalCondition());

	setFatherSchoolLevel(bean.getFatherSchoolLevel());
	setFatherProfessionType(bean.getFatherProfessionType());
	setFatherProfessionalCondition(bean.getFatherProfessionalCondition());
    }

    public Boolean isCandidacyInternal() {
	return this.getPersonalDetails() instanceof IndividualCandidacyInternalPersonDetails;
    }

    protected abstract void createDebt(final Person person);

    public void bindPerson(ChoosePersonBean bean) {
	if (this.isCandidacyInternal()) {
	    throw new DomainException("error.bind.candidacy.internal");
	}

	Person selectedPerson = bean.getPerson();
	if (selectedPerson != null) {
	    if (!selectedPerson.hasRole(RoleType.EMPLOYEE)) {
		selectedPerson.edit(this.getPersonalDetails());
	    }
	    this.setPersonalDetails(new IndividualCandidacyInternalPersonDetails(this, selectedPerson));
	} else {
	    selectedPerson = new Person(this.getPersonalDetails());
	    this.setPersonalDetails(new IndividualCandidacyInternalPersonDetails(this, selectedPerson));
	}

	createDebt(this.getPersonalDetails().getPerson());
    }

    protected void createFormationEntries(List<FormationBean> formationConcludedBeanList,
	    List<FormationBean> formationNonConcludedBeanList) {
	for (FormationBean formation : formationConcludedBeanList) {
	    this.addFormations(new Formation(this, formation));
	}

	for (FormationBean formation : formationNonConcludedBeanList) {
	    this.addFormations(new Formation(this, formation));
	}
    }

    public List<Formation> getConcludedFormationList() {
	return new ArrayList<Formation>(CollectionUtils.select(getFormations(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((Formation) arg0).getConcluded();
	    }

	}));
    }

    public List<Formation> getNonConcludedFormationList() {
	return new ArrayList<Formation>(CollectionUtils.select(getFormations(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return !((Formation) arg0).getConcluded();
	    }

	}));
    }

    public Boolean isCandidacyBelongsToStudent() {
	return this.isCandidacyInternal() && this.getPersonalDetails().getPerson().hasRole(RoleType.STUDENT);
    }

    public Boolean isCandidacyBelongsToEmployee() {
	return this.isCandidacyInternal() && this.getPersonalDetails().getPerson().hasRole(RoleType.EMPLOYEE);
    }

    public void editFormationEntries(List<FormationBean> formationConcludedBeanList,
	    List<FormationBean> formationNonConcludedBeanList) {
	List<Formation> formationsToBeRemovedList = new ArrayList<Formation>();
	for (final Formation formation : this.getFormations()) {
	    if (formation.getConcluded())
		editFormationEntry(formationConcludedBeanList, formationsToBeRemovedList, formation);
	}

	for (final Formation formation : this.getFormations()) {
	    if (!formation.getConcluded())
		editFormationEntry(formationNonConcludedBeanList, formationsToBeRemovedList, formation);
	}

	for (Formation formation : formationsToBeRemovedList) {
	    this.getFormations().remove(formation);
	    formation.delete();
	}

	for (FormationBean bean : formationConcludedBeanList) {
	    if (bean.getFormation() == null)
		this.addFormations(new Formation(this, bean));
	}

	for (FormationBean bean : formationNonConcludedBeanList) {
	    if (bean.getFormation() == null)
		this.addFormations(new Formation(this, bean));
	}
    }

    private void editFormationEntry(List<FormationBean> formationConcludedBeanList, List<Formation> formationsToBeRemovedList,
	    final Formation formation) {
	FormationBean bean = (FormationBean) CollectionUtils.find(formationConcludedBeanList, new Predicate() {
	    @Override
	    public boolean evaluate(Object arg0) {
		FormationBean bean = (FormationBean) arg0;
		return bean.getFormation() == formation;
	    }
	});

	if (bean == null) {
	    formationsToBeRemovedList.add(formation);
	} else {
	    formation.edit(bean);
	}
    }

    public void exportValues(final StringBuilder result) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());

	Formatter formatter = new Formatter(result);

	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.candidacy"), getCandidacyExecutionInterval()
		.getName());
	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.state"), getState().getLocalizedName());
	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.whenCreated"),
		getWhenCreated().toString("yyy-MM-dd"));
	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.candidacyDate"), getCandidacyDate().toString());
	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.responsible"),
		StringUtils.isEmpty(getResponsible()) ? StringUtils.EMPTY : getResponsible());
	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.notes"),
		StringUtils.isEmpty(getNotes()) ? StringUtils.EMPTY : getNotes());

	formatter.format("%s: %s\n", bundle.getString("label.IndividualCandidacy.observations"),
		StringUtils.isEmpty(getObservations()) ? StringUtils.EMPTY : getObservations());

	for (final Formation formation : getFormations()) {
	    formation.exportValues(result);
	}

    }

    abstract public String getDescription();

    public boolean isDegreeCandidacyForGraduatedPerson() {
	return false;
    }

    public boolean isDegreeChange() {
	return false;
    }

    public boolean isDegreeTransfer() {
	return false;
    }

    public boolean isErasmus() {
	return false;
    }

    public boolean isOver23() {
	return false;
    }

    public boolean isSecondCycle() {
	return false;
    }

    public boolean isStandalone() {
	return false;
    }

}
