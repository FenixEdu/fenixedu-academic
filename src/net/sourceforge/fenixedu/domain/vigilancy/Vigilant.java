package net.sourceforge.fenixedu.domain.vigilancy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class Vigilant extends Vigilant_Base {

	public static final Comparator<Vigilant> POINTS_COMPARATOR = new BeanComparator("totalPoints");
	public static final Comparator<Vigilant> NAME_COMPARATOR = new BeanComparator("person.name"); 
	public static final Comparator<Vigilant> USERNAME_COMPARATOR = new ReverseComparator(new BeanComparator("number"));	
	public static final Comparator<Vigilant> CATEGORY_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
				Vigilant v1 = (Vigilant) o1;
				Vigilant v2 = (Vigilant) o2;
				
				Category c1 = v1.getTeacherCategory();
				Category c2 = v2.getTeacherCategory();
				
				if(c1==null && c2==null) return 0;
				if(c1==null) return -1;
				if(c2==null) return 1;
				
				return -c1.compareTo(c2);
			
			}
		
	};
	
	protected Vigilant() {
		super();
		this.setStartPoints(0);
		this.setPointsWeight(new BigDecimal(1));
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public Vigilant(Person person) {
		this();
		this.setPerson(person);
		this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	}

	public Vigilant(Person person, ExecutionYear executionYear) {
		this();
		
		this.setPerson(person);
		this.setExecutionYear(executionYear);
	}

	public double getPoints() {
		double points = this.getStartPoints();
		BigDecimal weight = this.getPointsWeight();
		
		List<VigilancyWithCredits> vigilancies = Vigilancy.getAllVigilancyWithCredits(this); 

		for(Vigilancy vigilancy : vigilancies) {
			points += weight.doubleValue() * vigilancy.getPoints();
		}
		
		return points;
	}

	public Integer getNumber() {
		Person person = this.getPerson();
		Employee employee = person.getEmployee();
		if(employee!=null) return employee.getEmployeeNumber();
		
		Student student = person.getStudent();
		if(student!=null) return student.getNumber();
		
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

	public List<VigilantGroup> getVigilantGroups() {
		List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
		for (VigilantBound bound : this.getBounds()) {
			groups.add(bound.getVigilantGroup());
		}
		return groups;
	}

	private List<VigilantGroup> getVigilantGroupsForGivenBoundValue(Boolean value) {
		List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
		for (VigilantBound bound : this.getBounds()) {
			if(bound.getConvokable()==value) {
				groups.add(bound.getVigilantGroup());
			}
		}
		return groups;
	}

	public List<VigilantGroup> getVigilantsGroupsWhereCanBeConvoked() {
		return getVigilantGroupsForGivenBoundValue(true); 
	}
	
	public List<VigilantGroup> getVigilantsGroupsWhereCannotBeConvoked() {
		return getVigilantGroupsForGivenBoundValue(false); 
	}
	
	public void addVigilantGroups(VigilantGroup group) {
		VigilantBound bound = new VigilantBound(this, group);
		this.addBounds(bound);
	}

	public void removeVigilantGroups(VigilantGroup group) {
		for (VigilantBound bound : this.getBounds()) {
			if (bound.getVigilantGroup().equals(group)) {
				bound.delete();
				return;
			}
		}
	}

	public boolean hasVigilantGroup(VigilantGroup group) {
		for(VigilantBound bound : this.getBounds()) {
			if(bound.getVigilantGroup().equals(group)) {
				return true;
			}
		}
		return false;
	}
	
	public int getVigilantGroupsCount() {
		return this.getVigilantGroups().size();
	}

	public Boolean isAvailableOnDate(DateTime begin, DateTime end) {
		List<UnavailablePeriod> unavailablePeriods = this
				.getUnavailablePeriods();
		for (UnavailablePeriod period : unavailablePeriods) {
			if (period.containsInterval(begin, end))
				return Boolean.FALSE;

		}
		return Boolean.TRUE;
	}

	public Teacher getTeacher() {
		return this.getPerson().getTeacher();
	}

	public Category getTeacherCategory() {
		Teacher teacher = this.getTeacher();
		return (teacher == null) ? null : teacher.getCategory();
	}
	
	public String getTeacherCategoryCode() {
		Teacher teacher = this.getTeacher();
		return (teacher == null) ? "" : teacher.getCategory().getCode();
	}

	public List<Campus> getCampus() {
		Employee employee = this.getPerson().getEmployee();
		if (employee != null) {
			Assiduousness assiduousness = employee.getAssiduousness();
			ExecutionYear year = this.getExecutionYear();
			if (assiduousness != null) {
				return assiduousness.getCampusForInterval(year
						.getBeginDateYearMonthDay(), year
						.getEndDateYearMonthDay());
			} else {
				return new ArrayList<Campus>();
			}
		}
		return new ArrayList<Campus>();
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

	public Boolean isAvailableInCampus(
			net.sourceforge.fenixedu.domain.space.Campus campus) {
		List<Campus> campusList = this.getCampus();

		/*
		 * If campusList is empty it's best to say that he is available and then
		 * someone has to remove the vigilant by hand, instead of saying that
		 * the vigilant is never available in any campus (which is wrong).
		 */
		return campusList.isEmpty() ? true : campusList.contains(campus);
	}

	public Boolean isAvailableInCampus(
			net.sourceforge.fenixedu.domain.Campus campus) {
		List<Campus> campusList = this.getCampus();

		/*
		 * Check comment above for explanation of why you have the conditional
		 * statement below.
		 */
		if (campusList.isEmpty()) {
			return true;
		}

		for (Campus spaceCampus : campusList) {
			if (spaceCampus.getSpaceInformation().getName().equals(
					campus.getName())) {
				return true;
			}
		}
		return false;
	}

	public void removeIncompatiblePerson() {
		if (super.getIncompatiblePerson() != null) {
			super.setIncompatiblePerson(null);
		} else {
			Person person = this.getPerson();
			ExecutionYear year = this.getExecutionYear();
			List<Vigilant> vigilants = person.getIncompatibleVigilants();
			for (Vigilant vigilant : vigilants) {
				if (vigilant.getExecutionYear().equals(year)) {
					vigilant.setIncompatiblePerson(null);
				}
			}
		}
	}

	@Override
	public Person getIncompatiblePerson() {
		Person person = super.getIncompatiblePerson();
		if (person == null) {
			List<Vigilant> vigilants = this.getPerson()
					.getIncompatibleVigilants();
			ExecutionYear year = this.getExecutionYear();
			for (Vigilant vigilant : vigilants) {
				if (vigilant.getExecutionYear().equals(year)) {
					person = vigilant.getPerson();
				}
			}
		}
		return person;
	}

	@Override
	public void setIncompatiblePerson(Person person) {
		if (this.getPerson().equals(person)) {
			throw new DomainException(
					"vigilancy.error.cannotBeIncompatibleWithYourself");
		} else {
			super.setIncompatiblePerson(person);
		}
	}

	public Vigilant getIncompatibleVigilant() {
		Person person = this.getIncompatiblePerson();
		return person != null ? person.getVigilantForGivenExecutionYear(this
				.getExecutionYear()) : null;
	}

	public List<Interval> getConvokePeriods() {
		List<Interval> convokingPeriods = new ArrayList<Interval>();
		List<Vigilancy> convokes = this.getVigilancys();
		for (Vigilancy convoke : convokes) {
			convokingPeriods.add(new Interval(convoke.getBeginDate(), convoke
					.getEndDate()));
		}
		return convokingPeriods;
	}

	public Boolean canBeConvokedForWrittenEvaluation(
			WrittenEvaluation writtenEvaluation) {
		DateTime beginOfExam = writtenEvaluation.getBeginningDateTime();
		DateTime endOfExam = writtenEvaluation.getEndDateTime();

		return this.isAvailableOnDate(beginOfExam, endOfExam)
				&& this.hasNoEvaluationsOnDate(beginOfExam, endOfExam);
	}

	public boolean hasNoEvaluationsOnDate(DateTime beginOfExam,
			DateTime endOfExam) {
		List<Vigilancy> convokes = this.getVigilancys();
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

		if (this.getVigilancysCount() == 0) {
			removeIncompatiblePerson();
			for (; !this.getUnavailablePeriods().isEmpty(); this
					.getUnavailablePeriods().get(0).delete())
				;
			for (; !this.getBounds().isEmpty(); this.getBounds().get(0)
					.delete())
				;
			removeExecutionYear();
			removePerson();
			removeRootDomainObject();
			super.deleteDomainObject();
		} else {
			throw new DomainException(
					"vigilancy.error.cannotDeleteVigilantDueToConvokes");
		}
	}

	public boolean isAllowedToSpecifyUnavailablePeriod() {

		DateTime currentDate = new DateTime();
		for (VigilantGroup group : this.getVigilantGroups()) {
			if (group.canSpecifyUnavailablePeriodIn(currentDate)) {
				return true;
			}
		}
		return false;
	}

	public List<Vigilancy> getAllConvokes() {
		List<Vigilancy> convokes = new ArrayList<Vigilancy>(super.getVigilancys());
		Collections.sort(convokes,
				VigilancyWithCredits.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
		return convokes;
	}

	public List<VigilancyWithCredits> getVigilancyWithCredits() {
		List<VigilancyWithCredits> convokes = Vigilancy.getAllVigilancyWithCredits(this);
		Collections.sort(convokes,
				VigilancyWithCredits.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
		return convokes;		
	}
	
	public List<VigilancyWithCredits> getActiveVigilancyWithCredits() {
		List<VigilancyWithCredits> activeConvokes = getActiveVigilancysInList(Vigilancy.getAllVigilancyWithCredits(this));
		Collections.sort(activeConvokes,
				VigilancyWithCredits.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
		return activeConvokes;
	}
	
	@Override
	public List<Vigilancy> getVigilancys() {
		List<Vigilancy> convokes = new ArrayList<Vigilancy>();
		Collections.sort(convokes,
				VigilancyWithCredits.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
		return convokes;
	}

	public List<VigilancyWithCredits> getActiveConvokesBeforeCurrentDate() {
		return getActiveVigilancysInList(this.getConvokesBeforeCurrentDate());
	}
	
	public List<VigilancyWithCredits> getConvokesBeforeCurrentDate() {
		List<Vigilancy> convokes = super.getVigilancys();
		List<VigilancyWithCredits> pastConvokes = new ArrayList<VigilancyWithCredits>();
		YearMonthDay currentDate = new YearMonthDay();

		for (Vigilancy convoke : convokes) {
			if (currentDate.isAfter(convoke.getBeginYearMonthDay()) && (convoke instanceof VigilancyWithCredits)) {
				pastConvokes.add((VigilancyWithCredits)convoke);
			}
		}
		Collections.sort(pastConvokes,
				VigilancyWithCredits.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
		return pastConvokes;

	}

	public List<VigilancyWithCredits> getActiveConvokesAfterCurrentDate() {
		return getActiveVigilancysInList(this.getConvokesAfterCurrentDate());
	}

	private List<VigilancyWithCredits> getActiveVigilancysInList(List<VigilancyWithCredits> vigilancys) {
		List<VigilancyWithCredits> activeVigilancys = new ArrayList<VigilancyWithCredits> ();
		for(VigilancyWithCredits vigilancy : vigilancys) {
			if(vigilancy.isActive()) {
				activeVigilancys.add(vigilancy);
			}
		}
		return activeVigilancys;
	}
	
	
	public List<VigilancyWithCredits> getConvokesAfterCurrentDate() {
		List<Vigilancy> convokes = super.getVigilancys();
		List<VigilancyWithCredits> futureConvokes = new ArrayList<VigilancyWithCredits>();
		YearMonthDay currentDate = new YearMonthDay();

		for (Vigilancy convoke : convokes) {
			if ((currentDate.isBefore(convoke.getBeginYearMonthDay())
					|| currentDate.isEqual(convoke.getBeginYearMonthDay())) && (convoke instanceof VigilancyWithCredits)) {
				futureConvokes.add((VigilancyWithCredits)convoke);
			}
		}
		Collections.sort(futureConvokes,
				VigilancyWithCredits.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
		return futureConvokes;
	}

	public List<VigilancyWithCredits> getVigilancys(VigilantGroup group) {
		return group.getVigilancys(this);
	}

	public boolean hasBeenConvokedForEvaluation(
			WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = this.getVigilancys();
		for (Vigilancy convoke : convokes) {
			if (convoke.getWrittenEvaluation().equals(writtenEvaluation))
				return true;
		}
		return false;
	}

	public String getUnavailablePeriodsAsString() {
		String periods = "";
		int i = 0;
		int size = this.getUnavailablePeriodsCount() - 1;
		for (UnavailablePeriod period : this.getUnavailablePeriods()) {
			periods += period.getUnavailableAsString();
			periods += (i == size) ? " " : ", ";
			i++;
		}
		return periods;
	}

	public String getIncompatiblePersonName() {
		return (hasIncompatiblePerson()) ? getIncompatiblePerson().getName()
				: null;
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
		if (teacher != null
				&& teacher.getServiceExemptionSituations().size() > 0) {
			List<TeacherServiceExemption> situations = teacher
					.getServiceExemptionSituations();
			for (TeacherServiceExemption situation : situations) {
				YearMonthDay beginSituation = situation.getStartYearMonthDay();
				YearMonthDay endSituation = situation.getEndYearMonthDay();
				DateInterval interval = new DateInterval(beginSituation,
						endSituation);
				if (interval.containsDate(begin))
					return UnavailableTypes.SERVICE_EXEMPTION;
			}
		}

		Person person = this.getIncompatiblePerson();
		if (person != null) {
			List<Vigilancy> convokes = writtenEvaluation.getVigilancys();
			for (Vigilancy convoke : convokes) {
				if (convoke.getVigilant().getPerson().equals(person))
					return UnavailableTypes.INCOMPATIBLE_PERSON;
			}
		}

		return UnavailableTypes.UNKNOWN;

	}

	public boolean isCathedraticTeacher() {
		Teacher teacher = this.getTeacher();
		if(teacher!=null) {
			return teacher.getCategory().getWeight()<=3;
		}
		return false;
	}
	
	public String getBoundsAsString() {
		String result = "";
		int i=0;
		for(VigilantBound bound : this.getBounds()) {
			if(!bound.getConvokable()) {
				if(i > 0) result += ", ";
				result += bound.getVigilantGroup().getName() + ": " + bound.getJustification();
				i++;
			}
		}
		return result;
	}
	
	public List<VigilantGroup> getVisibleVigilantGroups() {
		
		Set<VigilantGroup> groups = new HashSet<VigilantGroup> ();
		groups.addAll(this.getVigilantGroups());
		
		Employee employee = this.getPerson().getEmployee();
		if(employee!=null) {
			ExecutionYear executionYear = this.getExecutionYear();
			Department department = employee.getLastDepartmentWorkingPlace(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());
			groups.addAll(department.getVigilantGroupsForGivenExecutionYear(executionYear)) ;

		}
			
		return new ArrayList<VigilantGroup>(groups);
	}
}
