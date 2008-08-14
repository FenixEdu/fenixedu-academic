package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

import org.joda.time.YearMonthDay;

public class MergeEventEditionPageContainerBean extends MergeResearchActivityPageContainerBean {
    private String edition;
    private String eventLocation;
    private String url;
    private String organization;
    private YearMonthDay startDate;
    private YearMonthDay endDate;

    private DomainReference<ResearchEvent> event;

    public MergeEventEditionPageContainerBean(ResearchEvent event) {
	this.event = new DomainReference<ResearchEvent>(event);
    }

    public String getEdition() {
	return edition;
    }

    public void setEdition(String edition) {
	this.edition = edition;
    }

    public String getEventLocation() {
	return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
	this.eventLocation = eventLocation;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getOrganization() {
	return organization;
    }

    public void setOrganization(String organization) {
	this.organization = organization;
    }

    public YearMonthDay getStartDate() {
	return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
	this.startDate = startDate;
    }

    public YearMonthDay getEndDate() {
	return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
	this.endDate = endDate;
    }

    public ResearchEvent getEvent() {
	return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(ResearchEvent event) {
	this.event = (event != null) ? new DomainReference<ResearchEvent>(event) : null;
    }
}
