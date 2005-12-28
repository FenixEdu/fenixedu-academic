package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutPropertyTag extends TagSupport {

	private String name = null;
	
	private String value = null;
	
	public LayoutPropertyTag() {
	}
    
    @Override
    public void release() {
        super.release();
        
        this.name = null;
        this.value = null;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		LayoutConfigTag parent = (LayoutConfigTag) findAncestorWithClass(this, LayoutConfigTag.class);
		
		if (parent != null) {
			parent.addProperty(getName(), getValue());
		}
		
		return SKIP_BODY;
	}
}
