package net.sourceforge.fenixedu.domain.research.activity;


public enum EventType {
	Seminar,
	Conference,
    Symposium,
    Workshop;
	
    public static EventType getDefaultType(){
        return Seminar;
    }
    
    public String getName() {
        return name();
    }
    
//    public List<ResearchActivityParticipationRole> getParticipationRoles() { 	
//    	
//    	return getEventRoles(this);   	
//    }   
//    
//    public static List<ResearchActivityParticipationRole> getParticipationRoles(EventType type) { 	
//    	
//    	return getEventRoles(type);   	
//    }  
//    
//    private static List<ResearchActivityParticipationRole> getEventRoles(EventType type) {
//		List<ResearchActivityParticipationRole> eventRoles = new ArrayList<ResearchActivityParticipationRole>();
//		
//		if(EventType.Conference.equals(type)) {
//			eventRoles = ResearchActivityParticipationRole.getAllConferenceParticipationRoles();
//    	}
//    	else if(EventType.Seminar.equals(type)) {
//    		eventRoles = ResearchActivityParticipationRole.getAllSeminarParticipationRoles();
//    	}
//    	else if(EventType.Symposium.equals(type)) {
//    		eventRoles = ResearchActivityParticipationRole.getAllSymposiumParticipationRoles();
//    	}
//    	else if(EventType.Workshop.equals(type)) {
//    		eventRoles = ResearchActivityParticipationRole.getAllWorkshopParticipationRoles();
//    	}
//		
//		return eventRoles;
//	}
}
