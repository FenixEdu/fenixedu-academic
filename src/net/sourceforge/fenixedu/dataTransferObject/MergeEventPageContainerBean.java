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
    private String url;
    

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
