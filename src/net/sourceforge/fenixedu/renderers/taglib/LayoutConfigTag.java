package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutConfigTag extends TagSupport {

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
		parent = (BaseRenderObjectTag) findAncestorWithClass(this, BaseRenderObjectTag.class);
		
		if (parent != null) {
			if (getName() != null) {
				parent.setLayout(getName());
			}
			
			return EVAL_BODY_INCLUDE;
		}
		else {
			return SKIP_BODY;
		}       
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
