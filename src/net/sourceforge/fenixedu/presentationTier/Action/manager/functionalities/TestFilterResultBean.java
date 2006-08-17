package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class TestFilterResultBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Functionality> functionality;
    private boolean accessible;
    
    public TestFilterResultBean(Functionality functionality, boolean accessible) {
        super();
    
        this.functionality = new DomainReference<Functionality>(functionality);
        this.accessible = accessible;
    }

    public boolean isAccessible() {
        return this.accessible;
    }

    public Functionality getFunctionality() {
        return this.functionality.getObject();
    }
    
}
