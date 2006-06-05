package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.renderers.DateRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

public class DateTimeRenderer extends DateRenderer {

    private boolean showFormat;

    private DateTime dateTime;

    public DateTimeRenderer() {
        super();
        this.showFormat = false;
    }

    public boolean isShowFormat() {
        return showFormat;
    }

    /**
     * 
     * @property
     */
    public void setShowFormat(boolean showFormat) {
        this.showFormat = showFormat;
    }

    @Override
    public String getFormat() {
        if (isFormatSet()) {
            return super.getFormat();
        }
        String format = "dd/MM/yyyy HH:mm:ss";
        if (dateTime.get(DateTimeFieldType.hourOfDay()) == 0
                && dateTime.get(DateTimeFieldType.minuteOfHour()) == 0
                && dateTime.get(DateTimeFieldType.secondOfMinute()) == 0) {
            format = "dd/MM/yyyy";
        }

        return format.trim();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        dateTime = (DateTime) object;
        final Date date = dateTime != null ? convertDateTimeToCalendar(dateTime).getTime() : null;

        final Layout superLayout = super.getLayout(date, type);

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlText text = (HtmlText) superLayout.createComponent(date, type);

                String formatText = isShowFormat() ? " (" + getFormat() + ")" : "";
                text.setText(text.getText() + formatText);

                return text;
            }

        };
    }

    private Calendar convertDateTimeToCalendar(DateTime dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, dt.get(DateTimeFieldType.dayOfMonth()));
        calendar.set(Calendar.MONTH, dt.get(DateTimeFieldType.monthOfYear()) - 1);
        calendar.set(Calendar.YEAR, dt.get(DateTimeFieldType.year()));
        calendar.set(Calendar.HOUR_OF_DAY, dt.get(DateTimeFieldType.hourOfDay()));
        calendar.set(Calendar.MINUTE, dt.get(DateTimeFieldType.minuteOfHour()));
        calendar.set(Calendar.SECOND, dt.get(DateTimeFieldType.secondOfMinute()));
        return calendar;
    }
}
