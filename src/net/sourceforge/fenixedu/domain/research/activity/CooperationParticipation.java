package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CooperationParticipation extends CooperationParticipation_Base {
    
    public  CooperationParticipation(Party party, ResearchActivityParticipationRole role, Cooperation cooperation, MultiLanguageString roleMessage) {
        super();
        if(alreadyHasParticipation(party, role, cooperation)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
        super.setParty(party);
        super.setRole(role);
        super.setCooperation(cooperation);
        super.setRoleMessage(roleMessage);
    }


    protected boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, Cooperation cooperation) {
	for(CooperationParticipation participation : party.getCooperationParticipations()) {
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
    	switch(this.getCooperation().getCooperationType()) {
    	case ScientificOrganizationsAndNetworks:
    		return ResearchActivityParticipationRole.getAllScientificOrganizationsAndNetworksRoles();
    	case BilateralCooperation:
    		return ResearchActivityParticipationRole.getAllBilateralCooperationRoles();
    	case Commission:
    		return ResearchActivityParticipationRole.getAllCommissionRoles();
    	default:
    		return ResearchActivityParticipationRole.getAllBilateralCooperationRoles();
    	}
    	
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

    @Override
    public Integer getCivilYear() {
	return this.getCooperation().getStartDate().getYear();
    }

    @Override
    public boolean scopeMatches(ScopeType type) {
	return false;
    }

    
}
