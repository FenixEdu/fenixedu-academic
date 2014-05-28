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
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.util.DayType;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram.ViewType;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.taglib.TagUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class GanttDiagramTagLib extends TagSupport {

    private static final Logger logger = LoggerFactory.getLogger(GanttDiagramTagLib.class);

    private static final String F = "F";

    private static final String S = "S";

    private static final String D = "D";

    private static final String EMPTY_TD_BAR = "emptytdbar";

    private static final String TD_BAR = "tdbar";

    private static int MONDAY_IN_JODA_TIME = 1, TUESDAY_IN_JODA_TIME = 2, WEDNESDAY_IN_JODA_TIME = 3, THURSDAY_IN_JODA_TIME = 4,
            FRIDAY_IN_JODA_TIME = 5, SATURDAY_IN_JODA_TIME = 6, SUNDAY_IN_JODA_TIME = 7;

    // Attributes
    private String ganttDiagram;

    private String eventUrl;

    private String eventParameter;

    private String bundle;

    private String firstDayParameter;

    private String monthlyViewUrl;

    private String weeklyViewUrl;

    private String dailyViewUrl;

    private String toWrite = null;

    private boolean showPeriod = true;

    private boolean showObservations = true;

    private boolean toMark = true;

    private boolean specialDiv = false;

    private boolean isEventToMarkWeekendsAndHolidays = false;

    // Auxiliar variables
    private GanttDiagram ganttDiagramObject;

    private List<? extends GanttDiagramEvent> events;

    private HttpServletRequest request;

    private ViewType viewTypeEnum;

    private static BigDecimal PX_TO_EM_CONVERSION_DIVISOR = BigDecimal.TEN;

    private static BigDecimal EMPTY_UNIT = BigDecimal.ZERO;

    private static int NUMBER_OF_DAY_5_MINUTES = 288;

    private static int NUMBER_OF_DAY_HALF_HOURS = 48;

    private static int NUMBER_OF_DAY_HOURS = 24;

    private static int FIXED_COLUMNS_SIZE_EM = 50;

    private static int PADDING_LEFT_MULTIPLIER = 15;

    @Override
    public int doStartTag() throws JspException {

        String ganttDiagram = "";
        Object object = pageContext.findAttribute(getGanttDiagram());

        if (object != null) {
            setGanttDiagramObject((GanttDiagram) object);
            setViewTypeEnum(getGanttDiagramObject().getViewType());
            setEvents(getGanttDiagramObject().getEvents());
            setShowPeriod(isShowPeriod());
            setShowObservations(isShowObservations());
            setRequest((HttpServletRequest) pageContext.getRequest());
            ganttDiagram = generateGanttDiagramString();
        }

        try {
            pageContext.getOut().print(ganttDiagram);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return SKIP_BODY;
    }

    private String generateGanttDiagramString() throws JspException {
        switch (getViewTypeEnum()) {

        case TOTAL:
            return generateGanttDiagramInTotalMode(convertToEm(getGanttDiagramObject().getMonthsDaysSize())).toString();

        case MONTHLY_TOTAL:
            return generateGanttDiagramInTotalMode(convertToEm(getGanttDiagramObject().getMonthsDaysSize())).toString();

        case MONTHLY:
            return generateGanttDiagramInTimeMode(
                    BigDecimal.valueOf(getGanttDiagramObject().getDays().size()).multiply(convertToEm(NUMBER_OF_DAY_HOURS)))
                    .toString();

        case WEEKLY:
            return generateGanttDiagramInTimeMode(BigDecimal.valueOf(7).multiply(convertToEm(NUMBER_OF_DAY_HALF_HOURS)))
                    .toString();

        case DAILY:
            return generateGanttDiagramInTimeMode(convertToEm(NUMBER_OF_DAY_5_MINUTES)).toString();

        case YEAR_DAILY:
            return generateGanttDiagramInTimeMode(
                    BigDecimal.valueOf(getGanttDiagramObject().getDays().size()).multiply(convertToEm(NUMBER_OF_DAY_HOURS)))
                    .toString();

        default:
            return "";
        }
    }

    private StringBuilder generateGanttDiagramInTimeMode(BigDecimal tableWidth) throws JspException {
        StringBuilder builder = new StringBuilder();

        if (!getEvents().isEmpty()) {
            if (isShowPeriod() && isShowObservations()) {
                builder.append("<table style=\"width:").append(tableWidth.add(BigDecimal.valueOf(FIXED_COLUMNS_SIZE_EM)))
                        .append("em;\" class=\"tcalendar thlight\">");
            } else {
                builder.append("<table class=\"tcalendar thlight\">");
            }

            generateHeaders(builder);

            int numberOfUnits = getNumberOfUnits();

            String selectedEvent = getRequest().getParameter(getEventParameter());
            Object selectedEventObject = getRequest().getAttribute(getEventParameter());

            for (GanttDiagramEvent event : getEvents()) {

                String eventUrl =
                        getRequest().getContextPath() + getEventUrl() + "&amp;" + getEventParameter() + "="
                                + event.getGanttDiagramEventIdentifier();

                if (event.getGanttDiagramEventUrlAddOns() != null) {
                    eventUrl = eventUrl.concat(event.getGanttDiagramEventUrlAddOns());
                }

                final MultiLanguageString diagramEventName = event.getGanttDiagramEventName();
                String eventName = diagramEventName == null ? "" : diagramEventName.getContent();
                String paddingStyle = "padding-left:" + event.getGanttDiagramEventOffset() * PADDING_LEFT_MULTIPLIER + "px";

                if (event.getGanttDiagramEventIdentifier().equals(selectedEvent)
                        || (selectedEventObject != null && event.getGanttDiagramEventIdentifier().equals(
                                selectedEventObject.toString()))) {
                    builder.append("<tr class=\"selected\">");
                } else {
                    builder.append("<tr>");
                }

                if (getViewTypeEnum() == ViewType.YEAR_DAILY) {
                    builder.append("<td class=\"padded\">").append("<div class=\"nowrap\">");
                    builder.append("<span style=\"").append(paddingStyle).append("\" title=\"").append(eventName).append("\">");
                    builder.append("<a href=\"").append(eventUrl).append("&amp;month=")
                            .append(Month.values()[event.getGanttDiagramEventMonth() - 1].toString()).append("\">")
                            .append(eventName);
                } else {
                    builder.append("<td class=\"padded\">").append(
                            "<div style=\"overflow:hidden; width: 14.5em;\" class=\"nowrap\">");
                    builder.append("<span style=\"").append(paddingStyle).append("\" title=\"").append(eventName).append("\">");
                    builder.append("<a href=\"").append(eventUrl).append("\">").append(eventName);
                }
                builder.append("</a></span></div></td>");

                for (DateTime day : getGanttDiagramObject().getDays()) {

                    int startIndex = 0, endIndex = 0;
                    int dayOfMonth = day.getDayOfMonth();
                    int monthOfYear = day.getMonthOfYear();
                    if (getViewTypeEnum() == ViewType.YEAR_DAILY) {
                        monthOfYear = event.getGanttDiagramEventMonth();
                    }
                    int year = day.getYear();
                    YearMonthDay yearMonthDay = new YearMonthDay(year, monthOfYear, 1);

                    isEventToMarkWeekendsAndHolidays = event.isGanttDiagramEventToMarkWeekendsAndHolidays();

                    if (!isEventToMarkWeekendsAndHolidays) {
                        builder.append("<td style=\"width: ").append(convertToEm(numberOfUnits))
                                .append("em;\"><div style=\"position: relative;\">");
                    }

                    if (getViewTypeEnum() == ViewType.YEAR_DAILY) {
                        if (dayOfMonth > yearMonthDay.plusMonths(1).minusDays(1).getDayOfMonth()) {
                            addEmptyDiv(builder);
                            builder.append("</div></td>");
                            continue;
                        }
                    }

                    specialDiv = false;

                    for (Interval interval : event.getGanttDiagramEventSortedIntervals()) {

                        toWrite = null;
                        toMark = true;
                        LocalDate localDate = yearMonthDay.withDayOfMonth(dayOfMonth).toLocalDate();
                        if ((event.getGanttDiagramEventDayType(interval) == DayType.SPECIFIC_DAYS)
                                || (event.getGanttDiagramEventDayType(interval) == DayType.WORKDAY)) {
                            if ((localDate.getDayOfWeek() == SATURDAY_IN_JODA_TIME)
                                    || (localDate.getDayOfWeek() == SUNDAY_IN_JODA_TIME) || (Holiday.isHoliday(localDate))) {
                                toMark = false;
                            }
                        }
                        if (isEventToMarkWeekendsAndHolidays) {
                            if (Holiday.isHoliday(localDate)) {
                                toWrite = F;
                            } else if (localDate.getDayOfWeek() == SATURDAY_IN_JODA_TIME) {
                                toWrite = S;
                            } else if (localDate.getDayOfWeek() == SUNDAY_IN_JODA_TIME) {
                                toWrite = D;
                            }
                        }

                        if (interval.getStart().getYear() <= year && interval.getEnd().getYear() >= year) {

                            if (interval.getStart().getYear() < year && interval.getEnd().getYear() > year) {
                                addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);
                            }
                            // Started in same year and Ended after
                            else if (interval.getStart().getYear() == year && interval.getEnd().getYear() > year) {

                                if (interval.getStart().getMonthOfYear() < monthOfYear) {
                                    addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

                                } else if (interval.getStart().getMonthOfYear() == monthOfYear) {

                                    if (interval.getStart().getDayOfMonth() == dayOfMonth) {
                                        startIndex = calculateTimeOfDay(interval.getStart());
                                        addSpecialDiv(builder, convertToEm(numberOfUnits - (startIndex - 1)),
                                                convertToEm(startIndex - 1));

                                    } else if (interval.getStart().getDayOfMonth() < dayOfMonth) {
                                        addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);
                                    }
                                }
                            }
                            // Ended in same year and started before
                            else if (interval.getStart().getYear() < year && interval.getEnd().getYear() == year) {

                                if (interval.getEnd().getMonthOfYear() > monthOfYear) {
                                    addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

                                } else if (interval.getEnd().getMonthOfYear() == monthOfYear) {

                                    if (interval.getEnd().getDayOfMonth() > dayOfMonth) {
                                        addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

                                    } else if (interval.getEnd().getDayOfMonth() == dayOfMonth) {
                                        endIndex = calculateTimeOfDay(interval.getEnd());
                                        addSpecialDiv(builder, convertToEm(endIndex), EMPTY_UNIT);
                                    }
                                }
                            }
                            // Ended and Started In Same Year
                            else if (interval.getStart().getYear() == year && interval.getEnd().getYear() == year) {

                                if (interval.getStart().getMonthOfYear() <= monthOfYear
                                        && interval.getEnd().getMonthOfYear() >= monthOfYear) {

                                    if (interval.getStart().getMonthOfYear() == monthOfYear
                                            && interval.getEnd().getMonthOfYear() > monthOfYear) {

                                        if (interval.getStart().getDayOfMonth() == dayOfMonth) {
                                            startIndex = calculateTimeOfDay(interval.getStart());
                                            addSpecialDiv(builder, convertToEm(numberOfUnits - (startIndex - 1)),
                                                    convertToEm(startIndex - 1));

                                        } else if (interval.getStart().getDayOfMonth() < dayOfMonth) {
                                            addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);
                                        }

                                    } else if (interval.getStart().getMonthOfYear() < monthOfYear
                                            && interval.getEnd().getMonthOfYear() == monthOfYear) {

                                        if (interval.getEnd().getDayOfMonth() > dayOfMonth) {
                                            addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

                                        } else if (interval.getEnd().getDayOfMonth() == dayOfMonth) {
                                            endIndex = calculateTimeOfDay(interval.getEnd());
                                            addSpecialDiv(builder, convertToEm(endIndex), EMPTY_UNIT);
                                        }

                                    } else if (interval.getStart().getMonthOfYear() < monthOfYear
                                            && interval.getEnd().getMonthOfYear() > monthOfYear) {
                                        addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

                                    } else if (interval.getStart().getMonthOfYear() == monthOfYear
                                            && interval.getEnd().getMonthOfYear() == monthOfYear) {

                                        if (interval.getStart().getDayOfMonth() <= dayOfMonth
                                                && interval.getEnd().getDayOfMonth() >= dayOfMonth) {

                                            if (event.isGanttDiagramEventIntervalsLongerThanOneDay()
                                                    && (interval.getStart().getDayOfMonth() == dayOfMonth || interval.getEnd()
                                                            .getDayOfMonth() > dayOfMonth)) {
                                                startIndex = calculateTimeOfDay(interval.getStart());
                                                addSpecialDiv(builder, convertToEm(numberOfUnits - (startIndex - 1)),
                                                        convertToEm(startIndex - 1));
                                            } else if (interval.getStart().getDayOfMonth() == dayOfMonth
                                                    && interval.getEnd().getDayOfMonth() > dayOfMonth) {
                                                startIndex = calculateTimeOfDay(interval.getStart());
                                                addSpecialDiv(builder, convertToEm(numberOfUnits - (startIndex - 1)),
                                                        convertToEm(startIndex - 1));
                                            }

                                            else if (interval.getStart().getDayOfMonth() < dayOfMonth
                                                    && interval.getEnd().getDayOfMonth() == dayOfMonth) {
                                                endIndex = calculateTimeOfDay(interval.getEnd());
                                                addSpecialDiv(builder, convertToEm(endIndex), EMPTY_UNIT);
                                            }

                                            else if (interval.getStart().getDayOfMonth() == dayOfMonth
                                                    && interval.getEnd().getDayOfMonth() == dayOfMonth) {

                                                startIndex = calculateTimeOfDay(interval.getStart());
                                                endIndex = calculateTimeOfDay(interval.getEnd());

                                                if (startIndex == endIndex) {
                                                    addSpecialDiv(builder, convertToEm(1), convertToEm(startIndex - 1));
                                                } else {
                                                    addSpecialDiv(builder, convertToEm((endIndex - startIndex) + 1),
                                                            convertToEm(startIndex - 1));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!isEventToMarkWeekendsAndHolidays) {
                        builder.append("</div></td>");
                    } else if (!specialDiv) {
                        builder.append("<td class=\"tdnomark\">");
                        if (dayOfMonth <= yearMonthDay.plusMonths(1).minusDays(1).getDayOfMonth()) {
                            LocalDate localDate = yearMonthDay.withDayOfMonth(dayOfMonth).toLocalDate();
                            if (Holiday.isHoliday(localDate)) {
                                builder.append(F);
                            } else if (localDate.getDayOfWeek() == SATURDAY_IN_JODA_TIME) {
                                builder.append("S");
                            } else if (localDate.getDayOfWeek() == SUNDAY_IN_JODA_TIME) {
                                builder.append("D");
                            }
                        }
                        builder.append("</td>");
                    }
                }
                if (isShowPeriod()) {
                    builder.append("<td class=\"padded smalltxt\" title=\"").append(event.getGanttDiagramEventPeriod())
                            .append("\"><div style=\"overflow:hidden;\" class=\"nowrap\">")
                            .append(event.getGanttDiagramEventPeriod()).append("</div></td>");
                }
                if (isShowObservations()) {
                    builder.append("<td class=\"padded smalltxt\">").append(event.getGanttDiagramEventObservations())
                            .append("</td>");
                }
                builder.append("</tr>");
            }
            insertNextAndBeforeLinks(builder);
            builder.append("</table>");
        }
        return builder;
    }

    private StringBuilder generateGanttDiagramInTotalMode(BigDecimal tableWidth) throws JspException {

        StringBuilder builder = new StringBuilder();

        if (!getEvents().isEmpty()) {
            if (isShowPeriod() && isShowObservations()) {
                builder.append("<table style=\"width:").append(tableWidth.add(BigDecimal.valueOf(FIXED_COLUMNS_SIZE_EM)))
                        .append("em;\" class=\"tcalendar thlight\">");
            } else {
                builder.append("<table style=\"width:").append(tableWidth.add(BigDecimal.valueOf(FIXED_COLUMNS_SIZE_EM - 35)))
                        .append("em;\" class=\"tcalendar thlight\">");
            }
            generateHeaders(builder);

            int scale = getScale();

            String selectedEvent = getRequest().getParameter(getEventParameter());
            Object selectedEventObject = getRequest().getAttribute(getEventParameter());

            for (GanttDiagramEvent event : getEvents()) {

                String eventUrl =
                        getRequest().getContextPath() + getEventUrl() + "&amp;" + getEventParameter() + "="
                                + event.getGanttDiagramEventIdentifier();
                String eventName = event.getGanttDiagramEventName().getContent(getGanttDiagramObject().getLocale());
                String paddingStyle = "padding-left:" + event.getGanttDiagramEventOffset() * PADDING_LEFT_MULTIPLIER + "px";

                if (event.getGanttDiagramEventIdentifier().equals(selectedEvent)
                        || (selectedEventObject != null && event.getGanttDiagramEventIdentifier().equals(
                                selectedEventObject.toString()))) {
                    builder.append("<tr class=\"selected\">");
                } else {
                    builder.append("<tr>");
                }

                builder.append("<td class=\"padded\">")
                        .append("<div style=\"overflow:hidden; width: 14.5em;\" class=\"nowrap\">");
                builder.append("<span style=\"").append(paddingStyle).append("\" title=\"").append(eventName).append("\">");
                builder.append("<a href=\"").append(eventUrl).append("\">").append("*").append(eventName);
                builder.append("</a></span></div></td>");

                for (DateTime month : getGanttDiagramObject().getMonths()) {

                    DateTime firstDayOfMonth = (month.getDayOfMonth() != 1) ? month.withDayOfMonth(1) : month;
                    DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
                    int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;
                    BigDecimal entryDays = EMPTY_UNIT, startDay = EMPTY_UNIT;

                    builder.append("<td style=\"width: ").append(convertToEm(monthNumberOfDays * scale))
                            .append("em;\"><div style=\"position: relative;\">");

                    for (Interval interval : event.getGanttDiagramEventSortedIntervals()) {

                        DateMidnight intervalStart = interval.getStart().toDateMidnight();
                        DateMidnight intervalEnd = interval.getEnd().toDateMidnight();

                        // Started in this month
                        if (intervalStart.getMonthOfYear() == month.getMonthOfYear()
                                && intervalStart.getYear() == month.getYear()) {

                            // Ended in this month
                            if (interval.getEnd().getMonthOfYear() == month.getMonthOfYear()
                                    && intervalEnd.getYear() == month.getYear()) {

                                // Started in first day of this month
                                if (intervalStart.getDayOfMonth() == 1) {

                                    // Ended in the last day of this month
                                    if (intervalEnd.getDayOfMonth() == monthNumberOfDays) {
                                        entryDays =
                                                convertToEm((Days.daysBetween(intervalStart, lastDayOfMonth).getDays() + 1)
                                                        * scale);
                                        startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);
                                        addSpecialDiv(builder, entryDays, startDay);
                                    }

                                    // Ended before last day of this month
                                    else {
                                        entryDays =
                                                convertToEm((Days.daysBetween(intervalStart, intervalEnd).getDays() + 1) * scale);
                                        startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);
                                        addSpecialDiv(builder, entryDays, startDay);
                                    }
                                }

                                // Started after first day of this month
                                else {

                                    // Ended in the last day of this month
                                    if (intervalEnd.getDayOfMonth() == monthNumberOfDays) {
                                        entryDays =
                                                convertToEm((Days.daysBetween(intervalStart, lastDayOfMonth).getDays() + 1)
                                                        * scale);
                                        startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);
                                        addSpecialDiv(builder, entryDays, startDay);
                                    }

                                    // Ended before last day of this month
                                    else {
                                        entryDays =
                                                convertToEm((Days.daysBetween(intervalStart, intervalEnd).getDays() + 1) * scale);
                                        startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);
                                        addSpecialDiv(builder, entryDays, startDay);
                                    }
                                }
                            }

                            // Ended after this month
                            else {
                                entryDays = convertToEm((Days.daysBetween(intervalStart, lastDayOfMonth).getDays() + 1) * scale);
                                startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);
                                addSpecialDiv(builder, entryDays, startDay);
                            }

                            // Not Started in this month
                        } else {

                            // Started before this month
                            if (intervalStart.getYear() < month.getYear()
                                    || (intervalStart.getYear() == month.getYear() && intervalStart.getMonthOfYear() < month
                                            .getMonthOfYear())) {

                                // Ended after this month
                                if (intervalEnd.getYear() > month.getYear()
                                        || (intervalEnd.getYear() == month.getYear() && intervalEnd.getMonthOfYear() > month
                                                .getMonthOfYear())) {

                                    entryDays =
                                            convertToEm((Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1) * scale);
                                    startDay = convertToEm((firstDayOfMonth.getDayOfMonth() - 1) * scale);
                                    addSpecialDiv(builder, entryDays, startDay);
                                } else {

                                    // Ended in this month
                                    if (intervalEnd.getMonthOfYear() == month.getMonthOfYear()
                                            && intervalEnd.getYear() == month.getYear()) {
                                        entryDays =
                                                convertToEm((Days.daysBetween(firstDayOfMonth, intervalEnd).getDays() + 1)
                                                        * scale);
                                        startDay = convertToEm((firstDayOfMonth.getDayOfMonth() - 1) * scale);
                                        addSpecialDiv(builder, entryDays, startDay);
                                    }
                                }
                            }
                        }
                    }
                    builder.append("</div></td>");
                }
                if (isShowPeriod()) {
                    builder.append("<td class=\"padded smalltxt\" title=\"").append(event.getGanttDiagramEventPeriod())
                            .append("\"><div style=\"overflow:hidden;\" class=\"nowrap\">")
                            .append(event.getGanttDiagramEventPeriod()).append("</div></td>");
                }
                if (isShowObservations()) {
                    builder.append("<td class=\"padded smalltxt\">").append(event.getGanttDiagramEventObservations())
                            .append("</td>");
                }
                builder.append("</tr>");
            }

            insertNextAndBeforeLinks(builder);
            builder.append("</table>");
        }
        return builder;
    }

    private void generateHeaders(StringBuilder builder) throws JspException {

        switch (getViewTypeEnum()) {

        case WEEKLY:

            builder.append("<tr>");
            builder.append("<th style=\"width: 15em;\" rowspan=\"4\">").append(getMessage("label.ganttDiagram.event"))
                    .append("</th>");
            for (Integer year : getGanttDiagramObject().getYearsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getYearsView().get(year)).append("\">")
                        .append(year).append("</th>");
            }
            if (isShowPeriod()) {
                builder.append("<th style=\"width: 20em;\" rowspan=\"4\">").append(getMessage("label.ganttDiagram.period"))
                        .append("</th>");
            }
            if (isShowObservations()) {
                builder.append("<th rowspan=\"4\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            }
            builder.append("</tr>");

            builder.append("<tr>");
            if (!StringUtils.isEmpty(getMonthlyViewUrl())) {
                String monthlyViewUrl_ =
                        getRequest().getContextPath() + getMonthlyViewUrl() + "&amp;" + getFirstDayParameter() + "=";
                for (YearMonthDay month : getGanttDiagramObject().getMonthsView().keySet()) {
                    builder.append("<th colspan=\"").append(getGanttDiagramObject().getMonthsView().get(month)).append("\">")
                            .append("<a href=\"").append(monthlyViewUrl_).append(month.toString("ddMMyyyy")).append("\">")
                            .append(month.toString("MMM", getGanttDiagramObject().getLocale())).append("</a>").append("</th>");
                }
            } else {
                for (YearMonthDay month : getGanttDiagramObject().getMonthsView().keySet()) {
                    builder.append("<th colspan=\"").append(getGanttDiagramObject().getMonthsView().get(month)).append("\">")
                            .append(month.toString("MMM", getGanttDiagramObject().getLocale())).append("</th>");
                }
            }
            builder.append("</tr>");

            builder.append("<tr>");
            for (DateTime day : getGanttDiagramObject().getDays()) {
                builder.append("<th>").append(day.toString("E", getGanttDiagramObject().getLocale())).append("</th>");
            }
            builder.append("</tr>");

            builder.append("<tr>");
            if (!StringUtils.isEmpty(getDailyViewUrl())) {
                String dailyViewUrl_ = getRequest().getContextPath() + getDailyViewUrl() + "&amp;" + getFirstDayParameter() + "=";
                for (DateTime day : getGanttDiagramObject().getDays()) {
                    builder.append("<th>").append("<a href=\"").append(dailyViewUrl_).append(day.toString("ddMMyyyy"))
                            .append("\">").append(day.getDayOfMonth()).append("</a>").append("</th>");
                }
            } else {
                for (DateTime day : getGanttDiagramObject().getDays()) {
                    builder.append("<th>").append(day.getDayOfMonth()).append("</th>");
                }
            }

            builder.append("</tr>");
            break;

        case MONTHLY:

            builder.append("<tr>");
            builder.append("<th style=\"width: 15em;\" rowspan=\"2\">").append(getMessage("label.ganttDiagram.event"))
                    .append("</th>");
            for (YearMonthDay month : getGanttDiagramObject().getMonthsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getMonthsView().get(month)).append("\">")
                        .append(month.toString("MMM yyyy", getGanttDiagramObject().getLocale())).append("</th>");
            }
            if (isShowPeriod()) {
                builder.append("<th style=\"width: 20em;\" rowspan=\"2\">").append(getMessage("label.ganttDiagram.period"))
                        .append("</th>");
            }
            if (isShowObservations()) {
                builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            }
            builder.append("</tr>");

            builder.append("<tr>");
            if (!StringUtils.isEmpty(getDailyViewUrl())) {
                String dailyViewUrl_ = getRequest().getContextPath() + getDailyViewUrl() + "&amp;" + getFirstDayParameter() + "=";
                for (DateTime day : getGanttDiagramObject().getDays()) {
                    builder.append("<th>").append("<a href=\"").append(dailyViewUrl_).append(day.toString("ddMMyyyy"))
                            .append("\">").append(day.getDayOfMonth()).append("</a>").append("</th>");
                }
            } else {
                for (DateTime day : getGanttDiagramObject().getDays()) {
                    builder.append("<th>").append(day.getDayOfMonth()).append("</th>");
                }
            }

            builder.append("</tr>");
            break;

        case DAILY:

            builder.append("<tr>");
            builder.append("<th style=\"width: 15em;\">").append(getMessage("label.ganttDiagram.event")).append("</th>");
            builder.append("<th>")
                    .append(getGanttDiagramObject().getFirstInstant().toString("E", getGanttDiagramObject().getLocale()))
                    .append(", ").append(getGanttDiagramObject().getFirstInstant().getDayOfMonth()).append(" ");

            if (!StringUtils.isEmpty(getMonthlyViewUrl())) {
                String monthlyViewUrl_ =
                        getRequest().getContextPath() + getMonthlyViewUrl() + "&amp;" + getFirstDayParameter() + "=";
                builder.append("<a href=\"").append(monthlyViewUrl_)
                        .append(getGanttDiagramObject().getFirstInstant().toString("ddMMyyyy")).append("\">")
                        .append(getGanttDiagramObject().getFirstInstant().toString("MMM", getGanttDiagramObject().getLocale()))
                        .append("</a>");
            } else {
                builder.append(getGanttDiagramObject().getFirstInstant().toString("MMM", getGanttDiagramObject().getLocale()));
            }

            builder.append(" ").append(getGanttDiagramObject().getFirstInstant().getYear());

            if (!StringUtils.isEmpty(getWeeklyViewUrl())) {
                String weeklyViewUrl_ =
                        getRequest().getContextPath() + getWeeklyViewUrl() + "&amp;" + getFirstDayParameter() + "=";
                builder.append(" (<a href=\"").append(weeklyViewUrl_)
                        .append(getGanttDiagramObject().getFirstInstant().toString("ddMMyyyy")).append("\">");
                builder.append(getMessage("label.ganttDiagram.week"))
                        .append(getGanttDiagramObject().getFirstInstant().getWeekOfWeekyear()).append(")</a>");
            }

            builder.append("</th>");
            if (isShowPeriod()) {
                builder.append("<th style=\"width: 20em;\">").append(getMessage("label.ganttDiagram.period")).append("</th>");
            }
            if (isShowObservations()) {
                builder.append("<th>").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            }
            builder.append("</tr>");
            break;

        case TOTAL:

            builder.append("<tr>");
            builder.append("<th style=\"width: 15em;\" rowspan=\"2\">").append(getMessage("label.ganttDiagram.event"))
                    .append("</th>");
            for (Integer year : getGanttDiagramObject().getYearsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getYearsView().get(year)).append("\">")
                        .append(year).append("</th>");
            }
            if (isShowPeriod()) {
                builder.append("<th style=\"width: 20em;\" rowspan=\"2\">").append(getMessage("label.ganttDiagram.period"))
                        .append("</th>");
            }
            if (isShowObservations()) {
                builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            }
            builder.append("</tr>");

            builder.append("<tr>");
            for (DateTime month : getGanttDiagramObject().getMonths()) {
                builder.append("<th>").append(month.toString("MMM", getGanttDiagramObject().getLocale())).append("</th>");
            }
            builder.append("</tr>");
            break;

        case MONTHLY_TOTAL:

            builder.append("<tr>");
            builder.append("<th style=\"width: 15em;\">").append(getMessage("label.ganttDiagram.event")).append("</th>");
            builder.append("<th>")
                    .append(getGanttDiagramObject().getFirstInstant().toString("MMM", getGanttDiagramObject().getLocale()))
                    .append(" ").append(getGanttDiagramObject().getFirstInstant().getYear()).append("</th>");
            if (isShowPeriod()) {
                builder.append("<th style=\"width: 20em;\">").append(getMessage("label.ganttDiagram.period")).append("</th>");
            }
            if (isShowObservations()) {
                builder.append("<th>").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            }
            builder.append("</tr>");
            break;

        case YEAR_DAILY:

            builder.append("<tr>");
            builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.event")).append("</th>");
            for (Integer year : getGanttDiagramObject().getYearsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getYearsView().get(year)).append("\">")
                        .append(year).append("</th>");
            }
            if (isShowPeriod()) {
                builder.append("<th style=\"width: 20em;\" rowspan=\"2\">").append(getMessage("label.ganttDiagram.period"))
                        .append("</th>");
            }
            if (isShowObservations()) {
                builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            }
            builder.append("</tr>");

            builder.append("<tr>");
            if (!StringUtils.isEmpty(getDailyViewUrl())) {
                String dailyViewUrl_ = getRequest().getContextPath() + getDailyViewUrl() + "&amp;" + getFirstDayParameter() + "=";
                for (DateTime day : getGanttDiagramObject().getDays()) {
                    builder.append("<th>").append("<a href=\"").append(dailyViewUrl_).append(day.toString("ddMMyyyy"))
                            .append("\">").append(day.getDayOfMonth()).append("</a>").append("</th>");
                }
            } else {
                for (DateTime day : getGanttDiagramObject().getDays()) {
                    builder.append("<th>").append(day.getDayOfMonth()).append("</th>");
                }
            }

            builder.append("</tr>");
            break;

        default:
            break;
        }
    }

    private void insertNextAndBeforeLinks(StringBuilder builder) throws JspException {

        YearMonthDay firstDay = getGanttDiagramObject().getFirstInstant().toYearMonthDay();
        if (firstDay != null) {

            String nextUrl = "";
            String beforeUrl = "";

            switch (getViewTypeEnum()) {

            case WEEKLY:

                if (!StringUtils.isEmpty(getWeeklyViewUrl())) {
                    nextUrl =
                            getRequest().getContextPath() + getWeeklyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK).toString("ddMMyyyy");
                    beforeUrl =
                            getRequest().getContextPath() + getWeeklyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.minusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK).toString("ddMMyyyy");
                    builder.append(
                            "<tr><td class=\"tcalendarlinks\"></td><td colspan=\"7\" class=\"acenter tcalendarlinks\"> <span class=\"smalltxt\"><a href=\"")
                            .append(beforeUrl).append("\">").append("&lt;&lt; ").append(getMessage("label.previous.week"))
                            .append("</a>");
                    builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">")
                            .append(getMessage("label.next.week")).append(" &gt;&gt;").append("</a>")
                            .append("</span></td><td class=\"tcalendarlinks\"></td><td class=\"tcalendarlinks\"></td></tr>");
                }
                break;

            case DAILY:

                if (!StringUtils.isEmpty(getDailyViewUrl())) {
                    nextUrl =
                            getRequest().getContextPath() + getDailyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.plusDays(1).toString("ddMMyyyy");
                    beforeUrl =
                            getRequest().getContextPath() + getDailyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.minusDays(1).toString("ddMMyyyy");
                    builder.append(
                            "<tr><td class=\"tcalendarlinks\"></td><td class=\"acenter tcalendarlinks\"><span class=\"smalltxt\"><a href=\"")
                            .append(beforeUrl).append("\">").append("&lt;&lt; ").append(getMessage("label.previous.day"))
                            .append("</a>");
                    builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">").append(getMessage("label.next.day"))
                            .append(" &gt;&gt;").append("</a>")
                            .append("</span></td><td class=\"tcalendarlinks\"></td><td class=\"tcalendarlinks\"></td></tr>");
                }
                break;

            case MONTHLY:

                if (!StringUtils.isEmpty(getMonthlyViewUrl())) {
                    DateTime month = firstDay.toDateTimeAtMidnight();
                    DateTime firstDayOfMonth = (month.getDayOfMonth() != 1) ? month.withDayOfMonth(1) : month;
                    DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
                    int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;
                    nextUrl =
                            getRequest().getContextPath() + getMonthlyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.plusMonths(1).toString("ddMMyyyy");
                    beforeUrl =
                            getRequest().getContextPath() + getMonthlyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.minusMonths(1).toString("ddMMyyyy");
                    builder.append("<tr><td class=\"tcalendarlinks\"></td><td colspan=\"").append(monthNumberOfDays)
                            .append("\" class=\"acenter tcalendarlinks\"><span class=\"smalltxt\"><a href=\"").append(beforeUrl)
                            .append("\">").append("&lt;&lt; ").append(getMessage("label.previous.month")).append("</a>");
                    builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">")
                            .append(getMessage("label.next.month")).append(" &gt;&gt;").append("</a>")
                            .append("</span></td><td class=\"tcalendarlinks\"></td><td class=\"tcalendarlinks\"></td></tr>");
                }
                break;

            case YEAR_DAILY:

                if (!StringUtils.isEmpty(getMonthlyViewUrl())) {
                    DateTime month = firstDay.toDateTimeAtMidnight();
                    DateTime firstDayOfMonth = (month.getDayOfMonth() != 1) ? month.withDayOfMonth(1) : month;
                    DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
                    int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;
                    nextUrl =
                            getRequest().getContextPath() + getMonthlyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.plusMonths(1).toString("ddMMyyyy");
                    beforeUrl =
                            getRequest().getContextPath() + getMonthlyViewUrl() + "&amp;" + getFirstDayParameter() + "="
                                    + firstDay.minusMonths(1).toString("ddMMyyyy");
                    builder.append("<tr><td class=\"tcalendarlinks\"></td><td colspan=\"").append(monthNumberOfDays)
                            .append("\" class=\"acenter tcalendarlinks\"><span class=\"smalltxt\"><a href=\"").append(beforeUrl)
                            .append("\">").append("&lt;&lt; ").append(getMessage("label.previous.month")).append("</a>");
                    builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">")
                            .append(getMessage("label.next.month")).append(" &gt;&gt;").append("</a>")
                            .append("</span></td><td class=\"tcalendarlinks\"></td><td class=\"tcalendarlinks\"></td></tr>");
                }
                break;

            default:
                break;
            }
        }
    }

    private int getScale() {

        switch (getViewTypeEnum()) {

        case TOTAL:
            return 1;

        case MONTHLY_TOTAL:
            return 10;

        default:
            return 0;
        }
    }

    private int calculateTimeOfDay(DateTime time) {

        int hourOfDay = time.getHourOfDay();
        int minuteOfHour = time.getMinuteOfHour();

        switch (getViewTypeEnum()) {

        case WEEKLY:

            // unit = 15 minutes
            int result = (hourOfDay + 1) * 2;
            if (minuteOfHour <= 30) {
                return result - 1;
            } else {
                return result;
            }

        case DAILY:

            // unit = 5 minutes
            for (int i = 1, j = 0; j < 60; j += 5, i++) {
                if (minuteOfHour < j + 5) {
                    return i + (12 * hourOfDay);
                }
            }

        case MONTHLY:

            // unit = hour of day
            return hourOfDay;

        case YEAR_DAILY:

            // unit = hour of day
            return hourOfDay;

        default:
            return 0;
        }
    }

    private int getNumberOfUnits() {
        switch (getViewTypeEnum()) {

        case WEEKLY:
            return NUMBER_OF_DAY_HALF_HOURS;

        case DAILY:
            return NUMBER_OF_DAY_5_MINUTES;

        case MONTHLY:
            return NUMBER_OF_DAY_HOURS;

        case YEAR_DAILY:
            return NUMBER_OF_DAY_HOURS;

        default:
            break;
        }
        return 0;
    }

    private void addEmptyDiv(StringBuilder builder) {
        builder.append("<td class=\"");
        builder.append(EMPTY_TD_BAR);
        builder.append("\"></td>");
    }

    private void addSpecialDiv(StringBuilder builder, BigDecimal entryDays, BigDecimal startDay) {
        specialDiv = true;
        if (isEventToMarkWeekendsAndHolidays) {
            builder.append("<td");
            if (toMark) {
                builder.append(" class=\"");
                builder.append(TD_BAR);
                builder.append("\">");
            } else {
                builder.append(" class=\"tdnomark\">");
            }
            if (toWrite != null) {
                builder.append(toWrite);
            }
            builder.append("</td>");
        } else {
            builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
            builder.append(startDay);
            builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");
        }
    }

    private BigDecimal convertToEm(int value) {
        return BigDecimal.valueOf(value).divide(PX_TO_EM_CONVERSION_DIVISOR, 2, RoundingMode.HALF_UP);
    }

    private String getMessage(String key) throws JspException {
        String message = getMessageFromBundle(key);
        return (message != null) ? message : key;
    }

    private String getMessageFromBundle(String key) throws JspException {
        return (getBundle() != null) ? ((TagUtils.getInstance().present(this.pageContext, getBundle(), getGanttDiagramObject()
                .getLocale().toString(), key)) ? TagUtils.getInstance().message(this.pageContext, getBundle(),
                getGanttDiagramObject().getLocale().toString(), key) : null) : null;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public List<? extends GanttDiagramEvent> getEvents() {
        return events;
    }

    public void setEvents(List<? extends GanttDiagramEvent> events) {
        this.events = events;
    }

    public GanttDiagram getGanttDiagramObject() {
        return ganttDiagramObject;
    }

    public void setGanttDiagramObject(GanttDiagram ganttDiagramObject) {
        this.ganttDiagramObject = ganttDiagramObject;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String url) {
        this.eventUrl = url;
    }

    public String getEventParameter() {
        return eventParameter;
    }

    public void setEventParameter(String parameter) {
        this.eventParameter = parameter;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getGanttDiagram() {
        return ganttDiagram;
    }

    public void setGanttDiagram(String ganttDiagram) {
        this.ganttDiagram = ganttDiagram;
    }

    public ViewType getViewTypeEnum() {
        return viewTypeEnum;
    }

    public void setViewTypeEnum(ViewType viewTypeEnum) {
        this.viewTypeEnum = viewTypeEnum;
    }

    public String getFirstDayParameter() {
        return firstDayParameter;
    }

    public void setFirstDayParameter(String firstDayParameter) {
        this.firstDayParameter = firstDayParameter;
    }

    public String getDailyViewUrl() {
        return dailyViewUrl;
    }

    public void setDailyViewUrl(String dailyViewMode) {
        this.dailyViewUrl = dailyViewMode;
    }

    public String getMonthlyViewUrl() {
        return monthlyViewUrl;
    }

    public void setMonthlyViewUrl(String monthlyViewUrl) {
        this.monthlyViewUrl = monthlyViewUrl;
    }

    public String getWeeklyViewUrl() {
        return weeklyViewUrl;
    }

    public void setWeeklyViewUrl(String weeklyViewUrl) {
        this.weeklyViewUrl = weeklyViewUrl;
    }

    public boolean isShowPeriod() {
        return showPeriod;
    }

    public void setShowPeriod(boolean showPeriod) {
        this.showPeriod = showPeriod;
    }

    public boolean isShowObservations() {
        return showObservations;
    }

    public void setShowObservations(boolean showObservations) {
        this.showObservations = showObservations;
    }
}
