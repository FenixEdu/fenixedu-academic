/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangePersonPasswordAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm changePasswordForm = (DynaActionForm) form;
            IUserView userView = getUserView(request);
            String oldPassword = (String) changePasswordForm.get("oldPassword");
            String newPassword = (String) changePasswordForm.get("newPassword");

            // Check the old Password
            Object args[] = { userView, oldPassword, newPassword };

            try {
                ServiceUtils.executeService(userView, "ChangePassword", args);
            } catch (InvalidPasswordServiceException e) {
                throw new InvalidPasswordActionException(e);
            } catch (FenixServiceException e) {
            	ActionError error = new ActionError("error.person.impossible.change", e.getMessage());
            	ActionErrors actionErrors = new ActionErrors();
            	actionErrors.add("error.person.impossible.change", error);
            	saveErrors(request, actionErrors);
            	return mapping.getInputForward();
            }
            return mapping.findForward("Success");
        }
        throw new Exception();
    }

}