package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.Context.DegreeModuleScopeContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

abstract public class WrittenEvaluation extends WrittenEvaluation_Base {

    public static final Comparator<WrittenEvaluation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("dayDateYearMonthDay"));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginningDateHourMinuteSecond"));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public static List<WrittenEvaluation> readWrittenEvaluations() {
	List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();

	for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
	    if (evaluation instanceof Evaluation) {
		result.add((WrittenEvaluation) evaluation);
	    }
	}

	return result;
    }

    protected WrittenEvaluation() {
	super();
    }

    public String getName() {
	List<ExecutionCourse> courses = this.getAssociatedExecutionCourses();
	String name = "";
	int i = 0;
	for (ExecutionCourse course : courses) {
	    if (i > 0)
		name = name + ", ";
	    name = name + " " + course.getSigla();
	    i++;
	}
	return name;
    }

    public String getFullName() {
	List<ExecutionCourse> courses = this.getAssociatedExecutionCourses();
	String fullName = "";
	int i = 0;
	for (ExecutionCourse course : courses) {
	    if (i > 0)
		fullName = fullName + ", ";
	    fullName = fullName + " " + course.getNome();
	    i++;
	}
	return fullName;
    }

    public Campus getCampus() {
	List<AllocatableSpace> rooms = getAssociatedRooms();
	if (rooms.size() > 0) {
	    return rooms.get(0).getSpaceCampus();
	} else
	    return null;
    }

    public Boolean getIsAfterCurrentDate() {
	DateTime currentDate = new DateTime();
	return currentDate.isBefore(this.getBeginningDateTime());
    }

    public DateTime getBeginningDateTime() {
	HourMinuteSecond begin = this.getBeginningDateHourMinuteSecond();
	YearMonthDay yearMonthDay = this.getDayDateYearMonthDay();
	return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), begin.getHour(),
		begin.getMinuteOfHour(), begin.getSecondOfMinute(), 0);

    }

    public DateTime getEndDateTime() {
	HourMinuteSecond end = this.getEndDateHourMinuteSecond();
	YearMonthDay yearMonthDay = this.getDayDateYearMonthDay();
	return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), end.getHour(),
		end.getMinuteOfHour(), end.getSecondOfMinute(), 0);

    }

    public EvaluationType getEvaluationType() {
	return EvaluationType.EXAM_TYPE;
    }

    public Calendar getBeginning() {
	if (this.getBeginningDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getBeginningDate());
	    return result;
	}
	return null;
    }

    public void setBeginning(Calendar calendar) {
	if (calendar != null) {
	    this.setBeginningDate(calendar.getTime());
	} else {
	    this.setBeginningDate(null);
	}
    }

    public Calendar getDay() {
	if (this.getDayDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getDayDate());
	    return result;
	}
	return null;
    }

    public void setDay(Calendar calendar) {
	if (calendar != null) {
	    this.setDayDate(calendar.getTime());
	} else {
	    this.setDayDate(null);
	}
    }

    public List<AllocatableSpace> getAssociatedRooms() {
	final List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupations()) {
	    result.add(roomOccupation.getRoom());
	}
	return result;
    }

    protected void checkIntervalBetweenEvaluations() {
	final IUserView userView = AccessControl.getUserView();
	if (userView == null || !userView.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
	    checkIntervalBetweenEvaluationsCondition();
	}
    }

    public void checkIntervalBetweenEvaluationsCondition() {
	if (getDayDateYearMonthDay() != null && getBeginningDateHourMinuteSecond() != null) {
	    for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
		for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
		    if (evaluation != this && evaluation instanceof WrittenEvaluation) {
			final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
			if (isIntervalBetweenEvaluationsIsLessThan48Hours(this, writtenEvaluation)
				&& hasMatchingCurricularCourseScopeOrContext(writtenEvaluation)) {
			    throw new DomainException("two.evaluations.cannot.occur.withing.48.hours");
			}
		    }
		}
	    }
	}
    }

    private boolean hasMatchingCurricularCourseScopeOrContext(WrittenEvaluation writtenEvaluation) {
	for (final CurricularCourseScope curricularCourseScope : getAssociatedCurricularCourseScopeSet()) {
	    if (writtenEvaluation.getAssociatedCurricularCourseScopeSet().contains(curricularCourseScope)) {
		return true;
	    }
	}
	for (final Context context : getAssociatedContextsSet()) {
	    if (writtenEvaluation.getAssociatedContextsSet().contains(context)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isIntervalBetweenEvaluationsIsLessThan48Hours(final WrittenEvaluation writtenEvaluation1,
	    final WrittenEvaluation writtenEvaluation2) {
	if (writtenEvaluation1.getBeginningDateTime().isBefore(writtenEvaluation2.getBeginningDateTime())) {
	    return !writtenEvaluation1.getBeginningDateTime().plusHours(48).isBefore(writtenEvaluation2.getBeginningDateTime());
	} else {
	    return !writtenEvaluation2.getBeginningDateTime().plusHours(48).isBefore(writtenEvaluation1.getBeginningDateTime());
	}
    }

    public Calendar getEnd() {
	if (this.getEndDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEndDate());
	    return result;
	}
	return null;
    }

    public void setEnd(Calendar calendar) {
	if (calendar != null) {
	    this.setEndDate(calendar.getTime());
	} else {
	    this.setEndDate(null);
	}
    }

    public Calendar getEnrollmentBeginDay() {
	if (this.getEnrollmentBeginDayDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentBeginDayDate());
	    return result;
	}
	return null;
    }

    public void setEnrollmentBeginDay(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentBeginDayDate(calendar.getTime());
	} else {
	    this.setEnrollmentBeginDayDate(null);
	}
    }

    public Calendar getEnrollmentBeginTime() {
	if (this.getEnrollmentBeginTimeDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentBeginTimeDate());
	    return result;
	}
	return null;
    }

    public void setEnrollmentBeginTime(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentBeginTimeDate(calendar.getTime());
	} else {
	    this.setEnrollmentBeginTimeDate(null);
	}
    }

    public Calendar getEnrollmentEndDay() {
	if (this.getEnrollmentEndDayDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentEndDayDate());
	    return result;
	}
	return null;
    }

    public void setEnrollmentEndDay(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentEndDayDate(calendar.getTime());
	} else {
	    this.setEnrollmentEndDayDate(null);
	}
    }

    public Calendar getEnrollmentEndTime() {
	if (this.getEnrollmentEndTimeDate() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentEndTimeDate());
	    return result;
	}
	return null;
    }

    public void setEnrollmentEndTime(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentEndTimeDate(calendar.getTime());
	} else {
	    this.setEnrollmentEndTimeDate(null);
	}
    }

    protected void setAttributesAndAssociateRooms(Date day, Date beginning, Date end,
	    List<ExecutionCourse> executionCoursesToAssociate, List<DegreeModuleScope> curricularCourseScopesToAssociate,
	    List<AllocatableSpace> rooms) {

	if (rooms == null) {
	    rooms = new ArrayList<AllocatableSpace>(0);
	}

	checkValidHours(beginning, end);

	// Associate ExecutionCourses and Context/Scopes
	getAssociatedExecutionCourses().addAll(executionCoursesToAssociate);
	for (DegreeModuleScope degreeModuleScope : curricularCourseScopesToAssociate) {
	    if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
		addAssociatedCurricularCourseScope(((DegreeModuleScopeCurricularCourseScope) degreeModuleScope)
			.getCurricularCourseScope());
	    } else if (degreeModuleScope instanceof DegreeModuleScopeContext) {
		addAssociatedContexts(((DegreeModuleScopeContext) degreeModuleScope).getContext());
	    }
	}

	setDayDate(day);
	setBeginningDate(beginning);
	setEndDate(end);

	// Associate New Rooms
	List<WrittenEvaluationSpaceOccupation> newOccupations = associateNewRooms(rooms);

	// Edit Existent Rooms
	final Set<WrittenEvaluationSpaceOccupation> roomOccupationsToDelete = new HashSet<WrittenEvaluationSpaceOccupation>();
	for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupations()) {
	    if (!newOccupations.contains(roomOccupation)) {
		final AllocatableSpace room = roomOccupation.getRoom();
		if (!rooms.contains(room)) {
		    roomOccupationsToDelete.add(roomOccupation);
		} else {
		    roomOccupation.edit(this);
		}
	    }
	}

	// Delete Rooms
	for (Iterator<WrittenEvaluationSpaceOccupation> iter = roomOccupationsToDelete.iterator(); iter.hasNext();) {
	    WrittenEvaluationSpaceOccupation occupation = iter.next();
	    occupation.removeWrittenEvaluations(this);
	    iter.remove();
	    occupation.delete();
	}
    }

    private boolean checkValidHours(Date beginning, Date end) {
	if (beginning.after(end)) {
	    throw new DomainException("error.data.exame.inv�lida");
	}
	return true;
    }

    private void deleteAllRoomOccupations() {
	while (hasAnyWrittenEvaluationSpaceOccupations()) {
	    WrittenEvaluationSpaceOccupation occupation = getWrittenEvaluationSpaceOccupations().get(0);
	    occupation.removeWrittenEvaluations(this);
	    occupation.delete();
	}
    }

    private List<WrittenEvaluationSpaceOccupation> associateNewRooms(final List<AllocatableSpace> rooms) {

	List<WrittenEvaluationSpaceOccupation> newInsertedOccupations = new ArrayList<WrittenEvaluationSpaceOccupation>();
	for (final AllocatableSpace room : rooms) {
	    if (!hasOccupationForRoom(room)) {

		WrittenEvaluationSpaceOccupation occupation = (WrittenEvaluationSpaceOccupation) room
			.getFirstOccurrenceOfResourceAllocationByClass(WrittenEvaluationSpaceOccupation.class);

		occupation = occupation == null ? new WrittenEvaluationSpaceOccupation(room) : occupation;
		occupation.edit(this);

		newInsertedOccupations.add(occupation);
	    }
	}
	return newInsertedOccupations;
    }

    private boolean hasOccupationForRoom(AllocatableSpace room) {
	for (final WrittenEvaluationSpaceOccupation roomOccupation : this.getWrittenEvaluationSpaceOccupations()) {
	    if (roomOccupation.getRoom() == room) {
		return true;
	    }
	}
	return false;
    }

    protected void edit(Date day, Date beginning, Date end, List<ExecutionCourse> executionCoursesToAssociate,
	    List<DegreeModuleScope> curricularCourseScopesToAssociate, List<AllocatableSpace> rooms) {

	setAttributesAndAssociateRooms(day, beginning, end, executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms);
	checkIntervalBetweenEvaluations();
    }

    public void delete() {
	if (hasAnyWrittenEvaluationEnrolments()) {
	    throw new DomainException("error.notAuthorizedWrittenEvaluationDelete.withStudent");
	}
	deleteAllVigilanciesAssociated();
	deleteAllRoomOccupations();
	getAssociatedCurricularCourseScope().clear();
	getAssociatedContextsSet().clear();
	super.delete();
    }

    private void deleteAllVigilanciesAssociated() {
	for (; !this.getVigilancies().isEmpty(); this.getVigilancies().get(0).delete())
	    ;
    }

    public void editEnrolmentPeriod(Date enrolmentBeginDay, Date enrolmentEndDay, Date enrolmentBeginTime, Date enrolmentEndTime)
	    throws DomainException {

	checkEnrolmentDates(enrolmentBeginDay, enrolmentEndDay, enrolmentBeginTime, enrolmentEndTime);

	this.setEnrollmentBeginDayDate(enrolmentBeginDay);
	this.setEnrollmentEndDayDate(enrolmentEndDay);
	this.setEnrollmentBeginTimeDate(enrolmentBeginTime);
	this.setEnrollmentEndTimeDate(enrolmentEndTime);
    }

    private void checkEnrolmentDates(final Date enrolmentBeginDay, final Date enrolmentEndDay, final Date enrolmentBeginTime,
	    final Date enrolmentEndTime) throws DomainException {

	final Date enrolmentBeginDate = createDate(enrolmentBeginDay, enrolmentBeginTime);
	final Date enrolmentEndDate = createDate(enrolmentEndDay, enrolmentEndTime);

	if (enrolmentBeginDate.before(Calendar.getInstance().getTime())) {
	    throw new DomainException("error.beginDate.sooner.today");
	}
	if (enrolmentEndDate.before(enrolmentBeginDate)) {
	    throw new DomainException("error.endDate.sooner.beginDate");
	}
	if (this.getDayDate().before(enrolmentEndDate)) {
	    throw new DomainException("error.examDate.sooner.endDate");
	}
    }

    private Date createDate(Date dateDay, Date dateTime) {
	final Calendar date = Calendar.getInstance();

	final Calendar day = Calendar.getInstance();
	day.setTime(dateDay);

	final Calendar time = Calendar.getInstance();
	time.setTime(dateTime);

	date.set(Calendar.YEAR, day.get(Calendar.YEAR));
	date.set(Calendar.MONTH, day.get(Calendar.MONTH));
	date.set(Calendar.DAY_OF_MONTH, day.get(Calendar.DAY_OF_MONTH));
	date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
	date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
	date.set(Calendar.SECOND, 0);

	return date.getTime();
    }

    public void enrolStudent(Registration registration) {
	for (WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() == this) {
		throw new DomainException("error.alreadyEnrolledError");
	    }
	}
	new WrittenEvaluationEnrolment(this, registration);
    }

    public void unEnrolStudent(Registration registration) {
	if (!this.validUnEnrollment()) {
	    throw new DomainException("error.notAuthorizedUnEnrollment");
	}

	WrittenEvaluationEnrolment writtenEvaluationEnrolmentToDelete = this.getWrittenEvaluationEnrolmentFor(registration);
	if (writtenEvaluationEnrolmentToDelete == null) {
	    throw new DomainException("error.studentNotEnroled");
	}

	writtenEvaluationEnrolmentToDelete.delete();
    }

    private boolean validUnEnrollment() {
	if (this.getEnrollmentEndDay() != null && this.getEnrollmentEndTime() != null) {
	    Date enrolmentEnd = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
	    Date now = Calendar.getInstance().getTime();

	    if (enrolmentEnd.after(now)) {
		return true;
	    }
	}
	return false;
    }

    public void distributeStudentsByRooms(List<Registration> studentsToDistribute, List<AllocatableSpace> selectedRooms) {

	this.checkIfCanDistributeStudentsByRooms();
	this.checkRoomsCapacityForStudents(selectedRooms, studentsToDistribute.size());

	for (final AllocatableSpace room : selectedRooms) {
	    for (int numberOfStudentsInserted = 0; numberOfStudentsInserted < room.getCapacidadeExame()
		    && !studentsToDistribute.isEmpty(); numberOfStudentsInserted++) {
		final Registration registration = getRandomStudentFromList(studentsToDistribute);
		final WrittenEvaluationEnrolment writtenEvaluationEnrolment = this.getWrittenEvaluationEnrolmentFor(registration);
		if (writtenEvaluationEnrolment == null) {
		    new WrittenEvaluationEnrolment(this, registration, room);
		} else {
		    writtenEvaluationEnrolment.setRoom(room);
		}
	    }
	    if (studentsToDistribute.isEmpty()) {
		break;
	    }
	}
    }

    public void checkIfCanDistributeStudentsByRooms() {
	if (!this.hasAnyWrittenEvaluationSpaceOccupations()) {
	    throw new DomainException("error.no.roms.associated");
	}

	final Date todayDate = Calendar.getInstance().getTime();
	final Date evaluationDateAndTime;
	try {
	    evaluationDateAndTime = DateFormatUtil.parse("yyyy/MM/dd HH:mm", DateFormatUtil.format("yyyy/MM/dd ", this
		    .getDayDate())
		    + DateFormatUtil.format("HH:mm", this.getBeginningDate()));
	} catch (ParseException e) {
	    // This should never happen, the string where obtained from other
	    // dates.
	    throw new Error(e);
	}

	Date enrolmentEndDate = null;
	// This can happen if the Enrolment OccupationPeriod for Evaluation
	// wasn't defined
	if (this.getEnrollmentEndDayDate() != null && this.getEnrollmentEndTimeDate() != null) {
	    enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
	}
	if (DateFormatUtil.isBefore("yyyy/MM/dd HH:mm", evaluationDateAndTime, todayDate)
		|| (enrolmentEndDate != null && enrolmentEndDate.after(todayDate))) {
	    throw new DomainException("error.out.of.period.enrollment.period");
	}
    }

    private void checkRoomsCapacityForStudents(final List<AllocatableSpace> selectedRooms, int studentsToDistributeSize) {
	int totalCapacity = 0;
	for (final AllocatableSpace room : selectedRooms) {
	    totalCapacity += room.getCapacidadeExame();
	}
	if (studentsToDistributeSize > totalCapacity) {
	    throw new DomainException("error.not.enough.room.space");
	}
    }

    private Registration getRandomStudentFromList(List<Registration> studentsToDistribute) {
	final Random randomizer = new Random();
	int pos = randomizer.nextInt(Math.abs(randomizer.nextInt()));
	return (Registration) studentsToDistribute.remove(pos % studentsToDistribute.size());
    }

    public WrittenEvaluationEnrolment getWrittenEvaluationEnrolmentFor(final Registration registration) {
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() == this) {
		return writtenEvaluationEnrolment;
	    }
	}
	return null;
    }

    public boolean isInEnrolmentPeriod() {
	final Date now = Calendar.getInstance().getTime();
	if (this.getEnrollmentBeginDayDate() == null || this.getEnrollmentBeginTimeDate() == null
		|| this.getEnrollmentEndDayDate() == null || this.getEnrollmentEndTimeDate() == null) {
	    throw new DomainException("error.enrolmentPeriodNotDefined");
	}
	final Date enrolmentBeginDate = createDate(this.getEnrollmentBeginDayDate(), this.getEnrollmentBeginTimeDate());
	final Date enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
	return enrolmentBeginDate.before(now) && enrolmentEndDate.after(now);
    }

    public boolean getIsInEnrolmentPeriod() {
	try { // Used for sorting purpose
	    return isInEnrolmentPeriod();
	} catch (final DomainException e) {
	    return false;
	}
    }

    public Integer getCountStudentsEnroledAttendingExecutionCourses() {
	int i = 0;
	for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
	    for (final Attends attends : executionCourse.getAttends()) {
		if (attends.getEnrolment() != null) {
		    i++;
		}
	    }
	}
	return i;
    }

    public Integer getCountNumberReservedSeats() {
	int i = 0;
	for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupations()) {
	    i += ((AllocatableSpace) roomOccupation.getRoom()).getCapacidadeExame().intValue();
	}
	return i;
    }

    public Integer getCountVacancies() {
	final int writtenEvaluationEnrolmentsCount = getWrittenEvaluationEnrolmentsCount();
	final int countNumberReservedSeats = getCountNumberReservedSeats().intValue();
	return Integer.valueOf(countNumberReservedSeats - writtenEvaluationEnrolmentsCount);
    }

    public List<DegreeModuleScope> getDegreeModuleScopes() {
	return DegreeModuleScope.getDegreeModuleScopes(this);
    }

    public String getDegreesAsString() {
	Set<Degree> degrees = new HashSet<Degree>();
	for (ExecutionCourse course : this.getAssociatedExecutionCourses()) {
	    degrees.addAll(course.getDegreesSortedByDegreeName());
	}
	String degreesAsString = "";
	int i = 0;
	for (Degree degree : degrees) {
	    if (i > 0)
		degreesAsString += ", ";
	    degreesAsString += degree.getSigla();
	    i++;
	}
	return degreesAsString;
    }

    public List<Vigilancy> getTeachersVigilancies() {
	List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : this.getVigilancies()) {
	    if (vigilancy.isOwnCourseVigilancy()) {
		vigilancies.add((Vigilancy) vigilancy);
	    }
	}
	return vigilancies;
    }

    public List<Vigilancy> getOthersVigilancies() {
	List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : this.getVigilancies()) {
	    if (vigilancy.isOtherCourseVigilancy()) {
		vigilancies.add((Vigilancy) vigilancy);
	    }
	}
	return vigilancies;
    }

    public List<Vigilancy> getActiveOtherVigilancies() {
	List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : this.getVigilancies()) {
	    if (vigilancy.isOtherCourseVigilancy() && vigilancy.isActive()) {
		vigilancies.add((Vigilancy) vigilancy);
	    }
	}
	return vigilancies;
    }

    public List<Vigilancy> getAllActiveVigilancies() {
	List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : this.getVigilancies()) {
	    if (vigilancy.isActive()) {
		vigilancies.add(vigilancy);
	    }
	}
	return vigilancies;
    }

    public Set<VigilantGroup> getAssociatedVigilantGroups() {
	Set<VigilantGroup> groups = new HashSet<VigilantGroup>();
	for (ExecutionCourse course : getAssociatedExecutionCourses()) {
	    if (course.hasVigilantGroup()) {
		groups.add(course.getVigilantGroup());
	    }
	}
	return groups;
    }

    public String getAssociatedRoomsAsString() {
	String rooms = "";
	for (AllocatableSpace room : getAssociatedRooms()) {
	    rooms += room.getName() + "\n";
	}
	return rooms;
    }

    public List<Vigilancy> getActiveVigilancies() {
	List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
	for (Vigilancy vigilancy : this.getVigilancies()) {
	    if (vigilancy.isActive()) {
		vigilancies.add(vigilancy);
	    }
	}
	return vigilancies;
    }

    public boolean hasScopeFor(final Integer year, final Integer semester, DegreeCurricularPlan degreeCurricularPlan) {
	for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
	    if (degreeModuleScope.getCurricularYear().equals(year) && degreeModuleScope.getCurricularSemester().equals(semester)
		    && degreeModuleScope.getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasScopeOrContextFor(List<Integer> curricularYears, DegreeCurricularPlan degreeCurricularPlan) {
	if (curricularYears != null && degreeCurricularPlan != null) {
	    for (final DegreeModuleScope scope : getDegreeModuleScopes()) {
		if (curricularYears.contains(scope.getCurricularYear())
			&& degreeCurricularPlan.equals(scope.getCurricularCourse().getDegreeCurricularPlan())) {
		    return true;
		}
	    }
	}
	return false;
    }

    public DiaSemana getDayOfWeek() {
	return new DiaSemana(DiaSemana.getDiaSemana(getDayDateYearMonthDay()));
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	HourMinuteSecond beginTime = getBeginningDateHourMinuteSecond();
	HourMinuteSecond endTime = getEndDateHourMinuteSecond();
	return getDayDateYearMonthDay() != null && beginTime != null && endTime != null && endTime.isAfter(beginTime);
    }

    public boolean isExam() {
	return false;
    }
}
