package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import pt.ist.fenixframework.DomainObject;

public class EventEditionParticipantBean extends ParticipantBean implements Serializable {

    EventEdition eventEdition;

    public EventEditionParticipantBean() {
        super();
        setEventEdition(null);
    }

    public void setEventEdition(EventEdition eventEdition) {
        this.eventEdition = eventEdition;
    }

    public EventEdition getEventEdition() {
        return this.eventEdition;
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
