package net.sourceforge.fenixedu.domain.cms.website;

public class Website extends Website_Base {

    public Website() {
        super();
    }

    public void delete() {
        removeTarget();
        
        super.delete();
    }
}
