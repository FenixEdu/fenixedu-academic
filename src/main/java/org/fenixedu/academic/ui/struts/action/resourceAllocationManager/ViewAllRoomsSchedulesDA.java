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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftEnrolment;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonSpaceOccupation;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMSchedulesApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.DateTime;

import com.google.common.collect.Ordering;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
@StrutsFunctionality(app = RAMSchedulesApp.class, path = "room-schedules", titleKey = "link.schedules.listAllByRoom")
@Mapping(path = "/viewAllRoomsSchedulesDA", module = "resourceAllocationManager")
@Forwards({ @Forward(name = "choose", path = "/resourceAllocationManager/choosePavillionsToViewRoomsSchedules.jsp"),
        @Forward(name = "list", path = "/resourceAllocationManager/viewAllRoomsSchedules.jsp") })
public class ViewAllRoomsSchedulesDA extends FenixDispatchAction {

    public static class ChooseBuildingBean implements Serializable {

        private static final long serialVersionUID = -663198492313971329L;

        private AcademicInterval academicInterval;
        private List<Space> selectedBuildings;

        public ChooseBuildingBean() {
            this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
        }

        public AcademicInterval getAcademicInterval() {
            return academicInterval;
        }

        public void setAcademicInterval(AcademicInterval academicInterval) {
            this.academicInterval = academicInterval;
        }

        public List<Space> getAvailableBuildings() {
            return Ordering.from(SpaceUtils.COMPARATOR_BY_PRESENTATION_NAME).sortedCopy(SpaceUtils.buildings());
        }

        public List<AcademicInterval> getAvailableIntervals() {
            return Ordering.from(AcademicInterval.COMPARATOR_BY_BEGIN_DATE).reverse()
                    .sortedCopy(AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER));
        }

        public List<Space> getSelectedBuildings() {
            return selectedBuildings;
        }

