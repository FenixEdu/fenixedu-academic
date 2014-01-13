package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.SearchExternalPersonsByName;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/findExternalPerson",
        input = "/findExternalPerson.do?page=0&method=prepare", attribute = "findExternalPersonForm",
        formBean = "findExternalPersonForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "error", path = "df.page.showExternalPersons_Error"),
        @Forward(name = "start", path = "df.page.findExternalPerson"),
        @Forward(name = "success", path = "df.page.showExternalPersons") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class FindExternalPersonDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("start");

    }

    public ActionForward find(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        ActionErrors actionErrors = new ActionErrors();

        DynaActionForm findExternalPersonForm = (DynaActionForm) form;
        String externalPersonName = (String) findExternalPersonForm.get("name");

        List infoExternalPersonsList = null;

        try {
            if (!externalPersonName.equals("")) {
                infoExternalPersonsList = SearchExternalPersonsByName.run(externalPersonName);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if ((infoExternalPersonsList != null) && (infoExternalPersonsList.isEmpty() == false)) {
            request.setAttribute(PresentationConstants.EXTERNAL_PERSONS_LIST, infoExternalPersonsList);

            return mapping.findForward("success");
        }
        actionErrors.add("label.masterDegree.administrativeOffice.searchResultsEmpty", new ActionError(
                "label.masterDegree.administrativeOffice.searchResultsEmpty"));

        saveErrors(request, actionErrors);
        return mapping.findForward("error");

    }

}