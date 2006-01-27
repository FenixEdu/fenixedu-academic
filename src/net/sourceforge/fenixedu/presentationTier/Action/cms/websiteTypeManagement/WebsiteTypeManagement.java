package net.sourceforge.fenixedu.presentationTier.Action.cms.websiteTypeManagement;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

public class WebsiteTypeManagement extends FenixDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        Collection websiteTypes = (Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] { WebsiteType.class });
        
        request.setAttribute("websiteTypes", websiteTypes);
        
        return mapping.findForward("list");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redirect("edit", mapping, actionForm, request, response);
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("create");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer oid;
        
        try {
            oid = Integer.parseInt(request.getParameter("oid"));
        } catch (NumberFormatException e) {
            addError(request, "cms.websiteTypeManagement.websiteTypeNotFound");
            return start(mapping, actionForm, request, response);
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        ServiceUtils.executeService(userView, "DeleteWebsiteType", new Object[] { oid });
        
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
            addError(request, "cms.websiteTypeManagement.websiteTypeNotFound");
            return start(mapping, actionForm, request, response);
        }
        
        IUserView userView = SessionUtils.getUserView(request);
        WebsiteType websiteType = (WebsiteType) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { WebsiteType.class, oid });
        
        if (websiteType == null) {
            addError(request, "cms.websiteTypeManagement.websiteTypeNotFound");
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
