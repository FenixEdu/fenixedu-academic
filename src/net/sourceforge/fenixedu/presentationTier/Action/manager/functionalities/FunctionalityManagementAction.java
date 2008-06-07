package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.IFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

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

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FunctionalityManagementAction extends FunctionalitiesDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Content functionality = getFunctionality(request);

        if (functionality == null) {
            return viewRoot(mapping, actionForm, request, response);
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
        Content functionality = getFunctionality(request);

        if (functionality == null) {
            return viewRoot(mapping, actionForm, request, response);
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

    public ActionForward manage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Content functionality = getFunctionality(request);

        if (functionality == null) {
            return viewRoot(mapping, actionForm, request, response);
        }

        ExpressionGroupAvailability availability = (ExpressionGroupAvailability) functionality.getAvailabilityPolicy();
        
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

    private void setupAvailabilityStack(Content content, HttpServletRequest request) {
        List<AvailabilityBean> availabilities = new ArrayList<AvailabilityBean>();
        
        IFunctionality functionality = (IFunctionality) content;
        for (Module module = functionality.getModule(); module != null; module = module.getModule()) {
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
        Content functionality = getFunctionality(request);

        if (functionality == null) {
            return viewRoot(mapping, actionForm, request, response);
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
        Content functionality = getFunctionality(request);

        Element toplevel = new Element("structure").setAttribute("version", "1.0");
        if (functionality != null) {
            Module parent = ((IFunctionality) functionality).getModule();
            if (parent != null) {
                toplevel.setAttribute("parent", parent.getContentId());
            }
            
            toplevel.addContent(generateElement(functionality));
        }
        else {
            for (Content topLevelFunctionality : Module.getRootModule().getOrderedChildren(Content.class)) {
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

    private Element generateElement(Content functionality) {
        Element element = new Element("functionality");
        
        element.setAttribute("type", functionality.getClass().getName());
        element.setAttribute("uuid", functionality.getContentId().toString());
        
        Element name = new Element("name");
        generateMultiLanguageStringElement(name, functionality.getName());
        
        Element title = new Element("title");
        generateMultiLanguageStringElement(title, functionality.getTitle());
        
        Element description = new Element("description");
        generateMultiLanguageStringElement(description, functionality.getDescription());
        
        element.addContent(name);
        element.addContent(title);
        element.addContent(description);
        
        if (functionality instanceof Functionality) {
            Functionality f = (Functionality) functionality;
        
            if (f.getPath() != null) {
                element.setAttribute("path", f.getPath());
            }
            
            if (f.getParameters() != null) {
                element.setAttribute("parameters", f.getParametersString());
            }
        }
        
        IFunctionality f = (IFunctionality) functionality;
        Module parent = f.getModule();
        ExplicitOrderNode node = (ExplicitOrderNode) functionality.getParentNode(parent);
        
        element.setAttribute("order", String.valueOf(node.getNodeOrder()));
        element.setAttribute("visible", String.valueOf(node.isNodeVisible()));

        if (functionality.getAvailabilityPolicy() != null) {
            String expression = ((ExpressionGroupAvailability) functionality.getAvailabilityPolicy()).getExpression();
            element.addContent(new Element("availability").addContent(new Text(expression)));
        }
        
        if (functionality instanceof Module) {
            Module module = (Module) functionality;
            
            element.setAttribute("prefix", module.getPrefix());
            
            Element children = new Element("children");
            for (Content child : module.getOrderedFunctionalities()) {
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
    
    public ActionForward removeParameter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FunctionalityParameter functionalityParameter = getFunctionalityParameter(request);
        Content functionality = getFunctionality(request);

        if (functionalityParameter != null) {
            deleteFunctionalityParameter(functionalityParameter);
        } 
        
        return forwardTo(mapping.findForward("view"), request, functionality);
    }
    
    private void deleteFunctionalityParameter(FunctionalityParameter functionalityParameter) throws FenixFilterException, FenixServiceException {
	executeService("DeleteFunctionalityParameter", functionalityParameter);
	
    }

    protected FunctionalityParameter getFunctionalityParameter(HttpServletRequest request) {
        return (FunctionalityParameter) getObject(request, FunctionalityParameter.class, "functionalityParameter");
    }

    
}
