package net.sourceforge.fenixedu.domain.research.event;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class EventParticipation extends EventParticipation_Base {
    	
    public  EventParticipation() {
    	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete(){
        this.removeParty();
        this.removeEvent();
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public enum EventParticipationRole {
        Committee_Chair,
        Committee_Member,
        General_Chair,
        Invited_Speaker,
        Reviewer;
        
        public static EventParticipationRole getDefaultUnitRoleType(){
            return Invited_Speaker;
        }
        
        public static EventParticipationRole getDefaultPersonRoleType(){
            return Invited_Speaker;
        }
    }
}
