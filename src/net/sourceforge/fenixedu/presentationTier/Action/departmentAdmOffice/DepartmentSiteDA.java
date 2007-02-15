package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DepartmentProcessor;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class DepartmentSiteDA extends SiteManagementDA {

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }
    
    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return getDepartment(request).getName();
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
    protected String getItemLocationForFile(HttpServletRequest request,
            Item item, Section section) {
        return null;
    }

    @Override
    protected DepartmentSite getSite(HttpServletRequest request) {
        Department department = getDepartment(request);
        
        Unit departmentUnit = department.getDepartmentUnit();
        if (departmentUnit == null) {
            return null;
        }
        
        return (DepartmentSite) departmentUnit.getSite();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Site site = getSite(request);
        if (site == null) {
            return mapping.findForward("no-site");
        }
        else {
            request.setAttribute("site", site);
            return super.execute(mapping, actionForm, request, response);
        }
    }

    public ActionForward information(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DepartmentSite site = getSite(request);
        request.setAttribute("site", site);
     
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
            request.setAttribute("successful", true);
        }
        
        return mapping.findForward("edit-site-information");
    }
}
