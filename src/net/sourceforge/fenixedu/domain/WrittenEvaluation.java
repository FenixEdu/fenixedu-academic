package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.IRoom;
import net.sourceforge.fenixedu.domain.space.IRoomOccupation;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

public class WrittenEvaluation extends WrittenEvaluation_Base {

    public WrittenEvaluation() {
    }

    public WrittenEvaluation(Date evaluationDay, Date evaluationBeginningTime, Date evaluationEndTime,
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period) {

        setAttributesAndAssociateRooms(evaluationDay, evaluationBeginningTime, evaluationEndTime,
                executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period);
    }

    public String toString() {
        return "[WRITTEN EVALUATION:" + " id= '" + this.getIdInternal() + "'\n" + " day= '"
                + this.getDay() + "'\n" + " beginning= '" + this.getBeginning() + "'\n" + " end= '"
                + this.getEnd() + "'\n" + "";
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
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period) {

        checkValidHours(beginning, end);

        this.setDayDate(day);
        this.setBeginningDate(beginning);
        this.setEndDate(end);

        this.getAssociatedExecutionCourses().addAll(executionCoursesToAssociate);
        this.getAssociatedCurricularCourseScope().addAll(curricularCourseScopesToAssociate);

        if (rooms != null) {
            associateRooms(rooms, period);
        }
    }

    private boolean checkValidHours(Date beginning, Date end) {
        if (beginning.after(end)) {
            throw new DomainException("error.data.exame.inválida");
        }
        return true;
    }

    private void associateRooms(final List<IRoom> rooms, final IPeriod period) {
        final DiaSemana dayOfWeek = new DiaSemana(this.getDay().get(Calendar.DAY_OF_WEEK));
        for (final IRoom room : rooms) {
            if (!hasOccupationForRoom(room)) {
                room.createRoomOccupation(period, this.getBeginning(), this.getEnd(), dayOfWeek,
                        RoomOccupation.DIARIA, null, this);
            }
        }

        deleteRoomOccupationsNotContainedInList(rooms);
    }

    private boolean hasOccupationForRoom(IRoom room) {
        for (final IRoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
            if (roomOccupation.getRoom() == room) {
                return true;
            }
        }
        return false;
    }

    protected void deleteRoomOccupationsNotContainedInList(final List<IRoom> rooms) {
        for (final IRoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
            if (rooms == null || !rooms.contains(roomOccupation.getRoom())) {
                roomOccupation.delete();
            }
        }
    }

    protected void edit(Date day, Date beginning, Date end,
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period) {
        setAttributesAndAssociateRooms(day, beginning, end, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms, period);
    }

    public void delete() {
        if (hasAnyWrittenEvaluationEnrolments()) {
            throw new DomainException("error.notAuthorizedWrittenEvaluationDelete.withStudent");
        }
        getAssociatedExecutionCourses().clear();
        deleteRoomOccupationsNotContainedInList(null);
        getAssociatedCurricularCourseScope().clear();

        super.deleteDomainObject();
    }

    public List<IRoom> getAssociatedRooms() {
        final List<IRoom> result = new ArrayList<IRoom>();
        for (final IRoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
            result.add(roomOccupation.getRoom());
        }
        return result;
    }

    public void editEnrolmentPeriod(Date enrolmentBeginDay, Date enrolmentEndDay,
            Date enrolmentBeginTime, Date enrolmentEndTime) throws DomainException {

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

    public void enrolStudent(IStudent student) {
        for (IWrittenEvaluationEnrolment writtenEvaluationEnrolment : student
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == this) {
                throw new DomainException("error.alreadyEnrolledError");
            }
        }
        new WrittenEvaluationEnrolment(this, student);
    }

    public void unEnrolStudent(IStudent student) {
        if (!this.validUnEnrollment()) {
            throw new DomainException("error.notAuthorizedUnEnrollment");
        }

        IWrittenEvaluationEnrolment writtenEvaluationEnrolmentToDelete = this
                .getWrittenEvaluationEnrolmentFor(student);
        if (writtenEvaluationEnrolmentToDelete == null) {
            throw new DomainException("error.studentNotEnroled");
        }

        writtenEvaluationEnrolmentToDelete.delete();
    }

    private boolean validUnEnrollment() {
        if (this.getEnrollmentEndDay() != null && this.getEnrollmentEndTime() != null) {
            Date enrolmentEnd = createDate(this.getEnrollmentEndDayDate(), this
                    .getEnrollmentEndTimeDate());
            Date now = Calendar.getInstance().getTime();

            if (enrolmentEnd.after(now)) {
                return true;
            }
        }
        return false;
    }

    public void distributeStudentsByRooms(List<IStudent> studentsToDistribute, List<IRoom> selectedRooms) {

        this.checkIfCanDistributeStudentsByRooms();
        this.checkRoomsCapacityForStudents(selectedRooms, studentsToDistribute.size());

        for (final IRoom room : selectedRooms) {
            for (int numberOfStudentsInserted = 0; numberOfStudentsInserted < room.getCapacidadeExame()
                    && !studentsToDistribute.isEmpty(); numberOfStudentsInserted++) {
                final IStudent student = getRandomStudentFromList(studentsToDistribute);
                final IWrittenEvaluationEnrolment writtenEvaluationEnrolment = this
                        .getWrittenEvaluationEnrolmentFor(student);
                if (writtenEvaluationEnrolment == null) {
                    new WrittenEvaluationEnrolment(this, student, room);
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
        Date enrolmentEndDate = null;
        // This can happen if the Enrolment Period for Evaluation wasn't defined
        if (this.getEnrollmentEndDayDate() != null && this.getEnrollmentEndTimeDate() != null) {
            enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this
                    .getEnrollmentEndTimeDate());
        }
        if (this.getDayDate().before(todayDate)
                || (enrolmentEndDate != null && enrolmentEndDate.after(todayDate))) {
            throw new DomainException("error.out.of.period.enrollment.period");
        }
    }

    private void checkRoomsCapacityForStudents(final List<IRoom> selectedRooms,
            int studentsToDistributeSize) {
        int totalCapacity = 0;
        for (final IRoom room : selectedRooms) {
            totalCapacity += room.getCapacidadeExame();
        }
        if (studentsToDistributeSize > totalCapacity) {
            throw new DomainException("error.not.enough.room.space");
        }
    }

    private IStudent getRandomStudentFromList(List<IStudent> studentsToDistribute) {
        final Random randomizer = new Random();
        int pos = randomizer.nextInt(Math.abs(randomizer.nextInt()));
        return (IStudent) studentsToDistribute.remove(pos % studentsToDistribute.size());
    }

    public IWrittenEvaluationEnrolment getWrittenEvaluationEnrolmentFor(final IStudent student) {
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : student
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
    	for (final IExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
    		for (final IAttends attends : executionCourse.getAttends()) {
    			if (attends.getEnrolment() != null) {
    				i++;
    			}
    		}
    	}
    	return i;
    }

    public Integer getCountNumberReservedSeats() {
    	int i = 0;
    	for (final IRoomOccupation roomOccupation : getAssociatedRoomOccupation()) {
    		i += roomOccupation.getRoom().getCapacidadeExame().intValue();
    	}
    	return i;
    }

    public Integer getCountVacancies() {
        final int writtenEvaluationEnrolmentsCount = getWrittenEvaluationEnrolmentsCount();
        final int countNumberReservedSeats = getCountNumberReservedSeats().intValue();
        return Integer.valueOf(countNumberReservedSeats - writtenEvaluationEnrolmentsCount);
    }

}
