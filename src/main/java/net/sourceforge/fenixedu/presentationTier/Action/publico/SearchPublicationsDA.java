package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.publication.SearchPublicationsAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/publications/search")
@Forwards(value = { @Forward(name = "SearchPublication", path = "/commons/sites/unitSite/searchPublications.jsp",
        tileProperties = @Tile(extend = "definition.public.homepage")) })
public class SearchPublicationsDA extends SearchPublicationsAction {

    @Override
    public ActionForward prepareSearchPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return super.prepareSearchPublication(mapping, form, request, response);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        setRequestDomainObject(request);

        return super.execute(mapping, actionForm, request, response);
    }

    protected void setRequestDomainObject(HttpServletRequest request) {

        FunctionalityContext context = AbstractFunctionalityContext.getCurrentContext(request);
        Site site = null;
        if (context != null) {
            site = (Site) context.getLastContentInPath(Site.class);
        } else {
            String siteID = request.getParameter("siteID");
            site = (Site) FenixFramework.getDomainObject(siteID);
        }
        request.setAttribute("site", site);
    }
}
