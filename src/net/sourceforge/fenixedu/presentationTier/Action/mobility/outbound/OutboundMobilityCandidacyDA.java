package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
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

    public ActionForward editCandidacyPeriod(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacyPeriod candidacyPeriod = getRenderedObject();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setExecutionYear((ExecutionYear) candidacyPeriod.getExecutionInterval());
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(candidacyPeriod));
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward addDegreeToGroup(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.addDegreeToGroup();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward removeDegreeFromGroup(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        final ExecutionDegree executionDegree = getDomainObject(request, "executionDegreeOid");
        outboundMobilityContextBean.setExecutionDegree(executionDegree);
        outboundMobilityContextBean.removeDegreeFromGroup();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward deleteContest(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = contest.getOutboundMobilityCandidacyContestGroup();
        if (mobilityGroup.getOutboundMobilityCandidacyContestCount() == 1) {
            outboundMobilityContextBean.getMobilityGroups().remove(mobilityGroup);
        }
        contest.delete();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

}
