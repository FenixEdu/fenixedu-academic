package net.sourceforge.fenixedu.renderers.components.state;

import java.io.Serializable;

public class ViewDestination implements Serializable {

    private String path;

    private boolean redirect;

    private String module;
    
    public ViewDestination(String path, String module, boolean redirect) {
        this.path = path;
        this.module = module;
        this.redirect = redirect;
    }

    public String getPath() {
        return this.path;
    }

    public String getModule() {
        return this.module;
    }
    
    public boolean getRedirect() {
        return this.redirect;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }
}
