package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class CooperationParticipation extends CooperationParticipation_Base {
    
    public  CooperationParticipation(Party party, ResearchActivityParticipationRole role, Cooperation cooperation) {
        super();
        if(alreadyHasParticipation(party, role, cooperation)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
        super.setParty(party);
        super.setRole(role);
        super.setCooperation(cooperation);
    }


    protected boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, Cooperation cooperation) {
	for(CooperationParticipation participation : party.getAllCooperationParticipations()) {
	    if(participation.match(party,role,cooperation)) {
		return true;
	    }
	}
	return false;
    }

    public boolean match(Party party, ResearchActivityParticipationRole role, Cooperation cooperation) {
	return this.getParty().equals(party) && this.getRole().equals(role) && this.getCooperation().equals(cooperation); 
    }


    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllBilateralCooperationRoles();
    }

    @Override
    public String getParticipationName() {
	return this.getCooperation().getName();
    }

    @Override
    public boolean isLastParticipation() {
	return this.getCooperation().getParticipationsFor(this.getParty()).size() == 1;
    }
    
    @Override 
    public void delete() {
	removeCooperation();
	super.delete();
    }
    
}
