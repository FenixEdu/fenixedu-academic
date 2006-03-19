package net.sourceforge.fenixedu.domain.cms;

public class FunctionalityLink extends FunctionalityLink_Base {
    
    public FunctionalityLink() {
        super();
    }

    @Override
    public void delete() {
        getTypes().clear();
        
        super.delete();
    }
}
