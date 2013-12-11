package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import pt.ist.bennu.core.security.Authenticate;

public abstract class FenixLookupDispatchAction extends LookupDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return super.execute(mapping, actionForm, request, response);
    }

    protected static User getUserView(HttpServletRequest request) {
        return Authenticate.getUser();
    }

    /**
     * This method returns a Map (x,y)
     * 
     * x - is a message resource identifier y - is the name of the method which
     * will be implemented within the subclasses
     * 
     */
    @Override
    protected abstract Map getKeyMethodMap();

}
