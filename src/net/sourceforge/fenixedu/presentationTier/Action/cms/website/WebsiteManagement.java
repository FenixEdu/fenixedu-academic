package net.sourceforge.fenixedu.presentationTier.Action.cms.website;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.cms.Bin;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.website.Website;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteItem;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteSection;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class WebsiteManagement extends FenixDispatchAction {
   
    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Content content = getContent(request);
        
        if (content == null) {
            mapping.findForward("viewWebsites");
        }
        
        processPath(request);
        
        if (content instanceof Website) {
            return editWebsite(mapping, actionForm, request, response);
        }
        else if (content instanceof WebsiteSection) {
            return editSection(mapping, actionForm, request, response);
        }
        else if (content instanceof WebsiteItem) {
            return editItem(mapping, actionForm, request, response);
        }
        
        addError(request, "cms.websiteManagement.notSupported.edit");
        return mapping.findForward("viewWebsites");
    }

    public ActionForward editWebsite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        return proceedWithContent("website", "edit", mapping, request);
    }
    
    public ActionForward editSection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        return proceedWithContent("section", "editSection", mapping, request);
    }

    private void processPath(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        String path = request.getParameter("path");
        
        if (path == null || path.length() == 0) {
            request.setAttribute("path", "");
        }
        else {
            request.setAttribute("path", path);
        
            List<Bin> parents = new ArrayList<Bin>();
            
            String parts[] = path.split("/");
            for (int i = 0; i < parts.length; i++) {
                String oid = parts[i];
                
                parents.add((Bin) readObject(request, oid, "cms.websiteManagement.parent.notFound", Bin.class));
            }
            
            request.setAttribute("parents", parents);
        }
    }

    public ActionForward editItem(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        request.setAttribute("parent", getParent(request));
        return proceedWithContent("item", "editItem", mapping, request);
    }

    public ActionForward createChild(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        processPath(request);
        
        if (request.getParameter("section") != null) {
            return createSection(mapping, actionForm, request, response);
        }
        
        if (request.getParameter("item") != null) {
            return createItem(mapping, actionForm, request, response);
        }
        
        addError(request, "cms.websiteManagement.notSupported.create");
        return mapping.findForward("viewWebsites");
    }
    
    public ActionForward createSection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return proceedWithContent("parent", "createSection", mapping, request);
    }

    public ActionForward createItem(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return proceedWithContent("parent", "createItem", mapping, request);
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Bin parent = (Bin) getContent(request);
        Content child = getChild(request);
        
        if (parent == null) {
            return mapping.findForward("viewWebsites");
        }
        
        if (child == null) {
            return edit(mapping, actionForm, request, response);
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        try {
            ServiceUtils.executeService(userView, "RemoveBinChild", new Object[] { parent, child });
        }
        catch (Exception e) {
            addError(request, "cms.websiteManagement.child.delete.error");
        }
        
        return edit(mapping, actionForm, request, response);
    }
    
    //
    // Utilities
    //
    
    protected Content getContent(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        return (Content) getObject(request, "oid", "cms.websiteManagement.content.notFound", Content.class);
    }

    protected Bin getParent(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        return (Bin) getObject(request, "parent", "cms.websiteManagement.parent.notFound", Bin.class);
    }
    
    protected Content getChild(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        return (Content) getObject(request, "child", "cms.websiteManagement.child.notFound", Content.class);
    }
    
    protected Object getObject(HttpServletRequest request, String name, String errorKey, Class type) throws FenixFilterException, FenixServiceException {
        return readObject(request, request.getParameter(name), errorKey, type);
    }
    
    protected Object readObject(HttpServletRequest request, String id, String errorKey, Class type) throws FenixFilterException, FenixServiceException {
        Integer oid;
        try {
            oid = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            addError(request, errorKey);
            return null;
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        return ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { type, oid });
    }
    
    protected ActionForward proceedWithContent(String name, String destination, ActionMapping mapping, HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        Content content = getContent(request);
        
        if (content == null) {
            addError(request, "cms.websiteManagement.content.notFound");
            return mapping.findForward("viewWebsites");
        }
        
        request.setAttribute(name, content);
        return mapping.findForward(destination);
    }

    protected void addError(HttpServletRequest request, String key) {
        ActionMessages errors = getErrors(request);
        ActionMessages messages = new ActionMessages();
        
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
        
        errors.add(messages);
        saveErrors(request, errors);
    }
}
