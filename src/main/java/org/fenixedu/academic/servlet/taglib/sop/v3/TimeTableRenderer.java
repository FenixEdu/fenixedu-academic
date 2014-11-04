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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ClassTimeTableWithoutLinksLessonContentRenderer;
import org.fenixedu.academic.servlet.taglib.sop.v3.renderers.ShiftEnrollmentTimeTableLessonContentRenderer;

/**
 * @author jpvl
 */
public class TimeTableRenderer {

    // private Integer endHour;
    private final Integer startHour;

    private final Integer slotSize;

    private final TimeTable timeTable;

    private final LessonSlotContentRenderer lessonSlotContentRenderer;

    private final ColorPicker colorPicker;

    /**
     * Constructor TimeTableRenderer.
     * 
     * @param timeTable
     * @param lessonSlotContentRenderer
     * @param integer
     * @param integer1
     * @param integer2
     */
    public TimeTableRenderer(TimeTable timeTable, LessonSlotContentRenderer lessonSlotContentRenderer, Integer slotSize,
            Integer startHour, Integer endHour, ColorPicker colorPicker) {
        if (colorPicker == null) {
            throw new IllegalArgumentException(this.getClass().getName() + ":Color picker must be not null!");
        }
        this.timeTable = timeTable;
        this.lessonSlotContentRenderer = lessonSlotContentRenderer;

        // this.endHour = endHour;
        this.startHour = startHour;
        this.slotSize = slotSize;
        this.colorPicker = colorPicker;
    }

