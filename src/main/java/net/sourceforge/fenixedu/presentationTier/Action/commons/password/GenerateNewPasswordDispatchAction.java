package net.sourceforge.fenixedu.presentationTier.Action.commons.password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SetUserUID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.GenerateNewPasswordService;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByUsername;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */

public class GenerateNewPasswordDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        DynaActionForm newPasswordForm = (DynaActionForm) form;

        String username = (String) newPasswordForm.get("username");

        InfoPerson infoPerson = null;
        try {

            infoPerson = ReadPersonByUsername.run(username);
        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("A Person", e);
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Confirm");
    }

    public ActionForward generatePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        String personID = request.getParameter("personID");

        String password = null;

        final Person person = (Person) AbstractDomainObject.fromExternalId(personID);
        if (person != null) {
            SetUserUID.run(person);
        }

        // Change the Password
        try {

            password = GenerateNewPasswordService.run(personID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("password", password);

        InfoPerson infoPerson = null;
        try {

            infoPerson = ReadPersonByUsername.run(request.getParameter("username"));
        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("A Person", e);
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Success");
    }
}