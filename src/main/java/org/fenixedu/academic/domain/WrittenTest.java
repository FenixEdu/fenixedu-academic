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
/*
 * Created on 10/Out/2003
 *
 */
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.space.EventSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonSpaceOccupation;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.icalendar.EvaluationEventBean;
import org.fenixedu.academic.dto.InfoEvaluation;
import org.fenixedu.academic.dto.InfoWrittenTest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
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

        this.getAssociatedExecutionCoursesSet().clear();
        this.getAssociatedCurricularCourseScopeSet().clear();
        this.getAssociatedContextsSet().clear();

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
        final User requestor = Authenticate.getUser();
        if (requestor != null && hasTimeTableManagerPrivledges(requestor) || hasCoordinatorPrivledges(requestor)
                || isTeacher(requestor) && allowedPeriod(date)) {
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
                for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
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
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            final ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
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
        if (requestor != null && RoleType.COORDINATOR.isMember(requestor.getPerson().getUser())) {
            final Person person = requestor.getPerson();
            if (person != null) {
                for (final Coordinator coordinator : person.getCoordinatorsSet()) {
                    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
                    for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()) {
                            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
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
        return requestor != null && RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(requestor.getPerson().getUser());
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

        for (ExecutionCourse ec : getAssociatedExecutionCoursesSet()) {
            EvaluationManagementLog.createLog(ec, Bundle.MESSAGING, "log.executionCourse.evaluation.generic.edited.rooms",
                    getPresentationName(), ec.getNameI18N().getContent(), ec.getDegreePresentationString());
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
        for (Lesson lesson : getAssociatedLessons(room, executionSemester)) {
            if (lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
                if (lesson.getExecutionCourse().teacherLecturesExecutionCourse(teacher)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Lesson> getAssociatedLessons(final Space space, final ExecutionSemester executionSemester) {
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (Occupation spaceOccupation : space.getOccupationSet()) {
            if (spaceOccupation instanceof LessonSpaceOccupation) {
                LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;
                final Lesson lesson = roomOccupation.getLesson();
                if (lesson.getExecutionPeriod() == executionSemester) {
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
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
        return BundleUtil.getString(Bundle.APPLICATION, "label.written.test") + " " + getDescription();
    }

    @Override
    public InfoEvaluation newInfoFromDomain() {
        return InfoWrittenTest.newInfoFromDomain(this);
    }
}
