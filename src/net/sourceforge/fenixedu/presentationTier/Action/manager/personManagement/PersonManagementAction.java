/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

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

        HashMap parametersSearch = new HashMap();
        parametersSearch.put(new String("name"), putSearchChar(name));
        parametersSearch.put(new String("email"), putSearchChar(email));
        parametersSearch.put(new String("username"), putSearchChar(username));
        parametersSearch.put(new String("documentIdNumber"), putSearchChar(documentIdNumber));

        Object[] args = { parametersSearch };

        List personListFinded = null;
        try {
            personListFinded = (List) ServiceManagerServiceFactory.executeService(userView,
                    "SearchPerson", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (personListFinded == null || personListFinded.size() < 2) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        request.setAttribute("personListFinded", personListFinded.get(1));

        return mapping.findForward("displayPerson");
    }

    private String putSearchChar(String searchElem) {
        String newSearchElem = null;
        if (searchElem != null) {
            newSearchElem = "%".concat(searchElem.replace(' ', '%')).concat("%");
        }
        return newSearchElem;
    }

    public ActionForward findEmployee(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("displayEmployee");
    }
}