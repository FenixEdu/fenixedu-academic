package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/theses/thesis", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showThesisDetails", path = "showThesisDetails") })
public class DirectShowThesesDA extends PublicShowThesesDA {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Site site = SiteMapper.getSite(request);
        if (site instanceof ThesisSite) {
            ThesisSite thesisSite = (ThesisSite) site;
            request.setAttribute("thesis", thesisSite.getThesis());
        }
        return mapping.findForward("showThesisDetails");
    }
}
