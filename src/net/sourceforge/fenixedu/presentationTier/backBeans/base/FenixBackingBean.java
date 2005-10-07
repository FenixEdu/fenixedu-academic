package net.sourceforge.fenixedu.presentationTier.backBeans.base;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.jsf.components.UIViewState;

public class FenixBackingBean {

    protected IUserView userView;
    protected String errorMessage;
    private UIViewState viewState;

    public FenixBackingBean() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false);
        userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
    }

    public IUserView getUserView() {
        return userView;
    }

    public void setUserView(IUserView userView) {
        this.userView = userView;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    protected String getRequestParameter(String parameterName) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                .get(parameterName);
    }

    public String getContextPath() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getContextPath();
    }
    
    public UIViewState getViewState() {
        if (this.viewState == null) {
            this.viewState = new UIViewState();
        }

        return viewState;
    }

    public void setViewState(UIViewState viewState) {
        this.viewState = viewState;
    }

    private void addMessage(Severity facesMessage ,String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(facesMessage, message, null));
    }
    
    protected void addErrorMessage(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, message);
    }
    
    protected void addInfoMessage(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, message);
    }
    
    protected void addWarnMessage(String message) {
        addMessage(FacesMessage.SEVERITY_WARN, message);
    }
}
