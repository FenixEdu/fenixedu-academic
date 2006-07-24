package net.sourceforge.fenixedu.dataTransferObject.research.event;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class EventParticipantionFullCreationBean implements Serializable {
  
    private EventParticipationRole role;
    private MultiLanguageString eventName;
    private EventType eventType;
    private String local;
    private YearMonthDay startDate;
    private YearMonthDay endDate;
    private Boolean fee;

    public EventParticipationRole getRole() {
        return role;
    }

    public void setRole(EventParticipationRole role) {
        this.role = role;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
    }

    public MultiLanguageString getEventName() {
        return eventName;
    }

    public void setEventName(MultiLanguageString eventName) {
        this.eventName = eventName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Boolean getFee() {
        return fee;
    }

    public void setFee(Boolean fee) {
        this.fee = fee;
    }

}
