package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.taglib.TagUtils;

public class HiddenSlotTag extends TagSupport {

    private String slot;
	private String name;
    private String property; 
    private String scope; 
    private String value; 
    private String converter; 
    private String multiple;
    
	private EditObjectTag parent;
		
	public HiddenSlotTag() {
	}
    
	@Override
    public void release() {
        super.release();
        
        this.slot = null;
        this.name = null;
        this.property = null;
        this.scope = null;
        this.value = null;
        this.converter = null;
        this.parent = null;
        this.multiple = null;
    }

	public String getConverter() {
        return this.converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSlot() {
        return this.slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMultiple() {
        return this.multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public EditObjectTag getContainerParent() {
        return this.parent;
    }

    public void setContainerParent(EditObjectTag parent) {
        this.parent = parent;
    }

    public int doStartTag() throws JspException {
		setContainerParent((EditObjectTag) findAncestorWithClass(this, EditObjectTag.class));
		
		if (getContainerParent() == null) {
            throw new JspException("could not find parent input tag");
        }

        Object value;
        if (getName() != null) {
            value = findObject();
        }
        else {
            value = getValue();
        }
        
        addHiddenSlot(getSlot(), value, getConverter());
        
        return SKIP_BODY;
	}

    protected Object findObject() throws JspException {
        return TagUtils.getInstance().lookup(pageContext, getName(), getProperty(), getScope());
    }
	
    protected void addHiddenSlot(String slot, Object value, String converterName) throws JspException {
        if (slot == null) {
            slot = getContainerParent().getSlot();
        }
        
        if (slot == null) {
            throw new RuntimeException("slot must be defined or directly or in the parent edit tag");
        }
        
        Class<Converter> converter = null;
        
        if (converterName != null) {
            try {
                converter = (Class<Converter>) Class.forName(converterName);
            } catch (ClassNotFoundException e) {
                throw new JspException("the specified converter could not be found: " + converterName);
            }
        }
        
        boolean multiple = isMultiple();
        getContainerParent().addHiddenSlot(slot, multiple, ConvertUtils.convert(value), converter);
    }

    protected boolean isMultiple() {
        return Boolean.parseBoolean(getMultiple());
    }

    public int doEndTag() throws JspException {
        release(); // force release
        
        return EVAL_PAGE;
    }
}
