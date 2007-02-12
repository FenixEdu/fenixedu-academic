package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public abstract class ResearchActivity extends ResearchActivity_Base {
    
    public  ResearchActivity() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete(){
    	for (;this.hasAnyParticipations(); getParticipations().get(0).delete());
    	for(; this.hasAnyParty(); this.getParties().get(0).removeResearchActivities(this));
        this.removeRootDomainObject();
        super.deleteDomainObject();
    }
    
   
    public List<Party> getParties() {
    	List<Party> parties = new ArrayList<Party> ();
    	for(Participation participation : this.getParticipations()) {
    		parties.add(participation.getParty());
    	}
    	return parties;
    }
    
    public Integer getPartyCount() {
    	return this.getParties().size();
    }
    
    public boolean hasAnyParty() {
    	return this.hasAnyParticipations();
    	
    }
    
    public boolean hasMoreParticipationsFrom(Party party){
    	for(Participation participation : this.getParticipations()){
    		if(participation.getParty().equals(party))
    			return true;
    	}
    	return false;
    }
    
    public int getParticipationsCountFor(Party party) {
    	int count = 0;
    	for(Participation participation : this.getParticipations()){
    		if(participation.getParty().equals(party))
    			count++;
    	}
    	return count;
    }
    
    public boolean isEvent(){
    	return (this instanceof Event);
    }
    
    public boolean isScientificJournal(){
    	return (this instanceof ScientificJournal);
    }
    
    public boolean isCooperation(){
    	return (this instanceof Cooperation);
    }
    
    public abstract List<ResearchActivityParticipationRole> getAllowedRoles();
}
