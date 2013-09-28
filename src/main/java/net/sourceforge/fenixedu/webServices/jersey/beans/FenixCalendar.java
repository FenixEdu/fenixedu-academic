package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

public class FenixCalendar {

    public static class FenixCalendarEvent {
        String startDay;
        String endDay;
        String startTime;
        String endTime;
        String location;
        String title;
        String url;
        String note;
        Boolean isAllDay;

        public FenixCalendarEvent(String startDay, String endDay, String startTime, String endTime, String location,
                String title, String url, String note, Boolean isAllDay) {
            super();
            this.startDay = startDay;
            this.endDay = endDay;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
            this.title = title;
            this.url = url;
            this.note = note;
            this.isAllDay = isAllDay;
        }

        public String getStartDay() {
            return startDay;
        }

        public void setStartDay(String startDay) {
            this.startDay = startDay;
        }

        public String getEndDay() {
            return endDay;
        }

        public void setEndDay(String endDay) {
            this.endDay = endDay;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public Boolean getIsAllDay() {
            return isAllDay;
        }

        public void setIsAllDay(Boolean isAllDay) {
            this.isAllDay = isAllDay;
        }

    }

    private String year;
    private List<FenixCalendarEvent> events;

    public FenixCalendar(String year, List<FenixCalendarEvent> events) {
        super();
        this.year = year;
        this.events = events;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<FenixCalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FenixCalendarEvent> events) {
        this.events = events;
    }

}
