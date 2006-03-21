package net.sourceforge.fenixedu.domain.research.result;

public enum PatentFormat {
    
    /*Types from publication format*/
	PAPER,
	MAGNETIC,
    DIGITAL,
    MOVIE,
    HYPERTEXT,
    OTHER,
    VARIOUS;
          
    public String getName() {
        return name();
    }  
}
