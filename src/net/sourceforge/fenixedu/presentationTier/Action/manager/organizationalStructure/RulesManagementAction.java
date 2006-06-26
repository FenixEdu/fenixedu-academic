/*
 * Created on Mar 2, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RulesManagementAction extends FenixDispatchAction {

    public ActionForward listRules(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        readConnectionRules(request);
        return mapping.findForward("listRules");
    }
    
    public ActionForward prepareInsertNewRule(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {              
        
        return mapping.findForward("insertNewRule");
    }
    
    public ActionForward insertNewRule(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        readConnectionRules(request);     
        return mapping.findForward("listRules");
    }

    public ActionForward deleteRule(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String connectionRuleId = (String) request.getParameter("oid");
        Object[] args = { Integer.valueOf(connectionRuleId) };

        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteConnectionRule", args);
        } catch (FenixServiceException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage()));
            saveMessages(request, actionMessages);
        }

        readConnectionRules(request);
        return mapping.findForward("listRules");
    }
    
    private void readConnectionRules(HttpServletRequest request) {
        Collection<ConnectionRule> connectionRules = rootDomainObject.getConnectionRulesSet();
        request.setAttribute("connectionRules", connectionRules);
    }
}
