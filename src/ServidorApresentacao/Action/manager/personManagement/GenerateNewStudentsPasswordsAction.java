/*
 * Created on Sep 8, 2004
 *
 */
package ServidorApresentacao.Action.manager.personManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsAction extends FenixDispatchAction {

    public ActionForward prepareGeneratePasswords(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("prepare");
    }

    public ActionForward generatePasswords(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm passwordsForm = (DynaActionForm) form;
        String fromNumber = (String) passwordsForm.get("fromNumber");
        String toNumber = (String) passwordsForm.get("toNumber");
        List infoPersonList = null;

        try {
            Object args[] = { new Integer(fromNumber), new Integer(toNumber) };
            infoPersonList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "GenerateNewStudentsPasswords", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        request.setAttribute("infoPersonList", infoPersonList);

        return mapping.findForward("success");
    }

}