/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.kerberosTest;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SetUserUID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePasswordKerberos;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
@Mapping(module = "manager", path = "/changePasswordForm", input = "pass-test-kerberos", attribute = "changePasswordForm",
        formBean = "changePasswordForm", scope = "request")
@Forwards(value = { @Forward(name = "Success", path = "/manager/changePasswordSucess_bd.jsp") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException.class,
        key = "resources.Action.exceptions.InvalidPasswordActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ChangePersonPasswordAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm changePasswordForm = (DynaActionForm) form;

        User userView = new MockUserView((String) changePasswordForm.get("user"), new ArrayList(), null);
        String oldPassword = (String) changePasswordForm.get("oldPassword");
        String newPassword = (String) changePasswordForm.get("newPassword");

        SetUserUID.run(userView.getPerson());

        // Check the old Password

        try {
            ChangePasswordKerberos.run(userView, oldPassword, newPassword);
        } catch (InvalidPasswordServiceException e) {
            throw new InvalidPasswordActionException(e);
        }
        return mapping.findForward("Success");

    }

}