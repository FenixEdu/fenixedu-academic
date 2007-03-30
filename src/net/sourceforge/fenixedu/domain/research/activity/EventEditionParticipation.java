package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class EventEditionParticipation extends EventEditionParticipation_Base {
    
   
    public EventEditionParticipation(Party party, ResearchActivityParticipationRole role, EventEdition eventEdition) {
	super();
	if(alreadyHasParticipation(party, role, eventEdition)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setParty(party);
	super.setRole(role);
	super.setEventEdition(eventEdition);
    }
    
    private boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, EventEdition eventEdition) {
	for(EventEditionParticipation participation : party.getAllEventEditionParticipations()) {
	    if(participation.match(party,role,eventEdition)) {
		return true;
	    }
	}
	return false;	
    }
    
    public boolean match(Party party, ResearchActivityParticipationRole role, EventEdition eventEdition) {
	return this.getParty().equals(party) && this.getRole().equals(role) && this.getEventEdition().equals(eventEdition);
    }

    @Override
    public void setEventEdition(EventEdition eventEdition) {
	if(alreadyHasParticipation(getParty(), getRole(), eventEdition)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setEventEdition(eventEdition);
    }

    @Override
    public void setParty(Party party) {
	if(alreadyHasParticipation(party, getRole(), getEventEdition())) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setParty(party);
    }

    @Override
    public void setRole(ResearchActivityParticipationRole role) {
	if(alreadyHasParticipation(getParty(), role, getEventEdition())) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setRole(role);
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventEditionRoles();
    }

    @Override
    public String getParticipationName() {
	return this.getEventEdition().getFullName();
    }

    @Override
    public boolean isLastParticipation() {
	return this.getEventEdition().getParticipationsFor(this.getParty()).size() == 1;
    }
    
    @Override
    public void delete() {
	removeEventEdition();
	super.delete();
    }
        
}
