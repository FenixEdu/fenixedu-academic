package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultTeacher;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.OldPublicationType;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.PublicationType;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class Teacher extends Teacher_Base {

    public static final Comparator TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER = new Comparator<Teacher>() {

	public int compare(Teacher teacher1, Teacher teacher2) {
	    final int teacherNumberCompare = teacher1.getTeacherNumber().compareTo(
		    teacher2.getTeacherNumber());

	    if (teacher1.getCategory() == null && teacher2.getCategory() == null) {
		return teacherNumberCompare;
	    } else if (teacher1.getCategory() == null) {
		return 1;
	    } else if (teacher2.getCategory() == null) {
		return -1;
	    } else {
		final int categoryCompare = teacher1.getCategory().compareTo(teacher2.getCategory());
		return categoryCompare == 0 ? teacherNumberCompare : categoryCompare;
	    }
	}

    };

    public Teacher(Integer teacherNumber, Person person) {
	super();
	checkParamaters(person, teacherNumber);
	setTeacherNumber(teacherNumber);
	setPerson(person);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    public void setTeacherNumber(Integer teacherNumber) {
	checkTeacherNumber(teacherNumber);
	super.setTeacherNumber(teacherNumber);
    }

    private void checkParamaters(Person person, Integer teacherNumber) {
	if (person == null) {
	    throw new DomainException("error.teacher.no.person");
	}
	if (teacherNumber == null) {
	    throw new DomainException("error.teacher.no.teacherNumber");
	}
    }

    private void checkTeacherNumber(Integer teacherNumber) {
	Teacher teacher = readByNumber(teacherNumber);
	if (teacher != null && !teacher.equals(this)) {
	    throw new DomainException("error.teacher.already.exists.one.teacher.with.same.number");
	}
    }

    /***********************************************************************
         * BUSINESS SERVICES *
         **********************************************************************/

    public void addToTeacherInformationSheet(ResearchResult result, PublicationArea publicationArea) {
	new ResultTeacher(result, this, publicationArea);
    }

    public void removeFromTeacherInformationSheet(ResearchResult result) {
	for (ResultTeacher resultTeacher : getTeacherResults()) {
	    if (resultTeacher.getResult().equals(result)) {
		resultTeacher.delete();
		return;
	    }
	}
    }

    public boolean canAddResultToTeacherInformationSheet(PublicationArea area) {
	/* method based on canAddPublicationToTeacherInformationSheet */
	int count = 0;
	for (ResultTeacher resultTeacher : getTeacherResults()) {
	    if (resultTeacher.getPublicationArea().equals(area)) {
		count++;
	    }
	}
	if (count < 5)
	    return true;
	else
	    return false;
    }

    public void addToTeacherInformationSheet(Publication publication, PublicationArea publicationArea) {
	new PublicationTeacher(publication, this, publicationArea);
    }

    public void removeFromTeacherInformationSheet(Publication publication) {
	Iterator<PublicationTeacher> iterator = getTeacherPublications().iterator();

	while (iterator.hasNext()) {
	    PublicationTeacher publicationTeacher = iterator.next();
	    if (publicationTeacher.getPublication().equals(publication)) {
		iterator.remove();
		publicationTeacher.delete();
		return;
	    }
	}
    }

    public Boolean canAddPublicationToTeacherInformationSheet(PublicationArea area) {
	// NOTA : a linha seguinte cont???m um n???mero expl???cito quando n???o
	// deve.
	// Isto deve ser mudado! Mas esta mudan???a implica tornar expl???cito o
	// conceito de Ficha de docente.
	return new Boolean(countPublicationsInArea(area) < 5);

    }

    public List<Professorship> responsibleFors() {
	final List<Professorship> result = new ArrayList<Professorship>();
	for (final Professorship professorship : this.getProfessorships()) {
	    if (professorship.isResponsibleFor())
		result.add(professorship);
	}
	return result;
    }

    public Professorship isResponsibleFor(ExecutionCourse executionCourse) {
	for (final Professorship professorship : this.getProfessorships()) {
	    if (professorship.getResponsibleFor()
		    && professorship.getExecutionCourse() == executionCourse) {
		return professorship;
	    }
	}
	return null;
    }

    public void updateResponsabilitiesFor(Integer executionYearId, List<Integer> executionCourses)
	    throws MaxResponsibleForExceed, InvalidCategory {

	if (executionYearId == null || executionCourses == null)
	    throw new NullPointerException();

	boolean responsible;
	for (final Professorship professorship : this.getProfessorships()) {
	    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
	    if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(
		    executionYearId)) {
		responsible = executionCourses.contains(executionCourse.getIdInternal());
		if (!professorship.getResponsibleFor().equals(Boolean.valueOf(responsible))) {
		    ResponsibleForValidator.getInstance().validateResponsibleForList(this,
			    executionCourse, professorship);
		    professorship.setResponsibleFor(responsible);
		}
	    }
	}
    }

    public Unit getCurrentWorkingUnit() {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getCurrentWorkingPlace() : null;
    }

    public Unit getLastWorkingUnit() {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getLastWorkingPlace() : null;
    }

    public Unit getLastWorkingUnit(YearMonthDay begin, YearMonthDay end) {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getLastWorkingPlace(begin, end) : null;
    }

    public Department getCurrentWorkingDepartment() {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getCurrentDepartmentWorkingPlace() : null;
    }

    public Department getLastWorkingDepartment(YearMonthDay begin, YearMonthDay end) {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getLastDepartmentWorkingPlace(begin, end) : null;
    }

    public Department getLastWorkingDepartment() {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getLastDepartmentWorkingPlace() : null;
    }

    public List<Unit> getWorkingPlacesByPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	Employee employee = this.getPerson().getEmployee();
	return (employee != null) ? employee.getWorkingPlaces(beginDate, endDate)
		: new ArrayList<Unit>();
    }

    public Category getCategory() {
	TeacherLegalRegimen regimen = getLastLegalRegimenWithoutEndSituations();
	return (regimen != null) ? regimen.getCategory() : null;
    }

    public TeacherLegalRegimen getCurrentLegalRegimenWithoutEndSitutions() {
	TeacherLegalRegimen lastLegalRegimen = getLastLegalRegimenWithoutEndSituations();
	return (lastLegalRegimen != null && lastLegalRegimen.isActive(new YearMonthDay())) ? lastLegalRegimen
		: null;
    }

    public TeacherLegalRegimen getLastLegalRegimenWithoutEndSituations() {
	SortedSet<TeacherLegalRegimen> legalRegimens = new TreeSet<TeacherLegalRegimen>(
		TeacherLegalRegimen.TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE);
	legalRegimens.addAll(getAllLegalRegimensWithoutEndSituations());
	return (!legalRegimens.isEmpty()) ? legalRegimens.last() : null;
    }

    public TeacherLegalRegimen getLastLegalRegimenWithoutEndSituations(YearMonthDay begin,
	    YearMonthDay end) {
	SortedSet<TeacherLegalRegimen> legalRegimens = new TreeSet<TeacherLegalRegimen>(
		TeacherLegalRegimen.TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE);

	legalRegimens.addAll(getAllLegalRegimensWithoutEndSituations(begin, end));
	return (!legalRegimens.isEmpty()) ? legalRegimens.last() : null;
    }

    public List<TeacherLegalRegimen> getAllLegalRegimensWithoutEndSituations(YearMonthDay beginDate,
	    YearMonthDay endDate) {
	Set<TeacherLegalRegimen> legalRegimens = new HashSet<TeacherLegalRegimen>();
	for (TeacherLegalRegimen legalRegimen : this.getLegalRegimens()) {
	    if (!legalRegimen.isEndSituation() && legalRegimen.belongsToPeriod(beginDate, endDate)) {
		legalRegimens.add(legalRegimen);
	    }
	}
	return new ArrayList<TeacherLegalRegimen>(legalRegimens);
    }

    public List<TeacherLegalRegimen> getAllLegalRegimensWithoutEndSituations() {
	Set<TeacherLegalRegimen> legalRegimens = new HashSet<TeacherLegalRegimen>();
	for (TeacherLegalRegimen legalRegimen : getLegalRegimens()) {
	    if (!legalRegimen.isEndSituation()) {
		legalRegimens.add(legalRegimen);
	    }
	}
	return new ArrayList<TeacherLegalRegimen>(legalRegimens);
    }

    public Category getLastCategory(YearMonthDay begin, YearMonthDay end) {
	TeacherLegalRegimen lastLegalRegimen = getLastLegalRegimenWithoutEndSituations(begin, end);
	return (lastLegalRegimen != null) ? lastLegalRegimen.getCategory() : null;
    }

    public TeacherPersonalExpectation getTeacherPersonalExpectationByExecutionYear(
	    ExecutionYear executionYear) {
	TeacherPersonalExpectation result = null;

	List<TeacherPersonalExpectation> teacherPersonalExpectations = this
		.getTeacherPersonalExpectations();

	for (TeacherPersonalExpectation teacherPersonalExpectation : teacherPersonalExpectations) {
	    if (teacherPersonalExpectation.getExecutionYear().equals(executionYear)) {
		result = teacherPersonalExpectation;
		break;
	    }
	}

	return result;
    }

    public List<Proposal> getFinalDegreeWorksByExecutionYear(ExecutionYear executionYear) {
	List<Proposal> proposalList = new ArrayList<Proposal>();
	for (Iterator iter = getAssociatedProposalsByOrientator().iterator(); iter.hasNext();) {
	    Proposal proposal = (Proposal) iter.next();
	    if (proposal.getScheduleing().getExecutionDegreesSet().iterator().next().getExecutionYear()
		    .equals(executionYear)) {
		// if it was attributed by the coordinator the proposal is
		// efective
		if (proposal.getGroupAttributed() != null) {
		    proposalList.add(proposal);
		}
		// if not, we have to verify if the teacher has proposed it to
		// any student(s) and if that(those) student(s) has(have)
		// accepted it
		else {
		    Group attributedGroupByTeacher = proposal.getGroupAttributedByTeacher();
		    if (attributedGroupByTeacher != null) {
			boolean toAdd = false;
			for (Iterator iterator = attributedGroupByTeacher.getGroupStudents().iterator(); iterator
				.hasNext();) {
			    GroupStudent groupStudent = (GroupStudent) iterator.next();
			    Proposal studentProposal = groupStudent
				    .getFinalDegreeWorkProposalConfirmation();
			    if (studentProposal != null && studentProposal.equals(proposal)) {
				toAdd = true;
			    } else {
				toAdd = false;
			    }
			}
			if (toAdd) {
			    proposalList.add(proposal);
			}
		    }
		}
	    }
	}
	return proposalList;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionYear(ExecutionYear executionYear) {
	List<ExecutionCourse> executionCourses = new ArrayList();
	for (Iterator iter = executionYear.getExecutionPeriods().iterator(); iter.hasNext();) {
	    ExecutionPeriod executionPeriod = (ExecutionPeriod) iter.next();
	    executionCourses.addAll(getLecturedExecutionCoursesByExecutionPeriod(executionPeriod));
	}
	return executionCourses;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {
	List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
	for (Iterator iter = getProfessorships().iterator(); iter.hasNext();) {
	    Professorship professorship = (Professorship) iter.next();
	    ExecutionCourse executionCourse = professorship.getExecutionCourse();

	    if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
		executionCourses.add(executionCourse);
	    }
	}
	return executionCourses;
    }

    public List<ExecutionCourse> getAllLecturedExecutionCourses() {
	List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

	for (Professorship professorship : this.getProfessorships()) {
	    executionCourses.add(professorship.getExecutionCourse());
	}

	return executionCourses;
    }

    public Double getHoursLecturedOnExecutionCourse(ExecutionCourse executionCourse) {
	double returnValue = 0;

	Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
	TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse
		.getExecutionPeriod());
	if (teacherService != null) {
	    List<DegreeTeachingService> teachingServices = teacherService
		    .getDegreeTeachingServiceByProfessorship(professorship);
	    for (DegreeTeachingService teachingService : teachingServices) {
		returnValue += ((teachingService.getPercentage() / 100) * teachingService.getShift()
			.hours());
	    }
	}
	return returnValue;
    }

    public TeacherService getTeacherServiceByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	return (TeacherService) CollectionUtils.find(getTeacherServices(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		TeacherService teacherService = (TeacherService) arg0;
		return teacherService.getExecutionPeriod() == executionPeriod;
	    }
	});
    }

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
	return (Professorship) CollectionUtils.find(getProfessorships(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		Professorship professorship = (Professorship) arg0;
		return professorship.getExecutionCourse() == executionCourse;
	    }
	});
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
	return (getProfessorshipByExecutionCourse(executionCourse) != null);
    }

    public List<Professorship> getDegreeProfessorshipsByExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {

	return (List<Professorship>) CollectionUtils.select(getProfessorships(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		Professorship professorship = (Professorship) arg0;
		return professorship.getExecutionCourse().getExecutionPeriod() == executionPeriod
			&& !professorship.getExecutionCourse().isMasterDegreeOnly();
	    }
	});
    }

    /***********************************************************************
         * OTHER METHODS *
         **********************************************************************/

    public InfoCredits getExecutionPeriodCredits(ExecutionPeriod executionPeriod) {
	return InfoCreditsBuilder.build(this, executionPeriod);
    }

    /***********************************************************************
         * PRIVATE METHODS *
         **********************************************************************/

    private int countPublicationsInArea(PublicationArea area) {
	int count = 0;
	for (PublicationTeacher publicationTeacher : getTeacherPublications()) {
	    if (publicationTeacher.getPublicationArea().equals(area)) {
		count++;
	    }
	}
	return count;
    }

    public List<MasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisByExecutionYear(
	    ExecutionYear executionYear) {
	List<MasterDegreeThesisDataVersion> guidedThesis = new ArrayList<MasterDegreeThesisDataVersion>();

	for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this
		.getMasterDegreeThesisGuider()) {

	    if (masterDegreeThesisDataVersion.getCurrentState().getState() == State.ACTIVE) {

		List<ExecutionDegree> executionDegrees = masterDegreeThesisDataVersion
			.getMasterDegreeThesis().getStudentCurricularPlan().getDegreeCurricularPlan()
			.getExecutionDegrees();

		for (ExecutionDegree executionDegree : executionDegrees) {
		    if (executionDegree.getExecutionYear().equals(executionYear)) {
			guidedThesis.add(masterDegreeThesisDataVersion);
		    }
		}

	    }
	}

	return guidedThesis;
    }

    public List<MasterDegreeThesisDataVersion> getAllGuidedMasterDegreeThesis() {
	List<MasterDegreeThesisDataVersion> guidedThesis = new ArrayList<MasterDegreeThesisDataVersion>();

	for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this
		.getMasterDegreeThesisGuider()) {
	    if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
		guidedThesis.add(masterDegreeThesisDataVersion);
	    }
	}

	return guidedThesis;
    }

    public void createTeacherPersonalExpectation(
	    net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation infoTeacherPersonalExpectation,
	    ExecutionYear executionYear) {

	checkIfCanCreatePersonalExpectation(executionYear);

	TeacherPersonalExpectation teacherPersonalExpectation = new TeacherPersonalExpectation(
		infoTeacherPersonalExpectation, executionYear);

	addTeacherPersonalExpectations(teacherPersonalExpectation);

    }

    private void checkIfCanCreatePersonalExpectation(ExecutionYear executionYear) {
	TeacherPersonalExpectation storedTeacherPersonalExpectation = getTeacherPersonalExpectationByExecutionYear(executionYear);

	if (storedTeacherPersonalExpectation != null) {
	    throw new DomainException(
		    "error.exception.personalExpectation.expectationAlreadyExistsForExecutionYear");
	}

	TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = this
		.getCurrentWorkingDepartment().readTeacherExpectationDefinitionPeriodByExecutionYear(
			executionYear);

	if (teacherExpectationDefinitionPeriod.isPeriodOpen() == false) {
	    throw new DomainException(
		    "error.exception.personalExpectation.definitionPeriodForExecutionYearAlreadyExpired");
	}

    }

    public List<TeacherServiceExemption> getServiceExemptionsWithoutMedicalSituations(
	    YearMonthDay beginDate, YearMonthDay endDate) {

	List<TeacherServiceExemption> serviceExemptions = new ArrayList<TeacherServiceExemption>();
	for (TeacherServiceExemption serviceExemption : this.getServiceExemptionSituations()) {
	    if (!serviceExemption.isMedicalSituation()
		    && serviceExemption.belongsToPeriod(beginDate, endDate)) {
		serviceExemptions.add(serviceExemption);
	    }
	}
	return serviceExemptions;
    }

    public List<PersonFunction> getPersonFuntions(YearMonthDay beginDate, YearMonthDay endDate) {
	return getPerson().getPersonFuntions(beginDate, endDate);
    }

    public int getLessonHours(ExecutionPeriod executionPeriod) {
	OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
	return getLessonHours(occupationPeriod);
    }

    private int getLessonHours(OccupationPeriod lessonsPeriod) {
	if (lessonsPeriod != null) {
	    TeacherLegalRegimen teacherLegalRegimen = getLastLegalRegimenWithoutEndSituations(
		    lessonsPeriod.getStartYearMonthDay(), lessonsPeriod.getEndYearMonthDay());
	    if (teacherLegalRegimen != null && teacherLegalRegimen.getLessonHours() != null) {
		return teacherLegalRegimen.getLessonHours();
	    }
	}
	return 0;
    }

    public double getManagementFunctionsCredits(ExecutionPeriod executionPeriod) {
	double totalCredits = 0.0;
	for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
	    if (personFunction.belongsToPeriod(executionPeriod.getBeginDateYearMonthDay(),
		    executionPeriod.getEndDateYearMonthDay())) {
		totalCredits = (personFunction.getCredits() != null) ? totalCredits
			+ personFunction.getCredits() : totalCredits;
	    }
	}
	return round(totalCredits);
    }

    public Category getCategoryForCreditsByPeriod(ExecutionPeriod executionPeriod) {

	OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
	if (occupationPeriod == null) {
	    return null;
	}
	return getLastCategory(occupationPeriod.getStartYearMonthDay(), occupationPeriod
		.getEndYearMonthDay());
    }
    
    public List<TeacherServiceExemption> getValidTeacherServiceExemptionsToCountInCredits(
	    ExecutionPeriod executionPeriod) {

	List<TeacherServiceExemption> result = new ArrayList<TeacherServiceExemption>();
	OccupationPeriod lessonsPeriod = executionPeriod.getLessonsPeriod();
	if (lessonsPeriod == null) {
	    return result;
	}

	List<TeacherServiceExemption> serviceExemptions = getServiceExemptionsWithoutMedicalSituations(
		lessonsPeriod.getStartYearMonthDay(), lessonsPeriod.getEndYearMonthDay());
	TeacherServiceExemption dominantExemption = chooseDominantServiceExemptionInLessonsPeriod(
		serviceExemptions, lessonsPeriod);

	if (dominantExemption != null) {
	    if (dominantExemption.isForNotCountInCredits()) {
		return result;

	    } else if (dominantExemption.isForCountInCreditsBecauseIsSabbaticalOrEquivalent()) {
		result.add(dominantExemption);

	    } else {
		for (TeacherServiceExemption exemption : serviceExemptions) {
		    if (exemption.isForCountInCreditsButDontIsSabbatical(this, executionPeriod)) {
			result.add(exemption);
		    }
		}
	    }
	}
	return result;
    }

    public double getServiceExemptionCredits(ExecutionPeriod executionPeriod) {

	List<TeacherServiceExemption> validServiceExemptions = getValidTeacherServiceExemptionsToCountInCredits(executionPeriod);

	if (!validServiceExemptions.isEmpty()) {
	    OccupationPeriod lessonsPeriod = executionPeriod.getLessonsPeriod();

	    if (validServiceExemptions.size() == 1
		    && validServiceExemptions.get(0)
			    .isForCountInCreditsBecauseIsSabbaticalOrEquivalent()) {

		TeacherServiceExemption exemption = validServiceExemptions.get(0);
		Interval serviceExemptionsInterval = new Interval(exemption.getStartYearMonthDay()
			.toDateMidnight(), exemption.getEndYearMonthDay().toDateMidnight());
		int sabbaticalMonths = serviceExemptionsInterval.toPeriod(PeriodType.months())
			.getMonths();
		return calculateSabbaticalCredits(sabbaticalMonths, lessonsPeriod, exemption,
			executionPeriod);

	    } else {

		Interval lessonsInterval = new Interval(lessonsPeriod.getStartYearMonthDay()
			.toDateMidnight(), lessonsPeriod.getEndYearMonthDay().toDateMidnight());
		int lessonsDays = lessonsInterval.toPeriod(PeriodType.days()).getDays();
		List<Interval> notYetOverlapedIntervals = new ArrayList<Interval>();
		List<Interval> newIntervals = new ArrayList<Interval>();
		notYetOverlapedIntervals.add(lessonsInterval);

		for (TeacherServiceExemption exemption : validServiceExemptions) {

		    YearMonthDay exemptionBegin = exemption.getStartYearMonthDay().isAfter(
			    lessonsPeriod.getStartYearMonthDay()) ? exemption.getStartYearMonthDay()
			    : lessonsPeriod.getStartYearMonthDay();

		    YearMonthDay exemptionEnd = exemption.getEndYearMonthDay() == null
			    || exemption.getEndYearMonthDay()
				    .isAfter(lessonsPeriod.getEndYearMonthDay()) ? lessonsPeriod
			    .getEndYearMonthDay() : exemption.getEndYearMonthDay();

		    for (Interval notYetOverlapedInterval : notYetOverlapedIntervals) {
			Interval exemptionInterval = new Interval(exemptionBegin.toDateMidnight(),
				exemptionEnd.toDateMidnight());
			Interval overlapInterval = exemptionInterval.overlap(notYetOverlapedInterval);
			if (overlapInterval != null) {
			    newIntervals.addAll(getNotOverlapedIntervals(overlapInterval,
				    notYetOverlapedInterval));
			} else {
			    newIntervals.add(notYetOverlapedInterval);
			}
		    }

		    notYetOverlapedIntervals.clear();
		    notYetOverlapedIntervals.addAll(newIntervals);
		    newIntervals.clear();
		}

		int notOverlapedDays = 0;
		for (Interval interval : notYetOverlapedIntervals) {
		    notOverlapedDays += interval.toPeriod(PeriodType.days()).getDays();
		}

		int overlapedDays = lessonsDays - notOverlapedDays;
		Double overlapedPercentage = round(Double.valueOf(overlapedDays)
			/ Double.valueOf(lessonsDays));
		int lessonHours = getLessonHours(lessonsPeriod);
		return round(overlapedPercentage * lessonHours);
	    }
	}

	return 0.0;
    }
    
    private List<Interval> getNotOverlapedIntervals(Interval overlapInterval,
	    Interval notYetOverlapedInterval) {

	List<Interval> intervals = new ArrayList<Interval>();
	YearMonthDay overlapIntervalStart = overlapInterval.getStart().toYearMonthDay();
	YearMonthDay overlapIntervalEnd = overlapInterval.getEnd().toYearMonthDay();
	YearMonthDay notYetOverlapedIntervalStart = notYetOverlapedInterval.getStart().toYearMonthDay();
	YearMonthDay notYetOverlapedIntervalEnd = notYetOverlapedInterval.getEnd().toYearMonthDay();

	if (overlapIntervalStart.equals(notYetOverlapedIntervalStart)
		&& !overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
	    intervals.add(new Interval(overlapInterval.getEnd().plusDays(1), notYetOverlapedInterval
		    .getEnd()));

	} else if (!overlapIntervalStart.equals(notYetOverlapedIntervalStart)
		&& overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
	    intervals.add(new Interval(notYetOverlapedInterval.getStart(), overlapInterval.getStart()
		    .minusDays(1)));

	} else if (!overlapIntervalStart.equals(notYetOverlapedIntervalStart)
		&& !overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
	    intervals.add(new Interval(notYetOverlapedInterval.getStart(), overlapInterval.getStart()
		    .minusDays(1)));
	    intervals.add(new Interval(overlapInterval.getEnd().plusDays(1), notYetOverlapedInterval
		    .getEnd()));
	}

	return intervals;
    }

    private int calculateSabbaticalCredits(int sabbaticalMonths, OccupationPeriod lessonsPeriod,
	    TeacherServiceExemption teacherServiceExemption, ExecutionPeriod executionPeriod) {

	double overlapPercentage1 = calculateLessonsIntervalAndExemptionOverlapPercentage(lessonsPeriod,
		teacherServiceExemption), overlapPercentage2 = 0.0;

	if (overlapPercentage1 == 1.0) {
	    return calculateSabbaticalOrEquivalentCreditsByType(teacherServiceExemption.getType(),
		    lessonsPeriod);

	} else if (executionPeriod.containsDay(teacherServiceExemption.getStart())) {
	    ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
	    if (sabbaticalMonths >= 11) {
		nextExecutionPeriod = (nextExecutionPeriod != null) ? nextExecutionPeriod
			.getNextExecutionPeriod() : null;
	    }
	    if (nextExecutionPeriod != null) {
		OccupationPeriod nextLessonsPeriod = nextExecutionPeriod.getLessonsPeriod();
		overlapPercentage2 = calculateLessonsIntervalAndExemptionOverlapPercentage(
			nextLessonsPeriod, teacherServiceExemption);
	    }
	    if (overlapPercentage1 > overlapPercentage2) {
		return calculateSabbaticalOrEquivalentCreditsByType(teacherServiceExemption.getType(),
			lessonsPeriod);
	    }

	} else {
	    ExecutionPeriod previousExecutionPeriod = executionPeriod.getPreviousExecutionPeriod();
	    if (sabbaticalMonths >= 11) {
		previousExecutionPeriod = previousExecutionPeriod.getPreviousExecutionPeriod();
	    }
	    OccupationPeriod previousLessonsPeriod = previousExecutionPeriod.getLessonsPeriod();
	    overlapPercentage2 = calculateLessonsIntervalAndExemptionOverlapPercentage(
		    previousLessonsPeriod, teacherServiceExemption);
	    if (overlapPercentage1 > overlapPercentage2) {
		return calculateSabbaticalOrEquivalentCreditsByType(teacherServiceExemption.getType(),
			lessonsPeriod);
	    }
	}
	return 0;
    }

    private int calculateSabbaticalOrEquivalentCreditsByType(ServiceExemptionType serviceExemptionType,
	    OccupationPeriod occupationPeriod) {
	if (serviceExemptionType.equals(ServiceExemptionType.SABBATICAL)) {
	    return 6;
	}
	return getLessonHours(occupationPeriod);
    }

    private double calculateLessonsIntervalAndExemptionOverlapPercentage(OccupationPeriod lessonsPeriod,
	    TeacherServiceExemption teacherServiceExemption) {

	Interval lessonsInterval = new Interval(lessonsPeriod.getStartYearMonthDay().toDateMidnight(),
		lessonsPeriod.getEndYearMonthDay().toDateMidnight());
	Interval serviceExemptionsInterval = new Interval(teacherServiceExemption.getStartYearMonthDay()
		.toDateMidnight(),
		(teacherServiceExemption.getEndYearMonthDay() != null) ? teacherServiceExemption
			.getEndYearMonthDay().toDateMidnight() : lessonsPeriod.getEndYearMonthDay()
			.toDateMidnight());

	Interval overlapInterval = lessonsInterval.overlap(serviceExemptionsInterval);
	if (overlapInterval != null) {
	    int intersectedDays = overlapInterval.toPeriod(PeriodType.days()).getDays();
	    return round(Double.valueOf(intersectedDays)
		    / Double.valueOf(lessonsInterval.toPeriod(PeriodType.days()).getDays()));
	}

	return 0.0;
    }
       
    private TeacherServiceExemption chooseDominantServiceExemptionInLessonsPeriod(
	    List<TeacherServiceExemption> serviceExemptions, OccupationPeriod lessonsPeriod) {

	if (lessonsPeriod == null) {
	    return null;
	}

	Integer numberOfDaysInPeriod = null, maxDays = 0;
	TeacherServiceExemption teacherServiceExemption = null;
	Interval lessonsInterval = new Interval(lessonsPeriod.getStartYearMonthDay().toDateMidnight(),
		lessonsPeriod.getEndYearMonthDay().toDateMidnight());

	for (TeacherServiceExemption serviceExemption : serviceExemptions) {
	    Interval serviceExemptionsInterval = new Interval(serviceExemption.getStartYearMonthDay()
		    .toDateMidnight(),
		    (serviceExemption.getEndYearMonthDay() != null) ? serviceExemption
			    .getEndYearMonthDay().toDateMidnight() : lessonsPeriod.getEndYearMonthDay()
			    .toDateMidnight());

	    Interval overlapInterval = lessonsInterval.overlap(serviceExemptionsInterval);
	    if (overlapInterval != null) {
		numberOfDaysInPeriod = overlapInterval.toPeriod(PeriodType.days()).getDays();
		if (numberOfDaysInPeriod >= maxDays) {
		    maxDays = numberOfDaysInPeriod;
		    teacherServiceExemption = serviceExemption;
		}
	    }
	}
	return teacherServiceExemption;
    }

    private Double round(double n) {
	return Math.round((n * 100.0)) / 100.0;
    }

    public boolean isDeceased() {
	for (TeacherLegalRegimen legalRegimen : getLegalRegimens()) {
	    if (legalRegimen.getLegalRegimenType().equals(LegalRegimenType.DEATH)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isInactive(ExecutionPeriod executionPeriod) {
	if (executionPeriod != null) {
	    OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
	    if (occupationPeriod != null) {
		List<TeacherLegalRegimen> allLegalRegimens = getAllLegalRegimensWithoutEndSituations(
			occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());
		if (allLegalRegimens.isEmpty()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean isMonitor(ExecutionPeriod executionPeriod) {
	if (executionPeriod != null) {
	    Category category = getCategoryForCreditsByPeriod(executionPeriod);
	    return (category != null && category.isMonitorCategory());
	}
	return false;
    }

    public List<Advise> getAdvisesByAdviseTypeAndExecutionYear(AdviseType adviseType,
	    ExecutionYear executionYear) {

	List<Advise> result = new ArrayList<Advise>();
	Date executionYearStartDate = executionYear.getBeginDate();
	Date executionYearEndDate = executionYear.getEndDate();

	for (Advise advise : this.getAdvises()) {
	    if ((advise.getAdviseType() == adviseType)) {
		Date adviseStartDate = advise.getStartExecutionPeriod().getBeginDate();
		Date adviseEndDate = advise.getEndExecutionPeriod().getEndDate();

		if (((executionYearStartDate.compareTo(adviseStartDate) < 0) && (executionYearEndDate
			.compareTo(adviseStartDate) < 0))
			|| ((executionYearStartDate.compareTo(adviseEndDate) > 0) && (executionYearEndDate
				.compareTo(adviseEndDate) > 0))) {
		    continue;
		}
		result.add(advise);
	    }
	}

	return result;
    }

    public List<Advise> getAdvisesByAdviseType(AdviseType adviseType) {

	List<Advise> result = new ArrayList<Advise>();
	for (Advise advise : this.getAdvises()) {
	    if (advise.getAdviseType() == adviseType) {
		result.add(advise);
	    }
	}

	return result;
    }

    public double getBalanceOfCreditsUntil(ExecutionPeriod executionPeriod) throws ParseException {

	double balanceCredits = 0.0;
	ExecutionPeriod firstExecutionPeriod = TeacherService.getStartExecutionPeriodForCredits();

	TeacherService firstTeacherService = getTeacherServiceByExecutionPeriod(firstExecutionPeriod);
	if (firstTeacherService != null) {
	    balanceCredits = firstTeacherService.getPastServiceCredits();
	}

	if (executionPeriod != null && executionPeriod.isAfter(firstExecutionPeriod)) {
	    balanceCredits = sumCreditsBetweenPeriods(firstExecutionPeriod.getNextExecutionPeriod(),
		    executionPeriod, balanceCredits);
	}
	return balanceCredits;
    }

    private double sumCreditsBetweenPeriods(ExecutionPeriod startPeriod,
	    ExecutionPeriod endExecutionPeriod, double totalCredits) throws ParseException {

	ExecutionPeriod executionPeriodAfterEnd = endExecutionPeriod.getNextExecutionPeriod();
	while (startPeriod != executionPeriodAfterEnd) {
	    TeacherService teacherService = getTeacherServiceByExecutionPeriod(startPeriod);
	    if (teacherService != null) {
		totalCredits += teacherService.getCredits();
	    }
	    totalCredits += getManagementFunctionsCredits(startPeriod);
	    totalCredits += getServiceExemptionCredits(startPeriod);
	    totalCredits -= getMandatoryLessonHours(startPeriod);
	    startPeriod = startPeriod.getNextExecutionPeriod();
	}
	return totalCredits;
    }

    public int getMandatoryLessonHours(ExecutionPeriod executionPeriod) {

	OccupationPeriod lessonsPeriod = executionPeriod.getLessonsPeriod();
	if (lessonsPeriod == null) {
	    return 0;
	}

	TeacherLegalRegimen lastLegalRegimen = getLastLegalRegimenWithoutEndSituations(lessonsPeriod
		.getStartYearMonthDay(), lessonsPeriod.getEndYearMonthDay());

	if (lastLegalRegimen != null) {
	    Category category = lastLegalRegimen.getCategory();
	    if (category != null && category.isMonitorCategory()) {
		return 0;
	    }

	    List<TeacherServiceExemption> serviceExemptions = getServiceExemptionsWithoutMedicalSituations(
		    lessonsPeriod.getStartYearMonthDay(), lessonsPeriod.getEndYearMonthDay());
	    TeacherServiceExemption teacherServiceExemption = chooseDominantServiceExemptionInLessonsPeriod(
		    serviceExemptions, lessonsPeriod);

	    if (teacherServiceExemption != null && teacherServiceExemption.isForNotCountInCredits()) {
		return 0;
	    }

	    final Integer hours = lastLegalRegimen.getLessonHours();
	    return (hours == null) ? 0 : hours.intValue();
	}

	return 0;
    }

    public List<PersonFunction> getManagementFunctions(ExecutionPeriod executionPeriod) {
	List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
	for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
	    if (personFunction.belongsToPeriod(executionPeriod.getBeginDateYearMonthDay(),
		    executionPeriod.getEndDateYearMonthDay())) {
		personFunctions.add(personFunction);
	    }
	}
	return personFunctions;
    }

    public static Teacher readTeacherByUsername(final String userName) {
	final Person person = Person.readPersonByUsername(userName);
	return (person.getTeacher() != null) ? person.getTeacher() : null;
    }

    public static Teacher readByNumber(final Integer teacherNumber) {
	for (final Teacher teacher : RootDomainObject.getInstance().getTeachers()) {
	    if (teacher.getTeacherNumber() != null && teacher.getTeacherNumber().equals(teacherNumber)) {
		return teacher;
	    }
	}
	return null;
    }

    public static List<Teacher> readByNumbers(Collection<Integer> teacherNumbers) {
	List<Teacher> selectedTeachers = new ArrayList<Teacher>();
	for (final Teacher teacher : RootDomainObject.getInstance().getTeachers()) {
	    if (teacherNumbers.contains(teacher.getTeacherNumber())) {
		selectedTeachers.add(teacher);
	    }
	    // This isn't necessary, its just a fast optimization.
	    if (teacherNumbers.size() == selectedTeachers.size()) {
		break;
	    }
	}
	return selectedTeachers;
    }

    public List<Professorship> getProfessorships(ExecutionPeriod executionPeriod) {
	List<Professorship> professorships = new ArrayList<Professorship>();
	for (Professorship professorship : this.getProfessorships()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)) {
		professorships.add(professorship);
	    }
	}
	return professorships;
    }

    public List<Professorship> getProfessorships(ExecutionYear executionYear) {
	List<Professorship> professorships = new ArrayList<Professorship>();
	for (Professorship professorship : this.getProfessorships()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(
		    executionYear)) {
		professorships.add(professorship);
	    }
	}
	return professorships;
    }

    public Set<TeacherDegreeFinalProjectStudent> findTeacherDegreeFinalProjectStudentsByExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {
	final Set<TeacherDegreeFinalProjectStudent> teacherDegreeFinalProjectStudents = new HashSet<TeacherDegreeFinalProjectStudent>();
	for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : getDegreeFinalProjectStudents()) {
	    if (executionPeriod == teacherDegreeFinalProjectStudent.getExecutionPeriod()) {
		teacherDegreeFinalProjectStudents.add(teacherDegreeFinalProjectStudent);
	    }
	}
	return teacherDegreeFinalProjectStudents;
    }

    public List<ManagementPositionCreditLine> getManagementPositionsFor(ExecutionPeriod executionPeriod) {
	final List<ManagementPositionCreditLine> result = new ArrayList<ManagementPositionCreditLine>();
	for (final ManagementPositionCreditLine managementPositionCreditLine : this
		.getManagementPositions()) {
	    if (managementPositionCreditLine.getStart().before(executionPeriod.getEndDate())
		    && managementPositionCreditLine.getEnd().after(executionPeriod.getBeginDate())) {
		result.add(managementPositionCreditLine);
	    }
	}
	return result;
    }

    public List<PublicationTeacher> readPublicationsByPublicationArea(PublicationArea publicationArea) {
	final List<PublicationTeacher> result = new ArrayList<PublicationTeacher>();
	for (final PublicationTeacher publicationTeacher : this.getTeacherPublicationsSet()) {
	    if (publicationTeacher.getPublicationArea().equals(publicationArea)) {
		result.add(publicationTeacher);
	    }
	}
	return result;
    }

    public List<OldPublication> readOldPublicationsByType(OldPublicationType oldPublicationType) {
	final List<OldPublication> result = new ArrayList<OldPublication>();
	for (final OldPublication oldPublication : this.getAssociatedOldPublicationsSet()) {
	    if (oldPublication.getOldPublicationType().equals(oldPublicationType)) {
		result.add(oldPublication);
	    }
	}
	return result;
    }

    public Orientation readOrientationByType(OrientationType orientationType) {
	for (final Orientation orientation : this.getAssociatedOrientationsSet()) {
	    if (orientation.getOrientationType().equals(orientationType)) {
		return orientation;
	    }
	}
	return null;
    }

    public PublicationsNumber readPublicationsNumberByType(PublicationType publicationType) {
	for (final PublicationsNumber publicationsNumber : this.getAssociatedPublicationsNumbersSet()) {
	    if (publicationsNumber.getPublicationType().equals(publicationType)) {
		return publicationsNumber;
	    }
	}
	return null;
    }

    public SortedSet<ExecutionCourse> getCurrentExecutionCourses() {
	final SortedSet<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(
		ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
	for (final Professorship professorship : getProfessorshipsSet()) {
	    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
	    final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
	    final ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
	    if (executionPeriod.getState().equals(PeriodState.CURRENT)
		    || (nextExecutionPeriod != null && nextExecutionPeriod.getState().equals(
			    PeriodState.CURRENT))) {
		executionCourses.add(executionCourse);
	    }
	}
	return executionCourses;
    }

    public Set<Proposal> findFinalDegreeWorkProposals() {
	final Set<Proposal> proposals = new HashSet<Proposal>();
	proposals.addAll(getAssociatedProposalsByCoorientatorSet());
	proposals.addAll(getAssociatedProposalsByOrientatorSet());
	return proposals;
    }

    public boolean isResponsibleFor(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
	    if (executionCourse.getExecutionPeriod() == executionPeriod) {
		return isResponsibleFor(executionCourse) != null;
	    }
	}
	return false;
    }

    public Double getHoursLecturedOnExecutionCourseByShiftType(ExecutionCourse executionCourse,
	    ShiftType shiftType) {
	double returnValue = 0;

	Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
	TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse
		.getExecutionPeriod());
	if (teacherService != null) {
	    List<DegreeTeachingService> teachingServices = teacherService
		    .getDegreeTeachingServiceByProfessorship(professorship);
	    for (DegreeTeachingService teachingService : teachingServices) {
		if (teachingService.getShift().getTipo() == shiftType) {
		    returnValue += ((teachingService.getPercentage() / 100) * teachingService.getShift()
			    .hours());
		}
	    }
	}
	return returnValue;
    }

    public boolean teachesAny(final List<ExecutionCourse> executionCourses) {
	for (final Professorship professorship : getProfessorshipsSet()) {
	    if (executionCourses.contains(professorship.getExecutionCourse())) {
		return true;
	    }
	}
	return false;
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

}