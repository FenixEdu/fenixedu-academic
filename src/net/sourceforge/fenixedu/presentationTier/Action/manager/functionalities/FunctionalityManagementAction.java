package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality.Movement;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class FunctionalityManagementAction extends FunctionalitiesDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        } else {
            return forwardTo(mapping.findForward("view"), request, functionality);
        }

    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        } else {
            return forwardTo(mapping.findForward("edit"), request, functionality);
        }
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);

        if (module == null) {
            return mapping.findForward("create.toplevel");
        } else {
            return forwardTo(mapping.findForward("create"), request, module, true);
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

    public ActionForward enable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }

        Functionality.enable(functionality);

        return manage(mapping, actionForm, request, response);
    }

    public ActionForward disable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }

        Functionality.disable(functionality);

        return manage(mapping, actionForm, request, response);
    }

    public ActionForward manage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }

        GroupAvailability availability = (GroupAvailability) functionality.getAvailabilityPolicy();
        
        if (availability != null) {
            request.setAttribute("availability", availability);
            request.setAttribute("bean", new ExpressionBean(availability.getExpression()));
        }
        else {
            request.setAttribute("bean", new ExpressionBean());
        }
        
        return forwardTo(mapping.findForward("manage"), request, functionality);
    }

    public ActionForward parse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }

        IViewState viewState = RenderUtils.getViewState();

        if (viewState == null) {
            return manage(mapping, actionForm, request, response);
        }

        ExpressionBean bean = (ExpressionBean) viewState.getMetaObject().getObject();

        if (bean == null || bean.getExpression() == null) {
            return manage(mapping, actionForm, request, response);
        }

        try {
            Functionality.setGroupAvailability(functionality, bean.getExpression());
            RenderUtils.invalidateViewState();

            addMessage(request, "expression", "functionalities.expression.success");
            return manage(mapping, actionForm, request, response);
        } catch (GroupExpressionException e) {
            createParserReport(request, e, bean);
        }
        
        request.setAttribute("bean", bean);
        return forwardTo(mapping.findForward("manage"), request, functionality);
    }

    private void createMessage(HttpServletRequest request, String name, GroupExpressionException e) {
        ActionMessages messages = getMessages(request);
        
        if (e.isResource()) {
            messages.add(name, new ActionMessage(e.getKey(), e.getArgs()));
        }
        else {
            messages.add(name, new ActionMessage(e.getMessage(), false));
        }
        
        saveMessages(request, messages);
    }

    private void addMessage(HttpServletRequest request, String name, String key) {
        ActionMessages messages = getMessages(request);
        messages.add(name, new ActionMessage(key));
        saveMessages(request, messages);
    }
    
    private void createParserReport(HttpServletRequest request, GroupExpressionException e, ExpressionBean bean) {
        createMessage(request, "error", e);
        
        if (e.hasLineInformation()) {
            request.setAttribute("parserReport", new ParserReport(e, bean.getExpression()));
        }
    }
}
