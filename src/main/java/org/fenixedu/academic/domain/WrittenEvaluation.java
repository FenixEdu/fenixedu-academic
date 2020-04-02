/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.Context.DegreeModuleScopeContext;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.util.icalendar.EvaluationEventBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

abstract public class WrittenEvaluation extends WrittenEvaluation_Base {

    public static final Comparator<WrittenEvaluation> COMPARATOR_BY_BEGIN_DATE = new Comparator<WrittenEvaluation>() {

        @Override
        public int compare(WrittenEvaluation o1, WrittenEvaluation o2) {
            final int c1 = o1.getDayDateYearMonthDay().compareTo(o2.getDayDateYearMonthDay());
            if (c1 != 0) {
                return c1;
            }
            final int c2 = o1.getBeginningDateHourMinuteSecond().compareTo(o2.getBeginningDateHourMinuteSecond());
            return c2 == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c2;
        }

    };

    public static List<WrittenEvaluation> readWrittenEvaluations() {
        List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();

        for (Evaluation evaluation : Bennu.getInstance().getEvaluationsSet()) {
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
        Collection<ExecutionCourse> courses = this.getAssociatedExecutionCoursesSet();
        String name = "";
        int i = 0;
        for (ExecutionCourse course : courses) {
            if (i > 0) {
                name = name + ", ";
            }
            name = name + " " + course.getSigla();
            i++;
        }
        return name;
    }

    public String getFullName() {
        Collection<ExecutionCourse> courses = this.getAssociatedExecutionCoursesSet();
        String fullName = "";
        int i = 0;
        for (ExecutionCourse course : courses) {
            if (i > 0) {
                fullName = fullName + ", ";
            }
            fullName = fullName + " " + course.getNome();
            i++;
        }
        return fullName;
    }

    public Space getCampus() {
        List<Space> rooms = getAssociatedRooms();
        if (rooms.size() > 0) {
            return SpaceUtils.getSpaceCampus(rooms.iterator().next());
        } else {
            return null;
        }
    }

    public ExecutionYear getExecutionYear() {
        return this.getAssociatedExecutionCoursesSet().iterator().next().getExecutionYear();
    }

    public ExecutionDegree getExecutionDegree() {
        for (ExecutionCourse cource : getAssociatedExecutionCoursesSet()) {
            for (CurricularCourse curricularCource : cource.getAssociatedCurricularCoursesSet()) {
                return curricularCource.getExecutionDegreeFor(getExecutionYear().getAcademicInterval());
            }
        }
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
                begin.getMinuteOfHour(), 0, 0);

    }

