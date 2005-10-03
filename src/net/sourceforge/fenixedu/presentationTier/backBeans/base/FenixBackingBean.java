package net.sourceforge.fenixedu.presentationTier.backBeans.base;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

public class FenixBackingBean {

    protected IUserView userView;

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

    protected String getRequestParameter(String parameterName) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                .get(parameterName);
    }

    public String getContextPath() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getContextPath();
    }

}
