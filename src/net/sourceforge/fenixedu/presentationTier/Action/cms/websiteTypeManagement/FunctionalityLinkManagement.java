package net.sourceforge.fenixedu.presentationTier.Action.cms.websiteTypeManagement;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.FunctionalityLink;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class FunctionalityLinkManagement extends FenixDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Set<FunctionalityLink> functionalityLinks = new HashSet<FunctionalityLink>();
        for (final Content content : rootDomainObject.getContentsSet()) {
            if (content instanceof FunctionalityLink) {
                functionalityLinks.add((FunctionalityLink) content);
            }
        }
        
        request.setAttribute("functionalityLinks", functionalityLinks);
        
        return mapping.findForward("list");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redirect("edit", mapping, actionForm, request, response);
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        return mapping.findForward("create");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer oid;
        
        try {
            oid = Integer.parseInt(request.getParameter("oid"));
        } catch (NumberFormatException e) {
            addError(request, "cms.functionalityLink.functionalityLinkNotFound");
            return start(mapping, actionForm, request, response);
        }
        
        try {
            IUserView userView = SessionUtils.getUserView(request);
            ServiceUtils.executeService(userView, "DeleteFunctionalityLink", new Object[] { oid });
        }
        catch (DomainException e) {
            addError(request, e.getKey());
        }
        
        return start(mapping, actionForm, request, response);
    }
    
    public ActionForward details(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redirect("details", mapping, actionForm, request, response);
    }

    protected ActionForward redirect(String name, ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer oid;
        
        try {
            oid = Integer.parseInt(request.getParameter("oid"));
        } catch (NumberFormatException e) {
            addError(request, "cms.functionalityLink.functionalityLinkNotFound");
            return start(mapping, actionForm, request, response);
        }
        
        FunctionalityLink functionalityLink = (FunctionalityLink) rootDomainObject.readContentByOID(oid);
        
        if (functionalityLink == null) {
            addError(request, "cms.functionalityLink.functionalityLinkNotFound");
            return start(mapping, actionForm, request, response);
        }

        request.setAttribute("functionalityLink", functionalityLink);
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
