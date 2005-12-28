package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu._development.MetadataManager;

public class FenixCreateObjectTag extends FenixEditObjectTag {

    @Override
    protected Object getTargetObject() throws JspException {
        String type;
        
        if (!isPostBack()) {
            type = getType();
        } else {
            type = getViewState().getMetaObject().getType().getName();
        }
        
        return MetadataManager.getDomainModel().findClass(type);
    }
}
