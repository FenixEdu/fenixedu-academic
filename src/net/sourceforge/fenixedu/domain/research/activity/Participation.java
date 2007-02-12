package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;


public class Participation extends Participation_Base {
    	
    private Participation() {
    	
    }
      
    
    public Participation(Party party, ResearchActivityParticipationRole role, ResearchActivity researchActivity) {
    	if(alreadyHasThatParticipation(party, role, researchActivity)) {
    		throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
    	}
    	super.setParty(party);
    	super.setRole(role);
    	super.setResearchActivity(researchActivity);
    	setRootDomainObject(RootDomainObject.getInstance());
    }


	private boolean alreadyHasThatParticipation(Party party, ResearchActivityParticipationRole role, ResearchActivity researchActivity) {
		for(Participation participation : party.getParticipations()) {
    		if(participation.getResearchActivity().equals(researchActivity) && participation.getRole().equals(role)) {
    			return true;
    		}
    	}
		return false;
	}
    
    @Override
    public void setParty(Party party) {
    	if(alreadyHasThatParticipation(party, this.getRole(), this.getResearchActivity())) {
    		throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");	
    	}
    	else {
    		super.setParty(party);
    	}
    }
    
    @Override
    public void setRole(ResearchActivityParticipationRole role) {
    	if(alreadyHasThatParticipation(this.getParty(), role,this.getResearchActivity())) {
    		throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");	
    	}
    	else {
    		super.setRole(role);
    	}
    }
    
    @Override
    public void setResearchActivity(ResearchActivity activity) {
    	if(alreadyHasThatParticipation(this.getParty(), this.getRole(),activity)) {
    		throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");	
    	}
    	else {
    		super.setResearchActivity(activity);
    	}
    }
    
    public void delete() {
    	super.setParty(null);
    	super.setResearchActivity(null);
    	super.setRole(null);
    	super.deleteDomainObject();
    }
    
    public boolean isEventParticipation() {
    	return (getResearchActivity() instanceof Event);
    }
    
    public boolean isScientificJournaltParticipation() {
    	return (getResearchActivity() instanceof ScientificJournal);
    }
    
    public boolean isCooperationParticipation() {
    	return (getResearchActivity() instanceof Cooperation);
    }
    
    public enum ResearchActivityParticipationRole {
    	Editor_in_Chief,
    	Associate_Editor,
    	Committee_Chair,
        General_Chair,
        Committee_Member,
        Invited_Speaker,
        Reviewer,
    	
    	//ScientificOrganizationsAndNetworks
    	President,
    	Vice_President,
    	Secretary ,

    	Advisory_Board,
    	Local_Chair,
    	National_Representative,
    	
    	//BilateralCooperation
    	Visitor,
    	Research_Advisorship,
    	Joint_Research_Work,
    	Invited_Expert;
        
        public static ResearchActivityParticipationRole getEventDefaultRole(){
            return Invited_Speaker;
        }
        
        public static ResearchActivityParticipationRole getScientificJournalDefaultRole(){
            return Reviewer;
        }
        
        public static ResearchActivityParticipationRole getDefaultScientificOrganizationsAndNetworksCooperationRole(){
			
            return National_Representative;
        } 
        
        public static ResearchActivityParticipationRole getDefaultBilateralCooperationRole(){
            return Visitor;
        }
        
        public static List<ResearchActivityParticipationRole> getAllEventParticipationRoles(){
        	List<ResearchActivityParticipationRole> eventRoles = new ArrayList<ResearchActivityParticipationRole>();
        	
        	eventRoles.add(General_Chair);
        	eventRoles.add(Committee_Member);
        	eventRoles.add(Committee_Chair);
        	eventRoles.add(Invited_Speaker);
        	eventRoles.add(Reviewer);
        	
        	return eventRoles;
        }
        
        public static List<ResearchActivityParticipationRole> getAllScientificJournalParticipationRoles(){
        	List<ResearchActivityParticipationRole> journalRoles = new ArrayList<ResearchActivityParticipationRole>();
        	
        	journalRoles.add(Editor_in_Chief);
        	journalRoles.add(Committee_Member);
        	journalRoles.add(Associate_Editor);
        	journalRoles.add(Reviewer);
        	
        	return journalRoles;
        }
        
        public static List<ResearchActivityParticipationRole> getAllScientificOrganizationsAndNetworksRoles() {
        	List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();
        	
        	cooperationRoles.add(President);
        	cooperationRoles.add(Vice_President);
        	cooperationRoles.add(Secretary);
        	cooperationRoles.add(Committee_Chair);
        	cooperationRoles.add(Committee_Member);
        	cooperationRoles.add(Advisory_Board);
        	cooperationRoles.add(Local_Chair);
        	cooperationRoles.add(National_Representative);
        	
        	return cooperationRoles;
        }
        
        public static List<ResearchActivityParticipationRole> getAllBilateralCooperationRoles() {
        	List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();
        	
        	cooperationRoles.add(Visitor);
        	cooperationRoles.add(Research_Advisorship);
        	cooperationRoles.add(Joint_Research_Work);
        	cooperationRoles.add(Invited_Expert);
        	
        	return cooperationRoles;
        }
        
        public static List<ResearchActivityParticipationRole> getAllCommissionRoles() {
        	List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();
        	
        	cooperationRoles.add(Committee_Member);
        	
        	return cooperationRoles;
        }
        
    }
}
