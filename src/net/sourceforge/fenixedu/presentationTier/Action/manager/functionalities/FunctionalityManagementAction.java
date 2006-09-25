package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality.Movement;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ModuleUtils;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class FunctionalityManagementAction extends FunctionalitiesDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        } else {
            if (functionality instanceof Module) {
                return viewModule((Module) functionality, mapping, actionForm, request, response);
            }
            else {
                return forwardTo(mapping.findForward("view"), request, functionality);
            }
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

        moveFunctionality(functionality, movement);

        return viewModule(module, mapping, actionForm, request, response);
    }

    public ActionForward enable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }

        enable(functionality);

        return manage(mapping, actionForm, request, response);
    }

    public ActionForward disable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }

        disable(functionality);

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

        setupAvailabilityStack(functionality, request);
        return forwardTo(mapping.findForward("manage"), request, functionality);
    }

    private void setupAvailabilityStack(Functionality functionality, HttpServletRequest request) {
        List<AvailabilityBean> availabilities = new ArrayList<AvailabilityBean>();
        
        for (Module module = functionality.getModule(); module != null; module = module.getParent()) {
            GroupAvailability availability = (GroupAvailability) module.getAvailabilityPolicy();
            
            if (availability != null) {
                availabilities.add(0, new AvailabilityBean(module));
            }
        }
        
        if (! availabilities.isEmpty()) {
            request.setAttribute("contextAvailabilities", availabilities);
        }
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
            setGroupAvailability(functionality, bean.getExpression());
            RenderUtils.invalidateViewState();

            addMessage(request, "expression", "functionalities.expression.success", new String[0]);
            return manage(mapping, actionForm, request, response);
        } catch (GroupExpressionException e) {
            createParserReport(request, e, bean);
        }
        
        request.setAttribute("bean", bean);
        setupAvailabilityStack(functionality, request);
        
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

    private void createParserReport(HttpServletRequest request, GroupExpressionException e, ExpressionBean bean) {
        createMessage(request, "error", e);
        
        if (e.hasLineInformation()) {
            request.setAttribute("parserReport", new ParserReport(e, bean.getExpression()));
        }
    }
    
    public ActionForward exportStructure(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);

        Element toplevel = new Element("structure").setAttribute("version", "1.0");
        if (functionality != null) {
            if (functionality.getModule() != null) {
                toplevel.setAttribute("parent", functionality.getModule().getUuid().toString());
            }
            
            toplevel.addContent(generateElement(functionality));
        }
        else {
            for (Functionality topLevelFunctionality : Module.getOrderedTopLevelFunctionalities()) {
                toplevel.addContent(generateElement(topLevelFunctionality));
            }
        }
        
        response.setContentType("text/xml");
        response.setHeader("Content-Disposition", "attachment; filename=\"module-structure.xml\"");

        Document document = new Document(toplevel);

        DocType docType = getEmbeddedDocType(request);
        if (docType != null) {
            document.setDocType(docType);
        }
        
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("ISO-8859-1"));
        outputter.output(document, response.getWriter());
        
        return null;
    }

    private DocType getEmbeddedDocType(HttpServletRequest request) throws IOException {
        String prefix = ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
        InputStream stream = getServlet().getServletContext().getResourceAsStream(prefix + "/dtd/structure-1.0.dtd");
        
        if (stream == null) {
            return null;
        }
        
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffered = new BufferedReader(reader);
        
        StringBuilder builder = new StringBuilder();
        
        String line = null;
        while ((line = buffered.readLine()) != null) {
            builder.append(line + "\n");
        }
        
        DocType docType = new DocType("structure");
        docType.setInternalSubset(builder.toString());
        
        return docType;
    }

    private Element generateElement(Functionality functionality) {
        Element element = new Element("functionality");
        
        element.setAttribute("type", functionality.getClass().getName());
        element.setAttribute("uuid", functionality.getUuid().toString());
        
        Element name = new Element("name");
        generateMultiLanguageStringElement(name, functionality.getName());
        
        Element title = new Element("title");
        generateMultiLanguageStringElement(title, functionality.getTitle());
        
        Element description = new Element("description");
        generateMultiLanguageStringElement(description, functionality.getDescription());
        
        element.addContent(name);
        element.addContent(title);
        element.addContent(description);
        
        if (functionality.getPath() != null) {
            element.setAttribute("path", functionality.getPath());
        }
        
        if (functionality.getParameters() != null) {
            element.setAttribute("parameters", functionality.getParameters());
        }
        
        if (functionality.getOrderInModule() != null) {
            element.setAttribute("order", String.valueOf(functionality.getOrderInModule()));
        }

        element.setAttribute("principal", String.valueOf(functionality.isPrincipal()));
        element.setAttribute("enabled", String.valueOf(functionality.isEnabled()));
        element.setAttribute("relative", String.valueOf(functionality.isRelative()));
        element.setAttribute("visible", String.valueOf(functionality.isVisible()));
        element.setAttribute("maximized", String.valueOf(functionality.isMaximized()));

        if (functionality.getAvailabilityPolicy() != null) {
            String expression = ((GroupAvailability) functionality.getAvailabilityPolicy()).getExpression();
            element.addContent(new Element("availability").addContent(new Text(expression)));
        }
        
        if (functionality instanceof Module) {
            Module module = (Module) functionality;
            
            element.setAttribute("prefix", module.getPrefix());
            
            Element children = new Element("children");
            for (Functionality child : module.getOrderedFunctionalities()) {
                children.addContent(generateElement(child));
            }
            
            element.addContent(children);
        }
        
        return element;
    }

    private void generateMultiLanguageStringElement(Element parent, MultiLanguageString mlString) {
        if (mlString == null) {
            return;
        }
        
        for (Language language : mlString.getAllLanguages()) {
            Element element = new Element("value");
            element.setAttribute("language", language.name());
            element.addContent(new Text(mlString.getContent(language)));
            parent.addContent(element);
        }
    }
    
}
