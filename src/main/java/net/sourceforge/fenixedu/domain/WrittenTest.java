/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.icalendar.EvaluationEventBean;
import net.sourceforge.fenixedu.predicates.WrittenTestPredicates;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 *
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest(Date testDate, Date testStartTime, Date testEndTime, List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<Space> rooms, GradeScale gradeScale,
            String description) {

        super();
        checkEvaluationDate(testDate, executionCoursesToAssociate);
        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms);

        this.setDescription(description);
        if (gradeScale == null) {
            this.setGradeScale(GradeScale.TYPE20);
        } else {
            this.setGradeScale(gradeScale);
        }
        checkIntervalBetweenEvaluations();
        logCreate();
    }

    public void edit(Date testDate, Date testStartTime, Date testEndTime, List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<Space> rooms, GradeScale gradeScale,
            String description) {

        checkEvaluationDate(testDate, executionCoursesToAssociate);

        this.getAssociatedExecutionCourses().clear();
        this.getAssociatedCurricularCourseScope().clear();
        this.getAssociatedContexts().clear();

        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms);

        this.setDescription(description);
        if (gradeScale == null) {
            this.setGradeScale(GradeScale.TYPE20);
        } else {
            this.setGradeScale(gradeScale);
        }
        checkIntervalBetweenEvaluations();
        logEdit();
    }

    @Override
    public void setDayDate(Date date) {
        check(this, WrittenTestPredicates.changeDatePredicate);
        final User requestor = Authenticate.getUser();
        if (hasTimeTableManagerPrivledges(requestor) || hasCoordinatorPrivledges(requestor) || isTeacher(requestor)
                && allowedPeriod(date)) {
            super.setDayDate(date);
        } else {
            throw new DomainException("not.authorized.to.set.this.date");
        }
    }

    private void checkEvaluationDate(final Date writtenEvaluationDate, final List<ExecutionCourse> executionCoursesToAssociate) {

        for (final ExecutionCourse executionCourse : executionCoursesToAssociate) {
            final long beginDate = executionCourse.getExecutionPeriod().getBeginDate().getTime();
            final long endDate = executionCourse.getExecutionPeriod().getEndDate().getTime();
            final DateTime endDateTime = new DateTime(endDate);
            final Interval interval = new Interval(beginDate, endDateTime.plusDays(1).getMillis());
            if (!interval.contains(writtenEvaluationDate.getTime())) {
                throw new DomainException("error.invalidWrittenTestDate");
            }
        }
    }

    private boolean isTeacher(User requestor) {
        if (requestor != null) {
            Person person = requestor.getPerson();
            Teacher teacher = person.getTeacher();
            if (teacher != null) {
                for (ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
                    if (teacher.hasProfessorshipForExecutionCourse(executionCourse)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean allowedPeriod(final Date date) {
        final YearMonthDay yearMonthDay = new YearMonthDay(date.getTime());
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            final ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
                final Date startExamsPeriod;
                if (executionSemester.getSemester().intValue() == 1) {
                    startExamsPeriod = executionDegree.getPeriodExamsFirstSemester().getStart();
                } else if (executionSemester.getSemester().intValue() == 2) {
                    startExamsPeriod = executionDegree.getPeriodExamsSecondSemester().getStart();
                } else {
                    throw new DomainException("unsupported.execution.period.semester");
                }
                if (!new YearMonthDay(startExamsPeriod.getTime()).isAfter(yearMonthDay)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasCoordinatorPrivledges(final User requestor) {
        if (requestor != null && requestor.getPerson().hasRole(RoleType.COORDINATOR)) {
            final Person person = requestor.getPerson();
            if (person != null) {
                for (final Coordinator coordinator : person.getCoordinators()) {
                    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
                    for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
                        if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()) {
                            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                                if (degreeCurricularPlan == curricularCourse.getDegreeCurricularPlan()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean hasTimeTableManagerPrivledges(final User requestor) {
        return requestor != null && requestor.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.TEST_TYPE;
    }

    public boolean canTeacherChooseRoom(ExecutionCourse executionCourse, Teacher teacher) {
        if (executionCourse.teacherLecturesExecutionCourse(teacher)) {
            for (Lesson lesson : executionCourse.getLessons()) {
                if (lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<Space> getTeacherAvailableRooms(Teacher teacher) {
        Collection<Space> rooms = new ArrayList<Space>();
        for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            if (executionCourse.teacherLecturesExecutionCourse(teacher)) {
                for (Lesson lesson : executionCourse.getLessons()) {
                    if (lesson.getRoomOccupation() != null
                            && lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
                        rooms.add(lesson.getRoomOccupation().getRoom());
                    }
                }
            }
        }
        return rooms;
    }

    public Collection<Space> getAvailableRooms() {
        Collection<Space> rooms = new ArrayList<Space>();
        for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            for (Lesson lesson : executionCourse.getLessons()) {
                if (lesson.getRoomOccupation() != null
                        && lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
                    rooms.add(lesson.getRoomOccupation().getRoom());
                }
            }
        }
        return rooms;
    }

    public void teacherEditRooms(Teacher teacher, ExecutionSemester executionSemester, List<Space> rooms) {
        Collection<Space> teacherAvailableRooms = getTeacherAvailableRooms(teacher);
        List<Space> associatedRooms = getAssociatedRooms();

        for (Space room : rooms) {
            if (!associatedRooms.contains(room)) {
                if (!teacherAvailableRooms.contains(room)) {
                    throw new DomainException("error.room.does.not.belong.to.teachers.avaliable.rooms");
                }
                associateNewRoom(room);
            }
        }

        for (Space room : associatedRooms) {
            if (!rooms.contains(room) && canTeacherRemoveRoom(executionSemester, teacher, room)) {
                removeRoomOccupation(room);
            }
        }

        for (ExecutionCourse ec : getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, Bundle.MESSAGING,
                    "log.executionCourse.evaluation.generic.edited.rooms", getPresentationName(), ec.getName(),
                    ec.getDegreePresentationString());
        }

    }

    @Override
    public boolean canBeAssociatedToRoom(Space space) {
        Set<Class<? extends EventSpaceOccupation>> classes = new HashSet<>();
        classes.add(LessonSpaceOccupation.class);
        classes.add(LessonInstanceSpaceOccupation.class);

        return SpaceUtils.isFree(space, getBeginningDateTime().toYearMonthDay(), getEndDateTime().toYearMonthDay(),
                getBeginningDateHourMinuteSecond(), getEndDateHourMinuteSecond(), getDayOfWeek(), null, null, null, classes);
    }

    public boolean canTeacherRemoveRoom(ExecutionSemester executionSemester, Teacher teacher, Space room) {
        for (Lesson lesson : SpaceUtils.getAssociatedLessons(room, executionSemester)) {
            if (lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
                if (lesson.getExecutionCourse().teacherLecturesExecutionCourse(teacher)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<EvaluationEventBean> getAllEvents(Registration registration) {
        return getAllEvents(this.getDescription(), registration);
    }

    public boolean getCanRequestRoom() {
        return this.getRequestRoomSentDate() == null;
    }

    public String getRequestRoomSentDateString() {
        return getRequestRoomSentDate().toString("dd/MM/yyyy HH:mm");
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.written.test") + " "
                + getDescription();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasRequestRoomSentDate() {
        return getRequestRoomSentDate() != null;
    }

}
