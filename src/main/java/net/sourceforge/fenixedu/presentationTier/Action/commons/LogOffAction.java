/*
 * LogOffAction.java
 * 
 * 
 * Created on 09 de Dezembro de 2002, 16:37
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.bennu.core.util.ConfigurationManager.CasConfig;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/logoff")
public class LogOffAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession(false);

        ActionForward result = null;

        final CasConfig casConfig = ConfigurationManager.getCasConfig();

        if (casConfig != null && casConfig.isCasEnabled()) {
            if (request.getParameter("logoutFromCAS") != null && request.getParameter("logoutFromCAS").equals("true")) {
                Authenticate.logout(session);
                result = new ActionForward("/commons/blankWithTitle.jsp");
            } else {
                result = getCasLogoutActionForward(casConfig);
                Authenticate.logout(session);
            }
        } else {
            Authenticate.logout(session);
            result = new ActionForward("/loginPage.jsp");
        }

        return result;
    }

    private ActionForward getCasLogoutActionForward(CasConfig casConfig) {
        ActionForward actionForward = new ActionForward();

        actionForward.setRedirect(true);
        actionForward.setPath(casConfig.getCasLogoutUrl());

        return actionForward;
    }

}
