package net.sourceforge.fenixedu.domain.research.result.publication;

public enum PublicationType {
    
	BOOK,
	ARTICLE,
    BOOK_PART,
    THESIS,
    CONFERENCE,
    TECHNICAL_REPORT,
    OTHER_PUBLICATION,
    UNSTRUCTURED;
          
    public String getName() {
        return name();
    }  
}
