package net.sourceforge.fenixedu.renderers.components.state;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sourceforge.fenixedu.renderers.model.UserIdentityFactory;

public class EditRequest extends HttpServletRequestWrapper {

    private IViewState viewState;
    
    public EditRequest(HttpServletRequest request) {
        super(request);
    }

    public IViewState getViewState() throws IOException, ClassNotFoundException {
        if (this.viewState == null) {
            String encodedViewState = getParameter(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        
            this.viewState = ViewState.decodeFromBase64(encodedViewState);
            
            this.viewState.setRequest(this);
            this.viewState.setUser(UserIdentityFactory.create(this));
        }
        
        return this.viewState;
    }
}
