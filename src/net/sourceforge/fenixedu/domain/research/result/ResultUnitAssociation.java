package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ResultUnitAssociation extends ResultUnitAssociation_Base {
    
    public  ResultUnitAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete(){
        final Result result = this.getResult();
        
        this.removeResult();
        result.sweep();
        
        this.removeUnit();
        
        this.removeRootDomainObject();
        deleteDomainObject();
    }

    public enum ResultUnitAssociationRole {
        Sponsor,
        Participant;
        
        public String getName() {
            return name();
        }
        
        public static ResultUnitAssociationRole getDefaultUnitRoleType(){
            return Sponsor;
        }
    }
}
