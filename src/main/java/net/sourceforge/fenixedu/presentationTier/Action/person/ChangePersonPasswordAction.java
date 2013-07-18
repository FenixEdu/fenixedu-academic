/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePassword;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException;

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

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm changePasswordForm = (DynaActionForm) form;
        IUserView userView = getUserView(request);
        String oldPassword = (String) changePasswordForm.get("oldPassword");
        String newPassword = (String) changePasswordForm.get("newPassword");

        // Check the old Password

        try {
            ChangePassword.run(userView, oldPassword, newPassword);
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

}