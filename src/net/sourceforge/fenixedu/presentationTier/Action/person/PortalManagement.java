package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Element;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PortalManagement extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("metaDomainObjects", rootDomainObject.getMetaDomainObjects());

	return mapping.findForward("selectMetaDomainObject");
    }

    public ActionForward prepareCreatePortal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	MetaDomainObject metaDomainObject = getMetaDomainObject(request);
	PortalBean bean = new PortalBean(metaDomainObject);

	request.setAttribute("bean", bean);
	return mapping.findForward("createPortal");
    }
    
    public ActionForward prepareEditPortal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	
	PortalBean bean = new PortalBean(getPortal(request));
	request.setAttribute("bean", bean);
	return mapping.findForward("editPortal");
    }
    
    public ActionForward editPortal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	
	PortalBean bean = (PortalBean) RenderUtils.getViewState("editPortal").getMetaObject()
	.getObject();
	Object[] args = new Object[] {
		bean.getPortal(), bean.getName(), bean.getPrefix()
	};
	try {
	    executeService("EditPortal",args);
	}catch(Exception exception) {
	    addActionMessage(request,exception.getMessage());
	}
	
	return prepare(mapping, form, request, response);
    }
    
    public ActionForward createPortal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	PortalBean bean = (PortalBean) RenderUtils.getViewState("createPortal").getMetaObject()
		.getObject();
	Object[] args = new Object[] {
		bean.getContainer() == null ? bean.getMetaDomainObject() : bean.getContainer(),
		bean.getName(), bean.getPrefix() };

	try {
	    executeService("CreatePortal", args);
	} catch (Exception exception) {
	    addActionMessage(request, exception.getMessage());
	}

	return prepare(mapping, form, request, response);
    }

    public ActionForward deleteFromPool(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	String elementId = request.getParameter("elementId");
	Element element = elementId != null ? (Element) RootDomainObject.getInstance().readContentByOID(Integer.valueOf(elementId)) : null;
	Portal portal = getPortal(request);
	
	try {
	    executeService("DeleteTemplatedContent", new Object[] { portal, element });
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}

	return prepareEditPortal(mapping, form, request, response);
    }

    public ActionForward prepareAddToPool(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	
	Portal portal = getPortal(request);
	request.setAttribute("portal", portal);
	request.setAttribute("rootModule",Module.getRootModule());
	
	
	return mapping.findForward("addToPool");
    }
    
    public ActionForward addToPool(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	
	Portal portal = getPortal(request);
	String elementId = request.getParameter("elementId");
	Element element = (elementId != null) ? (Element) RootDomainObject.getInstance().readContentByOID(Integer.valueOf(elementId)) : null;
	try {
	executeService("AddContentToPool", new Object[] { portal, element});
	}catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	}
	
	return prepareEditPortal(mapping, form, request, response);
    }
    
    private Portal getPortal(HttpServletRequest request) {
	String portalID = request.getParameter("pid");
	return (Portal) rootDomainObject.readContentByOID(Integer.valueOf(portalID));
    }
    
    private MetaDomainObject getMetaDomainObject(HttpServletRequest request) {
	String metaObjectID = request.getParameter("oid");
	return (MetaDomainObject) rootDomainObject.readMetaDomainObjectByOID(Integer
		.valueOf(metaObjectID));
    }
}
