/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.kerberosTest;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

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

            DynaActionForm changePasswordForm = (DynaActionForm) form;
            
            IUserView userView = new MockUserView((String) changePasswordForm.get("user"), new ArrayList(), null);
            String oldPassword = (String) changePasswordForm.get("oldPassword");
            String newPassword = (String) changePasswordForm.get("newPassword");

            ServiceUtils.executeService(userView, "SetUserUID", new Object[] { userView.getPerson() } );

            // Check the old Password
            Object args[] = { userView, oldPassword, newPassword };
            try {
                ServiceUtils.executeService(userView, "ChangePasswordKerberosTest", args);
            } catch (InvalidPasswordServiceException e) {
                throw new InvalidPasswordActionException(e);
            }
            return mapping.findForward("Success");

    }

}