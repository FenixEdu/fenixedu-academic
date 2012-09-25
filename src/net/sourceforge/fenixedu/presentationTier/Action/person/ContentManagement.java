package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.CreateGroupAvailability;
import net.sourceforge.fenixedu.applicationTier.Servico.person.AddFunctionalityToContainer;
import net.sourceforge.fenixedu.applicationTier.Servico.person.AddPortalToContainer;
import net.sourceforge.fenixedu.applicationTier.Servico.person.AddSectionToContainer;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ApplyStructureModifications;
import net.sourceforge.fenixedu.applicationTier.Servico.person.CopyModuleFunctionalityToContainer;
import net.sourceforge.fenixedu.applicationTier.Servico.person.DeleteNode;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Element;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.persistenceTier.statementInterceptors.FenixStatementInterceptor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.ExpressionBean;
import net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.ParserReport;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "person", path = "/contentManagement", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "addInitialContent", path = "/person/portals/addInitialContentToSection.jsp"),
		@Forward(name = "viewContainer", path = "/person/portals/manageContainer.jsp", tileProperties = @Tile(head = "/commons/renderers/treeRendererHeader.jsp")),
		@Forward(name = "addPortal", path = "/person/portals/addPortal.jsp"),
		@Forward(name = "editContent", path = "/person/portals/editContent.jsp"),
		@Forward(name = "viewElement", path = "/person/portals/manageElement.jsp", tileProperties = @Tile(head = "/commons/renderers/treeRendererHeader.jsp")),
		@Forward(name = "editAvailabilityPolicy", path = "/person/portals/editAvailabilityPolicy.jsp"),
		@Forward(name = "addFunctionality", path = "/person/portals/addFunctionality.jsp", tileProperties = @Tile(head = "/commons/renderers/treeRendererHeader.jsp")),
		@Forward(name = "createSection", path = "/person/portals/createSection.jsp") })
public class ContentManagement extends FenixDispatchAction {

    public ActionForward viewContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Content content = getContent(request);

