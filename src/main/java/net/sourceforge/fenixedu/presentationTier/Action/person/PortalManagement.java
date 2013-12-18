package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.AddContentToPool;
import net.sourceforge.fenixedu.applicationTier.Servico.person.DeleteTemplatedContent;
import net.sourceforge.fenixedu.applicationTier.Servico.person.EditPortal;
import net.sourceforge.fenixedu.domain.contents.Element;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "person", path = "/portalManagement", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "editPortal", path = "/person/portals/editPortal.jsp", tileProperties = @Tile(
                title = "private.personal.system.metadomainobjects")),
        @Forward(name = "addToPool", path = "/person/portals/addToPool.jsp", tileProperties = @Tile(
                title = "private.personal.system.metadomainobjects")),
        @Forward(name = "createPortal", path = "/person/portals/createMetaPortal.jsp", tileProperties = @Tile(
                title = "private.personal.system.metadomainobjects")),
        @Forward(name = "selectMetaDomainObject", path = "/person/portals/prepareCreatePortal.jsp", tileProperties = @Tile(
                title = "private.personal.system.metadomainobjects")) })
public class PortalManagement extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("metaDomainObjects", Bennu.getInstance().getMetaDomainObjectPortalSet());

        return mapping.findForward("selectMetaDomainObject");
    }

    public ActionForward prepareEditPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PortalBean bean = new PortalBean(getPortal(request));
        request.setAttribute("bean", bean);
        return mapping.findForward("editPortal");
    }

    public ActionForward editPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PortalBean bean = (PortalBean) RenderUtils.getViewState("editPortal").getMetaObject().getObject();

        try {
            EditPortal.run(bean.getPortal(), bean.getName(), bean.getPrefix());
        } catch (Exception exception) {
            addActionMessage(request, exception.getMessage());
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward deleteFromPool(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String elementId = request.getParameter("elementId");
        Element element = elementId != null ? (Element) FenixFramework.getDomainObject(elementId) : null;
        Portal portal = getPortal(request);

        try {
            DeleteTemplatedContent.run((MetaDomainObjectPortal) portal, element);
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
        }

        return prepareEditPortal(mapping, form, request, response);
    }

    public ActionForward prepareAddToPool(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Portal portal = getPortal(request);
        request.setAttribute("portal", portal);
        request.setAttribute("rootModule", Module.getRootModule());

        return mapping.findForward("addToPool");
    }

    public ActionForward addToPool(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Portal portal = getPortal(request);
        String elementId = request.getParameter("elementId");
        Element element = (elementId != null) ? (Element) FenixFramework.getDomainObject(elementId) : null;
        try {
            AddContentToPool.run((MetaDomainObjectPortal) portal, element);
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
        }

        return prepareEditPortal(mapping, form, request, response);
    }

    private Portal getPortal(HttpServletRequest request) {
        String portalID = request.getParameter("pid");
        return (Portal) FenixFramework.getDomainObject(portalID);
    }

}