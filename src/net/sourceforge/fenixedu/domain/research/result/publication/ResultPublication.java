package net.sourceforge.fenixedu.domain.research.result.publication;


public class ResultPublication extends ResultPublication_Base {
    
    public ResultPublication() {
        super();
    }
    
    public void delete()
    {
        removePublisher();
        removeOrganization();
        super.delete();
    }
    
    public String getResultPublicationTypeString()
    {
        String className = this.getClass().getName();
        Integer lastPoint = className.lastIndexOf(".");
        String type = className.substring(lastPoint+1);
        
        if (this instanceof BookPart) {
            //add bookPart type
            type = type + "." +((BookPart)this).getBookPartType().toString();
        }
        
        return type;
    }
    
    public Boolean haveResultPublicationRole()
    {
        if((this instanceof Book) || (this instanceof BookPart))
            return true;
        return false;
    }

    public enum ScopeType {
        LOCAL,
        NATIONAL,
        INTERNATIONAL;
    }

}
