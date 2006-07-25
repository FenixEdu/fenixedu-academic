package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality.Movement;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FunctionalityManagementAction extends FunctionalitiesDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);
        
        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        else {
            setBreadCrumbs(request, functionality);
            request.setAttribute("functionality", functionality);
            return mapping.findForward("view");
        }
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);
        
        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        else {
            setBreadCrumbs(request, functionality);
            request.setAttribute("functionality", functionality);
            return mapping.findForward("edit");
        }
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);

        if (module == null) {
            return mapping.findForward("create.toplevel");
        }
        else {
            setBreadCrumbs(request, module, true);
            request.setAttribute("module", module);
            return mapping.findForward("create");
        }
    }

    public ActionForward up(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(MoveFunctionality.Movement.Up, mapping, actionForm, request, response);
    }
    
    public ActionForward down(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(MoveFunctionality.Movement.Down, mapping, actionForm, request, response);
    }

    public ActionForward top(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(MoveFunctionality.Movement.Top, mapping, actionForm, request, response);
    }

    public ActionForward bottom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(MoveFunctionality.Movement.Bottom, mapping, actionForm, request, response);
    }
    
    public ActionForward indent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(MoveFunctionality.Movement.Right, mapping, actionForm, request, response);
    }
    
    public ActionForward outdent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(MoveFunctionality.Movement.Left, mapping, actionForm, request, response);
    }

    private ActionForward move(Movement movement, ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        Functionality functionality = getFunctionality(request);
    
        if (functionality == null) {
            return viewModule(module, mapping, actionForm, request, response);
        }
        
        Functionality.moveFunctionality(functionality, movement);
        return viewModule(module, mapping, actionForm, request, response);
    }
    
}
