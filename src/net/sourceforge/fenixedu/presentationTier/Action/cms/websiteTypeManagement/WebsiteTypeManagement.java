package net.sourceforge.fenixedu.presentationTier.Action.cms.websiteTypeManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.cms.Bin;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.FunctionalityLink;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteItem;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteSection;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class WebsiteTypeManagement extends FenixDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        Collection websiteTypes = (Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] { WebsiteType.class });
        
        request.setAttribute("websiteTypes", websiteTypes);
        
        return mapping.findForward("list");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer oid;
        
        try {
            oid = Integer.parseInt(request.getParameter("oid"));
        } catch (NumberFormatException e) {
            addError(request, "cms.websiteTypeManagement.websiteType.notFound");
            return start(mapping, actionForm, request, response);
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        WebsiteType websiteType = (WebsiteType) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { WebsiteType.class, oid });
        
        if (websiteType == null) {
            addError(request, "cms.websiteTypeManagement.websiteType.notFound");
            return start(mapping, actionForm, request, response);
        }
        
        List<Content> children = new ArrayList<Content>();
        for (Content content : websiteType.getMandatoryContents()) {
            if (! (content instanceof FunctionalityLink)) {
                children.add(content);
            }
        }
        
        request.setAttribute("websiteTypeChildren", children);
        return redirect("edit", mapping, actionForm, request, response);
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("create");
    }

    public ActionForward createChild(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        processPath(request);
        
        Cms cms;
        try {
            IUserView userView = SessionUtils.getUserView(request);

            // TODO: FenixCMS?! Do we really need support for multiple CMSs?
            cms = (Cms) ServiceUtils.executeService(userView, "ReadCmsByName", new Object[] { "FenixCMS" });
        }
        catch (DomainException e) {
            addError(request, e.getKey());
            return start(mapping, actionForm, request, response);
        }

        request.setAttribute("cms", cms);
        
        Bin parent = (Bin) readObject(request, request.getParameter("parent"), null, Bin.class);
        if (parent != null) {
            request.setAttribute("parent", parent);
        }
        
        if (request.getParameter("item") != null) {
            return redirect("createItem", mapping, actionForm, request, response);
        }
        else if (request.getParameter("section") != null) {
            return redirect("createSection", mapping, actionForm, request, response);
        }
        
        addError(request, "cms.websiteTypeManagement.notSupported.create");
        return edit(mapping, actionForm, request, response);
    }

    public ActionForward editChild(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        processPath(request);
        
        Content content = (Content) readObject(request, request.getParameter("child"), "cms.websiteTypeManagement.child.notFound", Content.class);
        if (content == null) {
            return start(mapping, actionForm, request, response);
        }

        Bin parent = (Bin) readObject(request, request.getParameter("parent"), null, Bin.class);
        if (parent != null) {
            request.setAttribute("parent", parent);
        }
        
        if (content instanceof WebsiteSection) {
            request.setAttribute("section", content);
            return redirect("editSection", mapping, actionForm, request, response);
        }
        else if (content instanceof WebsiteItem) {
            request.setAttribute("item", content);
            return redirect("editItem", mapping, actionForm, request, response);
        }
        
        addError(request, "cms.websiteTypeManagement.notSupported.edit");
        return start(mapping, actionForm, request, response);
    }

    public ActionForward deleteChild(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        addError(request, "cms.websiteTypeManagement.operation.notImplemented");
        return edit(mapping, actionForm, request, response);
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer oid;
        
        try {
            oid = Integer.parseInt(request.getParameter("oid"));
        } catch (NumberFormatException e) {
            addError(request, "cms.websiteTypeManagement.websiteType.notFound");
            return start(mapping, actionForm, request, response);
        }
        
        try {
            IUserView userView = SessionUtils.getUserView(request);
            ServiceUtils.executeService(userView, "DeleteWebsiteType", new Object[] { oid });
        }
        catch (DomainException e) {
            addError(request, e.getKey());
        }
        
        return start(mapping, actionForm, request, response);
    }
    
    public ActionForward details(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redirect("details", mapping, actionForm, request, response);
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
                
                parents.add((Bin) readObject(request, oid, "cms.websiteTypeManagement.parent.notFound", Bin.class));
            }
            
            request.setAttribute("parents", parents);
        }
    }
    
    protected Object readObject(HttpServletRequest request, String id, String errorKey, Class type) throws FenixFilterException, FenixServiceException {
        Integer oid;
        try {
            oid = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            if (errorKey != null) {
                addError(request, errorKey);
            }
            
            return null;
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        return ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { type, oid });
    }
    
    protected ActionForward redirect(String name, ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer oid;
        
        try {
            oid = Integer.parseInt(request.getParameter("oid"));
        } catch (NumberFormatException e) {
            addError(request, "cms.websiteTypeManagement.websiteType.notFound");
            return start(mapping, actionForm, request, response);
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        WebsiteType websiteType = (WebsiteType) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { WebsiteType.class, oid });
        
        if (websiteType == null) {
            addError(request, "cms.websiteTypeManagement.websiteType.notFound");
            return start(mapping, actionForm, request, response);
        }

        request.setAttribute("websiteType", websiteType);
        return mapping.findForward(name);
    }
    
    protected void addError(HttpServletRequest request, String key) {
        ActionMessages errors = getErrors(request);
        ActionMessages messages = new ActionMessages();
        
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
        
        errors.add(messages);
        saveErrors(request, errors);
    }
}
