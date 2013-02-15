package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/outboundMobilityCandidacy", module = "academicAdministration")
@Forwards({ @Forward(name = "prepare", path = "/mobility/outbound/OutboundMobilityCandidacy.jsp") })
public class OutboundMobilityCandidacyDA extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        if (outboundMobilityContextBean == null) {
            outboundMobilityContextBean = new OutboundMobilityContextBean();
        }
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward prepare(final ActionMapping mapping, final HttpServletRequest request,
            final OutboundMobilityContextBean outboundMobilityContextBean) {
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("prepare");
    }

    public ActionForward invalidadeAndPrepare(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward createNewOutboundMobilityCandidacyPeriod(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.createNewOutboundMobilityCandidacyPeriod();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward createNewOutboundMobilityCandidacyContest(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.createNewOutboundMobilityCandidacyContest();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

}
