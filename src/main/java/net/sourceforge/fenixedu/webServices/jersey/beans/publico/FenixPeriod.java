package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import org.joda.time.LocalDate;

public class FenixPeriod {

    String start;
    String end;

    public FenixPeriod() {
        this((String) null, (String) null);
    }

    public FenixPeriod(final String start, final String end) {
        this.start = start;
        this.end = end;
    }

    public FenixPeriod(final LocalDate start, final LocalDate end) {
        this.start = start == null ? null : start.toString("yyyy-MM-dd");
        this.end = end == null ? null : end.toString("yyyy-MM-dd");
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