    public StringBuilder render(Locale locale, PageContext pageContext, boolean definedWidth) {
        StringBuilder strBuffer = new StringBuilder("");
        HashMap slotLessons = new HashMap();
        TimeTableSlot[][] grid = timeTable.getTimeTableGrid();

        strBuffer.append("<table class='timetable' cellspacing='0' cellpadding='0'");
        if (definedWidth) {
            strBuffer.append("width='90%'");
        }
        strBuffer.append(">");

        final SchoolClass schoolClass = (SchoolClass) pageContext.getRequest().getAttribute("schoolClass");

        if (schoolClass != null) {
            final String className = schoolClass.getNome();
            strBuffer.append("<caption>");
            final String captionPrefix =
                    getMessageResource(pageContext, "public.degree.information.title.class.timetable", locale);
            strBuffer.append(captionPrefix);
            strBuffer.append(' ');
            strBuffer.append(className);
            strBuffer.append("</caption>");
        }

        renderHeader(strBuffer, locale, pageContext, definedWidth);

        for (int hourIndex = 0; hourIndex < timeTable.getNumberOfHours().intValue(); hourIndex++) {
            strBuffer.append("<tr>\r\n");
            strBuffer.append("<th class='period-hours'");
            strBuffer.append(" id='hour");
            strBuffer.append(hourIndex);
            strBuffer.append("'>");
            strBuffer.append(getHourLabelByIndex(hourIndex)).append("</th>\r\n");

            /* iterate over days */
            for (int dayIndex = 0; dayIndex < timeTable.getNumberOfDays().intValue(); dayIndex++) {
                DayColumn dayColumn = timeTable.getDayColumn(dayIndex);

                TimeTableSlot timeTableSlot = grid[dayIndex][hourIndex];

                if (timeTableSlot != null) {

                    List colisionList = timeTableSlot.getLessonSlotList();
                    InfoLessonWrapper[] lessonSlotListResolved = resolveColisions(colisionList, timeTable.getDayColumn(dayIndex));

                    for (int slotIndex = 0; slotIndex < lessonSlotListResolved.length; slotIndex++) {
                        InfoLessonWrapper infoLessonWrapper = lessonSlotListResolved[slotIndex];

                        strBuffer.append("<td ");

                        if (infoLessonWrapper != null) {
                            strBuffer.append("style='background-color: ")
                                    .append(colorPicker.getBackgroundColor(infoLessonWrapper)).append("' ");
                        }

                        int slotColspan = calculateColspan(infoLessonWrapper, lessonSlotListResolved, slotIndex, dayColumn);

                        if (slotColspan > 1) {
                            strBuffer.append(" colspan ='").append(slotColspan).append("'");
                            slotIndex += slotColspan - 1;
                        }

                        strBuffer.append(" headers='weekday");
                        strBuffer.append(dayIndex);
                        for (int hourHeader = hourIndex; hourHeader < hourIndex + slotColspan; hourHeader++) {
                            strBuffer.append(" hour");
                            strBuffer.append(hourHeader);
                        }
                        strBuffer.append("' ");

                        strBuffer.append(" class='");
                        strBuffer.append(getSlotCssClass(infoLessonWrapper, hourIndex));
                        strBuffer.append("' ");
                        if (infoLessonWrapper != null) {
                            final InfoShowOccupation occupation =
                                    infoLessonWrapper.getLessonSlot().getInfoLessonWrapper().getInfoShowOccupation();
                            strBuffer.append(" title='")
                                    .append(this.lessonSlotContentRenderer.renderTitleText(infoLessonWrapper.getLessonSlot()))
                                    .append("'");
                        }
                        strBuffer.append(">");

                        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
                        // if infolessonwrapper start and end are the same...
                        if ((infoLessonWrapper != null) && (infoLessonWrapper.getLessonSlot().getStartIndex() == hourIndex)) {
                            final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                            final String context = request.getContextPath();
                            strBuffer.append(this.lessonSlotContentRenderer.render(context, infoLessonWrapper.getLessonSlot()));
                            if (getSlotCssClass(infoLessonWrapper, hourIndex).equalsIgnoreCase("period-single-slot")) {
                                strBuffer.append(this.lessonSlotContentRenderer.renderSecondLine(context,
                                        infoLessonWrapper.getLessonSlot()));
                            }
                            if (this.lessonSlotContentRenderer instanceof ShiftEnrollmentTimeTableLessonContentRenderer) {
                                if (getSlotCssClass(infoLessonWrapper, hourIndex).equalsIgnoreCase("period-single-slot")) {
                                    LessonSlotContentRendererShift lessonSlotContentRendererShift =
                                            (LessonSlotContentRendererShift) this.lessonSlotContentRenderer;
                                    strBuffer.append(lessonSlotContentRendererShift.lastRender(infoLessonWrapper.getLessonSlot(),
                                            contextPath));
                                }
                                slotLessons.put(slotIndex + "-" + dayIndex, infoLessonWrapper);
                            }
                        } else {
                            if (infoLessonWrapper != null && !infoLessonWrapper.isSecondRowAlreadyAppended()) {
                                final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                                final String context = request.getContextPath();
                                strBuffer.append(this.lessonSlotContentRenderer.renderSecondLine(context,
                                        infoLessonWrapper.getLessonSlot()));
                            }
                            if (this.lessonSlotContentRenderer instanceof ShiftEnrollmentTimeTableLessonContentRenderer
                                    && getSlotCssClass((InfoLessonWrapper) slotLessons.get(slotIndex + "-" + dayIndex), hourIndex)
                                            .equalsIgnoreCase("period-last-slot")) {
                                LessonSlotContentRendererShift lessonSlotContentRendererShift =
                                        (LessonSlotContentRendererShift) this.lessonSlotContentRenderer;
                                InfoLessonWrapper infoLessonWrapperHashMap =
                                        (InfoLessonWrapper) slotLessons.get(slotIndex + "-" + dayIndex);
                                strBuffer.append(lessonSlotContentRendererShift.lastRender(
                                        infoLessonWrapperHashMap.getLessonSlot(), contextPath));
                            } else {
                                if (infoLessonWrapper != null
                                        && this.lessonSlotContentRenderer instanceof ClassTimeTableWithoutLinksLessonContentRenderer) {
                                    final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                                    final String context = request.getContextPath();
                                    strBuffer.append(this.lessonSlotContentRenderer.render(context,
                                            infoLessonWrapper.getLessonSlot()));
                                }

                                strBuffer.append("&nbsp;");
                            }
                        }
                        strBuffer.append("</td>\r\n");
                    }
                } else {
                    /** no lessons */
                    int colspan = dayColumn.getMaxColisionSize().intValue();
                    String cssClass = getSlotCssClass(null, hourIndex);
                    strBuffer.append("<td ").append(" class='").append(cssClass).append("' ");
                    if (colspan > 1) {
                        strBuffer.append("colspan='").append(colspan).append("'");
                    }
                    strBuffer.append(">").append("&nbsp;").append("</td>\r\n");
                }

            }
            strBuffer.append("</tr>\r\n");
        }

        strBuffer.append("</table>");
        return strBuffer;
    }

