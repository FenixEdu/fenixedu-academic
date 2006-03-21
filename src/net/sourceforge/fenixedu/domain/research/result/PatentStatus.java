package net.sourceforge.fenixedu.domain.research.result;

public enum PatentStatus {
    
	/*type defined in CERIF*/
	APPLIED,
	PUBLISHED,
	GRANTED,
    MAINTAINED;
          
    public String getName() {
        return name();
    }  
}
