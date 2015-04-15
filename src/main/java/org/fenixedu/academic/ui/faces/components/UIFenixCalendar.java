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
package org.fenixedu.academic.ui.faces.components;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.fenixedu.academic.ui.faces.components.util.CalendarLink;
import org.fenixedu.academic.util.DateFormatUtil;

public class UIFenixCalendar extends UIInput {
    public static final String COMPONENT_TYPE = "org.fenixedu.academic.ui.faces.components.UIFenixCalendar";

    public static final String COMPONENT_FAMILY = "org.fenixedu.academic.ui.faces.components.UIFenixCalendar";

    public UIFenixCalendar() {
        super();
        this.setRendererType(null);
    }

    @Override
    public String getFamily() {
        return UIFenixCalendar.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();

        Calendar[] begins = getDateArgument("begin");
        Calendar[] ends = getDateArgument("end");

        for (int i = 0; i < begins.length; i++) {
            final Calendar begin = begins[i];
            final Calendar end = ends[i];

            if (begin == null || end == null) {
                writer.write("<!-- begin and end dates must be specified -->");
            } else if (end.before(begin)) {
                writer.write("<!-- end date must be after begin date -->");
            } else {
                if (begin.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                    encodeMonthTable(writer, context, "MONTH" + begin.get(Calendar.MONTH), begin, end);
                } else {
//                    boolean isFirst = true;
                    for (; begin.before(end); setToFirstDayOfNextMonth(begin)) {
                        Calendar endPeriod = Calendar.getInstance();
                        endPeriod.setTime(begin.getTime());
                        endPeriod.set(Calendar.DAY_OF_MONTH, begin.getActualMaximum(Calendar.DAY_OF_MONTH));

                        if (endPeriod.after(end)) {
                            endPeriod.setTime(end.getTime());
                        }

//                        if (isFirst) {
//                            isFirst = false;
//                        } else {
//                            writer.append("<br style='page-break-after:always;'/>");
//                        }

                        encodeMonthTable(writer, context, "MONTH" + begin.get(Calendar.MONTH), begin, endPeriod);
                    }
                }
            }
        }
    }

