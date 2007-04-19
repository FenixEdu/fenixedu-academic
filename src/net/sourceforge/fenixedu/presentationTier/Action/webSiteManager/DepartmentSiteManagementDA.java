package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DepartmentProcessor;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class DepartmentSiteManagementDA extends SiteManagementDA {

    private Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Department getDepartment(final HttpServletRequest request) {
        DepartmentSite site = getSite(request);
        
        return site == null ? null : site.getDepartment();
    }
    
    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return getDepartment(request).getRealName();
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Department department = getDepartment(request);
        try {
            return RequestUtils.absoluteURL(request, DepartmentProcessor.getDepartmentPath(department)).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return null;
    }

    @Override
    protected DepartmentSite getSite(HttpServletRequest request) {
        Integer oid = getId(request.getParameter("oid"));

        if (oid == null) {
            return null;
        }
        
        return (DepartmentSite) RootDomainObject.getInstance().readSiteByOID(oid);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("start");
    }
    
    public ActionForward information(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
            request.setAttribute("successful", true);
        }
        
        return mapping.findForward("edit-site-information");
    }

}
