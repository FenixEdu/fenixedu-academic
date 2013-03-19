package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

@Mapping(path = "/outboundMobilityCandidacy", module = "academicAdministration")
@Forwards({ @Forward(name = "prepare", path = "/mobility/outbound/OutboundMobilityCandidacy.jsp"),
        @Forward(name = "viewContest", path = "/mobility/outbound/viewContest.jsp"),
        @Forward(name = "manageCandidacies", path = "/mobility/outbound/manageCandidacies.jsp"),
        @Forward(name = "viewCandidate", path = "/mobility/outbound/viewCandidate.jsp"),
        @Forward(name = "sendEmail", path = "/messaging/emails.do?method=newEmail", contextRelative = true) })
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

    public ActionForward addCandidateOption(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();

        outboundMobilityContextBean.addCandidateOption();

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
        if (contest != null) {
            final OutboundMobilityCandidacyContestGroup mobilityGroup = contest.getOutboundMobilityCandidacyContestGroup();
            if (mobilityGroup.getOutboundMobilityCandidacyContestCount() == 1) {
                outboundMobilityContextBean.getMobilityGroups().remove(mobilityGroup);
            }
            contest.delete();
        }
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward viewContest(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        request.setAttribute("contest", contest);
        return mapping.findForward("viewContest");
    }

    public ActionForward viewContestForm(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        request.setAttribute("contest", contest);
        return new ActionForward(viewContestPath(mapping, request, contest), true);
    }

    private String viewContestPath(final ActionMapping mapping, final HttpServletRequest request,
            final OutboundMobilityCandidacyContest contest) {
        final StringBuilder path = new StringBuilder();
        path.append(mapping.getModuleConfig().getPrefix());
        path.append("/outboundMobilityCandidacy.do?method=viewContest&contestOid=");
        path.append(contest.getExternalId());
        return constructRedirectPath(mapping, request, path);
    }

    private String constructRedirectPath(final ActionMapping mapping, final HttpServletRequest request, final StringBuilder path) {
        path.append('&');
        path.append(net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
        path.append('=');
        path.append(getFromRequest(request,
                net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME));
        final String result = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), path.toString());
        return result.substring(mapping.getModuleConfig().getPrefix().length());
    }

    public ActionForward removeMobilityCoordinator(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();

        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");
        final Person person = getDomainObject(request, "personOid");
        mobilityGroup.removeMobilityCoordinatorService(person);

        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward addMobilityCoordinator(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.addMobilityCoordinator();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward manageCandidacies(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }

    public ActionForward editGrade(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacySubmission submission = getDomainObject(request, "candidacySubmissionOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");
        final String grade = (String) getFromRequest(request, "grade");
        submission.setGrade(mobilityGroup, new BigDecimal(grade));
        return null;
    }

    public ActionForward viewCandidate(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        final Person person = getDomainObject(request, "personOid");
        if (person != null) {
            outboundMobilityContextBean.setPerson(person);
        }
        final ExecutionYear executionYear = getDomainObject(request, "executionYearOid");
        if (executionYear != null) {
            outboundMobilityContextBean.setExecutionYear(executionYear);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("viewCandidate");
    }

    public ActionForward selectCandite(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        candidacy.select();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setPerson(candidacy.getOutboundMobilityCandidacySubmission().getRegistration().getPerson());
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("viewCandidate");
    }

    public ActionForward unselectCandite(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        candidacy.unselect();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setPerson(candidacy.getOutboundMobilityCandidacySubmission().getRegistration().getPerson());
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("viewCandidate");
    }

    public ActionForward downloadCandidatesInformation(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        final String filename =
                BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
                        "label.mobility.candidates.information.filename");

        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        response.setContentType("application/vnd.ms-excel");

        final ServletOutputStream outputStream = response.getOutputStream();
        final Spreadsheet spreadsheet = mobilityGroup.getCandidatesInformationSpreadSheet(period);
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.close();

        return null;
    }

    public ActionForward downloadSelectedCandidates(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");

        final String filename =
                BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
                        "label.mobility.outbound.period.export.selected.candiadates.filename");

        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        response.setContentType("application/vnd.ms-excel");

        final ServletOutputStream outputStream = response.getOutputStream();
        final Spreadsheet spreadsheet = period.getSelectedCandidateSpreadSheet(period);
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.close();

        return null;
    }

    public ActionForward uploadClassifications(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        try {
            outboundMobilityContextBean.uploadClassifications();
            addActionMessage("success", request, "message.outbound.upload.success");
        } catch (DomainException ex) {
            addActionMessage("error", request, ex.getMessage(), ex.getArgs());
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }

    public ActionForward selectCandidates(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        mobilityGroup.selectCandidates(period);

        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(period));
        outboundMobilityContextBean.setMobilityGroupsAsList(Collections.singletonList(mobilityGroup));

        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }

    public ActionForward concludeCandidateSelection(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        mobilityGroup.concludeCandidateSelection(period);

        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(period));
        outboundMobilityContextBean.setMobilityGroupsAsList(Collections.singletonList(mobilityGroup));

        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }

    public ActionForward revertConcludeCandidateSelection(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        mobilityGroup.revertConcludeCandidateSelection(period);

        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(period));
        outboundMobilityContextBean.setMobilityGroupsAsList(Collections.singletonList(mobilityGroup));

        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }
    
    public ActionForward concludeCandidateNotification(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        mobilityGroup.concludeCandidateNotification(period);

        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(period));
        outboundMobilityContextBean.setMobilityGroupsAsList(Collections.singletonList(mobilityGroup));

        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }

    public ActionForward sendEmailToCandidates(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        final String toGroupName =
                BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
                        "label.send.email.to.candidates.group.to.name", mobilityGroup.getDescription(),
                        period.getExecutionInterval().getName());
        final GroupUnion group = new GroupUnion(getCandidateGroups(mobilityGroup, period));

        final Recipient recipient = Recipient.newInstance(toGroupName, group);
        final EmailBean bean = new EmailBean();
        bean.setRecipients(Collections.singletonList(recipient));

        final Person person = AccessControl.getPerson();
        if (person != null) {
            final PersonSender sender = person.getSender();
            if (sender != null) {
                bean.setSender(sender);
            }
        }

        request.setAttribute("emailBean", bean);

        return mapping.findForward("sendEmail");
    }

    private Collection<IGroup> getCandidateGroups(final OutboundMobilityCandidacyContestGroup mobilityGroup, 
            final OutboundMobilityCandidacyPeriod period) {
        final Collection<IGroup> groups = new HashSet<IGroup>();
        for (final OutboundMobilityCandidacySubmission submission : period.getOutboundMobilityCandidacySubmissionSet()) {
            if (submission.hasContestInGroup(mobilityGroup)) {
                groups.add(new PersonGroup(submission.getRegistration().getPerson()));
            }
        }
        return groups;
    }

    public ActionForward deleteOption(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyPeriodConfirmationOption option = getDomainObject(request, "optionOid");

        if (option != null) {
            option.delete();
        }

        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(period));
        return prepare(mapping, request, outboundMobilityContextBean);
    }

}
