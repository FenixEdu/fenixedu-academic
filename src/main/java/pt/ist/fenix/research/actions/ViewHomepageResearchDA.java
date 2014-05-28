package pt.ist.fenix.research.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ViewHomepageDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/viewHomepageResearch")
@Forwards({
        @Forward(path = "/publico/homepage/research/showPublications.jsp", name = "showPublications", tileProperties = @Tile(
                extend = "definition.public.homepage")),

        @Forward(path = "/publico/homepage/research/interests.jsp", name = "showInterests", tileProperties = @Tile(
                extend = "definition.public.homepage")),

        @Forward(path = "/publico/homepage/research/patents.jsp", name = "showPatents", tileProperties = @Tile(
                extend = "definition.public.homepage")),

        @Forward(path = "/publico/homepage/research/participations.jsp", name = "showParticipations", tileProperties = @Tile(
                extend = "definition.public.homepage")),

        @Forward(path = "/publico/homepage/research/show-prizes.jsp", name = "showPrizes", tileProperties = @Tile(
                extend = "definition.public.homepage")) })
public class ViewHomepageResearchDA extends ViewHomepageDA {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("researchers", getHomepage(request).getPerson().getUser().getUsername());
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showPublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showPublications");
    }

    public ActionForward showPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showPatents");
    }

    public ActionForward showInterests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showInterests");
    }

    public ActionForward showParticipations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showParticipations");

    }

    public ActionForward showPrizes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showPrizes");
    }

    @Override
    protected Homepage getHomepage(HttpServletRequest request) {
        Site site = SiteMapper.getSite(request);
        if (site instanceof Homepage) {
            return (Homepage) site;
        } else {
            String homepageID = request.getParameter("homepageID");
            if (homepageID != null) {
                site = FenixFramework.getDomainObject(homepageID);
            } else {
                site = getDomainObject(request, "siteID");
            }
            OldCmsSemanticURLHandler.selectSite(request, site);
            return (Homepage) site;
        }

    }
}
