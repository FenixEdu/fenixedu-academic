package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.components.Constants;
import net.sourceforge.fenixedu.renderers.model.MetaObject;

public class TemplateEditObjectTag extends EditObjectTag {

    public TemplateEditObjectTag() {
        super();
    }
    
    public String getName() {
        if (super.getName() == null) {
            return Constants.TEMPLATE_OBJECT_NAME;
        }
        else {
            return super.getName();
        }
    }
    
    @Override
    protected Object getTargetObjectByName() throws JspException {
        MetaObject metaObject = (MetaObject) super.getTargetObjectByName();
        
        return metaObject.getObject();
    }

    public String getScope() {
        if (super.getName() == null && super.getScope() == null) {
            return "request";
        }
        else {
            return super.getScope();
        }
    }
}
