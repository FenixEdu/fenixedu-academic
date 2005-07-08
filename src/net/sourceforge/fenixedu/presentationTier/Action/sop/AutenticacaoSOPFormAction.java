/*
 * AutenticacaoSOPFormAction.java
 *
 * Created on 15 de Novembro de 2002, 15:17
 */

package net.sourceforge.fenixedu.presentationTier.Action.sop;

/**
 * 
 * @author tfc130
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AutenticacaoSOPFormAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm autenticacaoForm = (DynaActionForm) form;

        Object argsAutenticacao[] = { autenticacaoForm.get("utilizador"),
                autenticacaoForm.get("password") };

        IUserView userView = null;
        try {
            userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsAutenticacao);
        } catch (Exception e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("invalidAuthentication", new ActionError("errors.invalidAuthentication"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }

        // Store the UserView into the session and return
        request.setAttribute(SessionConstants.U_VIEW, userView);
        request.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));
        return mapping.findForward("SOP");
    }
}