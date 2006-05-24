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
	
    public static EventType getDefaultType(){
        return Seminar;
    }
    
    public String getName() {
        return name();
    } 
}