    private String getMessageResource(PageContext pageContext, String key, Locale locale) {
        try {
            return RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY, key);
        } catch (JspException e) {
            return "???" + key + "???";
        }
    }

    /**
     * @param infoLessonWrapper
     * @param lessonSlotListResolved
     * @param dayColumn
     * @param slotIndex
     * @return
     */
    private int calculateColspan(InfoLessonWrapper infoLessonWrapper, InfoLessonWrapper[] lessonSlotListResolved,
            int currentSlotIndex, DayColumn dayColumn) {
        int colspan = 1;
        if (infoLessonWrapper != null && (infoLessonWrapper.getNumberOfCollisions().intValue() == 0)) {
            colspan = dayColumn.getMaxColisionSize().intValue();
        } else if (infoLessonWrapper == null) {
            for (int i = currentSlotIndex + 1; i < lessonSlotListResolved.length; i++) {
                InfoLessonWrapper next = lessonSlotListResolved[i];
                if (next == null) {
                    colspan++;
                } else {
                    break;
                }
            }
        }
        return colspan;
    }

    /**
     * 
     * @param infoLessonWrapper
     *            may be null
     * @param hourIndex
     * @param dayIndex
     * @param timeTableGrid
     * @param slotColisions
     *            may be null
     * @param slotIndex
     * @return String
     */
    private String getSlotCssClass(InfoLessonWrapper infoLessonWrapper, int hourIndex) {
        String css = "period-empty-slot";
        if (infoLessonWrapper != null) {

            LessonSlot lessonSlot = infoLessonWrapper.getLessonSlot();
            if ((lessonSlot.getStartIndex() == hourIndex) && (lessonSlot.getEndIndex() == hourIndex)) {
                css = "period-single-slot";
            } else if (lessonSlot.getStartIndex() == hourIndex) {
                css = "period-first-slot";
            } else if (lessonSlot.getEndIndex() == hourIndex) {
                css = "period-last-slot";
            } else {
                css = "period-middle-slot";
            }
        }
        return css;
    }

    /**
     * Method renderHeader.
     * 
     * @param strBuffer
     * @param pageContext
     * @param definedWidth
     */
    private void renderHeader(StringBuilder strBuffer, Locale locale, PageContext pageContext, boolean definedWidth) {

        // strBuffer.append("<th width='15%'>horas/dias</th>\r\n");
        String hourDaysTitle;
        try {
            hourDaysTitle =
                    RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY,
                            "public.degree.information.label.timesAndDays");
        } catch (JspException e) {
            hourDaysTitle = "???label.timesAndDays???";
        }

        strBuffer.append("<tr><th>");
        strBuffer.append(hourDaysTitle);
        strBuffer.append("</th>\r\n");

        int cellWidth = (100 - 15) / timeTable.getNumberOfDays().intValue();
        for (int index = 0; index < this.timeTable.getNumberOfDays().intValue(); index++) {
            strBuffer.append("<th colspan='");
            strBuffer.append(timeTable.getDayColumn(index).getMaxColisionSize());
            strBuffer.append("' ");

            if (definedWidth) {
                strBuffer.append("width='");
                strBuffer.append(cellWidth);
                strBuffer.append("%'");
            }

            strBuffer.append(" id='weekday");
            strBuffer.append(index);
            strBuffer.append("'");

            strBuffer.append(">\r\n");
            strBuffer.append(timeTable.getDayColumn(index).getLabel());
            strBuffer.append("</th>\r\n");
        }
        strBuffer.append("</tr>");
    }

    protected InfoLessonWrapper[] resolveColisions(List lessonSlotList, DayColumn dayColumn) {
        InfoLessonWrapper[] list = new InfoLessonWrapper[dayColumn.getMaxColisionSize().intValue()];

        Iterator lessonSlotListIterator = lessonSlotList.iterator();

        List lessonSlotNotLocked = new ArrayList();

        while (lessonSlotListIterator.hasNext()) {
            LessonSlot lessonSlot = (LessonSlot) lessonSlotListIterator.next();

            InfoLessonWrapper infoLessonWrapper = lessonSlot.getInfoLessonWrapper();

            if (infoLessonWrapper.isLocked()) {
                list[infoLessonWrapper.getSlotIndex()] = infoLessonWrapper;
            } else {
                lessonSlotNotLocked.add(infoLessonWrapper);
            }
        }

        Collections.sort(lessonSlotNotLocked);

        Iterator lessonSlotNotLockedIterator = lessonSlotNotLocked.iterator();
        while (lessonSlotNotLockedIterator.hasNext()) {
            InfoLessonWrapper infoLessonWrapper = (InfoLessonWrapper) lessonSlotNotLockedIterator.next();

            for (int index = 0; index < list.length; index++) {
                if (list[index] == null) {
                    list[index] = infoLessonWrapper;
                    infoLessonWrapper.setLocked(true, index);
                    break;
                }
            }
        }
        return list;

    }

    private StringBuilder getHourLabelByIndex(int hourIndex) {
        StringBuilder label = new StringBuilder("");

        double startLabelHour = startHour.doubleValue() + new Integer(hourIndex).doubleValue() / (60.0 / slotSize.doubleValue());
        double startMinutes = startLabelHour - Math.floor(startLabelHour);

        String startMinutesLabel = String.valueOf((int) (startMinutes * 60));
        if (startMinutesLabel.length() == 1) {
            startMinutesLabel = "0" + startMinutesLabel;
        }

        double endLabelHour =
                startHour.doubleValue() + new Integer(hourIndex + 1).doubleValue() / (60.0 / slotSize.doubleValue());

        double endMinutes = endLabelHour - Math.floor(endLabelHour);

        String endMinutesLabel = String.valueOf((int) (endMinutes * 60));
        if (endMinutesLabel.length() == 1) {
            endMinutesLabel = "0" + endMinutesLabel;
        }

        return label.append((int) startLabelHour).append(":").append(startMinutesLabel).append("-").append((int) endLabelHour)
                .append(":").append(endMinutesLabel);

    }
}