	return (content.isContainer()) ? viewContainer(mapping, form, request, response) : viewElement(mapping, form, request,
		response);
    }

    public ActionForward activateLogging(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (!FenixStatementInterceptor.isLogging()) {
	    FenixStatementInterceptor.startLogging();
	}
	return viewContainer(mapping, form, request, response);
    }

    public ActionForward deactivateLogging(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (FenixStatementInterceptor.isLogging()) {
	    FenixStatementInterceptor.stopLogging();
	}

	return viewContainer(mapping, form, request, response);
    }

    public ActionForward viewContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Container container = getContainer(request);
	request.setAttribute("content", container);

	request.setAttribute("parentContainer", getParentContainer(request));
	return mapping.findForward("viewContainer");
    }

    public ActionForward prepareEditAvailabilityPolicy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Content content = getContent(request);
	Group permittedGroup = content.getPermittedGroup();
	request.setAttribute("content", content);
	request.setAttribute("expressionBean", new ExpressionBean(permittedGroup != null ? content.getPermittedGroup()
		.getExpression() : null));

	return mapping.findForward("editAvailabilityPolicy");
    }

    public ActionForward editAvailabilityPolicy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	Content content = getContent(request);
	ExpressionBean bean = (ExpressionBean) RenderUtils.getViewState("expressionBean").getMetaObject().getObject();

	try {
	    CreateGroupAvailability.run(content, bean.getExpression());
	} catch (GroupExpressionException groupExpression) {
	    createParserReport(request, groupExpression, bean);
	    return prepareEditAvailabilityPolicy(mapping, form, request, response);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return prepareEditAvailabilityPolicy(mapping, form, request, response);
	}

	return viewContent(mapping, form, request, response);
    }

    public ActionForward viewParentContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Container container = getParentContainer(request);
	request.setAttribute("content", container);

	return mapping.findForward("viewContainer");
    }

    public ActionForward viewElement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Element element = getElement(request);
	request.setAttribute("content", element);

	request.setAttribute("parentContainer", getParentContainer(request));
	return mapping.findForward("viewElement");
    }

    private ActionForward createContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, String forwardTo) {

	Container container = getContainer(request);
	request.setAttribute("container", container);

	return mapping.findForward(forwardTo);
    }

    public ActionForward prepareAddPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PortalBean bean = new PortalBean(getContainer(request));
	request.setAttribute("bean", bean);
	return createContent(mapping, form, request, response, "addPortal");
    }

    public ActionForward addPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PortalBean bean = (PortalBean) RenderUtils.getViewState("portal").getMetaObject().getObject();

	try {
	    AddPortalToContainer.run(bean.getContainer(), bean.getPortal());
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}

	return viewContainer(mapping, form, request, response);

    }

    public ActionForward prepareCreateSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SectionBean bean = new SectionBean(getContainer(request));
	request.setAttribute("bean", bean);
	return createContent(mapping, form, request, response, "createSection");
    }

    public ActionForward createSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SectionBean bean = (SectionBean) RenderUtils.getViewState("createSection").getMetaObject().getObject();

	try {
	    AddSectionToContainer.run(bean.getContainer(), bean.getName(), bean.isVisible(), bean.getNextSection());
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}
	return viewContainer(mapping, form, request, response);
    }

    public ActionForward prepareAddFunctionality(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("rootModule", rootDomainObject.getRootModule());
	return createContent(mapping, form, request, response, "addFunctionality");
    }

    public ActionForward prepareEditPool(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("rootModule", rootDomainObject.getRootModule());
	return createContent(mapping, form, request, response, "editPool");
    }

    public ActionForward addFunctionality(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Functionality functionality = (Functionality) getElement(request);
	Container container = getParentContainer(request);

	try {
	    AddFunctionalityToContainer.run(functionality, container);
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}

	return viewParentContainer(mapping, form, request, response);

    }

    public ActionForward addModule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Module module = (Module) getContainer(request);
	Container container = getParentContainer(request);

	try {
	    CopyModuleFunctionalityToContainer.run(module, container);
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}

	return viewParentContainer(mapping, form, request, response);
    }

    public ActionForward deleteContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Content content = getContent(request);
	Container parent = getParentContainer(request);
	Node node = content.getParentNode(parent);
	try {
	    DeleteNode.run(node);
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}
	return viewParentContainer(mapping, form, request, response);
    }

    public ActionForward editContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Content content = getContent(request);
	request.setAttribute("content", content);

	return mapping.findForward("editContent");
    }

    public ActionForward addInitialContentToSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Content content = getContent(request);
	if (content instanceof Section) {
	    request.setAttribute("content", content);
	}

	return mapping.findForward("addInitialContent");

    }

    public ActionForward organizeStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Container container = getContainer(request);
	String structure = request.getParameter("tree");

	if (structure == null) {
	    return viewContainer(mapping, form, request, response);
	}

	List<ModifiedContentBean> modifiedContent = getModifiedContent(container, structure);

	try {
	    ApplyStructureModifications.run(modifiedContent);
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}

	return viewContainer(mapping, form, request, response);
    }

    public List<ModifiedContentBean> getModifiedContent(Container container, String structure) {

	List<Content> flatContent = flatten(Collections.singletonList((Content) container));
	List<ModifiedContentBean> modifiedContent = new ArrayList<ModifiedContentBean>();

	if (structure == null) {
	    return new ArrayList<ModifiedContentBean>();
	}

	String[] nodes = structure.split(",");
	Map<Integer, Integer> nodeOrder = new HashMap<Integer, Integer>();

	for (int i = 0; i < nodes.length; i++) {
	    String[] parts = nodes[i].split("-");

	    Integer childIndex = getId(parts[0]);
	    Integer currentParentIndex = getId(parts[1]);
	    Integer parentIndex = getId(parts[2]);

	    if (childIndex > 0) {
		childIndex--;
	    }
	    if (parentIndex > 0) {
		parentIndex--;
	    }
	    if (currentParentIndex > 0) {
		currentParentIndex--;
	    }

	    int orderForParent = 0;
	    if (nodeOrder.containsKey(parentIndex)) {
		orderForParent = nodeOrder.get(parentIndex) + 1;
		nodeOrder.remove(parentIndex);
		nodeOrder.put(parentIndex, orderForParent);
	    } else {
		nodeOrder.put(parentIndex, orderForParent);
	    }

	    Container parentContainer = (Container) flatContent.get(parentIndex);
	    Content content = flatContent.get(childIndex);
	    Container currentContainer = (Container) flatContent.get(currentParentIndex);
	    modifiedContent.add(new ModifiedContentBean(currentContainer, content, parentContainer, orderForParent));
	}

	return modifiedContent;

    }

    private Integer getId(String id) {
	if (id == null) {
	    return null;
	}

	try {
	    return new Integer(id);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    protected Content getContent(HttpServletRequest request) {
	String contentId = request.getParameter("contentId");
	return (contentId == null) ? null : rootDomainObject.readContentByOID(Integer.valueOf(contentId));
    }

    protected Element getElement(HttpServletRequest request) {
	Content content = getContent(request);
	return Element.class.isAssignableFrom(content.getClass()) ? (Element) content : null;
    }

    protected Container getContainer(HttpServletRequest request) {
	Content content = getContent(request);
	return content == null ? Portal.getRootPortal()
		: Container.class.isAssignableFrom(content.getClass()) ? (Container) content : null;
    }

    protected Container getParentContainer(HttpServletRequest request) {
	String containerId = request.getParameter("contentParentId");
	return containerId == null ? null : (Container) rootDomainObject.readContentByOID(Integer.valueOf(containerId));
    }

    private List<Content> flatten(Collection<Content> contents) {
	List<Content> flatContent = new ArrayList<Content>();
	for (Content content : contents) {
	    if (content instanceof Container) {
		flatContent.add(content);
		flatContent.addAll(flatten(((Container) content).getChildrenAsContent()));
	    } else {
		flatContent.add(content);
	    }
	}
	return flatContent;
    }

    private void createMessage(HttpServletRequest request, String name, GroupExpressionException e) {
	ActionMessages messages = getMessages(request);

	if (e.isResource()) {
	    messages.add(name, new ActionMessage(e.getKey(), e.getArgs()));
	} else {
	    messages.add(name, new ActionMessage(e.getMessage(), false));
	}

	saveMessages(request, messages);
    }

    protected void createParserReport(HttpServletRequest request, GroupExpressionException e, ExpressionBean bean) {
	createMessage(request, "error", e);

	if (e.hasLineInformation()) {
	    request.setAttribute("parserReport", new ParserReport(e, bean.getExpression()));
	}
    }

}