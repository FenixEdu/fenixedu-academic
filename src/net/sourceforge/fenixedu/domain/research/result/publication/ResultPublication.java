package net.sourceforge.fenixedu.domain.research.result.publication;


public class ResultPublication extends ResultPublication_Base {
    
    public  ResultPublication() {
        super();
    }
    
    public ResultPublicationType getResultPublicationType()
    {
        String className = this.getClass().getName();
        ResultPublicationType resultPublicationType = null;
        Integer lastPoint = className.lastIndexOf(".");
        String type = className.substring(lastPoint+1);
        resultPublicationType = ResultPublicationType.valueOf(type);
        
        return resultPublicationType;
    }
    
    public Boolean haveResultPublicationRole()
    {
        if((this instanceof Book) || (this instanceof BookPart))
            return true;
        return false;
    }
    
    public enum ResultPublicationType {
        /*Types based on BibTex*/
        Book,
        BookPart,
        Article,
        Inproceedings,
        Proceedings,
        Thesis,
        Manual,
        TechnicalReport,
        Booklet,
        Misc,
        Unpublished;
              
        public static ResultPublicationType getDefaultResultPublicationType() {
            return Book;
        }

    }
    
    public enum ScopeType {
        LOCAL,
        NATIONAL,
        INTERNATIONAL;
    }

}
