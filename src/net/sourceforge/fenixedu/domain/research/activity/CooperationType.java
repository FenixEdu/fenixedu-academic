package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public enum CooperationType {

	ScientificOrganizationsAndNetworks, 
	BilateralCooperation,
	Commission;
	
    public static CooperationType getDefaultType() {
        return ScientificOrganizationsAndNetworks;
    }
    
    public static List<ResearchActivityParticipationRole> getParticipationRoles(CooperationType type) {
    	
    	return getCooperationRoles(type);
    }
    
    public List<ResearchActivityParticipationRole> getParticipationRoles() { 	
    	
    	return getCooperationRoles(this);   	
    }    
    
	private static List<ResearchActivityParticipationRole> getCooperationRoles(CooperationType type) {
		List<ResearchActivityParticipationRole> cooperationsRoles = new ArrayList<ResearchActivityParticipationRole>();
		
		if(CooperationType.ScientificOrganizationsAndNetworks.equals(type)) {
			cooperationsRoles = ResearchActivityParticipationRole.getAllScientificOrganizationsAndNetworksRoles();
    	}
    	else if(CooperationType.BilateralCooperation.equals(type)) {
    		cooperationsRoles = ResearchActivityParticipationRole.getAllBilateralCooperationRoles();
    	}
    	else if(CooperationType.Commission.equals(type)) {
    		cooperationsRoles = ResearchActivityParticipationRole.getAllCommissionRoles();
    	}
		return cooperationsRoles;
	}  
}