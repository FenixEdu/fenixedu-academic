package net.sourceforge.fenixedu.domain.research.activity;


public enum ResearchActivityLocationType {
	LOCAL,
    	NATIONAL,
	INTERNATIONAL;
	
    public static ResearchActivityLocationType getDefaultType(){
        return NATIONAL;
    }
    
    public String getName() {
        return name();
    }
}
