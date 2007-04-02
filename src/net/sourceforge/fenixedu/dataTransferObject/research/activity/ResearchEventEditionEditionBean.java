package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;

public class ResearchEventEditionEditionBean extends ResearchActivityEditionBean implements Serializable {

    DomainReference<EventEdition> edition;
    
    public ResearchEventEditionEditionBean() {
	super();
	this.edition = new DomainReference<EventEdition>(null);
    }
    
    public EventEdition getEventEdition() {
	return this.edition.getObject();
    }
    
    public void setEventEdition(EventEdition edition) {
	this.edition = new DomainReference<EventEdition>(edition);
    }
}