    private Calendar[] getDateArgument(String argumentName) {
        final Object object = this.getAttributes().get(argumentName);
        if (object instanceof Calendar) {
            return new Calendar[] { (Calendar) object };
        } else if (object instanceof Date && object != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) object);
            return new Calendar[] { calendar };
        } else if (object instanceof Object[]) {
            final Object[] objects = (Object[]) object;
            final Calendar[] result = new Calendar[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof Calendar) {
                    result[i] = (Calendar) objects[i];
                } else if (objects[i] instanceof Date && objects[i] != null) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) objects[i]);
                    result[i] = calendar;
                } else {
                    result[i] = null;
                }
            }
            return result;
        } else {
            return new Calendar[0];
        }
    }

    private static void setToFirstDayOfNextMonth(Calendar date) {
        if (date.get(Calendar.MONTH) < 11) {
            date.roll(Calendar.MONTH, true);
        } else {
            date.roll(Calendar.MONTH, true);
            date.roll(Calendar.YEAR, true);
        }
        date.set(Calendar.DAY_OF_MONTH, 1);
    }

    private void encodeMonthTable(ResponseWriter writer, FacesContext context, String key, Calendar begin, Calendar end)
            throws IOException {
        writer.startElement("table", this);
        writer.writeAttribute("class", "table table-bordered", null);
        // writer.writeAttribute("class", "fenixCalendar breakafter", null);
        // writer.writeAttribute("name", getFieldKey(context, key), null);

        encodeMonthRow(writer, begin, context.getViewRoot().getLocale());
        encodeDaysOfWeek(writer, context.getViewRoot().getLocale());
        encodeWeeks(writer, begin, end);

        writer.endElement("table");
        writer.startElement("br", this);
        writer.endElement("br");
    }

    private String getFieldKey(FacesContext context, String key) {
        return new String(this.getClientId(context) + NamingContainer.SEPARATOR_CHAR + key);
    }

    private void encodeMonthRow(ResponseWriter writer, Calendar date, Locale locale) throws IOException {
        // writer.startElement("tr", this);
        // writer.startElement("td", this);
        writer.startElement("caption", this);
        writer.writeAttribute("style", "font-weight: 600; background: #bbb", null);
        writer.writeAttribute("class", "text-center", null);
        // writer.writeAttribute("colspan", 6, null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        DateFormatSymbols dfs = sdf.getDateFormatSymbols();
        writer.write((dfs.getMonths())[date.get(Calendar.MONTH)]);

        writer.endElement("caption");
        // writer.endElement("td");
        // writer.endElement("tr");
    }

    private void encodeDaysOfWeek(ResponseWriter writer, Locale locale) throws IOException {
        writer.startElement("tr", this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        DateFormatSymbols dfs = sdf.getDateFormatSymbols();

        encodeDayOfWeek(writer, (dfs.getWeekdays())[Calendar.MONDAY]);
        encodeDayOfWeek(writer, (dfs.getWeekdays())[Calendar.TUESDAY]);
        encodeDayOfWeek(writer, (dfs.getWeekdays())[Calendar.WEDNESDAY]);
        encodeDayOfWeek(writer, (dfs.getWeekdays())[Calendar.THURSDAY]);
        encodeDayOfWeek(writer, (dfs.getWeekdays())[Calendar.FRIDAY]);
        encodeDayOfWeek(writer, (dfs.getWeekdays())[Calendar.SATURDAY]);

        writer.endElement("tr");
    }

    private void encodeDayOfWeek(ResponseWriter writer, String dayLabel) throws IOException {
        writer.startElement("th", this);
        writer.write(dayLabel);
        writer.endElement("th");
    }

    private void encodeWeeks(ResponseWriter writer, Calendar begin, Calendar end) throws IOException {

        String createLink = (String) this.getAttributes().get("createLink");
        String editLinkPage = (String) this.getAttributes().get("editLinkPage");
        List<CalendarLink> editLinkParameters = (List<CalendarLink>) this.getAttributes().get("editLinkParameters");

        Calendar now = Calendar.getInstance();
        Calendar iter = Calendar.getInstance();
        iter.setTime(begin.getTime());

        for (int beginWeek = begin.get(Calendar.WEEK_OF_MONTH); beginWeek <= end.get(Calendar.WEEK_OF_MONTH); beginWeek++) {
            writer.startElement("tr", this);
            writer.writeAttribute("class", "text-right", null);
            for (int beginDayOfWeek = Calendar.MONDAY; beginDayOfWeek <= Calendar.SATURDAY; beginDayOfWeek++) {

                if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    doMonthAwareRoll(iter);
                }

                if (iter.after(end)) {
                    writer.startElement("td", this);
                    writer.writeAttribute("style", "width: 16.7%", null);
                    appendExtraLines(writer);
                    writer.endElement("td");
                } else if (iter.get(Calendar.DAY_OF_WEEK) == beginDayOfWeek) {
                    writer.startElement("td", this);

                    List<CalendarLink> toDisplay = objectsToDisplayOnThisDay(iter, editLinkParameters);
                    if (toDisplay != null && !toDisplay.isEmpty()) {
                        writer.writeAttribute("style", "background: #e6e6e6; width: 16.7%", null);
                        encodeDay(writer, createLink, now, iter);

                        for (CalendarLink calendarLink : toDisplay) {
                            writer.startElement("br", this);
                            writer.endElement("br");
                            if (calendarLink.isAsLink()) {
                                writer.startElement("a", this);
                                writer.writeAttribute("style", "text-decoration:none", null);
                                writer.writeAttribute("href", calendarLink.giveLink(editLinkPage), null);
                            }
                            writer.write(calendarLink.getObjectLinkLabel());
                            if (calendarLink.isAsLink()) {
                                writer.endElement("a");
                            }
                        }
                    } else {
                        writer.writeAttribute("style", "background: #eee; width: 16.7%", null);
                        encodeDay(writer, createLink, now, iter);
                    }

                    appendExtraLines(writer);

                    writer.endElement("td");

                    doMonthAwareRoll(iter);
                } else {
                    writer.startElement("td", this);
                    appendExtraLines(writer);
                    writer.endElement("td");
                }
            }
            writer.endElement("tr");
        }
    }

    private void appendExtraLines(final ResponseWriter writer) throws IOException {
        final String extraLines = (String) this.getAttributes().get("extraLines");
        if (extraLines != null && extraLines.length() > 0 && Boolean.valueOf(extraLines).equals(Boolean.TRUE)) {
            writer.startElement("br", this);
            writer.endElement("br");
            writer.startElement("br", this);
            writer.endElement("br");
            writer.startElement("br", this);
            writer.endElement("br");
            writer.startElement("br", this);
            writer.endElement("br");
            writer.startElement("br", this);
            writer.endElement("br");
            writer.startElement("br", this);
            writer.endElement("br");
        }
    }

    private void encodeDay(ResponseWriter writer, String createLink, Calendar now, Calendar iter) throws IOException {
        if (createLink == null || iter.before(now)) {
            writer.write(new Integer(iter.get(Calendar.DAY_OF_MONTH)).toString());
        } else {
            writer.startElement("a", this);
            writer.writeAttribute("style", "text-decoration:none", null);
            writer.writeAttribute("href", createLink + dateLink(iter), null);
            writer.write(new Integer(iter.get(Calendar.DAY_OF_MONTH)).toString());
            writer.endElement("a");
        }
    }

    private List<CalendarLink> objectsToDisplayOnThisDay(Calendar iter, List<CalendarLink> editLinkParameters) {
        List<CalendarLink> result = new ArrayList<CalendarLink>();

        for (CalendarLink calendarLink : editLinkParameters) {
            if (calendarLink.getObjectOccurrence().get(Calendar.DAY_OF_MONTH) == iter.get(Calendar.DAY_OF_MONTH)
                    && calendarLink.getObjectOccurrence().get(Calendar.MONTH) == iter.get(Calendar.MONTH)
                    && calendarLink.getObjectOccurrence().get(Calendar.YEAR) == iter.get(Calendar.YEAR)) {
                result.add(calendarLink);
            }
        }

        return result;
    }

    private void doMonthAwareRoll(Calendar date) {
        if (date.get(Calendar.DAY_OF_MONTH) < date.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            date.roll(Calendar.DAY_OF_MONTH, true);
        } else {
            date.roll(Calendar.DAY_OF_MONTH, true);
            date.roll(Calendar.MONTH, true);
        }
    }

    private String dateLink(Calendar date) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&day=");
        stringBuilder.append(date.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append("&month=");
        stringBuilder.append((date.get(Calendar.MONTH) + 1));
        stringBuilder.append("&year=");
        stringBuilder.append(date.get(Calendar.YEAR));
        stringBuilder.append("&selectedDate=");
        stringBuilder.append(DateFormatUtil.format("dd/MM/yyyy", date.getTime()));
        return stringBuilder.toString();
    }

}
