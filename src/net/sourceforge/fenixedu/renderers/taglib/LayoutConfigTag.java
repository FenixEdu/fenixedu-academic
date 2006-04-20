package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutConfigTag extends TagSupport implements PropertyContainerTag {

	private String name = null;
	private BaseRenderObjectTag parent = null;
		
	public LayoutConfigTag() {
	}
    
	@Override
    public void release() {
        super.release();
        
        this.name = null;
        this.parent = null;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doStartTag() throws JspException {
		this.parent = (BaseRenderObjectTag) findAncestorWithClass(this, BaseRenderObjectTag.class);
		
        if (this.parent == null) {
            throw new RuntimeException("layout tag can only be used inside a renderer tag");
        }

		if (getName() != null) {
			this.parent.setLayout(getName());
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
    public int doEndTag() throws JspException {
        release(); // force release
        
        return EVAL_PAGE;
    }
    
	public void addProperty(String name, String value) {
		if (parent != null) {
			parent.addRenderProperty(name, value);
		}
	}
}
