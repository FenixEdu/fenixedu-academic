package net.sourceforge.fenixedu.renderers.components.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sourceforge.fenixedu.renderers.model.UserIdentity;
import net.sourceforge.fenixedu.renderers.model.UserIdentityFactory;

public class EditRequest extends HttpServletRequestWrapper {

    private List<IViewState> viewStates;
    
    public EditRequest(HttpServletRequest request) {
        super(request);
    }

    public List<IViewState> getAllViewStates() throws IOException, ClassNotFoundException {
        if (this.viewStates == null) {
            String[] encodedViewStates = getParameterValues(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
            if (encodedViewStates != null) {
                this.viewStates = new ArrayList<IViewState>();

                for (int i = 0; i < encodedViewStates.length; i++) {
                    String encodedSingleViewState = encodedViewStates[i];
                
                    IViewState viewState = ViewState.decodeFromBase64(encodedSingleViewState);
                    this.viewStates.add(viewState);
                }
            }
            else {
                this.viewStates = ViewState.decodeListFromBase64(getParameter(LifeCycleConstants.VIEWSTATE_LIST_PARAM_NAME));
            }
        }

        UserIdentity userIdentity = UserIdentityFactory.create(this);
        for (IViewState viewState : this.viewStates) {
            viewState.setRequest(this);
            
            checkUserIdentity(viewState, userIdentity);
        }
        
        return this.viewStates;
    }

    private void checkUserIdentity(IViewState viewState, UserIdentity userIdentity) {
        if (! viewState.getUser().equals(userIdentity)) {
            throw new RuntimeException("viewstate.user.changed");
        }
    }
    
}
