package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;

import org.joda.time.YearMonthDay;

public class MergeEventPageContainerBean extends MergeResearchActivityPageContainerBean {
    
    private String name;
    private EventType eventType;
    private ResearchActivityLocationType researchActivityLocationType;
    private String eventLocation;
    private YearMonthDay startDate;
    private YearMonthDay endDate;
    
    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResearchActivityLocationType getResearchActivityLocationType() {
        return researchActivityLocationType;
    }

    public void setResearchActivityLocationType(ResearchActivityLocationType researchActivityLocationType) {
        this.researchActivityLocationType = researchActivityLocationType;
    }

    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
    }

}
