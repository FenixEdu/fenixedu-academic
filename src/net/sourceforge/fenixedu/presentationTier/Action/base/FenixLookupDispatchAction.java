package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.ExcepcaoSessaoInexistente;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import pt.ist.fenixWebFramework.security.UserView;

public abstract class FenixLookupDispatchAction extends LookupDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return super.execute(mapping, actionForm, request, response);
    }

    protected HttpSession getSession(HttpServletRequest request) throws ExcepcaoSessaoInexistente {
	HttpSession result = request.getSession(false);
	if (result == null)
	    throw new ExcepcaoSessaoInexistente();

	return result;
    }

    protected static IUserView getUserView(HttpServletRequest request) {
	return UserView.getUser();
    }

    /**
     * This method returns a Map (x,y)
     * 
     * x - is a message resource identifier y - is the name of the method which
     * will be implemented within the subclasses
     * 
     */
    protected abstract Map getKeyMethodMap();

}
