package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu._development.MetadataManager;
import dml.DomainClass;

public class FenixCreateObjectTag extends FenixEditObjectTag {

    @Override
    protected Object getTargetObject() throws JspException {
        String type;
        
        if (!isPostBack()) {
            type = getType();
        } else {
            type = getViewState().getMetaObject().getType().getName();
        }
        
        DomainClass domainClass = MetadataManager.getDomainModel().findClass(type);
        
        if (domainClass == null) {
            throw new JspException("Could not find domain class for type " + type);
        }
        
        return domainClass;
    }
}
