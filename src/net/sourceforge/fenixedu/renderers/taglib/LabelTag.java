package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.renderers.components.Constants;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.util.MessageResources;

public class LabelTag extends BodyTagSupport {

    private String property;

    private String bundle;
    
    private String key;
    
    public LabelTag() {
        super();
    }

    public String getBundle() {
        return this.bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
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
        
        if (getProperty() != null) {
            MetaSlot slot = findMetaSlot(object, getProperty());
            
            if (slot != null) {
                if (getKey() != null) {
                    write(RenderUtils.getSlotLabel(object.getType(), getProperty(), getBundle(), getKey()));
                }
                else if (getBundle() != null) {
                    write(RenderUtils.getSlotLabel(object.getType(), getProperty(), getBundle(), slot.getLabelKey()));
                }
                else {
                    write(slot.getLabel());
                }
            }
            else {
                write(RenderUtils.getSlotLabel(object.getType(), getProperty(), getBundle(), getKey()));
            }
        }
        else if (getKey() != null) {
            MessageResources resources = RenderUtils.getMessageResources(getBundle());
            write(resources.getMessage(getKey()));
        }
        else {
            throw new JspException("must specify a property or a key");
        }
        
        return EVAL_PAGE;
    }

    protected void write(String string) throws JspException {
        try {
            pageContext.getOut().write(string);
        } catch (IOException e) {
            throw new JspException("could not write to page: ", e);
        }
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
