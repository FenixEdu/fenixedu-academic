package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class FenixInterval {

    String start;
    String end;

    public FenixInterval(final String start, final String end) {
        this.start = start;
        this.end = end;
    }

    public FenixInterval(final DateTime start, final DateTime end) {
        this.start = start == null ? null : start.toString("yyyy-MM-dd HH:mm:ss");
        this.end = end == null ? null : end.toString("yyyy-MM-dd HH:mm:ss");
    }

    public FenixInterval(final Interval interval) {
        this(interval.getStart(), interval.getEnd());
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
