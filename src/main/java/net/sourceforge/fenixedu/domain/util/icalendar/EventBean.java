package net.sourceforge.fenixedu.domain.util.icalendar;

import java.util.Set;

import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

public class EventBean {
    private DateTime begin;
    private DateTime end;
    private boolean allDay;
    private String title;
    private Set<AllocatableSpace> rooms;
    private String url;
    private String note;

    public EventBean(String title, DateTime begin, DateTime end, boolean allDay, Set<AllocatableSpace> rooms, String url,
            String note) {
        this.allDay = allDay;
        this.begin = begin;
        this.end = end;
        this.note = note;
        this.rooms = rooms;
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
        return rooms == null ? "Fenix" : Joiner.on("; ").join(
                FluentIterable.from(rooms).transform(new Function<AllocatableSpace, String>() {

                    @Override
                    public String apply(AllocatableSpace input) {
                        return input.getIdentification();
                    }
                }).toSet());
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

    public Set<AllocatableSpace> getRooms() {
        return rooms;
    }

    public void setRoom(Set<AllocatableSpace> room) {
        this.rooms = room;
    }

    public String getOriginalTitle() {
        return getTitle();
    }

}
