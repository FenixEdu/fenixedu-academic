package net.sourceforge.fenixedu.presentationTier.Action.commons.password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *  
 */

public class GenerateNewPasswordDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        DynaActionForm newPasswordForm = (DynaActionForm) form;

        String username = (String) newPasswordForm.get("username");

        InfoPerson infoPerson = null;
        try {
            Object args[] = { username };
            infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPersonByUsername", args);
        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("A Person", e);
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Confirm");
    }

    public ActionForward generatePassword(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        Integer personID = new Integer(request.getParameter("personID"));

        String password = null;

        final Person person = (Person) RootDomainObject.getInstance().readPartyByOID(personID);
        if (person != null) {
        	ServiceUtils.executeService(null, "SetUserUID", new Object[] { person } );
        }

        // Change the Password
        try {
            Object args[] = { personID };
            password = (String) ServiceManagerServiceFactory.executeService(userView, "GenerateNewPassword", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("password", password);

        InfoPerson infoPerson = null;
        try {
            Object args[] = { request.getParameter("username") };
            infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPersonByUsername", args);
        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("A Person", e);
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Success");
    }
}