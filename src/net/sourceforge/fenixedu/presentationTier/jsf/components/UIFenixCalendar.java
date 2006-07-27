package net.sourceforge.fenixedu.presentationTier.jsf.components;

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

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class UIFenixCalendar extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIFenixCalendar";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIFenixCalendar";

    public UIFenixCalendar() {
        super();
        this.setRendererType(null);
    }

    public String getFamily() {
        return UIFenixCalendar.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        Calendar begin = getDateArgument("begin");
        Calendar end = getDateArgument("end");

        if (begin == null || end == null) {
            writer.write("<!-- begin and end dates must be specified -->");
        } else if (!end.after(begin)) {
            writer.write("<!-- end date must be after begin date -->");
        } else {
            if (begin.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                encodeMonthTable(writer, context, "MONTH" + begin.get(Calendar.MONTH), begin, end);
            } else {
                for (; begin.before(end); setToFirstDayOfNextMonth(begin)) {
                    Calendar endPeriod = Calendar.getInstance();
                    endPeriod.setTime(begin.getTime());
                    endPeriod.set(Calendar.DAY_OF_MONTH, begin.getActualMaximum(Calendar.DAY_OF_MONTH));

                    if (endPeriod.after(end)) {
                        endPeriod.setTime(end.getTime());
                    }

                    encodeMonthTable(writer, context, "MONTH" + begin.get(Calendar.MONTH), begin,
                            endPeriod);
                }
            }
        }
    }

    private Calendar getDateArgument(String argumentName) {
        final Object object = this.getAttributes().get(argumentName);
        if (object instanceof Calendar) {
            return (Calendar) object;
        } else if (object instanceof Date && object != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) object);
            return calendar;
        } else {
            return null;
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

    private void encodeMonthTable(ResponseWriter writer, FacesContext context, String key,
            Calendar begin, Calendar end) throws IOException {
        writer.startElement("table", this);
        writer.writeAttribute("class", "fenixCalendar", null);
        //writer.writeAttribute("name", getFieldKey(context, key), null);

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
        writer.startElement("tr", this);
        writer.startElement("td", this);
        writer.writeAttribute("class", "fenixCalendar_monthRow", null);
        writer.writeAttribute("colspan", 6, null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        DateFormatSymbols dfs = sdf.getDateFormatSymbols();
        writer.write((dfs.getMonths())[date.get(Calendar.MONTH)]);

        writer.endElement("td");
        writer.endElement("tr");
    }

    private void encodeDaysOfWeek(ResponseWriter writer, Locale locale) throws IOException {
        writer.startElement("tr", this);
        writer.writeAttribute("class", "fenixCalendar_daysOfWeek", null);

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
        writer.startElement("td", this);
        writer.write(dayLabel);
        writer.endElement("td");
    }

    private void encodeWeeks(ResponseWriter writer, Calendar begin, Calendar end) throws IOException {

        String createLink = (String) this.getAttributes().get("createLink");
        String editLinkPage = (String) this.getAttributes().get("editLinkPage");
        List<CalendarLink> editLinkParameters = (List<CalendarLink>) this.getAttributes().get(
                "editLinkParameters");

        Calendar now = Calendar.getInstance();
        Calendar iter = Calendar.getInstance();
        iter.setTime(begin.getTime());

        for (int beginWeek = begin.get(Calendar.WEEK_OF_MONTH); beginWeek <= end
                .get(Calendar.WEEK_OF_MONTH); beginWeek++) {
            writer.startElement("tr", this);
            writer.writeAttribute("class", "fenixCalendar_weekRow", null);
            for (int beginDayOfWeek = Calendar.MONDAY; beginDayOfWeek <= Calendar.SATURDAY; beginDayOfWeek++) {

                if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    doMonthAwareRoll(iter);
                }

                if (iter.after(end)) {
                    writer.startElement("td", this);
                    writer.endElement("td");
                } else if (iter.get(Calendar.DAY_OF_WEEK) == beginDayOfWeek) {
                    writer.startElement("td", this);

                    List<CalendarLink> toDisplay = objectsToDisplayOnThisDay(iter, editLinkParameters);
                    if (toDisplay != null && !toDisplay.isEmpty()) {
                        writer.writeAttribute("class", "fenixCalendar_dayWithObjectOccurence", null);
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
                        writer.writeAttribute("class", "fenixCalendar_defaultDay", null);
                        encodeDay(writer, createLink, now, iter);
                    }

                    writer.endElement("td");

                    doMonthAwareRoll(iter);
                } else {
                    writer.startElement("td", this);
                    writer.endElement("td");
                }
            }
            writer.endElement("tr");
        }
    }

    private void encodeDay(ResponseWriter writer, String createLink, Calendar now, Calendar iter)
            throws IOException {
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

    private List<CalendarLink> objectsToDisplayOnThisDay(Calendar iter,
            List<CalendarLink> editLinkParameters) {
        List<CalendarLink> result = new ArrayList<CalendarLink>();

        for (CalendarLink calendarLink : editLinkParameters) {
            if (calendarLink.getObjectOccurrence().get(Calendar.DAY_OF_MONTH) == iter
                    .get(Calendar.DAY_OF_MONTH)
                    && calendarLink.getObjectOccurrence().get(Calendar.MONTH) == iter
                            .get(Calendar.MONTH)
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
