package net.sourceforge.fenixedu.domain.research.result;

public enum FormatType{
    
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
