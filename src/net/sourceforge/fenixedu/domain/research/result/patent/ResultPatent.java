package net.sourceforge.fenixedu.domain.research.result.patent;

public class ResultPatent extends ResultPatent_Base {
    
    public  ResultPatent() {
        super();
    }
    
    public enum ResultPatentType {
        
        LOCAL,
        NATIONAL,
        INTERNATIONAL;
              
        public String getName() {
            return name();
        }  
    }
    
    public enum ResultPatentStatus {
        
        /*type defined in CERIF*/
        APPLIED,
        PUBLISHED,
        GRANTED,
        MAINTAINED;
              
        public String getName() {
            return name();
        }  
    }
}
