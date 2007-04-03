package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchEventEditionCreationBean extends ResearchEventCreationBean implements Serializable {

    private DomainReference<EventEdition> edition;
    private ResearchActivityParticipationRole editionRole;
    private String eventEditionName;
    private YearMonthDay endDate;
    private YearMonthDay startDate;
    private String organization;
    private String eventLocation;
    private String editionUrl;
    
    public String getEditionUrl() {
        return editionUrl;
    }

    public void setEditionUrl(String url) {
        this.editionUrl = url;
    }

    public String getEventEditionName() {
        return eventEditionName;
    }

    public void setEventEditionName(String eventEditionName) {
        this.eventEditionName = eventEditionName;
    }

    public ResearchActivityParticipationRole getEditionRole() {
        return editionRole;
    }

    public void setEditionRole(ResearchActivityParticipationRole role) {
        this.editionRole = role;
    }

    public ResearchEventEditionCreationBean() {
	super();
	this.edition = new DomainReference<EventEdition>(null);
    }
    
    public EventEdition getEventEdition() {
	return this.edition.getObject();
    }
    
    public void setEventEdition(EventEdition edition) {
	this.edition = new DomainReference<EventEdition>(edition);
    }

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
    
}
