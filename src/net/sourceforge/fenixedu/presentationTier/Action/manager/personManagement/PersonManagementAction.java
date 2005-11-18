/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonResults;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 * 
 */
public class PersonManagementAction extends FenixDispatchAction {
    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("firstPage");
    }

    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm findPersonForm = (DynaActionForm) actionForm;
        String name = null;
        if (findPersonForm.get("name") != null) {
            name = (String) findPersonForm.get("name");
        }

        String email = null;
        if (findPersonForm.get("email") != null) {
            email = (String) findPersonForm.get("email");
        }

        String username = null;
        if (findPersonForm.get("username") != null) {
            username = (String) findPersonForm.get("username");
        }
        String documentIdNumber = null;
        if (findPersonForm.get("documentIdNumber") != null) {
            documentIdNumber = (String) findPersonForm.get("documentIdNumber");
        }

        SearchParameters searchParameters = new SearchPerson.SearchParameters(name, email, username,
                documentIdNumber, null, null, null, null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        Object[] args = { searchParameters, predicate };

        SearchPerson.SearchPersonResults result = null;
        List<InfoPerson> searchPersons = null;
        try {
            result = (SearchPersonResults) ServiceManagerServiceFactory.executeService(userView,
                    "SearchPerson", args);

            searchPersons = searchParameters.getIntervalPersons(0, result.getValidPersons().size(),
                    result.getValidPersons());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (result == null) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        request.setAttribute("personListFinded", searchPersons);

        return mapping.findForward("displayPerson");
    }

    public ActionForward findEmployee(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("displayEmployee");
    }
}