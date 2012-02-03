package net.sourceforge.fenixedu.domain.vigilancy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class VigilantWrapper extends VigilantWrapper_Base {

    /*************** OLD VIGILANT *******************/

    public VigilantWrapper(VigilantGroup group, Person person) {
	super();
	this.setStartPoints(new BigDecimal(0));
	this.setPointsWeight(new BigDecimal(1));
	this.setConvokable(true);
	this.setVigilantGroup(group);
	this.setPerson(person);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static final Comparator<VigilantWrapper> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<VigilantWrapper>() {

	@Override
	public int compare(VigilantWrapper o1, VigilantWrapper o2) {
	    return o1.getVigilantGroup().getExecutionYear().compareTo(o2.getVigilantGroup().getExecutionYear());
	}

    };

    public static final Comparator<VigilantWrapper> POINTS_COMPARATOR = new BeanComparator("points");

    public static final Comparator<VigilantWrapper> ESTIMATED_POINTS_COMPARATOR = new Comparator<VigilantWrapper>() {

	@Override
	public int compare(VigilantWrapper o1, VigilantWrapper o2) {
	    return Double.valueOf(o1.getEstimatedPoints()).compareTo(o2.getEstimatedPoints());
	}

    };

    public static final Comparator<VigilantWrapper> NAME_COMPARATOR = new BeanComparator("person.name");

    public static final Comparator<VigilantWrapper> USERNAME_COMPARATOR = new ReverseComparator(new BeanComparator(
	    "person.username"));

    public static final Comparator<VigilantWrapper> CATEGORY_COMPARATOR = new Comparator<VigilantWrapper>() {

	public int compare(VigilantWrapper v1, VigilantWrapper v2) {

	    ProfessionalCategory c1 = v1.getTeacher() != null ? v1.getTeacher().getCategory() : null;
	    ProfessionalCategory c2 = v2.getTeacher() != null ? v2.getTeacher().getCategory() : null;

	    if (c1 == null && c2 == null)
		return 0;
	    if (c1 == null)
		return -1;
	    if (c2 == null)
		return 1;

	    return -c1.compareTo(c2);

	}

    };

    public static final Comparator<VigilantWrapper> SORT_CRITERIA_COMPARATOR = new Comparator<VigilantWrapper>() {
	public int compare(VigilantWrapper v1, VigilantWrapper v2) {
	    ComparatorChain comparator = new ComparatorChain();
	    comparator.addComparator(VigilantWrapper.ESTIMATED_POINTS_COMPARATOR);
	    comparator.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
	    comparator.addComparator(VigilantWrapper.USERNAME_COMPARATOR);

	    return comparator.compare(v1, v2);
	}
    };

    public double getPoints() {
	double points = this.getStartPoints().doubleValue();
	BigDecimal weight = this.getPointsWeight();

	List<Vigilancy> vigilancies = getVigilancies();

	for (Vigilancy vigilancy : vigilancies) {
	    points += weight.doubleValue() * vigilancy.getPoints();
	}

	return points;
    }

    public double getEstimatedPoints() {
	double totalPoints = getStartPoints().doubleValue();
	BigDecimal weight = getPointsWeight();

	for (Vigilancy vigilancy : getActiveVigilancies()) {
	    int points = vigilancy.hasPointsAttributed() ? vigilancy.getPoints() : vigilancy.getEstimatedPoints();
	    totalPoints += points * weight.doubleValue();
	}

	return totalPoints;
    }

    public Integer getNumber() {
	Person person = this.getPerson();
	Employee employee = person.getEmployee();
	if (employee != null)
	    return employee.getEmployeeNumber();

	Student student = person.getStudent();
	if (student != null)
	    return student.getNumber();

	return 0;
    }

    public String getEmail() {
	return this.getPerson().getEmail();
    }

    public double getPointsInExecutionYear(ExecutionYear executionYear) {
	return this.getPerson().getVigilancyPointsForGivenYear(executionYear);
    }

    public double getTotalPoints() {
	Person person = this.getPerson();
	return person.getTotalVigilancyPoints();
    }

    public boolean hasVigilantGroup(VigilantGroup group) {
	if (group.equals(getVigilantGroup())) {
	    return true;
	}
	return false;
    }

    public Boolean isAvailableOnDate(DateTime begin, DateTime end) {
	List<UnavailablePeriod> unavailablePeriods = this.getPerson().getUnavailablePeriods();
	for (UnavailablePeriod period : unavailablePeriods) {
	    if (period.containsInterval(begin, end))
		return Boolean.FALSE;

	}
	Interval interval = new Interval(begin, end);

	for (VigilantWrapper otherVigilant : this.getPerson().getVigilantWrappers()) {
	    for (Vigilancy vigilancy : otherVigilant.getVigilancies()) {
		if (interval.overlaps(vigilancy.getWrittenEvaluation().getDurationInterval())) {
		    if (vigilancy.getWrittenEvaluation().getDurationInterval().overlaps(interval)) {
			return Boolean.FALSE;
		    }
		}
	    }
	}

	return Boolean.TRUE;
    }

    public Teacher getTeacher() {
	return this.getPerson().getTeacher();
    }

    public String getTeacherCategoryCode() {
	return getTeacher() != null && getTeacher().getCategory() != null ? getTeacher().getCategory().getName().getContent()
		: "";
    }

    public List<Campus> getCampus() {
	List<Campus> campus = new ArrayList<Campus>();
	Employee employee = this.getPerson().getEmployee();
	if (employee != null) {
	    final GiafProfessionalData giafProfessionalData = getPerson().getPersonProfessionalData() != null ? getPerson()
		    .getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.EMPLOYEE) : null;
	    if (giafProfessionalData != null) {
		campus.add(giafProfessionalData.getCampus());
	    }
	}
	return campus;
    }

    public String getCampusNames() {
	List<Campus> campusList = this.getCampus();
	String campusNames = "";
	for (Campus campus : campusList) {
	    if (campusNames.length() != 0) {
		campusNames = campusNames + ", ";
	    }
	    campusNames += campus.getSpaceInformation().getPresentationName();
	}
	return campusNames;
    }

    public Boolean isAvailableInCampus(Campus campus) {
	List<Campus> campusList = this.getCampus();

	/*
	 * If campusList is empty it's best to say that he is available and then
	 * someone has to remove the vigilant by hand, instead of saying that
	 * the vigilant is never available in any campus (which is wrong).
	 */
	return campusList.isEmpty() ? true : campusList.contains(campus);
    }

    public List<Interval> getConvokePeriods() {
	List<Interval> convokingPeriods = new ArrayList<Interval>();
	List<Vigilancy> convokes = this.getVigilancies();
	for (Vigilancy convoke : convokes) {
	    convokingPeriods.add(new Interval(convoke.getBeginDate(), convoke.getEndDate()));
	}
	return convokingPeriods;
    }

    public Boolean canBeConvokedForWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
	DateTime beginOfExam = writtenEvaluation.getBeginningDateTime();
	DateTime endOfExam = writtenEvaluation.getEndDateTime();

	boolean isInExamPeriod = writtenEvaluation.getAssociatedExecutionCourses().get(0).isInExamPeriod();
	return this.isAvailableOnDate(beginOfExam, endOfExam)
		&& this.hasNoEvaluationsOnDate(beginOfExam, endOfExam)
		&& (isInExamPeriod || (!isInExamPeriod && ((this.getTeacher() != null && this.getTeacher().teachesAny(
			writtenEvaluation.getAssociatedExecutionCourses())) || !hasLessons(beginOfExam, endOfExam))));
    }

    private boolean hasLessons(DateTime beginOfExam, DateTime endOfExam) {
	Teacher teacher = getTeacher();
	return teacher == null ? false : teacher.hasLessons(beginOfExam, endOfExam);
    }

    public boolean hasNoEvaluationsOnDate(DateTime beginOfExam, DateTime endOfExam) {
	List<Vigilancy> convokes = this.getVigilancies();
	Interval requestedInterval = new Interval(beginOfExam, endOfExam);
	for (Vigilancy convoke : convokes) {
	    DateTime begin = convoke.getBeginDateTime();
	    DateTime end = convoke.getEndDateTime();
	    Interval convokeInterval = new Interval(begin, end);
	    if (convokeInterval.contains(requestedInterval)) {
		return Boolean.FALSE;
	    }
	}
	return Boolean.TRUE;

    }

    public void delete() {

	if (this.getActiveVigilanciesInList(this.getVigilancies()).size() == 0) {
	    for (; !this.getVigilancies().isEmpty(); this.getVigilancies().get(0).delete())
		;
	    removePerson();
	    removeVigilantGroup();
	    removeRootDomainObject();
	    super.deleteDomainObject();
	} else {
	    throw new DomainException("vigilancy.error.cannotDeleteVigilantDueToConvokes");
	}
    }

    public boolean isAllowedToSpecifyUnavailablePeriod() {
	DateTime currentDate = new DateTime();
	if (getVigilantGroup().canSpecifyUnavailablePeriodIn(currentDate)) {
	    return true;
	}
	return false;
    }

    public List<Vigilancy> getOtherCourseVigilancies() {
	List<Vigilancy> convokes = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : getVigilancies()) {
	    if (vigilancy.isOtherCourseVigilancy()) {
		convokes.add(vigilancy);
	    }
	}
	return convokes;
    }

    public List<Vigilancy> getOwnCourseVigilancies() {
	List<Vigilancy> convokes = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : getVigilancies()) {
	    if (vigilancy.isOwnCourseVigilancy()) {
		convokes.add(vigilancy);
	    }
	}
	return convokes;
    }

    public List<Vigilancy> getActiveOtherCourseVigilancies() {
	return getActiveVigilanciesInList(getOtherCourseVigilancies());
    }

    public List<Vigilancy> getActiveVigilancies() {
	return getActiveVigilanciesInList(getVigilancies());
    }

    public int getActiveVigilanciesCount() {
	return getActiveVigilancies().size();
    }

    public List<Vigilancy> getActiveOwnCourseVigilancies() {
	return getActiveVigilanciesInList(getOwnCourseVigilancies());
    }

    private List<Vigilancy> getActiveVigilanciesInList(List<Vigilancy> vigilancies) {
	List<Vigilancy> activeVigilancies = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : vigilancies) {
	    if (vigilancy.isActive()) {
		activeVigilancies.add(vigilancy);
	    }
	}
	return activeVigilancies;
    }

    public Vigilancy getVigilancyFor(WrittenEvaluation evaluation) {

	for (Vigilancy vigilancy : this.getVigilancies()) {
	    if (vigilancy.getWrittenEvaluation().equals(evaluation)) {
		return vigilancy;
	    }
	}
	return null;
    }

    @Override
    public List<Vigilancy> getVigilancies() {
	return this.getVigilantGroup().getVigilancies(this);
    }

    public boolean hasBeenConvokedForEvaluation(WrittenEvaluation writtenEvaluation) {
	List<Vigilancy> convokes = this.getVigilancies();
	for (Vigilancy convoke : convokes) {
	    if (convoke.getWrittenEvaluation().equals(writtenEvaluation))
		return true;
	}
	return false;
    }

    public String getUnavailablePeriodsAsString() {
	String periods = "";
	int i = 0;
	List<UnavailablePeriod> unavailablePeriodsForGivenYear = this.getPerson().getUnavailablePeriodsForGivenYear(
		getExecutionYear());
	int size = unavailablePeriodsForGivenYear.size() - 1;
	for (UnavailablePeriod period : unavailablePeriodsForGivenYear) {
	    periods += period.getUnavailableAsString();
	    periods += (i == size) ? " " : ", ";
	    i++;
	}
	return periods;
    }

    public String getIncompatiblePersonName() {
	return (this.getPerson().getIncompatibleVigilantPerson() != null) ? this.getPerson().getIncompatibleVigilantPerson()
		.getName() : "";
    }

    public UnavailableTypes getWhyIsUnavailabeFor(WrittenEvaluation writtenEvaluation) {
	DateTime begin = writtenEvaluation.getBeginningDateTime();
	DateTime end = writtenEvaluation.getEndDateTime();

	if (!this.isAvailableOnDate(begin, end)) {
	    return UnavailableTypes.UNAVAILABLE_PERIOD;
	}
	if (!this.isAvailableInCampus(writtenEvaluation.getCampus())) {
	    return UnavailableTypes.NOT_AVAILABLE_ON_CAMPUS;
	}
	if (!this.hasNoEvaluationsOnDate(begin, end)) {
	    return UnavailableTypes.ALREADY_CONVOKED_FOR_ANOTHER_EVALUATION;
	}

	Teacher teacher = this.getPerson().getTeacher();
	if (teacher != null) {
	    Set<PersonContractSituation> validTeacherServiceExemptions = teacher.getValidTeacherServiceExemptions(new Interval(
		    begin, end.plusDays(1)));
	    if (!validTeacherServiceExemptions.isEmpty()) {
		return UnavailableTypes.SERVICE_EXEMPTION;
	    }
	}

	if (teacher != null && teacher.hasLessons(begin, end)) {
	    return UnavailableTypes.LESSON_AT_SAME_TIME;
	}

	Person person = this.getPerson().getIncompatibleVigilantPerson();
	if (person != null) {
	    List<Vigilancy> convokes = writtenEvaluation.getVigilancies();
	    for (Vigilancy convoke : convokes) {
		if (convoke.getVigilantWrapper().getPerson().equals(person))
		    return UnavailableTypes.INCOMPATIBLE_PERSON;
	    }
	}

	return UnavailableTypes.UNKNOWN;

    }

    public boolean isCathedraticTeacher() {
	Teacher teacher = this.getTeacher();
	if (teacher != null) {
	    return teacher.getCategory().getWeight() <= 3;
	}
	return false;
    }

    @Override
    public void addVigilancies(Vigilancy vigilancy) {
	if (hasNoEvaluationsOnDate(vigilancy.getBeginDateTime(), vigilancy.getEndDateTime())) {
	    super.addVigilancies(vigilancy);
	} else {
	    throw new DomainException("error.collapsing.convokes");
	}
    }

    /*************** OLD VIGILANT BOUND *******************/

    public static final Comparator<VigilantWrapper> VIGILANT_GROUP_COMPARATOR = new BeanComparator("vigilantGroup.name");

    public String getJustificationforNotConvokable() {
	String result = "";
	if (!this.getConvokable()) {
	    result += this.getVigilantGroup().getName() + ": " + this.getJustification();
	}
	return result;
    }

    public ExecutionYear getExecutionYear() {
	return getVigilantGroup().getExecutionYear();
    }

    public List<UnavailablePeriod> getUnavailablePeriods() {
	return getPerson().getUnavailablePeriodsForGivenYear(getExecutionYear());
    }

}
