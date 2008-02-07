package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Portal extends Portal_Base {
    
    public Portal() {
        super();
    }
    
    public DomainObject getContext() {
        return null;
    }

    public static Portal getRootPortal() {
	return RootDomainObject.getInstance().getRootPortal();
    }
    
    public void setFirstParent(Container container) {
	container.addChild(this);
    }

    @Override
    public void appendReversePathPart(StringBuilder stringBuilder) {
	if (this != getRootPortal()) {
	    super.appendReversePathPart(stringBuilder);
	}
    }
    
    @Override
    protected Node createChildNode(Content childContent) {
        return new ExplicitOrderNode(this, childContent);
    }

    @Override
    protected void disconnect() {
	removePortalRootDomainObject();
	super.disconnect();
    }

}
