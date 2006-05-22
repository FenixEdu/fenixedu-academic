package net.sourceforge.fenixedu.domain.research.event;

public enum EventType {
	SEMINAR,
	CONFERENCE;
	
    public static String getDefaultType(){
        return SEMINAR.getName();
    }
    
    public String getName() {
        return name();
    } 
}
