package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class EventParticipation extends EventParticipation_Base {
    
    public  EventParticipation(Party party, ResearchActivityParticipationRole role, Event event) {
	super();
	if(alreadyHasParticipation(party, role, event)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setParty(party);
	super.setRole(role);
	super.setEvent(event);
    }
    
    private boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, Event event) {
	for(EventParticipation participation : party.getAllEventParticipations()) {
	    if(participation.match(party,role,event)) {
		return true;
	    }
	}
	return false;	
    }
    
    public boolean match(Party party, ResearchActivityParticipationRole role, Event event) {
	return this.getParty().equals(party) && this.getRole().equals(role) && this.getEvent() .equals(event);
    }

    @Override
    public void setEvent(Event event) {
	if(alreadyHasParticipation(getParty(), getRole(), event)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setEvent(event);
    }

    @Override
    public void setParty(Party party) {
	if(alreadyHasParticipation(party, getRole(), getEvent())) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setParty(party);
    }

    @Override
    public void setRole(ResearchActivityParticipationRole role) {
	if(alreadyHasParticipation(getParty(), role, getEvent())) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setRole(role);
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }

    @Override
    public String getParticipationName() {
	return this.getEvent().getName();
    }

    @Override
    public boolean isLastParticipation() {
	return this.getEvent().getParticipationsFor(this.getParty()).size() == 1;
    }
}
