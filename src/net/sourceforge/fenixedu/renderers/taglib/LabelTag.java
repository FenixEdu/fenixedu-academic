package net.sourceforge.fenixedu.renderers.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.renderers.components.Constants;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.PropertyUtils;

public class LabelTag extends BodyTagSupport {

    private String property;
    
    private String key;
    
    public LabelTag() {
        super();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public int doEndTag() throws JspException {
        MetaObject object = (MetaObject) pageContext.findAttribute(Constants.TEMPLATE_OBJECT_NAME);
        
        if (object == null) {
            throw new JspException("This tag can only be used inside a render template.");
        }
        
        try {
            MetaSlot slot = findMetaSlot(object, getProperty());
            if (slot != null) {
                pageContext.getOut().write(RenderUtils.getSlotLabel(object.getType(), getProperty(), getKey()));
            }
            else {
                Class type = PropertyUtils.getPropertyType(object.getObject(), getProperty());
                pageContext.getOut().write(RenderUtils.getSlotLabel(type, getProperty(), getKey()));
            }
        } catch (Exception e) {
            // print exception but keep going
            e.printStackTrace();
        }
        
        return EVAL_PAGE;
    }

    private MetaSlot findMetaSlot(MetaObject object, String property) {
        List<MetaSlot> slots = object.getSlots();
        
        for (MetaSlot slot : slots) {
            if (slot.getName().equals(property)) {
                return slot;
            }
        }
        
        return null;
    }

    @Override
    public void release() {
        super.release();
        
        this.property = null;
        this.key = null;
    }

    
}
