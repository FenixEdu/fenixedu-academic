package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

public class EditLinkTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(EditLinkTag.class);
    
    private String name;
    
    private String path;

    private String module;

    private String redirect;

    public EditLinkTag() {
        super();
    }

    @Override
    public void release() {
        super.release();

        this.name = null;
        this.path = null;
        this.module = null;
        this.redirect = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @Override
    public int doStartTag() throws JspException {
        BaseRenderObjectTag tag = (BaseRenderObjectTag) findAncestorWithClass(this, BaseRenderObjectTag.class);

        if (tag == null) {
            logger.warn("destination " + getName() + " specified but not parent tag supports destinations");
        }
        else {
            setDestination(tag, getPath(), getModule(), redirectToBoolean(getRedirect()));
        }

        return SKIP_BODY;
    }

    protected void setDestination(BaseRenderObjectTag tag, String path, String module,
            boolean redirect) {
        tag.addDestination(name, path, module, redirect);
    }

    protected boolean redirectToBoolean(String redirect) {
        if (redirect != null && (redirect.equalsIgnoreCase("true") || redirect.equalsIgnoreCase("yes"))) {
            return true;
        }

        return false;
    }
}
