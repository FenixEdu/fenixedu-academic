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

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof ViewDestination)) {
            return false;
        }

        ViewDestination otherDestination = (ViewDestination) other;

        if (getPath() != null && ! getPath().equals(otherDestination.getPath())) {
            return false;
        }
        
        if (getModule() != null && ! getModule().equals(otherDestination.getModule())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        String path = getPath();
        String module = getModule();
        
        return (path != null ? path.hashCode() : 0) + (module != null ? module.hashCode() : 0); 
    }
    
}
