package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/showDegreeTheses", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showThesisDetails", path = "degree-showDegreeThesisDetails"),
        @Forward(name = "showTheses", path = "degree-showDegreeTheses") })
public class DegreeShowThesesDA extends PublicShowThesesDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("degree", getDegree(request));
        return super.execute(mapping, form, request, response);
    }

    private Degree getDegree(HttpServletRequest request) {
        final DegreeSite site = SiteMapper.getSite(request);
        Degree degree = null;
        if (site != null) {
            degree = site.getDegree();
        } else {
            String degreeId = FenixContextDispatchAction.getFromRequest("degreeID", request);
            degree = FenixFramework.getDomainObject(degreeId);
        }
        if (degree != null) {
            request.setAttribute("degreeID", degree.getExternalId());
            request.setAttribute("degree", degree);
            OldCmsSemanticURLHandler.selectSite(request, degree.getSite());
        }
        return degree;
    }

    @Override
    protected ThesisFilterBean getFilterBean(HttpServletRequest request) throws Exception {
        ThesisFilterBean bean = super.getFilterBean(request);
        bean.setDegree(getDegree(request));
        bean.setDegreeOptions(Degree.readNotEmptyDegrees());
        return bean;
    }

}
