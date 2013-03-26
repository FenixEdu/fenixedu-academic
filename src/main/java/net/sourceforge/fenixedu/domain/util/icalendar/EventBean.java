package net.sourceforge.fenixedu.domain.util.icalendar;

import org.joda.time.DateTime;

public class EventBean {
    private DateTime begin;
    private DateTime end;
    private boolean allDay;
    private String title;
    private String location;
    private String url;
    private String note;

    public EventBean(String title, DateTime begin, DateTime end, boolean allDay, String location, String url, String note) {
        this.allDay = allDay;
        this.begin = begin;
        this.end = end;
        this.location = location;
        this.note = note;
        this.title = title;
        this.url = url;
    }

    public DateTime getBegin() {
        return begin;
    }

    public void setBegin(DateTime begin) {
        this.begin = begin;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
