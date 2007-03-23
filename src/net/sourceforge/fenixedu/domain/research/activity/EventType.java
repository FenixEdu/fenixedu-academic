package net.sourceforge.fenixedu.domain.research.activity;


public enum EventType {
	Seminar,
	Conference,
    Symposium,
    Workshop;
	
    public static EventType getDefaultType(){
        return Conference;
    }
    
    public String getName() {
        return name();
    }
}
