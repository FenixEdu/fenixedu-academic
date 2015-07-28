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
package org.fenixedu.academic.servlet.taglib.sop.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.fenixedu.academic.dto.InfoCurricularYear;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.servlet.taglib.sop.v3.colorPickers.ClassTimeTableColorPicker;
import org.fenixedu.academic.servlet.taglib.sop.v3.colorPickers.ExecutionCourseTimeTableColorPicker;
import org.fenixedu.academic.servlet.taglib.sop.v3.colorPickers.RoomTimeTableColorPicker;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ClassTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ClassTimeTableWithoutLinksLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ExecutionCourseTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.RoomTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ShiftEnrollmentTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ShiftTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.SopClassRoomTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.SopClassTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.SopRoomTimeTableLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.SpaceManagerRoomTimeTableLessonContentRenderer;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RenderTimeTableTag extends TagSupport {

    private static final Integer DEFAULT_END_TIME = 0;

    private static final Logger logger = LoggerFactory.getLogger(RenderTimeTableTag.class);

    private LessonSlotContentRenderer lessonSlotContentRenderer = null;

    private int type = 1;

    private String application = "";

    private String studentID = "";

    private String classID = "";

    private String executionCourseID = "";

    private String executionSemesterID = "";

    private String action = "";

    private String endTime = "";

    private Integer endTimeTableHour = new Integer(24);

    private final Integer slotSizeMinutes = new Integer(30);

    private boolean definedWidth = true;

    private ColorPicker colorPicker;

    // Nome do atributo que cont�m a lista de aulas.
    private String name;

    // Mensagens de erro.
    protected static MessageResources messages = MessageResources.getMessageResources("PublicDegreeInformation");

    private InfoCurricularYear infoCurricularYear = null;

    private InfoExecutionDegree infoExecutionDegree = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {

        try {
            infoCurricularYear = (InfoCurricularYear) pageContext.findAttribute(PresentationConstants.CURRICULAR_YEAR);
            infoExecutionDegree = (InfoExecutionDegree) pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE);

        } catch (ClassCastException e) {
            infoCurricularYear = null;
            infoExecutionDegree = null;
        }

        setLessonSlotRendererAndColorPicker();

        Integer startTimeTableHour = Integer.MAX_VALUE;
        this.endTimeTableHour = DEFAULT_END_TIME;

        List<InfoShowOccupation> infoLessonList = null;
        try {
            infoLessonList = (List<InfoShowOccupation>) pageContext.findAttribute(name);
            if (infoLessonList.size() > 0) {

                for (InfoShowOccupation occupation : infoLessonList) {
                    // Calculate start hour based on earliest event
                    if (occupation.getFirstHourOfDay() < startTimeTableHour) {
                        startTimeTableHour = occupation.getFirstHourOfDay();
                    }
                    // Calculate end hour based on latest event
                    if (occupation.getLastHourOfDay() > this.endTimeTableHour) {
                        this.endTimeTableHour = occupation.getLastHourOfDay();
                    }
                }
            } else {
                startTimeTableHour = DEFAULT_END_TIME;
                this.endTimeTableHour = DEFAULT_END_TIME + 1;
            }
        } catch (ClassCastException e) {
            logger.error(e.getMessage(), e);
            infoLessonList = null;
        }
        if (infoLessonList == null) {
            throw new JspException(messages.getMessage("gerarHorario.listaAulas.naoExiste", name));
        }

        // Gera o hor�rio a partir da lista de aulas.
        Locale locale = (Locale) pageContext.findAttribute(Globals.LOCALE_KEY);
        JspWriter writer = pageContext.getOut();
        TimeTable timeTable = generateTimeTable(infoLessonList, locale, pageContext, startTimeTableHour);

        TimeTableRenderer renderer =
                new TimeTableRenderer(timeTable, lessonSlotContentRenderer, this.slotSizeMinutes, startTimeTableHour,
                        this.endTimeTableHour, colorPicker);

        try {
            writer.print(renderer.render(locale, pageContext, getDefinedWidth()));
            writer.print(legenda(infoLessonList, locale));
        } catch (IOException e) {
            throw new JspException(messages.getMessage("gerarHorario.io", e.toString()));
        }
        return (SKIP_BODY);
    }

    private boolean hasLessonBefore8(final List<InfoShowOccupation> infoLessonList) {
        for (final InfoShowOccupation infoShowOccupation : infoLessonList) {
            if (infoShowOccupation.getFirstHourOfDay() < 8) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method generateTimeTable.
     *
     * @param startTimeTableHour
     *
     * @param listaAulas
     * @return TimeTable
     */
    private TimeTable generateTimeTable(List lessonList, Locale locale, PageContext pageContext, Integer startTimeTableHour) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, startTimeTableHour.intValue());
        calendar.set(Calendar.MINUTE, 0);

        Integer numberOfDays = new Integer(6);
        Integer numberOfHours =
                new Integer((endTimeTableHour.intValue() - startTimeTableHour.intValue()) * (60 / slotSizeMinutes.intValue()));

        TimeTable timeTable = new TimeTable(numberOfHours, numberOfDays, calendar, slotSizeMinutes, locale, pageContext);
        Iterator lessonIterator = lessonList.iterator();

        while (lessonIterator.hasNext()) {
            InfoShowOccupation infoShowOccupation = (InfoShowOccupation) lessonIterator.next();
            timeTable.addLesson(infoShowOccupation);
        }
        return timeTable;
    }

    @Override
    public int doEndTag() {
        return (EVAL_PAGE);
    }

    @Override
    public void release() {
        super.release();

        this.application = null;
        this.studentID = null;
        this.classID = null;
        this.executionCourseID = null;
        this.executionSemesterID = null;
        this.action = null;
        this.endTime = null;
    }

    private String getMessageResource(PageContext pageContext, String key) {
        try {
            return RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY, key);
        } catch (JspException e) {
            return "???" + key + "???";
        }
    }

    private StringBuilder legenda(List listaAulas, Locale locale) {

        StringBuilder result = new StringBuilder("");
        List<SubtitleEntry> listaAuxiliar = new ArrayList<SubtitleEntry>();
        Iterator<InfoShowOccupation> iterator = listaAulas.iterator();

        while (iterator.hasNext()) {

            InfoShowOccupation elem = iterator.next();

            if (elem instanceof InfoLesson || elem instanceof InfoLessonInstance) {
                SubtitleEntry subtitleEntry =
                        new SubtitleEntry(elem.getInfoShift().getInfoDisciplinaExecucao().getSigla(), elem.getInfoShift()
                                .getInfoDisciplinaExecucao().getNome());

                if (!listaAuxiliar.contains(subtitleEntry)) {
                    listaAuxiliar.add(subtitleEntry);
                }
            }
        }

        if (listaAuxiliar.size() > 1) {

            Collections.sort(listaAuxiliar);

            result.append("<br/><b>");
            result.append(getMessageResource(pageContext, "public.degree.information.label.legend"));
            result.append("</b><br /><br /><table cellpadding='0' cellspacing='0' style='margin-left:5px'>");

            for (int i = 0; i < listaAuxiliar.size(); i++) {

                SubtitleEntry elem = listaAuxiliar.get(i);

                boolean oddElement = (i % 2 == 1);
                if (!oddElement) {
                    result.append("<tr>\r\n");
                }
                result.append("<td width='60'><b>");
                result.append(elem.getKey());
                result.append("</b></td><td  style='vertical-align:top'>-</td><td>");
                result.append(elem.getValue());
                result.append("</td>");

                if (oddElement) {
                    result.append("</tr>\r\n");
                }
            }
            if (listaAuxiliar.size() % 2 == 1) {
                result.append("<td colspan='3'>&nbsp;</td></tr>");
            }

            // TODO(rspl): Will it stay like this the interface for showing
            // the legend of a quinzenal lesson?
            result.append("<tr><td style='vertical-align:top'><b>[Q]</b></td>");
            result.append("<td  style='vertical-align:top'>-</td>");
            result.append("<td>");
            result.append(getMessageResource(pageContext, "public.degree.information.label.biweekly"));
            result.append("</td></tr>");

            result.append("</table>");

        }
        return result;
    }

    /**
     * Returns the type.
     *
     * @return int
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            The type to set
     */
    public void setType(int timeTableType) {
        this.type = timeTableType;
        setLessonSlotRendererAndColorPicker();
    }

    private void setLessonSlotRendererAndColorPicker() {
        switch (this.type) {

        case TimeTableType.SHIFT_TIMETABLE:
            this.lessonSlotContentRenderer = new ShiftTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.EXECUTION_COURSE_TIMETABLE:
            this.lessonSlotContentRenderer = new ExecutionCourseTimeTableLessonContentRenderer();
            this.colorPicker = new ExecutionCourseTimeTableColorPicker();
            break;

        case TimeTableType.ROOM_TIMETABLE:
            this.lessonSlotContentRenderer = new RoomTimeTableLessonContentRenderer();
            this.colorPicker = new RoomTimeTableColorPicker();
            break;

        case TimeTableType.SOP_CLASS_TIMETABLE:
            this.lessonSlotContentRenderer = new SopClassTimeTableLessonContentRenderer(infoExecutionDegree, infoCurricularYear);
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.SOP_ROOM_TIMETABLE:
            this.lessonSlotContentRenderer = new SopRoomTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.SPACE_MANAGER_TIMETABLE:
            this.lessonSlotContentRenderer = new SpaceManagerRoomTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.SOP_CLASS_ROOM_TIMETABLE:
            this.lessonSlotContentRenderer = new SopClassRoomTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS:
            this.lessonSlotContentRenderer = new ClassTimeTableWithoutLinksLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.CLASS_TIMETABLE:
            this.lessonSlotContentRenderer = new ClassTimeTableWithLinksLessonContentRenderer(getApplication());
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.SHIFT_ENROLLMENT_TIMETABLE:
            this.lessonSlotContentRenderer =
                    new ShiftEnrollmentTimeTableLessonContentRenderer(getStudentID(), getApplication(), getClassID(),
                            getExecutionCourseID(), getExecutionSemesterID(), getAction());
            this.colorPicker = new ClassTimeTableColorPicker();
            Integer defaultTime = new Integer(19);
            Integer endTime = defaultTime;
            if (!getEndTime().equals("")) {
                endTime = new Integer(getEndTime());
                if (endTime < defaultTime) {
                    endTime = defaultTime;
                }
            }
            this.endTimeTableHour = endTime;
            break;

        default:
            this.lessonSlotContentRenderer = new ClassTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;
        }
    }

    /**
     * @return Returns the application.
     */
    public String getApplication() {
        return application;
    }

    /**
     * @param application
     *            The application to set.
     */
    public void setApplication(String application) {
        this.application = application;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getExecutionCourseID() {
        return executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseID) {
        this.executionCourseID = executionCourseID;
    }

    public String getExecutionSemesterID() {
        return executionSemesterID;
    }

    public void setExecutionSemesterID(String executionSemesterID) {
        this.executionSemesterID = executionSemesterID;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean getDefinedWidth() {
        return definedWidth;
    }

    public void setDefinedWidth(boolean definedWidth) {
        this.definedWidth = definedWidth;
    }

}
