package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.DomainObject;

public class DomainObjectLinkRenderer extends ObjectLinkRenderer {

    public DomainObjectLinkRenderer() {
        super();
    }

    @Override
    protected DomainObject getDomainObject(Object object) {
        return (DomainObject) object;
    }
    
}
