package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class EventEditionParticipantBean extends ParticipantBean implements Serializable {

    DomainReference<EventEdition> eventEdition;
    
    public EventEditionParticipantBean() {
	super();
	setEventEdition(null);
    }
    
    public void setEventEdition(EventEdition eventEdition) {
	this.eventEdition = new DomainReference<EventEdition>(eventEdition);
    }
    
    public EventEdition getEventEdition() {
	return this.eventEdition.getObject();
    }
    
    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventEditionRoles();
    }

    @Override
    public DomainObject getActivity() {
	return getEventEdition();
    }
}