    public DateTime getEndDateTime() {
        HourMinuteSecond end = this.getEndDateHourMinuteSecond();
        YearMonthDay yearMonthDay = this.getDayDateYearMonthDay();
        return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), end.getHour(),
                end.getMinuteOfHour(), 0, 0);

    }

    @Override
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

    public List<Space> getAssociatedRooms() {
        final List<Space> result = new ArrayList<Space>();
        for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupationsSet()) {
            result.add(roomOccupation.getRoom());
        }
        return result;
    }

    protected void checkIntervalBetweenEvaluations() {
        final User userView = Authenticate.getUser();
        if (userView == null || !RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(userView.getPerson().getUser())) {
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
            List<Space> rooms) {

        if (rooms == null) {
            rooms = new ArrayList<Space>(0);
        }

        checkValidHours(beginning, end);

        // Associate ExecutionCourses and Context/Scopes
        getAssociatedExecutionCoursesSet().addAll(executionCoursesToAssociate);
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
        for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupationsSet()) {
            if (!newOccupations.contains(roomOccupation)) {
                final Space room = roomOccupation.getRoom();
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
            throw new DomainException("error.data.exame.invalida");
        }
        return true;
    }

    private void deleteAllRoomOccupations() {
        while (!getWrittenEvaluationSpaceOccupationsSet().isEmpty()) {
            WrittenEvaluationSpaceOccupation occupation = getWrittenEvaluationSpaceOccupationsSet().iterator().next();
            occupation.removeWrittenEvaluations(this);
            occupation.delete();
        }
    }

    public void removeRoomOccupation(Space room) {
        if (hasOccupationForRoom(room)) {
            WrittenEvaluationSpaceOccupation occupation =
                    (WrittenEvaluationSpaceOccupation) SpaceUtils.getFirstOccurrenceOfResourceAllocationByClass(room, this);
            removeWrittenEvaluationSpaceOccupations(occupation);
        }
    }

    protected List<WrittenEvaluationSpaceOccupation> associateNewRooms(final List<Space> rooms) {

        List<WrittenEvaluationSpaceOccupation> newInsertedOccupations = new ArrayList<WrittenEvaluationSpaceOccupation>();
        for (final Space room : rooms) {
            WrittenEvaluationSpaceOccupation spaceOccupation = associateNewRoom(room);
            if (spaceOccupation != null) {
                newInsertedOccupations.add(spaceOccupation);
            }
        }
        return newInsertedOccupations;
    }

    protected WrittenEvaluationSpaceOccupation associateNewRoom(Space room) {
        if (!hasOccupationForRoom(room)) {

            WrittenEvaluationSpaceOccupation occupation =
                    (WrittenEvaluationSpaceOccupation) SpaceUtils.getFirstOccurrenceOfResourceAllocationByClass(room, this);

            occupation = occupation == null ? new WrittenEvaluationSpaceOccupation(room) : occupation;
            occupation.edit(this);
            return occupation;
        } else {
            return null;
        }
    }

    private boolean hasOccupationForRoom(Space room) {
        for (final WrittenEvaluationSpaceOccupation roomOccupation : this.getWrittenEvaluationSpaceOccupationsSet()) {
            if (roomOccupation.getRoom() == room) {
                return true;
            }
        }
        return false;
    }

    protected void edit(Date day, Date beginning, Date end, List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<Space> rooms, GradeScale gradeScale) {

        setAttributesAndAssociateRooms(day, beginning, end, executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms);

        if (getGradeScale() != gradeScale) {
            if (gradeScale != null) {
                setGradeScale(gradeScale);
            } else {
                setGradeScale(GradeScale.TYPE20);
            }
        }

        checkIntervalBetweenEvaluations();
    }

    @Override
    public void delete() {
        if (!getWrittenEvaluationEnrolmentsSet().isEmpty()) {
            throw new DomainException("error.notAuthorizedWrittenEvaluationDelete.withStudent");
        }
        logRemove();
        deleteAllRoomOccupations();
        getAssociatedCurricularCourseScopeSet().clear();
        getAssociatedContextsSet().clear();
        super.delete();
    }

    public void editEnrolmentPeriod(Date enrolmentBeginDay, Date enrolmentEndDay, Date enrolmentBeginTime, Date enrolmentEndTime)
            throws DomainException {

        checkEnrolmentDates(enrolmentBeginDay, enrolmentEndDay, enrolmentBeginTime, enrolmentEndTime);

        this.setEnrollmentBeginDayDate(enrolmentBeginDay);
        this.setEnrollmentEndDayDate(enrolmentEndDay);
        this.setEnrollmentBeginTimeDate(enrolmentBeginTime);
        this.setEnrollmentEndTimeDate(enrolmentEndTime);
        for (ExecutionCourse ec : getAssociatedExecutionCoursesSet()) {
            EvaluationManagementLog.createLog(ec, Bundle.MESSAGING, "log.executionCourse.evaluation.generic.edited.enrolment",
                    getPresentationName(), ec.getName(), ec.getDegreePresentationString());
        }
    }

    private void checkEnrolmentDates(final Date enrolmentBeginDay, final Date enrolmentEndDay, final Date enrolmentBeginTime,
            final Date enrolmentEndTime) throws DomainException {

        final DateTime enrolmentBeginDate = createDate(enrolmentBeginDay, enrolmentBeginTime);
        final DateTime enrolmentEndDate = createDate(enrolmentEndDay, enrolmentEndTime);

        if (getEnrollmentBeginDayDate() == null && enrolmentBeginDate.isBeforeNow()) {
            throw new DomainException("error.beginDate.sooner.today");
        }
        if (enrolmentEndDate.isBefore(enrolmentBeginDate)) {
            throw new DomainException("error.endDate.sooner.beginDate");
        }
        if (getBeginningDateTime().isBefore(enrolmentEndDate)) {
            throw new DomainException("error.examDate.sooner.endDate");
        }
    }

    public void removeEnrolmentPeriod() {
    	getWrittenEvaluationEnrolmentsSet().forEach(we -> we.delete());
    	setEnrollmentBeginDayDateYearMonthDay(null);
        setEnrollmentEndDayDateYearMonthDay(null);
        setEnrollmentBeginTimeDateHourMinuteSecond(null);
        setEnrollmentEndTimeDateHourMinuteSecond(null);        
    }
    
    private DateTime createDate(Date dateDay, Date dateTime) {
        final Calendar day = Calendar.getInstance();
        day.setTime(dateDay);

        final Calendar time = Calendar.getInstance();
        time.setTime(dateTime);

        return new DateTime(day.get(Calendar.YEAR), day.get(Calendar.MONTH) + 1, day.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), 0, 0);
    }

    public void enrolStudent(Registration registration) {
        for (WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration.getWrittenEvaluationEnrolmentsSet()) {
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
            DateTime enrolmentEnd = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
            if (enrolmentEnd.isAfterNow()) {
                return true;
            }
        }
        return false;
    }

    public void distributeStudentsByRooms(List<Registration> studentsToDistribute, List<Space> selectedRooms) {

        this.checkIfCanDistributeStudentsByRooms();
        this.checkRoomsCapacityForStudents(selectedRooms, studentsToDistribute.size());

        for (final Space room : selectedRooms) {
            Integer examCapacity = room.<Integer> getMetadata("examCapacity").orElse(0);
            for (int numberOfStudentsInserted = 0; numberOfStudentsInserted < examCapacity && !studentsToDistribute.isEmpty(); numberOfStudentsInserted++) {
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

        for (ExecutionCourse ec : getAssociatedExecutionCoursesSet()) {
            EvaluationManagementLog.createLog(ec, Bundle.MESSAGING,
                    "log.executionCourse.evaluation.generic.edited.rooms.distributed", getPresentationName(), ec.getName(),
                    ec.getDegreePresentationString());
        }

    }

    public void checkIfCanDistributeStudentsByRooms() {
        if (this.getWrittenEvaluationSpaceOccupationsSet().isEmpty()) {
            throw new DomainException("error.no.roms.associated");
        }

        final Date todayDate = Calendar.getInstance().getTime();
        final Date evaluationDateAndTime;
        try {
            evaluationDateAndTime =
                    DateFormatUtil.parse("yyyy/MM/dd HH:mm", DateFormatUtil.format("yyyy/MM/dd ", this.getDayDate())
                            + DateFormatUtil.format("HH:mm", this.getBeginningDate()));
        } catch (ParseException e) {
            // This should never happen, the string where obtained from other
            // dates.
            throw new Error(e);
        }

        DateTime enrolmentEndDate = null;
        // This can happen if the Enrolment OccupationPeriod for Evaluation
        // wasn't defined
        if (this.getEnrollmentEndDayDate() != null && this.getEnrollmentEndTimeDate() != null) {
            enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
        }
        if (DateFormatUtil.isBefore("yyyy/MM/dd HH:mm", evaluationDateAndTime, todayDate)
                || (enrolmentEndDate != null && enrolmentEndDate.isAfterNow())) {
            throw new DomainException("error.out.of.period.enrollment.period");
        }
    }

    private void checkRoomsCapacityForStudents(final List<Space> selectedRooms, int studentsToDistributeSize) {
        int totalCapacity = selectedRooms.stream().mapToInt(room -> room.<Integer> getMetadata("examCapacity").orElse(0)).sum();

        if (studentsToDistributeSize > totalCapacity) {
            throw new DomainException("error.not.enough.room.space");
        }
    }

    private Registration getRandomStudentFromList(List<Registration> studentsToDistribute) {
        final Random randomizer = new Random();
        int pos = randomizer.nextInt(Math.abs(randomizer.nextInt()));
        return studentsToDistribute.remove(pos % studentsToDistribute.size());
    }

    public WrittenEvaluationEnrolment getWrittenEvaluationEnrolmentFor(final Registration registration) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration.getWrittenEvaluationEnrolmentsSet()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == this) {
                return writtenEvaluationEnrolment;
            }
        }
        return null;
    }

    public WrittenEvaluationEnrolment getWrittenEvaluationEnrolmentFor(final Student student) {
        return student.getActiveRegistrationStream()
                .map(r -> getWrittenEvaluationEnrolmentFor(r))
                .filter(wee -> wee != null)
                .findAny().orElse(null);
    }

    public boolean isInEnrolmentPeriod() {
        if (this.getEnrollmentBeginDayDate() == null || this.getEnrollmentBeginTimeDate() == null
                || this.getEnrollmentEndDayDate() == null || this.getEnrollmentEndTimeDate() == null) {
            throw new DomainException("error.enrolmentPeriodNotDefined");
        }
        final DateTime enrolmentBeginDate = createDate(this.getEnrollmentBeginDayDate(), this.getEnrollmentBeginTimeDate());
        final DateTime enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
        return enrolmentBeginDate.isBeforeNow() && enrolmentEndDate.isAfterNow();
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
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            for (final Attends attends : executionCourse.getAttendsSet()) {
                if (attends.getEnrolment() != null) {
                    i++;
                }
            }
        }
        return i;
    }

    public Integer getCountNumberReservedSeats() {
        return getWrittenEvaluationSpaceOccupationsSet().stream()
                .mapToInt(occupation -> occupation.getRoom().<Integer> getMetadata("examCapacity").orElse(0)).sum();
    }

    public Integer getCountVacancies() {
        final int writtenEvaluationEnrolmentsCount = getWrittenEvaluationEnrolmentsSet().size();
        final int countNumberReservedSeats = getCountNumberReservedSeats().intValue();
        return Integer.valueOf(countNumberReservedSeats - writtenEvaluationEnrolmentsCount);
    }

    public List<DegreeModuleScope> getDegreeModuleScopes() {
        return DegreeModuleScope.getDegreeModuleScopes(this);
    }

    public String getDegreesAsString() {
        Set<Degree> degrees = new HashSet<Degree>();
        for (ExecutionCourse course : this.getAssociatedExecutionCoursesSet()) {
            degrees.addAll(course.getDegreesSortedByDegreeName());
        }
        String degreesAsString = "";
        int i = 0;
        for (Degree degree : degrees) {
            if (i > 0) {
                degreesAsString += ", ";
            }
            degreesAsString += degree.getSigla();
            i++;
        }
        return degreesAsString;
    }

    public String getAssociatedRoomsAsString() {
        String rooms = "";
        for (Space room : getAssociatedRooms()) {
            rooms += room.getName() + "\n";
        }
        return rooms;
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

    public boolean contains(final CurricularCourse curricularCourse) {
        for (final DegreeModuleScope each : getDegreeModuleScopes()) {
            if (each.getCurricularCourse() == curricularCourse) {
                return true;
            }
        }

        return false;
    }

    public Set<DegreeModuleScope> getDegreeModuleScopesFor(CurricularCourse curricularCourse) {
        final Set<DegreeModuleScope> result = new HashSet<DegreeModuleScope>();
        for (final DegreeModuleScope each : getDegreeModuleScopes()) {
            if (each.getCurricularCourse() == curricularCourse) {
                result.add(each);
            }
        }

        return result;
    }

    public abstract boolean canBeAssociatedToRoom(Space room);

    // couldn't find a smarter way to conver ymdhms to DateTiem
    private DateTime convertTimes(YearMonthDay yearMonthDay, HourMinuteSecond hourMinuteSecond) {
        return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(),
                hourMinuteSecond.getHour(), hourMinuteSecond.getMinuteOfHour(), hourMinuteSecond.getSecondOfMinute(), 0);
    }

    protected List<EvaluationEventBean> getAllEvents(String description, Registration registration) {
        List<EvaluationEventBean> result = new ArrayList<EvaluationEventBean>();
        String url = CoreConfiguration.getConfiguration().applicationUrl();

        Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
        executionCourses.addAll(this.getAttendingExecutionCoursesFor(registration));

        if (this.getEnrollmentBeginDayDateYearMonthDay() != null) {
            DateTime enrollmentBegin =
                    convertTimes(this.getEnrollmentBeginDayDateYearMonthDay(), this.getEnrollmentBeginTimeDateHourMinuteSecond());
            DateTime enrollmentEnd =
                    convertTimes(this.getEnrollmentEndDayDateYearMonthDay(), this.getEnrollmentEndTimeDateHourMinuteSecond());

            result.add(new EvaluationEventBean("Inicio das inscrições para " + description, enrollmentBegin, enrollmentBegin
                    .plusHours(1), false, null, url + "/login", null, executionCourses));

            result.add(new EvaluationEventBean("Fim das inscrições para " + description, enrollmentEnd.minusHours(1),
                    enrollmentEnd, false, null, url + "/login", null, executionCourses));
        }

        Set<Space> rooms = new HashSet<>();

        if (registration.getRoomFor(this) != null) {
            rooms.add(registration.getRoomFor(this));
        } else {
            for (WrittenEvaluationSpaceOccupation weSpaceOcupation : this.getWrittenEvaluationSpaceOccupationsSet()) {
                rooms.add(weSpaceOcupation.getRoom());
            }
        }

        WrittenEvaluationEnrolment writtenEvaluationEnrolment = getWrittenEvaluationEnrolmentFor(registration);
        Space assignedRoom = null;

        if (writtenEvaluationEnrolment != null) {
            assignedRoom = writtenEvaluationEnrolment.getRoom();
        }

        result.add(new EvaluationEventBean(description, this.getBeginningDateTime(), this.getEndDateTime(), false, assignedRoom,
                rooms, executionCourses.iterator().next().getSiteUrl(), null, executionCourses));

        return result;
    }

    public Set<Person> getTeachers() {
        Set<Person> persons = new HashSet<Person>();
        for (ExecutionCourse course : getAssociatedExecutionCoursesSet()) {
            for (Professorship professorship : course.getProfessorshipsSet()) {
                persons.add(professorship.getPerson());
            }
        }
        return persons;
    }

    public abstract List<EvaluationEventBean> getAllEvents(Registration registration);

    public String getAssociatedRoomsAsStringList() {
        StringBuilder builder = new StringBuilder("(");
        Iterator<Space> iterator = getAssociatedRooms().iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next().getName());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    /**
     *
     * @deprecated Use getInterval instead.
     */
    @Deprecated
    public Interval getDurationInterval() {
        return new Interval(getBeginningDateTime(), getEndDateTime());
    }

    @Deprecated
    public java.util.Date getBeginningDate() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getBeginningDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setBeginningDate(java.util.Date date) {
        if (date == null) {
            setBeginningDateHourMinuteSecond(null);
        } else {
            setBeginningDateHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getDayDate() {
        org.joda.time.YearMonthDay ymd = getDayDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDayDate(java.util.Date date) {
        if (date == null) {
            setDayDateYearMonthDay(null);
        } else {
            setDayDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getEndDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateHourMinuteSecond(null);
        } else {
            setEndDateHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentBeginDayDate() {
        org.joda.time.YearMonthDay ymd = getEnrollmentBeginDayDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnrollmentBeginDayDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentBeginDayDateYearMonthDay(null);
        } else {
            setEnrollmentBeginDayDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentBeginTimeDate() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getEnrollmentBeginTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnrollmentBeginTimeDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentBeginTimeDateHourMinuteSecond(null);
        } else {
            setEnrollmentBeginTimeDateHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentEndDayDate() {
        org.joda.time.YearMonthDay ymd = getEnrollmentEndDayDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnrollmentEndDayDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentEndDayDateYearMonthDay(null);
        } else {
            setEnrollmentEndDayDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentEndTimeDate() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getEnrollmentEndTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnrollmentEndTimeDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentEndTimeDateHourMinuteSecond(null);
        } else {
            setEnrollmentEndTimeDateHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    public DateTime getEnrolmentPeriodStart() {
        final YearMonthDay yearMonthDay = getEnrollmentBeginDayDateYearMonthDay();
        final HourMinuteSecond hourMinuteSecond = getEnrollmentBeginTimeDateHourMinuteSecond();
        return toDateTime(yearMonthDay, hourMinuteSecond);
    }

    public DateTime getEnrolmentPeriodEnd() {
        final YearMonthDay yearMonthDay = getEnrollmentEndDayDateYearMonthDay();
        final HourMinuteSecond hourMinuteSecond = getEnrollmentEndTimeDateHourMinuteSecond();
        return toDateTime(yearMonthDay, hourMinuteSecond);
    }

    private DateTime toDateTime(final YearMonthDay yearMonthDay, final HourMinuteSecond hourMinuteSecond) {
        if (yearMonthDay == null || hourMinuteSecond == null) {
            return null;
        }
        return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(),
                hourMinuteSecond.getHour(), hourMinuteSecond.getMinuteOfHour(), hourMinuteSecond.getSecondOfMinute(), 0);
    }

    public Interval getInterval() {
        return new Interval(getBeginningDateTime(), getEndDateTime());
    }

    @Override
    public Date getEvaluationDate() {
        return getDayDate();
    }
}
