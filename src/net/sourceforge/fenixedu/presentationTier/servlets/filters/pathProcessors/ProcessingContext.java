package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessingContext {

    private boolean accepted;
    private boolean childAccepted;
    
    private ProcessingContext parent;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private String contextPath;
    
    private ProcessingContext() {
        this.accepted = false;
        this.childAccepted = false;
    }
    
    public ProcessingContext(ProcessingContext parent) {
        this();
        
        this.parent = parent;
    }
    
    public ProcessingContext(String contextPath, HttpServletRequest request, HttpServletResponse response) {
        this();
    
        this.contextPath = contextPath;
        this.request = request;
        this.response = response;
    }

    public ProcessingContext getParent() {
        return this.parent;
    }
    
    public boolean isAccepted() {
        return this.accepted;
    }

    public void accept() {
        this.accepted = true;
        
        if (getParent() != null) {
            getParent().childAccept();
        }
    }

    public void childAccept() {
        this.childAccepted = true;
    }

    public boolean isChildAccepted() {
        return this.childAccepted;
    }

    public String getContextPath() {
        if (this.contextPath == null) {
            if (getParent() == null) {
                return null;
            }
            else {
                return getParent().getContextPath();
            }
        }
        else {
            return this.contextPath;
        }
    }

    public HttpServletRequest getRequest() {
        if (this.request == null) {
            if (getParent() == null) {
                return null;
            }
            else {
                return getParent().getRequest();
            }
        }
        else {
            return this.request;
        }
    }

    public HttpServletResponse getResponse() {
        if (this.response == null) {
            if (getParent() == null) {
                return null;
            }
            else {
                return getParent().getResponse();
            }
        }
        else {
            return this.response;
        }
    }

}
