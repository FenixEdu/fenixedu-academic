package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.Context.DegreeModuleScopeContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class WrittenEvaluation extends WrittenEvaluation_Base {

	public static final Comparator<WrittenEvaluation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
	static {
		((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("dayDateYearMonthDay"));
		((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator(
				"beginningDateHourMinuteSecond"));
		((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
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

	public WrittenEvaluation() {
		super();
	}

	public WrittenEvaluation(Date evaluationDay, Date evaluationBeginningTime, Date evaluationEndTime,
			List<ExecutionCourse> executionCoursesToAssociate,
			List<DegreeModuleScope> curricularCourseScopesToAssociate, List<OldRoom> rooms,
			OccupationPeriod period) {
		this();
		setAttributesAndAssociateRooms(evaluationDay, evaluationBeginningTime, evaluationEndTime,
				executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period);
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
		List<OldRoom> rooms = this.getAssociatedRooms();
		if (rooms.size() > 0) {
			return rooms.get(0).getBuilding().getCampus();
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
		return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay
				.getDayOfMonth(), begin.getHour(), begin.getMinuteOfHour(), begin.getSecondOfMinute(), 0);

	}

	public DateTime getEndDateTime() {
		HourMinuteSecond end = this.getEndDateHourMinuteSecond();
		YearMonthDay yearMonthDay = this.getDayDateYearMonthDay();
		return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay
				.getDayOfMonth(), end.getHour(), end.getMinuteOfHour(), end.getSecondOfMinute(), 0);

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

    protected void checkIntervalBetweenEvaluations() {
	if (getDayDateYearMonthDay() != null && getBeginningDateHourMinuteSecond() != null) {
	    for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
		for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
		    if (evaluation != this && evaluation instanceof WrittenEvaluation) {
			final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
			if (isIntervalBetweenEvaluationsIsLessThan48Hours(this, writtenEvaluation) && hasMatchingCurricularCourseScopeOrContext(writtenEvaluation)) {
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

    private void checkIntervalBetweenEvaluations(Set<WrittenEvaluation> associatedWrittenEvaluationsSet) {
	System.out.println("Running check for: " + getIdInternal());
	for (final WrittenEvaluation writtenEvaluation : associatedWrittenEvaluationsSet) {
	    if (writtenEvaluation != this) {
		System.out.println("Testing against: " + writtenEvaluation.getIdInternal());
		System.out.println("   t1: " + containsCommonExecutionCourse(getAssociatedExecutionCoursesSet(), writtenEvaluation.getAssociatedExecutionCoursesSet()));
		System.out.println("   t2: " + isIntervalBetweenEvaluationsIsLessThan48Hours(this, writtenEvaluation));
		if (containsCommonExecutionCourse(getAssociatedExecutionCoursesSet(), writtenEvaluation.getAssociatedExecutionCoursesSet())
			&& isIntervalBetweenEvaluationsIsLessThan48Hours(this, writtenEvaluation)) {
		    throw new DomainException("two.evaluations.cannot.occur.withing.48.hours");
		}
	    }
	}
    }

    private boolean containsCommonExecutionCourse(Set<ExecutionCourse> associatedExecutionCoursesSet1, Set<ExecutionCourse> associatedExecutionCoursesSet2) {
	for (final ExecutionCourse executionCourse : associatedExecutionCoursesSet2) {
	    if (associatedExecutionCoursesSet1.contains(executionCourse)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isIntervalBetweenEvaluationsIsLessThan48Hours(final WrittenEvaluation writtenEvaluation1, final WrittenEvaluation writtenEvaluation2) {
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
            List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<OldRoom> rooms,
            OccupationPeriod period) {
       
	if (rooms == null) {
            rooms = new ArrayList<OldRoom>(0);
        }

        checkValidHours(beginning, end);

        this.getAssociatedExecutionCourses().addAll(executionCoursesToAssociate);
        for (DegreeModuleScope degreeModuleScope : curricularCourseScopesToAssociate) {
            if(degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                this.addAssociatedCurricularCourseScope(((DegreeModuleScopeCurricularCourseScope)degreeModuleScope).
                        getCurricularCourseScope());
            } else if(degreeModuleScope instanceof DegreeModuleScopeContext) {                
                this.addAssociatedContexts(((DegreeModuleScopeContext)degreeModuleScope).getContext());
            }
        }        

        this.setDayDate(day);
        this.setBeginningDate(beginning);
        this.setEndDate(end);

        final DiaSemana dayOfWeek = new DiaSemana(this.getDay().get(Calendar.DAY_OF_WEEK));

        associateRooms(rooms, period);

        final Set<RoomOccupation> roomOccupationsToDelete = new HashSet<RoomOccupation>();
        for (final RoomOccupation roomOccupation : getAssociatedRoomOccupation()) {
            final Room room = roomOccupation.getRoom();
            if (!rooms.contains(room)) {
                roomOccupationsToDelete.add(roomOccupation);
            } else {                
                roomOccupation.setDayOfWeek(dayOfWeek);
                roomOccupation.setEndTimeDate(end);
                roomOccupation.setStartTimeDate(beginning);
                roomOccupation.setPeriod(period);
            }
        }

        for (final RoomOccupation roomOccupation : roomOccupationsToDelete) {
            roomOccupation.delete();
        }

        final Set<OldRoom> occupiedRooms = new HashSet<OldRoom>();
        for (final RoomOccupation roomOccupation : getAssociatedRoomOccupationSet()) {
            if (((OldRoom)roomOccupation.getRoom()).findOccupationSet(period, this.getBeginning(), this.getEnd(), roomOccupation.getDayOfWeek(), 
        	    null, null, Boolean.TRUE, Boolean.TRUE).size() > 1) {
                occupiedRooms.add((OldRoom) roomOccupation.getRoom());
            }
        }
        if (!occupiedRooms.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final OldRoom oldRoom : occupiedRooms) {
                stringBuilder.append(oldRoom.getNome()).append(" ");
            }
            throw new DomainException("error.occupied.rooms", stringBuilder.toString());
        }
    }


	private boolean checkValidHours(Date beginning, Date end) {
		if (beginning.after(end)) {
			throw new DomainException("error.data.exame.inv�lida");
		}
		return true;
	}

    private void deleteAllRoomOccupations() {
        for (; !getAssociatedRoomOccupation().isEmpty(); getAssociatedRoomOccupation().get(0).delete());
    }

    private void associateRooms(final List<OldRoom> rooms, final OccupationPeriod period) {
        final DiaSemana dayOfWeek = new DiaSemana(this.getDay().get(Calendar.DAY_OF_WEEK));
        for (final OldRoom room : rooms) {
            if (!hasOccupationForRoom(room)) {
                room.createRoomOccupationForWrittenEvaluations(period, this.getBeginning(), this.getEnd(), dayOfWeek, this);
            }
        }
    }

	private boolean hasOccupationForRoom(OldRoom room) {
		for (final RoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
			if (roomOccupation.getRoom() == room) {
				return true;
			}
		}
		return false;
	}

    protected void edit(Date day, Date beginning, Date end,
            List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<OldRoom> rooms,
            OccupationPeriod period) {
        setAttributesAndAssociateRooms(day, beginning, end, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms, period);
        checkIntervalBetweenEvaluations();
    }

	public void delete() {
		if (hasAnyWrittenEvaluationEnrolments()) {
			throw new DomainException("error.notAuthorizedWrittenEvaluationDelete.withStudent");
		}
		// getAssociatedExecutionCourses().clear();
		deleteAllVigilanciesAssociated();
		deleteAllRoomOccupations();
		getAssociatedCurricularCourseScope().clear();
		super.delete();
	}

	private void deleteAllVigilanciesAssociated() {
		for (; !this.getVigilancies().isEmpty(); this.getVigilancies().get(0).delete())
			;
	}

	public List<OldRoom> getAssociatedRooms() {
		final List<OldRoom> result = new ArrayList<OldRoom>();
		for (final RoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
			result.add((OldRoom) roomOccupation.getRoom());
		}
		return result;
	}

	public void editEnrolmentPeriod(Date enrolmentBeginDay, Date enrolmentEndDay, Date enrolmentBeginTime,
			Date enrolmentEndTime) throws DomainException {

		checkEnrolmentDates(enrolmentBeginDay, enrolmentEndDay, enrolmentBeginTime, enrolmentEndTime);

		this.setEnrollmentBeginDayDate(enrolmentBeginDay);
		this.setEnrollmentEndDayDate(enrolmentEndDay);
		this.setEnrollmentBeginTimeDate(enrolmentBeginTime);
		this.setEnrollmentEndTimeDate(enrolmentEndTime);
	}

	private void checkEnrolmentDates(final Date enrolmentBeginDay, final Date enrolmentEndDay,
			final Date enrolmentBeginTime, final Date enrolmentEndTime) throws DomainException {

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
		for (WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration
				.getWrittenEvaluationEnrolments()) {
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

		WrittenEvaluationEnrolment writtenEvaluationEnrolmentToDelete = this
				.getWrittenEvaluationEnrolmentFor(registration);
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

	public void distributeStudentsByRooms(List<Registration> studentsToDistribute, List<OldRoom> selectedRooms) {

		this.checkIfCanDistributeStudentsByRooms();
		this.checkRoomsCapacityForStudents(selectedRooms, studentsToDistribute.size());

		for (final OldRoom room : selectedRooms) {
			for (int numberOfStudentsInserted = 0; numberOfStudentsInserted < room.getCapacidadeExame()
					&& !studentsToDistribute.isEmpty(); numberOfStudentsInserted++) {
				final Registration registration = getRandomStudentFromList(studentsToDistribute);
				final WrittenEvaluationEnrolment writtenEvaluationEnrolment = this
						.getWrittenEvaluationEnrolmentFor(registration);
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
		if (!this.hasAnyAssociatedRoomOccupation()) {
			throw new DomainException("error.no.roms.associated");
		}

		final Date todayDate = Calendar.getInstance().getTime();
		final Date evaluationDateAndTime;
		try {
			evaluationDateAndTime = DateFormatUtil.parse("yyyy/MM/dd HH:mm", DateFormatUtil.format(
					"yyyy/MM/dd ", this.getDayDate())
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

	private void checkRoomsCapacityForStudents(final List<OldRoom> selectedRooms, int studentsToDistributeSize) {
		int totalCapacity = 0;
		for (final OldRoom room : selectedRooms) {
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
		for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration
				.getWrittenEvaluationEnrolments()) {
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
		final Date enrolmentBeginDate = createDate(this.getEnrollmentBeginDayDate(), this
				.getEnrollmentBeginTimeDate());
		final Date enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this
				.getEnrollmentEndTimeDate());
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
		for (final RoomOccupation roomOccupation : getAssociatedRoomOccupation()) {
			i += ((OldRoom)roomOccupation.getRoom()).getCapacidadeExame().intValue();
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
	
	public List<Vigilancy> getAllActiveVigilancies() {
		List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
		for (Vigilancy vigilancy : this.getVigilancies()) {
			if(vigilancy.isActive()) {
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
		for (OldRoom room : this.getAssociatedRooms()) {
			rooms += room.getName() + "\n";
		}
		return rooms;
	}
}
