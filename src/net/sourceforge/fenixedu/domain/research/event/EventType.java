package net.sourceforge.fenixedu.domain.research.event;

public enum EventType {
	Seminar,
	Conference,
    Fair,
    CulturalEvent,
    Exhibition,
    PoliticalEvent,
    TradeFair,
    Workshop;
	
    public static String getDefaultType(){
        return Seminar.getName();
    }
    
    public String getName() {
        return name();
    } 
}
