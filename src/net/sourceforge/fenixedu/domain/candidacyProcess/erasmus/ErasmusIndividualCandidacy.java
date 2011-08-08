package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ErasmusIndividualCandidacy extends ErasmusIndividualCandidacy_Base {

    public ErasmusIndividualCandidacy() {
	super();
    }

    ErasmusIndividualCandidacy(final ErasmusIndividualCandidacyProcess process, final ErasmusIndividualCandidacyProcessBean bean) {
	this();

	bean.getPersonBean().setCreateLoginIdentificationAndUserIfNecessary(false);

	Person person = init(bean, process);

	createEramusStudentData(bean);

	associateCurricularCourses(bean.getSelectedCurricularCourses());
    }

    private void associateCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
	for (CurricularCourse curricularCourse : selectedCurricularCourses) {
	    addCurricularCourses(curricularCourse);
	}
    }

    private void createEramusStudentData(ErasmusIndividualCandidacyProcessBean bean) {
	setErasmusStudentData(new ErasmusStudentData(this, bean.getErasmusStudentDataBean(), bean.calculateErasmusVacancy()));
    }

    @Override
    protected void createDebt(final Person person) {

    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	ErasmusIndividualCandidacyProcess erasmusIndividualCandidacyProcess = (ErasmusIndividualCandidacyProcess) process;
	ErasmusIndividualCandidacyProcessBean secondCandidacyProcessBean = (ErasmusIndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();

	checkParameters(person, erasmusIndividualCandidacyProcess, candidacyDate, null);
    }

    private void checkParameters(final Person person, final ErasmusIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, Object dummy) {

	checkParameters(person, process, candidacyDate);

	/*
	 * 31/03/2009 - The candidacy may be submited externally hence may not
	 * be associated to a person
	 * 
	 * 
	 * if(person.hasValidSecondCycleIndividualCandidacy(process.
	 * getCandidacyExecutionInterval())) { throw newDomainException(
	 * "error.SecondCycleIndividualCandidacy.person.already.has.candidacy",
	 * process .getCandidacyExecutionInterval().getName()); }
	 */
    }

    void editDegreeAndCoursesInformation(ErasmusIndividualCandidacyProcessBean bean) {
	Set<CurricularCourse> setOne = new HashSet<CurricularCourse>(this.getCurricularCourses());
	setOne.addAll(bean.getSelectedCurricularCourses());

	getErasmusStudentData().setSelectedVacancy(bean.calculateErasmusVacancy());

	for (CurricularCourse curricularCourse : setOne) {
	    if (hasCurricularCourses(curricularCourse) && !bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		removeCurricularCourses(curricularCourse);
	    } else if (!hasCurricularCourses(curricularCourse) && bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		addCurricularCourses(curricularCourse);
	    }
	}
    }

    public Degree getSelectedDegree() {
	return getErasmusStudentData().getSelectedVacancy().getDegree();
    }

    protected boolean hasSelectedDegree() {
	return getSelectedDegree() != null;
    }

    @Override
    public String getDescription() {
	return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public ErasmusIndividualCandidacyProcess getCandidacyProcess() {
	return (ErasmusIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    public ApprovedLearningAgreementDocumentFile getMostRecentApprovedLearningAgreement() {
	if (!hasAnyActiveApprovedLearningAgreements()) {
	    return null;
	}

	List<ApprovedLearningAgreementDocumentFile> approvedLearningAgreement = new ArrayList<ApprovedLearningAgreementDocumentFile>(
		getActiveApprovedLearningAgreements());

	Collections.sort(approvedLearningAgreement, Collections
		.reverseOrder(ApprovedLearningAgreementDocumentFile.SUBMISSION_DATE_COMPARATOR));

	return approvedLearningAgreement.get(0);
    }

    public boolean isMostRecentApprovedLearningAgreementNotViewed() {
	if (!hasAnyActiveApprovedLearningAgreements()) {
	    return false;
	}

	return !getMostRecentApprovedLearningAgreement().isApprovedLearningAgreementViewed();
    }

    boolean hasProcessWithAcceptNotification() {
	return hasProcessWithAcceptNotificationAtDate(new DateTime());
    }

    boolean hasProcessWithAcceptNotificationAtDate(final DateTime dateTime) {
	return getMostRecentApprovedLearningAgreement().getMostRecentSentEmailAcceptedStudentAction() != null
		&& getMostRecentApprovedLearningAgreement().getMostRecentSentEmailAcceptedStudentAction().getWhenOccured()
			.isBefore(dateTime);
    }

    public List<ApprovedLearningAgreementDocumentFile> getActiveApprovedLearningAgreements() {
	List<ApprovedLearningAgreementDocumentFile> activeDocuments = new ArrayList<ApprovedLearningAgreementDocumentFile>();
	CollectionUtils.select(getApprovedLearningAgreements(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		ApprovedLearningAgreementDocumentFile document = (ApprovedLearningAgreementDocumentFile) arg0;
		return document.getCandidacyFileActive();
	    }

	}, activeDocuments);

	return activeDocuments;
    }

    public boolean hasAnyActiveApprovedLearningAgreements() {
	return !getActiveApprovedLearningAgreements().isEmpty();
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration", degreeCurricularPlan
		    .getPresentationName());
	}

	if (hasActiveRegistration(degreeCurricularPlan)) {
	    final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
	    setRegistration(registration);
	    return registration;
	}

	getPersonalDetails().ensurePersonInternalization();
	return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    @Override
    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
	    final CycleType cycleType, final Ingression ingression) {

	final Registration registration = new Registration(person, degreeCurricularPlan, RegistrationAgreement.ERASMUS,
		cycleType, ((ExecutionYear) getCandidacyExecutionInterval()));

	registration.editStartDates(getStartDate(), registration.getHomologationDate(), registration.getStudiesStartDate());
	setRegistration(registration);

	person.addPersonRoleByRoleType(RoleType.PERSON);
	person.addPersonRoleByRoleType(RoleType.STUDENT);

	return registration;
    }

    void enrol() {
	final Registration registration = getRegistration();
	final ExecutionYear executionYear = (ExecutionYear) getCandidacyExecutionInterval();
	final ExecutionSemester semesterToEnrol = executionYear.getFirstExecutionPeriod();

	Set<IDegreeModuleToEvaluate> degreeModulesToEnrol = new HashSet<IDegreeModuleToEvaluate>();
	degreeModulesToEnrol.addAll(getModulesToEnrolForFirstSemester());

	registration.getActiveStudentCurricularPlan().enrol(semesterToEnrol, degreeModulesToEnrol, Collections.EMPTY_LIST,
		CurricularRuleLevel.ENROLMENT_NO_RULES);
    }

    public Collection<DegreeModuleToEnrol> getModulesToEnrolForFirstSemester() {
	final Registration registration = getRegistration();
	final ExecutionYear executionYear = (ExecutionYear) getCandidacyExecutionInterval();
	final ExecutionSemester semesterToEnrol = executionYear.getFirstExecutionPeriod();
	final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
	final DegreeCurricularPlan degreeCurricularPlan = registration.getLastDegreeCurricularPlan();

	Set<DegreeModuleToEnrol> degreeModulesToEnrol = new HashSet<DegreeModuleToEnrol>();

	for (CurricularCourse selectedCurricularCourse : getCurricularCourses()) {
	    List<Context> contextList = selectedCurricularCourse.getParentContextsByExecutionSemester(semesterToEnrol);

	    if (contextList.isEmpty()) {
		continue;
	    }

	    Context selectedContext = contextList.get(0);

	    CurriculumGroup curriculumGroup = null;
	    if (selectedCurricularCourse.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
		curriculumGroup = studentCurricularPlan.getRoot().findCurriculumGroupFor(selectedContext.getParentCourseGroup());
	    } else {
		// Enrol on extra curriculum group
		curriculumGroup = studentCurricularPlan.getExtraCurriculumGroup();
	    }

	    if (curriculumGroup == null) {
		continue;
	    }

	    DegreeModuleToEnrol toEnrol = new DegreeModuleToEnrol(curriculumGroup, selectedContext, semesterToEnrol);
	    degreeModulesToEnrol.add(toEnrol);
	}

	return degreeModulesToEnrol;
    }

    public void answerNationalIdCardAvoidanceOnSubmission(ErasmusIndividualCandidacyProcessBean bean) {
	NationalIdCardAvoidanceQuestion question = bean.getNationalIdCardAvoidanceQuestion();

	this.setNationalIdCardAvoidanceQuestion(question);
	this.setNationalIdCardAvoidanceAnswerDate(new DateTime());
	this.setIdCardAvoidanceOtherReason(bean.getIdCardAvoidanceOtherReason());
    }
}
