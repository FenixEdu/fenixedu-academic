package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * This renderer as a behaviour similar to {@link ObjectLinkRenderer}.
 * The difference is that it can only be used to present domain objects and 
 * generates a link to the details of that object.
 * 
 * @author cfgi
 */
public class DomainObjectLinkRenderer extends ObjectLinkRenderer {

    public DomainObjectLinkRenderer() {
        super();
    }

    @Override
    protected DomainObject getDomainObject(Object object) {
        return (DomainObject) object;
    }
    
}
