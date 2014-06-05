/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
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
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCandidaciesApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "outbound-mobility", titleKey = "label.mobility.outbound",
        accessGroup = "academic(MANAGE_MOBILITY_OUTBOUND)")
@Mapping(path = "/outboundMobilityCandidacy", module = "academicAdministration")
@Forwards({ @Forward(name = "prepare", path = "/mobility/outbound/OutboundMobilityCandidacy.jsp"),
        @Forward(name = "viewContest", path = "/mobility/outbound/viewContest.jsp"),
        @Forward(name = "manageCandidacies", path = "/mobility/outbound/manageCandidacies.jsp"),
        @Forward(name = "viewCandidate", path = "/mobility/outbound/viewCandidate.jsp"),
        @Forward(name = "sendEmail", path = "/messaging/emails.do?method=newEmail") })
public class OutboundMobilityCandidacyDA extends FenixDispatchAction {

    @EntryPoint
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

    public ActionForward deletePeriod(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacyPeriod candidacyPeriod = getDomainObject(request, "candidacyPeriodOid");
        candidacyPeriod.delete();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
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
            try {
                final OutboundMobilityCandidacyContestGroup mobilityGroup = contest.getOutboundMobilityCandidacyContestGroup();
                if (mobilityGroup.getOutboundMobilityCandidacyContestSet().size() == 1) {
                    outboundMobilityContextBean.getMobilityGroups().remove(mobilityGroup);
                }
                contest.delete();
            } catch (Exception e) {
                addErrorMessage(request, "errors", e.getMessage());
            }

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
        final String result =
                GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), path.toString(), request.getSession());
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

        //There's no mobility groups
        if (outboundMobilityContextBean.getMobilityGroups().size() == 0) {
            return prepare(mapping, request, outboundMobilityContextBean);
        }

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

    public ActionForward editVacancies(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        final String vacancies = (String) getFromRequest(request, "vacancies");
        contest.editVacancies(vacancies == null || vacancies.isEmpty() ? null : new Integer(vacancies));
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
                BundleUtil.getString(Bundle.ACADEMIC,
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
                BundleUtil.getString(Bundle.ACADEMIC,
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

    public ActionForward selectCandidatesForAllGroups(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");

        try {
            final String result = period.selectCandidatesForAllGroups();
            request.setAttribute("result", result);
        } catch (final DomainException ex) {
            final String error = ex.getKey();
            request.setAttribute("error", error);
        }

        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(period));

        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("prepare");
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

    public ActionForward revertConcludeCandidateNotification(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final OutboundMobilityCandidacyPeriod period = getDomainObject(request, "candidacyPeriodOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");

        mobilityGroup.revertConcludeCandidateNotification(period);

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
                BundleUtil.getString(Bundle.ACADEMIC,
                        "label.send.email.to.candidates.group.to.name", mobilityGroup.getDescription(), period
                                .getExecutionInterval().getName());
        final Group group = UnionGroup.of(getCandidateGroups(mobilityGroup, period));

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

    private Set<Group> getCandidateGroups(final OutboundMobilityCandidacyContestGroup mobilityGroup,
            final OutboundMobilityCandidacyPeriod period) {
        final Set<Group> groups = new HashSet<Group>();
        for (final OutboundMobilityCandidacySubmission submission : period.getOutboundMobilityCandidacySubmissionSet()) {
            if (submission.hasContestInGroup(mobilityGroup)) {
                groups.add(UserGroup.of(submission.getRegistration().getPerson().getUser()));
            }
        }
        return groups;
    }

    public ActionForward deleteOption(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
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