        public void setSelectedBuildings(List<Space> selectedBuildings) {
            this.selectedBuildings = selectedBuildings;
        }
    }

    @EntryPoint
    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ChooseBuildingBean bean = getRenderedObject();
        if (bean == null) {
            bean = new ChooseBuildingBean();
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);
        return mapping.findForward("choose");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ChooseBuildingBean bean = getRenderedObject();

        List<Space> rooms = new ArrayList<Space>();
        for (Space building : bean.getSelectedBuildings()) {
            rooms.addAll(SpaceUtils.getAllActiveSubRoomsForEducation(building));
        }

        final List<RoomLessonsBean> beans = new ArrayList<RoomLessonsBean>();
        for (final Space room : rooms) {
            if (!StringUtils.isEmpty(room.getName())) {
                final Collection<Lesson> lessons = getAssociatedLessons(room, bean.getAcademicInterval());
                final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(lessons.size());
                for (Lesson lesson : lessons) {
                    infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
                }
                beans.add(new RoomLessonsBean(room, infoLessons));
            }
        }

        request.setAttribute("academicInterval", bean.getAcademicInterval());
        request.setAttribute("beans", beans);
        return mapping.findForward("list");
    }

    private static Collection<Lesson> getAssociatedLessons(Space space, AcademicInterval academicInterval) {
        final Set<Lesson> lessons = new HashSet<>();
        for (Occupation spaceOccupation : space.getOccupationSet()) {
            if (spaceOccupation instanceof LessonSpaceOccupation) {
                LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;
                final Lesson lesson = roomOccupation.getLesson();
                if (lesson.getAcademicInterval().equals(academicInterval)) {
                    lessons.add(lesson);
                }
            }
            if (spaceOccupation instanceof LessonInstanceSpaceOccupation) {
                LessonInstanceSpaceOccupation roomOccupation = (LessonInstanceSpaceOccupation) spaceOccupation;
                roomOccupation.getLessonInstancesSet().stream().map(LessonInstance::getLesson)
                        .filter(lesson -> lesson.getAcademicInterval().equals(academicInterval)).forEach(lessons::add);
            }
        }
        return lessons;
    }

    public static class RoomLessonsBean {
        private final Space room;
        private final List<InfoLesson> lessons;

        public RoomLessonsBean(Space room, List<InfoLesson> lessons) {
            super();
            this.room = room;
            this.lessons = lessons;
        }

        public Space getRoom() {
            return room;
        }

        public List<InfoLesson> getLessons() {
            return lessons;
        }

    }

    private static final int WEEKDAY_COUNT = 6;
    private static final int HOUR_COUNT = (24 - 8) * 2;

    private static class RoomMap extends HashMap<Space, boolean[][]> {

        @Override
        public boolean[][] get(final Object key) {
            boolean[][] value = super.get(key);
            if (value == null) {
                value = new boolean[WEEKDAY_COUNT][HOUR_COUNT];
                put((Space) key, value);
            }
            return value;
        }

        public void register(final Space allocatableSpace) {
            if (allocatableSpace != null) {
                get(allocatableSpace);
            }
        }

        public void register(final Lesson lesson) {
            final LessonSpaceOccupation lessonSpaceOccupation = lesson.getLessonSpaceOccupation();
            if (lessonSpaceOccupation != null) {
                final Space allocatableSpace = lessonSpaceOccupation.getRoom();
                if (allocatableSpace != null) {
                    final boolean[][] slots = get(allocatableSpace);
                    final int weekDayOffSet = lesson.getDiaSemana().getDiaSemana().intValue() - 2;
                    final int startOffSet = getHourOffSet(lesson.getBeginHourMinuteSecond());
                    final int endOffSet = getHourOffSet(lesson.getEndHourMinuteSecond());
                    for (int i = startOffSet; i < endOffSet; slots[weekDayOffSet][i++] = true) {
                        ;
                    }
                }
            }
            for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
                final Space allocatableSpace = lessonInstance.getRoom();
                if (allocatableSpace != null) {
                    final boolean[][] slots = get(allocatableSpace);
                    final int weekDayOffSet = lessonInstance.getBeginDateTime().getDayOfWeek() - 1;
                    final int startOffSet = getHourOffSet(lessonInstance.getBeginDateTime());
                    final int endOffSet = getHourOffSet(lessonInstance.getEndDateTime());
                    for (int i = startOffSet; i < endOffSet; slots[weekDayOffSet][i++] = true) {
                        ;
                    }
                }
            }
        }

        private int getHourOffSet(final HourMinuteSecond hourMinuteSecond) {
            final int hour = hourMinuteSecond.getHour();
            final int minutes = hourMinuteSecond.getMinuteOfHour();
            final int minutesOffSet = minutes < 30 ? 0 : 1;
            return ((hour - 8) * 2) + minutesOffSet;
        }

        private int getHourOffSet(final DateTime dateTime) {
            final int hour = dateTime.getHourOfDay();
            final int minutes = dateTime.getMinuteOfHour();
            final int minutesOffSet = minutes < 30 ? 0 : 1;
            return ((hour - 8) * 2) + minutesOffSet;
        }

    }

    public ActionForward downloadRoomLessonOccupationInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final ExecutionInterval executionSemester = getExecutionSemester(request);

        final ExecutionYear executionYear = executionSemester.getExecutionYear();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=occupationMap" + executionYear.getYear().replace('/', '_')
                + "_" + executionSemester.getChildOrder() + ".xls");

        final RoomMap occupationMap = new RoomMap();

        Space.getSpaces().forEach(s -> occupationMap.register(s));

        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
                for (final Shift shift : courseLoad.getShiftsSet()) {
                    for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
                        occupationMap.register(lesson);
                    }
                }
            }
        }

        final Spreadsheet spreadsheet = new Spreadsheet("OccupationMap");
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.building"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.identification"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.blueprintNumber"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.doorNumber"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.description"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.classification"));
        final DateTime now = new DateTime();
        for (int weekDay = 0; weekDay < 6; weekDay++) {
            final DateTime dateTime = now.withDayOfWeek(weekDay + 1);
            final String weekDayString = dateTime.dayOfWeek().getAsText(I18N.getLocale());
            for (int hour = 0; hour < 16; hour++) {
                spreadsheet.setHeader(weekDayString + " " + (hour + 8) + ":00");
                spreadsheet.setHeader(weekDayString + " " + (hour + 8) + ":30");
            }
        }
        for (final Entry<Space, boolean[][]> entry : occupationMap.entrySet()) {
            final Space space = entry.getKey();
            final String identification = space.getName();
            final Space building = SpaceUtils.getSpaceBuilding(space);
            final String buildingName = building == null ? "" : building.getPresentationName();
            final boolean[][] slots = entry.getValue();

            final Row row = spreadsheet.addRow();
            row.setCell(buildingName);
            row.setCell(identification == null ? " " : identification);
            final String blueprintNumber = findClosestBlueprintNumber(space);
            row.setCell(blueprintNumber);
            if (SpaceUtils.isRoom(space)) {
                Optional<String> doorNumber = space.getMetadata("doorNumber");
                Optional<String> description = space.getMetadata("description");
                row.setCell(doorNumber.isPresent() ? doorNumber.get() : " ");
                row.setCell(description.isPresent() ? description.get() : " ");
            } else if (SpaceUtils.isRoomSubdivision(space)) {
                final Space room = findSurroundingRoom(space);
                if (room == null) {
                    row.setCell(" ");
                    row.setCell(" ");
                } else {
                    Optional<String> doorNumber = space.getMetadata("doorNumber");
                    Optional<String> description = space.getMetadata("description");
                    row.setCell(doorNumber.isPresent() ? doorNumber.get() : " ");
                    row.setCell(description.isPresent() ? description.get() : " ");
                }
            } else {
                row.setCell(" ");
                row.setCell(" ");
            }
            SpaceClassification classification = space.getClassification();
            if (classification == null) {
                row.setCell(" ");
            } else {
                row.setCell(classification.getAbsoluteCode() + " " + classification.getName().getContent());
            }

            for (int i = 0; i < WEEKDAY_COUNT; i++) {
                for (int j = 0; j < HOUR_COUNT; j++) {
                    row.setCell(Boolean.toString(slots[i][j]));
                }
            }
        }

        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    private ExecutionInterval getExecutionSemester(HttpServletRequest request) {
        String academicIntervalString = (String) getFromRequest(request, "academicIntervalString");
        AcademicInterval academicInterval = AcademicInterval.getAcademicIntervalFromString(academicIntervalString);
        final ExecutionInterval executionSemester = (ExecutionInterval) ExecutionInterval.getExecutionInterval(academicInterval);
        return executionSemester;
    }

    private String findClosestBlueprintNumber(final Space space) {
        if (space == null) {
            return null;
        }
        return space.getBlueprintNumber().isPresent() ? space.getBlueprintNumber()
                .get() : findClosestBlueprintNumber(space.getParent());
    }

    private Space findSurroundingRoom(final Space space) {
        final Space suroundingSpace = space.getParent();
        return suroundingSpace == null ? null : SpaceUtils.isRoom(space) ? suroundingSpace : findSurroundingRoom(suroundingSpace);
    }

    public ActionForward downloadScheduleList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final ExecutionInterval executionSemester = getExecutionSemester(request);
        final Integer semester = executionSemester.getChildOrder();
        final String executionYear = executionSemester.getExecutionYear().getYear();

        final Spreadsheet spreadsheet = new Spreadsheet("ScheduleMap");
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.executionPeriod"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.executionYear"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.executionCourse"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.executionDegree"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.curricular.year"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.shift"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.shift.schedule"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.shift.schedule.hasAllocatedRooms"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.teacher.emails"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.comments"));

        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            final StringBuilder executionDegreeBuilder = new StringBuilder();
            for (final ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
                if (executionDegreeBuilder.length() > 0) {
                    executionDegreeBuilder.append("\n");
                }
                executionDegreeBuilder.append(executionDegree.getDegree().getSigla());
            }
            final StringBuilder emailBuilder = new StringBuilder();
            for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                if (emailBuilder.length() > 0) {
                    emailBuilder.append("\n");
                }
                emailBuilder.append(professorship.getPerson().getEmailForSendingEmails());
            }

            for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
                for (final Shift shift : courseLoad.getShiftsSet()) {
                    final Set<Integer> curricularYears = new TreeSet<Integer>();
                    for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
                        curricularYears.add(schoolClass.getAnoCurricular());
                    }
                    final StringBuilder curricularYearBuilder = new StringBuilder();
                    for (final Integer curricularYear : curricularYears) {
                        if (curricularYearBuilder.length() > 0) {
                            curricularYearBuilder.append(", ");
                        }
                        curricularYearBuilder.append(curricularYear);
                    }

                    final Row row = spreadsheet.addRow();
                    row.setCell(semester);
                    row.setCell(executionYear);
                    row.setCell(executionCourse.getName());
                    row.setCell(executionDegreeBuilder.toString());
                    row.setCell(curricularYearBuilder.toString());
                    row.setCell(shift.getNome());
                    row.setCell(shift.getLessonPresentationString().replace(';', '\n'));
                    row.setCell(hasRoomsAttributed(shift));
                    row.setCell(emailBuilder.toString());
                    row.setCell(shift.getComment() == null ? "" : shift.getComment());
                }
            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=scheduleMap" + executionYear.replace('/', '_') + "_"
                + executionSemester.getChildOrder() + ".xls");

        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    public ActionForward downloadShiftAttendence(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final ExecutionInterval executionSemester = getExecutionSemester(request);
        final String executionYear = executionSemester.getExecutionYear().getYear();

        final Spreadsheet spreadsheet = new Spreadsheet("ShiftAttendenceMap");
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.shift"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.executionCourse"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.executionDegree"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.shift.schedule"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.lesson.room"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.number.students.enrolled"));

        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            final StringBuilder executionDegreeBuilder = new StringBuilder();
            for (final ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
                if (executionDegreeBuilder.length() > 0) {
                    executionDegreeBuilder.append("\n");
                }
                executionDegreeBuilder.append(executionDegree.getDegree().getSigla());
            }
            final StringBuilder emailBuilder = new StringBuilder();
            for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                if (emailBuilder.length() > 0) {
                    emailBuilder.append("\n");
                }
                emailBuilder.append(professorship.getPerson().getEmailForSendingEmails());
            }

            for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
                for (final Shift shift : courseLoad.getShiftsSet()) {
                    final Row row = spreadsheet.addRow();
                    final StringBuilder roomBuilder = new StringBuilder();
                    final StringBuilder scheduleBuilder = new StringBuilder();
                    if (!shift.getAssociatedLessonsSet().isEmpty()) {
                        for (Iterator<Lesson> iterator = shift.getAssociatedLessonsSet().iterator(); iterator.hasNext();) {
                            Lesson lesson = iterator.next();
                            scheduleBuilder.append(WeekDay.getWeekDay(lesson.getDiaSemana()).getLabelShort());
                            scheduleBuilder.append(" ");
                            scheduleBuilder.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
                            scheduleBuilder.append(" - ");
                            scheduleBuilder.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
                            if (lesson.hasSala()) {
                                roomBuilder.append(lesson.getSala().getName());
                            }
                            if (iterator.hasNext()) {
                                scheduleBuilder.append(" ; ");
                                roomBuilder.append(" ; ");
                            }
                        }
                    }

                    row.setCell(shift.getNome());
                    row.setCell(executionCourse.getName());
                    row.setCell(executionDegreeBuilder.toString());
                    row.setCell(scheduleBuilder.toString().replace(';', '\n'));
                    row.setCell(roomBuilder.toString().replace(';', '\n'));
                    row.setCell(ShiftEnrolment.getTotalEnrolments(shift));
                }
            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=shiftAttendenceMap" + executionYear.replace('/', '_')
                + "_" + executionSemester.getChildOrder() + ".xls");

        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    private String hasRoomsAttributed(final Shift shift) {
        if (shift.getAssociatedLessonsSet().isEmpty()) {
            return BundleUtil.getString(Bundle.APPLICATION, "label.no");
        }
        for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
            if (!hasRoomsAttributed(lesson)) {
                return BundleUtil.getString(Bundle.APPLICATION, "label.no");
            }
        }
        return BundleUtil.getString(Bundle.APPLICATION, "label.yes");
    }

    private boolean hasRoomsAttributed(final Lesson lesson) {
        return lesson.getSala() != null;
    }

}